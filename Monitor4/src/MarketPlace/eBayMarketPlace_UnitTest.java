package MarketPlace;

import static org.junit.Assert.*;

import org.junit.Test;

import Item.Item;
import junit.framework.Assert;

public class eBayMarketPlace_UnitTest {

	
	public void GetItemInformationTest() 
	{
		eBayMarketPlace eBayCall = new eBayMarketPlace();
		Item item = new Item();
		item.setMarketPlaceCode("223076708812");
		eBayCall.GetItemInformation(item);
		assertTrue(item.getUniversalCode().equals("16963413344"));
	}

	@Test
	public void GetItemPosition() 
	{
		eBayMarketPlace eBayCall = new eBayMarketPlace();
		Item item = new Item();
		item.setMarketPlaceCode("223076708812");
		eBayCall.GetItemInformation(item);
		eBayCall.GetItemPosition(item);
		assertTrue(item.getMarketPlaceResults() == 24);
		assertTrue(item.getPlaceInLowestPrice() == 10);
		assertTrue(item.getMarketPlaceLowestPrice() == 26.95);
	}
	
}
