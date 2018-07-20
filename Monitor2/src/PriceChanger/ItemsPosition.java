package PriceChanger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xml.sax.SAXException;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.ApiLogging;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.TimeFilter;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.GetItemTransactionsCall;
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
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;
import com.ebay.soap.eBLBaseComponents.TransactionType;
import Gui.OrdersGui;
import Main.Store;
import Main.Yaballe;

public class ItemsPosition implements Runnable  {

	public  static ClientConfig config = null; 
	public static int Ebayresults =-1;
	public static double EbayLowestprice =-1;
	public static double EbaySecondLowestprice =-1;
	public static double CurrenteBayPrice =-1;
	public   Date date = null;
	public   Time time = null;
	public  DatabasePriceChanger Db = null;
	public   static ResultSet res = null;
	public static  Connection con = null;
	public   static java.sql.Statement statement = null;
	public   String Asin ="";
	public   String EbayId ="";
	public   int BestMatchPosition =-1;
	public   int lowestpricePosition =-1;

	public  double CurrentAmazonPriceApi =-1;
	public  double CurrentAmazonPriceScraper =-1;

	public  double CurrentAmazonTaxScraper =-1;
	public  double BreakEvenLowestForPrice =-1;
	public static  ApiContext apiContext = new ApiContext();
	public  static  ApiCredential cred = apiContext.getApiCredential(); //
	public  static  String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public  static String Connection = null;
	public  static String scham = null;
	public  static String eBay_token = null;
	public   static String Server_url = null;
	public   static String Application_id= null;
	public   static SiteCodeType SiteCode = null;
	public  static  String Contry = null;
	public  String Code = "";
	public static  SimpleDateFormat  format= null;
	public  static ChromeOptions options = new ChromeOptions();
	public static  ChromeDriver Driver = null;
	public   ArrayList<ListInformation> list = new ArrayList<ListInformation>();
	public static int Lock = 0;
	
