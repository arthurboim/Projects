package Ebay;



import java.io.IOException;

import java.util.ArrayList;


import org.junit.Test;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.call.GetOrdersCall;
import com.ebay.soap.eBLBaseComponents.OrderType;
import com.ebay.soap.eBLBaseComponents.PaginationType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;
import com.ebay.soap.eBLBaseComponents.TradingRoleCodeType;

import Main.DatabaseMain;
import Main.Store;

public class eBayCallTest {
	
	public static ArrayList<Store> ListOfStores = new ArrayList<Store>();
	public static ApiContext apiContext;
	public static  ApiCredential cred;
	public static  String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static  String eBay_token = null;
	public static  String Server_url = null;
	public static  String Application_id= null;
	public static  SiteCodeType SiteCode = null;
	public static  String Contry = null;
	public static  String Connection = null;
	public static  String scham = null;
	public static  String StoreName = null;
	public static  String MonitorUser = null;
	public static  String MonitorPassword = null;
	
 	public void OrdersInfo(Store store) {
 		MonitorUser = store.MonitorUser;
 		MonitorPassword = store.MonitorPassword;
 		StoreName = store.Store_name;
		Connection = store.Connection;
		scham = store.Schame;
		Connection =Connection+scham;
		eBay_token = store.token;
		Server_url = store.ServerUrl;
		Application_id = store.ApplicationId;
		Contry = store.Site;
		if (Contry.equals("US")) SiteCode =  SiteCodeType.US;
		if (Contry.equals("UK")) SiteCode =  SiteCodeType.UK; 
	}
 	
 	
	@Test
	public void test() throws ApiException, SdkException, Exception {
		DatabaseMain DB = new DatabaseMain();
 		DB.GetAllStores(ListOfStores);
 		OrdersInfo(ListOfStores.get(0));
        ApiContext apiContext = getApiContext();
        GetOrdersCall orders = new GetOrdersCall(apiContext);
        int NumOfDays = 3;
        int counter=1;
        orders.setIncludeFinalValueFee(true);
        orders.setNumberOfDays(NumOfDays);
        orders.setOrderRole(TradingRoleCodeType.SELLER);
        PaginationType page = new PaginationType();
        page.setEntriesPerPage(200);
        page.setPageNumber(counter);
        
        do
        {
        orders.setPagination(page);
        OrderType[] os = orders.getOrders(); 
        for(OrderType o:os)
        {
        	System.out.println(o.getTransactionArray().getTransaction(0).getFinalValueFee().getValue());
        }
        counter++;
        page.setPageNumber(counter);
        }while(orders.getReturnedHasMoreOrders());
	}
	
	private static ApiContext getApiContext() throws IOException {
		  String input;
	      apiContext = new ApiContext();
	      cred = apiContext.getApiCredential();	  
	      cred.seteBayToken(eBay_token);
	      input = Server_url;
	      apiContext.setApiServerUrl(input);
	      return apiContext;
	  }

}
