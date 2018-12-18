package MarketPlace;

import java.util.List;

import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;

import DataBase.SQLDataBase;
import Ebay.eBayAPI;
import Item.Item;

public class eBayMarketPlace implements MarketPlaceInterface
{
	/* Variables */
	
	private eBayAPI 	eBayAPI;
	private SQLDataBase SQLDb;
	/* Constructor */
	
	public eBayMarketPlace()
	{
		eBayAPI = new eBayAPI();
	}
	
	/* Interface implementation */
	
	@Override
	public void GetItemsUpdate(List<Item> ListOfItems)
	{
		eBayAPI.GetItemsUpdate(ListOfItems);
	}
	
	@Override
	public void PriceChange(Item itemToChange) throws ApiException, SdkException, Exception
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
	public void PlaceInSearchLowestFirst(List<Item> ListOfItems)
	{
		//eBayAPI.GetPositionInLowestPriceSearch(itemToCheck);
		for(Item ele:ListOfItems)
		{
			eBayAPI.GetPositionInLowestPriceSearch(ele);
			SQLDb.SetUniversalCode(ele);
			SQLDb.SetBestResults(ele);
			SQLDb.SetMarketPlaceResults(ele);
			SQLDb.SetPlaceInLowestPrice(ele);
		}
	}
	
	/* Private functions */

	
}
