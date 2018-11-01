import java.io.IOException;
import java.sql.SQLException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import Control.Control;
import Gui.ABEMonitorGUI;


public class ABEMonitorMain {

	public static void main(String[] args) throws IOException, SQLException, ParserConfigurationException, SAXException, InterruptedException {

//		Control Control = new Control();
//		while(true)
//		{
//			Control.WebDriverCheckItemBeforeUpload();
//			Thread.sleep(1000*60*10);
//		}

		ABEMonitorGUI.main(null);
		
	}

}
