package Supplier;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import Item.Item;
import Supplier.AmazonSupplier.CallStatus;

public class AmazonSupplierTest {

	
	public void BuildCodesStringTest() 
	{
		long startTime = System.currentTimeMillis();
		AmazonSupplier supplier = new AmazonSupplier();
		ArrayList<Item> ListOfItems = new ArrayList<Item>();
		ListOfItems.add(new Item("B07BM9LHRB"));
		ListOfItems.add(new Item("B0GHV9LHRB"));
		ListOfItems.add(new Item("B0YFGDSBRB"));
		ListOfItems.add(new Item("DG7BM9LHRB"));
		ListOfItems.add(new Item("QQ7BM9LHRB"));
		ListOfItems.add(new Item("DG7BM9LHRB"));
		ListOfItems.add(new Item("QQ7BM9LHRB"));
		ListOfItems.add(new Item("DG7BM9LHRB"));
		ListOfItems.add(new Item("QQ7BM9LHRB"));
		ListOfItems.add(new Item("QQ7BM9LHRB"));
		assertEquals(supplier.BuildCodesString(ListOfItems,0,9), "B07BM9LHRB,B0GHV9LHRB,B0YFGDSBRB,DG7BM9LHRB,QQ7BM9LHRB,DG7BM9LHRB,QQ7BM9LHRB,DG7BM9LHRB,QQ7BM9LHRB,QQ7BM9LHRB");
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("BuildCodesString Runtime = "+elapsedTime+" in millis");
	}
	
	
	@Test
	public void AmazonLookUpCallTest()
	{
		long startTime = System.currentTimeMillis();
		AmazonSupplier supplier = new AmazonSupplier();
		ArrayList<Item> ListOfItems = new ArrayList<Item>();
		Map<String, String> params = new HashMap<String, String>(); 
		String Codes = new String();
		
		ListOfItems.add(new Item("1985310309"));
		ListOfItems.add(new Item("1285194969"));
		ListOfItems.add(new Item("1980297967"));
		ListOfItems.add(new Item("1522021671"));
		ListOfItems.add(new Item("8591377796"));
		ListOfItems.add(new Item("8494479148"));
		ListOfItems.add(new Item("9587384431"));
		ListOfItems.add(new Item("0871549557"));
		ListOfItems.add(new Item("1609301382"));
		ListOfItems.add(new Item("1979584729"));
		Codes = supplier.BuildCodesString(ListOfItems,0,9);
		supplier.InitAmazonGetItemApiCall(Codes,params);
	
		try {
			assertEquals(supplier.AmazonLookUpCall(params, ListOfItems, 0, 9), CallStatus.SUCCESS);
			System.out.println("Price = "+ListOfItems.get(0).getPrice());
			assertTrue(12.99 == ListOfItems.get(0).getPrice());
			System.out.println("Price = "+ListOfItems.get(1).getPrice());
			assertTrue(45.35 == ListOfItems.get(1).getPrice() );
			System.out.println("Price = "+ListOfItems.get(2).getPrice());
			assertTrue(15.99 == ListOfItems.get(2).getPrice());
			System.out.println("Price = "+ListOfItems.get(3).getPrice());
			assertTrue(14.95 == ListOfItems.get(3).getPrice());
			
		} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("AmazonLookUpCall Runtime = "+elapsedTime+" in millis");
	}
	
}
