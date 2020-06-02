package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

import gui.GUI;

/**
 * Date: 30-05-2020
 * This class implements the thread through which the client will communicate with the server
 * 
 * With the contribution of TABOU Metagang
 * @author JATOB Nicolas
 */
public class ServerThread implements Runnable
{
	/**
	 * Socket object through which the communication link will be establisshed
	 */
	private Socket socket;
	
	/**
     * String holding the user's name
     */
    private String userName;
    
    /**
     * List containing all the message to send to the server
     */
    private final LinkedList<String> messagesToSend;
    
    
    /**
     * boolean to verify if there is a message in the list
     */
    private boolean hasMessages = false;
    
    /**
     * AES object to encrypt the actual client's message
     */
    private AES clientEncryptor;
    
    /**
     * ObjectOutputStream object which writes primitive data types and graphs of Java objects to an OutputStream
     */
    private ObjectOutputStream objectOStream;
    
    /**
     * MessagePacket object to pack all the information about the sender and receiver so the packet properly reaches the destination
     */
    private MessagePacket clientPacket;
    
    /**
     * GUI object being instantiated with the GUI of the client starting this thread.
     */
    private GUI thisclientGUI;
    
    /**
     * This method instantiates objects of this class
     * @param socket Socket object to establish a connection
     * @param userName String value to hold the name of the user
     * @param encryptor AES object to encrypt the user's message unique to each user
     * @param clientGUI GUI object received from the client starting this thread
     * @throws IOException Error sent by the AES object cipher and decipher method
     */
    public ServerThread(Socket socket, String userName, AES encryptor, GUI clientGUI) throws IOException{
        this.socket = socket;
        this.userName = userName;
        thisclientGUI = clientGUI;
        messagesToSend = new LinkedList<String>();
        clientEncryptor = encryptor;
    }

    /**
     * This method adds a message to the list of messages to be sent
     * @param message string value which holds the message to be sent
     */
    public void addNextMessage(String message){
        synchronized (messagesToSend){
            hasMessages = true;
            messagesToSend.push(message);
        }
    }

    /**
     * This method is the actual runnable method of the class, it uses the connection established in the client class.
     * It will set up the different I/O streams used to send data to the server
     * Then it runs indefinitely to send any input of the user to the server
     */
    @Override
    public void run(){
        System.out.println("Welcome :" + userName);
        System.out.println("Local Port :" + socket.getLocalPort());
        System.out.println("Server = " + socket.getRemoteSocketAddress() + ":" + socket.getPort());
        try{
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverInStream = socket.getInputStream();
            serverOut.write(userName + "\n");
            serverOut.flush();
            serverOut = null;
            
            Scanner serverIn = new Scanner(serverInStream);
            
            //Instantiating the stream to send our packets
            objectOStream = new ObjectOutputStream(socket.getOutputStream());
            
            while(!socket.isClosed()){
                if(serverInStream.available() > 0){
                	//We retrieve an input from the server, precisely the list of usernames
                	synchronized(serverIn)
                	{
                		if(serverIn.hasNextLine()){
                        	System.out.println("Heard server");
                        	String toGUI = serverIn.nextLine();
                        	System.out.println(toGUI+"\n");
                        	if(toGUI.contains("["))
                        	{
                        		if(toGUI.contains(","))
                        		{
                        			//For 2nd to any number of connection
                        			System.out.println("Retrieved connected user list");
                        			String result = toGUI.substring(1, toGUI.indexOf("]"));
                        			result = result.replaceAll("\\s+","");
                        			System.out.println(result);
                        			String[] listname = result.split(",");
                        			thisclientGUI.setConnectedUserNormalMode(listname);
                        		}
                        	}
                        	else
                        	{
                        		thisclientGUI.retrieveMessage(toGUI);
                                System.out.println(toGUI);
                        	}
                        }
                	}
                }
                
                //If we have a message available we get it prepared to be sent
                if(hasMessages){
                	System.out.println("message received to server thread");
                    String nextSend = "";
                    synchronized(messagesToSend){
                        nextSend = messagesToSend.pop();
                        System.out.println("nextsend ="+nextSend);
                        hasMessages = !messagesToSend.isEmpty();
                        System.out.println(messagesToSend.toString());
                    }

                    //For private message, we recognize the format format /username/messagetosend
                    if(nextSend.contains("/"))
                    {
                    	//We split the string corresponding to the command to extract the username
                    	//of the receiver and the message to send
                    	String[] result = nextSend.split("/");
                    	
                    	clientPacket = new MessagePacket(result[1],userName,result[2],clientEncryptor);
                    	clientPacket.encryptMessage();
                    	
                    	objectOStream.reset();
                    	objectOStream.writeObject(clientPacket);
                    	objectOStream.flush();
                    }
                    //We print a public message 
                    else if(!(nextSend.isEmpty()))
                    {
                    	System.out.println("public msg sent");
                    	clientPacket = new MessagePacket(userName,nextSend);
                    	
                    	objectOStream.reset();
                    	objectOStream.writeObject(clientPacket);
                    	objectOStream.flush();
                    }
                }
            }
            if(socket.isClosed())
            {
	        	 System.out.println("Lost connection to host: Server closed");
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
	
}
