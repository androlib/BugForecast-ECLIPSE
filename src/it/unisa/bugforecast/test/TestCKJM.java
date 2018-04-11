package it.unisa.bugforecast.test;

import java.io.File;

import it.unisa.bugforecast.CreateTask;


public class TestCKJM {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		File fInput = new File("C:\\Users\\aleso\\Desktop\\Tesi\\binEs");
		File fOutput = new File("file.txt");
		CreateTask ct = new CreateTask(fInput, fOutput);
		ct.startTask();
	}
	
	
}
