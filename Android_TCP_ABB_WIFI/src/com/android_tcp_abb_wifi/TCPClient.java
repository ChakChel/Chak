package com.android_tcp_abb_wifi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;


public class TCPClient {

	
    PrintWriter out;
    BufferedReader in;
    //private OnMessageReceived mMessageListener = null;

    
    private String connection_established = new String("Connexion etablie avec le client Android.\r");
    private String serverMessage;
    private boolean mRun = false;

    public static final String SERVERIP = "172.24.123.8"; 		// IP du superviseur
    public static final int SERVERPORT = 1235;					// port a determiner avec le superviseur
    private OnMessageReceived mMessageListener = null;

	
    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    
	
    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message+'\r');
            out.flush();
        }
    }




	public void run() {

		try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
 
            Log.e("TCP Client", "C: Connecting...");
 
            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVERPORT);
            //ABB_Activity.TCP_Task.publishProgress("test");
            try {
 
                mRun = true;

                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                
                /*if (out != null && !out.checkError()) {
                    out.println(connection_established);
                    out.flush();
                }*/

                Log.e("TCP Client", "C: Sent.");
 
                Log.e("TCP Client", "C: Done.");
 
                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                Log.e("TCP Client", "C: after ''in''.");

                while (mRun) {
                	
                    Log.e("TCP Client","We are before the in.readLine");

                    serverMessage = in.readLine();
                    
                    Log.e("TCP Client","We are in the while(mrRun)");

                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class 
                    	//(and doing so, publishProgress in DoInBackground !)
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;
 
                }    
                
                
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
 
 
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

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

    
}
