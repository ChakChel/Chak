package com.android_tcp_envoi;


import com.android_tcp_envoi.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	
    private TCPClient mTcpClient;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        final EditText editText = (EditText)findViewById(R.id.editText_send);
        
        Button send = (Button)findViewById(R.id.send_button);

		
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
			
			// � remplir !!		
			
			mTcpClient = new TCPClient();
			mTcpClient.run();
			
			
			return null;
			
		}
		
		
		protected void onProgressUpdate (String... values){
			
			// � remplir pour le debug surtout dans un premier temps.
			
			
			
		}
		
		protected void onPostExecute (TCPClient mTcpClient){
			
			//rien ici a priori ... La connexion ne renvoit rien directement ?
			
			
		}
		
		
		
		
		
		
		
	}
}