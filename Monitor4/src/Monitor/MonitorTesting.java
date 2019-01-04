package Monitor;

import java.util.ArrayList;

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
		newItem.setMarketPlaceCode("223159222703");
		ArrayList<Item> list = new ArrayList<Item>();
		list.add(newItem);
		ebay.GetItemsUpdate(list);
		System.out.println("Market place code = "+list.get(0).getMarketPlaceCode());
		System.out.println("Current market place price = "+list.get(0).getCurrentMarketPlacePrice());
		System.out.println("New price = "+list.get(0).getUpdatedPrice());
		System.out.println("***************************");
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
