package it.unisa.bugforecast;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;


public class TestCSV {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ReaderTxt rt;
		CreateCSV csv;
		try {
			rt = new ReaderTxt(new FileReader("C:\\Users\\aleso\\Desktop\\Tesi\\file.txt"));
			ArrayList<MetricClass> list = rt.execute();
			csv = new CreateCSV("MetricClasses.csv", list);
			csv.execute();
			} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} 
		} 
	}


