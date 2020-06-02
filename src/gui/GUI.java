package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Items.*;
import database.DatabaseSQLite;


/**
 * @author Stella THAMMAVONG
 * with contribution of Nicolas JATOB
 * This class corresponds to the graphical user interface of the chat.
 *
 */
public class GUI implements ActionListener, ListSelectionListener {
	/**
	 * instance of the database
	 */
	DatabaseSQLite db = DatabaseSQLite.getInstance();
	/**
	 * boolean value to indicate if a message is available to be sent to the server
	 */
	public boolean gotInput = false;
	/**
	 * boolean value to check if the user entered the right values to login
	 */
	public boolean logged;
	
	/**
	 * ArrayList containing the message to load between to users from the database
	 */
	private ArrayList<Message> conversationlog;
	
	
	//Information about ContactDestination
	/**
	 * string that contains the message to be sent
	 */
	private String messageToClient;
	/**
	 * string that contains the receiver of this message
	 */
	private String ContactUsername;
	
	
	//Information about the LogInUser
	/**
	 * string value for the username of the connected user
	 */
	private String username;	
	/**
	 * string value for the lastname of the connected user
	 */
	private String lastname;
	/**
	 * string value for the firstname of the connected user
	 */
	private String firstname;
	/**
	 * string value for the password of the connected user
	 */
	private String password;
	
	/**
	 * user object corresponding to the connected user
	 */
	private User thisUser;
	
	/**
	 * string value to display who the user is trying to contact
	 */
	JLabel talkingTo = new JLabel("Talking to...");
	
	
	//Creation of frames
	/**
	 * frame for the principal functionalities of the chat, main window of the chat
	 */
	JFrame principal_frame = new JFrame("Chat GUI");
	/**
	 * frame to create a new account
	 */
	JFrame new_account_frame = new JFrame("New account");
	/**
	 * frame to login
	 */
	JFrame connection_frame = new JFrame("Log in");
	/**
	 * frame to display errors
	 */
	JFrame error_frame = new JFrame("Error");
	/**
	 * frame for information for the user
	 */
	JFrame rules_frame = new JFrame("Rules");
	
	
	//Creation of panels
	/**
	 * panel for the account frame
	 */
	JPanel accountPanel = new JPanel();
	/**
	 * only panel for the login frame
	 */
	JPanel loginPanel = new JPanel();
	/**
	 * panel for the principal frame, area where the messages are shown
	 */
	JPanel messagesPanel = new JPanel();
	/**
	 * panel for the principal frame, area where the contacts are shown
	 */
	JPanel contactsPanel = new JPanel();
	/**
	 * panel for the principal frame, area where user can type text and send it
	 */
	JPanel textPanel = new JPanel();
	/**
	 * only panel for the rules frame
	 */
	JPanel rulesPanel = new JPanel();

	
	//Creation of buttons
	/**
	 * button to send message written by the user
	 */
	JButton send_button = new JButton("SEND");
	/**
	 * button to login
	 */
	JButton log_in_button = new JButton("Log in");
	/**
	 * button to display the new account frame
	 */
	JButton new_account_button = new JButton("Create account");
	/**
	 * button to create a new account
	 */
	JButton create_account_button = new JButton("OK");
	/**
	 * button to refresh the GUI (messages not displayed and list of contacts if new users just logged in)
	 */
	JButton refresh_button = new JButton("<REFRESH>");
	/**
	 * button to make the user confirm that he read the information/rules
	 */
	JButton rules_understood_button = new JButton("Understood!");
		
	
	//Creation of text areas
	/**
	 * textarea for messages
	 */
	JTextArea message_textArea = new JTextArea(6, 105);
	/**
	 * textarea for the username used to login
	 */
	JTextArea username_textArea = new JTextArea(1, 10);
	/**
	 * textarea for the password used to login
	 */
	JTextArea password_textArea = new JTextArea(1, 10);
	/**
	 * textarea for the username used to create a new account
	 */
	JTextArea new_username_textArea = new JTextArea(1, 10);
	/**
	 * textarea for the password used to create a new account
	 */
	JTextArea new_password_textArea = new JTextArea(1, 10);
	/**
	 * textarea for the firstname used to create a new account
	 */
	JTextArea new_firstname_textArea = new JTextArea(1, 10);
	/**
	 * textarea for the lastname used to create a new account
	 */
	JTextArea new_lastname_textArea = new JTextArea(1, 10);
	
