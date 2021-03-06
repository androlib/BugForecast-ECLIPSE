package it.unisa.bugforecast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
aleso
*/
public class ReaderCSV {

	public ReaderCSV(FileReader f) {
		// TODO Auto-generated constructor stub
		br = new BufferedReader(f);
		list = new ArrayList<MetricClass>();
	}
	
	public ArrayList<MetricClass> execute() {
		String line = null;
		
		try {
			br.readLine();
			while((line = br.readLine()) != null) {
			     String[] metrics = line.split(";");
			     String name = metrics[0];
			     int one = Integer.parseInt(metrics[1]);
			     int two = Integer.parseInt(metrics[2]);
			     int three = Integer.parseInt(metrics[3]);
			     int four = Integer.parseInt(metrics[4]);
			     int five = Integer.parseInt(metrics[5]);
			     int six = Integer.parseInt(metrics[6]);
			     int seven = Integer.parseInt(metrics[7]);
			     int eight = Integer.parseInt(metrics[8]);
			     boolean flag = Boolean.parseBoolean(metrics[9]);
			     
			     
			     list.add(new MetricClass(name,one,two,three,four,five,six,seven,eight,flag));
			  
			}
			
			return list;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public BufferedReader getBr() {
		return br;
	}
	public void setBr(BufferedReader br) {
		this.br = br;
	}

	public ArrayList<MetricClass> getList() {
		return list;
	}

	public void setList(ArrayList<MetricClass> list) {
		this.list = list;
	}

	private BufferedReader br;
	private ArrayList<MetricClass> list;
}
