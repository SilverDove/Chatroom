package client;

import java.io.IOException;
import java.net.Socket;
import gui.GUI;


/**
 * Date: 30-05-2020
 * This class represents a client defined by its username, password and other informations.
 * After execution it establishes a connection to a local server and launch a connection thread with the said server.
 * All input of the user will be relayed to the thread for analysis and then sent to the server.
 * 
 * With the contribution of TABOU Metagang
 * @author JATOB Nicolas
 */
public class Client 
{
	/**
	 * String holding the name of the host server
	 */
	private static final String host = "localhost";
	
	/**
     * int value holding the port number through which the host or client will communicate
     */
    private static final int portNumber = 4444;

    /**
     * String holding the user's name
     */
    private String userName;
    
    /**
     * String holding the name of the server
     */
    private String serverHost;
    
    /**
     * integer value holding the server's chosen communication port number
     */
    private int serverPort;
    
    /**
     *AES object which will be used to encrypt the host's messages (unique to every user)
     */
    private AES clientEncriptor;
    
    /**
     *GUI object to instantiate the GUI of the current client
     */
    private GUI clientGUI;
    
    /**
     * The main method of this application. It let us launch a client and its GUI.
     * From the GUI onward we will receive his username which is an important information for this class.
     * It also initiate the connection with the server.
     * 
     * @param args array of String arguments
     */
    public static void main(String[] args){
        //Creating a client from the inputed user name
        Client client = new Client(host, portNumber);
        client.startClient();
    }

    /**
     *Constructor of the class.
     *This method receive from the GUI the username inputed by the user.
     *It then initialize every other fields.
     *
     * @param host
     * @param portNumber
     */
    public Client(String host, int portNumber){
    	clientGUI = new GUI();
    	while(!clientGUI.logged)
    	{
    		System.out.println("waiting");
    	}
        userName = clientGUI.getUsername();
        this.serverHost = host;
        this.serverPort = portNumber;
        clientEncriptor = new AES();
    }
    
    /**
     * Method to initiate the connection with the server.
     * We start a new thread by instantiating a new serverThread.
     * We have an infinite loop so that while we are still connected to the server we will take care of
     * new messages and send it to the server.
     * 
     * @see ServerThread
     */
    private void startClient(){
        try{
            Socket socket = new Socket(serverHost, serverPort);
            Thread.sleep(1000); // waiting for network communicating.
            
            //initiating a server thread
            ServerThread serverThread = new ServerThread(socket, userName, clientEncriptor,clientGUI);
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
            
            //While a thread with a server exist we verify if the user input anything in the console
            while(serverAccessThread.isAlive()){
            	if(clientGUI.gotInput)
                {
            		System.out.println("message received");
                	serverThread.addNextMessage(clientGUI.getMessageFromGUI());
                	clientGUI.gotInput = false;
                }
            }
            if(!serverAccessThread.isAlive())
            {
            	System.out.println("Lost connection to host");
            }
        }catch(IOException ex){
            System.err.println("Fatal Connection error!");
            ex.printStackTrace();
        }catch(InterruptedException ex){
            System.out.println("Interrupted");
        }
    }
	
}
