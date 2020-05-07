import java.util.ArrayList;

public class Main {
	
	public static void main(String args[]) {
		Database db = new Database();
		ArrayList<User> listUsers =new ArrayList<User>();
		User Clara = new User("Clacla","Clara","Tricot","1234");
		User Antoine = new User("Goldby","Antoine","Mairet","1234");
		User Karen = new User("Kk","Karen","Kaspar","1234");
		User Stella = new User("Stellouch","Stella","Thammavong","1234");
		User Nicolas = new User("Nico","Nicolas","Jatob","1234");
		
		//db.createNewDatabase(); // Connections with the database and creation of tables if database is new				
		//db.addUser(Clara);
		/*db.addUser("Goldby","Antoine","Mairet","1234");
		db.addUser("Stellouch","Stella","Thammavong","1234");
		db.addUser("Kk","Karen","Kaspar","1234");
		db.addUser("Tabouche","Metagang","Tabou","1234");
		db.addUser("Nico","Nicolas","Jatob","1234");*/
		
		/*listUsers.add(Stella);
		listUsers.add(Karen);
		db.creationRoomXUsers("DuoTest",listUsers);
		
		db.logIn("Goldby","1234");
				
		db.updateTimeConnectionUser("Goldby");
		
		db.viewTable("user");
		db.viewTable("message");
		db.viewTable("participants");
		db.viewTable("room");*/
		
	}	

}