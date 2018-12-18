package Monitor;

import org.junit.Test;

import Ebay.*;
import Item.Item;
import Item.Item.*;


public class MonitorTesting
{
	
	
	public void Test_UpdateChanges()
	{
		MonitorManager Manager = new MonitorManager();
		Item newItem= new Item();
		newItem.setMarketPlaceCode("223184142309");
		newItem.setUpdatedPrice(117.38);
		newItem.setPriceStatus(PriceChangeStatus.PriceIncrease);
		Manager.UpdateChanges(newItem);
		
	}
	
	@Test
	public void Test_ebayChangeprice1()
	{
		eBayAPI ebay = new eBayAPI();

		Item newItem= new Item();
		newItem.setMarketPlaceCode("223159966105");
		newItem.setUpdatedPrice(113.04);
		ebay.ChangePrice(newItem);
		System.out.println("$$$");
		newItem.setUpdatedPrice(113.05);
		ebay.ChangePrice(newItem);
		System.out.println("$$$");
		newItem.setUpdatedPrice(113.04);
		ebay.ChangePrice(newItem);
	}
		
	
	
	
	public void Test_ebayChangeprice()
	{
		eBayAPI eBay = new eBayAPI();
		Item newItem= new Item();
		newItem.setMarketPlaceCode("223184142309");
		newItem.setUpdatedPrice(117.38);
		newItem.setPriceStatus(PriceChangeStatus.PriceIncrease);
		//eBay.UpdateChanges(newItem);
		try
		{
			eBay.ChangePrice(newItem);
			newItem.setUpdatedPrice(117.38);
			System.out.println("!!!");
			eBay.ChangePrice(newItem);
			System.out.println("!!!");
			newItem.setUpdatedPrice(117.37);
			eBay.ChangePrice(newItem);
			newItem.setUpdatedPrice(117.38);
			//eBay.ChangePrice(newItem);

		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		} 
	}
	
}
