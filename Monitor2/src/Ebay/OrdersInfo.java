package Ebay;


import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.call.GetOrdersCall;
import com.ebay.soap.eBLBaseComponents.OrderType;
import com.ebay.soap.eBLBaseComponents.PaginationType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;
import com.ebay.soap.eBLBaseComponents.TradingRoleCodeType;


import AmazonOrders.*;
import Main.DatabaseMain;
import Main.Email;
import Main.Order;
import Main.Store;

public class OrdersInfo extends Thread{

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
	
 	public OrdersInfo(Store store) {
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
		if (Contry.equals("US")) 
		{
			SiteCode =  SiteCodeType.US;
		}
		
		if (Contry.equals("UK")) 
		{
			SiteCode =  SiteCodeType.UK; 
		}
	}

	public synchronized void Get_Orders_Info() throws ApiException, SdkException, Exception
	{
        ApiContext apiContext = getApiContext();
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd 'at' hh:mm:ss");
        Date now = new Date();

        GetOrdersCall orders = new GetOrdersCall(apiContext);
        Email email = new Email();
        int NumOfDays = 30;
        
        orders.setNumberOfDays(NumOfDays);
        orders.setOrderRole(TradingRoleCodeType.SELLER);
        
        int counter=1;
        PaginationType page = new PaginationType();
        page.setEntriesPerPage(200);
        page.setPageNumber(counter);
        
        do
        {
        orders.setPagination(page);
        OrderType[] os = orders.getOrders(); //here we get everything//
        
        System.out.println("Number of order in "+NumOfDays+" days is "+os.length+" page number "+(counter));
        Calendar cal = Calendar.getInstance();
        for(OrderType o:os)
        {
        	//System.out.println("Length ==> "+o.getTransactionArray().getTransactionLength());
        	if (o.getTransactionArray().getTransactionLength()>1) 
        	{
        		System.out.println("Length ==> "+o.getTransactionArray().getTransactionLength());
        		try{
        		email.Send_report_mail(null, null, null, null);
        		}catch(Exception e){}
        	}

            String Asin_for_order = null;
            String Database_set_order_message = null;
            String Order_on_amazon_message = null;
            DatabaseMain Db = new DatabaseMain();
            AmazonOrder amazon = new AmazonOrder();
            Order_report Report = new Order_report();
            
            try{cal = o.getShippedTime();
            if (cal == null)  // checking the order //
            {	
            try{
            	if (Db.Check_status_in_database(o.getOrderID(),StoreName).equals("No_order")&&o.getCheckoutStatus().getStatus().value().contains("Complete") )
            		{
            		System.out.println("Order no "+o.getOrderID()+ " is starting the order process on amazon.com");
            		Asin_for_order = Db.GetAsinFromOnlineProducts(o.getOrderID()); //Getting product Asin from online table// 
            		Database_set_order_message = Db.Set_new_order(StoreName,o,"Ordering",Asin_for_order);
            		if (Database_set_order_message.equals("Success"))
            		amazon.Order_on_amazon(o,Asin_for_order,Report,MonitorUser,MonitorPassword); // here is the order in amazon //
            		else Report.Message = "Error";
            		if (!Asin_for_order.equals(null)&&Report.Message.equals("Success")&&Database_set_order_message.equals("Success"))
            		{
            		Db.UpdateOrderStatus(o,"Orderd");
            		email.Send_report_mail(Asin_for_order,Report,Database_set_order_message,"Item Ordered successfully");
            		System.out.println("Order no "+o.getOrderID()+ " is ended successfully");
            		}else 
            		{
                		email.Send_report_mail(Asin_for_order,Report,Database_set_order_message,"Item Order Error");
            		}
            		}
            		}catch(Exception e)
            		{   
            			System.out.println(e);
            			Db.UpdateOrderStatus(o,"Order_Error");
            			System.out.println("******************************************************************");
            			System.out.println("Order no "+o.getOrderID()+ " fail");
            			System.out.println("Error in order "+o.getOrderID());
            			System.out.println("Asin_for_order returnd = "+Asin_for_order);
            			System.out.println("Order_on_amazon_message returnd = "+Order_on_amazon_message);
            			System.out.println("Database_set_order_message returnd = "+Database_set_order_message);
            			System.out.println("******************************************************************");
                		email.Send_report_mail(Asin_for_order,Report,Database_set_order_message,"Item Ordered Fail");
            		}
             Asin_for_order = null;
             Database_set_order_message = null;
             Order_on_amazon_message = null;
             Db = null;
             amazon = null;
             Report = null;
             System.gc();
            } // end of if cal //


        	}//try
            catch (Exception e){System.out.println("Error while geting the order from ebay");}

        	}//for
        counter++;
        page.setPageNumber(counter);
        }while(orders.getReturnedHasMoreOrders());
        
        	
        	System.out.println("Scan orders at "+dateFormat2.format(now)+" Completed");
	}

    public void Set_order(Order order , String Order_Status,OrderType o, Calendar cal)
    {
        order.setSale_date(cal.getTime().toString());
    	order.setOrderStatus(Order_Status);
    	order.setOrderId(o.getOrderID());
        String EbayId = o.getOrderID();
        EbayId = EbayId.substring(0, EbayId.indexOf("-"));
        order.setEbayId(EbayId);
        order.setBuyer_User_ID(o.getBuyerUserID());
    	order.setBuyer_Full_Name(o.getShippingAddress().getName());
        order.setStreet(o.getShippingAddress().getStreet1());
        if (!o.getShippingAddress().getStreet2().equals(""))
        order.setStreet2(o.getShippingAddress().getStreet2());
        order.setCity(o.getShippingAddress().getCityName()); 
        order.setState_Province(o.getShippingAddress().getStateOrProvince());
        order.setZip_Postal_Code(o.getShippingAddress().getPostalCode());
        order.setPhone_Number(o.getShippingAddress().getPhone());
        order.setTotal_price(o.getAmountPaid().getValue());
        order.setCheckoutStatus(o.getCheckoutStatus().getStatus().value());
        
    }

	public void print_order(Order order)
	{
        System.out.println();
        
        System.out.println(order.getOrderId());
        System.out.println(order.getBuyer_User_ID());
        System.out.println(order.getBuyer_Full_Name());
        System.out.println(order.getStreet());
        System.out.println(order.getStreet2()); 
        System.out.println(order.getPhone_Number());
        System.out.println(order.getTotal_price());
    	System.out.println(order.getCheckoutStatus());
    	System.out.println(order.getZip_Postal_Code());
    	System.out.println(order.getState_Province()); 
    	System.out.println(order.getCity());

        System.out.println();
        
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

	@Override
	

	public void run() 
	{
		try {
			DatabaseMain Db = new DatabaseMain();
			try 
			{
				System.out.println("Retrying failed");
				System.out.println("Deleting order number = "+Db.SelecteOrderingOrOrderErrorAndReturnLast());
				Db.DeleteOrderById(Db.SelecteOrderingOrOrderErrorAndReturnLast());
			} catch (SQLException e) {e.printStackTrace();}
			Db = null;
			System.gc();
		} catch (Exception e) {e.printStackTrace();}
	}

}
