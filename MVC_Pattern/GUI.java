package MVC_Pattern;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import javax.swing.event.ListSelectionEvent;

import Items.*;


public class GUI implements ActionListener, ListSelectionListener, IChatroomView, IChatroomObserver {
	private IChatroomModel chatroomModel;
	private IChatroomController chatroomController;
	
	/* --- Variables --- */
	private User currentUser;//User currently using the software
	private User contactUser;//User with whom currentUser is talking to
	private ArrayList<User> listConnectedUser;//List of all connected User
	private ArrayList<Message> listMessage;//List of all the messages between currentUser and contactUser
	
	//String[] data = {"Bob", "Alice", "Jean", "Patrick"}; //Contacts list for example
	//JList<String> contactsList = new JList<String>(data);
	
	JList<String> contactsList = new JList<String>();//List of contact 
	
	//Creation of frames
	JFrame principal_frame = new JFrame("Chat GUI");
	JFrame new_account_frame = new JFrame("New account");
	JFrame connection_frame = new JFrame("Log in");
	JFrame error_frame = new JFrame("Error");
	
	//Creation of panels
	JPanel accountPanel = new JPanel();
	JPanel loginPanel = new JPanel();
	JPanel messagesPanel = new JPanel();
	JPanel contactsPanel = new JPanel();
	JPanel textPanel = new JPanel();

	//Creation of buttons
	JButton send = new JButton("SEND");
	JButton log_in = new JButton("Log in");
	JButton new_account = new JButton("Create account");
	JButton create_account = new JButton("OK");
	
	
	//Creation of text areas
	JTextArea message_textArea = new JTextArea(6, 105);
	JTextArea username_textArea = new JTextArea(1, 10);
	JTextArea password_textArea = new JTextArea(1, 10);
	JTextArea new_username_textArea = new JTextArea(1, 10);
	JTextArea new_password_textArea = new JTextArea(1, 10);
	JTextArea new_firstname_textArea = new JTextArea(1, 10);
	JTextArea new_lastname_textArea = new JTextArea(1, 10);
	
	public GUI(IChatroomModel model, IChatroomController chatroomController){
		this.chatroomController = chatroomController;
		this.chatroomModel = model;
		this.chatroomModel.registerObserver(this);
		
		this.currentUser=null;
		this.contactUser=null;
		listConnectedUser=null;
		listMessage=null;
		
		ConnectionFrame();//Initialize Connection frames
	}
	
	public void updateUserReceiver(User UserReceiver, User userPopUp) {
		this.contactUser = UserReceiver;
		if(userPopUp !=null) {
			Notification(userPopUp);//Appear new window for notification	
		}
	}
	
	public void Notification(User userPopup) {
		//TODO:Display the new window to notify the fact that we receive a message from another User
	}
	
	public void InitializeListContacts() {//List of connected user
		String fullName = null;
		int index=0;
		
		//listUser = contacts;//get List of all the users
		listConnectedUser = chatroomController.controllerGetListConnectedUsers();
		DefaultListModel<String> model = new DefaultListModel<>();
		contactsList = new JList<>(model);
		
		for (int i=0; i<listConnectedUser.size(); i++) {//Add name and last name of each User in the contactsList
				fullName = listConnectedUser.get(i).getFirstname() +" "+ listConnectedUser.get(i).getLastname();
				System.out.println(fullName);
				model.addElement(fullName);
		}
		
		PrincipalFrame();//Initialize Chatroom frames
	}
	
