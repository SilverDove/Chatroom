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
		
		public static DatabaseSQLite getInstance() {//get the unique instance of the database
			return uniqueInstance;
		}
		
		public void connect() {
			con = null;
			try {
				//Register JDBC driver 
				Class.forName(JDBC_DRIVER);
				//Open a connection
				System.out.println("Connecting to database...");
				con=DriverManager.getConnection(DB_URL);
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
				con.close();
				st.close();
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
		    	   st = con.createStatement();
		           st.execute(request);
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
			        + "	(idUser INTEGER PRIMARY KEY AUTOINCREMENT,"
			        + "	username VARCHAR(20) NOT NULL  , "
			        + "	firstname VARCHAR(20) NOT NULL, "
			        + "	lastname varchar(20) NOT NULL, "
			        + "	password varchar(20) NOT NULL,"
			        + "	lastConnection varchar(20),"
			        + " CONSTRAINT unique_username UNIQUE(username));";                
			ExecuteQuery(sql);	//execute the query     
			 
			sql = "CREATE TABLE IF NOT EXISTS message"
			        + "	(idMessage INTEGER PRIMARY KEY AUTOINCREMENT,"
			        + "	idUser1 INTEGER , "
			        + " idUser2 INTEGER , "
			        + " timeSent varchar(20) NOT NULL , "
			        + " message text NOT NULL, "
			        + "	FOREIGN KEY(idUser1) REFERENCES user(idUser), "
			        + "	FOREIGN KEY(idUser2) REFERENCES user(idUser), "
			        + " CONSTRAINT unique_message UNIQUE(idMessage, idUser1, idUser2)); "; 
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
			}finally {
			     close();//Close the database	
			}

		}
		
		public void updateTimeConnectionUser(String username) {//Update the connection time of the user
			//Query to change the date
		    sql = "UPDATE user set lastConnection = datetime('now') where username = '"+username+"'  ;";
		    ExecuteQuery(sql);	//execute the query
		        
		}
		
		public void SaveMessage(String messageSend, String userSender, String userReceiver)  {//Save messages send and receive 
			ResultSet rs = null;
			int idUser1 = 0,idUser2 = 0;
			connect();//Open the database
			
	        try {
	        	rs=ResultQuery("select idUser from user where username ='"+userSender+"';");	        
	        	while (rs.next()) {
	        		idUser1 = rs.getInt("idUser");
	        	}
			
	        	rs=ResultQuery("select idUser from user where username ='"+userReceiver+"';");
	        	while (rs.next()) {
	        		idUser2 = rs.getInt("idUser");
	        	}
	        	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		    ExecuteQuery("insert into message (idUser1,idUser2,timeSent,message) values ('"+idUser1+"', '"+idUser2+"', datetime('now'), '"+userSender+" :"+messageSend+"');");
			System.out.println("Message save in the table Message");
					
			updateTimeConnectionUser(userSender);
			System.out.println("Get time update!");
					
			//Close the database
			close();//Close the database


		}
			
		
		public ArrayList<Message> retrieveListOfMessageFromDiscussion(String username1,String username2){
			ArrayList<Message> listMessages = new ArrayList<Message>();
			ResultSet rs = null;
			Message message;
			int idUser1=0,idUser2 = 0,idMessage=0;
			String text, timeSent;
			connect();//Open the database
			

	        try {
	        	
				rs=ResultQuery("select idUser from user where username = '"+username1+"';");	        
				while (rs.next()) {
				    idUser1 = rs.getInt("idUser");
				}
				
				rs=ResultQuery("select idUser from user where username = '"+username2+"';");
				while (rs.next()) {
				    idUser2 = rs.getInt("idUser");
				}
				
				rs=ResultQuery("select idUser1, idUser2, idMessage, message, timeSent from message where (idUser1 ="+idUser1+" AND idUser2 ="+idUser2+") OR (idUser1 ="+idUser2+" AND idUser2 ="+idUser1+");");
				
				while (rs.next()) {
				    idUser1 = rs.getInt("idUser1");
				    idUser2 = rs.getInt("idUser2");
				    idMessage = rs.getInt("idMessage");
				    text = rs.getString("idMessage");
				    timeSent = rs.getString("timeSent");
				    message = new Message(idUser1,idUser2,idMessage,text,timeSent);
				    listMessages.add(message);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
				close();//close the database	
			
	        
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
						
						ExecuteQuery("delete from message where idUser = "+idUser+" and idRoom = "+idRoom+" and message = '"+messageToDelete+"';");
					}
					
			        updateTimeConnectionUser(username);
			        
			        System.out.println("Message deleted with success");
					
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {        
				     close();//close the database	
				}
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
				}finally {
				     close();//close the database	
				}
				
				return contactList;
		}
		
}
