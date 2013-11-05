
package com.android_graph_tcp_abb_wifi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	String ServerPort = null;
	String ServerIp = null;

	Random random = new Random();
	public ViewPager pager = null;
	SampleAdapter sampleAdapter = null;
	
	//Bundle extras = getIntent().getExtras(); 
		
	public ArrayList <DataInc> dataIncList = new ArrayList<DataInc>();
	
    private static TCPClient mTcpClient;
	boolean initialization_done = false;
	
	int nombreMaxBoosts = 256;	//need modulo of 4

	BoostView boostViews[] = new BoostView[nombreMaxBoosts];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		Intent intent = getIntent();
		ServerPort = intent.getStringExtra("ServerPort");
		ServerIp = intent.getStringExtra("ServerIp");

	    int i;
	    
		//creating and initializing all the boosts
		for(i=0;i<nombreMaxBoosts;i++){
			dataIncList.add(new DataInc());
		}
	        	        	    
		pager=(ViewPager)findViewById(R.id.pager);
		sampleAdapter = new SampleAdapter(this);
		pager.setAdapter(sampleAdapter);
	
	}
	
	@Override
	public void onPause(){
	
		super.onPause();
		
	}

	public void commandHasBeenChanged(BoostView currentBV) {

		mTcpClient.sendMessage(currentBV.position+"\t"+currentBV.V+"\n");
		
		System.out.println("commandHasBeenChanged commandHasBeenChanged commandHasBeenChanged : " + currentBV);
	}
	
	public void onRestart(Bundle savedInstanceState) {
		  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    int i;
	    
		//creating and initializing all the boosts
		for(i=0;i<nombreMaxBoosts;i++){
			dataIncList.add(new DataInc());
		}
	        	        	    
		pager=(ViewPager)findViewById(R.id.pager);
		sampleAdapter = new SampleAdapter(this);
		pager.setAdapter(sampleAdapter);

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch(item.getItemId())
		{
		//about
		case R.id.about:
			Toast toast = Toast.makeText(this, R.string.About, Toast.LENGTH_SHORT);
			toast.show();
			return true;

		/*** TO BE TESTED	*/
		case R.id.disconnect: 
			Dc_Dialog dc_dialog = new Dc_Dialog();
			// should work ... show() method really demanding !
			dc_dialog.show(getFragmentManager(), dc_dialog.getTag());
			return true; 
		}
	return super.onOptionsItemSelected(item);
	}

	
	/*** recently implemented */
	static public class Dc_Dialog extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage("Etes-vous sur de vouloir vous deconnecter ?")
	               .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							try {
								mTcpClient.socket.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							getActivity().finish();

	                   }
	               })
	               .setNegativeButton("Non", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	} 

	
	
	
  /*
   * Inspired by
   * https://gist.github.com/8cbe094bb7a783e37ad1
   */
  private class SampleAdapter extends PagerAdapter {
	  
	private MainActivity mActivity;
	 	 
	public BoostView view1 = null;
	private BoostView view2 = null;
	private BoostView view3 = null;
	private BoostView view4 = null;
	
	boolean executeTCPTaskOneTime = true;
	

	public SampleAdapter(MainActivity activity) {
		super();
		
		mActivity = activity;
	}

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      View page= getLayoutInflater().inflate(R.layout.page, container, false);

      view1=(BoostView)page.findViewById(R.id.view1);
      view1.mainActivity = mActivity;
      position=position * 4;
      populateBoostView(view1, position);
      view1.setTag(position);
      boostViews[position] = view1;
      position++;
      
      
      
      view2=(BoostView)page.findViewById(R.id.view2);
      view2.mainActivity = mActivity;
      populateBoostView(view2, position);
      view2.setTag(position);
      boostViews[position] = view2;
      position++;
      
      
      view3=(BoostView)page.findViewById(R.id.view3);
      view3.mainActivity = mActivity;
      populateBoostView(view3, position);
      view3.setTag(position);
      boostViews[position] = view3;
      position++;
      
      
      view4=(BoostView)page.findViewById(R.id.view4);
      view4.mainActivity = mActivity;
      view4.setTag(position);
      boostViews[position] = view4;
      
      
      if (position < getRealCount()) {
    	  populateBoostView(view4, position);
    	  view4.setVisibility(View.VISIBLE);
      }
      else {
    	  view4.setVisibility(View.INVISIBLE);
      }

      
      
      /// update the view
      container.addView(page, 0);
      
      if (executeTCPTaskOneTime == true) {
    	  new TCP_Task(mActivity).execute("");
    	  executeTCPTaskOneTime = false;
      }
      
      return(page);
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
      container.removeView((View)object);
    }

    @Override
    public int getCount() {
      return((getRealCount() /4) + 0);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

    	return(view == object);
    }

    private int getRealCount() {
      return(nombreMaxBoosts);
    }
    /*
     * Met a jour l'id de la BoostView
     * */
    private void populateBoostView(BoostView tv, int position) {
      tv.position = position + 1;
    }
        
  }
  
  //TCP
	public class TCP_Task extends AsyncTask<String,String,TCPClient> {
		
		private MainActivity myActivity = null;
		
		public TCP_Task(MainActivity anActivity) {
			myActivity = anActivity;
			
		}
		

		protected void onPreExecute (){
			

		}

		protected TCPClient doInBackground (String... message){
			
            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
            	
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate through publishProgress
                	
                	//Log.e("Main","Voici le message recu : "+ message);
                	int BoostNumber = 0;
                	
                	//split of the string of data in several strings of data, all relative to one precise Boost   	
                	String[] dataList = message.split(":");                	

                    // Data setting loop. Assigning the latest data to their respective boost
                    // the data from dataList.Length Boosts are received. Gotta treat'em all
                	for( BoostNumber=0;BoostNumber < dataList.length;BoostNumber++ ){
                			dataIncList.get(BoostNumber).splitDataABB(dataList[BoostNumber]);
   
                	}
                	
                    //refresh the display. The argument String is not actually used
                	publishProgress("update");
                
            	    mTcpClient.setShouldIRequestTheSupervisor(true);
            	    
                }
                
               
                
            }, ServerIp, ServerPort);
           
            mTcpClient.run();
            
			
			//if we get here, that is due to an issue ...
			return null;
			
		}

		protected void onProgressUpdate(String ...values){
			
		//This method runs in the same thread as the UI. 
				for (int i=0; i<nombreMaxBoosts; i++) {


			    	BoostView boostView = (BoostView)pager.findViewWithTag(i);
			    	
			    	if (boostView != null) {
			    		boostView.mainActivity = myActivity;
			    		InitBoostViewWith(boostView);
			    	}
			    	
					
					pager.invalidate();
					pager.postInvalidate();
				}
				
				for (int i=0; i<dataIncList.size(); i++) {
	
	
			    	BoostView boostView = (BoostView)pager.findViewWithTag(i);
			    	
			    	if (boostView != null) {
			    		boostView.mainActivity = myActivity;
			    		updateBoostViewWith(boostView, dataIncList.get(i));
			    	}
			    	
					
					pager.invalidate();
					pager.postInvalidate();
				}
			
		}

	    /*
	     * Raffraichir la vue via un objet 'DataInc'
	     * */
	    private void updateBoostViewWith(BoostView bv, DataInc dataInc) {

    		if (dataInc.getVo()>0)
    			bv.boostState = true;
    		
    		bv.position = dataInc.getN();
	    	bv.Vi = dataInc.getVi();
	    	bv.Il = dataInc.getIl();
	    	bv.Pi = dataInc.getPi();
	    	bv.Vo = dataInc.getVo();
	    	bv.Io = dataInc.getIo();
    	
    	
    	//re-draw the interface
    	bv.invalidate();
    	bv.postInvalidate();
    	

    }
	    //Method to tell the user a boost is disconnected
	    private void InitBoostViewWith(BoostView bv) {

   			bv.boostState = false;
    		
   	
    	//re-draw the interface
    	bv.invalidate();
    	bv.postInvalidate();
    	

    }
		
		
		protected void onPostExecute (TCPClient mTcpClient){
					
			
		}
		
		
	}
}
