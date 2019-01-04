package Item;

import org.junit.Test;

public class Item_UnitTest {

	@Test
	public void test_SetMinPriceToSale() {
		Item item = new Item();
		item.setMarketPlaceLowestPrice(10);
		item.setCurrentSupplierPrice(7);
		item.setCurrentTax(0.7);
		item.CalculateMinPriceToSale();
		System.out.println(item.toString());
	}
	
	
	public void test_CalculateNewProfitPersentFromSpecificPrice() {
		Item item = new Item();
		item.setMarketPlaceLowestPrice(10);
		item.setCurrentSupplierPrice(7);
		item.setCurrentTax(1.2);
		item.CalculateNewProfitPersentFromSpecificPrice(10);
		System.out.println(item.toString());
	}
	
	
	public void test_CalculateCurrentProfitPersent() {
		Item item = new Item();
		item.setMarketPlaceLowestPrice(10);
		item.setCurrentSupplierPrice(7);
		item.setCurrentTax(0);
		item.setCurrentMarketPlacePrice(7.5);
		item.CalculateCurrentProfitPersent();
		System.out.println(item.toString());
	}

	
	public void test_CalculateNewMarketPlacePrice() {
		Item item = new Item();
		item.setMarketPlaceLowestPrice(10);
		item.setCurrentSupplierPrice(7);
		item.setCurrentTax(1.2);
		item.setCurrentMarketPlacePrice(12);
		item.CalculateNewProfitPersentFromSpecificPrice(item.getMarketPlaceLowestPrice());
		item.CalculateNewMarketPlacePrice(item.getMarketPlaceLowestPrice());
		System.out.println(item.toString());
	}
}
