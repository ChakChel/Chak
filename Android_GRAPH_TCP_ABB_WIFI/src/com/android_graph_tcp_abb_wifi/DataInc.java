package com.android_graph_tcp_abb_wifi;

public class DataInc {
	
		public int N,V;
		public float Vi, Il, Pi, Vo, Io;

		
		//Builder for the class
		public DataInc(int n, int v, float vi, float il, float pi, float vo, float io) {
			N = n;
			V = v;
			Vi = vi;
			Il = il;
			Pi = pi;
			Vo = vo;
			Io = io;
		}
		
		//Builder initializing the fields
		public DataInc() {
			N = 0;
			V = 0;
			Vi = 0;
			Il = 0;
			Pi = 0;
			Vo = 0;
			Io = 0;
		}
 
		
		//getters and setters
		public int getN() {
			return N;
		}
		public void setN(int n) {
			N = n;
		}
		public int getV() {
			return V;
		}
		public void setV(int v) {
			V = v;
		}
		public float getVi() {
			return Vi;
		}
		public void setVi(float vi) {
			Vi = vi;
		}
		public float getIl() {
			return Il;
		}
		public void setIl(float il) {
			Il = il;
		}
		public float getPi() {
			return Pi;
		}
		public void setPi(float pi) {
			Pi = pi;
		}
		public float getVo() {
			return Vo;
		}
		public void setVo(float vo) {
			Vo = vo;
		}
		public float getIo() {
			return Io;
		}
		public void setIo(float io) {
			Io = io;
		}

		
		
		//splitting method to store data received through ABB protocol
		public void splitDataABB(String incMessage){
		
			//the split method separates the data from one another
			String[] List = incMessage.split("\t");
			
			//the sequence theses data are received in is : N\tV\tVi\tIl\tPi\tVo\tIo:
			
			//the 2 indicators and five measures are casted from String to Float and stored
			
			this.setN(Integer.parseInt(List[0]));
			this.setV(Integer.parseInt(List[1]));
			this.setVi(Float.parseFloat(List[2]));
			this.setIl(Float.parseFloat(List[3]));
			this.setPi(Float.parseFloat(List[4]));
			this.setVo(Float.parseFloat(List[5]));
			this.setIo(Float.parseFloat(List[6]));
			
			
		}
}
