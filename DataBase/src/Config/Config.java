package Config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class Config {
	
	/* Variables and definitions */
	
	public static enum platform
	{
		Windows,
		Linux,
		Mac
	}
	public static enum MonitorMode
	{
		Optimizer,
		Normal
	}

	public static String LogPath = "C:\\Keys\\Log.txt";
	public static String KeysFilePath = "C:\\Keys\\ConfigFile-Keys-New.txt";
	public static platform currentPlatform;
	public static MonitorMode currentMonitorMode;
	public static String OS;
	public static int Version  = 0x0101;
	public static int Month	   = (02);
	public static int Day	   = (11);
	public static int Year	   = (2018);
	public static DateFormat dateFormat;
	public static Date date;
	/* Contractor */
	
	public Config() {
		try {
			// Change this function in other project
			ReadFileConfigurations(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date = new Date();
		OS = new String();

	}

	

	/* Functions */
	
	public void Initialization()
	{
		// Get platform 
		GetPlatform();

	}
	
	public static String GetCurrentTime()
	{
		return dateFormat.format(date);
	}
	
	
	
	//Platform information 
	
	private static void GetPlatform()
	{
		String OS = System.getProperty("os.name").toLowerCase();
		System.out.println("Platform: "+OS);
		
		if (isWindows(OS)) {
			currentPlatform = platform.Windows;
		} else if (isMac(OS)) {
			currentPlatform = platform.Mac;
		} else if (isUnix(OS)) {
			currentPlatform = platform.Linux;
		}  else {
			System.out.println("Your OS is not support!!!");
		}
	}
	
	private static boolean isWindows(String OS) {

		return (OS.indexOf("win") >= 0);

	}

	private static boolean isMac(String OS) {

		return (OS.indexOf("mac") >= 0);

	}

	private static boolean isUnix(String OS) {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
		
	}

	
	
	
	// Read configuration from file
	private void ReadFileConfigurations(String KeysFilePath) throws IOException
	{
		String temp = null;
		BufferedReader br = null;
		FileReader fr = null;
		try {
			
			if(null == KeysFilePath)
			{
				fr = new FileReader(Config.KeysFilePath);
			}else
			{
				fr = new FileReader(KeysFilePath);
			}
			
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				if (sCurrentLine.contains("MonitorMode: "))
				{
					temp = sCurrentLine.substring(sCurrentLine.indexOf("MonitorMode: ")+"MonitorMode: ".length());
				}	
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();
				throw ex;
			}
			}
		
		if (temp.equals("optimizer"))
		{
			currentMonitorMode = MonitorMode.Optimizer;
		}else
		{
			currentMonitorMode = MonitorMode.Normal;
		}
		
	}


}
