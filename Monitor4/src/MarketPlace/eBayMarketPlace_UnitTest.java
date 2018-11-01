package MarketPlace;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsByKeywordsRequest;
import com.ebay.services.finding.FindItemsByKeywordsResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.PaginationInput;
import com.ebay.services.finding.SortOrderType;

import Item.Item;
import junit.framework.Assert;

public class eBayMarketPlace_UnitTest {

	public void test_GetItemInformationTest() {
		eBayMarketPlace eBayCall = new eBayMarketPlace();
		Item item = new Item();
		item.setMarketPlaceCode("223076708812");
		eBayCall.GetItemInformation(item);
		assertTrue(item.getUniversalCode().equals("16963413344"));
	}

	public void test_GetItemPosition() {
		eBayMarketPlace eBayCall = new eBayMarketPlace();
		Item item = new Item();
		item.setMarketPlaceCode("223076708812");
		eBayCall.GetItemInformation(item);
		eBayCall.GetItemPosition(item);
		assertTrue(item.getMarketPlaceResults() == 24);
		assertTrue(item.getPlaceInLowestPrice() == 10);
		assertTrue(item.getMarketPlaceLowestPrice() == 26.95);
	}

	public void test_ChangePrice() {
		eBayMarketPlace eBayCall = new eBayMarketPlace();
		Item item = new Item();
		item.setQuantity(1);
		item.setUpdatedPrice(11.30);
		item.setMarketPlaceCode("223087004409");
		try {
			eBayCall.ChangePrice(item);
		} catch (Exception e) {
		}
	}

	public void test_DeleteItem() {
		eBayMarketPlace eBayCall = new eBayMarketPlace();
		Item item = new Item();
		item.setMarketPlaceCode("223072085030");
		try {
			assertTrue(eBayCall.DeleteItem(item));
		} catch (Exception e) {

		}
	}

	public void test_ChangeQuantity() {
		eBayMarketPlace eBayCall = new eBayMarketPlace();
		Item item = new Item();
		item.setMarketPlaceCode("223092580375");
		try {
			eBayCall.ChangeQuantity(item, 1);
		} catch (Exception e) {

		}
	}
	
	
	public void test_PlaceInSearchLowestFirst()
	{
		MarketPlaceInterface eBayCall = new eBayMarketPlace();
		Item item = new Item();
		item.setMarketPlaceCode("223075458994");
		eBayCall.PlaceInSearchLowestFirst(item);
		//eBayCall.GetItemInformation(item);
		//eBayCall.GetItemPosition(item);
		System.out.println(item.toString());
	}
	
	@Test
	public void test_FindItemByKeyWord()
	{
		try{
		ClientConfig config = new ClientConfig();
		config.setApplicationId("Arthurbo-Getprodu-PRD-c45f6444c-b98e8933");
		config.setEndPointAddress("https://svcs.ebay.com/services/search/FindingService/v1");
		FindingServicePortType serviceClient = null;
		serviceClient = FindingServiceClientFactory.getServiceClient(config);
		
		FindItemsByKeywordsRequest req = new FindItemsByKeywordsRequest();
		FindItemsByKeywordsResponse  res = new FindItemsByKeywordsResponse();
		req.setKeywords("nike");
		req.setSortOrder(SortOrderType.PRICE_PLUS_SHIPPING_LOWEST);
		PaginationInput input = new PaginationInput();
		input.setPageNumber(1);
		input.setEntriesPerPage(8);
		req.setPaginationInput(input);
		res = serviceClient.findItemsByKeywords(req);
		}catch(Exception e)
		{
			System.out.println();	
		}
		
	}
}
