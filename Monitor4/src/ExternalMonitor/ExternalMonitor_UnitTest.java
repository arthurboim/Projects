package ExternalMonitor;

import org.junit.Test;

public class ExternalMonitor_UnitTest {

	@Test	
	public void GetItemsList_test()
	{
		ExternalMonitorInterface monitor = new ShopMaster();
		monitor.GetItemsList();
	}
	

}
