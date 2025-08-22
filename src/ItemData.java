import java.util.ArrayList;

import javax.swing.ImageIcon;

public class ItemData
{
	public int id, unique_id, weight, sell_price, hit_rate, min_damage, max_damage;
	String name;
	
	public ArrayList<CraftData> crafts;
	//public ArrayList<DropData> droppedBy;
	
	ImageIcon image = null, smallImage = null;
	
	public ItemData(int i, String n, int uid, int w, int sp, int hr, int mindam, int maxdam)
	{
		id = i;
		name = n;
		unique_id = uid;
		weight = w;
		sell_price = sp;
		hit_rate = hr;
		min_damage = mindam;
		max_damage = maxdam;
		
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
