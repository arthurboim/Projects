import java.io.IOException;
import java.sql.SQLException;
import Control.MainControl;

public class MainRun {

	public static void main(String[] args) throws SQLException, IOException, InterruptedException 
	{
		/*
		FilesGraber Graber = new FilesGraber();
		FileDecoder Decoder = new FileDecoder();
		Graber.GetFilesNameFromFolder("D:\\Downloads\\TM-XML");
		Decoder.ParseFile("D:\\Downloads\\TM-XML\\tt160101.xml", null);
		*/
		MainControl Control = new MainControl();
		Control.TMFilesParser();
		
		/*
		Database db = new Database();
		
		db.isExcist("LI'L PEACH");
		*/
	}

}
