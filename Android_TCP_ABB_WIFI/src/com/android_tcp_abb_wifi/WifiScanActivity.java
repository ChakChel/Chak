package com.android_tcp_abb_wifi;

import java.util.ArrayList;
import java.util.HashMap;    
import java.util.List;    


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;    
import android.content.Intent;     
import android.content.IntentFilter;    
import android.net.wifi.ScanResult;     
import android.net.wifi.WifiManager;    
import android.os.Bundle;       
import android.view.View;    
import android.view.View.OnClickListener;       
import android.widget.Button;    
import android.widget.ListView;    
import android.widget.SimpleAdapter;    
import android.widget.TextView;    
import android.widget.Toast;

public class WifiScanActivity extends Activity implements OnClickListener {
	// L'identifiant de notre requête
	public final static int CHOOSE_BUTTON_REQUEST = 0;
	// L'identifiant de la chaîne de caracteres qui contient le	resultat de l'intent
	public final static String BUTTONS = "com.ixchel.select_reseau_wifi.Boutons";

    WifiManager wifi;
    TextView textStatus;
    Button buttonScan;
    int size = 0;
    List<ScanResult> results;

    String ITEM_KEY = "key";
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;
    
    ListView vue;
    
    private ArrayList<MWifiHotspot> wifihotspots;
    private ArrayList<ScanResult> scanResultList;
    

    /* Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_scan);
        
        performWifiHotspots();

        scanResultList = new ArrayList<ScanResult>();
        
        
        textStatus = (TextView) findViewById(R.id.textStatus);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(this);

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
            textStatus.setText(" Inactif ");
        }   
        else {
        	textStatus.setText(" Actif ");
        }

        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context c, Intent intent) 
            {
            	scanResultList.clear();
            	results = wifi.getScanResults();
               size = results.size();
               /// DEBUG
               System.out.println("nombre Wifi : "+size);
               wifihotspots.clear();
               
               for (int i=0; i<results.size(); i++) {
            	   ScanResult s = results.get(i);
            	   wifihotspots.add(new MWifiHotspot(s.SSID, s.level, s.frequency));
            	   scanResultList.add(s);
            	   System.out.println("SSID : "+s.SSID +"/f:"+s.frequency+"/level:"+s.level);
               } 
               pushWifiActifity();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));	
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// On verifie tout d'abord à quel intent on fait reference ici à l'aide de notre identifiant
		if (requestCode == CHOOSE_BUTTON_REQUEST) {
			// On verifie aussi que l'ope�?ration s'est bien deroulee 
			if (resultCode == RESULT_OK) {
				// On affiche le bouton qui a ete choisi
				Toast.makeText(this, "Vous avez choisi le bouton " + data.getStringExtra(BUTTONS), Toast.LENGTH_SHORT).show();
				System.out.println("Vous avez choisi le bouton " + data.getStringExtra(BUTTONS));

			}		
		}
	}
	void pushWifiActifity()  {
		Intent secondeActivite = new Intent(WifiScanActivity.this, WifiConnexionActivity.class);
		///
		secondeActivite.putExtra("wifihotspots", wifihotspots);
		secondeActivite.putExtra("scanResultList", scanResultList);
		// On associe l'identifiant à notre intent 
		startActivityForResult(secondeActivite, CHOOSE_BUTTON_REQUEST);		
	}
	
	
	
	void performWifiHotspots() {
		this.wifihotspots = new ArrayList<MWifiHotspot>();
		this.wifihotspots.add(new MWifiHotspot("Aucun réseau dispnible", 0, 6));
	}
    public void onClick(View view) 
    {
        arraylist.clear();
        wifi.startScan();
        Toast.makeText(this, "Scanning...." + size, Toast.LENGTH_SHORT).show();
    }    
}
