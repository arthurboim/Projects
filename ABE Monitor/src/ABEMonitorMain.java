import java.io.IOException;
import java.sql.SQLException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import Control.Control;


public class ABEMonitorMain {

	public static void main(String[] args) throws IOException, SQLException, ParserConfigurationException, SAXException {

		Control Control = new Control();		
		Control.BulkCheckItemBeforeUpload();
		/*
		ArrayList<Item> ListOfItemsAsins = new ArrayList<Item>();
		Item item = new Item();
		
		Ebay Ebay = new Ebay();
		item.SupplierCode = "B009IAA9DQ";
		ListOfItemsAsins.add(item);
		Control.AmazonApiCheck(ListOfItemsAsins);
		Ebay.IsVeroBrand(ListOfItemsAsins.get(0));
		 */
		//Ebay.DivideStringToWords("#16,838 in Toys & Games (See Top 100 in Toys & Games) ");
		
		
	}

}