	public ItemsPosition() throws InterruptedException, SQLException {

		
		config = new ClientConfig(); 
		Db = new DatabasePriceChanger();
		date = new Date();
		format = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
		System.out.println("Constractor of Database");
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

	public void ItemPositionMain(Store Store) throws InterruptedException
	{
		
		try {
			Db.GetOnlineItemsInfo(list,Store.Store_name); //get all items from DB //
			GetSoldAmountLastDays(list,30);
			list.removeAll(list);
			Db.GetOnlineItemsWithOutCode(list,Store.Store_name);
			System.out.println("There is a "+list.size()+" without code");
			BestMatchCodeList(list);
			list.removeAll(list);
			Db.GetOnlineItemsInfoThatNotSold(list,Store.Store_name); 
			System.out.println("There is a "+list.size()+" to change");
			ItemPositionCheckBestMatch();
			ItemPositionCheckLowestPrice();
			while(Lock<2);
			Db.SetListInfoToDb(list);
			GetAmazonInfoScraper(Store); // Testing here //
			list.removeAll(list);
			CalculateData(Store);
			PriceChangerConditionYaballe(Store);
		}catch(Exception e2){System.out.println(e2);}
		
			//PriceChangerCondition();
	}

	public void PriceChangerConditionYaballe(Store Stroe) throws InterruptedException, SQLException
	{
		list.removeAll(list);
		Db.GetOnlineItemsToUpdate(list); 
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver Driver = new ChromeDriver(options);
	
		Yaballe.YaballeLogin(Driver,Stroe.MonitorUser,Stroe.MonitorPassword);//Login to monitor //

		double ProfitPercent = 5; 
		System.out.println("list size = "+list.size());
		for(ListInformation ele:list)
		{
		try{

		
		ProfitPercent = getProfitPercent(ele.AmazonPrice,ele.EbayLowestprice,ele.Tax>0?18.5:13.1);
		if (ProfitPercent<2)
		ProfitPercent = getProfitPercent(ele.AmazonPrice,ele.EbaySecondLowestprice,ele.Tax>0?18.5:13.1);
		if (ProfitPercent<2) 
		{
			System.out.println("Profit Percent = "+ProfitPercent);
			continue;
		}
		if (ItemSearch(Driver,ele.Asin)==1)
		SetPriceFileds(Driver,ProfitPercent,ele.Tax>0?18.5:13.1);
		Thread.sleep(5000);
		}catch(Exception e){System.out.println("Exception in loop");}
		}
		Driver.close();
		Driver = null;
		System.gc();
	}

	public void PriceChangerConditionYaballe(ArrayList<ListInformation> list,Store store) throws InterruptedException, SQLException
	{
		list.removeAll(list);
		Db.GetOnlineItemsToUpdate(list); 
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver Driver = new ChromeDriver(options);
	
		Yaballe.YaballeLogin(Driver,store.MonitorUser, store.MonitorPassword);

		double ProfitPercent = 5; 
		System.out.println("list size = "+list.size());
		for(ListInformation ele:list)
		{
		try{
		if (ele.PriceChangeFlag==0)
		Db.UpdateFlagChange(ele.EbayId);
		else continue;
			
		if (ele.lowestpricePosition==1)
		{
		ProfitPercent = getProfitPercent(ele.AmazonPrice,ele.EbaySecondLowestprice,ele.Tax>0?18.5:13.1);
		if (ProfitPercent>2)
		{
			if (ItemSearch(Driver,ele.Asin)==1)
			{
			SetPriceFileds(Driver,ProfitPercent,ele.Tax>0?18.5:13.1);
			Db.UpdateTimeChange(ele.EbayId);
			Db.UpdateProfitPercent(ele.EbayId,ProfitPercent);
			Db.UpdateBreakEven(ele.EbayId,ele.Tax>0?18.5:13.1);
			}
		}
		}
		
		
		ProfitPercent = getProfitPercent(ele.AmazonPrice,ele.EbayLowestprice,ele.Tax>0?18.5:13.1);
		if (ProfitPercent<2)
		ProfitPercent = getProfitPercent(ele.AmazonPrice,ele.EbaySecondLowestprice,ele.Tax>0?18.5:13.1);
		if (ProfitPercent<2) 
		{
			System.out.println("Profit Percent = "+ProfitPercent);
			if (ItemSearch(Driver,ele.Asin)==1)
			{
			SetPriceFileds(Driver,2,ele.Tax>0?18.5:13.1);
			Db.UpdateTimeChange(ele.EbayId);
			Db.UpdateProfitPercent(ele.EbayId,2);
			Db.UpdateBreakEven(ele.EbayId,ele.Tax>0?18.5:13.1);
			}
			continue;
		}
		
		System.out.println("Profit Percent = "+ProfitPercent);
		if (ItemSearch(Driver,ele.Asin)==1)
		{
		SetPriceFileds(Driver,ProfitPercent,ele.Tax>0?18.5:13.1);
		Db.UpdateTimeChange(ele.EbayId);
		Db.UpdateProfitPercent(ele.EbayId,ProfitPercent);
		Db.UpdateBreakEven(ele.EbayId,ele.Tax>0?18.5:13.1);
		}
		
		
		Thread.sleep(5000);
		}catch(Exception e){System.out.println("Exception in loop");Thread.sleep(20000);}
		}
		Driver.close();
		Driver = null;
		System.gc();
	}

	private double getProfitPercent(double AmazonPrice ,double EbayLowestprice , double breakEven )
	{

		return ((((EbayLowestprice-0.01)*(1-(breakEven/100))-0.3)-AmazonPrice)/AmazonPrice)*100;
	}
	
	public int ItemSearch(ChromeDriver Driver, String ItemCode) throws InterruptedException
	{
		System.out.println("ItemSearch start");
		WebDriverWait wait = new WebDriverWait(Driver, 20);
		while(true)
		{
			try{
				
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='page-content']/div/div/div/div/div[2]/div[2]")));
			break;
			}catch(Exception e){System.out.println();System.out.println(e);return 0;}
		}
									
		while (Driver.findElement(By.xpath("//*[@id='page-content']/div/div/div/div/div[2]/div[2]")).getText().length()<"Items:".length());
		Thread.sleep(3000);
		Driver.findElementByXPath("//*[@id='page-content']/div/div/div/div/div[1]/div[1]/input").clear();
		Driver.findElementByXPath("//*[@id='page-content']/div/div/div/div/div[1]/div[1]/input").sendKeys(ItemCode);
		Thread.sleep(3000);		   
		while (true)
		{
			try{
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='page-content']/div/div/md-content/div/table/tbody/tr/td[15]/div/button/i")));
			break;
			}catch(Exception e){System.out.println();System.out.println(e);return 0;}
		}
		Thread.sleep(4000);		   
		Driver.findElementByXPath("//*[@id='page-content']/div/div/md-content/div/table/tbody/tr/td[15]/div/button/i").click();
		
