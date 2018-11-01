import Config.Config;
import DataBase.SQLDataBase;
import Monitor.Manager;
import Supplier.AmazonSupplier;
import Supplier.SupplierInterface;

public class MonitorMainApp {


	public static void main(String[] args) 
	{
		Config.GetPlatform();
		Manager AmazonToeBayManager = new Manager();
		AmazonToeBayManager.AmazonToeBay();
	}
	
	
}
