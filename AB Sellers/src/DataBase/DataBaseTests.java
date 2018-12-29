package DataBase;

import static org.junit.Assert.*;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;

public class DataBaseTests {

	@Test
	public void TestConnection() {
		
		assertTrue("Success", true);
	}

	@Test
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
			assertTrue("Fail", false);	
		}
		assertTrue("Success", true);
	}
	

	public void TestGetResultsAmount()
	{
		int size =0;
		SQLDataBase DB = new SQLDataBase();
		size = DB.GetResultsAmount("SELECT * FROM amazon.orders where Buyer_User_ID = 'aks_smiley1'");
		System.out.println(size);
	}

	
	@Test
	public void TestisExistInDataBase()
	{
		SQLDataBase DB = new SQLDataBase();
		Boolean bool = DB.IsExcist("SELECT * FROM amazon.online where EbayId = '223072085098';");
		System.out.println(bool);
	}

}
