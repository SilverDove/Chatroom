import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseSQLite extends AbstractDatabase {
	
	//JDBC driver name and database URL
		private static final String JDBC_DRIVER = "org.sqlite.JDBC";
		private static final String DB_URL = "jdbc:sqlite:test.db";
		
	//Variables	
		private String sql;
		private Connection con = null;
		private Statement st = null;
		private SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm"); 
		
	//Singleton Pattern
		private static DatabaseSQLite uniqueInstance = new DatabaseSQLite();
		
		public DatabaseSQLite() {
			con = null;
			st = null;
		}
		
		public static DatabaseSQLite getInstance() {
			return uniqueInstance;
		}
		
		public void connect() {
			try {
				//Register JDBC driver 
				Class.forName(JDBC_DRIVER);
				//Open a connection
				con=DriverManager.getConnection(DB_URL);
				//Execute a query
				st= con.createStatement();
	        } catch (ClassNotFoundException notFoundException) {
	            notFoundException.printStackTrace();
	            System.out.println("Connection error");
	        } catch (SQLException sqlException) {
	            sqlException.printStackTrace();
	            System.out.println("Connection error");
	        }
			
			System.out.println("*** Connection succeed ***");
		}
		
		public void close() {
			try {
				st.close();
				con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		}
		
		public ResultSet ResultQuery(String request) {
			ResultSet result = null;
		       try {
		           result = st.executeQuery(request);
		       } catch (SQLException e) {
		           e.printStackTrace();
		           System.out.println("Request error : " + request);
		       }	
		       
		      return result;
		}
		
		public void ExecuteQuery(String request) {
		       try {
		           st.execute(request);
		           System.out.println("rs succeed");
		       } catch (SQLException e) {
		           e.printStackTrace();
		           System.out.println("Request error : " + request);
		       }	  
		}
		
		public void createNewDatabase(){//Creation of our database	
			connect();//Open the database
				
				//Create tables if they don't exist

				sql = "CREATE TABLE IF NOT EXISTS user"
		                + "	(idUser integer PRIMARY KEY AUTOINCREMENT ,"
		                + "	username varchar(20) NOT NULL  , "
		                + "	firstname varchar(20)NOT NULL, "
		                + "	lastname varchar(20)NOT NULL, "
		                + "	password varchar(20) NOT NULL,"
		                + "	lastConnection varchar(20),"
		                + " CONSTRAINT unique_username UNIQUE(username));";                
				ExecuteQuery(sql);	//execute the query     
				
				sql = "CREATE TABLE IF NOT EXISTS room"
		                + "	(idRoom integer PRIMARY KEY AUTOINCREMENT,"
		                + "	name varchar(20) NOT NULL, "
		                + "	creationDate varchar(20),"
		                + " CONSTRAINT unique_room UNIQUE(name)); ";             
				ExecuteQuery(sql);	//execute the query     
				
				sql = "CREATE TABLE IF NOT EXISTS participants"
		                + "	(idRoom INTEGER ,"
		                + "	idUser INTEGER , "
		                + "	FOREIGN KEY(idUser) REFERENCES user(idUser), "
		                + "	FOREIGN KEY(idRoom) REFERENCES room(idRoom),"
		                + " CONSTRAINT unique_user_room UNIQUE(idRoom, idUser)); ";
				ExecuteQuery(sql);	//execute the query     
				
				sql = "CREATE TABLE IF NOT EXISTS message"
		                + "	(idMessage INTEGER PRIMARY KEY AUTOINCREMENT,"
		                + "	idUser INTEGER , "
		                + " idRoom INTEGER , "
		                + " timeSent varchar(20) NOT NULL , "
		                + " message text NOT NULL, "
		                + "	FOREIGN KEY(idUser) REFERENCES user(idUser), "
		                + "	FOREIGN KEY(idRoom) REFERENCES room(idRoom)"
		                + " CONSTRAINT unique_message UNIQUE(idMessage, idUser, idRoom)); "; 
				ExecuteQuery(sql);	//execute the query     
				
				close();//Close the database
			
				System.out.println("Tables created successfully");
		}
		
		public void addUser(User user) {//Add a new user into the database (sign up)
			
			connect();//Open the database
		        
		    //Query to add a user into the database
		    sql =  " INSERT INTO user ( username, firstname, lastname, password) "  
		        	+ " VALUES ('"+user.getUsername()+"','"+user.getFirstname()+"','"+user.getLastname()+"','"+user.getPassword()+"'); ";
		    ExecuteQuery(sql);//execute the query        
		        
		    close();//Close the database

		    System.out.println("Records created successfully");
		   
		}
		
		public void logIn(String username, String password) {// Check if a user can use the app
			boolean flag=false;
			ResultSet rs = null;
			
			connect();//Open the database
		         
		    //Queries to check if the user exists in the database
		    rs = ResultQuery("SELECT * FROM user;");// Fetch all the users
		        
		    try {
				while ( (rs.next()) && (flag==false) ) { // We go through all users and stop if we find it	
					//int id = rs.getInt("idUser");
				    String  username1 = rs.getString("username");
				    String  firstname = rs.getString("firstname");
				    String  lastname = rs.getString("lastname");
				    String password1 = rs.getString("password");
				       
				    if ( (username.equals(username1)) && (password.equals(password1)) ) { // Check if the username and password match with a user
				    	flag=true;
				    	System.out.println("Welcome back,"+firstname+" "+lastname);
				    }
				 }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		     if (flag==false) { // we didn't find it 
		    	 System.out.println("Username or password wrong ! Retry");
		     }
		    
		     close();//Close the database
			
		}
		
		public void updateTimeConnectionUser(String username) {//Update the connection time of the user
			
			connect();//Open the database
			Date aujourdhui = new Date();
		    String date = format.format(aujourdhui);	
			//Query to change the date
		    sql = "UPDATE user set lastConnection = '"+date+"' where username = '"+username+"'  ;";
		    ExecuteQuery(sql);	//execute the query  
		        
		    close();//Close the database
		}
		
		public void SaveMessage(String messageSend, String username, String roomName) {//Save messages send and receive 
			ResultSet rs = null;	
			
			connect();//Open the database
				
		        //Query to save a message into the database
		        rs=ResultQuery("select idUser, idRoom from participants where idUser is (select idUser from user where username = '"+username+"') and idRoom is (select idRoom from room where name = '"+roomName+"');");
		        try {
					while (rs.next()) {
						int idUser = rs.getInt("idUser");
						int idRoom = rs.getInt("idRoom");
						
						ExecuteQuery("insert into message (idUser,idRoom,timeSent,message) values ('"+idUser+"', '"+idRoom+"', datetime('now'), '"+messageSend+"');");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
		        
		        updateTimeConnectionUser(username);//MAYBE USE TRIGGER
		        
		      //Close the database
				close();//Close the database
		}
			
		public void creationRoomXUsers(String nameRoom,ArrayList<User> listUsers) {
			StringBuilder sb = new StringBuilder();
			ResultSet rs = null;
		    Date aujourdhui = new Date();
		    String date = format.format(aujourdhui);
			connect();//Open the database
				
			if (nameRoom == null) { // Change the name of the conversation by a default one
				for(User user : listUsers) {	
					sb.append(user.getUsername()+", ");
				}
				sb.delete(sb.length()-2, sb.length());
				nameRoom = sb.toString();
			}
				
			sql = " INSERT INTO Room ( name, creationDate) "   // Creation of the room
			       + " VALUES ('"+nameRoom+"','"+date+"'); ";
			ExecuteQuery(sql);//execute the query
			    	
			rs=ResultQuery("SELECT MAX(idRoom) as idRoom FROM room;"); // Fetch the id of the room created before
			int idRoom;
			try {
				idRoom = rs.getInt("idRoom");
		   	
				for  (User user : listUsers) {
					// Fetch the userId of the user1
				   rs=ResultQuery("SELECT idUser FROM user where username = '"+user.getUsername()+"';"); 
				   int idUser = rs.getInt("idUser");		    	
			    			    
				   // Fill the table participants to link user with room
				    sql = " INSERT INTO Participants ( idUser, idRoom) "  
						+ " VALUES ('"+idUser+"','"+idRoom+"'); ";
				    ExecuteQuery(sql);
				 }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		        
		     close();//close the database			
		}		
		
		public ArrayList<Message> retrieveListOfMessageFromRoom(int idRoom){
			ArrayList<Message> listMessages = new ArrayList<Message>();
			ResultSet rs = null;
			Message message;
			int idUser,idMessage;
			String text, timeSent;
			connect();//Open the database
			
			rs=ResultQuery("select idMessage, idUser,timeSent,message from Message where idRoom ="+idRoom+"");
	        try {
				while (rs.next()) {
				
				    idMessage = rs.getInt("idMessage");
				    idUser = rs.getInt("idUser");
				    timeSent = rs.getString("timeSent");
				    text = rs.getString("message");
				    
				    
				    message = new Message(idMessage,idUser,text,timeSent);			    
				    listMessages.add(message);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	        
			return listMessages;
		}
}
