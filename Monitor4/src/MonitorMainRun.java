
import Config.Config;
import Monitor.Manager;


public class MonitorMainRun {


	public static void main(String[] args) 
	{
		// Application Initialization
		Config AppConfigurations = new Config();
		Manager AmazonToeBayManager = new Manager();
		AppConfigurations.Initialization();
		
		// Application start
		AmazonToeBayManager.MonitorScannerRun();
	}
	
	
}