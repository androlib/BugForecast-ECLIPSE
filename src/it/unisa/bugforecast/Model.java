package it.unisa.bugforecast;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.RBFNetwork;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.Stacking;
import weka.classifiers.meta.Vote;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

/**
aleso
*/
public class Model {

	public Model(String classifierName, Instances pTrainingSet, Instances pTestSet) {
		super();
		this.pTrainingSet = pTrainingSet;
		this.pTestSet = pTestSet;
		predictions = new ArrayList<String>();
		
		switch (classifierName) {
		case "Log":
			setClassifier(new Logistic());
			break;
		case "Voting":
			setClassifier(new Vote());
			break;
		case "Bagging":
			setClassifier(new Bagging());
			break;
		case "Boostring":
			setClassifier(new AdaBoostM1());
			break;
		case "Random Forest":
			setClassifier(new RandomForest());
			break;
		case "CODEP":
			setClassifier(new Stacking());
			break;
		case "C45":
			setClassifier(new J48());
			break;
		case "Decision Table":
			setClassifier(new DecisionTable());
			break;
		case "MLP": //non funziona, va in loop
			setClassifier(new MultilayerPerceptron());
			
			break;
		case "RBF": 
			setClassifier(new RBFNetwork());
			break;
		case "NB":
			setClassifier(new NaiveBayes());
			break;
		case "ASCI": //non funziona
			//weka.core.UnsupportedAttributeTypeException:
			//weka.classifiers.rules.ZeroR: Cannot handle unary class!
			setClassifier(new ASCI());
			
			break;
		default:
			System.err.println("Unknown classifier.");
		}
	}
	
	public Evaluation buildAndEvaluate() {
		pTrainingSet.setClassIndex(pTrainingSet.numAttributes() - 1);
		pTestSet.setClassIndex(pTrainingSet.numAttributes() - 1);
		
		try {
			getClassifier().buildClassifier(pTrainingSet);
			Evaluation eval = new Evaluation(pTrainingSet);
			
			eval.evaluateModel(getClassifier(), pTestSet);
			
			for (int i = 0; i < pTestSet.numInstances(); i++) {
				   double pred = classifier.classifyInstance(pTestSet.instance(i));
				   System.out.print("ID: " + pTestSet.instance(i).value(0));
				   System.out.print(", actual: " + pTestSet.classAttribute().value((int) pTestSet.instance(i).classValue()));
				   System.out.println(", predicted: " + pTestSet.classAttribute().value((int) pred));
				   predictions.add(pTestSet.classAttribute().value((int) pred));
				 
				 }
			
			return eval;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		
	}
	
	public void generateFilePredictions(String path) {
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(path+"\\results.csv");
			fileWriter.append(FILE_HEADER);
			for (int i = 0; i < pTestSet.numInstances(); i++) {
				double pred = classifier.classifyInstance(pTestSet.instance(i));
				fileWriter.append(NEW_LINE_SEPARATOR);
				fileWriter.append(pTestSet.instance(i).stringValue(0));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+ (int) pTestSet.instance(i).value(1));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+ (int) pTestSet.instance(i).value(2));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+ (int) pTestSet.instance(i).value(3));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+ (int) pTestSet.instance(i).value(4));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+ (int) pTestSet.instance(i).value(5));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+ (int) pTestSet.instance(i).value(6));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+ (int) pTestSet.instance(i).value(7));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+ (int) pTestSet.instance(i).value(8));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(""+ pTestSet.classAttribute().value((int) pred));
				
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


	public AbstractClassifier getClassifier() {
		return classifier;
	}
	public void setClassifier(AbstractClassifier classifier) {
		this.classifier = classifier;
	}

	public ArrayList<String> getPredictions() {
		return predictions;
	}

	public void setPredictions(ArrayList<String> predictions) {
		this.predictions = predictions;
	}

	
	public Instances getpTrainingSet() {
		return pTrainingSet;
	}

	public void setpTrainingSet(Instances pTrainingSet) {
		this.pTrainingSet = pTrainingSet;
	}

	public Instances getpTestSet() {
		return pTestSet;
	}

	public void setpTestSet(Instances pTestSet) {
		this.pTestSet = pTestSet;
	}

	private AbstractClassifier classifier;
	private ArrayList<String> predictions;
	private Instances pTrainingSet;
	private Instances pTestSet;
	private static String COMMA_DELIMITER = ";";
	private static String NEW_LINE_SEPARATOR = "\n";
	private static String FILE_HEADER = "name;wmc;dit;noc;cbo;rfc;lcom;ca;npm;bug";
	
}
