import java.io.*;
import java.util.ArrayList;

//gfx
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

//time keeping
import java.time.Duration;
import java.time.Instant;

public class Tracker
{
    boolean active = false;
    
    SettingsProfile settings = new SettingsProfile(this);
    Web web;
    GfxWindow panel;
    
    // record keeping
    ArrayList<ArrayList<Entry>> previous = new ArrayList<ArrayList<Entry>>();
    int dumpSize = 40;
    
    
    //time keeping
    Instant lastUpdate = Instant.now();
    int updateTimer = 150000; // milliseconds.... 300000 is 5 minutes.
    
    
    //frames for UI update
    int framerate = 5000; // milliseconds
    
    // graphics related vars
    //ArrayList<JLabel> stringList;
    
    // specific user tracking
    ArrayList<String> specificList;
    
    
    public Tracker()
    {
    	specificList = new ArrayList<String>();
    	web = new Web(this);
    	
    	loadData();
    }
    
    public void run()
    {	
        try
        {
            SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Endless Online - Highscore Tracker");
            panel = new GfxWindow(this);
            frame.add(panel);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            panel.start(); // Start the game loop
            
            settings.pullSize = Integer.parseInt(panel.settingsDataPullSize.getText());	//	Maybe comment out this line once verify settings profile save feature works correctly.
            });
        }
        
        catch(Exception e)
        {
            System.out.println("Tracker.run() [graphics] error - " + e);
        }
        
        try
        {
            active = true;
            pullData(); // pull data from site
            
            Thread.sleep(5000);
            
            
            while(active)
            {
                if(Instant.now().getEpochSecond() > lastUpdate.plusMillis(updateTimer).getEpochSecond())
                    pullData();
                
                //long nextUpdate = lastUpdate.plusMillis(updateTimer).getEpochSecond()-Instant.now().getEpochSecond();
                
                updateFrame();
                Thread.sleep(framerate);
            }
            
            //printData();
        }
        
        catch(Exception e)
        {
            System.out.println("Tracker.run() [console] error - " + e);
        }
    }
    
    public String appendSpaces(String input, int desiredLength)
    {
        int spacesToAdd = Math.max(0, desiredLength - input.length());
        StringBuilder sb = new StringBuilder(input);
        
        for (int i = 0; i < spacesToAdd; i++)
        {
            sb.append(" ");
        }
        
        return sb.toString();
    }
    
    public Entry findName(ArrayList<Entry> checkList, String checkName)
    {
        for(int a=0;a<checkList.size();a++)
        {
            if(checkList.get(a).name.equalsIgnoreCase(checkName))
            {
                return checkList.get(a);
            }
        }
        
        return null;
    }
    
    public int getEXPChange(Entry ee)
    {
        int xp = 0;
        
        if(previous.size() > 0) //  check if previous list exists
        {
            Entry prevEntry = findName(previous.get(0), ee.name);
            
            if(prevEntry != null)
                xp = ee.exp - prevEntry.exp;
        }
        
        return xp;
    }
    
    public float predictXPHR(Entry ee)
    {
        try
        {
            float result = 0;
            
            if(previous.size() > 0) //  check if previous list exists
            {
            	int flagCount = 0;
            	int flagLimit = 2;
            	
                ArrayList lastArray = null, safeArray = null;
                Entry currEntry = null, safeEntry = findName(web.list, ee.name);
                for(int a=0;a<previous.size();a++)
                {
                	for(int b=0;b<previous.get(a).size(); b++)
                	{
	                	currEntry = findName(previous.get(a), ee.name);
	                	
	                	if(currEntry != null)
	                	{
	                		if(currEntry.expChange > 1)
	                		{
	                			safeEntry = currEntry;
	                    		safeArray = previous.get(a);
	                    		lastArray = safeArray;
	                		}
	                		
	                		else
	                			flagCount++;
	                	}
	                	
	                	else if(flagCount >= flagLimit)
	                	{
	                		break;
	                	}
	                	
	                	else
	                		flagCount++;
                	}
                	
                	if(flagCount >= flagLimit)
                		break;
                }
                
                if(safeEntry == null)
                    return -0;
                
                // timespan measure data (in seconds) set from newest data, to oldest data..
                float timespan = (float) ((ee.timeRecorded.getEpochSecond() - safeEntry.timeRecorded.getEpochSecond()));
                
                if(timespan >= 3600)
                    result = (float) ((ee.exp-safeEntry.exp) / ((timespan/60)/60));
                else
                    result = (float) (-1 * ((ee.exp-safeEntry.exp) * (3600/timespan))); // make negative number so printData knows its estimation
            }
            
            return Float.isNaN(result) ? 0 : result;
        }
        
        catch(Exception e)
        {
            System.out.println("Tracker.predictXPHR() error - " + e);
            return 0;
        }
    }
    
    public void pullData()
    {
        try
        {
            previous.add(0, web.list);
            System.out.println("previous list size: " + web.list.size());
            
            if(previous.size() > dumpSize)
            {
                previous.remove(previous.size()-1); // remove entry at end of array
            }
                    
            web.retrieve();
            saveData();
            
            System.out.println("web list size: " + web.list.size());
            
            int highCount = settings.graphXPCeiling; // Ceiling on graph height 
            
            //calculate exp change from previous list
            for(int a=0;a<previous.get(0).size();a++)
            {
                Entry prevData = previous.get(0).get(a);
                Entry newData = findName(web.list, prevData.name);
                
                newData.expChange = newData.exp - prevData.exp;
                
                if(newData.expChange >= 1)
                {
                	int tempEXPDiff = newData.expChange;	//	Naming convention tempEXPDifference a.k.a expChange
                	
                	if(tempEXPDiff > highCount)
                	{
                		tempEXPDiff = highCount;
                	}
                	
                	//if(prevData.graphPoint != null)
                	newData.graphPoint = new ArrayList<Double>(prevData.graphPoint);
                    newData.graphPoint.add(0, (double) tempEXPDiff / highCount);
                    
                	if(newData.graphPoint.size() > dumpSize)
                		newData.graphPoint.remove(newData.graphPoint.size() - 1);
                }
            }
            
            lastUpdate = Instant.now();
        }
        
        catch(Exception e)
        {
            System.out.println("Web.pullData() error - " + e);
            e.printStackTrace();
        }
    }
    
    public void printData()
    {
        try
        {
            panel.xpList.removeAll();
            //panel.stringList = new ArrayList<JLabel>();
            
            System.out.println(appendSpaces("Rank", 6) + appendSpaces("Name", 15) + appendSpaces("Level", 16) + appendSpaces("EXP", 22) + appendSpaces("CHANGE", 15) + "XP/HR   ");
            JLabel tHeader = new JLabel(appendSpaces("Rank", 6) + appendSpaces("Name", 15) + appendSpaces("Level", 10) + appendSpaces("EXP", 17) + appendSpaces("CHANGE", 16) + "XP/HR      ");
            tHeader.setForeground(Color.WHITE);
            tHeader.setBackground(panel.colorSchemaColor);
            tHeader.setOpaque(true);
            tHeader.setFont(panel.lineFont);
            tHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.xpList.add(tHeader);
            
            for(int a=0;a<web.list.size();a++)
            {
            	if(previous.size() > 0)
            	{
	                if(findName(previous.get(0), web.list.get(a).name) != null)
	                {
	                	Entry tempEntry = web.list.get(a);
	                	
	                    float xpPrediction = predictXPHR(web.list.get(a));
	                    String xpMod = " ";
	                    if(xpPrediction < 0)
	                    {
	                        xpPrediction = Math.abs(xpPrediction);
	                        xpMod = "*";
	                    }
	                    
	                    String expChange = "";
	                    if(web.list.get(a) != null)
	                        expChange = getEXPChange(web.list.get(a)) + "";
	                    else
	                        expChange = "Error1";
	                    
	                    String tempString = web.list.get(a) + "       +" + String.format("%-15s" + String.format("%.1f", xpPrediction/1000) + "K" + xpMod, expChange); // Add Math.abs(value) on getEXPChange
	                    
	                    //if(tempString
	                    JLabel tempLabel = new JLabel(tempString);
	                    //tempLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, panel.colorSchemaColor)); // Top border
	                    tempLabel.setFont(panel.lineFont);
	                    //tempLabel.setBounds(0, a*16, 600, 32);
	                    //tempLabel.setSize(tempLabel.getPreferredSize());
	                    //tempLabel.setLocation(8, a*16);
	                    tempLabel.setForeground(Color.WHITE);
	                    
	                    
	                    JPanel tempPanel = new JPanel();
	                    tempPanel.setBackground(Color.BLACK);
	                    tempPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, panel.colorSchemaColor));
	                    tempPanel.setPreferredSize(new Dimension(tempLabel.getX()+16, tempLabel.getY()));   //  +16 is size of red x icon image
	                    tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.X_AXIS));
	                    tempPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
	                    tempPanel.setVisible(true);
	                    
	                    tempPanel.add(tempLabel);
	                    
	                    if(web.list.get(a).rank > settings.pullSize)
	                    {
	                        JLabel tempLabel2 = new JLabel(panel.redXIcon);
	                        
	                        final int itNumButton = a;
	                        
	                        // Add MouseListener to the label
	                        tempLabel2.addMouseListener(new MouseAdapter()
	                        {
	                            @Override
	                            public void mouseClicked(MouseEvent e)
	                            {
	                                for(int i=0;i<specificList.size();i++)
	                                {
	                                    if(specificList.get(i).equalsIgnoreCase(web.list.get(itNumButton).name))
	                                    {
	                                        specificList.remove(i);
	                                        
	                                        web.recompile();
	                                        printData();
	                                    }
	                                }
	                            }
	                        });
	                        
	                        //tempLabel2.setContentAreaFilled(false);
	                        tempLabel2.setIconTextGap(0);
	                        tempLabel2.setBackground(Color.BLACK);
	                        //tempButton.setPreferredSize(new Dimension(panel.redXIcon.getIconWidth(), panel.redXIcon.getIconHeight()));
	                        //tempButton.setBounds(0, 0, panel.redXIcon.getIconWidth(), panel.redXIcon.getIconHeight());
	                        tempLabel2.setMaximumSize(new Dimension(14, 14));
	                        tempPanel.add(tempLabel2);
	                    }
	                    
	                    //JPanel test = new JPanel();
                    	//test.setBackground(Color.decode("#555555"));
                    	//test.setPreferredSize(new Dimension(100, 14));
                    	//test.setSize(new Dimension(100, 14));
                    	//test.setMinimumSize(new Dimension(100, 14));
                    	//test.setMaximumSize(tempLabel.getSize());
                    	//tempPanel.add(test);z
                    	
                    	//System.out.println("testpanel size: " + test.getSize() + " tempPanel: " + tempPanel.getSize());
	                    
	                    if(tempEntry.graphPoint != null && tempEntry.graphPoint.size() > 0)
	                    {
	                    	//System.out.println("Attempting to draw graph for " + tempEntry.name + " [" + tempEntry.graphPoint.size() + " points of graph data]=[" + tempEntry.graphPoint + "]");
		                    GraphPanel graph = new GraphPanel(tempEntry.graphPoint.stream().mapToDouble(Double::doubleValue).toArray());
		                    graph.setBackground(Color.BLACK);
		                    graph.setPreferredSize(new Dimension(90, 14));
		                    graph.setMaximumSize(new Dimension(graph.data.length*settings.graphNodeSpacing, settings.fontSize));
		                    //graph.setSize(new Dimension(tempPanel.getSize().width, tempPanel.getSize().height));
		                    //graph.setMaximumSize(tempPanel.getSize());
		                    graph.setVisible(true);
		                    
		                    //tempPanel.setPreferredSize(new Dimension(tempPanel.getPreferredSize().width + graph.getPreferredSize().width, tempPanel.getSize().height));
		                    tempPanel.setPreferredSize(new Dimension(tempLabel.getX()+16+graph.getX(), tempLabel.getY()));
		                    tempPanel.add(graph);
	                    }
	                    
	                    panel.xpList.add(tempPanel);
	                    panel.xpList.setPreferredSize(panel.xpList.getPreferredSize());
	                    
	                    
	                    if(settings.debugMode)
	                    {
	                    	String debugTempString = "[" + tempEntry.name + "] Graph [" + tempEntry.graphPoint.size() + " data nodes]";
		                    JLabel debugTempLabel = new JLabel(debugTempString);
		                    debugTempLabel.setFont(panel.lineFont);
		                    debugTempLabel.setForeground(Color.WHITE);
		                    
		                    JPanel debugTempPanel = new JPanel();
		                    debugTempPanel.setBackground(Color.BLACK);
		                    debugTempPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.RED));
		                    debugTempPanel.setPreferredSize(debugTempPanel.getPreferredSize());
		                    debugTempPanel.setLayout(new BoxLayout(debugTempPanel, BoxLayout.X_AXIS));
		                    debugTempPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		                    debugTempPanel.setVisible(true);
		                    
		                    debugTempPanel.add(debugTempLabel);
		                    
		                    panel.xpList.add(debugTempPanel);
		                    panel.xpList.setPreferredSize(panel.xpList.getPreferredSize());
	                    }
	                    
	                    System.out.println(tempString);
	                }
	                
	                else
	                {
	                    String expChange = "";
	                    if(web.list.get(a) != null)
	                        expChange = getEXPChange(web.list.get(a)) + "";
	                    else
	                        expChange = "Error2";
	                   
	                    String tempString = web.list.get(a) + "       +" + expChange;
	                    
	                    JLabel tempLabel = new JLabel(tempString);
	                    //tempLabel.setBounds(0, a*16, 600, 32);
	                    tempLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, panel.colorSchemaColor)); // Top border
	                    tempLabel.setFont(panel.lineFont);
	                    //tempLabel.setSize(tempLabel.getPreferredSize());
	                    //tempLabel.setLocation(8, a*16);
	                    tempLabel.setForeground(Color.WHITE);
	                    //tempLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	                    //panel.xpList.add(tempLabel);
	                    
	                    JPanel tempPanel = new JPanel();
	                    tempPanel.setBackground(Color.BLACK);
	                    tempPanel.setPreferredSize(new Dimension(tempLabel.getX()+16, tempLabel.getY()));   //  +16 is size of red x icon image
	                    tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.X_AXIS));
	                    tempPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
	                    tempPanel.setVisible(false);
	                    
	                    tempPanel.add(tempLabel);
	                    
	                    if(web.list.get(a).rank > settings.pullSize)
	                    {
	                        JLabel tempLabel2 = new JLabel(panel.redXIcon);
	                        
	                        final int itNumButton = a;
	                        
	                        // Add MouseListener to the label
	                        tempLabel2.addMouseListener(new MouseAdapter()
	                        {
	                            @Override
	                            public void mouseClicked(MouseEvent e)
	                            {
	                                for(int i=0;i<specificList.size();i++)
	                                {
	                                    if(specificList.get(i).equalsIgnoreCase(web.list.get(itNumButton).name))
	                                    {
	                                        specificList.remove(i);
	                                        
	                                        web.recompile();
	                                        printData();
	                                    }
	                                }
	                            }
	                        });
	                        
	                        //tempLabel2.setContentAreaFilled(false);
	                        tempLabel2.setIconTextGap(0);
	                        tempLabel2.setBackground(Color.BLACK);
	                        //tempButton.setPreferredSize(new Dimension(panel.redXIcon.getIconWidth(), panel.redXIcon.getIconHeight()));
	                        //tempButton.setBounds(0, 0, panel.redXIcon.getIconWidth(), panel.redXIcon.getIconHeight());
	                        tempLabel2.setMaximumSize(new Dimension(14, 14));
	                        tempPanel.add(tempLabel2);
	                        
	                        tempPanel.setPreferredSize(tempPanel.getPreferredSize());
	                    }
	                    
	                    panel.xpList.add(tempPanel);
	                    
	                    System.out.println(tempString);
	                }
            	}
            }
        }
        
        catch(Exception e)
        {
            e.printStackTrace(); // Print the stack trace for debugging
            System.out.println("Tracker.printData() error - " + e);
        }
    }
    
    public void saveData()
    {
    	try
    	{
    		String fileName= "settings.ser";
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            oos.writeObject(settings);
            
            oos.close();
    	}
    	
    	catch(Exception e)
    	{
    		System.out.println("Tracker.saveData() settings error - " + e);
    	}
    	
        try
        {
            String fileName= "lastSession.ser";
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            oos.writeObject(web.list);
            
            oos.close();
            
            saveArrayList(web.list);
        }
        
        catch(Exception e)
        {
            System.out.println("Tracker.saveData() lastSession error - " + e);
        }
    }
    
    public void saveArrayList(ArrayList<Entry> targetList)
    {
        try
        {
        	// Create the players directory if it doesn't exist
            File playersDir = new File("players");
            if (!playersDir.exists()) {
                playersDir.mkdir();
            }
            
            for(int a=0;a<targetList.size();a++)
            {
                String fileName= "players/" + targetList.get(a).name + ".ser";
                FileOutputStream fos = new FileOutputStream(fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                
                oos.writeObject(targetList.get(a));
                
                oos.close();
            }
        }
        
        catch(Exception e)
        {
            System.out.println("Tracker.saveArrayList() error - " + e);
        }
    }
    
    public void loadData()
    {
    	try
    	{
    		String fileName= "settings.ser";
    		
    		File file = new File(fileName);
            if (file.exists())
            {
	            FileInputStream fin = new FileInputStream(fileName);
	            ObjectInputStream ois = new ObjectInputStream(fin);
	            
	            SettingsProfile tempProfile = (SettingsProfile) ois.readObject();
	            settings.applySettingsProfile(tempProfile);
	        
	            ois.close();
            }
            
            else
            	settings = new SettingsProfile(this);
    	}
    	
    	catch(Exception e)
    	{
    		System.out.println("Tracker.loadData() settings error - " + e);
            e.printStackTrace();
    	}
    	
        try
        {
            String fileName= "lastSession.ser";
            
            File file = new File(fileName);
            if (file.exists())
            {
	            FileInputStream fin = new FileInputStream(fileName);
	            ObjectInputStream ois = new ObjectInputStream(fin);
	            
	            ArrayList<Entry> tempList = (ArrayList<Entry>) ois.readObject();
	            
	            //previous.add(0, tempList);
	            //System.out.println("----------PREVIOUS SESSION-----------");
	            //for(int a=0;a<previous.get(0).size();a++)
	            //{
	            //    System.out.println("" + previous.get(0).get(a));
	            //}
	            //System.out.println("-------------------------------------");
	            
	            web.list = tempList;
	            System.out.println("----------PREVIOUS SESSION-----------");
	            for(int a=0;a<web.list.size();a++)
	            {
	                System.out.println("" + web.list.get(a));
	            }
	            System.out.println("-------------------------------------");
	        
	            ois.close();
            }
        }
        
        catch(Exception e)
        {
            System.out.println("Tracker.loadData() lastSession error - " + e);
            e.printStackTrace();
        }
    }
    
    public void updateFrame()
    {
        try
        {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            printData();
            
            long lastUpdateInSeconds = ((Instant.now().getEpochSecond()-lastUpdate.getEpochSecond()));
            long nextUpdate = lastUpdate.plusMillis(updateTimer).getEpochSecond()-Instant.now().getEpochSecond();
            
            System.out.println("Last update: " + lastUpdateInSeconds + " seconds ago.. Next update in " + nextUpdate + " seconds.");
            panel.updateTimer.setText("        Refresh in " + nextUpdate + " seconds" + appendSpaces("", 8));
        }
        
        catch(Exception e)
        {
            System.out.println("Tracker.updateFrame error - " + e);
        }
    }
}
