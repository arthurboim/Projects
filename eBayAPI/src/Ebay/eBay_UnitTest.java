package Ebay;

import static org.junit.Assert.*;

import org.junit.Test;

import Item.Item;

public class eBay_UnitTest
{
	
	@Test
	public void test()
	{

			eBayAPI 	eBayAPI;
			eBayAPI = new eBayAPI();
			Item item = new Item();
			item.setMarketPlaceCode("223179495935");
			item.setUniversalCode("8850999371566");
			eBayAPI.GetItemPosition(item);
			System.out.println(item.toString());
	}
	
}
