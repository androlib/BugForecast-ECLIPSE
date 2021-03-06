package it.unisa.bugforecast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class CSVtoARFF {

	public CSVtoARFF() {
		// TODO Auto-generated constructor stub
		
	}
	
	public static boolean convert(String name, String path) {
		CSVLoader csv = new CSVLoader();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String proof = br.readLine();
			if(proof == null) {
				br.close();
				return false;
			}
			else {
				if(proof.indexOf(";") > -1) {
					br.close();
					return false; //controllo se ci sono ; invece di ,
				}
			}
			csv.setSource(new File(path));
			ArffSaver arff = new ArffSaver();
			Instances data = csv.getDataSet();
            arff.setInstances(data);
            File f = new File(name+".arff");
            if(f.exists()) {
            	
                f.delete();
            };
            arff.setFile(new File(name+".arff"));
            arff.writeBatch();
            br.close();
            return true;
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
		
	}

}
