package MainPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ebay.sdk.ApiAccount;
import com.ebay.sdk.ApiCall;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.ApiLogging;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.TimeFilter;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.GetItemTransactionsCall;
import com.ebay.sdk.helper.ConsoleUtil;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.Amount;
import com.ebay.services.finding.AspectFilter;
import com.ebay.services.finding.FindCompletedItemsRequest;
import com.ebay.services.finding.FindCompletedItemsResponse;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindItemsByKeywordsRequest;
import com.ebay.services.finding.FindItemsByKeywordsResponse;
import com.ebay.services.finding.FindItemsByProductRequest;
import com.ebay.services.finding.FindItemsByProductResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ItemFilter;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.ListingInfo;
import com.ebay.services.finding.OutputSelectorType;
import com.ebay.services.finding.PaginationInput;
import com.ebay.services.finding.ProductId;
import com.ebay.services.finding.SearchItem;
import com.ebay.services.finding.SearchResult;
import com.ebay.services.finding.SortOrderType;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.GetItemRequestType;
import com.ebay.soap.eBLBaseComponents.GetItemResponseType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.NameValueListArrayType;
import com.ebay.soap.eBLBaseComponents.NameValueListType;
import com.ebay.soap.eBLBaseComponents.PaginationType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;
import com.ebay.soap.eBLBaseComponents.TransactionType;




public class EbaySearch {
	
	

	public  static Connection con ;
	public  static java.sql.Statement statement_update ;
	public  static java.sql.Statement statement ;
	public  static String Connection = null;
	public  static String scham = null;
	public  String FILENAME = "C:\\Users\\Noname\\Desktop\\ubuythebest4u-Keys.txt";
	public  String AWS_ACCESS_KEY_ID_FROM_FILE  = null;
	public  String AWS_SECRET_KEY_FROM_FILE = null;
	public  String ENDPOINT_FROM_FILE = null;
	public  ApiContext apiContext = new ApiContext();
	public  ApiCredential cred = apiContext.getApiCredential(); //
	public  String eBay_token = null;
	public  String Server_url = null;
	public  String Application_id= null;
	public  SiteCodeType SiteCode = null;
	public  String Contry = null;
	public  ResultSet res = null;
	public  ApiCredential cred2 = apiContext.getApiCredential(); //
	public  ApiContext    apiContext2 = new ApiContext();
	public  ApiCredential cred3 = apiContext.getApiCredential(); //
	public  ApiContext    apiContext3 = new ApiContext();
	
	
	
	public EbaySearch() {

		FILENAME = "C:\\Users\\Noname\\Desktop\\ubuythebest4u-Keys.txt";
		System.out.println("File name that loaded = "+FILENAME);

		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{


				if (sCurrentLine.contains("eBay token:"))
				{
					eBay_token = sCurrentLine.substring(sCurrentLine.indexOf("eBay token:")+12);
					
				}
				
				if (sCurrentLine.contains("Server url:"))
				{
					Server_url = sCurrentLine.substring(sCurrentLine.indexOf("Server url:")+12);
					
				}
				
				if (sCurrentLine.contains("Application id:"))
				{
					Application_id = sCurrentLine.substring(sCurrentLine.indexOf("Application id:")+16);
					
				}
				
				if (sCurrentLine.contains("Site:")&&sCurrentLine.contains("US"))
				{
					
					SiteCode =  SiteCodeType.US;
					Contry = "US";
				}
				
				if (sCurrentLine.contains("Site:")&&sCurrentLine.contains("UK"))
				{
					
					SiteCode =  SiteCodeType.UK;
					Contry = "UK";
				}
				
			
				if (sCurrentLine.contains("AWS_ACCESS_KEY_ID:"))
				{
					AWS_ACCESS_KEY_ID_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("AWS_ACCESS_KEY_ID:")+19);
				}
				
				if (sCurrentLine.contains("AWS_SECRET_KEY:"))
				{
					AWS_SECRET_KEY_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("AWS_SECRET_KEY:")+16);
				}
				
