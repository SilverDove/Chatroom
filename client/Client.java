package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client 
{

	private static final String host = "localhost";
    private static final int portNumber = 4444;

    //TODO implement login feature
    private String userName;
    private String userPassword;
    private String serverHost;
    private int serverPort;
    private Scanner userInputScanner;
    private AES clientEncriptor;
    
    public static void main(String[] args){
        String readName = null;
        Scanner scan = new Scanner(System.in);
        System.out.println("Please input username:");
        while(readName == null || readName.trim().equals("")){
            // null, empty, whitespace(s) not allowed.
            readName = scan.nextLine();
            if(readName.trim().equals("")){
                System.out.println("Invalid. Please enter again:");
            }
        }

        //Creating a client from the inputed user name
        Client client = new Client(readName, host, portNumber);
        client.startClient(scan);
    }

    private Client(String userName, String host, int portNumber){
        this.userName = userName;
        this.serverHost = host;
        this.serverPort = portNumber;
        clientEncriptor = new AES();
    }

    private void startClient(Scanner scan){
        try{
            Socket socket = new Socket(serverHost, serverPort);
            Thread.sleep(1000); // waiting for network communicating.
            
            //initiating a server thread
            ServerThread serverThread = new ServerThread(socket, userName, clientEncriptor);
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
            
            //While a thread with a server exist we verify if the user input anything in the console
            while(serverAccessThread.isAlive()){
                if(scan.hasNextLine()){
                    serverThread.addNextMessage(scan.nextLine());
                }
                // NOTE: scan.hasNextLine waits input (in the other words block this thread's process).
                // NOTE: If you use buffered reader or something else not waiting way,
                // NOTE: I recommends write waiting short time like following.
                // else {
                //    Thread.sleep(200);
                // }
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