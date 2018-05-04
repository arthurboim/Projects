package PriceChanger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ItemFilter;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.OutputSelectorType;
import com.ebay.services.finding.SearchItem;
import com.ebay.services.finding.SortOrderType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;

public class EbayPriceChanger {

	public   ResultSet res = null;
	public  Connection con = null;
	public   java.sql.Statement statement = null;
	public  ClientConfig config = null; 
	public  ApiContext apiContext = new ApiContext();
	public   ApiCredential cred = apiContext.getApiCredential(); //
	public   String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public   String eBay_token = null;
	public   String Server_url = null;
	public   String Application_id= null;
	public   SiteCodeType SiteCode = null;
	public   String Contry = null;
	public  String Connection = null;
	public  String scham = null;
	public static SimpleDateFormat  format= null;
	public static ChromeOptions options = new ChromeOptions();
	public static ChromeDriver Driver = null;
	public DatabasePriceChanger Db = null;
	public static int Ebayresults =-1;
	public static double EbayLowestprice =-1;
	public static double EbaySecondLowestprice =-1;
	public static double CurrenteBayPrice =-1;
	
	public EbayPriceChanger() throws SQLException {


		Db = new DatabasePriceChanger();
		//System.out.println("Constractor of Database");
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
				if (sCurrentLine.contains("Connection:"))
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:")+12);
					//System.out.println("Connection = "+Connection);
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+8);
					//System.out.println("Schame = "+scham);
					Connection =Connection+scham;
					//System.out.println("Connection = "+Connection);
				}
				if (sCurrentLine.contains("Site:")&&sCurrentLine.contains("US"))
				{
					//System.out.println("Site code --> US");
					SiteCode =  SiteCodeType.US;
					Contry = "US";
				}
				
				if (sCurrentLine.contains("Site:")&&sCurrentLine.contains("UK"))
				{
					//System.out.println("Site code --> UK");
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

	public int ItemPosition(String Code, String SortType)
	{

	   
	  		  	ClientConfig config = new ClientConfig(); 
	        	config.setApplicationId(Application_id);
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
	    		filter5.getValue().add(Contry);
	    		request.getItemFilter().add(filter5);  
	    		
	    		ItemFilter filter6 = new ItemFilter();
	    		filter6.setName(ItemFilterType.LOCATED_IN);
	    		filter6.getValue().add(Contry);
	    		request.getItemFilter().add(filter6);  
	            List<OutputSelectorType> opSelector = request.getOutputSelector();
	            opSelector.add(OutputSelectorType.SELLER_INFO);
	            
	            try
	            {
	            if (Code!=null) 
	            {
	            System.out.println("Code = "+Code);
	            request.setKeywords(Code);
	            }
	            if (SortType.equals("BestMatch")) request.setSortOrder(SortOrderType.BEST_MATCH);
	            if (SortType.equals("LowestPrice")) request.setSortOrder(SortOrderType.PRICE_PLUS_SHIPPING_LOWEST);
	            FindItemsAdvancedResponse result = serviceClient.findItemsAdvanced(request);//
	            if (!result.getAck().equals("FAILURE"))
	            {
	            System.out.println("Finditem = "+result.getAck());
	            System.out.println("Finditem =  " + result.getSearchResult().getCount());
	            Ebayresults = result.getSearchResult().getCount();
	            List<SearchItem> items = result.getSearchResult().getItem();
	            if (SortType.equals("BestMatch"))
	            {
	            int BestMatchPositioncounter =1;
	            for(SearchItem ele:items)
	            {
	            	if (ele.getSellerInfo().getSellerUserName().equals("ConfigFile"))
	            	{
	            	System.out.println(SortType+" Place = "+BestMatchPositioncounter);
		            filter2 = null;
		            opSelector = null;
		            filter5 = null;
		            filter6 = null;
		            items = null;
		            opSelector = null;
		            items = null;
		            result = null;
		            config = null;
		            serviceClient = null;
		            request = null;
		            SortType = null;
		            Code = null;
				
			    	System.gc();
	            	return BestMatchPositioncounter;
	            	}
	            	BestMatchPositioncounter++;
	            }
	            }
	            else
	            {
	            int LowestPricePositioncounter =1;
	            EbayLowestprice = items.get(0).getSellingStatus().getCurrentPrice().getValue()+items.get(0).getShippingInfo().getShippingServiceCost().getValue();
	            try{
	            EbaySecondLowestprice = items.get(1).getSellingStatus().getCurrentPrice().getValue()+items.get(1).getShippingInfo().getShippingServiceCost().getValue();
	            }catch(Exception e1){EbaySecondLowestprice = -1;}
	            System.out.println("Lowest price "+EbayLowestprice);
	            for(SearchItem ele:items)
	            {
	            	if (ele.getSellerInfo().getSellerUserName().equals("ConfigFile"))
	            	{
	            	System.out.println(SortType+" Place = "+LowestPricePositioncounter);
	            	CurrenteBayPrice = ele.getSellingStatus().getCurrentPrice().getValue();
		            filter2 = null;
		            opSelector = null;
		            filter5 = null;
		            filter6 = null;
		            items = null;
		            opSelector = null;
		            items = null;
		            result = null;
		            config = null;
		            serviceClient = null;
		            request = null;
		            SortType = null;
		            Code = null;
			    	System.gc();

	            	return LowestPricePositioncounter;
	            	}
	            	LowestPricePositioncounter++;
	            }
	            }
	            }
	            }
	            catch(Exception ex){ex.printStackTrace();}

			return 0;
	}

	public String BestMatchCode(String EbayId)
	{
	
		String Code ="";
	    cred.seteBayToken(eBay_token);
	    apiContext.setApiServerUrl(Server_url);
	    apiContext.setSite(SiteCode);
	    
		GetItemCall Call = new GetItemCall (apiContext);
		Call.setItemID(EbayId);
		Call.setIncludeItemSpecifics(true);
		try{
		ItemType Item = Call.getItem();
		
		for(int i=0;i<Item.getItemSpecifics().getNameValueListLength();i++)
		{

		if (Item.getItemSpecifics().getNameValueList(i).getName().equals("UPC") && !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does Not Apply"))
		{
		System.out.println(Item.getItemSpecifics().getNameValueList(i).getName()+" = "+Item.getItemSpecifics().getNameValueList(i).getValue(0));
		Code = Item.getItemSpecifics().getNameValueList(i).getValue(0);
		break;
		}
		else if (Item.getItemSpecifics().getNameValueList(i).getName().equals("ISBN")&& !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does Not Apply"))
		{
		System.out.println(Item.getItemSpecifics().getNameValueList(i).getName()+" = "+Item.getItemSpecifics().getNameValueList(i).getValue(0));
		Code =  Item.getItemSpecifics().getNameValueList(i).getValue(0);
		break;
		}
		
		}
	
		}catch(Exception e)
		{}
		
		System.gc();
		return Code;	
	}
	
}
