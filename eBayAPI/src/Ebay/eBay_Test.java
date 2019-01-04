package Ebay;

import org.junit.Test;

import Item.Item;

public class eBay_Test {

	
	public void Test_changeprice()
	{
		eBayAPI ebay =new eBayAPI();
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
	//223159223928
	@Test
	public void Test_GetPositionInLowestPriceSearch()
	{
		eBayAPI ebay =new eBayAPI();
		Item newItem= new Item();
		newItem.setMarketPlaceCode("223159223928");
		ebay.GetPositionInLowestPriceSearch(newItem);
		System.out.println(newItem.getMarketPlaceResults());
		System.out.println(newItem.getPlaceInLowestPrice());
	}
	
}
