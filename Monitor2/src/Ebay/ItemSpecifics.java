package Ebay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.ApiLogging;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.ReviseFixedPriceItemCall;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.NameValueListArrayType;
import com.ebay.soap.eBLBaseComponents.NameValueListType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;

import Database.DatabaseOp;
import Gui.OrdersGui;
import Main.Store;



public class ItemSpecifics extends Thread {
	public static ApiContext apiContext = new ApiContext();
	public static ApiCredential cred = apiContext.getApiCredential(); //
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static  String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static  String eBay_token = null;
	public static  String Server_url = null;
	public static  String Application_id= null;
	public static  SiteCodeType SiteCode = null;
	public static  String Contry = null;

	public ItemSpecifics() {
		System.out.println("Constractor of ItemSpecifics");
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				//System.out.println(sCurrentLine);
				if (sCurrentLine.contains("eBay token:"))
				{
					eBay_token = sCurrentLine.substring(sCurrentLine.indexOf("eBay token:")+12);
					//System.out.println("eBay_token = "+eBay_token);
				}
				
				if (sCurrentLine.contains("Server url:"))
				{
					Server_url = sCurrentLine.substring(sCurrentLine.indexOf("Server url:")+12);
					//System.out.println("Server_url = "+Server_url);
				}
				
				if (sCurrentLine.contains("Application id:"))
				{
					Application_id = sCurrentLine.substring(sCurrentLine.indexOf("Application id:")+16);
					//System.out.println("Application_id = "+Application_id);
				}
				//
				if (sCurrentLine.contains("Site:")&&sCurrentLine.contains("US"))
				{
					//System.out.println("Site code --> US");
					SiteCode =  SiteCodeType.US;
					Contry = "US";
				}
				
				if (sCurrentLine.contains("Site:")&&sCurrentLine.contains("UK"))
				{
				//	System.out.println("Site code --> UK");
					SiteCode =  SiteCodeType.UK;
					Contry = "UK";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {

				ex.printStackTrace();

			}
			}

	}

	public void ItemSpecificsMain(Store Store) throws SQLException
	{

		eBayCalls Call = new eBayCalls();
		DatabaseOp Db = new DatabaseOp();
		ArrayList<ItemForUpdate> ItemsList = new ArrayList<ItemForUpdate>() ;
		
		
		Db.GetAllOnlineItems(ItemsList,Store.Store_name);
		System.out.println("List Size for update = "+ItemsList.size());
		for (ItemForUpdate ele:ItemsList)
		{
			try {
				Call.AddItemSpecifics(ele.EbayId);
				Db.UpdateItemSpecific(ele.EbayId);
			} catch (ApiException e1) {
				e1.printStackTrace();
			} catch (SdkException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	
	}
	
	public void AddItemSpecifics(String EbayID) throws ApiException, SdkException, Exception
	{	
		cred.seteBayToken(eBay_token);
		apiContext.setApiServerUrl(Server_url);
		ApiLogging apiLogging = new ApiLogging();
		apiContext.setApiLogging(apiLogging);       
		apiContext.setSite(SiteCode); 
	    ReviseFixedPriceItemCall reviseFP = new ReviseFixedPriceItemCall(apiContext);
        ItemType item = new ItemType();

        // We are using SKU to identify items
        item.setItemID(EbayID); 

        // Create 4 item specifics for item which was listed on category 63889          
        NameValueListArrayType itemSpec = new NameValueListArrayType();
        NameValueListArrayType temp = new NameValueListArrayType();
        temp =  GetItemSpecifics(EbayID);
        NameValueListType[] array = new NameValueListType[temp.getNameValueListLength()+25];
        int counter = 0;
        for (int i=0;i<temp.getNameValueListLength();i++)
        {
        	array[i] = temp.getNameValueList(i);
        	counter = i;
        }
        NameValueListType nv1 = new NameValueListType();
        nv1.setName("Ships from");
        nv1.setValue(new String[]{Contry});
    	array[counter+1] =  nv1;
    	counter++;
    	NameValueListType nv2 = new NameValueListType();
    	nv2.setName("Seller");
    	nv1.setValue(new String[]{Contry+" Seller"});
    	array[counter+1] =  nv2;
    	counter++;
    	NameValueListType nv3 = new NameValueListType();
    	nv3.setName("Money Back");
    	nv3.setValue(new String[]{"14 Days"});
    	array[counter+1] =  nv3;
    	counter++;
    	NameValueListType nv4 = new NameValueListType();
    	nv4.setName("Shipping");
    	nv4.setValue(new String[]{"Free"});
    	array[counter+1] =  nv4;
    	counter++;
    	
    	NameValueListType nv5 = new NameValueListType();
    	nv5.setName("Satisfaction");
    	nv5.setValue(new String[]{"100%"});
    	array[counter+1] =  nv5;
    	counter++;
    	
    	NameValueListType nv7 = new NameValueListType();
    	nv7.setName("Gift Option");
    	nv7.setValue(new String[]{"FREE"});
    	array[counter+1] =  nv7;
    	counter++;
    	
    	NameValueListType nv8 = new NameValueListType();
    	nv8.setName("Delivery");
    	nv8.setValue(new String[]{"Fast"});
    	array[counter+1] =  nv8;
    	counter++;
    	
    	NameValueListType nv9 = new NameValueListType();
    	nv9.setName("Customer Service");
    	nv9.setValue(new String[]{"Professional Service, We Will Solve Any Problem"});
    	array[counter+1] =  nv9;
    	counter++;
    	
    	NameValueListType nv10 = new NameValueListType();
    	nv10.setName("Handling Time");
    	nv10.setValue(new String[]{"Up to 3 Business Days (Usually same day)"});
    	array[counter+1] =  nv10;
    	counter++;
    	
    	NameValueListType nv11 = new NameValueListType();
    	nv11.setName("Questions");
    	nv11.setValue(new String[]{"ASK SELLER"});
    	array[counter+1] =  nv11;
    	counter++;
    
    	
    	
    	NameValueListType nv6 = new NameValueListType();
    	nv6.setName("Tax");
    	nv6.setValue(new String[]{"No Tax Additoin"});
    	array[counter+1] =  nv6;
    	
        itemSpec.setNameValueList(array);
        item.setItemSpecifics(itemSpec);
        reviseFP.setItemToBeRevised(item);
        try {
            reviseFP.reviseFixedPriceItem();
            
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (SdkException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        nv6 = null;
        nv1= null;
        nv2= null;
        nv3= null;
        nv4= null;
        nv5= null;
        item = null;
        System.gc();
	}
	
	public NameValueListArrayType GetItemSpecifics(String EbayID)
	{
	    cred.seteBayToken(eBay_token);
	    apiContext.setApiServerUrl(Server_url);
	    apiContext.setSite(SiteCode);
		GetItemCall Call = new GetItemCall (apiContext);
		Call.setItemID(EbayID);
		Call.setIncludeItemSpecifics(true);
		try{
		ItemType Item = Call.getItem();
		NameValueListArrayType list =Item.getItemSpecifics();
		return list;
		}catch(Exception e)
		{

		}
		return null;

        
	}


	@Override
	public void run() {
		try {
			if (OrdersGui.StoreName.equals("All"))
			{
				for (Store ele:Main.Main.ListOfStores)
				{
					ItemSpecificsMain(ele);
				}
			}else
			{
				ItemSpecificsMain(Main.Main.GetStoreByName(OrdersGui.StoreName));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
