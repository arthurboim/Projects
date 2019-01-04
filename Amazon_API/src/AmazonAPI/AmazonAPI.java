package AmazonAPI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import Config.Config;
import Item.Item;
import WebDriver.SelenumWebDriver;

public class AmazonAPI 
{
	
	static int			numberOfItemsInCall	= 9;
	private static int	NItemsInstock		= 10;
	
	enum CallStatus
	{
		SUCCESS, FAIL;
	}
	
	static enum StockStatusEnum
	{
		InStock, NotInStock, OnlyNLeftOrderSoon, OnlyNLeftOrderMoreOntheWay, UsuallyShipsWithinN, AvailableToShip, AvailableFromTheseSellers, OOS, PreOrder, Unknown
	};
	
	private SelenumWebDriver	Driver;
	private String				AmazonUser;
	private String				AmazonPass;
	
	ArrayList<Item>				ItemsToUpdate;

	
	/* Contractors */
	
	public AmazonAPI()
	{
		try
		{
			ReadFileConfigurations(null);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public AmazonAPI(String KeysFilePath)
	{
		try
		{
			ReadFileConfigurations(KeysFilePath);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void ReadFileConfigurations(String KeysFilePath) throws IOException
	{
		BufferedReader br = null;
		FileReader fr = null;
		try
		{
			
			if (null == KeysFilePath)
			{
				fr = new FileReader(Config.KeysFilePath);
			}
			else
			{
				fr = new FileReader(KeysFilePath);
			}
			
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null)
			{
				if (sCurrentLine.contains("Acid: "))
				{
					AmazonUser = sCurrentLine.substring(sCurrentLine.indexOf("Acid: ") + "Acid: ".length());
				}
				
				if (sCurrentLine.contains("APass: "))
				{
					AmazonPass = sCurrentLine.substring(sCurrentLine.indexOf("APass: ") + "APass: ".length());
				}
				
			}
		} catch (IOException e)
		{
			e.printStackTrace();
			throw e;
		} finally
		{
			
			try
			{
				
				if (br != null)
					br.close();
				
				if (fr != null)
					fr.close();
				
			} catch (IOException ex)
			{
				
				ex.printStackTrace();
				throw ex;
			}
		}
	}
	
	/* Public functions */

	public void UpDateTrackingNumber(ArrayList<Item> ItemsToUpdate)
	{
		Driver = new SelenumWebDriver();
		Login(Driver);
		UpdateItemsTrackingNumber(ItemsToUpdate);
		Driver.CloseWebDriver();
	}
	
	public void UpdateItemViaWebSelenum(List<Item> listOfItems)
	{
		Driver = new SelenumWebDriver();
		
		Login(Driver);
		
		for (Item ele : listOfItems)
		{
			Driver.wait(1);
			Driver.OpenLink("https://www.amazon.com/dp/" + ele.getSupplierCode() + "/");
			
			 ele.setInStock(GetInstock(Driver,ele));
			Driver.wait(1);
			Driver.OpenLink("https://www.amazon.com/gp/offer-listing/" + ele.getSupplierCode()
					+ "/ref=olp_f_new?ie=UTF8&f_all=true&f_new=true&f_primeEligible=true");
			ele.setCurrentTax(GetTax(Driver));
			ele.setCurrentPrice(GetPrice(Driver));
			
			if (ele.getCurrentTax() == -1 || ele.getCurrentPrice() == -1)
			{
				ele.setInStock(false);
			}
			else
			{
				ele.setInStock(true);
				
			}
		}
		
		Driver.CloseWebDriver();
		try {
			Runtime.getRuntime().exec("taskkill /IM chrome.exe /F");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 
	
	
	
	
	
	
	/* Private functions */
	
	private void Login(SelenumWebDriver Driver)
	{
		/* Nevigate to login */
		Driver.OpenLink("https://www.amazon.com/");
		Driver.wait(3);
		
		Driver.Click(null, "nav-link-accountList");
		Driver.wait(3);
		
		/* Set user name */
		Driver.Click("//*[@id='ap_email']", null);
		Driver.GetDriver().findElementByXPath("//*[@id='ap_email']").clear();
		Driver.SetText("//*[@id='ap_email']", null, AmazonUser);
		Driver.Click("//*[@id='continue']", null);
		Driver.wait(3);
		
		/* Set password */
		Driver.Click("//*[@id='ap_password']", null);
		Driver.SetText("//*[@id='ap_password']", null, AmazonPass);
		Driver.Click("//*[@id='signInSubmit']", null);
		Driver.wait(3);
	}
	
	
	/* Item update info update */

	private boolean GetInstock(SelenumWebDriver Driver, Item ele)
	{
		IsPrime(Driver, ele);
		List<WebElement> Webelements = Driver.GetDriver().findElements(By.id("availability"));
		for (WebElement ele1 : Webelements)
		{
			if (InStockFilter(ele1.getText()) == true)
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean InStockFilter(String text)
	{
		StockStatusEnum StockStatus = null;
		StockStatus = StockStatusEnum.Unknown;
		boolean status = false;
		int N = -1;
		
		if (text.contains("In Stock"))
		{
			StockStatus = StockStatusEnum.InStock;
		}
		else if (text.contains("Only") && text.contains("left in stock") && text.contains("more on the way"))
		{
			StockStatus = StockStatusEnum.OnlyNLeftOrderMoreOntheWay;
		}
		else if (text.contains("Only") && text.contains("left in stock") && text.contains("order soon"))
		{
			StockStatus = StockStatusEnum.OnlyNLeftOrderSoon;
		}
		else if (text.contains("Usually ships within"))
		{
			StockStatus = StockStatusEnum.UsuallyShipsWithinN;
		}
		else if (text.contains("Available to ship in"))
		{
			StockStatus = StockStatusEnum.AvailableToShip;
		}
		else if (text.contains("Available from these sellers"))
		{
			StockStatus = StockStatusEnum.AvailableFromTheseSellers;
		}
		else if (text.contains("Temporarily out of stock"))
		{
			StockStatus = StockStatusEnum.OOS;
		}
		else if (text.contains("This title will be released on"))
		{
			StockStatus = StockStatusEnum.PreOrder;
		}
		else if (text.contains("Currently unavailable"))
		{
			StockStatus = StockStatusEnum.OOS;
		}
		
		if (StockStatusEnum.Unknown == StockStatus)
		{
			System.out.println("Parsing failes...");
			System.out.println("Text = \n" + text);
		}
		
		switch (StockStatus)
		{
		case InStock:
			status = true;
			break;
		
		case OnlyNLeftOrderMoreOntheWay:
			text = text.substring(text.indexOf("Only ") + "Only ".length(),
					text.indexOf(" left in stock (more on the way)."));
			N = Integer.parseInt(text);
			if (N < NItemsInstock)
			{
				status = false;
			}
			else
			{
				status = true;
			}
			break;
		
		case OnlyNLeftOrderSoon:
			text = text.substring(text.indexOf("Only ") + "Only ".length(),
					text.indexOf(" left in stock - order soon."));
			N = Integer.parseInt(text);
			if (N < NItemsInstock)
			{
				status = false;
			}
			else
			{
				status = true;
			}
			break;
		
		case UsuallyShipsWithinN:
			status = false;
			break;
		
		case NotInStock:
			status = false;
			break;
		
		case AvailableToShip:
			status = false;
			break;
		case AvailableFromTheseSellers:
			status = false;
			break;
		case OOS:
			status = false;
			break;
		case PreOrder:
			status = false;
			break;
		case Unknown:
			status = false;
			break;
		}
		
		return status;
	}
	
	private boolean IsPrime(SelenumWebDriver Driver, Item ele)
	{
		boolean status = false;
		List<WebElement> Webelements = Driver.GetDriver().findElements(By.xpath("//*[@id='priceBadging_feature_div']/i"));
		if (Webelements.size() > 0)
		{
			if (Driver.GetDriver().findElement(By.xpath("//*[@id='priceBadging_feature_div']/i")).getAttribute("class")
					.equals("a-icon a-icon-prime"))
			{
				status = true;
			}
			else
			{
				status = false;
			}
		}
		ele.setPrime(status);
		
		return status;
	}
	
	private double GetPrice(SelenumWebDriver driver)
	{
		try
		{
			return Double.parseDouble(
					driver.GetDriver().findElementByXPath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/span[1]")
							.getText().replace("$", ""));
		} catch (Exception e)
		{
			return -1;
		}
	}
	
	private double GetTax(SelenumWebDriver driver)
	{
		try
		{
			String CurrenteTax = driver.GetDriver()
					.findElement(By.xpath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/p/span/span")).getText();
			CurrenteTax = CurrenteTax.substring(CurrenteTax.indexOf("+") + 3, CurrenteTax.indexOf("estimated tax") - 1);
			return Double.parseDouble(CurrenteTax);
		} catch (Exception e)
		{
			return -1;
		}
	}
	
	private String GetTitle(SelenumWebDriver Driver)
	{
		List<WebElement> Webelements = new ArrayList<WebElement>();
		
		Webelements = Driver.GetDriver().findElements(By.cssSelector("span[id='productTitle']"));
		for (WebElement ele : Webelements)
		{
			if (ele.getAttribute("id").equals("productTitle"))
			{
				return ele.getText();
			}
		}
		
		return null;
	}
	
	
	
	/* Tracking number update */
	
	private void UpdateItemsTrackingNumber(List<Item> ItemsToUpdate)
	{
		for (Item ele : ItemsToUpdate)
		{
			GetNextItemTrackingAndCarrier(ele);
		}
	}

	private void GetNextItemTrackingAndCarrier(Item itemToUpdate)
	{
		String Text;
		Driver.OpenLink("https://www.amazon.com/gp/your-account/order-history/ref=oh_aui_search?opt=ab&search="
				+ itemToUpdate.getOrderNumber());
		Driver.wait(2);
		Text = Driver.GetText(null, "ordersContainer");
		if (Text.contains("No results found. Please try another search."))
		{
			// Go next
		}
		else
		{
			Driver.wait(2);
			Driver.Click("//*[@id='a-autoid-8-announce']", "a-autoid-8-announce");
			ParseText(itemToUpdate);
		}
	}
	
	private void ParseText(Item itemToUpdate)
	{
		String Text;
		String Carrier;
		String Tracking;
		
		Driver.wait(2);
		try{
		Text = Driver.GetText("//*[@id='carrierRelatedInfo-container']", "carrierRelatedInfo-container");
		if (Text.contains("Shipped with"))
		{
			Text = Text.replace("\n", " ");
			Carrier = Text.substring(Text.indexOf("Shipped with") + "Shipped with".length() + 1,
					Text.indexOf("Tracking ID"));
			Tracking = Text.substring(Text.indexOf("Tracking ID") + "Tracking ID".length() + 1);
			Tracking = Tracking.replace(" ", "");
			itemToUpdate.setCarrier(Carrier);
			itemToUpdate.setTracking(Tracking);
			System.out.println("Carrier	 : " + itemToUpdate.getCarrier());
			System.out.println("Tracking : " + itemToUpdate.getTracking());
		}
		}catch(Exception e)
		{
			itemToUpdate.setCarrier(null);
			itemToUpdate.setTracking(null);
			System.out.println("Carrier	 : " + itemToUpdate.getCarrier());
			System.out.println("Tracking : " + itemToUpdate.getTracking());
		}
	}
	
}
