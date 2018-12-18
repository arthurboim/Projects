package MarketPlace;


import java.util.List;

import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;

import Item.Item;

public interface MarketPlaceInterface 
{

	void GetItemsUpdate(List<Item> ListOfItems);
	
	void PriceChange(Item itemToChange) throws ApiException, SdkException, Exception;
	
	void RemoveList(Item itemToChange);

	void ChangeStock(Item itemToChange, int newQuantity);
	
	//void PlaceInSearchLowestFirst(Item itemToCheck);
	 void PlaceInSearchLowestFirst(List<Item> ListOfItems);
}
