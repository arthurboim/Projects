package MainPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import org.apache.commons.lang3.StringUtils;
import com.ebay.sdk.ApiContext;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ItemFilter;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.SearchItem;
import com.ebay.services.finding.SortOrderType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;


public class EbaySearch {
	public  String FILENAME = "C:\\keys\\ConfigFile-Keys.txt";
	public  ApiContext apiContext = new ApiContext();
	public  String eBay_token = null;
	public  String Server_url = null;
	public  SiteCodeType SiteCode = null;
	public  ChromeDriver driver;
	public  int driverCounter;
	
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

		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
	    driver = new ChromeDriver();
	    driver.get("https://www.ebay.com/");
	    driverCounter =0;
	}

	public void Finditem (List<product> ItemsList) throws IOException
	{
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
//	        try
//	        {
//	        	
//	        	if (true == IsCharacterExist(ele.ASIN))
//	        	{
//	        		request.setKeywords(ele.getBestresult());	            		
//	        	}else
//	        	{
//	        		request.setKeywords(ele.ASIN);
//	        	}
//	        	
//	        	FindItemsAdvancedResponse result = serviceClient.findItemsAdvanced(request);
//	        	ele.ebayResults = result.getSearchResult().getCount();
//	        	if(0 == ele.ebayResults)
//	        	{
//	        		continue;
//	        	}
//	        	
//	            List<SearchItem> items = result.getSearchResult().getItem();
//	            int counter=1;
//	            
//	            ele.ebayLowestPrice = (double)(items.get(0).getSellingStatus().getCurrentPrice().getValue()+items.get(0).getShippingInfo().getShippingServiceCost().getValue());
//	            ele.arbitraje 		= (ele.ebayLowestPrice + ele.Ebay_shipping) - ScrapInfo.Min_price_to_sale(ele.price);
//	            
//	            for(SearchItem item : items) 
//	            {
//		          try{
//	            	if (item.getSellingStatus().getCurrentPrice().getValue() +item.getShippingInfo().getShippingServiceCost().getValue() > ScrapInfo.Min_price_to_sale(ele.price) && item != null )
//	            	{
//	            		ele.PlaceInlowestprice = counter;
//	            		break;
//	            	}
//		          }catch (Exception e){}
//		           counter++;
//		        }
//	        }
//	        catch(Exception ex)
//	        {
	        	try{
	        		FinditemByWebDriver(ele);
	        	}catch(Exception ex1)
	        	{
		        	System.out.println(ex1.getMessage());
		        	System.out.println("Ebay Call fail api and webdriver");
		        	System.out.println("Item code = "+ele.ASIN);
		        	ele.ebayResults = -1;
		        	restDriver();
		        	System.out.println("Driver reset");
	        	}
//	        }
//	        
//	        if (ele.price>0)
//	        {
//	        	ele.Breakevenlowestprice = ele.ebayLowestPrice/(ele.price+0.3);
//	        }
//	
//	        if (ele.ebayResults>0)
//	        {
//	        	ele.Sale_true = (ele.sold/ele.ebayResults)*100;
//	        }
	      }
        
        

    }

	public static boolean IsCharacterExist(String str)
	{
		Pattern pattern = Pattern.compile(".*[a-zA-Z]+.*");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches())
		{
			return true;
		}else
		{
			return false;
		}
	}
	
	public static boolean IsContainsXinTheEnd(String str)
	{
		int count = StringUtils.countMatches(str, "X");
		if (1 == count && str.indexOf("X") == str.length()-1)
		{
			return true;
		}else
		{
			return false;
		}
	}
	
	public static int AmountOfCharsInString(String str)
	{
		int charCount = 0;
		char temp;

		for( int i = 0; i < str.length( ); i++ )
		{
		    temp = str.charAt( i );

		    if( Character.isLetter(temp))
		        charCount++;
		}
		
		return charCount;
	}
	
	public void FinditemByWebDriver(product productToCheck)
	{
		String Keyword = null;
		driverCounter++;
		
		if (driverCounter > 300)
		{
			restDriver();
		}
		
		if (true  == IsCharacterExist(productToCheck.ASIN))
		{	
			if (true  == IsContainsXinTheEnd(productToCheck.ASIN) &&
				1	  == AmountOfCharsInString(productToCheck.ASIN))
			{
				Keyword = productToCheck.ASIN;
			}else
			{
				Keyword = productToCheck.getBestresult();				
			}
		}else
		{
			Keyword = productToCheck.ASIN;
		}
    	driver.findElement(By.id("gh-ac")).click();
    	driver.findElement(By.id("gh-ac")).clear();
    	driver.findElement(By.id("gh-ac")).sendKeys(Keyword);
    	driver.findElement(By.id("gh-btn")).click();

		try{
    		driver.findElement(By.id("e1-20")).click();
	 	}catch(Exception e)
    	{
	 		
    	}
    	
    	try{
    		driver.findElement(By.cssSelector("a[class='small cbx btn btn-s btn-ter tab tgl_button last_b']")).click();
    	}catch(Exception e)
    	{
    		
    	}
    	
    	try{
    		productToCheck.ebayResults = Integer.parseInt(driver.findElement(By.className("rcnt")).getText());
    	}catch(Exception e)
    	{
    		try{
    			String s = driver.findElement(By.className("srp-controls__count-heading")).getText();
    			s = s.substring(0, s.indexOf("result"));
    			s = s.trim();
        		productToCheck.ebayResults = Integer.parseInt(s);
    		}
    		catch(Exception e1)
    		{
    			System.out.println("Parsing error 1");
    			productToCheck.ebayResults = -1; 
    			restDriver();
    		}
    	}
    	
    	
    	if(0 == productToCheck.ebayResults)
    	{
    		return;
    	}
    
    	
	}
	

	public void restDriver()
	{
		driver.close();
		driver.quit();
	    driver = new ChromeDriver();
	    driver.get("https://www.ebay.com/");
	    driverCounter =0;
	}
	
	
	
	
	public  double Min_price_to_sale(double price) {
		double ebay_fees = 0.1;
		double paypal_fees = 0.044;
		double paypal_fixed = 0.3;
		double my_percent = 1.13;
		return ((price * my_percent) + paypal_fixed) / (1 - ebay_fees - paypal_fees);
	}
	
	public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

	
	
	
	// Destractor
	@Override
	protected void finalize() throws Throwable {
	
		driver.close();
		driver.quit();
	}
	

}
