package com.android_graph_tcp_abb_wifi;

import android.os.Parcel;
import android.os.Parcelable;

public class MWifiHotspot implements Parcelable{

	public String SSID;
	public String password;
	public int signalLevel;
	public int signalFrequency;
	
	public MWifiHotspot(String mySSID, int mySignalLevel, int mySignalFrequency) {
		this.SSID = mySSID;
		this.password = "";
		this.signalLevel = mySignalLevel;
		this.signalFrequency = mySignalFrequency;	
	}
	
	@Override
	public int describeContents() {
		//On renvoie 0, car notre classe ne contient pas de	FileDescriptor
		return 0; 
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
	// On ajoute les objets dans l'ordre dans lequel on les a deÌ?clareÌ?s
		dest.writeString(SSID);
		dest.writeString(password);
		dest.writeInt(signalLevel);
		dest.writeInt(signalFrequency);
	}
	
	public static final Parcelable.Creator<MWifiHotspot> CREATOR = new Parcelable.Creator<MWifiHotspot>() {
		@Override
		public MWifiHotspot createFromParcel(Parcel source) {
			return new MWifiHotspot(source); 
		}
		@Override
		public MWifiHotspot[] newArray(int size) {
			return new MWifiHotspot[size];
		}
	};
	public MWifiHotspot(Parcel in) {
		SSID = in.readString();
		password = in.readString();
		signalLevel = in.readInt();
		signalFrequency = in.readInt();
	}
}
