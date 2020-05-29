package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ChatServer
{

	private static final int portNumber = 4444;

    private int serverPort;
    private Set<ClientThread> clients;
    private Set<String> userNames = new LinkedHashSet<String>();
    
    public static void main(String[] args){
        ChatServer server = new ChatServer(portNumber);
        server.startServer();
    }

    public ChatServer(int portNumber)
    {
        this.serverPort = portNumber;
    }

    public Set<ClientThread> getClients(){
        return clients;
    }

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

    //method to constantly search for for a new connection from a client 
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
    
    public void addUserName(String username)
    {
    	userNames.add(username);
    }
    
    public String getUsersNamesList()
    {
    	return userNames.toString();
    }
    
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
    
    public Set<String> getUsersNames()
    {
    	return userNames;
    }
    
    public void removeUser(String username,ClientThread user)
    {
    	boolean removed = userNames.remove(username);
    	if(removed)
    	{
    		clients.remove(user);
    		System.out.println(username + "has logged out");
    	}
    }
	
    //method to get from an given user name the thread associated to this name
    public ClientThread getClientThreadfromUsername(String _username)
    {
    	int i = 0;
    	//We create a dummy List instantiated as an arraylist to store all the clients
    	//from the client thread list. We do this to have acces from all ArrayList's method
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
