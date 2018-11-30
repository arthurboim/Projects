package ExternalMonitor;


import java.util.ArrayList;
import org.openqa.selenium.By;
import DataBase.SQLDataBase;
import Item.Item;
import WebDriver.SelenumWebDriver;

public class Yaballe implements ExternalMonitorInterface
{
	private static final int	ItemsPerPage	= 50;
	private static int			LoadedItems		= 0;
	private static int			ItemsNumber		= 0;
	
	private SelenumWebDriver	Driver;
	private SQLDataBase			DB;
	private String				userName;
	private String				password;
	private ArrayList<Item>		ItemsListFromWeb;
	private ArrayList<Item>		ItemsListFromDb;
	
	/* Constractor */
	
	public Yaballe()
	{
		Driver = new SelenumWebDriver();
		DB = new SQLDataBase();
		ItemsListFromWeb	= new ArrayList<Item>();
		ItemsListFromDb		= new ArrayList<Item>();
		userName = "arthur.boim@gmail.com";
		password = "b0104196";
	}


	/* Public */
	
	@Override
	public  void GetItemsList()
	{
		int pagesNumber =0;
		
		// Login
		YaballeLogin();
		
		// Check if get
		pagesNumber = YaballeGetPagesNumber();
		if (-1 == pagesNumber)
		{
			Driver.CloseWebDriver();
			return;
		}
		
		for(int i=1; i <= pagesNumber; i++)
		{
			GetItemsFromPage();
			while(NextPage(i)==0);
		}
		
		Driver.CloseWebDriver();
		
		DB.GetOnlineItems(ItemsListFromDb);
		CompareItems();
		AddAndRemoveItems();
	}
	
	
	
	
	
	
	/* Private */
	
	private int NextPage(int i)
	{
		try{
			
		if (LoadedItems==ItemsNumber)
		{
			return 1;
		}
		
			Driver.Click("//a[@ng-switch-when='next']", null);
			Driver.wait(4);	
		
			while (Driver.GetText("//*[@id='page-content']/div/div/div/div/div[2]/div[2]", null).length() < "Items: ".length());
		
		}catch(Exception e)
		{
			System.out.println(e);
			return 0;
		}
		
		return 1;
	}
	
	private void YaballeLogin()
	{
		try
		{
			Driver.OpenLink("https://yaballe.com");
			Driver.wait(4);
			Driver.Click("//*[@id='page-header-inner']/div/div/div/div/div[3]/div[2]/a[1]", null);
			Driver.wait(2);
			Driver.Click("/html/body/div[1]/ul/li[2]/a", null);
			Driver.wait(2);
			Driver.Click("//*[@id='login_email']", null);
			Driver.GetDriver().findElement(By.xpath("//*[@id='login_email']")).clear();
			Driver.SetText("//*[@id='login_email']", null, userName);
			Driver.Click("//*[@id='password']", null);
			Driver.GetDriver().findElement(By.xpath("//*[@id='password']")).clear();
			Driver.SetText("//*[@id='password']", null, password);
			Driver.Click("//*[@id='login_site']", null);
			Driver.wait(4);
			Driver.Click("//*[@id='sidebar-menu']/li[2]/a", null);
			
		} catch (Exception e)
		{
			System.out.println(e);
			Driver.CloseWebDriver();
		}
	}

	private int YaballeGetPagesNumber()
	{
		String tempStr = null;
		int PagesNumber = -1;
		try
		{
			Driver.wait(2);
			tempStr = Driver.GetText("//*[@id='page-content']/div/div/div/div/div[2]/div[2]", null);
			tempStr = tempStr.substring(tempStr.indexOf("Items: ") + "Items: ".length(), tempStr.indexOf("/") - 1);
			ItemsNumber = Integer.parseInt(tempStr);
			PagesNumber = (ItemsNumber / ItemsPerPage) + 1;
		} catch (Exception e)
		{
			PagesNumber = -1;
		}
		
		return PagesNumber;
	}

	private void GetItemsFromPage()
	{
		try{		
			
		for(int i=0;i<ItemsPerPage && LoadedItems < ItemsNumber;i++)
		{
			if (LoadedItems == ItemsNumber)
			{
				break;         
			}
			
			Item newItem = new Item();
			newItem.setCurrentPrice(Double.parseDouble(Driver.GetText("//*[@id='page-content']/div/div/md-content/div/table/tbody/tr["+(i+1)+"]/td[3]/div/div[2]",null).replace("$", "")));
			newItem.setSupplierCode(Driver.GetText("//*[@id='page-content']/div/div/md-content/div/table/tbody/tr["+(i+1)+"]/td[3]/div/div[1]/a", null));
			newItem.setMarketPlaceCode(Driver.GetText("//*[@id='page-content']/div/div/md-content/div/table/tbody/tr["+(i+1)+"]/td[4]/div[1]/a", null));
			newItem.setCurrentMarketPlacePrice(Double.parseDouble(Driver.GetText("//*[@id='page-content']/div/div/md-content/div/table/tbody/tr["+(i+1)+"]/td[8]/a",null).substring(1)));
			newItem.setMyProfitPercent(Double.parseDouble(Driver.GetText("//*[@id='page-content']/div/div/md-content/div/table/tbody/tr["+(i+1)+"]/td[7]/a",null)));
			ItemsListFromWeb.add(newItem);
			LoadedItems++;
			//
		}
		} catch (Exception e)
		{
			
		}
	}

	private void CompareItems()
	{
		for(Item ele:ItemsListFromWeb)
		{
			for(Item ele1:ItemsListFromDb)
			{
				if (ele.getMarketPlaceCode().trim().equals(ele1.getMarketPlaceCode().trim()))
				{
					ele.setArbitraje(100);
					ele1.setArbitraje(100);
				}
			}
		}
		
	}
	
	private void AddAndRemoveItems()
	{
		for(Item ele:ItemsListFromWeb)
		{
			if (ele.getArbitraje() != 100)
			{
				DB.InsertItem(ele);
			}
		}
		
		for(Item ele:ItemsListFromDb)
		{
			if (ele.getArbitraje() != 100)
			{
				DB.RemoveItem(ele);				
			}
		}
	}
	
}
