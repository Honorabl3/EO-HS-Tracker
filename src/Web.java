import java.io.IOException;
import java.util.List;

import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.util.ArrayList;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
//import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.Reader;
import java.io.InputStreamReader;

import java.io.IOException;

class PlayerData
{

    private String name;
    private int level, exp, rank;
    String movement;
    int positions, stars;
    
    public PlayerData(String cName, int cLevel, int xExp, int cRank, String cMovement, int cPos, int cStars)
    {
        name = cName;
        level = cLevel;
        exp = xExp;
        rank = cRank;
        movement = cMovement;
        positions = cPos;
        stars = cStars;
    }

    @Override
    public String toString() {
        return "PlayerData{" + "name=" + name + ", level=" 
                + level + ", exp=" + exp + '}';
    }
}

public class Web
{
    Tracker tracker;
    ArrayList<Entry> list = new ArrayList<Entry>();
    
    JsonArray jsonArray;
    String jsonString = "";
    
    public Web(Tracker t)
    {
        tracker = t;
    }
    
    public ImageIcon getItemImage(int id)
    {
        String imageUrl = "https://eor-api.exile-studios.com/api/items/" + id + "/graphic";
        
        try
        {
            // Create a connection to the image URL
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            // Check if the response code is HTTP OK (200)
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get input stream from the connection
                InputStream inputStream = connection.getInputStream();

                // Read the image
                Image image = ImageIO.read(inputStream);

                // Close the stream
                inputStream.close();

                // Return the ImageIcon if image is loaded
                if (image != null)
                {
                    return new ImageIcon(image);
                }
                
                else
                {
                    System.out.println("Failed to load image.");
                    return null;
                }
            }
            
            else
            {
                System.out.println("Failed to connect. Response code: " + connection.getResponseCode());
                return null;
            }
        }
        
