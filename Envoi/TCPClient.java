package com.android_tcp_envoi;

import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class TCPClient {

	
    PrintWriter out;
    String connection_established = new String("Connexion etablie avec le client Android.\r");
    public static final String SERVERIP = "192.168.1.19"; 	// IP du superviseur
    public static final int SERVERPORT = 8600;					// port a determiner avec le superviseur

	
	
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
 
            try {
 
                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                
                if (out != null && !out.checkError()) {
                    out.println(connection_established);
                    out.flush();
                }

                Log.e("TCP Client", "C: Sent.");
 
                Log.e("TCP Client", "C: Done.");
 
                /*//receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 
                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = in.readLine();
 
                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;
 
                }*/
 
 
                //Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
 
                while(true);
 
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

    
    
}
