package Ebay;

import java.sql.SQLException;
import java.util.ArrayList;

import Database.DatabaseOp;

public class ItemSalesUpdate extends Thread {

	
	public void ItemSalesUpdateMain() throws SQLException
	{

		ArrayList<ItemForUpdate> ItemsList = new ArrayList<ItemForUpdate> ();
		eBayCalls Call = new eBayCalls(); 
		DatabaseOp Db = new DatabaseOp();
		Db.GetAllOnline(ItemsList);
		System.out.println("Items to update = "+ItemsList.size());
		for (ItemForUpdate ele:ItemsList)
		{
		try {
			Db.SetWatcher(ele.EbayId,Call.Getwatchers(ele.EbayId));
			Call.GetMyItemsHistory(ele, 30);
			Db.GetAndSetTax(ele);
			Db.SetItemsOnlineHistory(ele);
		} catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		}
		
	
	}

	@Override
	public void run() {
		try {
			ItemSalesUpdateMain();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
