package Main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import Database.DatabaseOp;

public class AutoDsTool extends Thread {

	
	@Override
	public void run() {
		try {
			Get_AutoDsTool_items();
		} catch (SQLException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}

	public void Get_AutoDsTool_items() throws SQLException, InterruptedException
	{
		DatabaseOp DatabaseOp = new DatabaseOp();
		List<ProductOnline> database_list = new ArrayList<ProductOnline>();
		List<ProductOnline> list = new ArrayList<ProductOnline>();
		DatabaseMain Db = new DatabaseMain();
		Db.GetDatabase(database_list);

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver Driver = new ChromeDriver(options);
		Driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		Driver.get("https://autodstools.com/");
		Driver.findElementByXPath("//*[@id='email']").sendKeys("arthur.boim@gmail.com");
		Driver.findElementByXPath("//*[@id='password']").sendKeys("b0104196");
		Driver.findElementByXPath("//*[@id='form_login']/div[4]/button").click();
		Thread.sleep(10000);
		Driver.get("https://autodstools.com/active_listings/?ln=500");
		Thread.sleep(60000);
		String s = Driver.findElementByXPath("//*[@id='products_table_info']").getText();
		System.out.println(s);
		s = s.substring(s.lastIndexOf("of")+3, s.lastIndexOf("entries")-1);
		if (s.contains(",")) s = s.replace(",","");
		int counter500 = 0;
		int j= Integer.parseInt(s);
		System.out.println(j);
		String price;
		try{
		int z = 0;
		do
		{
			if (j>500) z = 500;
			else z = j;
			Driver.get("https://autodstools.com/active_listings/?st="+counter500+"&ln=500");
		for (int i = 1;i<=z;i++) // Neet to change when i pass 500 lists //
		{
			ProductOnline  temp = new ProductOnline(); 
			System.out.println(Driver.findElementByXPath("//*[@id='products_table']/tbody/tr["+i+"]/td[4]/a/div/strong").getText());
			System.out.println(Driver.findElementByXPath("//*[@id='products_table']/tbody/tr["+i+"]/td[3]/a/div/strong").getText());
			temp.AmazonAsin = Driver.findElementByXPath("//*[@id='products_table']/tbody/tr["+i+"]/td[4]/a/div/strong").getText();
			temp.EbayId = Driver.findElementByXPath("//*[@id='products_table']/tbody/tr["+i+"]/td[3]/a/div/strong").getText();
			price = Driver.findElementByXPath("//*[@id='products_table']/tbody/tr["+i+"]/td[9]/div/strong").getText();
			temp.Price = Double.parseDouble(price);
			list.add(temp);
		}
		j=j-500;
		if (j>0){
		counter500+=500;
		Driver.get("https://autodstools.com/active_listings/?st="+counter500+"&ln=500");
		}
		}while (j>0); //B0731N9KN4
		
		}catch(Exception e)
		{
			System.out.println("Error update profit");
		}

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
		
		
		//update and remove //
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
		Driver.close();

	}

}
