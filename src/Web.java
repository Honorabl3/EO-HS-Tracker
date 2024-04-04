import java.io.IOException;
import java.util.List;
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
    
    public void recompile()
    {
        list.clear();
        
        // Limit to the first 1000 entries
        int limit = Math.min(tracker.settings.pullSize, jsonArray.size());
        
        // Iterate through the first 1000 entries
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
            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();
            
            // Parse the JSON string to a JsonArray
            jsonArray = (JsonArray) jsonParser.parse(jsonString).getAsJsonObject().get("players");
            
            // Limit to the first 1000 entries
            int limit = Math.min(tracker.settings.pullSize, jsonArray.size());
            
            // Iterate through the first 1000 entries
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
        }
    }
}