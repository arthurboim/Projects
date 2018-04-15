

import java.io.IOException;
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
import com.ebay.sdk.SdkException;
import com.ebay.sdk.call.GetItemCall;
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
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ItemFilter;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.ListingInfo;
import com.ebay.services.finding.OutputSelectorType;
import com.ebay.services.finding.PaginationInput;
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

import Database.GetInfoFromDb;
import Database.Seller;
import Database.SetInfoToDb;

/**
 * A sample to show eBay Finding servcie call using the simplied interface 
 * provided by the findingKit.
 * 
 * @author boyang
 * 
 */



// Basic service call flow:
// 1. Setup client configuration
// 2. Create service client
// 3. Create outbound request and setup request parameters
// 4. Call the operation on the service client and receive inbound response
// 5. Handle response accordingly
// Handle exception accordingly if any of the above steps goes wrong.


public class EbaySearch {
	
	

	
	public void Finditem (List<product> ItemsList)
	{
	    // Basic service call flow:
	    // 1. Setup client configuration
	    // 2. Create service client
	    // 3. Create outbound request and setup request parameters
	    // 4. Call the operation on the service client and receive inbound response
	    // 5. Handle response accordingly
	    // Handle exception accordingly if any of the above steps goes wrong.

		ScrapInfo info1 = new ScrapInfo();
	        try {
	        	//SetInfoToDb SetInfo = new SetInfoToDb();
	    		//ArrayList<Seller> Existing_Seller = new ArrayList<Seller>();
	    		//GetInfoFromDb GetInfoFromDb = new GetInfoFromDb();
	    		//GetInfoFromDb.GetAllSellers(Existing_Seller);
	        	// initialize service end-point configuration
	        	ClientConfig config = new ClientConfig();
	        	// endpoint address can be overwritten here, by default, production address is used,
	        	// to enable sandbox endpoint, just uncomment the following line
	        	//config.setEndPointAddress("http://svcs.sandbox.ebay.com/services/search/FindingService/v1");
	        	config.setApplicationId("Arthurbo-Getprodu-PRD-c45f6444c-b98e8933");

	        	//create a service client
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


	            //call service
	    		
	            FindItemsAdvancedResponse result = serviceClient.findItemsAdvanced(request);//
	            
	            //output result
	            System.out.println("Finditem = "+result.getAck());
	            System.out.println("Finditem =  " + result.getSearchResult().getCount());
	            ele.ebayResults = result.getSearchResult().getCount();
	            
	            
	            
	            List<SearchItem> items = result.getSearchResult().getItem();
	       
	            int counter=1;
	            int median_place = result.getSearchResult().getCount()/2;
	            int  Flag = 0;
	            for(SearchItem item : items) 
	            {
	            	
	            	if (counter ==1) 
	            	{
	            		ele.ebayLowestPrice = (double)(item.getSellingStatus().getCurrentPrice().getValue()+item.getShippingInfo().getShippingServiceCost().getValue());
	            		ele.arbitraje = (ele.ebayLowestPrice + ele.Ebay_shipping) -info1.Min_price_to_sale(ele.price);
	            	}
	            	
	            	try{
	            	if (item.getSellingStatus().getCurrentPrice().getValue() +item.getShippingInfo().getShippingServiceCost().getValue()>info1.Min_price_to_sale(ele.price)&&Flag==0)
	            		{
	            		
	            		ele.PlaceInlowestprice = counter;
	            		Flag = 1;
	            		}
	            	}catch (Exception e){}
	            	
	            	try{
	            	//System.out.println(item.getSellingStatus().getCurrentPrice().getValue()+item.getShippingInfo().getShippingServiceCost().getValue());
	            	}catch (Exception e){}
	            	if (counter == median_place) 
	            	{
	            		//System.out.println("Median is ="+item.getSellingStatus().getCurrentPrice().getValue()+item.getShippingInfo().getShippingServiceCost().getValue());
	            		ele.MedianPrice = (double)(item.getSellingStatus().getCurrentPrice().getValue()+item.getShippingInfo().getShippingServiceCost().getValue());
	            	}
	            	
	            	// Here add a function //
	            	/*
	            	if (item.getSellerInfo().getFeedbackScore()<1200 && item.getSellerInfo().getFeedbackScore()>300&&Existing_Seller_Check(Existing_Seller,item.getSellerInfo().getSellerUserName()))
	            	{
	            	Seller seller = new Seller();
	            	seller.Feedbackscore = item.getSellerInfo().getFeedbackScore().intValue();
	            	seller.PositiveFeedbackPercent = item.getSellerInfo().getPositiveFeedbackPercent();
	            	seller.Seller_name = item.getSellerInfo().getSellerUserName();
	            	SetInfo.Set_Seller(seller);	
	            	}
	            	*/
	           counter++;
	            }// inner loop
	            }
	            catch(Exception ex){ex.printStackTrace();}
	            }
	            
	            }// for
	            } //try
	        	catch (Exception ex) 
	        	{
	            ex.printStackTrace();
	            }//catch
				

	        
	    }//FindItem

	public boolean Existing_Seller_Check(ArrayList<Seller> Existing_Seller , String NewSeller)
	{
		for (Seller ele:Existing_Seller)
		{
			if (ele.Seller_name.equals(NewSeller)) return false;
		}
		
		return true;
	}
	
	public  double Min_price_to_sale(double price) {
		double ebay_fees = 0.1;
		double paypal_fees = 0.044;
		double paypal_fixed = 0.3;
		double my_percent = 1.13;
		return ((price * my_percent) + paypal_fixed) / (1 - ebay_fees - paypal_fees);
	}
	

	
	public void findCompletedItems(List<product> ItemsList) throws IOException, SQLException
	{
		List<String> al = new ArrayList<>();
		// add elements to al, including duplicates
		Set<String> hs = new HashSet<>();

		ScrapInfo info1 = new ScrapInfo();
		Database db = new Database();
		ArrayList<Seller> list = new ArrayList<Seller>();
		ArrayList<Seller> list_new = new ArrayList<Seller>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Date date2 = new Date();
		date2 = addDays(date,-45);// dateFormat
		try{
    	ClientConfig config = new ClientConfig();
    	config.setApplicationId("Arthurbo-Getprodu-PRD-c45f6444c-b98e8933");
    	FindingServicePortType serviceClient = FindingServiceClientFactory.getServiceClient(config);
    	
    
		for (product ele:ItemsList)
		{
		try{
		FindCompletedItemsRequest request = new FindCompletedItemsRequest();
		PaginationInput pi = new PaginationInput();
		pi.setEntriesPerPage(700);
		request.setPaginationInput(pi);
		PaginationType pagination = new PaginationType();
		pagination.setEntriesPerPage(500);
		request.setKeywords(ele.bestresult);
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
	    
	    //OutputSelectorType.STORE_INFO
	    request.getOutputSelector().add(OutputSelectorType.STORE_INFO);
	    request.getOutputSelector().add(OutputSelectorType.SELLER_INFO);
	    request.getOutputSelector().add(OutputSelectorType.ASPECT_HISTOGRAM);
	    if (ele.bestresult!=null)
	    {
		FindCompletedItemsResponse response = serviceClient.findCompletedItems(request);  
		//System.out.println("findCompletedItems = "+response.getAck());
		//System.out.println("findCompletedItems = "+response.getSearchResult().getCount());
		ele.sold = response.getSearchResult().getCount(); //
		System.out.println("Sold On ebay = "+ele.sold);
		List<SearchItem> items = response.getSearchResult().getItem();
	    
	    
		int counter =1;
		int Flag =0;
		int median_place = response.getSearchResult().getCount()/2;
		
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
			
			if (counter == median_place) 
        	{
        		//System.out.println("Median is ="+ele1.getSellingStatus().getCurrentPrice().getValue());
        		ele.MedianPriceSold = (double)(ele1.getSellingStatus().getCurrentPrice().getValue()+ele1.getShippingInfo().getShippingServiceCost().getValue());
        	}
			
			counter++;
		
		}//for (SearchItem ele1:items)
		/*
		for (SearchItem ele1:items)
		if (ele1.getSellerInfo().getSellerUserName()!=null)
		{
		al.add(ele1.getSellerInfo().getSellerUserName());
		}
		hs.addAll(al);
		al.clear();
		al.addAll(hs);
		 */
	    }
		
		}catch (Exception e){ System.out.println(e.getMessage());}
		
		// set the saller to the database //

		}// for big
		

		
		}catch (Exception e){System.out.println(e.getMessage());}
		/*
		hs.addAll(al);
		al.clear();
		al.addAll(hs);
		
		System.out.println("*************ALL SAllERS****************");
		hs.addAll(al);
		al.clear();
		al.addAll(hs);
		for (String ele1:al)
			System.out.println(ele1);
		System.out.println("*************ALL SAllERS****************");
		System.out.println("\n\n*************Update into the database****************");
		db.Get_Sellers(list);
		
		for (String ele1:al)
		{
		if (Seller_exist(list,ele1)== false)
		{
			Seller  temp = new Seller();
			temp.Store_Check = 0;
			temp.SellerID = ele1;
			list_new.add(temp);
		}
		}
		
		db.Set_Seller(list_new);
		
		System.out.println("*************Update into the database****************");
		*/
	}//End

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
		System.out.println("Checking Ebay Id = "+EbayID);
	    //DetailLevelCodeType[] values = new DetailLevelCodeType[1];
      //  values[0] = DetailLevelCodeType.RETURN_ALL;
	//	Request.setDetailLevel(values);
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


}//EbaySearch 
