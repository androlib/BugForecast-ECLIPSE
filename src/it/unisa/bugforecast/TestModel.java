package it.unisa.bugforecast;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			// apro il training set in arff
			CSVtoARFF.convert("ant-1.7", "C:\\Users\\aleso\\Desktop\\ant-1.7.csv");
			BufferedReader reader = new BufferedReader(new FileReader("ant-1.7.arff"));
			Instances training = new Instances(reader);

			// apro e converto test set
			CSVtoARFF.convert("MetricClasses", "C:\\Users\\aleso\\Desktop\\MetricClasses.csv");
			BufferedReader reader1 = new BufferedReader(new FileReader("MetricClasses.arff"));

			// seleziono attributo training
			training.setClassIndex(9);
			Instances test = new Instances(reader1);

			// seleziono attributo test
			test.setClassIndex(9);

			// scelgo il classifier e lo buildo con il training
			AbstractClassifier c = new Logistic();
			c.buildClassifier(training);

			// produco e uso modello
			Evaluation eval = new Evaluation(training);
			eval.evaluateModel(c, test);
			System.out.println(eval.toSummaryString());
			System.out.println(eval.toClassDetailsString());
			System.out.println(eval.toMatrixString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
