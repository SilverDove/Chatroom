package MVC_Pattern;

import java.util.ArrayList;

import Items.Message;
import Items.User;

public interface IChatroomController {
	
	public void controllerCreationDatabase();
	public void controllerNewAccount(User newUser);
	public boolean controllerLogIn(String username, String password);
	public void controllerSaveMessage(String messageSend);
	public ArrayList<Message> controllerGetListMessageFromConversation();
	public ArrayList<User> controllerGetListContact();
	public User controllerGetUser(String username);
	public int controllerGetIDFromUsername(String username);
	public ArrayList<User> controllerGetListConnectedUsers();
	public User controllerGetUserLogin();
	public void controllerSetUserLogin(User userLogin);
	public User controllerGetUserReceiver();
	public void controllerSetUserReceiver(User userReceiver);
	public User controllerGetReceiverUser(String username);

}
