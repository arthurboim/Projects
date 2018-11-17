package WebDriver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import Config.Config;

public class SelenumWebDriver {

	
	final private int driverWatingTime = 30;
	//private List<WebElement> elements;
	private ChromeDriver Driver; 
	private String KeysPath; 
	private ChromeOptions options;
	private	WebDriverWait wait;
	private String CurrentPageUrl;
	private String PageUrlAfterClick;

	
	public enum OperationStatus{
		SUCSESS,
		FAIL
	}
	
	/* Contractors */
	
	public SelenumWebDriver() {
		
		try{
			ReadFileConfigurations(Config.KeysFilePath);
		}catch(Exception e)
		{
		}
		

        OpenWebDriver();
        System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
        wait = new WebDriverWait(Driver,driverWatingTime);
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
	

	public void OpenWebDriver() {
		System.setProperty("webdriver.chrome.driver", KeysPath);
		options = new ChromeOptions();
		options.addArguments("--start-maximized");
		Driver = new ChromeDriver(options);
		Driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		/* Minimize command */
		//Driver.manage().window().setPosition(new Point(0, -3000));
	}

	
	public void CloseWebDriver() {
		Driver.close();
		Driver.quit();
	}

	
	public void OpenLink(String URL) {
		Driver.get(URL);
	}

	
	
	/* Get text */
	public String GetText(String xpath, String elementId)
	{
		String Text = null;
		
		if (xpath != null)
		{
			Text = GetTextByXpath(xpath);
		}
		
		if (null == Text && null != elementId)
		{
			Text = GetTextById(elementId);
		}
		
		return Text;
	}
	
	public String GetTextByXpath(String xpath) {
		try{
			waitForLoad(Driver);
			return Driver.findElementByXPath(xpath).getText();
		}catch(Exception e)
		{
			
		}
		
		return null;
	}
	
	public String GetTextById(String elementId)
	{
		try{
			waitForLoad(Driver);
			return Driver.findElementById(elementId).getText();
		}catch(Exception e)
		{
		}
		return null;
	}

	
	
	/* Set text */
	
	public OperationStatus SetText(String xpath,String elementId , String text)
	{
		OperationStatus status = OperationStatus.FAIL;
		
		if (xpath == null && elementId == null )
		{
			return status;
		}
		
		waitForLoad(Driver);
		status = Click(xpath,elementId);
		
		if (OperationStatus.FAIL == status && xpath != null)
		{
			status = SetTextByXpath(xpath,text);			
		}
		
		if (OperationStatus.FAIL == status && elementId != null)
		{
			status = SetTextById(elementId,text);
		}
		
		return status;
	}
	
	public OperationStatus SetTextByXpath(String xpath, String text) {
		Driver.findElementByXPath(xpath).sendKeys(text);
		return OperationStatus.SUCSESS;
	}

	public OperationStatus SetTextById(String elementId, String text)
	{
		Driver.findElement(By.id(elementId)).sendKeys(text);		
		return OperationStatus.SUCSESS;
	}


	
	
	/* Click */

	public OperationStatus Click(String xpath, String elementId) {
		
		OperationStatus status = OperationStatus.FAIL;
		
		if (xpath == null && elementId == null )
		{
			return status;
		}
		
		if (xpath != null)
		{
			status = ClickByXpath(xpath);			
		}
		
		if (OperationStatus.FAIL == status)
		{
			status = ClickById(elementId);
		}
		
		return status;
	}
	
	public OperationStatus ClickById(String elementId) 
	{
		OperationStatus status = OperationStatus.SUCSESS;
		try{
			CurrentPageUrl = GetPageUrl();
			Driver.findElementById(elementId).click();
			waitForLoad(Driver);
			PageUrlAfterClick = GetPageUrl();
			if (PageUrlAfterClick.equals(CurrentPageUrl))
			{
				status =  OperationStatus.FAIL;
			}
		}catch(Exception e)
		{
			status =  OperationStatus.FAIL;
		}
		
		return status;
	}
	
	public OperationStatus ClickByXpath(String xpath) {
		
		OperationStatus status = OperationStatus.SUCSESS;
		try{
			CurrentPageUrl = GetPageUrl();
			Driver.findElementByXPath(xpath).click();
			waitForLoad(Driver);
			PageUrlAfterClick = GetPageUrl();
			if (PageUrlAfterClick.equals(CurrentPageUrl))
			{
				status =  OperationStatus.FAIL;
			}
		}catch(Exception e)
		{
			status =  OperationStatus.FAIL;
		}
		
		return status;
	}

	

	
	/* Inner functions */
	
	private String GetPageUrl()
	{
		return Driver.getCurrentUrl();
	}
	
	
	
	private void waitForLoad(ChromeDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        wait.until(pageLoadCondition);
    }
	
}
