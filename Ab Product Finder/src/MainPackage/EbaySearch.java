package MainPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.ebay.sdk.ApiAccount;
import com.ebay.sdk.ApiCall;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.TimeFilter;
import com.ebay.sdk.call.GetItemTransactionsCall;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ItemFilter;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.SearchItem;
import com.ebay.services.finding.SortOrderType;
import com.ebay.soap.eBLBaseComponents.GetItemRequestType;
import com.ebay.soap.eBLBaseComponents.GetItemResponseType;
import com.ebay.soap.eBLBaseComponents.NameValueListType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;
import com.ebay.soap.eBLBaseComponents.TransactionType;

public class EbaySearch {
	public  String FILENAME = "C:\\keys\\ConfigFile-Keys.txt";
	public  ApiContext apiContext = new ApiContext();
	public  String eBay_token = null;
	public  String Server_url = null;
	public  SiteCodeType SiteCode = null;

	public EbaySearch() {

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
	    		request.setSortOrder(SortOrderType.PRICE_PLUS_SHIPPING_LOWEST);

	    	    
	            for (product ele:ItemsList)
	            {
	            
	            try
	            {
	            	request.setKeywords(ele.getBestresult());
	            	FindItemsAdvancedResponse result = serviceClient.findItemsAdvanced(request);
	            	ele.ebayResults = result.getSearchResult().getCount();
	            	if(0 == ele.ebayResults)
	            	{
	            		continue;
	            	}
	            	
		            List<SearchItem> items = result.getSearchResult().getItem();
		            int counter=1;
		            
		            ele.ebayLowestPrice = (double)(items.get(0).getSellingStatus().getCurrentPrice().getValue()+items.get(0).getShippingInfo().getShippingServiceCost().getValue());
		            ele.arbitraje 		= (ele.ebayLowestPrice + ele.Ebay_shipping) - info1.Min_price_to_sale(ele.price);
		            
		            for(SearchItem item : items) 
		            {
			          try{
		            	if (item.getSellingStatus().getCurrentPrice().getValue() +item.getShippingInfo().getShippingServiceCost().getValue()>info1.Min_price_to_sale(ele.price) && item != null )
		            	{
		            		ele.PlaceInlowestprice = counter;
		            		break;
		            	}
			          }catch (Exception e){}
			           counter++;
			        }
	            }
	            catch(Exception ex){}
	            
	            if (ele.price>0)
	            {
	            	ele.Breakevenlowestprice = ele.ebayLowestPrice/(ele.price+0.3);
	            }

	            if (ele.ebayResults>0)
	            {
	            	ele.Sale_true = (ele.sold/ele.ebayResults)*100;
	            }
	            
	            }

	    }//FindItem

	public int GetItemTransactions(String EbayId,int days) throws ApiException, SdkException, Exception
	{
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
	
}
