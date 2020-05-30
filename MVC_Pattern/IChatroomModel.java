package MVC_Pattern;

import java.util.ArrayList;

import Items.Message;
import Items.User;

public interface IChatroomModel {
	
	public void modelCreationDatabase();
	public void modelNewAccount(User newUser);
	public boolean modelLogIn(String username, String password);
	public void modelSaveMessage(String messageSend);
	public ArrayList<Message> modelGetListMessageFromConversation();
	public ArrayList<User> modelGetListContact();
	public User modelGetUser(String username);
	public User modelGetReceiverUser(String username);
	public int modelGetIDFromUsername(String username);
	public ArrayList<User> modelGetConnectedUsers();
	public void registerObserver(IChatroomObserver o);
	public void removeObserver(IChatroomObserver o);
	public void notifyObservers(User userPopUp);
	public User getUserLogin();
	public void setUserLogin(User userLogin);
	public User getUserReceiver();
	public void setUserReceiver(User userReceiver);

}
