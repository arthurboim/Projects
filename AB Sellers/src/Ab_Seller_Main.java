import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;

import Amazon.AmazonCalls;
import Ebay.eBayCalls;
import Gui.AB_Sellers_Gui;

public class Ab_Seller_Main {

	public static String PathToFile = null;
	public static void main(String[] args) throws ApiException, SdkException, Exception 
	{
		AB_Sellers_Gui Gui = new AB_Sellers_Gui();

		
		for(int i=0 ; i<18 ;i++)
		{
			eBayCalls eBay = new eBayCalls();
			Thread thread= new Thread(eBay);
			thread.start();
		}
		
		AmazonCalls Amazon = new AmazonCalls();
		Thread threadAmazon= new Thread(Amazon);
		threadAmazon.start();

	}

}




