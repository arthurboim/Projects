package DataBase;

import java.sql.ResultSet;



public interface IDataBase {
	
	public void OpenConnection();
	
	public void CloseConnection();
	
	public void Write(String statment);
	
	public ResultSet Read(String statment);

	public Boolean isExistInDataBase (String statment);
}