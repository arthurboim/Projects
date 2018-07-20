import java.io.IOException;
import java.sql.SQLException;
import Control.MainControl;

public class MainRun {

	public static void main(String[] args) throws SQLException, IOException, InterruptedException 
	{
		MainControl Control = new MainControl();
		Control.TMFilesParser(); // 22.06.18 last download //
	}

}
