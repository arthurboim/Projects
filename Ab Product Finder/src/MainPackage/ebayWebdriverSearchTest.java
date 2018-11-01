package MainPackage;


import org.junit.Test;

public class ebayWebdriverSearchTest {

	@Test
	public void test() {
		EbaySearch ebay = new EbaySearch();
		product productToCheck = new product();
		productToCheck.ASIN = "B01A9ME018";
		productToCheck.bestresult = "dasd";
		ebay.FinditemByWebDriver(productToCheck);
//		productToCheck = new product();
//		productToCheck.bestresult = "128408017X";
//		ebay.FinditemByWebDriver(productToCheck);
	}

}
