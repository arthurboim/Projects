
import Config.Config;
import Monitor.MonitorManager;


public class MonitorMainRun {


	public static void main(String[] args) 
	{
		// Application Initialization
		Config AppConfigurations = new Config();
		MonitorManager AmazonToeBayManager = new MonitorManager();
		AppConfigurations.Initialization();
		
		// Application start
		while(true)
		{
			AmazonToeBayManager.MonitorScannerRun();
			try
			{
				Thread.sleep(1000*60*60);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
