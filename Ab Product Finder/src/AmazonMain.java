import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.xml.sax.SAXException;

import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.gargoylesoftware.htmlunit.javascript.host.file.FileReader;

import Database.DataBaseOp;
import Gui.AbFinderGui;
import MainPackage.*;

public class AmazonMain {

	public static void main(String[] args) throws ApiException, SdkException, Exception {

		/*
		FileOp Op = new FileOp();
		Op.TotalAmountOfLinesInFile("C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Toys-Games - All - Copy.txt");
		Op.GetCurrentLineInFile("C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Toys-Games - All - Copy.txt");
		System.out.println(Op.CheckIfFileContainsHere("C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Toys-Games - All - Copy.txt"));
		Op.SetHere("C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Toys-Games - All - Copy.txt", "https://www.amazon.com/Best-Sellers-Toys-Games-Kids-Bedding/zgbs/toys-and-games/1063268/ref=zg_bs_nav_t_2_166210011");
		System.out.println(Op.CheckIfFileContainsHere("C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Toys-Games - All - Copy.txt"));
		Op.RemoveHere("C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Toys-Games - All - Copy.txt", "HERE-->https://www.amazon.com/Best-Sellers-Toys-Games-Kids-Bedding/zgbs/toys-and-games/1063268/ref=zg_bs_nav_t_2_166210011");
		System.out.println(Op.CheckIfFileContainsHere("C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Toys-Games - All - Copy.txt"));
		 */
		

		AbFinderGui gui = new AbFinderGui();
		gui.main(null);
		//ScrapInfo info = new ScrapInfo();
		//List<product> ItemsList = new ArrayList<product>();
		//info.Pre_order_books();
		//info.Pre_order_movies();
		//info.Update_Pre_order_Relevant();
		/*
		info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\MoversAndShakers\\Hot-Releseas.txt");
		info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\MoversAndShakers\\Movers-and-shakers.txt");
		info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Toys_And_Games.txt");
		info.Items_info(ItemsList,"C:\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Home-Kitchen.txt");
		
		info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Pet-Supplies.txt");
		info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-books.txt");
		info.Items_info(ItemsList,"C:\\Users\\Noname\\D\esktop\\Amazon\\BestSellerLinks\\Best-Sellers-Movies-TV-DVD.txt");
		info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Industrial-Scientific.txt");
		info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Baby.txt");
		info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Automotive.txt");
		*/
		
		
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Automotive-ALL.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\best-sellers-books-Amazon.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Sports-Outdoors-ALL9.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Sports-Outdoors-ALL.txt");
		///info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Sports-Outdoors-ALL1.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Sports-Outdoors-ALL2.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Sports-Outdoors-ALL3.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Sports-Outdoors-ALL4.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Sports-Outdoors-ALL5.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Sports-Outdoors-ALL6.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Sports-Outdoors-ALL7.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Sports-Outdoors-ALL8.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-video-games.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Kitchen-Dining - ALL.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Patio-Lawn-Garden - All.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\best-sellers-camera-photo-all.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Arts-Crafts-Sewing-ALL.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Office-Products - ALL.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Pet-Supplies - ALL.txt");
		//info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Toys-Games - All.txt");
	//	info.Items_info(ItemsList,"C:\\Users\\Noname\\Desktop\\Amazon\\BestSellerLinks\\Best-Sellers-Electronics - ALL.txt");
		
		//Links links_Scrap = new Links();
		//links_Scrap.get_links("https://www.amazon.com/Best-Sellers-Computers-Accessories/zgbs/pc/ref=zg_bs_nav_0");
		//links_Scrap.get_links("https://www.amazon.com/Best-Sellers-Entertainment-Collectibles/zgbs/entertainment-collectibles/ref=zg_bs_nav_0");
		//links_Scrap.get_links("https://www.amazon.com/Best-Sellers-Health-Personal-Care/zgbs/hpc/ref=zg_bs_nav_0");
		//links_Scrap.get_links("https://www.amazon.com/best-sellers-video-games/zgbs/videogames/ref=zg_bs_nav_0");

	}

}