	public void PrincipalFrame() {//Principal frame initialization
				principal_frame.setMinimumSize(new Dimension(1280, 800));
				principal_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				contactsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				contactsList.setLayoutOrientation(JList.VERTICAL);
				contactsList.setVisibleRowCount(-1);
				JScrollPane listScroller = new JScrollPane(contactsList);
				listScroller.setPreferredSize(new Dimension(260, 560));
				contactsPanel.add(listScroller);
				contactsList.addListSelectionListener(this);
				
				send.setActionCommand("send");
				send.addActionListener(this);
				

				contactsPanel.setPreferredSize(new Dimension(270, 600));
				textPanel.setPreferredSize(new Dimension(1270, 180));
				
				messagesPanel.setBorder(BorderFactory.createTitledBorder("Messages"));
				contactsPanel.setBorder(BorderFactory.createTitledBorder("Chats"));
				textPanel.setBorder(BorderFactory.createTitledBorder("Write here"));
				
				messagesPanel.setLayout(new BoxLayout(messagesPanel,BoxLayout.Y_AXIS));
				
				JScrollPane scrollPane = new JScrollPane(message_textArea);
				JScrollPane chat_scrollPane = new JScrollPane(messagesPanel);
				chat_scrollPane.setPreferredSize(new Dimension(1000, 600));
				scrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
				chat_scrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
				chat_scrollPane.setHorizontalScrollBarPolicy ( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
				textPanel.add(scrollPane);
				message_textArea.setEditable(true);
				
				textPanel.add(scrollPane, BorderLayout.WEST);
				textPanel.add(send, BorderLayout.EAST);
				principal_frame.add(chat_scrollPane, BorderLayout.WEST);
				principal_frame.add(contactsPanel, BorderLayout.EAST);
				principal_frame.add(textPanel, BorderLayout.SOUTH);
				
				connection_frame.setLocationRelativeTo(null);
				connection_frame.pack();
				connection_frame.setVisible(true);
		
	}
	
	public void ConnectionFrame() {//Connection frame initialization
		connection_frame.setMinimumSize(new Dimension(500, 100));
		connection_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		loginPanel.setPreferredSize(new Dimension(490, 90));
		loginPanel.setBorder(BorderFactory.createTitledBorder("Welcome to our chat! :)"));
		
		JLabel enter_username1 = new JLabel("Username:");
		JLabel enter_password1 = new JLabel("Password:");
		
		connection_frame.add(loginPanel);
		loginPanel.add(enter_username1);
		loginPanel.add(username_textArea);
		loginPanel.add(enter_password1);
		loginPanel.add(password_textArea);
		loginPanel.add(log_in);
		loginPanel.add(new_account);
		
		username_textArea.setEditable(true);
		password_textArea.setEditable(true);
		log_in.setActionCommand("login");
		log_in.addActionListener(this);
		new_account.setActionCommand("new_account");
		new_account.addActionListener(this);

		//New account frame initialization	
		new_account_frame.setMinimumSize(new Dimension(200, 150));
		new_account_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		accountPanel.setPreferredSize(new Dimension(190, 140));
		accountPanel.setBorder(BorderFactory.createTitledBorder("Enter your information"));
		//accountPanel.setLayout(new GridLayout(5,2, 1, 1));
		
		new_username_textArea.setEditable(true);
		new_password_textArea.setEditable(true);
		new_firstname_textArea.setEditable(true);
		new_lastname_textArea.setEditable(true);
		JLabel enter_username2 = new JLabel("Username:");
		JLabel enter_password2 = new JLabel("Password:");
		JLabel enter_firstname = new JLabel("Firstname:");
		JLabel enter_lastname = new JLabel("Lastname:");
		
		create_account.setActionCommand("create");
		create_account.addActionListener(this);
		

		new_account_frame.add(accountPanel);
		accountPanel.add(enter_username2, BorderLayout.NORTH);
		accountPanel.add(new_username_textArea, BorderLayout.NORTH);
		accountPanel.add(enter_firstname, BorderLayout.NORTH);
		accountPanel.add(new_firstname_textArea, BorderLayout.NORTH);
		accountPanel.add(enter_lastname, BorderLayout.NORTH);
		accountPanel.add(new_lastname_textArea, BorderLayout.NORTH);
		accountPanel.add(enter_password2, BorderLayout.NORTH);
		accountPanel.add(new_password_textArea, BorderLayout.NORTH);
		accountPanel.add(create_account, BorderLayout.SOUTH);
		
		connection_frame.setLocationRelativeTo(null);
		connection_frame.pack();
		connection_frame.setVisible(true);
		
	}

	public void actionPerformed(ActionEvent e) {
		String username = null, firstname = null, lastname = null, password = null;
		User newUser = null;//User to add in the database (create new account)
		boolean flag =false;
		
		switch(e.getActionCommand()) {
		case "send"://Send a message in the chat
			String message = message_textArea.getText();
			JLabel new_message = new JLabel(message);
			if(contactUser != null) {
				messagesPanel.add(new_message);
				messagesPanel.updateUI();
				message_textArea.setText(null);
			
				//Save message into the database	
				chatroomController.controllerSaveMessage(message);
			}
			
			break;
		case "login"://Log in the Chatroom
			//Get information from textArea
			username = username_textArea.getText();
			password = password_textArea.getText();
			
			//Check if the user exists in the database
			flag = chatroomController.controllerLogIn(username, password);
			System.out.println("LOGIN DONE");
			
			if(flag == false) {//Wrong password/username
				JOptionPane.showMessageDialog(error_frame, "Your password/username is incorrect. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
			}else {//Correct information about the user
				currentUser = chatroomController.controllerGetUser(username);//get information about the current user 
				chatroomController.controllerSetUserLogin(currentUser);//set currentUser in the Model
				System.out.println("BEFORE INITIALIZE LIST CONTACT");
				InitializeListContacts();//Initialize list of contacts
				System.out.println("END INITIALIZE LIST CONTACT");
				//Display the chatroom
				connection_frame.setVisible(false);
				principal_frame.setLocationRelativeTo(null);
				principal_frame.pack();
				principal_frame.setVisible(true);
			}
			//Clear the contents of the textArea
			username_textArea.setText(null);
			password_textArea.setText(null);
					
			break;
			
		case "new_account":
			
			new_account_frame.setLocationRelativeTo(null);
			new_account_frame.pack();
			new_account_frame.setVisible(true);
			
			break;
			
		case "create"://Add account to database
			//Get information from textArea
			username = new_username_textArea.getText();
			firstname = new_firstname_textArea.getText();
			lastname = new_lastname_textArea.getText();
			password = new_password_textArea.getText();
			
			//Save new user into the database
			newUser = new User(username,firstname,lastname,password);//create user
			System.out.println(newUser.toString());
			chatroomController.controllerNewAccount(newUser);//add the new user into the database
			
			//Clear the contents of the textArea
			new_username_textArea.setText(null);
			new_firstname_textArea.setText(null);
			new_lastname_textArea.setText(null);
			new_password_textArea.setText(null);
			
			//GUI
			new_account_frame.setVisible(false);
			connection_frame.setVisible(true);
			break;
		}
	}
	
	//If the user clicks on a contact in the list
		public void valueChanged(ListSelectionEvent e) {
		    if (e.getValueIsAdjusting() == false) { //if the user is not manipulating the selection anymore
		    	/*Get information about the user when click on a specific contact*/
		    	int index = (Integer) contactsList.getSelectedIndex();//Get the index where the user clicked
		    	contactUser = listConnectedUser.get(index);//Save current contact User
		    	chatroomController.controllerSetUserReceiver(contactUser);//SetCurrentcontactUser in the Model
		    	
		    	/*Display the records of the chat with the selected contact*/
		    	ArrayList<Message> messageConversation = chatroomController.controllerGetListMessageFromConversation();//Retrieve all the messages;
		    	
		    	JLabel displayMessage;
		    	Message m;
		    	
		    	if (messageConversation.size()!=0) {
		    		messagesPanel.removeAll();
		    		messagesPanel.updateUI();
		    		for (int i=0 ;i<messageConversation.size(); i++) {
			    		m = messageConversation.get(i);
			    		if(m.getIdUser1() == chatroomController.controllerGetIDFromUsername(contactUser.getUsername())) {//If the contact send the message
			    			displayMessage = new JLabel(contactUser.getFirstname()+" "+contactUser.getLastname());
			    			messagesPanel.add(displayMessage);
			    			displayMessage = new JLabel(m.getTimesent());
			    			messagesPanel.add(displayMessage);
			    			displayMessage = new JLabel(m.getText());
			    			messagesPanel.add(displayMessage);
			    			displayMessage = new JLabel(" ");
			    			messagesPanel.add(displayMessage);
			    			messagesPanel.add(displayMessage);

			    			
			    		}else {//if the current user send the message
			    			displayMessage = new JLabel(currentUser.getFirstname()+" "+currentUser.getLastname());
			    			messagesPanel.add(displayMessage);
			    			displayMessage = new JLabel(m.getTimesent());
			    			messagesPanel.add(displayMessage);
			    			displayMessage = new JLabel(m.getText());
			    			messagesPanel.add(displayMessage);
			    			displayMessage = new JLabel(" ");
			    			messagesPanel.add(displayMessage);
			    			messagesPanel.add(displayMessage);
			    		}
			    		messagesPanel.add(displayMessage);
			    		messagesPanel.updateUI();
			    	}
		    	}else {
		    		messagesPanel.removeAll();
		    		messagesPanel.updateUI();
		    	}
		    
		    	//to get the name of the selected contact just use contactsList.getSelectedValue()
		    	//the following code is just shown as an example of how this works
		    	/*JLabel coucou = new JLabel(contactsList.getSelectedValue());
		    	messagesPanel.add(coucou);
		    	messagesPanel.updateUI();*/
		    }
		}

	
	public static void main(String[] args) {
		
		IChatroomModel model = new ChatroomModel();
		new ChatroomController(model);
		
	}
}