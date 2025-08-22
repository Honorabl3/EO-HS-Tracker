import java.util.ArrayList;

import javax.swing.ImageIcon;

public class MonsterData
{
	public int id, exp, respawn, level, behavior, min_damage, max_damage, health, accuracy, evasion, armor, critical_chance;
	public String name;
	public boolean boss, child;
	
	public ArrayList<DropData> drops;
	
	ImageIcon image = null, smallImage = null;
	
	public MonsterData(int i, String n, int xp, int r, int b, int chld, int lvl, int behave, int minDam, int maxDam, int npcHP, int acc, int eva, int arm, int crit, ArrayList<DropData> dropD)
	{
		id = i;
		name = n;
		exp = xp;
		respawn = r;
		boss = false;
		if(b > 0)
			boss = true;
		child = false;
		if(chld > 0)
			child = true;
		level = lvl;
		behavior = behave;
		min_damage = minDam;
		max_damage = maxDam;
		health = npcHP;
		accuracy = acc;
		evasion = eva;
		armor = arm;
		critical_chance = crit;
		
		drops = dropD;
		
		image = null;
		smallImage = null;
	}
	
	public void setImage(ImageIcon i)
	{
		image = i;
	}
	
	public void setSmallImage(ImageIcon i)
	{
		smallImage = i;
	}
}
