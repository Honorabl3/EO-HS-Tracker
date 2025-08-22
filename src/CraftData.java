import java.util.ArrayList;

public class CraftData
{
	public int id, price;
	public String shopName;
	
	ArrayList<CraftIngredientData> storeList;
	
	public CraftData(int i, String storeName, int cost, ArrayList<CraftIngredientData> shopList)
	{
		id = i;
		shopName = storeName;
		price = cost;
		
		storeList = shopList;
	}
}
