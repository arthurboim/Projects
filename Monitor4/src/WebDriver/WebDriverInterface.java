package WebDriver;

public interface WebDriverInterface {
	
	/* Interface functions */
	
	public void OpenWebDriver();
	public void CloseWebDriver();
	public void OpenLink(String URL);
	public void SetTextByXpath(String xpath, String text);
	public void ClickOnButtonByXpath(String xpath);
	public String GetTextByXpath(String xpath);


}
