package MVC_Pattern;

import java.util.ArrayList;
import Database.*;
import Items.Message;
import Items.User;

/* --- Model which communicates with the database to get/update data ---*/
public class ChatroomModel implements IChatroomModel{
	
	private DatabaseSQLite db;
	private User UserLogin;
	private User UserReceiver;
	private ArrayList<IChatroomObserver> observers = new ArrayList<IChatroomObserver>();
	
	public ChatroomModel() {
		System.out.println("INDISDE constructor of ChatroomModel");
		db = DatabaseSQLite.getInstance();
		UserLogin = null;
		UserReceiver = null;
	}
	
	public void modelCreationDatabase() {
		db.createNewDatabase();
	}
	
	public User getUserLogin() {
		return UserLogin;
	}

	public void setUserLogin(User userLogin) {
		UserLogin = userLogin;
	}
	
	public User getUserReceiver() {
		return UserReceiver;
	}

	public void setUserReceiver(User userReceiver) {
		UserReceiver = userReceiver;
	}

	public void modelNewAccount(User newUser) {
		db.CreateAccount(newUser);
	}
	
	public boolean modelLogIn(String username, String password) {
		boolean flag = db.logIn(username, password);;
		if(db.logIn(username, password)==true) {//User is in the database
			this.setUserLogin(this.modelGetUser(username));
		}
		return flag;
	}
	
	public void modelSaveMessage(String messageSend) {
		db.SaveMessage(messageSend, this.UserLogin.getUsername(), UserReceiver.getUsername());
	}
	
	public ArrayList<Message> modelGetListMessageFromConversation(){
		return db.retrieveListOfMessageFromDiscussion(UserLogin.getUsername(), UserReceiver.getUsername());
	}

	public ArrayList<User> modelGetListContact(){
		return db.listContact();
	}
	
	public User modelGetUser(String username) {//When I click on a contact, I want to get its username
		return db.getUser(username);
	}
	
	public User modelGetReceiverUser(String username) {
		notifyObservers(null);
		return db.getUser(username);
	}
	
	public int modelGetIDFromUsername(String username) {
		return db.getIDFromUsername(username);
	}
	
	public ArrayList<User> modelGetConnectedUsers(){
		return db.getListConnectedUser(UserLogin.getUsername());
	}
	
	//implementation of the Subject interface (Observer design pattern)
	public void registerObserver(IChatroomObserver o) {
			observers.add(o);
		   }
		   
	public void removeObserver(IChatroomObserver o) {
		int i = observers.indexOf(o);
		if (i >= 0) observers.remove(i);
	}
	
	public void notifyObservers(User userPopUp){//Notify Observers
		System.out.println("notify observers ");
		   for (int i = 0; i < observers.size(); i++) {
			IChatroomObserver o = observers.get(i);
			o.updateGUI( UserReceiver, userPopUp);
		   }
	}
	
	//TODO: create function to call notifyObservers when receive a message
}
