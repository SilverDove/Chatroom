package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable{

	private Socket socket;
    private PrintWriter clientOut;
    private ChatServer server;
    private PrintWriter serverOutput;

    public ClientThread(ChatServer server, Socket socket){
        this.server = server;
        this.socket = socket;
    }

    private PrintWriter getWriter(){
        return clientOut;
    }

    @Override
    public void run() {
        try{
            // setting up
        	InputStream inputtest = socket.getInputStream();
        	BufferedReader reader = new BufferedReader(new InputStreamReader(inputtest));
            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            Scanner in = new Scanner(socket.getInputStream());
            
            //OutputStream output = socket.getOutputStream();
            //serverOutput = new PrintWriter(output,true);
            
            //getting username
            String username = reader.readLine();
            server.addUserName(username);
            
            // start communicating
            while(!socket.isClosed()){
                if(in.hasNextLine()){
                    String input = in.nextLine();
                    // NOTE: if you want to check server can read input, uncomment next line and check server file console.
                    // System.out.println(input);
                    
                    //We verify if the string in the server input stream is a username
                    //If its the case we will print a private message
                    if(server.userExist(input))
                    {
                    	//String to get the name of the receiver
                    	String localUserName;
                    	localUserName = input;
                    	input = in.nextLine();
                    	
                    	//We send the private message
                    	sendPrivateMessage(localUserName,input);
                    	input = in.nextLine();
                    	this.clientOut.println(input);
                    	this.clientOut.flush();
                    }
                    //If the message to send is not a private message we send the message
                    //to every clients
                    else
                    {
	                	for(ClientThread thatClient : server.getClients()){
		                    PrintWriter thatClientOut = thatClient.getWriter();
		                    if(thatClientOut != null){
		                        thatClientOut.write(input + "ethvrthvrthvr\r\n");
		                        
		                        thatClientOut.flush();
		                    }
	                    }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
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
