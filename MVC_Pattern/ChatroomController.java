package MVC_Pattern;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Items.Message;
import Items.User;

/*--- acts on both model and view(GUI).
 It controls the data flow into model object and updates the view whenever data changes. 
 It keeps view and model separate. ---*/

public class ChatroomController implements IChatroomController {
	
	private IChatroomModel model;
	private IChatroomView view;
	
	public ChatroomController(IChatroomModel m) {
		this.model = m;
		this.view= new GUI(model, this);	
	}
		
	public void controllerCreationDatabase() {
		model.modelCreationDatabase();
	}
	
	public void controllerNewAccount(User newUser) {
		model.modelNewAccount(newUser);
	}
	
	public boolean controllerLogIn(String username, String password) {
		return model.modelLogIn(username, password);
	}
	
	public void controllerSaveMessage(String messageSend) {
		model.modelSaveMessage(messageSend);
	}
	
	public ArrayList<Message> controllerGetListMessageFromConversation(){
		return model.modelGetListMessageFromConversation();
	}
	
	public ArrayList<User> controllerGetListContact(){
		return model.modelGetListContact();
	}
	
	public User controllerGetUser(String username) {
		return model.modelGetUser(username);
	}
	
	public User controllerGetReceiverUser(String username) {
		return model.modelGetReceiverUser(username);
	}
	
	public int controllerGetIDFromUsername(String username) {
		return model.modelGetIDFromUsername(username);
	}
	
	public ArrayList<User> controllerGetListConnectedUsers(){
		return model.modelGetConnectedUsers();
	}

	@Override
	public User controllerGetUserLogin() {
		return model.getUserLogin();
	}

	@Override
	public void controllerSetUserLogin(User userLogin) {
		model.setUserLogin(userLogin);
		
	}

	@Override
	public User controllerGetUserReceiver() {
		return model.getUserReceiver();
	}

	@Override
	public void controllerSetUserReceiver(User userReceiver) {
		model.setUserReceiver(userReceiver);
		
	}
	
	
}
