import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.h2.jdbc.JdbcSQLSyntaxErrorException;

public class DatabaseSQLite extends AbstractDatabase {
	
	//JDBC driver name and database URL
		private static final String JDBC_DRIVER = "org.h2.Driver";
		private static final String DB_URL = "jdbc:h2:tcp://localhost/~/test";
		
	//Database credentials
		private static final String USER = "sa";
		private static final String PASS = "";
		
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
		
		public static DatabaseSQLite getInstance() {//get the unique instance of the database
			return uniqueInstance;
		}
		
		public void connect() {
			con = null;
			st = null;
			try {
				//Register JDBC driver 
				Class.forName(JDBC_DRIVER);
				//Open a connection
				System.out.println("Connecting to database...");
				con=DriverManager.getConnection(DB_URL, USER, PASS);
				//Execute a query
				st= con.createStatement();
				
				System.out.println("*** Connection succeed ***");
				
	        } catch (ClassNotFoundException notFoundException) {
	            notFoundException.printStackTrace();
	            System.out.println("Connection error");
	        } catch (SQLException sqlException) {
	            sqlException.printStackTrace();
	            System.out.println("Connection error");
	        }
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
		    	   st = con.createStatement();
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
			System.out.println("Creating tables in given database ...");
			
			sql = "CREATE TABLE IF NOT EXISTS user"
			        + "	(idUser INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,"
			        + "	username VARCHAR(20) NOT NULL  , "
			        + "	firstname VARCHAR(20) NOT NULL, "
			        + "	lastname varchar(20) NOT NULL, "
			        + "	password varchar(20) NOT NULL,"
			        + "	lastConnection varchar(20),"
			        + " CONSTRAINT unique_username UNIQUE(username));";                
			ExecuteQuery(sql);	//execute the query     
			
			sql = "CREATE TABLE IF NOT EXISTS room"
			        + "	(idRoom integer PRIMARY KEY AUTO_INCREMENT NOT NULL,"
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
			        + "	(idMessage INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,"
			        + "	idUser INTEGER , "
			        + " idRoom INTEGER , "
			        + " timeSent varchar(20) NOT NULL , "
			        + " message text NOT NULL, "
			        + "	FOREIGN KEY(idUser) REFERENCES user(idUser), "
			        + "	FOREIGN KEY(idRoom) REFERENCES room(idRoom),"
			        + " CONSTRAINT unique_message UNIQUE(idMessage, idUser, idRoom)); "; 
			ExecuteQuery(sql);	//execute the query     


			System.out.println("Tables created successfully");
			
			close();//Close the database	
				
		}
		
		public void CreateAccount(User user) {//Add a new user into the database (sign up)
			
			connect();//Open the database
		        
		    //Query to add a user into the database
		    sql =  " INSERT INTO user ( username, firstname, lastname, password) "  
		        	+ " VALUES ('"+user.getUsername()+"','"+user.getFirstname()+"','"+user.getLastname()+"','"+user.getPassword()+"'); ";
		    ExecuteQuery(sql);//execute the query        
		        
		    close();//Close the database

		    System.out.println("New account created successfully");
		   
		}
		
