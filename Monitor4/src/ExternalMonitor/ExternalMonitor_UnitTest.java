package ExternalMonitor;

import org.junit.Test;

public class ExternalMonitor_UnitTest {

		
	public void GetItemsList_test()
	{
		ExternalMonitorInterface monitor = new ShopMaster();
		monitor.GetItemsList();
	}
	
	@Test
	public void test()
	{
		ExternalMonitorInterface monitor = new Yaballe();
		monitor.GetItemsList();
	}
	

}
