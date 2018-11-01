package Monitor;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import Item.Item;

public class ManagerTest {

	Manager manager;
	
	public ManagerTest() {
		manager = new Manager();
	}
	
	public void Test() {
		manager.AmazonToeBay();
	}
	

	public void test_InitDatabase()
	{
		manager.InitDatabase();
	}
	
	
	public void test_PriceChangingDecision()
	{
		ArrayList<Item> list = new ArrayList<Item>();
		Item item = new Item();
		item.setCurrentMarketPlacePrice(10);
		item.setPlaceInLowestPrice(15);
		item.setMarketPlaceLowestPrice(8);
		item.setCurrentSupplierPrice(5);
		item.setCurrentTax(0);
		item.setMarketPlaceResults(1);
		list.add(item);
		
		item = new Item();
		item.setCurrentMarketPlacePrice(10);
		item.setPlaceInLowestPrice(1);
		item.setMarketPlaceLowestPrice(10);
		item.setCurrentSupplierPrice(5);
		item.setCurrentTax(0);
		item.setMarketPlaceResults(10);
		item.setMarketPlaceSecondLowestPrice(12);
		list.add(item);

		manager.PriceChangingDecision(list);
		manager.printList(list);
	}
	
	@Test
	public void test_MakeChange()
	{
		ArrayList<Item> list = new ArrayList<Item>();
		Item item = new Item();
		item.setSupplierCode("B01G5AXR3K");
		item.setMarketPlaceCode("223179492175");
		list.add(item);
		manager.AmazonToeBay(list);
	}
}
