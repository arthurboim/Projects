package Supplier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import AmazonAPI.AmazonAPI;
import Config.Config;
import DataBase.SQLDataBase;
import Item.Item;
import WebDriver.SelenumWebDriver;

public class AmazonSupplier implements SupplierInterface
{
	private AmazonAPI			Amazon;
	private SQLDataBase			DB;
	
	/* Contractors */
	
	public AmazonSupplier()
	{
		try
		{
			ReadFileConfigurations(null);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		Amazon = new AmazonAPI();
		DB = new SQLDataBase();
	}
	
	public AmazonSupplier(String KeysFilePath)
	{
		try
		{
			ReadFileConfigurations(KeysFilePath);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void ReadFileConfigurations(String KeysFilePath) throws IOException
	{
		BufferedReader br = null;
		FileReader fr = null;
		try
		{
			
			if (null == KeysFilePath)
			{
				fr = new FileReader(Config.KeysFilePath);
			}
			else
			{
				fr = new FileReader(KeysFilePath);
			}
			
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null)
			{

				
			}
		} catch (IOException e)
		{
			e.printStackTrace();
			throw e;
		} finally
		{
			
			try
			{
				
				if (br != null)
					br.close();
				
				if (fr != null)
					fr.close();
				
			} catch (IOException ex)
			{
				
				ex.printStackTrace();
				throw ex;
			}
		}
	}
	
	
	
	/* Interface implementations */

	@Override
	public void GetItemsUpdate(List<Item> ListOfItems)
	{
		Amazon.UpdateItemViaWebSelenum(ListOfItems);
		for(Item ele:ListOfItems)
		{
			DB.UpdatePrice(ele);
			DB.UpdateTax(ele);
			DB.UpdateStock(ele);
		}
	}
	

}
