package Monitor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DataBase.IDataBase;
import DataBase.SQLDataBase;
import Item.Item;
import Supplier.AmazonSupplier;
import Supplier.SupplierInterface;

public class Manager {

	public void AmazonToeBay()
	{
		SupplierInterface Amazon  = new AmazonSupplier();
		IDataBase 	  SQLDb	    = new SQLDataBase();
		ArrayList<Item> ListOfItems = new ArrayList<Item>();
		ResultSet res = SQLDb.Read("SELECT * FROM amazon.online;");
		ArrangeResultSet(res,ListOfItems);
		Amazon.GetItemsUpdate(ListOfItems);
		PrintItems(ListOfItems);

	}
	
	
	private void ArrangeResultSet(ResultSet res,ArrayList<Item> ListOfItems)
	{
		try {
			while(res.next())
			{
				Item tempItem = new Item();
				tempItem.setSupplierCode(res.getString("AmazonAsin"));
				tempItem.setMarketPlaceCode(res.getString("EbayId"));	
				tempItem.setUniversalCode(res.getString("Bestresults"));	
				ListOfItems.add(tempItem);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static  void PrintItems(ArrayList<Item> ListOfItems)
	{
		for(Item ele:ListOfItems)
		{
			if ((ele.isPrime() == false ||ele.isPreorder() == true || ele.isInStock() == false) )
			{
				System.out.println(ele.toString());
			}
		}
		
		System.out.println("Fail to call");
		for(Item ele:ListOfItems)
		{
			if (ele.isSupplyerRequestSuccess() == false && ele.getPrice() == -1)
			{
				System.out.println(ele.toString());
			}
		}
	}
}
