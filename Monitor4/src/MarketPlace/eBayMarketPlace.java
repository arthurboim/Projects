package MarketPlace;


import java.util.ArrayList;
import Ebay.eBayCalls;
import Item.Item;

public class eBayMarketPlace implements MarketPlaceInterface {

	/* Variables */

	private eBayCalls					eBayCalls;
	
	/* Constructor */

	public eBayMarketPlace()
	{
		eBayCalls = new eBayCalls();
	}

	/* Interface implementation */

	@Override
	public void GetItemsUpdate(ArrayList<Item> ListOfItems)
	{
		eBayCalls.GetItemsUpdate(ListOfItems);
	}

	@Override
	public void PriceChange(Item itemToChange)
	{
		eBayCalls.ChangePrice(itemToChange);
	}

	@Override
	public void RemoveList(Item itemToChange)
	{
		eBayCalls.DeleteItem(itemToChange);
	}

	@Override
	public void ChangeStock(Item itemToChange, int newQuantity)
	{
		eBayCalls.ChangeQuantity(itemToChange, newQuantity);
	}

	@Override
	public void PlaceInSearchLowestFirst(Item itemToCheck)
	{
		eBayCalls.GetPositionInLowestPriceSearch(itemToCheck);
	}

	/* Private functions */

	
	
	/* Getter and Setters */

}
