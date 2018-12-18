package MarketPlace;


import java.util.List;


import Item.Item;

public interface MarketPlaceInterface 
{

	void GetItemsUpdate(List<Item> ListOfItems);
	
	void PriceChange(Item itemToChange);
	
	void RemoveList(Item itemToChange);

	void ChangeStock(Item itemToChange, int newQuantity);
	
	//void PlaceInSearchLowestFirst(Item itemToCheck);
	 void PlaceInSearchLowestFirst(List<Item> ListOfItems);
}