        catch (Exception e)
        {
            System.out.println("Web.getMonsterImage() error - " + e);
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean loadItemData()
    {
    	//	Wipe all item data from monsterPanel
    	tracker.panel.monsterPanel.itemData.clear();
    	
    	try
    	{
    		String html = "https://eor-api.exile-studios.com/api/items/dump";
            
            Document doc = Jsoup.connect(html).maxBodySize(0).ignoreContentType(true).get();
            Elements tableElements = doc.select("body");
            
            String docJson = doc.text();
    
            // Parse the JSON string directly into a JsonArray
            JsonParser jsonParser = new JsonParser();
            JsonArray jsonArray = jsonParser.parse(docJson).getAsJsonArray();

            // Iterate through the array and access each element
            for (JsonElement element : jsonArray)
            {
            	//ArrayList<DropData> tempRecipe = new ArrayList<DropData>();
            	
            	/*JsonObject elementObject = element.getAsJsonObject();
            	if (elementObject.has("ingredientFor") && elementObject.get("ingredientFor").isJsonArray())
            	{
                    JsonArray dropArray = elementObject.getAsJsonArray("drops");
            	
	                for (JsonElement drop : dropArray)
	                {
	                	JsonObject dropObj = drop.getAsJsonObject();
	                	tempRecipe.add(new DropData(dropObj.get("itemID").getAsInt(), dropObj.get("drop_percent").getAsFloat()));
	                }
            	}*/
            	
            	ItemData itemEntry = new ItemData(element.getAsJsonObject().get("id").getAsInt(), element.getAsJsonObject().get("name").getAsString(), element.getAsJsonObject().get("item_unique").getAsInt(), element.getAsJsonObject().get("weight").getAsInt(), element.getAsJsonObject().get("sell_price").getAsInt(), element.getAsJsonObject().get("hit_rate").getAsInt(), element.getAsJsonObject().get("min_damage").getAsInt(), element.getAsJsonObject().get("max_damage").getAsInt());
            	tracker.panel.monsterPanel.itemData.put(itemEntry.name, itemEntry);
            }
            
            System.out.println("loadItemData - [Loaded " + tracker.panel.monsterPanel.itemData.size() + " entries]!");
		}
		    	
    	catch(Exception e)
    	{
    		System.out.println("Web.loadItemData() error - " + e);
    		e.printStackTrace();
    		
    		return false;
    	}
    	
    	return true;
    }
    
    public ImageIcon getMonsterImage(int id)
    {
        String imageUrl = "https://eor-api.exile-studios.com/api/npcs/" + id + "/graphic";
        
        try
        {
            // Create a connection to the image URL
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            // Check if the response code is HTTP OK (200)
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get input stream from the connection
                InputStream inputStream = connection.getInputStream();

                // Read the image
                Image image = ImageIO.read(inputStream);

                // Close the stream
                inputStream.close();

                // Return the ImageIcon if image is loaded
                if (image != null)
                {
                    return new ImageIcon(image);
                }
                
                else
                {
                    System.out.println("Failed to load image.");
                    return null;
                }
            }
            
            else
            {
                System.out.println("Failed to connect. Response code: " + connection.getResponseCode());
                return null;
            }
        }
        
        catch (Exception e)
        {
            System.out.println("Web.getMonsterImage() error - " + e);
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean loadMonsterData()
    {
    	//	Wipe all monster data from monsterPanel
    	tracker.panel.monsterPanel.monsterData.clear();
    	
    	try
    	{
    		String html = "https://eor-api.exile-studios.com/api/npcs/dump";
            
            Document doc = Jsoup.connect(html).maxBodySize(0).ignoreContentType(true).get();
            Elements tableElements = doc.select("body");
            
            String docJson = doc.text();
    
            // Parse the JSON string directly into a JsonArray
            JsonParser jsonParser = new JsonParser();
            JsonArray jsonArray = jsonParser.parse(docJson).getAsJsonArray();

            // Iterate through the array and access each element
            for (JsonElement element : jsonArray)
            {
            	ArrayList<DropData> tempDrop = new ArrayList<DropData>();
            	
            	JsonObject elementObject = element.getAsJsonObject();
            	if (elementObject.has("drops") && elementObject.get("drops").isJsonArray())
            	{
                    JsonArray dropArray = elementObject.getAsJsonArray("drops");
            	
	                for (JsonElement drop : dropArray)
	                {
	                	JsonObject dropObj = drop.getAsJsonObject();
	                	tempDrop.add(new DropData(dropObj.get("itemID").getAsInt(), dropObj.get("drop_percent").getAsFloat()));
	                }
            	}
            	
            	MonsterData monsterEntry = new MonsterData(element.getAsJsonObject().get("id").getAsInt(), element.getAsJsonObject().get("name").getAsString(), element.getAsJsonObject().get("experience").getAsInt(), element.getAsJsonObject().get("npc_respawn_secs").getAsInt(), element.getAsJsonObject().get("boss").getAsInt(), element.getAsJsonObject().get("child").getAsInt(), element.getAsJsonObject().get("level").getAsInt(), element.getAsJsonObject().get("behavior").getAsInt(), element.getAsJsonObject().get("min_damage").getAsInt(), element.getAsJsonObject().get("max_damage").getAsInt(), element.getAsJsonObject().get("hp").getAsInt(), element.getAsJsonObject().get("accuracy").getAsInt(), element.getAsJsonObject().get("evasion").getAsInt(), element.getAsJsonObject().get("armor").getAsInt(), element.getAsJsonObject().get("critical_chance").getAsInt(), tempDrop);
                tracker.panel.monsterPanel.monsterData.put(monsterEntry.name, monsterEntry);
            }
            
            System.out.println("loadMonsterData - [Loaded " + tracker.panel.monsterPanel.monsterData.size() + " entries]!");
    	}
    	
    	catch(Exception e)
    	{
    		System.out.println("Web.loadMonsterData() error - " + e);
    		e.printStackTrace();
    		
    		return false;
    	}
    	
    	
    	return true;
    }
    
    public void recompile()
    {
        list.clear();
        
        // Limit to the first X entries
        int limit = Math.min(tracker.settings.pullSize, jsonArray.size());
        
        // Iterate through the first X amount of entries
        for (int i = 0; i < limit; i++)
        {
            JsonElement jsonElement = jsonArray.get(i);
            // Convert the JsonElement to your custom object if necessary
            // For example, assuming each entry represents a Person object
            // Person person = gson.fromJson(jsonElement, Person.class);
            // Then you can work with the person object accordingly
            //System.out.println("Entry " + (i + 1) + ": " + jsonElement);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            
            Entry tempEntry = new Entry();
            tempEntry.setEntry(jsonObject.get("name").getAsString(), jsonObject.get("rank").getAsInt(), jsonObject.get("level").getAsInt(), jsonObject.get("exp").getAsInt());
            list.add(tempEntry);
        }
        
        int searchSize = tracker.settings.searchSize;
        for(int a=0; a<tracker.specificList.size();a++)
        {
            boolean found = false;
            for (int i = 0; i < searchSize; i++)
            {
                JsonElement jsonElement = jsonArray.get(i);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                
                if(jsonObject.get("name").getAsString().equalsIgnoreCase(tracker.specificList.get(a)))
                {
                    Entry tempEntry = new Entry();
                    tempEntry.setEntry(jsonObject.get("name").getAsString(), jsonObject.get("rank").getAsInt(), jsonObject.get("level").getAsInt(), jsonObject.get("exp").getAsInt());
                    list.add(tempEntry);
                    System.out.println("Added " +  tempEntry.name);
                    found = true;
                    break;
                }
            }
            
            if(!found)
            {
                
            }
        }
    }
    
    //@Test
    public void retrieve() throws IOException
    {
        try
        {
            list = new ArrayList<Entry>();
            
            //String html = "https://eodash.com/leaderboard";
            //String html = "https://game.endless-online.com/top100.html";
            String html = "https://www.eodash.com/api/players";
            
            //InputStream input = new URL(html).openStream();
            //Reader reader = new InputStreamReader(input, "UTF-8");
            //Data data = new Gson().fromJson(reader, Data.class);
            Document doc = Jsoup.connect(html).maxBodySize(0).ignoreContentType(true).get();
            Elements tableElements = doc.select("body");
            
            String docJson = doc.text();
            System.out.println("Pulled data size: " + docJson.length() + " !!!");
            
            int charSize = tracker.settings.dataLimitSize;
            if(docJson.length() < charSize)
            	charSize = docJson.length()-1;
            
            jsonString = docJson.substring(0, charSize);
            jsonString = jsonString.substring(0, jsonString.lastIndexOf("}") + 1) + " ] }";
            
            //System.out.println("printOUTTTTT:" + jsonString);
            
            
            //String jsonString = doc.text() + "}, ...]\"},";
    
            // Using Gson to parse JSON
            //Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();
            
            // Parse the JSON string to a JsonArray
            jsonArray = (JsonArray) jsonParser.parse(jsonString).getAsJsonObject().get("players");
            
            // Limit to the first X amount of entries
            int limit = Math.min(tracker.settings.pullSize, jsonArray.size());
            
            // Iterate through the first X amount of entries
            for (int i = 0; i < limit; i++)
            {
                JsonElement jsonElement = jsonArray.get(i);
                // Convert the JsonElement to your custom object if necessary
                // For example, assuming each entry represents a Person object
                // Person person = gson.fromJson(jsonElement, Person.class);
                // Then you can work with the person object accordingly
                //System.out.println("Entry " + (i + 1) + ": " + jsonElement);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                
                Entry tempEntry = new Entry();
                tempEntry.setEntry(jsonObject.get("name").getAsString(), jsonObject.get("rank").getAsInt(), jsonObject.get("level").getAsInt(), jsonObject.get("exp").getAsInt());
                list.add(tempEntry);
            }
            
            int searchSize = tracker.settings.searchSize;
            for(int a=0; a<tracker.specificList.size();a++)
            {
                boolean found = false;
                for (int i = 0; i < searchSize; i++)
                {
                    JsonElement jsonElement = jsonArray.get(i);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    
                    if(jsonObject.get("name").getAsString().equalsIgnoreCase(tracker.specificList.get(a)))
                    {
                        Entry tempEntry = new Entry();
                        tempEntry.setEntry(jsonObject.get("name").getAsString(), jsonObject.get("rank").getAsInt(), jsonObject.get("level").getAsInt(), jsonObject.get("exp").getAsInt());
                        list.add(tempEntry);
                        found = true;
                        break;
                    }
                }
                
                if(!found)
                {
                    
                }
            }
            
            // 0 RANK
            // 1 NAME
            // 2 LEVEL
            // 3 EXPERIENCE
            // 4 LAST SEEN
        }
        
        catch(Exception e)
        {
            System.out.println("Web.retrieve() error - " + e);
            e.printStackTrace();
        }
    }
}