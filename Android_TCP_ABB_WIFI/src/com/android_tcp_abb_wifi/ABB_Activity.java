package com.android_tcp_abb_wifi;


import com.android_tcp_abb_wifi.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class ABB_Activity extends Activity {

	ArrayList <DataInc> dataIncList = new ArrayList<DataInc>();
    private TextView Rec_display = null;
    private TextView Rec_message = null;
    
    private TCPClient mTcpClient;

	private DataOut data_1 = new DataOut('0','0');
	
	boolean initialization_done = false;
	
	int i;
	boolean isPresent = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
        final EditText data_out_n_editText = (EditText) findViewById(R.id.data_out_n_value);
        final EditText data_out_v_editText = (EditText) findViewById(R.id.data_out_v_value);

        Button send = (Button)findViewById(R.id.send_button);
        
        Rec_display = (TextView) findViewById(R.id.textView_rec_display);
        Rec_message = (TextView) findViewById(R.id.textView_rec_message);

        
        Rec_display.setText("Display test pre-task");
        
		new TCP_Task().execute("");
		
		
		send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 
                data_1.setN(data_out_n_editText.getText().toString().charAt(0));
                data_1.setV(data_out_v_editText.getText().toString().charAt(0));
 
                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(data_1.format());
                }
 
            }
        });
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	
	
	
	

	public class TCP_Task extends AsyncTask<String,String,TCPClient> {
	
		protected void onPreExecute (){
			
			//initializations ?
			
		}
		
		
		
		protected TCPClient doInBackground (String... message){
			
            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
            	
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate through publishProgress
                	
                	Log.e("Main","Voici le message recu : "+ message);
                	int BoostNumber = 0;
                	
                	//split of the string of data in several strings of data, all relative to one precise Boost   	
                	String[] dataList = message.split(":");

                    Log.e("Main", "Nombre d'elts de la liste dataList : " + dataList.length);
                	
                    
                    //Data setting loop
                    // the data from dataList.Length Boosts are received. Gotta treat'em all
                    while( BoostNumber <= dataList.length-1 ){
                		
                    	//if unknown boost number (N), create a new element, fill it with the latest data and add it to list
                    	
                    
                    	//gotta add an initialization case...	
                		if(initialization_done){

                			
                    	
	                    	//Let's check if the boost hans't already been processed in our list.
                			i = 0;
	                    	isPresent = false;
	                   		
	                  		for(i=0; i <= dataIncList.size() -1 ;i++){

	                   			if(Integer.parseInt( dataList[BoostNumber].split("\t")[0] ) == dataIncList.get(i).getN()){
	                   				isPresent=true;
	                   				break;
	                   			}
	              			}
	               			
	                  		// if the boost is already there in the list, we update the data
	               			if(isPresent == true){
	               				
                    			dataIncList.get(i).splitDataABB(dataList[BoostNumber]);

	               				
	           				}else{	
	           					
	           					//else we create the boost and store its data
		                		DataInc dataInc = new DataInc();
		                		dataInc.splitDataABB(dataList[BoostNumber]);
								dataIncList.add(dataInc); 

	
	           				}
	                
                
                		}else{
                		
	                		DataInc dataInc = new DataInc();
	                		dataInc.splitDataABB(dataList[BoostNumber]);
							dataIncList.add(dataInc); 
							initialization_done = true;
                			
                		}
                		BoostNumber++;
                    	
                    	
                    	
                    	
                    	/*N_Boost=0;
                    	
                    	//This loop checks if the Boost hasn't already been processed
                    	while( N_Boost <= dataIncList.size() ){
                    		
							//we check if the boost number (N) isn't already in the database
                    		
                    		if(initialization_done){
                    			Log.e("Main","dataIncList.get(dataList.length-BoostNumber).getN() : " + dataIncList.get(dataList.length-BoostNumber).getN() );
                                int temp = Integer.parseInt(		dataList[N_Boost].split("\t")[0]      );
                                Log.e("Main", "Integer.parseInt(		dataList[N_Boost].split(''t'')[0]      ) : " + temp );

                    		}

                    		if(initialization_done && (dataIncList.get(dataList.length-BoostNumber).getN() == Integer.parseInt(	dataList[N_Boost].split("\t")[0] ) ) ){
                                
                    			dataIncList.get(N_Boost).splitDataABB(dataList[BoostNumber]);
                    			
                    		}else{
                    			
		                		DataInc dataInc = new DataInc();
		                		dataInc.splitDataABB(dataList[BoostNumber]);
								dataIncList.add(dataInc); 
	                    	
                    		}
                    		N_Boost++;
                    	}
                    	
						initialization_done = true;
                    	BoostNumber ++;*/

                	}
                	
                    //refresh the display. The argument String is not actually used
                	publishProgress("update");
                }
            });

			mTcpClient.run();
			
			
			//if we get here, that is due to an issue ...
			publishProgress("erreur reception");
			
			
			return null;
			
		}
		
		
		
		
		
		
		
		protected void onProgressUpdate (String... values){
			
			//Rec_display.setText(values[0]);
            Log.e("Main", "Before string setting.");

			String tout_a_la_suite = Float.toString(dataIncList.get(0).getN()) +" "
									+Float.toString(dataIncList.get(0).getV()) +" " 
									+Float.toString(dataIncList.get(0).getVi()) +" " 
									+Float.toString(dataIncList.get(0).getIl()) +" " 
									+Float.toString(dataIncList.get(0).getPi()) +" " 
									+Float.toString(dataIncList.get(0).getVo()) +" " 
									+Float.toString(dataIncList.get(0).getIo()) + "\n" 
									+Float.toString(dataIncList.get(1).getN()) +" " 
									+Float.toString(dataIncList.get(1).getV()) +" " 
									+Float.toString(dataIncList.get(1).getVi()) +" " 
									+Float.toString(dataIncList.get(1).getIl()) +" " 
									+Float.toString(dataIncList.get(1).getPi()) +" " 
									+Float.toString(dataIncList.get(1).getVo()) +" " 
									+Float.toString(dataIncList.get(1).getIo()) +"\n"
									+Float.toString(dataIncList.get(2).getN()) +" " 
									+Float.toString(dataIncList.get(2).getV()) +" " 
									+Float.toString(dataIncList.get(2).getVi()) +" " 
									+Float.toString(dataIncList.get(2).getIl()) +" " 
									+Float.toString(dataIncList.get(2).getPi()) +" " 
									+Float.toString(dataIncList.get(2).getVo()) +" " 
									+Float.toString(dataIncList.get(2).getIo()) +"\n"
									+Float.toString(dataIncList.get(3).getN()) +" " 
									+Float.toString(dataIncList.get(3).getV()) +" " 
									+Float.toString(dataIncList.get(3).getVi()) +" " 
									+Float.toString(dataIncList.get(3).getIl()) +" " 
									+Float.toString(dataIncList.get(3).getPi()) +" " 
									+Float.toString(dataIncList.get(3).getVo()) +" " 
									+Float.toString(dataIncList.get(3).getIo()) +"\n";
					            
			Log.e("Main", "After string setting.");
			
			Rec_display.setText(tout_a_la_suite);
            Log.e("Main", "After Rec_display.setText.");

		}
		
		
		
		
		
		
		protected void onPostExecute (TCPClient mTcpClient){
			
			//rien ici a priori ... La connexion ne renvoit rien directement ?
			
			
		}
		
		
		
		
		
		
		
	}
}