package it.unisa.bugforecast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import gr.spinellis.ckjm.MetricsFilter;
import gr.spinellis.ckjm.PrintPlainResults;
import gr.spinellis.ckjm.ant.CkjmTask;

public class CreateTask {

	public CreateTask() {
		// TODO Auto-generated constructor stub
	}
	
	public CreateTask(File input, File output) {
		fInput = input;
		fOutput = output;
		nameFiles = new ArrayList<String>();
		c = new CkjmTask();
		c.setFormat("txt");
		c.setClassdir(fInput);
		c.setOutputfile(fOutput);
	}
	
	public void startTask() {
		lista(fInput);
		files = nameFiles.toArray(new String[nameFiles.size()]);
		
		try {
			OutputStream outputStream = new FileOutputStream(fOutput);
			PrintPlainResults outputPlain = new PrintPlainResults(new PrintStream(outputStream));
			MetricsFilter.runMetrics(files, outputPlain);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void lista (File partenza) { 
		File[] list=partenza.listFiles(); 
		int MAX=list.length;
		for (int i = 0; i<MAX; i++){
			if (list[i].isDirectory()) 
				lista( list[i] ); 
			else {
				String nome = list[i].toString();
				nameFiles.add(nome);
			}
		}
	}

	public File getfInput() {
		return fInput;
	}
	public void setfInput(File fInput) {
		this.fInput = fInput;
	}

	public File getfOutput() {
		return fOutput;
	}

	public void setfOutput(File fOutput) {
		this.fOutput = fOutput;
	}
	
	
	private CkjmTask c;
	private File fInput;
	private File fOutput;
	private static ArrayList<String> nameFiles;
	private String[] files;
}
