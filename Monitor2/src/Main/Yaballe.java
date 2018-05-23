package Main;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Database.DatabaseOp;
import Gui.OrdersGui;
import PriceChanger.DatabasePriceChanger;

public class Yaballe extends Thread{

	public static int ItemsPerPage = 100;
	public static int LoadedItems = 0;
	public static int ItemsNumber = 0;
	@Override
	public void run() {
		try {
			if (OrdersGui.StoreName.equals("All"))
			{
			for (Store ele:Main.ListOfStores)
			{
			GetItemsFromYaballe(ele);
			}
			}
			else
			{
			GetItemsFromYaballe(Main.GetStoreByName(OrdersGui.StoreName));
			}

		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static void GetItemsFromYaballe(Store Store) throws SQLException, InterruptedException
	{
		int pagesNumber = -1;
		List<ProductOnline> database_list = new ArrayList<ProductOnline>();
		List<ProductOnline> list = new ArrayList<ProductOnline>();
		DatabaseMain Db = new DatabaseMain();
		Db.GetDatabase(database_list);

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver Driver = new ChromeDriver(options);
		Driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		//WebDriverWait wait = new WebDriverWait(Driver, 20);
		if (YaballeLogin(Driver,Store.MonitorUser,Store.MonitorPassword)==0) return;
		WatingForPageToUpload(Driver);
		YaballeGetPagesNumber(Driver);
		WatingForPageToUpload(Driver);
		Thread.sleep(2000);
		SetPageTo100(Driver);
		WatingForPageToUpload(Driver);
		Thread.sleep(2000);
		if ((pagesNumber = YaballeGetPagesNumber(Driver))==-1) return;
		WatingForPageToUpload(Driver);
		for(int i=1;i<=pagesNumber;i++)
		{
			if ((GetItemsFromPage(Driver,list)==0)) return;
			while(NextPage(Driver,i)==0);
			
		}
		System.out.println("Items amount loaded "+list.size()+" from excisting "+ItemsNumber);
		Thread.sleep(1000);
		System.out.println("Writing items to Database...");
		if (UpdateDatabase(list,database_list,Store)==0) return; //check here //
		Thread.sleep(100);
		System.out.println("Updateing items ended successfully!");
		Thread.sleep(100);
		LoadedItems = 0;
		System.out.println("Good luck!");
		Driver.close();
	}
	
	public  static  int YaballeLogin(ChromeDriver Driver,String UserName,String Password)
	{
		WebDriverWait wait = new WebDriverWait(Driver, 20);
		
		try{
			Driver.get("https://yaballe.com");
			wait.until(ExpectedConditions.elementToBeClickable(Driver.findElementByXPath("//*[@id='modal_trigger']")));
			Driver.findElement(By.xpath("//*[@id='modal_trigger']")).click();
			wait.until(ExpectedConditions.elementToBeClickable(Driver.findElementByXPath("//*[@id='login_email']")));
			System.out.println("Sending...");
			Driver.findElement(By.xpath("//*[@id='login_email']")).click();
			Driver.findElement(By.xpath("//*[@id='login_email']")).clear();
			Driver.findElement(By.xpath("//*[@id='login_email']")).sendKeys(UserName);
			Driver.findElement(By.xpath("//*[@id='password']")).click();
			Driver.findElement(By.xpath("//*[@id='password']")).clear();
			Driver.findElement(By.xpath("//*[@id='password']")).sendKeys(Password);
			Driver.findElement(By.xpath("//*[@id='login_site']")).click();	
			Thread.sleep(4000);
			Driver.findElement(By.xpath("//*[@id='sidebar-menu']/li[2]/a")).click();	

		}catch(Exception e){
			System.out.println(e);
			return 0;
		}
		return 1;
	}
	
	private static int YaballeGetPagesNumber(ChromeDriver Driver)
	{
		try{								  
			//while (Driver.findElement(By.xpath("//*[@id='page-content']/div/div/div/div/div[3]")).getText().length()<"Total Items: ".length());
			Thread.sleep(2000);
			String s  = Driver.findElement(By.xpath("//*[@id='page-content']/div/div/div/div/div[2]/div[2]")).getText();
			System.out.println(s);
			s = s.substring(s.indexOf("Items: ")+"Items: ".length(),s.indexOf("/")-1);
			ItemsNumber =  Integer.parseInt(s);
			System.out.println("Pages number = "+((ItemsNumber/ItemsPerPage)+1));
			return (ItemsNumber/ItemsPerPage)+1;
		}catch(Exception e){
			System.out.println(e);
			return -1;
		}
	}
	
	private static int SetPageTo100(ChromeDriver Driver)
	{
		Driver.findElementByXPath("//*[@id='page-content']/div/div/md-content/div/div/div/div/div/button[4]/span").click();
		return 1;
	}
	
	private static int WatingForPageToUpload(ChromeDriver Driver)
	{	
		while (Driver.findElement(By.xpath("//*[@id='page-content']/div/div/div/div/div[2]/div[2]")).getText().length()<"Items: ".length());
		return 1;
	}
	
	private static int GetItemsFromPage(ChromeDriver Driver,List<ProductOnline> list)
	{
		String price;
		String ProfitPercent;
		String BreakEven;
		try{
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_R);
			robot.keyRelease(KeyEvent.VK_R);
			robot.keyRelease(KeyEvent.VK_ALT);
			Thread.sleep(1000);//											 
		for(int i=0;i<ItemsPerPage && LoadedItems <ItemsNumber;i++)
		{
			if (LoadedItems==ItemsNumber) break;         
			ProductOnline  temp = new ProductOnline();
			
			temp.AmazonPrice = Double.parseDouble(Driver.findElementByXPath("//*[@id='page-content']/div/div/md-content/div/table/tbody/tr["+(i+1)+"]/td[3]/div/div[2]").getText().replace("$", ""));
			temp.AmazonAsin = Driver.findElementByXPath("//*[@id='page-content']/div/div/md-content/div/table/tbody/tr["+(i+1)+"]/td[3]/div/div[1]/a").getText();
													 
			temp.EbayId = Driver.findElementByXPath("//*[@id='page-content']/div/div/md-content/div/table/tbody/tr["+(i+1)+"]/td[4]/div[1]/a").getText();
			price = Driver.findElementByXPath("//*[@id='page-content']/div/div/md-content/div/table/tbody/tr["+(i+1)+"]/td[8]/a").getText().substring(1);
			temp.Price = Double.parseDouble(price);
			ProfitPercent = Driver.findElementByXPath("//*[@id='page-content']/div/div/md-content/div/table/tbody/tr["+(i+1)+"]/td[7]/a").getText();
			temp.ProfitPersent = Double.parseDouble(ProfitPercent);									  
			BreakEven = Driver.findElementByXPath("//*[@id='page-content']/div/div/md-content/div/table/tbody/tr["+(i+1)+"]/td[8]/a").getText();
			temp.BreakEven = Double.parseDouble(BreakEven);
			System.out.println("Profit persent = "+temp.ProfitPersent);
			System.out.println("Break even = "+temp.BreakEven);
			list.add(temp);
			LoadedItems++;
			System.out.println("LoadedItems = "+LoadedItems+"/"+ItemsNumber);
		}
		}catch(Exception e){
			System.out.println(e);
			return 0;
		}
		return 1;

	}

	private static  int NextPage(ChromeDriver Driver,int i)
	{
		try{
		if (LoadedItems==ItemsNumber)return 1;
									
		Driver.findElementByXPath("//*[@id='page-content']/div/div/md-content/div/div/div/div/ul/li[6]/a").click();
		System.out.println("Loading page number "+(i+1));
		Thread.sleep(4000);				
		while (Driver.findElement(By.xpath("//*[@id='page-content']/div/div/div/div/div[2]/div[2]")).getText().length()<"Items: ".length());
		System.out.println("Page "+i+" loaded");
		WatingForPageToUpload(Driver);
		}catch(Exception e){System.out.println(e);return 0;}
		return 1;
	}

	private static int UpdateDatabase(List<ProductOnline> list,List<ProductOnline> database_list,Store Store) throws SQLException, InterruptedException
	{
		
		DatabaseOp DatabaseOp = new DatabaseOp();
		DatabasePriceChanger DatabasePriceChanger = new DatabasePriceChanger();
		try{
			
		for(ProductOnline ele:list)
		{
			DatabasePriceChanger.UpdateStoreName(ele.EbayId,Store.Store_name);
			DatabasePriceChanger.UpdateProfitPercent(ele.EbayId,ele.ProfitPersent);
			DatabasePriceChanger.UpdateBreakEven(ele.EbayId,ele.BreakEven);
			DatabasePriceChanger.UpdateAmazonPrice(ele.EbayId,ele.AmazonPrice);
		}
		DatabasePriceChanger = null;
		System.gc();
		
		for(ProductOnline ele:list)
		{
			for(ProductOnline ele1:database_list) 
			{
				if (ele.AmazonAsin.equals(ele1.AmazonAsin)) 
					{
					ele1.flag = 1;
					ele.flag = 1;
					}
			}

		}
		}catch(Exception e){System.out.println("Compering items error");return 0;}
		
		//update and remove //
		try{
		for(ProductOnline ele1:database_list)
		{
		if (ele1.flag==-1)
		DatabaseOp.RemoveItemFromOnlineTable(ele1.AmazonAsin);
		}
		
		for(ProductOnline ele1:list)
		{
		if (ele1.flag==-1)
		DatabaseOp.SetNewItemToDb(ele1);
		}
		}catch(Exception e){System.out.println("Update and remove error");System.out.println(e);return 0;}
		
		return 1;
	}
	
	
}
