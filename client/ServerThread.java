package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
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
    private AES clientEncryptor;
    private ObjectOutputStream objectOStream;
    private MessagePacket clientPacket;
    
    
    public ServerThread(Socket socket, String userName, AES encryptor) throws IOException{
        this.socket = socket;
        this.userName = userName;
        messagesToSend = new LinkedList<String>();
        clientEncryptor = encryptor;
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
            serverOut = null;
            
            Scanner serverIn = new Scanner(serverInStream);
            
            //Instantiating the stream to send our packets
            objectOStream = new ObjectOutputStream(socket.getOutputStream());
            
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
                    	//We split the string corresponding to the command to extract the username
                    	//of the receiver and the message to send
                    	String[] result = nextSend.split("/");
                    	
                    	clientPacket = new MessagePacket(result[1],userName,result[2],clientEncryptor);
                    	clientPacket.encryptMessage();
                    	
                    	objectOStream.reset();
                    	objectOStream.writeObject(clientPacket);
                    	objectOStream.flush();
                    }
                    //We print a normal message 
                    else if(!(nextSend.isEmpty()))
                    {
                    	clientPacket = new MessagePacket(userName,nextSend);
                    	
                    	objectOStream.reset();
                    	objectOStream.writeObject(clientPacket);
                    	objectOStream.flush();
                    	//objectOStream.close();
                    	//serverOut.println(userName + " > " + nextSend);
                        //serverOut.flush();
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
