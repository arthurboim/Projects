package WebDriver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import Config.Config;

public class SelenumWebDriver implements WebDriverInterface{

	private ChromeDriver Driver; 
	private String KeysPath; 
	private ChromeOptions options;
	
	/* Contractors */
	
	public SelenumWebDriver() {
		
		try{
			ReadFileConfigurations(Config.KeysFilePath);
		}catch(Exception e)
		{
		}
		
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
	}
	
	public SelenumWebDriver(String KeysFilePath) {
		
		try{
			ReadFileConfigurations(KeysFilePath);
		}catch(Exception e)
		{
		}
	}
	
	private void ReadFileConfigurations(String KeysFilePath) throws IOException {
		BufferedReader br = null;
		FileReader fr = null;
		String Temp;
		try {

			if (null == KeysFilePath) {
				fr = new FileReader(Config.KeysFilePath);
			} else {
				fr = new FileReader(KeysFilePath);
			}

			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				Temp = "webdriver.chrome.driver:";
				if (sCurrentLine.contains(Temp)) {
					KeysPath = sCurrentLine.substring(sCurrentLine.indexOf(Temp) + Temp.length() + 1);
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
	}

	
	
	
	/* Implementation for interface */
	

	@Override
	public void OpenWebDriver() {
		System.setProperty("webdriver.chrome.driver", KeysPath);
		options = new ChromeOptions();
		options.addArguments("--start-maximized");
		Driver = new ChromeDriver(options);
		Driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		/* Minimize command */
		//Driver.manage().window().setPosition(new Point(0, -3000));
	}

	@Override
	public void CloseWebDriver() {
		Driver.close();
		Driver.quit();
	}

	@Override
	public void OpenLink(String URL) {
		Driver.get(URL);
	}

	@Override
	public String GetTextByXpath(String xpath) {
		
		try{
			
			return Driver.findElementByXPath(xpath).getText();
		}catch(Exception e)
		{
		}
		return null;
	}

	@Override
	public void SetTextByXpath(String xpath, String text) {
		Driver.findElementByXPath(xpath).sendKeys(text);
	}

	@Override
	public void ClickOnButtonByXpath(String xpath) {
		Driver.findElementByXPath(xpath).click();
	}


// TODO Getelements 

	
	/* Inner functions */
	
}
