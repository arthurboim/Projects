
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
		AmazonToeBayManager.MonitorScannerRun();
	}
	
	
}
