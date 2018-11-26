package ExternalMonitor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;

import DataBase.IDataBase;
import DataBase.SQLDataBase;
import Item.Item;
import WebDriver.SelenumWebDriver;

public class ShopMaster implements ExternalMonitorInterface  {

	SelenumWebDriver Driver;
	IDataBase DB;
	String userName;
	String password;
	int pageSize;
	
	/* Constractor */
	
	public ShopMaster() {
		Driver = new SelenumWebDriver();
		DB = new SQLDataBase();
		userName = "arthur.boim@gmail.com";
		password = "b0104196";
		pageSize = 300;
	}
	
	/* Public */
	
	@Override
	public void GetItemsList() {
		
		Driver.OpenLink("https://www.shopmaster.com/");
		Login();
		Driver.OpenLink("https://www.shopmaster.com/ebay/product/index.htm?curSelect=ebayActive&state=active");
		Driver.Click("//*[@id='downPage']/li[1]/a/select", null);
		Driver.Click("//*[@id='downPage']/li[1]/a/select/option[4]", null);
		try {
			GetItems();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Driver.CloseWebDriver();
	}

	
	



	
	/* Private */

	private void Login()
	{
		Driver.SetText("//*[@id='username']", "username", userName);
		Driver.SetText("//*[@id='password']", "password", password);
		Driver.Click("//*[@id='login']/button", null);
	}
	
	private void GetItems() throws InterruptedException
	{
		String text = null;
		String XpathForAmazon = null;
		String XpathForEbay = null;
		int numberOfItems = 0;
		List<WebElement> listOfElements;
		ArrayList<Item> ItemsListFromWeb = new ArrayList<Item>();
		ArrayList<Item> ItemsListFromDb = new ArrayList<Item>();
	
		Thread.sleep(5*1000);
	
		ResultSet res = DB.Read("SELECT * FROM amazon.online;");
		
		
		try {
			while(res.next())
			{
				Item newItem = new Item();
				newItem.setSupplierCode(res.getString("AmazonAsin"));
				newItem.setMarketPlaceCode(res.getString("EbayId"));
				ItemsListFromDb.add(newItem);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		printList(ItemsListFromDb);
		
		text = Driver.GetText("//*[@id='leftNav']/ul/li[2]/ul/li[1]/p/span[2]", null);
		numberOfItems = Integer.parseInt(text);
		listOfElements = Driver.GetDriver().findElementsByXPath("//*[starts-with(@id,'sku')]");

		for(int i=0;i<numberOfItems;i++)
		{
			Item newItem = new Item();
			XpathForAmazon = "//*[@id='"+listOfElements.get(i).getAttribute("id") + "']";
			XpathForEbay = "//*[@id='varList"+listOfElements.get(i).getAttribute("id").substring(3)+"']/td[3]/p[2]/a";
			newItem.setSupplierCode(Driver.GetDriver().findElementByXPath(XpathForAmazon).getText());
			newItem.setMarketPlaceCode(Driver.GetDriver().findElementByXPath(XpathForEbay).getText());
			if (i%pageSize ==0 && i>0)
			{
				// to to next page
				listOfElements = Driver.GetDriver().findElementsByXPath("//*[starts-with(@id,'sku')]");
			}
			ItemsListFromWeb.add(newItem);
		}
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		printList(ItemsListFromWeb);
	}
	
	private void printList(ArrayList<Item> list)
	{
		for(Item ele:list)
		{
			System.out.println(ele.getSupplierCode());
			System.out.println(ele.getMarketPlaceCode());
			System.out.println("---------------");
		}
	}
}
