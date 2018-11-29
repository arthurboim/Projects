package MarketPlace;

import java.util.ArrayList;
import Ebay.eBayAPI;
import Item.Item;

public class eBayMarketPlace implements MarketPlaceInterface
{
	/* Variables */
	
	private eBayAPI eBayAPI;
	
	/* Constructor */
	
	public eBayMarketPlace()
	{
		eBayAPI = new eBayAPI();
	}
	
	/* Interface implementation */
	
	@Override
	public void GetItemsUpdate(ArrayList<Item> ListOfItems)
	{
		eBayAPI.GetItemsUpdate(ListOfItems);
	}
	
	@Override
	public void PriceChange(Item itemToChange)
	{
		eBayAPI.ChangePrice(itemToChange);
	}
	
	@Override
	public void RemoveList(Item itemToChange)
	{
		eBayAPI.DeleteItem(itemToChange);
	}
	
	@Override
	public void ChangeStock(Item itemToChange, int newQuantity)
	{
		eBayAPI.ChangeQuantity(itemToChange, newQuantity);
	}
	
	@Override
	public void PlaceInSearchLowestFirst(Item itemToCheck)
	{
		eBayAPI.GetPositionInLowestPriceSearch(itemToCheck);
	}
	
	/* Private functions */

	
}
