package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class ServerThread implements Runnable
{

	private Socket socket;
    private String userName;
    private boolean isAlived;
    //List containing all the message to send to the server
    private final LinkedList<String> messagesToSend;
    //boolean to verify if there is a message in the list
    private boolean hasMessages = false;
    
    public ServerThread(Socket socket, String userName){
        this.socket = socket;
        this.userName = userName;
        messagesToSend = new LinkedList<String>();
        
    }

    public void addNextMessage(String message){
        synchronized (messagesToSend){
            hasMessages = true;
            messagesToSend.push(message);
        }
    }

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
            
            Scanner serverIn = new Scanner(serverInStream);
            // BufferedReader userBr = new BufferedReader(new InputStreamReader(userInStream));
            // Scanner userIn = new Scanner(userInStream);

            while(!socket.isClosed()){
                if(serverInStream.available() > 0){
                    if(serverIn.hasNextLine()){
                        System.out.println(serverIn.nextLine());
                    }
                }
                if(hasMessages){
                    String nextSend = "";
                    synchronized(messagesToSend){
                        nextSend = messagesToSend.pop();
                        hasMessages = !messagesToSend.isEmpty();
                    }

                    //For private message, format is /username/messagetosend
                    if(nextSend.contains("/"))
                    {
                    	String toSend = "";
                    	
                    	//We split the string corresponding to the command to extract the username
                    	//of the receiver and the message to send
                    	String[] result = nextSend.split("/");
                    	serverOut.write(result[1]+"\n"); 
                    	
                    	//We send to the server the first element splited: the receiver's name
                    	serverOut.flush();
                    	toSend = "From " + userName + " > " + result[2] + "\n";
                    	serverOut.write(toSend);
                    	
                    	//We send to the server the message to print to the receiver
                    	serverOut.flush();
                    	
                    	toSend = "To " + result[1] + " > " + result[2] + "\n";
                    	serverOut.write(toSend);
                    	
                    	//We send to the server the message to print to the sender
                    	serverOut.flush();
                    }
                    //We print a normal message 
                    else if(!(nextSend.isEmpty()))
                    {
                    	serverOut.println(userName + " > " + nextSend);
                        serverOut.flush();
                    }
                }
            }
            if(socket.isClosed())
            {
	        	 serverOut.println("Lost connection to host: Server closed");
	             serverOut.flush();
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
	
}
