import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Download implements Runnable{

	String Path;
	String Filename;
	 private final Object lock = new Object();
	public static int TotalFilesDownloaded = 0;
	
	public Download(String Path ,String Filename ) 
	{
		this.Path = Path;
		this.Filename = Filename;
	}
	
	public void DownloadFile(String Path, String Filename) throws IOException
	{
		File f = new File(Filename);
		if(f.exists())
		{
			return;
		}
		
		try{
			
			URL url = new URL(Path);
			System.setProperty("http.agent", "Chrome"); // This is must Don't remove
			InputStream in = url.openStream(); 
			Files.copy(in, Paths.get(Filename), StandardCopyOption.REPLACE_EXISTING);
			in.close();
			
			
		}catch(Exception e)
		{
			//System.out.println(e.getMessage());
		}
		
		synchronized (lock) {
			TotalFilesDownloaded++;
			System.out.println("Total amount => "+TotalFilesDownloaded);
		}
	}

	
	// Run the thread

        public void run() {
        	try {
				DownloadFile(Path,Filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }

}
