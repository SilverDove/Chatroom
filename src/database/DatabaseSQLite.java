package database;

/**
 * Date: June 2 2020
 * SQLite Database used to store messages exchanged between 2 users. It also stored information about the users who created a account. 
 * @author Clara TRICOT
 * @author Antoine MAIRET
 * 
 */

import java.sql.*;
import java.util.ArrayList;
import Items.*;


public class DatabaseSQLite extends AbstractDatabase {
	
		/**
		 * JDBC driver name
		 */
		private static final String JDBC_DRIVER = "org.sqlite.JDBC";
		/**
		 * Database URL
		 */
		private static final String DB_URL = "jdbc:sqlite:test.db";
		
		
	//Variables	
		/**
		 * String value holding a request
		 */
		private String sql;
		/**
		 * Connection value used to manage the connection with the database
		 */
		private Connection con = null;
		/**
		 * Statement value executing query request
		 */
		private Statement st = null;
		
	//Singleton Pattern
		/**
		 * DatabaseSQLite variable holding the instance of the database
		 */
		private static DatabaseSQLite uniqueInstance = new DatabaseSQLite();
	
		/**
		 * DatabaseSQLite constructor
		 */
		private DatabaseSQLite() {
			con = null;
			st = null;
		}
		
		/**
		 * Returns the unique instance of the database to respect the Singleton pattern
		 * @return returning DatabaseSQLite value corresponding to the database itself
		 */
		public static DatabaseSQLite getInstance() {
			return uniqueInstance;
		}
		
		/**
		 *Connection to the database
		 */
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
		
