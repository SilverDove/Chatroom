package server;

import client.MessagePacket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Date: 29-05-2020
 * This class manages all the different client's connections and relay messages from one to the other
 * 
 * With the contribution of TABOU Metagang
 * @author JATOB Nicolas
 */
public class ClientThread implements Runnable{

	/**
	 * socket to wait for connections from remote clients
	 */
	private Socket socket;
	
	/**
     * PrintWriter value creates the necessary intermediate OutputStreamWriter which is connected to the client's output stream
     */
    private PrintWriter clientOut;
    
    /**
     * ChatServer object to get a server
     */
    private ChatServer server;
    
    /**
     * ObjectInputStream object to de-serializes primitive data and objects previously written using an ObjectOutputStream
     */
    private ObjectInputStream objectIStream;
    
    /**
     * MessagePacket object to encrypt or decrypt the message when needed 
     */
    private MessagePacket receivedPacket;
    
    /**
     * Constructor of the Class.
     * We initialize the server and socket field of the class to then initiate the connection
     * @param ChatsServer object 
     * @param Socket object 
     */
    public ClientThread(ChatServer server, Socket socket){
        this.server = server;
        this.socket = socket;
    }

    /**
     * This method returns the client's outputstream
     * @return returning PrintWriter object
     */
    public PrintWriter getWriter(){
        return clientOut;
    }

    /** 
     * Runnable method of the class.
     * This thread will receive the user's informations and will indefinitely wait for the next user input until either the user is disconnected or the server shuts down
     */
    @Override
    public void run() {
        try{
            // setting up
        	InputStream inputtest = socket.getInputStream();
        	BufferedReader reader = new BufferedReader(new InputStreamReader(inputtest));
            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            
            //getting username
            String username = reader.readLine();
            server.addUserName(username);
            reader =null;
    		
            if(!server.getClients().isEmpty())
        	{
        		System.out.println("in user list send");
        		for(ClientThread thatClient : server.getClients()){
        			System.out.println("sending to client");
                    PrintWriter thatClientOut = thatClient.getWriter();
                    if(thatClientOut != null){
                    	System.out.println("client found");
                        thatClientOut.println(server.getUsersNamesList());
                    }
        		}
        	}
            
    		objectIStream = new ObjectInputStream(socket.getInputStream());
    		
            // start communicating
            while(!socket.isClosed()){
            	
            	System.out.println("waiting for packet");
            	receivedPacket = (MessagePacket)objectIStream.readObject();
            	System.out.println("received packet");
                
                //We verify if the string in the server input stream is a username
                //If its the case we will print a private message
                if(receivedPacket.getPacketState())
                {
                	if(server.userExist(receivedPacket.getReceiver()))
                	{
                		receivedPacket.decryptMessage();
                		sendPrivateMessage(receivedPacket.getReceiver(),"From " + receivedPacket.getSender() + " > " + receivedPacket.getMessage());
                	}
                }
                
                //If the message to send is not a private message we send the message
                //to every clients
                else
                {
                	System.out.println("public msg processing");
                	for(ClientThread thatClient : server.getClients()){
	                    PrintWriter thatClientOut = thatClient.getWriter();
	                    if(thatClientOut != null){
	                        thatClientOut.write(receivedPacket.getSender() + " > " +receivedPacket.getMessage() + " ethvrthvrthvr\r\n");
	                        thatClientOut.flush();
	                    }
                    }
                }
                System.out.println("trigger end");
            }
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            e.printStackTrace();
        }
    }
    
    /**
     * This method is used to send private messages
     * @param username String to store the user's name
     * @param message String to Store the communicated message
     */
    public void sendPrivateMessage(String username,String message)
    {
    	PrintWriter clientWriter;
    	
    	//We go through every client and see if the name of the receiver is found before
    	//sending it to this client
    	for(ClientThread thatClient : server.getClients())
    	{
    		if(thatClient.equals(server.getClientThreadfromUsername(username)))
    		{
    			clientWriter = thatClient.getWriter();
    			if(clientWriter != null){
    				clientWriter.write(message + "\n");
    				clientWriter.flush();
                }
    		}
    	}
    }
	
    
    
}
