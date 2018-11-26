package Supplier;


import java.util.ArrayList;
import org.junit.Test;
import Item.Item;

public class AmazonSupplierTest
{
	
	@Test
	public void AmazonSuppliyerViaWebTest()
	{
		AmazonSupplier supplier = new AmazonSupplier();
		ArrayList<Item> ListOfItems = new ArrayList<Item>();
		ListOfItems.add(new Item("B07498PXPS"));
		ListOfItems.add(new Item("1683758919"));
		ListOfItems.add(new Item("B004VHITRE"));
		supplier.UpdateItemViaWebSelenum(ListOfItems);
		PrintReport(ListOfItems);
	}
	
	public void PrintReport(ArrayList<Item> ListOfItems)
	{
		for (Item ele : ListOfItems)
		{
			System.out.println(ele.toString());
		}
	}
}
