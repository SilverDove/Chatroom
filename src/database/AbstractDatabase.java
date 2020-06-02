package database;
import java.sql.ResultSet;

/**
 * Date: June 2 2020
 * SQLite Database used to store messages exchanged between 2 users. It also stored information about the users who created a account. 
 * @author Clara TRICOT
 * @author Antoine MAIRET
 * 
 */

public abstract class AbstractDatabase {
	
	public abstract void connect();
	public abstract void close();
	public abstract void createNewDatabase();
	public abstract ResultSet ResultQuery(String request);
	public abstract void ExecuteQuery(String request);

}
