import java.sql.ResultSet;

public abstract class AbstractDatabase {
	
	public abstract void connect();
	public abstract void close();
	public abstract void createNewDatabase();
	public abstract ResultSet ResultQuery(String request);
	public abstract void ExecuteQuery(String request);

}