		public void logIn(String username, String password) {// Check if a user can use the app
			boolean flag=false;
			ResultSet rs = null;
			
			connect();//Open the database
		         
		    //Queries to check if the user exists in the database
		    rs = ResultQuery("SELECT * FROM user;");// Fetch all the users
		        
		    try {
				while (rs.next()) { // We go through all users and stop if we find it	
				    String  username1 = rs.getString("username");
				    String  firstname = rs.getString("firstname");
				    String  lastname = rs.getString("lastname");
				    String password1 = rs.getString("password");
				       
				    if ( (username.equals(username1)) && (password.equals(password1)) ) { // Check if the username and password match with a user
				    	flag=true;
				    	System.out.println("Welcome back,"+firstname+" "+lastname);
				    	
				    	updateTimeConnectionUser(username);
				    	break;
				    }
				 }
				
				if (flag==false) { // we didn't find it 
			    	 System.out.println("Username or password wrong ! Retry");
			     }
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
	   
		     close();//Close the database
			
		}
		
		public void updateTimeConnectionUser(String username) {//Update the connection time of the user
			//Query to change the date
		    sql = "UPDATE user set lastConnection = FORMATDATETIME(CURRENT_TIMESTAMP(), 'yyyy-MM-dd hh:mm:ss') where username = '"+username+"'  ;";
		    System.out.println("Please");
		    ExecuteQuery(sql);	//execute the query
		    System.out.println("At the end of updateTimeConnecionUser");
		        
		}
		
		public void SaveMessage(String messageSend, String username, String roomName) {//Save messages send and receive 
			ResultSet rs = null;	
			
			connect();//Open the database
				
		        //Query to save a message into the database
		        rs=ResultQuery("select idUser, idRoom from participants where idUser is (select idUser from user where username = '"+username+"') and idRoom is (select idRoom from room where name = '"+roomName+"');");
		        System.out.println("Get query of Save Message");
		        int idUser, idRoom;
		        try {
					while (rs.next()) {
						idUser = rs.getInt("idUser");
						idRoom = rs.getInt("idRoom");
						
						con.createStatement().execute("insert into message (idUser,idRoom,timeSent,message) values ('"+idUser+"', '"+idRoom+"', FORMATDATETIME(CURRENT_TIMESTAMP(), 'yyyy-MM-dd hh:mm:ss'), '"+messageSend+"');");
						System.out.println("Get query of Save Message 2");
					}
					
					System.out.println("Message save");
					
					updateTimeConnectionUser(username);
					System.out.println("Get time update!");
					
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
				      //Close the database
						close();//Close the database
				}

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
			
			System.out.println("1");
				
			sql = " INSERT INTO Room ( name, creationDate) "   // Creation of the room
			       + " VALUES ('"+nameRoom+"','"+date+"'); ";
			ExecuteQuery(sql);//execute the query
			
			System.out.println("2");
			    	
			rs=ResultQuery("SELECT MAX(idRoom) as idRoom FROM room;"); // Fetch the id of the room created before
			System.out.println("3");
			int idRoom=-1;
			int idUser = -1;
			try {
				while (rs.next()) {
					idRoom = rs.getInt("idRoom");
				}
				System.out.println("BEFORE FOR: "+idRoom);
				for  (User user : listUsers) {
					// Fetch the userId of the user1
					System.out.println("FOR ;)");
				   rs=ResultQuery("SELECT idUser FROM user where username = '"+user.getUsername()+"';"); 
				   System.out.println("4");
				   while (rs.next()) {
					   idUser = rs.getInt("idUser");
				   }
				   		    			    
				   // Fill the table participants to link user with room
				    sql = " INSERT INTO Participants ( idUser, idRoom) "  
						+ " VALUES ('"+idUser+"','"+idRoom+"'); ";
				    ExecuteQuery(sql);
				    System.out.println("5");
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
			
			rs=ResultQuery("select idMessage, idUser,timeSent,message from Message where idRoom ="+idRoom+";");
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
		
		public void deleteMessage(String messageToDelete, String username, String roomName) {//Delete message from the conversation
			ResultSet rs = null;	
			
			connect();//Open the database
				
		        //Query to delete a message from the database
		        rs=ResultQuery("select idUser, idRoom from participants where idUser is (select idUser from user where username = '"+username+"') and idRoom is (select idRoom from room where name = '"+roomName+"');");
		        try {
					while (rs.next()) {
						int idUser = rs.getInt("idUser");
						int idRoom = rs.getInt("idRoom");
						
						con.createStatement().execute("delete from message where idUser = "+idUser+" and idRoom = "+idRoom+" and message = '"+messageToDelete+"';");
					}
					
			        updateTimeConnectionUser(username);
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
		        
		      //Close the database
				close();//Close the database
		}
		
		public ArrayList<User> listContact() {//returns the list of contact
			ResultSet rs = null;	
			
			String userName, firstName, lastName, password;
			
			ArrayList<User> contactList = new ArrayList<User>();
			
			connect();//Open the database
				
		        //Query to get the contact list 
		        rs=ResultQuery("select * from user;");
		        try {
					while (rs.next()) {
						userName = rs.getString("username");
						firstName = rs.getString("firstname");
						lastName = rs.getString("lastname");
						password = rs.getString("password");
						
						contactList.add(new User(userName, firstName, lastName, password));
						
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
		        
		      //Close the database
				close();//Close the database
				
				return contactList;
		}
		
		public void addFriend(String userName, String roomName) {//add a user into a conversation
			
			ResultSet rs = null, rs2=null;
			
			String userID= null, roomID = null;
			
			connect();//Open the database
				
		        //Query to get the contact list 
		        rs=ResultQuery("select idUser from user where username = '"+userName+"';");
		        System.out.println("YES");
		        rs2=ResultQuery("select idRoom from room where name = '"+roomName+"';");
		        System.out.println("Yes2");
		        try {
		        	while (rs.next()) {
						userID = rs.getString("idUser");
		        	}
		        	System.out.println("PLEASE");
		        	while (rs2.next()) {
						roomID = rs2.getString("idRoom");
		        	}
					
		        	con.createStatement().execute("insert into Participants (idRoom, idUser) values ('"+roomID+"','"+userID+"');");
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
		        
		      //Close the database
				close();//Close the database
		}
		
		/*public Message getMessage() {
			//TODO:get the latest message from the room 
		}*/
}