		while (true)
		{
			try{															   
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[4]/md-dialog/form/md-toolbar/div/h4")));
			break;
			}catch(Exception e){System.out.println();System.out.println(e);return 0;}
		}
		Thread.sleep(500);
		//System.out.println(Driver.findElement(By.xpath("/html/body/div[4]/md-dialog/form/md-toolbar/div/h4")).getText());
		System.out.println("Done!");
		wait = null;
		System.gc();
		return 1;
	}
	
	public void SetPriceFileds(ChromeDriver Driver, double newProfit,double newbreakeven) throws InterruptedException
	{
		String dialogContent = null;
		
		try{
		Thread.sleep(3000);
		List<WebElement> listOfElements = new ArrayList<WebElement>();
		listOfElements = Driver.findElements(By.tagName("md-dialog-content"));
		for (WebElement ele1:listOfElements)
		{
			try{

			System.out.println(dialogContent = ele1.getAttribute("id"));
			break;
			}catch(Exception e){System.out.println("SetPriceFileds loop error");}
		}
		Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[1]/input[3]").clear();
		Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[1]/input[3]").sendKeys(""+newbreakeven);
		Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[2]/input[1]").clear();
		Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[2]/input[1]").sendKeys(""+0);
		Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[2]/input[2]").clear();
		Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[2]/input[2]").sendKeys(""+newProfit);
		Thread.sleep(500);
		Driver.findElementByXPath("/html/body/div[4]/md-dialog/form/md-dialog-actions/button[2]/span").click();
		Thread.sleep(1000);
		System.out.println("SetPriceFileds finished");
		System.out.println();
		}catch(Exception e1){System.out.println("SetPriceFileds error");}
	}
	
	public void SetRisePriceFileds(ChromeDriver Driver,double Tax) throws InterruptedException
	{
		String dialogContent = null;
		
		Thread.sleep(3000);
		List<WebElement> listOfElements = new ArrayList<WebElement>();
		listOfElements = Driver.findElements(By.tagName("md-dialog-content"));
		for (WebElement ele1:listOfElements)
		{
			try{

			System.out.println(dialogContent = ele1.getAttribute("id"));
			break;
			}catch(Exception e){}
		}
		double currPercent = 5;
		if (Tax>0)
		{
			if (Double.parseDouble(Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[1]/input[3]").getAttribute("value"))<15);
			{
				Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[1]/input[3]").clear();
				Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[1]/input[3]").sendKeys(""+18.5);
			}
		}
		
		
		
		Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[2]/input[1]").clear();
		Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[2]/input[1]").sendKeys(""+0);
		Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[2]/input[2]").click();
		
		System.out.println(Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[2]/input[2]").getAttribute("value"));
		currPercent = Double.parseDouble(Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[2]/input[2]").getAttribute("value"));
		currPercent+=2;
		System.out.println("New percent = "+currPercent);
		Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[2]/input[2]").clear();
		Driver.findElementByXPath("//*[@id='"+dialogContent+"']/div[2]/input[2]").sendKeys(""+currPercent);
		Thread.sleep(500);
		Driver.findElementByXPath("/html/body/div[4]/md-dialog/form/md-dialog-actions/button[2]/span").click();
		Thread.sleep(1000);
	}
	
	public void GetSoldAmountLastDays(ArrayList<ListInformation> List,int NumberOfDays) throws ApiException, SdkException, Exception
	{
		Db.UpdatePriceChangeTo0();
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		cred = apiContext.getApiCredential();
		cred.seteBayToken(eBay_token);
		apiContext.setApiServerUrl(Server_url);
		ApiLogging apiLogging2 = new ApiLogging();
		apiContext.setApiLogging(apiLogging2);       
		apiContext.setSite(SiteCode); 
		GetItemTransactionsCall call = new GetItemTransactionsCall(apiContext);
	    Calendar calFrom = Calendar.getInstance();
	    calFrom.add(Calendar.DAY_OF_MONTH,0);
	    Calendar calTo = Calendar.getInstance();
	    calTo.add(Calendar.DAY_OF_MONTH,60);
	    TimeFilter tf = new TimeFilter(calFrom, calTo);
	    call.setModifiedTimeFilter(tf); 
	    call.setNumberOfDays(NumberOfDays);

	    System.out.println("Start checking sold amount last "+NumberOfDays+" days...");
	    Thread.sleep(1000);
		for(ListInformation ele:List)
		{
		try{
		call.setItemID(ele.EbayId);
		TransactionType[] transactions = call.getItemTransactions();
		if (transactions==null)
		System.out.println("Item sold "+0+" times");
		else
		{
		System.out.println("Item sold "+transactions.length+" times");
		System.out.println("Last one sold at "+dt1.format(transactions[transactions.length-1].getPaidTime().getTime()));
		Db.UpdateDateSoldAndSoldAmount(dt1.format(transactions[transactions.length-1].getPaidTime().getTime()),transactions.length,ele.EbayId);
		}
		// write info to database 	Db.SetListInfoToDb(list);//
		}catch(Exception e){System.out.println("Can't get item info");}
		}
	    System.out.println("End checking sold amount last "+NumberOfDays+" days...");
	    Thread.sleep(1000);
		//transactions = null;
		apiLogging2 = null;
		call= null;
		calFrom = null;
		calTo = null;
		tf = null;
		System.gc();
	
	}
	
	public static int ItemPosition(String Code, String SortType)
	{
		
	        try {
	        	
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
	            
	            //output result
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
	            	return LowestPricePositioncounter;
	            	}
	            	LowestPricePositioncounter++;
	            }
	            }

	            filter2 = null;
	            opSelector = null;
	            filter5 = null;
	            filter6 = null;
	            items = null;
	            opSelector = null;
	            System.gc();

	            }
	            catch(Exception ex){ex.printStackTrace();}
	            } //try
	        	catch (Exception ex) {ex.printStackTrace();}//catch
	        
	     
			return 0;
	}
	
	public void ItemPositionList(String Code, String SortType , ArrayList<ListInformation> List)
	{	
	        try {
	            int BestMatchPositioncounter =1;
	            int LowestPricePositioncounter =1;
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
	            
	            
	            if (SortType.equals("BestMatch")) request.setSortOrder(SortOrderType.BEST_MATCH);
	            if (SortType.equals("LowestPrice")) request.setSortOrder(SortOrderType.PRICE_PLUS_SHIPPING_LOWEST);
	    		int counter = List.size();
	            for (ListInformation ele:List)
	            {
	            	try{
	            	System.out.println("Left = "+counter--);
	            	request.setKeywords(ele.Code);
		            FindItemsAdvancedResponse result = serviceClient.findItemsAdvanced(request);//
		            ele.Ebayresults = result.getSearchResult().getCount();
		            List<SearchItem> items = result.getSearchResult().getItem();
		            if (SortType.equals("BestMatch"))
		            {
		            BestMatchPositioncounter = 0;
		            for(SearchItem ele1:items)
		            {
		            	BestMatchPositioncounter++;
		            	if (ele1.getSellerInfo().getSellerUserName().equals("ConfigFile"))
		            	{
		            	System.out.println(SortType+" Place = "+BestMatchPositioncounter);
		            	ele.BestMatchPosition = BestMatchPositioncounter;
		            	break;
		            	}
		            	
		            }
		    
		            }
		            else
		            {
		            try{
		            ele.EbayLowestprice = items.get(0).getSellingStatus().getCurrentPrice().getValue()+items.get(0).getShippingInfo().getShippingServiceCost().getValue();
		            }catch(Exception e1){ele.EbayLowestprice = -1;}
		            try{
		            ele.EbaySecondLowestprice = items.get(1).getSellingStatus().getCurrentPrice().getValue()+items.get(1).getShippingInfo().getShippingServiceCost().getValue();
		            }catch(Exception e1){ele.EbaySecondLowestprice = -1;}
		            System.out.println("Lowest price "+ele.EbayLowestprice);
		            System.out.println("Second Lowest price "+ele.EbaySecondLowestprice);
		            LowestPricePositioncounter = 0;
		            for(SearchItem ele1:items)
		            {
		            	LowestPricePositioncounter++;
		            	if (ele1.getSellerInfo().getSellerUserName().equals("ConfigFile"))
		            	{
		            	System.out.println(SortType+" Place = "+LowestPricePositioncounter);
		            	ele.CurrenteBayPrice = ele1.getSellingStatus().getCurrentPrice().getValue();
		            	ele.lowestpricePosition = LowestPricePositioncounter;
		            	
		            	break;
		            	}
		            
		            }}
		            
	            }catch(Exception e1){System.out.println("Request failed");}
	            }
	            	
	            }catch (Exception ex) {ex.printStackTrace();}//catch

	}
	
	public  void BestMatchCodeList(ArrayList<ListInformation> List) throws SQLException, InterruptedException, ParserConfigurationException, SAXException, IOException
	{
		String ListOfAsins = "";
		AmazonCall AmazonApiCall = new AmazonCall();

		int ItemsCounter = 0;
		System.out.println("Geting all codes for items that NULL...");
		for(ListInformation ele:List)
		{
			if (ele.Code==null || ele.Code.equals("null") ||  ele.Code.equals("Does not apply"))
			{
				
				if (ItemsCounter==9) ListOfAsins+=ele.Asin;
				else ListOfAsins+=ele.Asin+",";
				
				System.out.println(ListOfAsins);
				if (ItemsCounter==9) 
				{
					AmazonApiCall.GetItemCodeFromAmazon(ListOfAsins, "ASIN", List,ItemsCounter+1);
					ListOfAsins = "";
					ItemsCounter=0;
				}else ItemsCounter++;
				}
		}
		
		if (ItemsCounter>0)
		{
			ListOfAsins = ListOfAsins.substring(0, ListOfAsins.length()-1);
			System.out.println("Last amazon call "+ListOfAsins);
			AmazonApiCall.GetItemCodeFromAmazon(ListOfAsins, "ASIN", List,ItemsCounter);
			ListOfAsins = null;
			ItemsCounter=0;
		}
		/*
		System.out.println("Writing to database...");
		for(ListInformation ele:List)
		{
		Db.UpdateCode(ele.EbayId, ele.Code);  
		}
		System.out.println("Writing to database ended...");
		*/
		System.out.println("Getting codes ended...");
	}
	
	public static String BestMatchCode(String EbayId)
	{
	    cred.seteBayToken(eBay_token);
	    apiContext.setApiServerUrl(Server_url);
	    apiContext.setSite(SiteCode);
	    
		GetItemCall Call = new GetItemCall (apiContext);
		Call.setItemID(EbayId);
		Call.setIncludeItemSpecifics(true);
		Call.addDetailLevel(DetailLevelCodeType.RETURN_ALL);
		try{
		ItemType Item = Call.getItem();
	
		for(int i=0;i<Item.getItemSpecifics().getNameValueListLength();i++)
		{
		if (Item.getItemSpecifics().getNameValueList(i).getName().equals("UPC") && !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does Not Apply") && !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does not apply"))
		{
		System.out.println(Item.getItemSpecifics().getNameValueList(i).getName()+" = "+Item.getItemSpecifics().getNameValueList(i).getValue(0));
		return Item.getItemSpecifics().getNameValueList(i).getValue(0);
		}
		}
		
		for(int i=0;i<Item.getItemSpecifics().getNameValueListLength();i++)
		{

		if (Item.getItemSpecifics().getNameValueList(i).getName().equals("ISBN")&& !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does not apply")&& !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does Not Apply"))
		{
		System.out.println(Item.getItemSpecifics().getNameValueList(i).getName()+" = "+Item.getItemSpecifics().getNameValueList(i).getValue(0));
		return  Item.getItemSpecifics().getNameValueList(i).getValue(0);
		}
		}
		
		for(int i=0;i<Item.getItemSpecifics().getNameValueListLength();i++)
		{
		
		if (Item.getItemSpecifics().getNameValueList(i).getName().equals("EAN")&& !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does not apply")&& !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does Not Apply"))
		{
		System.out.println(Item.getItemSpecifics().getNameValueList(i).getName()+" = "+Item.getItemSpecifics().getNameValueList(i).getValue(0));
		return  Item.getItemSpecifics().getNameValueList(i).getValue(0);
		}
		}
		
		
		for(int i=0;i<Item.getItemSpecifics().getNameValueListLength();i++)
		{
		if (Item.getItemSpecifics().getNameValueList(i).getName().equals("SKU")&& !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does not apply")&& !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does Not Apply"))
		{
		System.out.println(Item.getItemSpecifics().getNameValueList(i).getName()+" = "+Item.getItemSpecifics().getNameValueList(i).getValue(0));
		return  Item.getItemSpecifics().getNameValueList(i).getValue(0);
		}}
		
		
		for(int i=0;i<Item.getItemSpecifics().getNameValueListLength();i++)
		{
		if (Item.getItemSpecifics().getNameValueList(i).getName().equals("MPN")&& !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does not apply")&& !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does Not Apply"))
		{
		System.out.println(Item.getItemSpecifics().getNameValueList(i).getName()+" = "+Item.getItemSpecifics().getNameValueList(i).getValue(0));
		return  Item.getItemSpecifics().getNameValueList(i).getValue(0);
		}}
		}catch(Exception e){System.out.println("BestMatchCode error");System.out.println(e);}
		return null;
	}
	
	public void GetAmazonInfoScraper(Store Store) throws InterruptedException
	{
		if(Driver==null)
		{
		options.addArguments("--start-maximized");
		//options.addArguments("--user-data-dir=C:\\User Data2");// remove and login into amaozn with arthur.boim4@gmail.com//
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		Driver = new ChromeDriver(options);
		Driver.get("https://www.amazon.com/");
		Thread.sleep(2000);
		Login(Driver,Store);
		//Driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		}
		int counter = 0;
		System.out.println("Updating amazon price Started...");
		try {
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			 res = statement.executeQuery("SELECT * FROM "+scham+".online where StoreName = '"+Store.Store_name+"';");
			 res.last();
			 counter = res.getRow();
			 res.first();
			while(res.next())
			{
				try{
				Asin = res.getString("amazonasin");
				CurrentAmazonPriceScraper = GetAmazonPriceScraper(Asin,Driver);
				CurrentAmazonTaxScraper = GetAmazonTaxScraper(Asin,Driver);
				Db.UpdateAmazonPriceScraper(Asin,CurrentAmazonPriceScraper);
				Db.UpdateTaxScraper(Asin,CurrentAmazonTaxScraper);
				System.out.println("Asin = "+Asin+" Amazon Price = "+CurrentAmazonPriceScraper+" Tax = "+CurrentAmazonTaxScraper);
				System.out.println("Price scraper counter = "+(counter--));
				System.out.println();
				}catch(Exception e){}
			}
			con = null;
			statement =null;
			Driver.close();
			Driver = null;
			
			System.gc();
		} catch (SQLException e) {System.out.println("GetAmazonInfoScraper Error");}
		
		
	}
	
	public double GetAmazonTaxScraper(String Asin, ChromeDriver Driver2)
	{
		try{									
		String CurrenteTax = null;
		
		CurrenteTax = Driver2.findElement(By.xpath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/p/span/span")).getText();

		
		CurrenteTax = CurrenteTax.substring(CurrenteTax.indexOf("+")+3, CurrenteTax.indexOf("estimated tax")-1);
		
		return Double.parseDouble(CurrenteTax);
		}catch(Exception e){
			System.out.println(e);
			return -1;}
	}
	
	public double GetAmazonPriceScraper(String Asin,ChromeDriver DriverTemp)
	{
		try{
		String CurrentPrice = null;
		Thread.sleep(200);
		DriverTemp.get("https://www.amazon.com/gp/offer-listing/"+Asin+"/ref=olp_f_primeEligible?ie=UTF8&f_new=true&f_primeEligible=true");
		if (DriverTemp.findElementsByXPath("//*[@id='olpOfferList']/div/p").size()>0)
		{
			if (DriverTemp.findElementByXPath("//*[@id='olpOfferList']/div/p").getText().contains("There are currently no listings for this search. Try a different refinement."))
			{
				return -1;
			}
		}
			
		CurrentPrice = DriverTemp.findElementByXPath("//*[@id='olpOfferList']/div/div/div[2]/div[1]").getText();
		CurrentPrice = CurrentPrice.substring(CurrentPrice.indexOf("$")+1, CurrentPrice.indexOf(".")+3);
		return Double.parseDouble(CurrentPrice);
		}catch(Exception e){System.out.println("Error");return -1;}
	}
	
	public double CalcualteBreakEvenLowest(double CurrentAmazonPriceScraper,double CurrentAmazonTaxScraper,double EbayLowestprice)
	{
		if (CurrentAmazonPriceScraper==-1||CurrentAmazonTaxScraper==-1)
			return -1;
		else
		return EbayLowestprice/(CurrentAmazonPriceScraper+CurrentAmazonTaxScraper+0.3);
	}
	
	public void AutodsToolLogin() throws InterruptedException
	{
		Driver.get("https://autodstools.com/active_listings/");
		Thread.sleep(1000);
		if (Driver.findElementsByXPath("//*[@id='email']").size()>0)
		{
		Driver.findElementByXPath("//*[@id='email']").click();
		Driver.findElementByXPath("//*[@id='email']").clear();
		Driver.findElementByXPath("//*[@id='email']").sendKeys("arthur.boim@gmail.com");
		Driver.findElementByXPath("//*[@id='password']").click();
		Driver.findElementByXPath("//*[@id='password']").clear();
		Driver.findElementByXPath("//*[@id='password']").sendKeys("b0104196");
		Driver.findElementByXPath("//*[@id='form_login']/div[4]/button").click();
		}
	}
	
	public int  PriceChange(String Asin, double ebaylowestprice ) throws InterruptedException, SQLException
	{
		try{
		
		if (Driver.findElementsByXPath("//*[@id='products_table']/tbody/tr/td").size()>0)
		{
			if (Driver.findElementByXPath("//*[@id='products_table']/tbody/tr/td").getText()=="No data available in table")
			{
				Db.UpdateRemoveItemFlag(Asin, 1);
				return 1;
			}
		}}catch(Exception e){}
		try{
		String new_additional_percentage = null;
		double new_additional_percentage_Double = -1;

		while(Driver.findElementsByXPath("//*[@id='products_table_filter']/label/input").size()<1);
		Thread.sleep(1000);
		Driver.findElementByXPath("//*[@id='products_table_filter']/label/input").click();
		Driver.findElementByXPath("//*[@id='products_table_filter']/label/input").clear();
		Driver.findElementByXPath("//*[@id='products_table_filter']/label/input").sendKeys(Asin);
		//while(Driver.findElementsByXPath("//*[@id='products_table']/tbody/tr/td[2]/span/img").size()<1);
		Thread.sleep(4000); 	    
		Driver.findElementByXPath("//*[@id='products_table']/tbody/tr/td[2]/span/img").click();
		//while(Driver.findElementsByXPath("//*[@id='new_break_even']").size()<1);
		Thread.sleep(3000);
		//bestmatch optimizer //
		if (ebaylowestprice==(double)-1&&Db.CheckBestMatchOptimizedFlag(Asin)==0) // check in the Db of i can change it //
		{
			return BestMatchOptimizer(Asin);
		}
		if (ebaylowestprice==(double)-1&&Db.CheckBestMatchOptimizedFlag(Asin)==1) return 1;
		Driver.findElementByXPath("//*[@id='new_break_even']").clear();
		Driver.findElementByXPath("//*[@id='new_break_even']").sendKeys(""+13.1);
		Driver.findElementByXPath("//*[@id='item_sell_price']").clear();
		Driver.findElementByXPath("//*[@id='item_sell_price']").sendKeys(""+(ebaylowestprice));
		new_additional_percentage = Driver.findElementByXPath("//*[@id='new_additional_percentage']").getAttribute("value");
		new_additional_percentage_Double = Double.parseDouble(new_additional_percentage);
		System.out.println("new_additional_percentage = "+new_additional_percentage);
		
		if (new_additional_percentage.contains("-")||new_additional_percentage_Double<2||new_additional_percentage_Double>40) 
			{

			if (Driver.findElementsByXPath("//*[@id='modal-change_active_product_details']/div/div/div[3]/button[4]").size()>0)
			{
				Driver.findElementByXPath("//*[@id='modal-change_active_product_details']/div/div/div[3]/button[4]").click();
				System.out.println("low profit percent or negative");
				Driver.navigate().refresh();
				return -1;
			}

			}
		else {
			Driver.findElementByXPath("//*[@id='modal-change_active_product_details']/div/div/div[3]/button[6]").click();
			Thread.sleep(2000);
			Driver.findElementByXPath("/html/body/div[11]/div[7]/div/button").click();
			Db.UpdateBreakeven(Asin,13.1);

			Db.ProfitPersent(Asin,Double.parseDouble(new_additional_percentage));
		}
		Thread.sleep(8000);
		
		return 1;
}catch(Exception e){return 1;}
	}

	public int  ProfitPercentChange(String Asin, double ProfitPercent ) throws InterruptedException, SQLException
	{
		try{

		while(Driver.findElementsByXPath("//*[@id='products_table_filter']/label/input").size()<1);
		Thread.sleep(1000);

		Driver.findElementByXPath("//*[@id='products_table_filter']/label/input").click();
		Driver.findElementByXPath("//*[@id='products_table_filter']/label/input").clear();
		Driver.findElementByXPath("//*[@id='products_table_filter']/label/input").sendKeys(Asin);

		while(Driver.findElementsByXPath("//*[@id='products_table_wrapper']/div[5]/div[1]/div").size()<1);
		Thread.sleep(2000); 	    
		if (Driver.findElementByXPath("//*[@id='products_table_wrapper']/div[5]/div[1]/div").getText().contains("Showing 0 to 0 of 0 entries")) 
		{
		Db.UpdateRemoveItemFlag(Asin, 1);
		return -1;
		}
		Driver.findElementByXPath("//*[@id='products_table']/tbody/tr/td[2]/span/img").click();
		while(Driver.findElementsByXPath("//*[@id='new_break_even']").size()<1);
		Thread.sleep(1000);
		Driver.findElementByXPath("//*[@id='new_break_even']").clear();
		Driver.findElementByXPath("//*[@id='new_break_even']").sendKeys(""+13.1);
		Driver.findElementByXPath("//*[@id='new_additional_percentage']").clear();
		Driver.findElementByXPath("//*[@id='new_additional_percentage']").sendKeys(""+ProfitPercent);
	    Driver.findElementByXPath("//*[@id='modal-change_active_product_details']/div/div/div[3]/button[6]").click();
		Thread.sleep(2000);
		Driver.findElementByXPath("/html/body/div[11]/div[7]/div/button").click();
		Db.UpdateBreakeven(Asin,13.1);
		Db.ProfitPersent(Asin,ProfitPercent);
		Thread.sleep(8000);
		return 1;}catch(Exception e){}
		return -1;
	}

	public int BestMatchOptimizer(String Asin) throws InterruptedException, SQLException
	{
		/*
		if (Driver.findElementByXPath("//*[@id='new_additional_percentage']").getAttribute("value").equals("0")) 
			{
			Driver.findElementByXPath("//*[@id='modal-change_active_product_details']/div/div/div[3]/button[4]").click();
			System.out.println("low profit percent or negative");
			Driver.navigate().refresh();
			return 1;
			}
		*/
		try{
		String s = "";
		s= Driver.findElementByXPath("//*[@id='new_additional_percentage']").getAttribute("value");
		double temp = Double.parseDouble(s);
		temp +=3;
		Driver.findElementByXPath("//*[@id='new_additional_percentage']").clear();
		Driver.findElementByXPath("//*[@id='new_additional_percentage']").sendKeys(""+temp);
		Driver.findElementByXPath("//*[@id='modal-change_active_product_details']/div/div/div[3]/button[6]").click();
		Thread.sleep(2000);
		Driver.findElementByXPath("/html/body/div[11]/div[7]/div/button").click();
		Db.UpdateBreakeven(Asin,13.1);
		Db.ProfitPersent(Asin,temp);
		Db.UpdateBestmatchFlag(Asin,1);
		return 1;
		}catch(Exception e){
			Db.UpdateBestmatchFlag(Asin,0);
			return -1;}
	}

	@Override
	public void run() {
		
		try {
			
			if (OrdersGui.StoreName.equals("All"))
			{
			for (Store ele:Main.Main.ListOfStores)
			{
			ItemPositionMain(ele);
			}
			}else
			{
				ItemPositionMain(Main.Main.GetStoreByName(OrdersGui.StoreName));
			}
			
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}

	public void ItemPositionCheckBestMatch() {

	    new Thread() {
	        
			public void run() {
				System.out.println("Item position check best match START...");
				ItemPositionList(Code,"BestMatch", list);
				Lock++;
				System.out.println("Lock = "+Lock);
				System.out.println("Item position check best match End...");
			}
	    }.start();
	}
	
	public void ItemPositionCheckLowestPrice() {

	    new Thread() {
	        
			public void run() {
				System.out.println("Item position check lowest price START...");
				ItemPositionList(Code,"LowestPrice", list);
				Lock++;
				System.out.println("Lock = "+Lock);
				System.out.println("Item position check lowest price End...");
			}
	    }.start();
	}
	
	public void CalculateData(Store Store)
	{
		System.out.println("Calculate data is running....");
		try {
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			 res = statement.executeQuery("SELECT * FROM "+scham+".online where Storename = '"+Store.Store_name+"';");
			while(res.next())
			{
				try{
				Asin = res.getString("amazonasin");
				CurrentAmazonPriceScraper =Db.CurrentAmazonPrice(Asin);
				CurrentAmazonTaxScraper =Db.CurrentAmazonTax(Asin);
				EbayLowestprice = Db.GeteBayLowestLowest(Asin);
				BreakEvenLowestForPrice = CalcualteBreakEvenLowest(CurrentAmazonPriceScraper,CurrentAmazonTaxScraper,EbayLowestprice);
				Db.UpdateCalcualteBreakEvenLowest(Asin,BreakEvenLowestForPrice);
				}catch(Exception e){System.out.println(e);}
			}
			con = null;
			statement =null;
			System.gc();
		} catch (SQLException e) {System.out.println("CalculateData Error");}
		
		System.out.println("Calculate Data ended...");
	
	}
	
	public void CheckSoldItems(Store Store) throws SQLException // This is Includes the writing to Db //
	{

				ArrayList<ListInformation> listCheckSoldItems = new ArrayList<ListInformation>();
				DatabasePriceChanger Db = new DatabasePriceChanger();
				try {

					Db.GetOnlineItemsInfo(listCheckSoldItems,Store.Store_name);
					
				} catch (SQLException e) {
					
					e.printStackTrace();
				} //get all items from DB //
				try {
					GetSoldAmountLastDays(listCheckSoldItems,30);
				} catch (ApiException e) {
					
					e.printStackTrace();
				} catch (SdkException e) {
					
					e.printStackTrace();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				listCheckSoldItems.removeAll(listCheckSoldItems);
	}
	
	public void CheckItemsPosition(Store Store) throws SQLException , InterruptedException, ParserConfigurationException, SAXException, IOException
	{

				DatabasePriceChanger Db = new DatabasePriceChanger();
				try {
					
					Db.GetOnlineItemsWithOutCode(list,Store.Store_name);
				} catch (SQLException e) {
					e.printStackTrace();
				} 
				try {
					BestMatchCodeList(list);
					list.removeAll(list);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				Db.GetOnlineItemsInfo(list,Store.Store_name);
				ItemPositionCheckBestMatch();
				ItemPositionCheckLowestPrice();
				while(Lock<2);
				Lock = 0;
				try {
					Db.SetListInfoToDb(list);
				} catch (SQLException e) {
					e.printStackTrace();
				}

	}
	
	public void CheckAmazonPriceAndTax(Store Store) throws InterruptedException // read and write //
	{
		GetAmazonInfoScraper(Store);
	}
	
	public void PriceChange(Store Stroe) throws SQLException, InterruptedException 
	{
		DatabasePriceChanger Db = new DatabasePriceChanger();
		ArrayList<ListInformation> listPriceChange = new ArrayList<ListInformation>();
		Db.GetOnlineItemsToUpdate(listPriceChange); 
		Db.GetOnlineItemsToUpdate1(listPriceChange); 
		Db.GetOnlineItemsToUpdate2(listPriceChange); 
		Set<ListInformation> hs = new HashSet<>();
		hs.addAll(listPriceChange);
		listPriceChange.clear();
		listPriceChange.addAll(hs);
		hs = null;
		PriceChangerConditionYaballe(listPriceChange,Stroe);
		listPriceChange.removeAll(listPriceChange);
	}
	
	public int  Login(ChromeDriver Driver,Store store) throws InterruptedException
	{	
		Thread.sleep(2000);
		Driver.findElementByXPath("//*[@id='nav-link-accountList']/span[1]").click();
		Thread.sleep(2000);
		SetLogin(Driver,store);
		SetPass(Driver,store);
		return 0;
	}
	
	public void  SetLogin(ChromeDriver Driver,Store store)
	{
		try{
		if (Driver.findElementsByXPath("//*[@id='ap_email']").size()>0)
		{
			Driver.findElementByXPath("//*[@id='ap_email']").click();
			Driver.findElementByXPath("//*[@id='ap_email']").clear();
			Driver.findElementByXPath("//*[@id='ap_email']").sendKeys(store.AmazonUser);
			if (Driver.findElementsByXPath("//*[@id='continue']").size()>0)
			Driver.findElementByXPath("//*[@id='continue']").click();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	    }catch(Exception e){System.out.println(e);}
	}
	
	public void  SetPass(ChromeDriver Driver,Store store)
	{
		try{
		if (Driver.findElementsByXPath("//*[@id='ap_password']").size()>0)
		{
			Driver.findElementByXPath("//*[@id='ap_password']").click();
			Driver.findElementByXPath("//*[@id='ap_password']").sendKeys(store.AmazonPassword);
			if (Driver.findElementsByXPath("//*[@id='signInSubmit']").size()>0) 
			Driver.findElementByXPath("//*[@id='signInSubmit']").click();
		}
		}catch(Exception e){System.out.println(e);}
	}
	

}
