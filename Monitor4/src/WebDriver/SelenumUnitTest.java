package WebDriver;


public class SelenumUnitTest {

	
	public void test() {
		WebDriverInterface WebDriver = new SelenumWebDriver();
		WebDriver.OpenWebDriver();
		WebDriver.OpenLink("https://www.amazon.com/gp/offer-listing/B0746MHQ5G?f_new=true");
		System.out.println(WebDriver.GetTextByXpath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/span"));;
		WebDriver.CloseWebDriver();
	}
	


}
