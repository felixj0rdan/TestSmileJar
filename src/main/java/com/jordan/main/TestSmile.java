package com.jordan.main;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.*;

import smile.classification.*;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.io.Read;
import smile.validation.ClassificationMetrics;
import smile.validation.metric.Accuracy;

public class TestSmile implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public RandomForest forest;
	public ClassificationMetrics metrics;
	public double accuracy;
	public int[] testResult;
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		// server object to run Train() and Query()
//		Server server = new Server();
		TestSmile model = new TestSmile();
				
		// to train from the client side
		String irisTrainPath = "src/files/IrisTrain.csv";
		String irisTestPath = "src/files/IrisTest.csv";
//		String filename = "src/files/Forest.txt";
		
		DataFrame irisTrain = Read.csv(irisTrainPath);
	    DataFrame irisTest = Read.csv(irisTestPath);
	    
	    model.trainModel(irisTrain);
	    model.testModel(irisTest);
	    
	    System.out.println(model.metrics);
	    
		
//		model.Train(irisTrainPath, irisTestPath, filename);
		
		// to test from the client side
//		String modelFilename = "src/files/Forest.txt";
//		String csvFile = "src/files/IrisTest.csv";
//		
//		System.out.println("Test Results:");

	}
	
	public void trainModel(DataFrame irisTrain) {
			
		Formula formula = Formula.lhs("V5");
	    
	    System.out.println();
	    
	    Properties prop = new Properties();
	    prop.setProperty("smile.random.forest.trees", "200");
	    RandomForest forest = RandomForest.fit(formula, irisTrain, prop);
	    ClassificationMetrics metrics = forest.metrics();
	    this.forest = forest;
	    this.metrics = metrics;
	    this.accuracy = this.metrics.accuracy*100;
	}
	
	public void testModel(DataFrame irisTest) {
		this.testResult = forest.predict(irisTest);
		for(int j: this.testResult)
	    	System.out.println(j);
		
	}
	
	public int[] testAccuracy(String fileToVerify) throws FileNotFoundException {
		int[] iAccu = this.csvToArr(fileToVerify);
	    this.accuracy = Accuracy.of(testResult, iAccu)*100.0;
	    return iAccu;
	}
	
	public void modelMetrics() {
		System.out.format("OOB error rate = %.2f%%%n", (this.metrics.accuracy));
		System.out.println("Accuracy= "+this.accuracy);   
	}
	
	private int[] csvToArr(String path) throws FileNotFoundException {
		Scanner obj = new Scanner(new BufferedReader(new FileReader(path)));
		int i = 0;
	    int[] iAccu = new int[this.testResult.length];
	    while(obj.hasNextLine()) {
	    	iAccu[i] = Integer.parseInt(obj.nextLine());
	    	i++;
	    }
	    return iAccu;
	}

}
