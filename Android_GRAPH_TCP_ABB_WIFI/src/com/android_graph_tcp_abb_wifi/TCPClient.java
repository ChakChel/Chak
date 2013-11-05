package com.android_graph_tcp_abb_wifi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;


public class TCPClient {
	private String SERVERIP = null;
	private int SERVERPORT = 0;
	Socket socket;
    PrintWriter out;
    BufferedReader in;
    //private OnMessageReceived mMessageListener = null;

    private String serverMessage;
	private boolean mRun = false;

    //public static final String SERVERIP = "192.168.1.46"; 		// IP du superviseur
    //public static final int SERVERPORT = 1235;					// port a determiner avec le superviseur
    private OnMessageReceived mMessageListener = null;

    private boolean shouldIRequestTheSupervisor = false; // let the user choose when he wants to request
	private String ServerIp = null;
	private int ServerPort = 0;
    
	
    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(OnMessageReceived listener,String Server_Ip,  String Server_Port) {
        mMessageListener = listener;
        this.ServerIp = Server_Ip;
        this.ServerPort = Integer.parseInt(Server_Port);
        SERVERIP = ServerIp;
        SERVERPORT = ServerPort;
        
    }

    
	
    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
    	
        if (out != null && !out.checkError()) {
        	System.out.println(" in sendMessage");
        	
            out.println(message);
            out.flush();
        	Log.e("SENDING",message);

        }
        else {
        	if (out != null) {
        		System.out.println("sendMessage: Error : " + out.checkError() );
        	}
        	else {
        		System.out.println(" sendMessage: out NULLError : " );
        	}
        	
        }
    }

	public void run() {

		try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(this.SERVERIP);
 
            Log.e("TCP Client", "C: Connecting...");
 
            //create a socket to make the connection with the server
            socket = new Socket(serverAddr, this.SERVERPORT);

            try {
 
                mRun = true;

                //item send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                //create and initialize the input item
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                if (out != null && !out.checkError()) {
                    out.println("0\t0\n");
                    out.flush();
                }

                Log.e("TCP Client", "C: Sent.");
 
                Log.e("TCP Client", "C: Done.");
 
                //receive the message which the server sends back
                
                Log.e("TCP Client", "C: after ''in''.");

                while (true) {
                	
                    Log.e("TCP Client","We are before the in.readLine");

                    
                    serverMessage = in.readLine();
                    
                    Log.e("TCP Client","We are in the while(mrRun)");

                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageRecei@ved from MyActivity class 
                    	//(and doing so, publishProgress in DoInBackground !)
                        mMessageListener.messageReceived(serverMessage);
                        Thread.sleep(1000);
                        
                        if (isShouldIRequestTheSupervisor()) {
                        	this.sendMessage("0\t0\n");
                        }
                    }
                    serverMessage = null;
 
                }    
                //Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
 
 
            } catch (Exception e) {
 
                Log.e("TCP", "S: Error", e);
 
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }
 
        } catch (Exception e) {
 
            Log.e("TCP", "C: Error", e);
 
            
        }
		
	}

    public boolean isShouldIRequestTheSupervisor() {
		return shouldIRequestTheSupervisor;
	}



	public void setShouldIRequestTheSupervisor(
			boolean ashouldIRequestTheSupervisor) {
		shouldIRequestTheSupervisor = ashouldIRequestTheSupervisor;
	}

	//Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

    
}
