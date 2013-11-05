package com.android_graph_tcp_abb_wifi;

public class DataOut {
	
	public char N, V;

	public DataOut(char n, char v) {
		super();
		N = n;
		V = v;
	}

	public char getN() {
		return N;
	}

	public void setN(char n) {
		N = n;
	}

	public char getV() {
		return V;
	}

	public void setV(char v) {
		V = v;
	}

	public String format() {
		
		String formattedCommand = this.getN() + "\t" + this.getV() + "\n";
		
		return formattedCommand;
	}
	
}
