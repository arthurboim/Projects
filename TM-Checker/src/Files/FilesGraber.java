package Files;

import java.io.File;
import java.util.ArrayList;

public class FilesGraber 
{

	public ArrayList<String> GetFilesNameFromFolder(String path)
	{
		ArrayList<String> FilesList = new ArrayList<String>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) 
	    {	
	      if (listOfFiles[i].isFile()) 
	      {
	    	FilesList.add("D:\\Downloads\\TM-XML\\"+listOfFiles[i].getName());
	      } 
	    }
	    
	    PrintAllFilesInDirectory(FilesList);
	    
		return FilesList;
	}
	
	public void PrintAllFilesInDirectory(ArrayList<String> _list)
	{
		System.out.println("####Files start####");
		for(String ele:_list)
		{
			System.out.println(ele);
		}
		System.out.println("Total files: "+_list.size());
		System.out.println("####Files end ####");
	}
	
}
