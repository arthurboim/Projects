package Supplier;


import java.util.ArrayList;
import org.junit.Test;
import Item.Item;

public class AmazonSupplierTest
{
	
	@Test
	public void AmazonSuppliyerViaWebTest()
	{
	}
	
	public void PrintReport(ArrayList<Item> ListOfItems)
	{
		for (Item ele : ListOfItems)
		{
			System.out.println(ele.toString());
		}
	}
}
