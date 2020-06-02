package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * Date: 29-05-2020
 * This is our application server that accepts and responds to the request made by other program, known as client. It is used to manage the network resources and for running the program or software that provides services.
 * 
 * With the contribution of TABOU Metagang
 * @author JATOB Nicolas 
 */
public class ChatServer
{

	/**
	 * Static final integer to hold the communication port number.
	 */
	private static final int portNumber = 4444;

	/**
     * Integer to hold the server's communication port's number.
     */
    private int serverPort;
    
    /**
     * Set of ClientThraed objects to hold each client's thread.
     */
    private Set<ClientThread> clients;
    
    /**
     * set of String to hold users' names.
     */
    private Set<String> userNames = new LinkedHashSet<String>();
    
    /**
     * The main method of this application.
     * It is used to start the server.
     * @param args array of String arguments
     */
    public static void main(String[] args){
        ChatServer server = new ChatServer(portNumber);
        server.startServer();
    }

    /**
     * Constructor of the class.
     * It initialize the port of the server.
     * @param portNumber integer port number value
     */
    public ChatServer(int portNumber)
    {
        this.serverPort = portNumber;
    }

    /**
     * This method returns the threads of all the clients to our server
     * @return set of clients' thread
     */
    public Set<ClientThread> getClients(){
        return clients;
    }

    /**
     * Method to start the server using a socket and then constantly look for new connections
     * 
     * @see #acceptClients(ServerSocket)
     */
    private void startServer(){
        clients = new LinkedHashSet<ClientThread>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        } catch (IOException e){
            System.err.println("Could not listen on port: "+serverPort);
            System.exit(1);
        }
    }

    /** 
     * This method is used to constantly search for a new connection from a client.
     * We create a new ClientThread upon a new connection.
     * 
     * @param serverSocket The instance of the socket on which the server is started on.
     * @see ClientThread
     */
    private void acceptClients(ServerSocket serverSocket){
        System.out.println("server starts port = " + serverSocket.getLocalSocketAddress());
        while(true){
            try{
            	//We accept a connection
                Socket socket = serverSocket.accept();
                System.out.println("accepts : " + socket.getRemoteSocketAddress());
                
                //We create a client thread from the connection
                ClientThread client = new ClientThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);	//adding the new client to the list
                
                System.out.println("Connected Users" + userNames.toString());
            } catch (IOException ex){
                System.out.println("Accept failed on : "+serverPort);
            }
        }
    }
    
    /**
     * This method adds a user's name to the list of users' names
     * @param username String to hold the user's name
     */
    public void addUserName(String username)
    {
    	userNames.add(username);
    }
    
    /**
     * This method returns the list of all the connected users
     * @return String which is the name of all users
     */
    public String getUsersNamesList()
    {
    	return userNames.toString();
    }
    
    /**
     * This method searches through the list of all user names and check if the one provided is present in the list
     * @param username string holding the name to be searched for
     * @return boolean value depending on whether or not the user exists.
     */
    public boolean userExist(String username)
    {
    	if(userNames.contains(username))
    	{
    		return true;
    	}
    	else 
    	{
    		return false;
    	}
    }
    
    /**
     * This method returns the name of a user
     * @return the set of strings holding the users' names
     */
    public Set<String> getUsersNames()
    {
    	return userNames;
    }
    
    /**
     * This method removes the client's name from the list of connected users.
     * @param username string holding the username
     * @param user ClientThread object which is the thread of the user we want to remove
     */
    public void removeUser(String username,ClientThread user)
    {
    	boolean removed = userNames.remove(username);
    	if(removed)
    	{
    		clients.remove(user);
    		System.out.println(username + "has logged out");
    	}
    }
	
    /**
     * This method is used to get from an given user name the thread associated to this name
     * @param _username String holding the user's name
     * @return ClientThread object of the user whose name is the fist parameter
     */ 
    public ClientThread getClientThreadfromUsername(String _username)
    {
    	int i = 0;
    	//We create a dummy List instantiated as an array list to store all the clients
    	//from the client thread list. We do this to have access to all ArrayList's method
    	List<ClientThread> localClientList = new ArrayList<ClientThread>();
    	localClientList.addAll(clients);
    	for(String username : userNames)
    	{
    		if(username.matches(_username))
    		{
    			return localClientList.get(i);
    		}
    		i++;
    	}
    	
    	//make a proper error for this method when client not found
		return null;
    }
    
}
