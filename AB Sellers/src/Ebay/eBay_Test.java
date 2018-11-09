package Ebay;

import java.util.ArrayList;
import org.junit.Test;
import com.ebay.soap.eBLBaseComponents.ItemType;
import Item.Item;


public class eBay_Test {



	public void test_GetSellerItems() {
		ArrayList<ItemType> sellerItemsList = new ArrayList<ItemType>();
		eBayCalls ebay_caller = new eBayCalls();
		ebay_caller.GetSellerItems("ubuythebestforu", sellerItemsList);
		
		for(ItemType ele:sellerItemsList)
		{
			System.out.println(ele.getItemID());
		}
	}

	
	public void test_GetItemCall()
	{
		Item item = new Item();
		eBayCalls ebay_caller = new eBayCalls();
		item.setMarketPlaceCode("223076704397");
		ebay_caller.GetItemUniversalCode(item);
		System.out.println(item.getCodeType());
		System.out.println(item.getUniversalCode());
	}

	@Test
	public void test_GetAndUpdateSearchResults()
	{
		int counter = 0;
		Item item = new Item();
		eBayCalls ebay_caller = new eBayCalls();
		item.setCodeType("UPC");
		item.setUniversalCode("712182988204");
		item.setMarketPlaceCode("223076704397");
		while(true)
		{
			System.out.println("Counter: "+counter++);
		}
	}

	
	public void test_GetItemTrasactions()
	{
		eBayCalls ebay_caller = new eBayCalls();
		System.out.println(ebay_caller.GetItemTrasactions("223179492175", 7));
	}
	
	
	public void test_RunProductFinder()
	{
		eBayCalls ebay_caller = new eBayCalls();
		ebay_caller.run();
	}
}
