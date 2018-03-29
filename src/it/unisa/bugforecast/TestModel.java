package it.unisa.bugforecast;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			// apro il training set in arff
			CSVtoARFF.convert("ant-1.7", "ant-1.7.csv");
			BufferedReader reader = new BufferedReader(new FileReader("ant-1.7.arff"));
			Instances training = new Instances(reader);

			// apro e converto test set
			CSVtoARFF.convert("MetricClasses", "MetricClasses.csv");
			BufferedReader reader1 = new BufferedReader(new FileReader("MetricClasses.arff"));
			
			Instances test = new Instances(reader1);
			
			Evaluation eval = Model.buildAndEvaluate("Log", "optionsString", training, test); //non so come passare opzioni
			//System.out.println(eval.toSummaryString());
			//System.out.println(eval.toClassDetailsString());
			
			System.out.println(eval.toMatrixString());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