		/**
		 *Disconnect from the database
		 */
		public void close() {
			try {
				con.close();
				st.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		}
		
		/**
		 *Returns the result of the query
		 *@param request String variable corresponding to the request to execute
		 *@return returning ResultSet variable result from the execution of the query
		 */
		public ResultSet ResultQuery(String request) {//Get result from query
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
		
		/**
		 *Execute the query request
		 */
		public void ExecuteQuery(String request) {
		       try {
		    	   st = con.createStatement();
		           st.execute(request);
		       } catch (SQLException e) {
		           e.printStackTrace();
		           System.out.println("Request error : " + request);
		       }
		}
		
		/**
		 *Creation of the database
		 */
		public void createNewDatabase(){
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
		
		/**
		 * Add a new user into the database
		 * @param user User variable corresponding to the new user to add
		 */
		public void CreateAccount(User user) {
			
			connect();//Open the database
		        
		    //Query to add a user into the database
		    sql =  " INSERT INTO user ( username, firstname, lastname, password) "  
		        	+ " VALUES ('"+user.getUsername()+"','"+user.getFirstname()+"','"+user.getLastname()+"','"+user.getPassword()+"'); ";
		    ExecuteQuery(sql);//execute the query        
		        
		    close();//Close the database

		    System.out.println("New account created successfully");
		   
		}
		
		/**
		 * Check if a user is register in the database 
		 * @param username String variable corresponding to the user's username
		 * @param password String variable corresponding to the user's password
		 * @return returning true if the user exists, false otherwise
		 */
		public boolean logIn(String username, String password) {
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
				      
				    //Check if the username and password match with a user
				    if ( (username.equals(username1)) && (password.equals(password1)) ) { //If it matches 
				    	flag=true;
				    	System.out.println("Welcome back,"+firstname+" "+lastname);
				    	
				    	updateTimeConnectionUser(username);//Update user connection time
				    	return true;
				    }
				 }
				
				if (flag==false) { // we didn't find it 
			    	 System.out.println("Username or password wrong ! Retry");
			    	 return false;
			     }
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
			     close();//Close the database
			}
		    
		    return false;

		}
		
		/**
		 * Update the connection time of the user
		 * @param username String variable corresponding to the user's username
		 */
		public void updateTimeConnectionUser(String username) {
			//Query to change the date
		    sql = "UPDATE user set lastConnection = datetime('now','localtime') where username = '"+username+"'  ;";
		    ExecuteQuery(sql);	//execute the query       
		}
		
		/**
		 * Save messages
		 * @param messageSend String variable corresponding to the message sent
		 * @param userSender String variable corresponding to the sender's username
		 * @param userReceiver String variable corresponding to the receiver's username
		 */
		public void SaveMessage(String messageSend, String userSender, String userReceiver)  {
			ResultSet rs = null;
			int idUser1 = 0,idUser2 = 0;
			connect();//Open the database
			
	        try {
	        	//Get user id of the Sender
	        	rs=ResultQuery("select idUser from user where username ='"+userSender+"';");	        
	        	while (rs.next()) {
	        		idUser1 = rs.getInt("idUser");
	        	}
	        	
	        	//Get user id of the Receiver
	        	rs=ResultQuery("select idUser from user where username ='"+userReceiver+"';");
	        	while (rs.next()) {
	        		idUser2 = rs.getInt("idUser");
	        	}
	        	
			} catch (SQLException e) {
				e.printStackTrace();
			}
	        
		    ExecuteQuery("insert into message (idUser1,idUser2,timeSent,message) values ('"+idUser1+"', '"+idUser2+"', datetime('now','localtime'), '"+messageSend+"');");//execute query
			System.out.println("Message save in the table Message");
			
			//Update time connection of the users
			updateTimeConnectionUser(userSender);
			updateTimeConnectionUser(userReceiver);
					
			close();//Close the database


		}
		
		/**
		 * Returns the conversation of 2 people
		 * @param username1 String variable corresponding to the first user
		 * @param username2 String variable corresponding to the second user
		 * @return
		 */
		public ArrayList<Message> retrieveListOfMessageFromDiscussion(String username1,String username2){
			ArrayList<Message> listMessages = new ArrayList<Message>();
			ResultSet rs = null;
			Message message;
			int idUser1=0,idUser2 = 0,idMessage=0;
			String text, timeSent;
			
			connect();//Open the database
			
	        try {
	        	//Get user id of the Sender
				rs=ResultQuery("select idUser from user where username = '"+username1+"';");	        
				while (rs.next()) {
				    idUser1 = rs.getInt("idUser");
				}
				//Get user id of the Receiver
				rs=ResultQuery("select idUser from user where username = '"+username2+"';");
				while (rs.next()) {
				    idUser2 = rs.getInt("idUser");
				}
				
				//Query to get messages from 2 users
				rs=ResultQuery("select idUser1, idUser2, idMessage, message, timeSent from message where (idUser1 ="+idUser1+" AND idUser2 ="+idUser2+") OR (idUser1 ="+idUser2+" AND idUser2 ="+idUser1+");");
				
				while (rs.next()) {//Save all the messages into a list
				    idUser1 = rs.getInt("idUser1");
				    idUser2 = rs.getInt("idUser2");
				    idMessage = rs.getInt("idMessage");
				    text = rs.getString("message");
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
		
		/**
		 * Returns the list of contact
		 * @return ArrayList of User corresponding to the list of user in the database
		 */
		public ArrayList<User> listContact() {
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
		
		/**
		 * Returns user from the username
		 * @param username String variable corresponding to the user's username
		 * @return User variable corresponding to the user we wanted to find from the username
		 */
		public User getUser(String username) {
			//Variables
			ResultSet rs = null;	
			String userName = null, firstName = null, lastName = null, password= null;
			User user = null;
			
			connect();//Open the database
				
		        //Query to get the user
		        rs=ResultQuery("select * from user where username = '"+username+"';");
		        try {
					while (rs.next()) {
						userName = rs.getString("username");
						firstName = rs.getString("firstname");
						lastName = rs.getString("lastname");
						password = rs.getString("password");
						
						user = new User(userName, firstName, lastName, password);
						
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
				     close();//close the database	
				}
				
				return user;
		}
		
		/**
		 * Get use's id from the given username
		 * @param username String variable corresponding to the user's username
		 * @return int variable corresponding to the user's id
		 */
		public int getIDFromUsername(String username) {
			//Variables
			ResultSet rs = null;	
			int id=0;
			
			connect();//Open the database
				
		        //Query to get the user 
		        rs=ResultQuery("select idUser from user where username = '"+username+"';");
		        try {
					while (rs.next()) {
						id = rs.getInt("idUser");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
				     close();//close the database	
				}
				
			return id;
			
		}
		
		/**
		 * Get list of the users excepted the current user using the chatroom
		 * @param username String variable corresponding to the user's username
		 * @return ArrayList of User corresponding to the list of user without the current user using chatroom
		 */
		public ArrayList<User> getListConnectedUser(String username) {
			ResultSet rs = null;	
			
			String userName, firstName, lastName, password;			
			ArrayList<User> contactList = new ArrayList<User>();
			
			connect();//Open the database
				
		        //Query to get the contact list of connected users 
		        rs=ResultQuery("select * from user;");
		        try {
					while (rs.next()) {
						userName = rs.getString("username");
						firstName = rs.getString("firstname");
						lastName = rs.getString("lastname");
						password = rs.getString("password");
						
						if(userName.equals(username) == false) { //If we didn't find the user currently login
							contactList.add(new User(userName, firstName, lastName, password));//Add the connected user into the list
						}
						
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
				     close();//close the database	
				}
				
				return contactList;
		}
		
}
