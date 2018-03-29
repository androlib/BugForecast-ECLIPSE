package it.unisa.bugforecast;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CreateCSV {

	public CreateCSV(String path, ArrayList<MetricClass> array) {
		// TODO Auto-generated constructor stub
		try {
			fileWriter = new FileWriter(path);
			list = array;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void execute() {
		try {
			fileWriter.append(FILE_HEADER);
			for(MetricClass mc : list) {
				fileWriter.append(NEW_LINE_SEPARATOR);
				fileWriter.append(mc.getName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+mc.getOne());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+mc.getTwo());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+mc.getThree());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+mc.getFour());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+mc.getFive());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+mc.getSix());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+mc.getSeven());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+mc.getEight());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+mc.getBug());
				
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private FileWriter fileWriter;
	private ArrayList<MetricClass> list;
	private String COMMA_DELIMITER = ",";
	private String NEW_LINE_SEPARATOR = "\n";
	private String FILE_HEADER = "name,wmc,dit,noc,cbo,rfc,lcom,ca,npm,bug";
}
