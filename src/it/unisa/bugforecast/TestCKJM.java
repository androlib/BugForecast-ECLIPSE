package it.unisa.bugforecast;

import java.io.File;


public class TestCKJM {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		File fInput = new File("C:\\Users\\aleso\\Desktop\\Tesi\\Testest\\bin");
		File fOutput = new File("C:\\Users\\aleso\\Desktop\\file.txt");
		CreateTask ct = new CreateTask(fInput, fOutput);
		ct.startTask();
	}
	
	
}
