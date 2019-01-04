package DataBase;


import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;
import Item.Item;


public class DataBaseTests {




	public void TestRead() throws IOException, SQLException {
		SQLDataBase DB = new SQLDataBase();
		DB.Read("SELECT * FROM amazon.online;");
		assert(DB.getRes().getString("AmazonAsin")!=null);
	}
	
	
	public void TestWrite() throws IOException, SQLException {
		SQLDataBase DB = new SQLDataBase();
		try{
		DB.Write("UPDATE amazon.online SET Tax='0';");
		}catch(Exception e){
			
		}
		
	}
	

	public void TestGetResultsAmount()
	{
		int size =0;
		SQLDataBase DB = new SQLDataBase();
		size = DB.GetResultsAmount("SELECT * FROM amazon.orders where Buyer_User_ID = 'aks_smiley1'");
		System.out.println(size);
	}


	public void TestisExistInDataBase()
	{
		SQLDataBase DB = new SQLDataBase();
		Boolean bool = DB.IsExcist("SELECT * FROM amazon.online where EbayId = '223072085098';");
		System.out.println(bool);
	}
	
	@Test
	public void TestUpdateLastTimeUpdated()
	{
		SQLDataBase DB = new SQLDataBase();
		Item newItem = new Item();
		newItem.setMarketPlaceCode("223184142309");
		DB.UpdateLastTimeUpdated(newItem);
	}
	
	
}
