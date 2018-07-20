import java.io.IOException;
import java.sql.SQLException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import Control.Control;


public class ABEMonitorMain {

	public static void main(String[] args) throws IOException, SQLException, ParserConfigurationException, SAXException {

		Control Control = new Control();		
		Control.BulkCheckItemBeforeUpload();
	}

}
