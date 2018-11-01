package MarketPlace;

import java.util.ArrayList;

import Item.Item;

public interface MarketPlaceInterface 
{

	void GetItemsUpdate(ArrayList<Item> ListOfItems);
	
	void PriceChange(Item itemToChange);
	
	void RemoveList(Item itemToChange);

	void ChangeStock(Item itemToChange, int newQuantity);
	
	void PlaceInSearchLowestFirst(Item itemToCheck);
	
}
