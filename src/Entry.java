import java.io.Serializable;
import java.util.ArrayList;

import java.time.Instant;

public class Entry implements Serializable
{
    public String name;
    public int rank, level, exp, expChange = 0;
    public Instant timeRecorded;
    
    public transient ArrayList<Double> graphPoint;
    
    public Entry()
    {
    	graphPoint = new ArrayList<Double>();
    }
    
    public boolean setEntry(String s, int r, int le, int xp)
    {
        try
        {
            name = s;
            rank = r;
            level = le;
            exp = xp;
            
            timeRecorded = Instant.now();
            
            return true;
        }
        
        catch(Exception e)
        {
            System.out.println("Entry.setEntry() error - " + e);
            return false;
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
    
    public String toString()
    {
        int numberOfSpaces = 40;
        int preSize = (rank + " " + name).length();
        return appendSpaces("#" + rank, 5) + " " + appendSpaces(name, 14) + " " + appendSpaces("" + level, 10) + appendSpaces("" + exp, 10);
    }
}
