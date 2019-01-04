package Main;

import java.sql.Connection;
import java.sql.DriverManager;

import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import Controller.Control;


public class EbayOptimizerMain {

	public static int ErrorCount = 0;
	public static int ThreadNumber = 5;
	
	public static void main(String[] args) throws ApiException, SdkException, Exception {
		// TODO Auto-generated method stub
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
		Control control = new Control(con);
		//control.GetSalesUpdate();
		//control.Update_All_Items_With_Item_Specific();
		control.GetImageBeforeUpload();
		control.GetAsinAfterFilter();
		////control.RemoveDuplicats();
		//EbayCalls call = new EbayCalls();
		//System.out.println("Watcher amount = "+call.Getwatchers("222588976449"));
		//control.GetCategory();
		
		/*
		NameValueListArrayType list = null;
		list = call.GetItemSpecifics("272586117591");
		
		for (int i=0;i<list.getNameValueListLength();i++)
		{
			System.out.println(list.getNameValueList(i).getName()+": "+list.getNameValueList(i).getValue(0));
		}
		*/
		//call.ChangeTitle(EbayId, NewTitle);
		//GuiOptimizer Go = new GuiOptimizer();
		//Go.MainGuiOptimizer();
		
		//GuiTitleResult GuiTitleResult = new GuiTitleResult();
		//GuiTitleResult.main();
		
	//	Excel Excel = new Excel();
	//	Excel.ExcelMain();
		

	/*
		Thread[] ThreadArray = new Thread[ThreadNumber];
		for (int i=0 ;i<ThreadNumber;i++)
		{
			ThreadArray[i] = new Thread(control);
			System.out.println("$$$$$$$$$$$$$$$$$$$ Starting = "+ThreadArray[i].getId()+" $$$$$$$$$$$$$$$$$$$");
			ThreadArray[i].start();
			Thread.sleep(3000);
		
		}
	 */
	
	}
		

}
