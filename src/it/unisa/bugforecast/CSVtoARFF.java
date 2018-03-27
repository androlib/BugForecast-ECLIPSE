package it.unisa.bugforecast;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class CSVtoARFF {

	public CSVtoARFF() {
		// TODO Auto-generated constructor stub
		
	}
	
	public static void convert(String name, String path) {
		CSVLoader csv = new CSVLoader();
		try {

			csv.setSource(new File(path));
			ArffSaver arff = new ArffSaver();
			Instances data = csv.getDataSet();
            arff.setInstances(data);
            arff.setFile(new File(name+".arff"));
            arff.writeBatch();
            
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
