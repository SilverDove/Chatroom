import java.util.Date;
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
		
		//db.createNewDatabase(); // Connections with the database and creation of tables if database is new		
		
		//db.addFriend("Goldby", "DuoTest");
		
		User Clara = new User("Clacla","Clara","Tricot","1234");
		User Antoine = new User("Goldby","Antoine","Mairet","1234");
		User Karen = new User("Kk","Karen","Kaspar","1234");
		User Stella = new User("Stellouch","Stella","Thammavong","1234");
		User Nicolas = new User("Nico","Nicolas","Jatob","1234");
		
				
		/*db.CreateAccount(Clara);
		db.CreateAccount(Antoine);
		db.CreateAccount(Karen);
		db.CreateAccount(Stella);
		db.CreateAccount(Nicolas);*/
		
		/*listUsers.add(Stella);
		listUsers.add(Karen);
		db.creationRoomXUsers("DuoTest6",listUsers); //ERROR HERE
		*/
		
		//db.logIn("Goldby","1234");
		
		db.SaveMessage("Message 1","Kk","DuoTest");//Message not saved (WTF ?!)
		db.SaveMessage("Message 2","Stellouch","DuoTest");
		db.SaveMessage("Message 3","Stellouch","DuoTest");
		
		//db.deleteMessage("Message 2", "Stellouch", "DuoTest");//Delete message from the conversation
		
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