package ExternalMonitor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebElement;

import DataBase.IDataBase;
import DataBase.SQLDataBase;
import Item.Item;
import WebDriver.SelenumWebDriver;

public class ShopMaster implements ExternalMonitorInterface
{
	
	SelenumWebDriver	Driver;
	SQLDataBase			DB;
	String				userName;
	String				password;
	int					pageSize;
	ArrayList<Item>		ItemsListFromWeb;
	ArrayList<Item>		ItemsListFromDb;
	
	/* Constractor */
	
	public ShopMaster()
	{
		Driver = new SelenumWebDriver();
		DB = new SQLDataBase();
		ItemsListFromWeb	= new ArrayList<Item>();
		ItemsListFromDb		= new ArrayList<Item>();
		userName = "arthur.boim@gmail.com";
		password = "b0104196";
		pageSize = 300;
	}
	
	/* Public */
	
	@Override
	public void GetItemsList()
	{
		// Enter site 
		Driver.OpenLink("https://www.shopmaster.com/");
		Login();
		
		// Nevigate 
		Driver.OpenLink("https://www.shopmaster.com/ebay/product/index.htm?curSelect=ebayActive&state=active");
		Driver.Click("//*[@id='downPage']/li[1]/a/select", null);
		Driver.Click("//*[@id='downPage']/li[1]/a/select/option[4]", null);

		// Get the items
		GetItems();
		Driver.CloseWebDriver();
		CompareItems();
		AddAndRemoveItems();
		
	}
	
	/* Private */
	
	private void Login()
	{
		Driver.SetText("//*[@id='username']", "username", userName);
		Driver.SetText("//*[@id='password']", "password", password);
		Driver.Click("//*[@id='login']/button", null);
	}
	
	private void GetItems()
	{
		String text = null;
		String XpathForAmazon = null;
		String XpathForEbay = null;
		int numberOfItems = 0;
		List<WebElement> listOfElements;

		Driver.wait(5);
		DB.GetOnlineItems(ItemsListFromDb);
		
		text = Driver.GetText("//*[@id='leftNav']/ul/li[2]/ul/li[1]/p/span[2]", null);
		numberOfItems = Integer.parseInt(text);
		listOfElements = Driver.GetDriver().findElementsByXPath("//*[starts-with(@id,'sku')]");
		
		for (int i = 0; i < numberOfItems; i++)
		{
			Item newItem = new Item();
			XpathForAmazon = "//*[@id='" + listOfElements.get(i).getAttribute("id") + "']";
			XpathForEbay = "//*[@id='varList" + listOfElements.get(i).getAttribute("id").substring(3)
					+ "']/td[3]/p[2]/a";
			newItem.setSupplierCode(Driver.GetDriver().findElementByXPath(XpathForAmazon).getText().trim());
			newItem.setMarketPlaceCode(Driver.GetDriver().findElementByXPath(XpathForEbay).getText().trim());
			if (i % pageSize == 0 && i > 0)
			{
				listOfElements = Driver.GetDriver().findElementsByXPath("//*[starts-with(@id,'sku')]");
			}
			ItemsListFromWeb.add(newItem);
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
	
	private void printList(ArrayList<Item> list)
	{
		for (Item ele : list)
		{
			System.out.println(ele.getSupplierCode());
			System.out.println(ele.getMarketPlaceCode());
			System.out.println("---------------");
		}
	}
}
