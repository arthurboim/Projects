package CashBack;


import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;



public class TopCashBackUnitTest {

	@Test
	public void test() throws InterruptedException 
	{
		TopCashBack TopCashBackTest = new TopCashBack();
		ChromeDriver Driver = TopCashBackTest.MainTopCashBack("B07DFXJ8LK");
		for(String handle : Driver.getWindowHandles()) 
		{
		 Driver.switchTo().window(handle);
		 Driver.close();
		}
		assert(Driver != null);
	}

}
