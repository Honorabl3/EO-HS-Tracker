import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;

public class ChatLogReader
{
	public Tracker tracker;
	
	public String lastProcessedLine, lastLastProcessedLine;
	
	public float frequency = 1; // seconds/frequency per tick of reading chatlog 
	
	//	Seek size
	public int numLines = 10;
	
	public ChatLogReader(Tracker t)
	{
		tracker = t;
		//filePath = directory;
		
		//filePath = "C:/Users/Honor/Desktop/Endless Online/CHATLOG.txt";
	}
	
	public void processFile() throws IOException {
        try {
            if (tracker.panel.monsterPanel.active.isSelected()) {
                Paths.get(tracker.settings.chatLogDirectory);

                LinkedList<String> lines = readLastLines(tracker.settings.chatLogDirectory, numLines);
                boolean lastProcessedFound = false;
                int lastIndex = -1;

                // Locate the last processed line in the log
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).equals(lastProcessedLine)) {
                        lastProcessedFound = true;
                        lastIndex = i;
                    }
                }

                // Process all lines AFTER the last known processed line
                for (int i = lastIndex + 1; i < lines.size(); i++) {
                    String line = lines.get(i);

                    // Ensure duplicates in the same batch are processed
                    if (line.equals(lastProcessedLine) && line.equals(lastLastProcessedLine)) {
                        continue; // Avoid redundant processing across multiple reads
                    }

                    processLine(line);

                    // Shift tracking variables
                    lastLastProcessedLine = lastProcessedLine;
                    lastProcessedLine = line;
                }

