import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.openqa.selenium.WebDriver;

import WebDriver.SelenumWebDriver;

public class DownloaderMain {
	public static void main(String[] args) throws IOException 
	{
		//SelenumWebDriver Driver = new SelenumWebDriver();
		int i=0;				
		for (i = 2222422;i<2223472;i++)  
		{
			//http://listen.freshrecords.ru/?id=2220788
			//Driver.OpenLink("http://listen.freshrecords.ru/?id="+i);
			System.out.println("http://listen.freshrecords.ru/?id="+i);
			//Download x = new Download("http://listen.freshrecords.ru/?id="+i,"C:\\Downloads\\"+i+".mp3");
			//x.DownloadFile("http://listen.freshrecords.ru/?id="+i, "C:\\Downloads\\"+i+".mp3");
			//x.downloadFile("http://listen.freshrecords.ru/?id="+i, "C:\\Downloads\\"+i+".mp3");
								  
//			Thread t = new Thread(new Download("http://listen.freshrecords.ru/?id="+i,"C:\\Downloads\\"+i+".mp3"));
//			while(Download.ThreadAmount>100);   
//			t.start();
		}

	}
	
 

}
