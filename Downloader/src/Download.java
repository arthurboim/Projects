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
	public static int ThreadAmount = 0;
	public static int TotalFilesDownloaded = 0;
	private static final int BUFFER_SIZE = 4096;
	
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
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
			//System.setProperty("http.agent", "Chrome");
			InputStream in = url.openStream(); 
			Files.copy(in, Paths.get(Filename), StandardCopyOption.REPLACE_EXISTING);
			in.close();
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
			
			System.out.println("Not found");
		}
	}

	
	public static void downloadFile(String fileURL, String saveDir)
            throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
 
        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();
 
            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }
 
            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);
 
            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;
             
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
 
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            outputStream.close();
            inputStream.close();
 
            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }
	
        public void run() {
        	try {
        		ThreadAmount++;
        		
				DownloadFile(Path,Filename);
				TotalFilesDownloaded++;
				System.out.println("Thread amount => "+ThreadAmount+" Downloading =>\n"+this.Path);
				System.out.println("Total amount => "+TotalFilesDownloaded);
				ThreadAmount--;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

}
