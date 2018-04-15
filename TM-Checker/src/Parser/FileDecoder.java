package Parser;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Spring;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class FileDecoder implements Runnable
{
	static public int numofthreads=0;
	ArrayList<String> BrandsList;
	String path;
	public FileDecoder(String path,ArrayList<String> BrandsList) 
	{
		this.BrandsList = BrandsList;
		this.path = path;
	}

	public void ParseFile(String PathToFile,ArrayList<String> BrandsName)
	{
		int i;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(PathToFile);
			/*NodeList list = doc.getElementsByTagName("mark-text");*/
			NodeList list = doc.getElementsByTagName("mark-identification");
			for(i=0;i<list.getLength();i++)
			{
				BrandsName.add(list.item(i).getTextContent());
			}
			RemoveDuplicatedBrands(BrandsName);
			
			System.out.println("Total TM: "+BrandsName.size());
			builder = null;
			doc = null;
			list = null;
			System.gc();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ParseFileWithScanner(String PathToFile,ArrayList<String> BrandsName) throws IOException
	{
		
		String str;
		long counter = 0;
	       try {
	               FileInputStream fileInput = new FileInputStream(PathToFile);
	               DataInputStream dataInput = new DataInputStream(fileInput);
	               InputStreamReader inputStr = new InputStreamReader(dataInput);
	               BufferedReader bufRead = new BufferedReader(inputStr);

	                   while((str = bufRead.readLine()) != null)
	                   {
	                	   counter++;
	                	   if(str.contains("mark-identification"))
	                	   {
	                		   str = str.substring(str.indexOf("mark-identification")+("mark-identification").length()+1);
	                		   str = str.replace("</mark-identification>", "");
	                		  /* System.out.println(str);*/
	                		   BrandsName.add(str);
	                	   }
	                	   
	                	   
	                	   if(str.contains("mark-text"))
	                	   {
	                		   str = str.substring(str.indexOf("mark-text")+("mark-text").length()+1);
	                		   str = str.replace("</mark-text>", "");
	                		   /*System.out.println(str);*/
	                		   BrandsName.add(str);
	                	   }
	                   }
	                   System.out.println(counter);
	                   RemoveDuplicatedBrands(BrandsName);
	           } catch (FileNotFoundException e) {
	               e.printStackTrace();
	           }
	}

	public static void RemoveDuplicatedBrands(ArrayList<String> BrandsName)
	{
		Set<String> hs = new HashSet<>();
		hs.addAll(BrandsName);
		BrandsName.clear();
		BrandsName.addAll(hs);
	}

	@Override
	public void run() {
		try {
			numofthreads++;
			ParseFileWithScanner(path,BrandsList);
			numofthreads--;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
