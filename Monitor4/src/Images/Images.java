package Images;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import Config.Config;
import DataBase.SQLDataBase;
import Item.Item;
import WebDriver.SelenumWebDriver;

public class Images
{
	SQLDataBase 	  	 SQLDb;
	SelenumWebDriver 	 Driver;
	ArrayList<Item>      ListOfItems;
	// Constarctor 
	
	public Images()
	{
		SQLDb = new SQLDataBase();
		ListOfItems = new ArrayList<Item>();
		Driver = new SelenumWebDriver();
	}
	
	
	

	
	// Public 
	
	public void SaveItemImages()
	{
		String temp = null;
		
		SQLDb.GetItemForImagesGrab(ListOfItems);
		
		for(Item ele:ListOfItems)
		{
			try{
			Driver.OpenLink("https://www.amazon.com/dp/"+ele.getSupplierCode());
			Thread.sleep(1000);
			temp = Driver.GetDriver().findElementByXPath("//*[@id='landingImage']").getAttribute("src");
			System.out.println(temp);
			}catch(Exception e)
			{
				try{
				temp = Driver.GetDriver().findElementByXPath("//*[@id='imgBlkFront']").getAttribute("src");
				System.out.println(temp);
				}catch(Exception e1)
				{
					
				}
			}
			try
			{
				saveImage(temp,Config.ImageSavePath+ele.getSupplierCode()+".png");
			} catch (IOException e)
			{
				e.printStackTrace();
			}			
		}
		Driver.CloseWebDriver();
	}
	
	// Protected
	
	
	// Private
	
	 private void saveImage(String imageUrl, String destinationFile) throws IOException 
	 {
	    URL url = new URL(imageUrl);
	    InputStream is = url.openStream();
	    OutputStream os = new FileOutputStream(destinationFile);

	    byte[] b = new byte[2048];
	    int length;

	    while ((length = is.read(b)) != -1) {
	        os.write(b, 0, length);
	    }

	    is.close();
	    os.close();
	}
	
}


