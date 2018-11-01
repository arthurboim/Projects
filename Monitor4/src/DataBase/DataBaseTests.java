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
	
	@Test
	public void TestWrite() throws IOException, SQLException {
		SQLDataBase DB = new SQLDataBase();
		try{
		DB.Write("UPDATE amazon.online SET Tax='0';");
		}catch(Exception e){
			assertTrue("Fail", false);	
		}
		assertTrue("Success", true);
	}
	
	

}
