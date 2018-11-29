package Monitor;

import java.util.ArrayList;
import Item.Item;
import Monitor.MonitorManager;

public class ManagerTest {

	MonitorManager monitorManager;
	
	public ManagerTest() {
		monitorManager = new MonitorManager();
	}
	
	public void Test() {
		monitorManager.AmazonToeBayScanner();
	}
	
	public void test_InitDatabase()
	{
		monitorManager.UpdateDatabase();
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

		monitorManager.PriceChangingDecision(list);
		monitorManager.printList(list);
	}
	

}
