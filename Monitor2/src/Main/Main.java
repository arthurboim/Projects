package Main;

import java.util.ArrayList;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import AmazonOrders.Tracking_number_amazon;
import Ebay.OrdersInfo;
import Gui.OrdersGui;


public class Main {

	public static ArrayList<Store> ListOfStores = new ArrayList<Store>();
	
	public static void main(String[] args) throws ApiException, SdkException, Exception {

		DatabaseMain DB = new DatabaseMain();
		DB.GetAllStores(ListOfStores);
	    OrdersGui Gui = new OrdersGui();
        mainrun();

}
	
	public synchronized static void mainrun() throws ApiException, SdkException, Exception
	{
		int counter = 0;
		Tracking_number_amazon Tracking_number_amazon = new Tracking_number_amazon();
		while(true)
		{
			for(Store ele:ListOfStores)
			{
			OrdersInfo OrdersInfo = new OrdersInfo(ele);
			OrdersInfo.Get_Orders_Info();
			OrdersInfo = null;
			System.gc();
			}
			System.out.println("Counter now = "+counter);
			if (counter==10)
			{
				Tracking_number_amazon.Get_tracking_from_amazon1();
				counter = 0;
			}
			Thread.sleep(1000*60*10);
			counter++;
		}

	}

	public static Store GetStoreByName(String StoreName)
	{
		for(Store ele:ListOfStores)
		{
			if (ele.Store_name.equals(StoreName)) return ele;
		}
		return null;
	}

}