	/**
	 * model for the contacts list
	 */
	private DefaultListModel<String> contactsListModel = new DefaultListModel<String>();
	/**
	 * list that contains the indices of items that needs to change color (green)
	 */
	static List<Integer> colorToChangeList = new ArrayList<Integer>();
	/**
	 * list displayed in the GUI
	 */
	JList<String> contactsList = new JList<String>(contactsListModel);
	
	/**
	 * constructor of GUI, will initialize all frames
	 */
	public GUI(){
		
		//Initialization of variables
		this.ContactUsername=null;
		this.username = null;
		this.lastname = null;
		this.firstname = null;
		this.password = null;
		this.messageToClient=null;
		logged = false;

		//Connection frame initialization
		connection_frame.setMinimumSize(new Dimension(500, 100));
		connection_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		connection_frame.setResizable(false);

		//login panel initialization
		loginPanel.setPreferredSize(new Dimension(490, 90));
		loginPanel.setBorder(BorderFactory.createTitledBorder("Welcome to our chat! :)"));
		
		//Creation of labels for the login panel
		JLabel enter_username1 = new JLabel("Username:");
		JLabel enter_password1 = new JLabel("Password:");
		
		//Adding elements to the connection frame
		connection_frame.add(loginPanel);
		loginPanel.add(enter_username1);
		loginPanel.add(username_textArea);
		loginPanel.add(enter_password1);
		loginPanel.add(password_textArea);
		loginPanel.add(log_in_button);
		loginPanel.add(new_account_button);
		
		//Initialization of textareas and buttons of the connection frame
		username_textArea.setEditable(true);
		password_textArea.setEditable(true);
		log_in_button.setActionCommand("login");
		log_in_button.addActionListener(this);
		new_account_button.setActionCommand("new_account");
		new_account_button.addActionListener(this);

		//New account frame initialization	
		new_account_frame.setMinimumSize(new Dimension(225, 175));
		new_account_frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		new_account_frame.setResizable(false);
		
		//New account panel initialization
		accountPanel.setPreferredSize(new Dimension(215, 165));
		accountPanel.setBorder(BorderFactory.createTitledBorder("Enter your information"));
		
		//Initialization of textareas and buttons of the new account frame
		new_username_textArea.setEditable(true);
		new_password_textArea.setEditable(true);
		new_firstname_textArea.setEditable(true);
		new_lastname_textArea.setEditable(true);
		create_account_button.setActionCommand("create");
		create_account_button.addActionListener(this);
		
		//Creation of labels for the new account panel
		JLabel enter_username2 = new JLabel("Username:");
		JLabel enter_password2 = new JLabel("Password:");
		JLabel enter_firstname = new JLabel("Firstname:");
		JLabel enter_lastname = new JLabel("Lastname:");
	
		//Adding elements to the new account frame
		new_account_frame.add(accountPanel);
		accountPanel.add(enter_username2);
		accountPanel.add(new_username_textArea);
		accountPanel.add(enter_firstname);
		accountPanel.add(new_firstname_textArea);
		accountPanel.add(enter_lastname);
		accountPanel.add(new_lastname_textArea);
		accountPanel.add(enter_password2);
		accountPanel.add(new_password_textArea);
		accountPanel.add(create_account_button);
		
		
		//Rules frame initialization
		rulesPanel.setPreferredSize(new Dimension(890, 170));
		rulesPanel.setBorder(BorderFactory.createTitledBorder("Rules and Info"));
		rules_frame.setMinimumSize(new Dimension(900, 180));
		rules_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rules_frame.setResizable(false);
		
		//label that contains the rules and information to know
		JLabel rules = new JLabel("<html>Some things to know before starting using the chat:<br/>- Write your message only with characters existing in English! "
                + "And never use / .<br/>- Sorry there are sometimes errors that we couldn't resolve.<br/>Example: it can happen that your list of contacts"
                + " becomes empty without any reason. But there is a few chance that it happens.<br/>If this error occurs please reboot.<br/>" 
+ "After login please ALWAYS click 2 times on the refresh button to initialize the server. (for each account connected)</html>");
		
		//Initialization of the button
		rules_understood_button.setActionCommand("understood");
		rules_understood_button.addActionListener(this);
		
		//Adding elements to the rules frame
		rulesPanel.setLayout(new BorderLayout());
		rulesPanel.add(rules, BorderLayout.NORTH);
		rulesPanel.add(rules_understood_button, BorderLayout.SOUTH);
		rules_frame.add(rulesPanel);
	
		//Principal frame initialization
		principal_frame.setMinimumSize(new Dimension(1280, 800));
		principal_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		principal_frame.setResizable(false);
		
		//Intialization of the contacts list
		contactsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		contactsList.setLayoutOrientation(JList.VERTICAL);
		contactsList.setVisibleRowCount(-1);
		contactsList.setCellRenderer(new CellColorRenderer());
		
		//Creation and initialization of scrollpane for the contacts list
		JScrollPane listScroller = new JScrollPane(contactsList);
		listScroller.setPreferredSize(new Dimension(260, 500));
		

		
		
		//Adding elements to the contact panel
		contactsPanel.add(refresh_button);
		contactsPanel.add(listScroller);
		contactsList.addListSelectionListener(this);
	
		
		//Initialization of the buttons of the principal frame
		refresh_button.setActionCommand("refresh");
		refresh_button.addActionListener(this);
		send_button.setActionCommand("send");
		send_button.addActionListener(this);
		
		//Initialization of panels
		contactsPanel.setPreferredSize(new Dimension(270, 600));
		textPanel.setPreferredSize(new Dimension(1270, 180));
		messagesPanel.setBorder(BorderFactory.createTitledBorder("Messages"));
		contactsPanel.setBorder(BorderFactory.createTitledBorder("Chats"));
		textPanel.setBorder(BorderFactory.createTitledBorder("Write here"));
		messagesPanel.setLayout(new BoxLayout(messagesPanel,BoxLayout.Y_AXIS));
	
		//Creation and initialization of scrollpane for the message textarea
		JScrollPane scrollPane = new JScrollPane(message_textArea);
		JScrollPane chat_scrollPane = new JScrollPane(messagesPanel);
		chat_scrollPane.setPreferredSize(new Dimension(1000, 600));
		scrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		chat_scrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		chat_scrollPane.setHorizontalScrollBarPolicy ( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		
		//Adding elements to the principal frame
		textPanel.add(scrollPane);
		message_textArea.setEditable(true);
		textPanel.add(talkingTo,BorderLayout.NORTH);
		textPanel.add(scrollPane, BorderLayout.WEST);
		textPanel.add(send_button, BorderLayout.EAST);
		principal_frame.add(chat_scrollPane, BorderLayout.WEST);
		principal_frame.add(contactsPanel, BorderLayout.EAST);
		principal_frame.add(textPanel, BorderLayout.SOUTH);
		
		//We display first the login frame
		connection_frame.setLocationRelativeTo(null);
		connection_frame.pack();
		connection_frame.setVisible(true);
	}


	/**
	 * Action listener for the buttons
	 */
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		
			case "understood":
				//It hides the rules fram and display the principal frame
				rules_frame.setVisible(false);
				principal_frame.setLocationRelativeTo(null);
				principal_frame.pack();
				principal_frame.setVisible(true);
				break;

			case "send":
				if(ContactUsername != null){//If a contact is selected
					String message = message_textArea.getText();
				
					if(message.contentEquals("")==false) {//If the message is not empty
						//It "sends" the message written in the textarea to the client
						messageToClient = "/" + ContactUsername + "/" + message;
						messagesPanel.updateUI();
						message_textArea.setText(null);
						this.gotInput = true;
						contactsPanel.updateUI();
					}
				}else{//Otherwise it shows an error
					JOptionPane.showMessageDialog(error_frame, "Select first a contact in the list before sending a message!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				break;
			
			case "login":
				//Hide the connection frame
				connection_frame.setVisible(false);
				
				//Verify the data entered by the user
				username = username_textArea.getText();
				password = password_textArea.getText();
				if(db.logIn(username, password)){//If input are correct
					//login and displays the rule frame
					logged = true;
					thisUser = db.getUser(username);
					principal_frame.setTitle(username);
					rules_frame.setLocationRelativeTo(null);
					rules_frame.pack();
					rules_frame.setVisible(true);
				}else{//Otherwise it shows an error
					JOptionPane.showMessageDialog(error_frame, "Your password/username is incorrect. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
					connection_frame.setVisible(true);
				}
			
				//Clear the contents of the textArea
				username_textArea.setText(null);
				password_textArea.setText(null);
				break;
			
			case "new_account":
				//It displays the new account frame
				new_account_frame.setLocationRelativeTo(null);
				new_account_frame.pack();
				new_account_frame.setVisible(true);
				break;
			
			case "create":
				//It gets the input of the user
				username = new_username_textArea.getText();
				password = new_password_textArea.getText();
				lastname = new_lastname_textArea.getText();
				firstname = new_firstname_textArea.getText();
			
				if(db.getUser(username) == null){//If the user doesn't exist in the database
					thisUser = new User(username,firstname,lastname,password);
					db.CreateAccount(thisUser);
					new_account_frame.setVisible(false);
					connection_frame.setVisible(true);
				}else{//Otherwise it shows an error
					JOptionPane.showMessageDialog(error_frame, "username already in used. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				//Clears the text areas
				username_textArea.setText(null);
				password_textArea.setText(null);
				break;
			case "refresh":
				//It refreshes the GUI
				if(contactsList.getSelectedValue() != null){
					loadMessages(contactsList.getSelectedValue());
				}
				messageToClient = "\r";
				this.gotInput = true;
				break;
		}
	}
	
	/**
	 * Action listener when the user click on an item in the list
	 */
	public void valueChanged(ListSelectionEvent e) {
	    if (e.getValueIsAdjusting() == false){ //if the user is not manipulating the selection anymore
	    	
	    	ContactUsername = contactsList.getSelectedValue();//Save the person with who the client to communicate
	    	
	    	if(ContactUsername != null){
	    		//We retrieve the previous messages exchanged bewteen the user and the selected contact
	    		conversationlog = db.retrieveListOfMessageFromDiscussion(username, ContactUsername);
		    	loadMessages();
		    	talkingTo.setText("Talking to " + ContactUsername);
		        textPanel.updateUI();
		        
		        //Prepares the message to be sent to the selected contact
		        String sendMessage = "/"+contactsList.getSelectedValue()+"/";
		        messageToClient = sendMessage;
		        
		    	if(colorToChangeList.contains(contactsList.getSelectedIndex())) {//If the selected item is in the list of items to change the color i.e. if it is green
		    		if(contactsList.getSelectedIndex()==0) {
		    			colorToChangeList.remove(getIndexColorFromIndexContact(getIndexFromName(contactsList.getSelectedValue())));
		    		}else {
		    			colorToChangeList.remove(contactsList.getSelectedIndex()-1);
		    		}//removes it from the list to change it back to white
		    		contactsPanel.updateUI();
		    	}    	
		    	messagesPanel.updateUI();
	    	}
	    }
	}
	
	/**
	 * This method puts the given username in green in the contacts list
	 * @param name is the username as it appears in contactsListModel
	 */
	public void greenNewMessage(String name) {
        if(!(colorToChangeList.contains(getIndexFromName(name)))) {//If the item in the list is not already green
            colorToChangeList.add(getIndexFromName(name));//Add it to the list of items that must be green
            displayMessage("entered greenNewMessage if ");
        }
        contactsPanel.updateUI();
    }
	
	/**
	 * This method finds the index in the colorToChangeList when you give the index of the contact in the contactsListModel (because they're two different things apart)
	 * @param indexContact is the index in the contactsListModel
	 * @return the index in colorToChangeList
	 */
	public int getIndexColorFromIndexContact(int indexContact) {
		for(int i = 0; i<colorToChangeList.size(); i++) {
			if(colorToChangeList.get(i)==indexContact) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * This method finds the index of the given username in contactsListModel
	 * @param name is the username
	 * @return the index in contactsListModel
	 */
	public int getIndexFromName(String name) {
		for(int i = 0; i<contactsListModel.getSize(); i++) {
			if(contactsListModel.get(i)==name) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Method to retrieve the message received by the client from the server.
	 * According to the format of the message we decide either to save the message and notify the user,
	 * or save and immediately reload the conversation
	 * @param message
	 */
	public void retrieveMessage(String message){
		String sender = null;
		
		//Checking if we received a private message
		//We retrieve the sender to save the message and notify the current user
		if(message.contains("From")){
			for(int i =0;i<contactsListModel.getSize();i++){
				if(message.substring(0, message.indexOf(">")).contains(contactsListModel.get(i)) && !contactsListModel.get(i).contains(username)){
					sender = contactsListModel.get(i);
					greenNewMessage(sender);
					db.SaveMessage(message, sender, username);
					break;
				}
			}
		}
		
		//If The message is not private
		if(!message.contains("From")){
			
			//If the user is alone we do nothing (Handling the refresh button)
			if(contactsListModel.getSize() > 1){
				return;
			}
			
			//If there is 2 users connected, We load their conversation logs
			if(contactsListModel.getSize() == 1){
				String userInContact = contactsListModel.get(0);
				loadMessages(userInContact);
			}
		}
		else
		{
			//If we have currently selected a user in the connected users list
			//We retrieve his username and load his conversation with the current user
			if(contactsList.getSelectedValue() != null){
				String selecteduser = contactsList.getSelectedValue();
				if(selecteduser.equals(sender)){
					loadMessages(selecteduser);
				}
			}
		}
			
	}
	
	/**
	 * This method displays messages in the messages panel
	 * @param message is what is to be displayed
	 */
	private void displayMessage(String message) {
        JLabel new_message = new JLabel(message);
        messagesPanel.add(new_message);
        messagesPanel.updateUI();
    }
	
	/**
	 * This method returns the message to send
	 * @return the message to the client
	 */
	public String getMessageFromGUI(){
		return messageToClient;
	}
	
	/**
	 * This method loads messages in the database between the selected contact
	 */
	private void loadMessages(){
		JLabel displayMessage;
    	Message m;
    	
    	if (conversationlog.size()!=0) {//If there is messages
    		//Refresh of display
    		messagesPanel.removeAll();
    		messagesPanel.updateUI();
    		
    		for (int i=0 ;i<conversationlog.size(); i++) {//For every message
	    		m = conversationlog.get(i);
	    		if(m.getIdUser1() == db.getIDFromUsername(ContactUsername)) {//If the contact sent the message
	    			displayMessage = new JLabel(db.getUser(ContactUsername).getFirstname()+" "+db.getUser(ContactUsername).getLastname());
	    			messagesPanel.add(displayMessage);
	    			
	    		}else {//if the current user sent the message
	    			displayMessage = new JLabel(db.getUser(username).getFirstname()+" "+db.getUser(username).getLastname());
	    			messagesPanel.add(displayMessage);
	    		}
	    		
	    		//Display message
	    		displayMessage = new JLabel(m.getTimeSent());
    			messagesPanel.add(displayMessage);
    			displayMessage = new JLabel(m.getText());
    			messagesPanel.add(displayMessage);
    			displayMessage = new JLabel(" ");
    			messagesPanel.add(displayMessage);
    			messagesPanel.add(displayMessage);
	    		messagesPanel.add(displayMessage);
	    		messagesPanel.updateUI();
	    	}
    		
    	}else {//there is no message to display
    		messagesPanel.removeAll();
    		messagesPanel.updateUI();
    	}
	}
	
	/**
	 * Overloaded version of loadMessages to display the messages of a chosen user
	 * @param userloadfrom is the name of the user from whom we want to load messages
	 */
	private void loadMessages(String userloadfrom){
		
		conversationlog = db.retrieveListOfMessageFromDiscussion(username, userloadfrom);
		
		JLabel displayMessage;
    	Message m;
    	
    	if (conversationlog.size()!=0) {//If there is messages
    		/*Refresh display*/
    		messagesPanel.removeAll();
    		messagesPanel.updateUI();
    		
    		for (int i=0 ;i<conversationlog.size(); i++) {//For every message
	    		m = conversationlog.get(i);
	    		if(m.getIdUser1() == db.getIDFromUsername(userloadfrom)) {//If the contact sent the message
	    			displayMessage = new JLabel(db.getUser(userloadfrom).getFirstname()+" "+db.getUser(userloadfrom).getLastname());
	    			messagesPanel.add(displayMessage);
	    			
	    		}else {//if the current user sent the message
	    			displayMessage = new JLabel(db.getUser(username).getFirstname()+" "+db.getUser(username).getLastname());
	    			messagesPanel.add(displayMessage);
	    		}
	    		
	    		//Display message
	    		displayMessage = new JLabel(m.getTimeSent());
    			messagesPanel.add(displayMessage);
    			displayMessage = new JLabel(m.getText());
    			messagesPanel.add(displayMessage);
    			displayMessage = new JLabel(" ");
    			messagesPanel.add(displayMessage);
    			messagesPanel.add(displayMessage);
	    		messagesPanel.add(displayMessage);
	    		messagesPanel.updateUI();
	    	}
    		
    	}else {//there is no message
    		/*Nothing to show*/
    		messagesPanel.removeAll();
    		messagesPanel.updateUI();
    	}
	}
	
	/**
	 * This method sets the "normal" contactsList
	 * @param newconnected is the list of connected users on the server
	 */
	public void setConnectedUserNormalMode(String[] newconnected){
		contactsListModel.clear();
		for(String name : newconnected){
			if(!name.equals(username)){//We add all users except the one connected on this session
				contactsListModel.addElement(name);
			}
		}
		contactsList.setModel(contactsListModel);

	}

	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

