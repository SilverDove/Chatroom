package MVC_Pattern;

import java.util.ArrayList;

import Items.Message;
import Items.User;

public interface IChatroomObserver {
	
	public void updateGUI(User userReceiver, User userPopUp);

}