                // If last processed line was not found, process all lines
                if (!lastProcessedFound) {
                    for (String line : lines) {
                        processLine(line);

                        // Shift tracking variables
                        lastLastProcessedLine = lastProcessedLine;
                        lastProcessedLine = line;
                    }
                }
            }
        } catch (InvalidPathException e) {
            System.out.println("The [Monster Log] file path syntax is invalid: " + e.getMessage());
            tracker.panel.monsterPanel.active.setSelected(false);
        }
    }

    public LinkedList<String> readLastLines(String filePath, int numLines) throws IOException
    {
        LinkedList<String> lines = new LinkedList<>();
        
        try
        {
        	RandomAccessFile file = new RandomAccessFile(filePath, "r");
        	
            long fileLength = file.length();
            long pointer = fileLength - 1;
            file.seek(pointer);

            StringBuilder line = new StringBuilder();
            int linesRead = 0;

            while (pointer >= 0)
            {
                file.seek(pointer);
                char c = (char) file.readByte();
                if (c == '\n')
                {
                    if (line.length() > 0)
                    {
                        lines.addFirst(line.toString());
                        line.setLength(0);
                        linesRead++;
                        if (linesRead >= numLines)
                        {
                            break;
                        }
                    }
                }
                
                else
                {
                    line.insert(0, c);
                }
                pointer--;
            }

            // Add the last line if it's not empty
            if (line.length() > 0 && linesRead < numLines)
            {
                lines.addFirst(line.toString());
            }
            
            file.close();
        }
        
        catch(Exception e)
		{
        	System.out.println("[Error] ChatLogReader.readLastLines() - " + e);
        	e.printStackTrace();
		}

        return lines;
    }

    public void processLine(String input)
    {
        //System.out.println("Processing line: " + input);
        
        try
		{
        	//	On 7/3/2025 (July 3rd) the game had an update which switched the way
        	//	system messages read from chatlog are structured.. bits any pieces of
        	//	the new system are written in, weaved to work with some of the old system.
        	//	if this breaks good luck fixing it LOL
        	//
        	//	5/21/2025 11:09:17 PM -s > Gained +240 exp from White Imp
        	//	5/21/2025 11:09:23 PM -s > Gained +240 exp from White Imp
        	//	5/11/2025 8:37:07 PM -s > Gained +233 group exp from Ant Soldier

        	//	NEW
        	//	6/29/2025 4:17:54 PM -s > Received 540 exp from Pupil Optica 2x boosted
        	//	6/29/2025 4:11:55 AM -s > Received 690 group exp from Bird Soldier 3x boosted




        	//	OLD
        	//	1/21/2025 10:01:21 PM -s > You succesfully gathered: Puffyweed +30 exp.
//
        	//	NEW
        	//	6/29/2025 4:33:12 PM -s > Received 30 exp for collecting 1 Puffyweed.
        	//	6/30/2025 4:58:27 AM -s > Received 80 exp for collecting 1 Wood Log. 2x boosted
        	//	6/30/2025 4:58:33 AM -s > Received 60 exp for collecting 1 Puffyweed. 2x boosted
        	//	6/30/2025 5:20:27 AM -s > Received 7 exp from Crow 3x boosted

        	//
	        // Step 1: Split the input string by spaces and special characters
        	String[] parts = input.trim().split(" ");

	        // Extract the date
	        String date = parts[0]; // "9/6/2024"

	        // Extract the time and AM/PM
	        String time = parts[1] + " " + parts[2]; // "10:19:42 AM"

	        // Extract the "-s"
	        String flag = parts[3]; // "-s"
	        
	        if(flag.equals("-s"))	//	If the message is a sys log message.
	        {
	        	// Extract the "Gained" as the action indicator from parts[5]
		        String action = parts[5]; // "Gained"

		        if(action.equals("Gained") || action.equals("Received"))
		        {
		        	//	CHECK IF GAINING CORRUPT CORE PIECE... IF SO STOP PROCESSING
		        	if(parts[6].equalsIgnoreCase("Corrupt"))
		        	{
		        		return;
		        	}
		        	
		        	
		        	// Extract the exp points
		        	int expGained = 0;
		        	if(action.equals("Gained"))
		        		expGained = Integer.parseInt(parts[6].substring(1)); // "+600" -> 600
		        	else
		        		expGained = Integer.parseInt(parts[6]);
		        	
			        
			        //	If the player is in a party, then the string will change..
			        int inPartyOffset = 0;
			        if(parts[7].equalsIgnoreCase("group"))
			        	inPartyOffset = 1;
			        
			        // Extract the word "from"
			        String from = parts[8+inPartyOffset]; // "from"

			        // Combine all the strings after parts[9] (including parts[9])
			        int offset = 9+inPartyOffset;
			        int nameEnd = 11;
			        StringBuilder targetBuilder = new StringBuilder();
			        for (int i = 9+inPartyOffset; i < parts.length; i++)
			        {
			        	offset++;
			        	
			            if (i > 9+inPartyOffset)
			            {
			                targetBuilder.append(" "); // Add space between words
			            }
			            
			            String currentPart = parts[i];
			            if(currentPart.length() <= 1 && Character.isDigit(currentPart.charAt(0)))
			            {
			            	//System.out.println(Arrays.toString(parts));
			            	//System.out.println("Detecting " + currentPart.toString());
			            	targetBuilder.setLength(0);
			            	continue;
			            }
			            
			            // Check if the current part contains a comma at the end
			            if (currentPart.endsWith(",") || currentPart.endsWith("."))
			            {
			                // Remove the comma
			                currentPart = currentPart.substring(0, currentPart.length() - 1);
			                //System.out.println("CURRPART[" + currentPart + "]");
			                targetBuilder.append(currentPart);
			                nameEnd = i;
			                break;  // Stop concatenating after finding the comma
			            }
			            
			            if(currentPart.endsWith("x"))
			            {
			            	//System.out.println("Checking digit:" + Character.isDigit(currentPart.charAt(currentPart.length() - 2)) + " with string \"" + currentPart.charAt(currentPart.length() - 2) + "\"");
			            	if (currentPart.length() >= 2 && Character.isDigit(currentPart.charAt(currentPart.length() - 2)))
			            	{
			            		//System.out.println("HIT HIT HIT");
			                    break; // Disqualify if there's a digit before the 'x'
			                }
			            }
			            
			            targetBuilder.append(currentPart);
			        }
			        
		            String target = targetBuilder.toString(); // Combine into one string
		            target = target.trim();
		            
		            
		            //	CODE HERE COMMENTED OUT - on NPC drop then add the entry to item list.. 
		            /*if(parts.length > offset)
		            {
			            String afterComma = parts[offset];	//	hopefully get the word "Dropped"
			            //System.out.println("after comma word:" + afterComma + " !!!");
			            
			            if(afterComma.equalsIgnoreCase("dropped"))
			            {
			            	int quan = Integer.parseInt(parts[offset + 1]);
			            	
			            	StringBuilder targetBuilder2 = new StringBuilder();
			            	for (int i = offset + 2; i < parts.length; i++)
					        {
			            		if (i > offset + 2)
					            {
					                targetBuilder2.append(" "); // Add space between words
					            }
			            		
			            		String currentPart = parts[i];
					            targetBuilder2.append(currentPart);
					        }
	
				            String itemName = targetBuilder2.toString(); // Combine into one string
				            //System.out.println("Detected item drop of:[" + itemName + "] x" + quan + "!");
			            	
			            	tracker.panel.monsterPanel.addItem(itemName, quan, 0);
			            }
		            }*/
		            
		            //System.out.println("target:" + target + " and expGained " + expGained);
			        
		            boolean inParty = false;
		            
		            if(inPartyOffset >= 1)
		            	inParty = true;
		            
		            //System.out.println("Monster Logging: " + target);
		            
		            if(from.equalsIgnoreCase("for"))	//	If 'for' it means the player is harvesting plant or collecting material.. 6/30/25
		            {
		            	//target = parts[11] + "" ;
		            	//System.out.println("target[" + target + "] expGained[" + expGained + "]");
		            	tracker.panel.monsterPanel.addItem(target, 1, expGained);
		            }
		            
		            else
		            	tracker.panel.monsterPanel.addKill(target, 1, expGained, inParty);
		        }
		        
		        else if(action.equalsIgnoreCase("You"))
		        {
		        	if(parts[6].equalsIgnoreCase("succesfully") && parts[7].equalsIgnoreCase("gathered:"))
		        	{
		        		int offset = 8;
		        		StringBuilder harvestBuilder = new StringBuilder();
				        for (int i = 8; i < parts.length; i++)
				        {
				        	offset++;
				        	
				            // Check if the current part contains a comma at the end
				            String currentPart = parts[i];
				            
				            // Check if the next word starts with a "+"
				            if (i + 1 < parts.length && parts[i + 1].startsWith("+"))
				            {
				            	if(i > 8)
				            		harvestBuilder.append(" "); // Add space between words
				                harvestBuilder.append(currentPart); // Append current word
				                break; // Stop concatenating and exit the loop
				            }

				            harvestBuilder.append(currentPart);
				        }
				        
			            String resourceName = harvestBuilder.toString(); // Combine into one string
			            
			            int expGained = Integer.parseInt(parts[offset].substring(1)); // "+40" -> 40
			            
			            tracker.panel.monsterPanel.addItem(resourceName, 1, expGained);
			            
			            //System.out.println("EFFICACY: [" + target + "][+" + expGained + "]");
		        	}
		        	
		        	else if(parts[6].equalsIgnoreCase("picked") && parts[7].equalsIgnoreCase("up"))
		        	{
		        		int quantity = Integer.parseInt(parts[8]);
		        		
		        		StringBuilder pickupBuilder = new StringBuilder();
				        for (int i = 9; i < parts.length; i++)
				        {
				        	String currentPart = parts[i];
				            if (i > 9)
				            {
				            	pickupBuilder.append(" "); // Add space between words
				            }

				            pickupBuilder.append(currentPart);
				        }
				        
				        String resourceName = pickupBuilder.toString(); // Combine into one string
				        tracker.panel.monsterPanel.addItem(resourceName, quantity, 0);
		        	}
		        }
	        }
		}
		
		catch(Exception e)
		{
			System.out.println("[Error] ChatLogReader.processLine() - " + e);
			System.out.println("Error parsing input string: " + input);
			e.printStackTrace();
		}
    }
}
