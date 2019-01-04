package Images;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import Config.Config;
import DataBase.SQLDataBase;
import WebDriver.SelenumWebDriver;

public class Images
{
	SQLDataBase 	  	 SQLDb;
	SelenumWebDriver 	 Driver;
	ArrayList<String>    ListOfItems;
	// Constarctor 
	
	public Images()
	{
		SQLDb = new SQLDataBase();
		ListOfItems = new ArrayList<String>();
		Driver = new SelenumWebDriver();
	}
	
	
	

	
	// Public 
	
	public void SaveItemImageS()
	{
		for(String ele:ListOfItems)
		{
			Driver.OpenLink("www.amazon.com/dp/"+ele);
			//Driver.GetDriver()
		}
		//saveImage(,Config.ImageSavePath++".png");			
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


