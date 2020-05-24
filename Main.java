import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Main {
	
	public static void main(String args[]) throws ParseException {
		DatabaseSQLite db = DatabaseSQLite.getInstance();
		ArrayList<User> listUsers =new ArrayList<User>();
		ArrayList<Message> listMessage =new ArrayList<Message>();
		
		/*Creation of the database*/
		//db.createNewDatabase(); // Connections with the database and creation of tables if database is new		
		
		/*List of contacts*/
		User Clara = new User("Clacla","Clara","Tricot","1234");
		User Antoine = new User("Goldby","Antoine","Mairet","1234");
		User Karen = new User("Kk","Karen","Kaspar","1234");
		User Stella = new User("Stellouch","Stella","Thammavong","1234");
		User Nicolas = new User("Nico","Nicolas","Jatob","1234");
		
		db.CreateAccount(Clara);
		/*db.CreateAccount(Antoine);
		db.CreateAccount(Karen);
		db.CreateAccount(Stella);
		db.CreateAccount(Nicolas);
		
		listUsers = db.listContact();
		for (int i=0; i< listUsers.size(); i ++) {
			System.out.println("Surname: "+listUsers.get(i).getUsername()+" FirstName: "+listUsers.get(i).getFirstname()+" LastName: "+listUsers.get(i).getLastname()+" Password: "+listUsers.get(i).getPassword());
		}*/
		
		
		/*Create a Conversation*/
		/*listUsers.add(Stella);
		listUsers.add(Karen);
		db.creationRoomXUsers("DuoTest",listUsers);*/
		
		/*Add friend into a group*/
		//db.addFriend("Goldby", "DuoTest");
		
		/*LogIn*/	
		//db.logIn("Goldby","1234");
		
		/*Save Message*/
		/*db.SaveMessage("Coucou, �a va?","Kk","DuoTest");//Message not saved (WTF ?!)
		db.SaveMessage("Ouais et toi ?","Stellouch","DuoTest");
		db.SaveMessage("Tu as bien profit� ton week-end?","Stellouch","DuoTest");*/
		
		/*Delete Message*/
		//db.deleteMessage("Tu as bien profit� ton week-end?", "Stellouch", "DuoTest");//Delete message from the conversation
		
		/*List of Message*/
		/*listMessage = db.retrieveListOfMessageFromRoom(1);
		for(Message m : listMessage) {
			m.toString();
		}*/
		
		
		/*
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm"); 
	    Date aujourdhui = new Date();
	    String date = format.format(aujourdhui);
	    System.out.println(date);
	    Date date1 = format.parse(date);
	    System.out.println(date1);
	    date = format.format(date1);
	    System.out.println(date);
		*/
	}	

}