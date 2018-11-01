package Control;

import org.junit.Test;
import Database.Item;

public class UniversalCodeChekerFromAsinLabTests {

	@Test
	public void test() {
		UniversalCodeCheckerFromAsinLab Cheker = new UniversalCodeCheckerFromAsinLab();
	
		TestingItem("0374120927",Cheker);
		TestingItem("B012JCX2SI",Cheker);
		
		Cheker = null;
		System.gc();
	}

	private void TestingItem(String Code, UniversalCodeCheckerFromAsinLab Cheker)
	{
		Item item = new Item();
		item.SupplierCode = Code;
		Cheker.GetItemInfo(item);
		Cheker.GetResultsByCode(item);
		
		System.out.println("ASIN = "+item.SupplierCode );
		System.out.println("UPC = "+item.UPC );
		System.out.println("EAN = "+item.EAN );
		System.out.println("eBay results = "+item.eBayResults );
		System.out.println("---------------------------");
		
	}
	
}
