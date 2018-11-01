package Config;


public class Config {
	
	/* Variables and definitions */
	
	public static enum platform
	{
		Windows,
		Linux,
		Mac
	}
	public static String KeysFilePath = "C:\\Keys\\ConfigFile-Keys-New.txt";
	public static platform currentPlatform;

	/* Functions */
	

	/* Platform information */ 
	
	public static void GetPlatform()
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
			System.out.println("Your OS is not support!!");
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


}
