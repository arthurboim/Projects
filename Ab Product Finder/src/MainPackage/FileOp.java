package MainPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileOp 
{

	public void SetHere(String FilePath , String LineTochange) throws IOException
	{
		File fileToBeModified = new File(FilePath);
		String oldContent = "";
		BufferedReader reader = new BufferedReader(new FileReader(fileToBeModified));
		
		String line = reader.readLine();

		while (line != null) 
		{
		        oldContent = oldContent + line + System.lineSeparator();
		        line = reader.readLine();
		}

		String newContent = oldContent.replaceFirst(LineTochange, "HERE-->"+LineTochange);
		FileWriter writer = new FileWriter(fileToBeModified);
		writer.write(newContent);
		reader.close();
		writer.close();
	}
	
	public void RemoveHere(String FilePath , String LineTochange) throws IOException
	{
		
		File fileToBeModified = new File(FilePath);
		String oldContent = "";
		BufferedReader reader = new BufferedReader(new FileReader(fileToBeModified));
		
		String line = reader.readLine();

		while (line != null) 
		{
		        oldContent = oldContent + line + System.lineSeparator();
		        line = reader.readLine();
		}
		String newContent = oldContent.replaceFirst("HERE-->","");
		FileWriter writer = new FileWriter(fileToBeModified);
		writer.write(newContent);
		reader.close();
		writer.close();
				

	}
	
	public boolean CheckIfFileContainsHere(String FilePath) throws IOException
	{
		FileInputStream fis = null;
		BufferedReader reader = null;
		fis = new FileInputStream(FilePath);
		reader = new BufferedReader(new InputStreamReader(fis));
		String line = reader.readLine();
		while(line!=null)
		{
			if (line.contains("HERE-->")) 
				{
					fis.close();
					reader.close();
					return true;
				}
			line = reader.readLine();
		}
		
		fis.close();
		reader.close();
		line = null;
		System.gc();
		
		return false;
	}
	
	public String GetLineContainsHere(String FilePath) throws IOException
	{

		FileInputStream fis = null;
		BufferedReader reader = null;
		fis = new FileInputStream(FilePath);
		reader = new BufferedReader(new InputStreamReader(fis));
		String line = reader.readLine();
		while(line!=null)
		{
			if (line.contains("HERE-->")) 
				{
					fis.close();
					reader.close();
					return line;
				}
			line = reader.readLine();
		}
		
		fis.close();
		reader.close();
		line = null;
		System.gc();
		reader.close();
		return null;
	}

	public int TotalAmountOfLinesInFile(String FilePath) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(FilePath));
		int lines = 0;
		while (reader.readLine() != null) 
		{
		    lines++;
		}
		reader.close();
		System.out.println(lines);
		return lines;
	}
	
	public int GetCurrentLineInFile(String FilePath) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(FilePath));
		int lines = 0;
		String line = null;
		while ((line=reader.readLine()) != null) 
		{
		    lines++;
		    //System.out.println(lines);
		  //  System.out.println(reader.readLine());
		    if (line.contains("HERE-->")) 
		    	{
			    System.out.println(line);
			    System.out.println(lines);
		    	reader.close();
		    	return lines;
		    	}
		}
		reader.close();
		System.out.println(lines);
		return 0;
	}
	
	
}
