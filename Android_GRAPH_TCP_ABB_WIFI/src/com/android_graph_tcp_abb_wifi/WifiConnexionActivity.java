package com.android_graph_tcp_abb_wifi;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android_graph_tcp_abb_wifi.R;


public class WifiConnexionActivity extends FragmentActivity { 
	
	static String TAG = "WIFI : ";
	static String connectedSsidName = "-1";
	public String ServerIp = null;
	public String ServerPort = null;
	
	TextView title = null;
	ListView listView = null;
	
	private ArrayList<MWifiHotspot> wifihotspots;
	private ArrayList<ScanResult> scanResultList;
	
	private Button mButton1 = null;
	private Button mButton2 = null;
	
	
	private String m_Text = "-1";
	
	WifiManager wifiManager;
	
	
	TextView label1 = null;
	TextView label2 = null;
	TextView label3 = null;
	TextView label4 = null;
	
	
	private MWifiHotspot selectedWifiHotspot = null;

	private WifiConfiguration wc = null;
	
	
	EditText textOut;
	TextView textIn;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_connexion);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		label1 = (TextView) findViewById(R.id.textView2);
		label2 = (TextView) findViewById(R.id.textView4);
		label3 = (TextView) findViewById(R.id.textView6);
		label4 = (TextView) findViewById(R.id.textView8);
		
		// On recupere l'intent qui a lance cette activite
		Intent i = getIntent();
		wifihotspots = (ArrayList<MWifiHotspot>)i.getSerializableExtra("wifihotspots");
		scanResultList = (ArrayList<ScanResult>)i.getSerializableExtra("scanResultList");
		wc = new WifiConfiguration();
		
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
            System.out.println("WIFI Inactif ");
        }   
        else {
        	System.out.println("WIFI Actif ");
        }
        
        
        
        
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Connexion");
        alert.setMessage("Veuillez rentrer l'adresse IP et le port pour vous connecter au serveur :");

        // Set an EditText view to get user input 
        final EditText input_Ip = new EditText(this);
        input_Ip.setText("192.168.7.1");
        final EditText input_Port = new EditText(this);
        input_Port.setText("1234");
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(input_Ip);
        layout.addView(input_Port);
        alert.setView(layout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
        		
			Intent startABB = new Intent(WifiConnexionActivity.this, MainActivity.class);
			
			//posting the IP and Port data
			startABB.putExtra("ServerPort",input_Port.getText().toString() );
			startABB.putExtra("ServerIp",input_Ip.getText().toString() );

			startActivity(startABB);
			finish();
			
          }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            // Canceled.
          }
        });

      
		
		mButton1 = (Button) findViewById(R.id.button1); mButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//display a window with 2 fields : ip and port (pre-filled if possible)
                	alert.show();

				//and start activity in "go" button
			} 
		});
		mButton2 = (Button) findViewById(R.id.button2); mButton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent result = new Intent(); result.putExtra(WifiScanActivity.BUTTONS, "2"); setResult(RESULT_OK, result);
				finish();
				} 
		});
		
		configureTableView();
	}
	


	
	
	public String getIpAddr() {
		   WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		   WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		   int ip = wifiInfo.getIpAddress();

		   String ipString = String.format(
		   "%d.%d.%d.%d",
		   (ip & 0xff),
		   (ip >> 8 & 0xff),
		   (ip >> 16 & 0xff),
		   (ip >> 24 & 0xff));

		   return ipString;
		}

    public void testPing() {
        String info = new String();
        String edit = new String();
        String host = "192.168.43.165";
        
        InetAddress in;
        in = null;
        // we define the IP which we will ping
        try {
            in = InetAddress.getByName(host.toString());
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // we define the period within which we expect a response
        try {
            if (in.isReachable(5000)) {
                info = "Response OK";
            } else {
                info = "No response: Time out";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            info = e.toString();
        }
        
        
        System.out.println(info);
        System.out.println(edit);
        
    }
	
	
	
	public void showMe(View v, MWifiHotspot w) {
		PasswordDialog d = new PasswordDialog();
		d.setWifiName(w.SSID);	
		d.show(getSupportFragmentManager(), "ixchel");
	}
	 
	public void doPositiveClick(PasswordDialog pD) {
		 // Do stuff here.
		
		selectedWifiHotspot.password = pD.wifiPass;
		String offSet = "        ";
		label1.setText(offSet + selectedWifiHotspot.SSID);
		label2.setText(offSet + selectedWifiHotspot.password);
		label3.setText(offSet + ""+selectedWifiHotspot.signalLevel);
		label4.setText(offSet + ""+selectedWifiHotspot.signalFrequency);
		Log.i("FragmentAlertDialog", "Positive click!");
		
		
		
		
		connectTo(selectedWifiHotspot.SSID, selectedWifiHotspot.password);
		
		
	 }
	 public void doNegativeClick(PasswordDialog pD) {
		    // Do stuff here.
		Log.i("FragmentAlertDialog", "Negative click!");
	 }
	 public void configureTableView() {
		listView = (ListView) findViewById(R.id.listView1);
		String[][] repertoire = new String[this.wifihotspots.size()][2];
		for (int i =0; i<this.wifihotspots.size(); i++) {
			MWifiHotspot w = (MWifiHotspot)this.wifihotspots.get(i);
			repertoire[i][0] = w.SSID;
			repertoire[i][1] = "level : "+ w.signalLevel + "/" + "freq : " + w.signalFrequency;
		}
		List<HashMap<String, String>> liste = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> element;
		//Pour chaque personne dans notre réseau... 
		for(int i = 0 ; i < repertoire.length ; i++) {
			//... on creee un element pour la liste...
			element = new HashMap<String, String>();
			element.put("titre", repertoire[i][0]);
			element.put("description", repertoire[i][1]);
			element.put("img", String.valueOf(R.drawable.wifi_hotspot));
			liste.add(element);
		}
		ListAdapter adapter = new SimpleAdapter(this,liste,R.layout.wificonnexionlistviewcell,new String[] {"img", "titre", "description"},new int[] {R.id.img, R.id.titre, R.id.description});
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,long id) {
			// Que faire quand on clique sur un element de la liste ?
				showMe(null, wifihotspots.get(position));
				selectedWifiHotspot = wifihotspots.get(position); //// on recupere le reseau selectionné
			}
		});
	}
	 
	 
	 private void connectTo(String mySSID, String pass) {

		 connectToAP(mySSID, pass); 
		 
		 ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		 NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		 
	 }
	 
	 /// http://stackoverflow.com/questions/5504556/getting-scan-result-when-using-zxing
	 public void connectToAP(String ssid, String passkey) {
		    Log.i(TAG, "* connectToAP");

		    WifiConfiguration wifiConfiguration = new WifiConfiguration();

		    String networkSSID = ssid;
		    String networkPass = passkey;

		    Log.d(TAG, "# password " + networkPass);

		    for (ScanResult result : scanResultList) {
		    	
		        if (result.SSID.equals(networkSSID)) {

		            String securityMode = getScanResultSecurity(result);

		            if (securityMode.equalsIgnoreCase("OPEN")) {

		                wifiConfiguration.SSID = "\"" + networkSSID + "\"";
		                wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		                int res = wifiManager.addNetwork(wifiConfiguration);
		                Log.d(TAG, "# add Network returned " + res);

		                boolean b = wifiManager.enableNetwork(res, true);
		                Log.d(TAG, "# enableNetwork returned " + b);

		                wifiManager.setWifiEnabled(true);

		            } else if (securityMode.equalsIgnoreCase("WEP")) {

		                wifiConfiguration.SSID = "\"" + networkSSID + "\"";
		                wifiConfiguration.wepKeys[0] = "\"" + networkPass + "\"";
		                wifiConfiguration.wepTxKeyIndex = 0;
		                wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		                int res = wifiManager.addNetwork(wifiConfiguration);
		                Log.d(TAG, "### 1 ### add Network returned " + res);

		                boolean b = wifiManager.enableNetwork(res, true);
		                Log.d(TAG, "# enableNetwork returned " + b);

		                wifiManager.setWifiEnabled(true);
		            }

		            wifiConfiguration.SSID = "\"" + networkSSID + "\"";
		            wifiConfiguration.preSharedKey = "\"" + networkPass + "\"";
		            wifiConfiguration.hiddenSSID = true;
		            wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
		            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		            wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		            wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

		            int res = wifiManager.addNetwork(wifiConfiguration);
		            Log.d(TAG, "### 2 ### add Network returned " + res);

		            wifiManager.enableNetwork(res, true);

		            boolean changeHappen = wifiManager.saveConfiguration();

		            if(res != -1 && changeHappen){
		                Log.d(TAG, "### Change happen");

		                WifiConnexionActivity.connectedSsidName = networkSSID;

		            }else{
		                Log.d(TAG, "*** Change NOT happen");
		            }

		            wifiManager.setWifiEnabled(true);
		        }
		    }
		}

		public String getScanResultSecurity(ScanResult scanResult) {
		    Log.i(TAG, "* getScanResultSecurity");
		    final String cap = scanResult.capabilities;
		    final String[] securityModes = { "WEP", "PSK", "EAP" };

		    for (int i = securityModes.length - 1; i >= 0; i--) {
		        if (cap.contains(securityModes[i])) {
		            return securityModes[i];
		        }
		    }
		    return "OPEN";
		}

		
}