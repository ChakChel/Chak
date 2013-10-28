package com.android_tcp_reception;


import com.android_tcp_reception.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	
    private TextView Rec_display = null;
    private TextView Rec_message = null;
	
    private TCPClient mTcpClient;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        final EditText editText = (EditText) findViewById(R.id.editText_send);
        Button send = (Button)findViewById(R.id.send_button);
        Rec_display = (TextView) findViewById(R.id.textView_rec_display);
        Rec_message = (TextView) findViewById(R.id.textView_rec_message);
        //String data_Rec = null;

        Rec_display.setText("Display test pre-task");
        
		new TCP_Task().execute("");
		
		
		send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 
                String message = editText.getText().toString();
 
                //add the text in the arrayList
                //arrayList.add("c: " + message);
 
                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }
 
                //refresh the list
                //mAdapter.notifyDataSetChanged();
                //editText.setText("");
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
			
			//initialisations
			
		}
		
		
		
		protected TCPClient doInBackground (String... message){
			
			// à remplir !!		
			
            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });

			mTcpClient.run();
			
			// call publishProgress ?
			
			//Si on arrive ici, c'est qu'il y a eu un problème ...
			publishProgress("erreur reception");
			
			
			return null;
			
		}
		
		
		protected void onProgressUpdate (String... values){
			
			Rec_display.setText(values[0]);
			
			
			
		}
		
		protected void onPostExecute (TCPClient mTcpClient){
			
			//rien ici a priori ... La connexion ne renvoit rien directement ?
			
			
		}
		
		
		
		
		
		
		
	}
}