				if (sCurrentLine.contains("ENDPOINT:"))
				{
					ENDPOINT_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("ENDPOINT:")+10);
				}
				
				if (sCurrentLine.contains("Connection:"))
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:")+12);
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+8);
					Connection =Connection+scham;
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
				System.out.println("Ebay Constarctor ");
			}
			}
	
	
	
	
	}

	public void Finditem (List<product> ItemsList) throws IOException
	{
		ScrapInfo info1 = new ScrapInfo();
	        try {
	        	ClientConfig config = new ClientConfig();
	        	config.setApplicationId("Arthurbo-Getprodu-PRD-c45f6444c-b98e8933");
	            FindingServicePortType serviceClient = FindingServiceClientFactory.getServiceClient(config);
	            FindItemsAdvancedRequest request = new FindItemsAdvancedRequest();
	    		ItemFilter filter2 = new ItemFilter();
	    		filter2.setName(ItemFilterType.CONDITION);
	    		filter2.getValue().add("1000");
	    		request.getItemFilter().add(filter2);  
	    		ItemFilter filter = new ItemFilter();
	    		filter.setName(ItemFilterType.LISTING_TYPE);
	    		filter.getValue().add("FixedPrice");
	    		request.getItemFilter().add(filter);  
	    		ItemFilter filter5 = new ItemFilter();
	    		filter5.setName(ItemFilterType.AVAILABLE_TO);
	    		filter5.getValue().add("US");
	    		request.getItemFilter().add(filter5);  
	    		
	    		ItemFilter filter6 = new ItemFilter();
	    		filter6.setName(ItemFilterType.LOCATED_IN);
	    		filter6.getValue().add("US");
	    		request.getItemFilter().add(filter6);  

	    		
	            for (product ele:ItemsList)
	            {
	            if (ele.getBestresult()!=null)
	            {
	            try
	            {
	            System.out.println("Key word = "+ele.getBestresult());
	            request.setKeywords( ele.getBestresult());
	            request.setSortOrder(SortOrderType.PRICE_PLUS_SHIPPING_LOWEST);
	            FindItemsAdvancedResponse result = serviceClient.findItemsAdvanced(request);//
	            System.out.println("Finditem = "+result.getAck());
	            System.out.println("Finditem =  " + result.getSearchResult().getCount());
	            ele.ebayResults = result.getSearchResult().getCount();
	            List<SearchItem> items = result.getSearchResult().getItem();
	            int counter=1;
	            int  Flag = 0;
	            for(SearchItem item : items) 
	            {
	            	if (counter ==1&&item!=null) 
	            	{
	            		
	            		ele.ebayLowestPrice = (double)(item.getSellingStatus().getCurrentPrice().getValue()+item.getShippingInfo().getShippingServiceCost().getValue());
	            		ele.arbitraje = (ele.ebayLowestPrice + ele.Ebay_shipping) -info1.Min_price_to_sale(ele.price);
	            	}
	            	
	            	try{
	            	if (item.getSellingStatus().getCurrentPrice().getValue() +item.getShippingInfo().getShippingServiceCost().getValue()>info1.Min_price_to_sale(ele.price)&&item!=null&&Flag==0)
	            		{
	            		ele.PlaceInlowestprice = counter;
	            		Flag = 1;
	            		}
	            	}catch (Exception e){}
	            	
	            	/*
	            	if (counter<6)
	            	{
	            		if (item.getListingInfo().getBuyItNowPrice().getValue()>10 &&item.getListingInfo().getBuyItNowPrice().getValue()<30)
	            			ele.sold+=GetItemTransactions(item.getItemId(),7);
	            		if (item.getListingInfo().getBuyItNowPrice().getValue()>30 &&item.getListingInfo().getBuyItNowPrice().getValue()<70)
	            			ele.sold+=GetItemTransactions(item.getItemId(),14);
	            		if (item.getListingInfo().getBuyItNowPrice().getValue()>70 &&item.getListingInfo().getBuyItNowPrice().getValue()<1000)
	            			ele.sold+=GetItemTransactions(item.getItemId(),30);
	            	}
	            	*/
	            counter++;
	            }
	           
	            
	            }
	            catch(Exception ex){ex.printStackTrace();}
	            }
	            
	            if (ele.price>0)
	            {
	            ele.Breakevenlowestprice = ele.ebayLowestPrice/(ele.price+0.3);
	            }else ele.Breakevenlowestprice = 0;
	            
	            if (ele.ebayResults>0)
	            {
	            ele.Sale_true = (ele.sold/ele.ebayResults)*100;
	            }else ele.Sale_true =  0;
	            
	            }
	            } 
	        	catch (Exception ex){System.out.println(ex);ex.printStackTrace();}
	    }//FindItem

	public void findCompletedItems(List<product> ItemsList) throws IOException, SQLException
	{
		ScrapInfo info1 = new ScrapInfo();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Date date2 = new Date();
		date2 = addDays(date,-45);// dateFormat
		try{
    	ClientConfig config = new ClientConfig();
    	config.setApplicationId("Arthurbo-Getprodu-PRD-c45f6444c-b98e8933");
    	FindingServicePortType serviceClient = FindingServiceClientFactory.getServiceClient(config);
		FindCompletedItemsRequest request = new FindCompletedItemsRequest();
		PaginationInput pi = new PaginationInput();
		pi.setEntriesPerPage(200);
		request.setPaginationInput(pi);
        request.setSortOrder(SortOrderType.PRICE_PLUS_SHIPPING_LOWEST);
		ItemFilter filter1 = new ItemFilter();
		filter1.setName(ItemFilterType.SOLD_ITEMS_ONLY);
		filter1.getValue().add("true");
		ItemFilter filter2 = new ItemFilter();
		filter2.setName(ItemFilterType.CONDITION);
		filter2.getValue().add("1000");
	    ItemFilter conditionFilter = new ItemFilter();
	    conditionFilter.setName(ItemFilterType.AVAILABLE_TO);
	    conditionFilter.getValue().add("US");
	    ItemFilter conditionFilter3 = new ItemFilter();
	    conditionFilter3.setName(ItemFilterType.END_TIME_FROM);
	    conditionFilter3.getValue().add(dateFormat.format(date2)+"T00:00:00.768Z");
	    request.getItemFilter().add(conditionFilter3);
	    request.getItemFilter().add(conditionFilter);
	    request.getItemFilter().add(filter1);
	    request.getItemFilter().add(filter2);
	    request.getOutputSelector().add(OutputSelectorType.STORE_INFO);
	    request.getOutputSelector().add(OutputSelectorType.SELLER_INFO);
	    request.getOutputSelector().add(OutputSelectorType.ASPECT_HISTOGRAM);
    
		for (product ele:ItemsList)
		{
		try{
		request.setKeywords(ele.bestresult);
	    if (ele.bestresult!=null)
	    {
		FindCompletedItemsResponse response = serviceClient.findCompletedItems(request); 
		System.out.println(response.getAck().name());
		ele.sold = response.getSearchResult().getCount(); //
		System.out.println("Sold On ebay = "+ele.sold);
		List<SearchItem> items = response.getSearchResult().getItem();
		int counter =1;
		int Flag =0;
		for (SearchItem ele1:items)
		{
			if (counter ==1) ele.LowestSold = ele1.getSellingStatus().getCurrentPrice().getValue()+ele1.getShippingInfo().getShippingServiceCost().getValue();
			try{
			if (ele1.getSellingStatus().getCurrentPrice().getValue() +ele1.getShippingInfo().getShippingServiceCost().getValue()>info1.Min_price_to_sale(ele.price)&&Flag==0)
    		{
    		ele.PlaceInlowestpriceSold = counter;
    		Flag = 1;
    		}
			}catch (Exception e){
			}
			counter++;
		}
	
	    }
		
		}catch (Exception e){ System.out.println(e.getMessage());}
		}
		}catch (Exception e){System.out.println(e.getMessage());}
		
	}

	public int GetItemTransactions(String EbayId,int days) throws ApiException, SdkException, Exception
	{
		cred = apiContext.getApiCredential();
		cred.seteBayToken(eBay_token);
		apiContext.setApiServerUrl(Server_url);     
		apiContext.setSite(SiteCode); 
		GetItemTransactionsCall call = new GetItemTransactionsCall(apiContext);
		call.setItemID(EbayId);
	    Calendar calFrom = Calendar.getInstance();
	    calFrom.add(Calendar.DAY_OF_MONTH,0);
	    Calendar calTo = Calendar.getInstance();
	    calTo.add(Calendar.DAY_OF_MONTH,60);
	    TimeFilter tf = new TimeFilter(calFrom, calTo);
	    call.setModifiedTimeFilter(tf);
	    call.setNumberOfDays(days);
	    try{
		TransactionType[] transactions = call.getItemTransactions();
		System.out.println(transactions.length);
		calFrom = null;
		calTo = null;
		tf = null;
		int x=transactions.length;
		transactions = null;
		System.gc();
		return x;
	    }catch(Exception e1){System.out.println(e1);return -1;}
	}
	
	public  double Min_price_to_sale(double price) {
		double ebay_fees = 0.1;
		double paypal_fees = 0.044;
		double paypal_fixed = 0.3;
		double my_percent = 1.13;
		return ((price * my_percent) + paypal_fixed) / (1 - ebay_fees - paypal_fees);
	}
	
	public void GetItem(String EbayID,product product) throws ApiException, SdkException, Exception
	{
		
		// set devId, appId, certId in ApiAccount
		ApiAccount account = new ApiAccount();
		
		account.setDeveloper("fad33e89-2c15-4347-8d78-b2afe3dd2c93");
		account.setApplication("Arthurbo-Getprodu-PRD-c45f6444c-b98e8933");
		account.setCertificate("PRD-45f6444cb382-970a-4ed3-9a6c-6fba");

		// set ApiAccount and token in ApiCredential
		ApiCredential credential = new ApiCredential();
		credential.setApiAccount( account );
		credential.seteBayToken("AgAAAA**AQAAAA**aAAAAA**G2HQWA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6ABkoOkCZiEowydj6x9nY+seQ**ro4DAA**AAMAAA**fMLY/HzOw6nZlCW6NMNc0ZF5yczH9Bzw9fGxoc900S9F8p40unY/UowNaFG0I83wT3EmRP0XI5nqEaYI/VRBDSi2NoHbNdpYlzEluruw305jIiDJoM17cA1B4kvwRuS2SIa6QRkAc/CF9YNJlq/SgtSqGwMoPNxBydVSB3k0X7bg6OdF2t1und4oDGk9cnQz6BMGJ4qjnZ6HikTePybzuyRaCo71dKNHEiyWCbm6HdG0ZoCPRO0w8WT42EjiJvaHmwbyQhscZ94jRqDL13RgeIYnlqq/1/JUJJHfKuuSjQDX0LWSMmKIDZdxvs9peofR6L7yxQfMpAbtpmZA2voBUM/LWEoCUmBa03b9J87tWoi7bs7zFWSNhjp8L+8rwFbofJar5qaPitqd0pZ9jkryVWzKLbsLV1MYIm9Mh35aA/LCGDNA+Fxd1CruFqSXUIDMle4px/PC/zBCx8FahjtaDISKyxcwjUIv+vd92Vxnpr0yN//qx8Hkep0qGFJDckgXe8uUt6RU+ZVQw2DiqS4pgBP/lUSikQrXzjMXy+Lm/LDmcz0PXBc62Yu++9yYIicfrligFJixfQ03RUIXo46Uuiju/r5iKYmt8yzjCZ7UpxW4FD4SOmnLdCTW6eaWqMe7LSQsiJozFsdq1rNAccz3Ujb5l0aLKXH6a5K7FE/Qgx7UO9kGfNnABYMsgpPLfPRP1on941pkqy6UytI/KijusS/3K8iQEeLtSTXl+EpTJXMiJ2Me6q7GwOLzHrTQnG5b");

		// add ApiCredential to ApiContext
		ApiContext context = new ApiContext();
		context.setApiCredential(credential);
		// set eBay server URL to call
		context.setApiServerUrl("https://api.ebay.com/wsapi"); 

		// set timeout in milliseconds - 3 minutes
		context.setTimeout(180000);

		// set wsdl version number
		//context.setWSDLVersion("423");

		ApiCall call = new ApiCall( context );

		GetItemRequestType Request = new GetItemRequestType();
		
		Request.setItemID(EbayID);
		System.out.println("Checking eBay Id = "+EbayID);
		Request.setIncludeItemSpecifics(true);

		GetItemResponseType Response;
		try{
		Response = (GetItemResponseType) call.executeByApiName("GetItem", Request);
		NameValueListType[] list  = Response.getItem().getItemSpecifics().getNameValueList();

		for (NameValueListType ele:list)
		{
			System.out.println(ele.getName()+" = "+ele.getValue(0));
			if (ele.getName().contains("UPC"))
			{
			System.out.println( ele.getName()+" = "+ele.getValue(0));
			//System.out.println( ele.getValue(0));
			product.UPC = ele.getValue(0);
			product.bestresult = ele.getValue(0);
			}
			
			if (ele.getName().contains("ISBN"))
			{
			System.out.println( ele.getName()+" = "+ele.getValue(0));
			//System.out.println( ele.getValue(0));
			product.Isbn = ele.getValue(0);
			product.bestresult = ele.getValue(0);
			}

		}
		}catch(Exception e){
			System.out.println(e);
			System.out.println("Fail");
		}
		
	}
	
	public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
	
	private static ApiContext getApiContext() throws IOException {
		  
		  String input;
	      ApiContext apiContext = new ApiContext();
	      
	      //set Api Token to access eBay Api Server
	      ApiCredential cred = apiContext.getApiCredential();
	      input = ConsoleUtil.readString("AgAAAA**AQAAAA**aAAAAA**QI5uWA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6ABkoOkCZiEowydj6x9nY+seQ**ro4DAA**AAMAAA**fMLY/HzOw6nZlCW6NMNc0ZF5yczH9Bzw9fGxoc900S9F8p40unY/UowNaFG0I83wT3EmRP0XI5nqEaYI/VRBDSi2NoHbNdpYlzEluruw305jIiDJoM17cA1B4kvwRuS2SIa6QRkAc/CF9YNJlq/SgtSqGwMoPNxBydVSB3k0X7bg6OdF2t1und4oDGk9cnQz6BMGJ4qjnZ6HikTePybzuyRaCo71dKNHEiyWCbm6HdG0ZoCPRO0w8WT42EjiJvaHmwbyQhscZ94jRqDL13RgeIYnlqq/1/JUJJHfKuuSjQDX0LWSMmKIDZdxvs9peofR6L7yxQfMpAbtpmZA2voBUM/LWEoCUmBa03b9J87tWoi7bs7zFWSNhjp8L+8rwFbofJar5qaPitqd0pZ9jkryVWzKLbsLV1MYIm9Mh35aA/LCGDNA+Fxd1CruFqSXUIDMle4px/PC/zBCx8FahjtaDISKyxcwjUIv+vd92Vxnpr0yN//qx8Hkep0qGFJDckgXe8uUt6RU+ZVQw2DiqS4pgBP/lUSikQrXzjMXy+Lm/LDmcz0PXBc62Yu++9yYIicfrligFJixfQ03RUIXo46Uuiju/r5iKYmt8yzjCZ7UpxW4FD4SOmnLdCTW6eaWqMe7LSQsiJozFsdq1rNAccz3Ujb5l0aLKXH6a5K7FE/Qgx7UO9kGfNnABYMsgpPLfPRP1on941pkqy6UytI/KijusS/3K8iQEeLtSTXl+EpTJXMiJ2Me6q7GwOLzHrTQnG5b");
	      
	    		  
	      cred.seteBayToken(input);
	     
	      //set Api Server Url
	     input = ConsoleUtil.readString("https://api.sandbox.ebay.com/wsapi");
	      
	      apiContext.setApiServerUrl(input);
	      
	      return apiContext;
	  }

}
