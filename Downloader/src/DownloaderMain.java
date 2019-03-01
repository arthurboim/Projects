import java.io.IOException;

public class DownloaderMain {
	public static void main(String[] args) throws IOException 
	{			
		int start= 2228592;
		int end = 2229176;
		for (;start<end;start++)  
		{	
			System.out.println("Files amount: "+(end - start));
			//downloader.DownloadFile("https://listen.freshrecords.ru/?id="+i,"F:\\Downloads\\"+i+".mp3");
			Thread t = new Thread(new Download("https://listen.freshrecords.ru/?id="+start,"C:\\Downloads\\"+start+".mp3"));
			while(java.lang.Thread.activeCount()>100);
			t.start();
		}

	}
	
 

}
