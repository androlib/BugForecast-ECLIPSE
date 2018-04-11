package it.unisa.bugforecast.test;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import it.unisa.bugforecast.CreateCSV;
import it.unisa.bugforecast.MetricClass;
import it.unisa.bugforecast.ReaderTxt;


public class TestCSV {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ReaderTxt rt;
		CreateCSV csv;
		try {
			rt = new ReaderTxt(new FileReader("file.txt"));
			ArrayList<MetricClass> list = rt.execute();
			csv = new CreateCSV("MetricClasses.csv", list);
			csv.execute();
			} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} 
		} 
	}


