package it.unisa.bugforecast;

import java.io.File;


public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		File fInput = new File("C:\\Users\\Aleandro\\Desktop\\Tesi\\QuattroClassi\\QuattroClassi\\bin");
		File fOutput = new File("C:\\Users\\Aleandro\\Desktop\\file.txt");
		CreateTask ct = new CreateTask(fInput, fOutput);
		ct.startTask();
	}
	
	
}
