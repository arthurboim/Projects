package Control;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import Database.Item;

public class VeroCheck {

	@Test
	public void testBrand() throws IOException, ParserConfigurationException, SAXException {
		//B06XWB7DPS
		Control control = new Control();
		ArrayList<Item> ListOfItemsAsins = new ArrayList<Item>();
		Item temp = new Item();
		temp.SupplierCode = "B019YJRZO6";
		ListOfItemsAsins.add(temp);
		control.AmazonApiCheck(ListOfItemsAsins);
		control.ItemsCheckingBeforeUpload(ListOfItemsAsins);
		System.out.println("Ready to upload? "+ListOfItemsAsins.get(0).ReadyToUpload);
		assertEquals("Ready to upload ", ListOfItemsAsins.get(0).ReadyToUpload, false);
	}
	
	public void testAmazonCall() throws IOException, ParserConfigurationException, SAXException {
		//B06XWB7DPS
		Control control = new Control();
		ArrayList<Item> ListOfItemsAsins = new ArrayList<Item>();
		Item temp = new Item();
		temp.SupplierCode = "0671631985";
		ListOfItemsAsins.add(temp);
		control.AmazonApiCheck(ListOfItemsAsins);
		control.ItemsCheckingBeforeUpload(ListOfItemsAsins);
		assertEquals("Ready to upload ", ListOfItemsAsins.get(0).prime, false);
	}
	
}
