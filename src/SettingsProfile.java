import java.awt.Color;
import java.awt.Font;

import java.io.*;

public class SettingsProfile implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public transient Tracker tracker;
	
	public boolean debugMode = false, printConsole = false, stayOnTop = false;
	public int pullSize = 20, searchSize = 5000, dataLimitSize = 128000;
	
	public int colorSchemaR = 69, colorSchemaG = 0, colorSchemaB = 0;
	
	public String font = "Courier New";
	public int fontSize = 12;
	
	public int graphNodeSpacing = 8, graphXPCeiling = 3000;
	boolean drawInactiveGraph = false;
	
	public String chatLogDirectory = "No directory selected..";
	
	public SettingsProfile(Tracker t)
	{
		tracker = t;
	}
	
	public void applySettingsProfile(SettingsProfile p)
	{
		debugMode = p.debugMode;
		printConsole = p.printConsole;
		stayOnTop = p.stayOnTop;
		
		pullSize = p.pullSize;
		searchSize = p.searchSize;
		dataLimitSize = p.dataLimitSize;
		
		colorSchemaR = p.colorSchemaR;
		colorSchemaG = p.colorSchemaG;
		colorSchemaB = p.colorSchemaB;
		
		font = p.font;
		fontSize = p.fontSize;
		
		graphNodeSpacing = p.graphNodeSpacing;
		graphXPCeiling = p.graphXPCeiling;
		drawInactiveGraph = p.drawInactiveGraph;
		
		chatLogDirectory = p.chatLogDirectory;
	}
}
