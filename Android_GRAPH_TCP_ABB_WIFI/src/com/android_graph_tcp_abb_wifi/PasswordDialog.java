package com.android_graph_tcp_abb_wifi;

import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android_graph_tcp_abb_wifi.R;

public class PasswordDialog extends DialogFragment implements DialogInterface.OnClickListener {
	  private View form=null;
	  private EditText username = null; 
	  private EditText password = null;
	  public String wifiName = "none";
	  public String wifiPass = "-1";

	  @Override
	  public Dialog onCreateDialog(Bundle savedInstanceState) {
	    form= getActivity().getLayoutInflater().inflate(R.layout.password_dialog, null);
	    username=(EditText)form.findViewById(R.id.username);
	    password=(EditText)form.findViewById(R.id.password);
	    username.setText(wifiName);
	    password.requestFocus();
	    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
	    return(builder//.setTitle(R.string.dlg_title)
	    				.setView(form)
	                  .setPositiveButton(android.R.string.ok, this)
	                  .setNegativeButton(android.R.string.cancel, null).create());
	  }

	  @Override
	  public void onClick(DialogInterface dialog, int which) {
	    String template=getActivity().getString(R.string.toast);
	    String msg= String.format(template, username.getText().toString(),password.getText().toString());
	    wifiPass = password.getText().toString();
	    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	    ((WifiConnexionActivity)getActivity()).doPositiveClick(this);
	  }
	  
	  @Override
	  public void onDismiss(DialogInterface unused) {
	    super.onDismiss(unused);
	    Log.d(getClass().getSimpleName(), "Goodbye!");
	    ((WifiConnexionActivity)getActivity()).doNegativeClick(this);
	  }
	  
	  @Override
	  public void onCancel(DialogInterface unused) {
	    super.onCancel(unused);
	    Toast.makeText(getActivity(), R.string.back, Toast.LENGTH_LONG).show();
	    ((WifiConnexionActivity)getActivity()).doNegativeClick(this);
	  }
	  public void setWifiName(String wName) {
		  wifiName = wName;
	  }
	  public String getWifiPassword() {
		  return password.getText().toString();
	  }
}
