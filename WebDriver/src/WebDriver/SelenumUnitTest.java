package WebDriver;



public class SelenumUnitTest {

	
	public void Click_test() throws InterruptedException {
		SelenumWebDriver WebDriver = new SelenumWebDriver();
		WebDriver.OpenLink("https://www.amazon.com/dp/B0746MHQ5G");
		//System.out.println(WebDriver.Click(null, null));
		//System.out.println(WebDriver.Click(null, "olp-upd-new"));
		//System.out.println(WebDriver.Click("//*[@id='olp-upd-new']/span/a", null));
		//System.out.println(WebDriver.Click("//*[@id='olp-upd-new']/span/a", "olp-upd-new"));
		//Thread.sleep(5000);
		//System.out.println(WebDriver.GetTextByXpath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/span"));;
		WebDriver.CloseWebDriver();
	}
	

	public void GetText_Test()
	{
		String Text;
		SelenumWebDriver WebDriver = new SelenumWebDriver();
		WebDriver.OpenLink("https://www.amazon.com/dp/B0746MHQ5G");
		Text = WebDriver.GetText(null, null);
		System.out.println(Text);
		Text = WebDriver.GetText("//*[@id='productTitle']", null);
		System.out.println(Text);
		Text = WebDriver.GetText(null, "productTitle");
		System.out.println(Text);
		Text = WebDriver.GetText("//*[@id='productTitle']", "productTitle");
		System.out.println(Text);
		WebDriver.CloseWebDriver();
	}

	public void SetText_Test()
	{
		// TODO run a test
	}

}
