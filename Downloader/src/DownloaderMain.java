import java.io.IOException;

public class DownloaderMain {
	public static void main(String[] args) throws IOException 
	{			
		
		for (int i = 2227092;i<2228592;i++)  
		{					  
			//downloader.DownloadFile("https://listen.freshrecords.ru/?id="+i,"F:\\Downloads\\"+i+".mp3");
			Thread t = new Thread(new Download("https://listen.freshrecords.ru/?id="+i,"C:\\Downloads\\"+i+".mp3"));
			while(java.lang.Thread.activeCount()>200);   
			t.start();
		}

	}
	
 

}
