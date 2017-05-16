package beerTracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import flatlandv1.GenoType; 
import flatlandv1.PhenoType;
import flatlandv1.GOs; 
import flatlandv1.Problem;
import flatlandv1.ToolBox;



public class BeerTrackerProblem extends Problem {
	
	private int noOfThreads = 1; 
	private int task = 3; 
	
	private int noOfStartingGenoTypes = 500;
	private int noOfSymbolsPerDNA = 0; 
	private int symbolSetSize = 128;  
	private int bitLengthPerGeno = 7;
	
	private double fitnesLimit = 600000; 
										//TODO set on input;
	private String adultSelectionType = "overp"; //Full, overp and mixing
	private String matingSelectionType = "sigma";  //fp, sigma, tour, boltz  
	private int adultPoolMaxSize = 10; 
	private int childrenToProduce = 50; // Has to be even number
	
	private double eParamForTour = 0.2;	//% that is random select 0.2
	private double kParamForTour = 0.4; //% that are included in the tournament. 0.5
	
	private double crossoverRate = 0.50; //0.45 High CCrate for static (0,45)
	private double mutationRate = 0.2; //0.15

	private GOs go; 
	ToolBox tb = new ToolBox();
	
	private int[] hiddenTopologi = {2}; 
	private int noOfinputNodes = 5; 
	private int noOfOutputNodes = 2;
	
	private ArrayList<BeerTrackerGame> games = new ArrayList<BeerTrackerGame>();
	private int[][] objectSeqeunce = new int[250][2]; 
	
	
	
	public BeerTrackerProblem(){
		
		input(); 
		
		
		if(task > 1){
			noOfOutputNodes = 3; 
		}
		if(task > 2){
			noOfinputNodes = 7; 
			noOfOutputNodes = 2;
		}
		
		for(int layer = 0; layer < hiddenTopologi.length; layer++){
			if(layer == 0){
				noOfSymbolsPerDNA += noOfinputNodes * hiddenTopologi[0]; 
			}
			else{
				noOfSymbolsPerDNA += hiddenTopologi[layer] * hiddenTopologi[layer-1]; 
			}
			noOfSymbolsPerDNA += (3 * hiddenTopologi[layer]);
			noOfSymbolsPerDNA += (hiddenTopologi[layer] * hiddenTopologi[layer]); 
		}
		noOfSymbolsPerDNA += noOfOutputNodes * hiddenTopologi[0];
		noOfSymbolsPerDNA += (3 * noOfOutputNodes);
		noOfSymbolsPerDNA += (noOfOutputNodes * noOfOutputNodes);
		
		for(int i = 0; i < 250; i++){
			Random rnd = new Random(); 
			objectSeqeunce[i][0] = rnd.nextInt(30); 
			Random rnd2 = new Random();
			objectSeqeunce[i][1] = rnd2.nextInt(6) +1;
		}
		for(int i = 0; i < noOfThreads; i++){
			games.add(new BeerTrackerGame(noOfinputNodes, noOfOutputNodes, hiddenTopologi, symbolSetSize, task, objectSeqeunce));
			//games.get(i).start(); 
		}
		this.go = new GOs(noOfSymbolsPerDNA, bitLengthPerGeno);
		
		
	
	}
	
	
	
	public void assignFitness(ArrayList<PhenoType> pts){
		
		pts.get(0).fitness = getFitnes(pts); 
		
	
		
	}
	
	public boolean allCompleted(){
		for(int i = 0; i< noOfThreads; i++){
			if(games.get(i).threadCompleted== false){
				return false; 
			}
		}
		return true; 
	}
	
	public double getFitnes(ArrayList<PhenoType> pts){
		double error = 0; 
		double score[] = new double[2]; 
		for(int i = 0; i < 2; i++){
			score[i] = games.get(games.size()-1).runAgent(false, pts.get(0));
		}
		if(score[0] < score[1]){
			return score[0]; 
		}
		else{
			return score[1];
		}
		
	}
	
	
	public ArrayList<GenoType> crossover1(PhenoType pt1, PhenoType pt2){
		return go.crossover1(pt1, pt2); 
	}
	
	
	public GenoType mutate(PhenoType pt1, double mutationFactor){
		return go.mutate(pt1, mutationFactor);
		
	}
	
	
	public GenoType copy(PhenoType pt1){
		return go.copy(pt1); 
		
	}
	
	
	public void simulatePhenoType(PhenoType pts, boolean rnd){
		
		
		//int[] sym = {118, 127, 23, 47, 71, 30, 29, 31, 127, 31, 126, 126, 106, 57, 41, 111, 33, 71, 10, 13, 94, 110, 31, 119, 87, 69, 71, 123, 87, 99, 7, 9, 115, 15}; 
		//pts.setSymbols(sym); 
		double score = games.get(games.size()-1).runAgent(true, pts);
		
		
		
	}
	

	
	
	
	
	
	
	
	
	
	
	private void input(){
		System.out.println("Beertracker: Which task:");
		 try{
		     BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in)); 
		     String inputString = bufferRead.readLine();
		     if(tb.getDigitNumbers(inputString).size() == 1){
		    	 task = tb.getDigitNumbers(inputString).get(0); 
		     }
		     
		 }
		 catch(IOException ex)
		 {
		    ex.printStackTrace();
		}
		 System.out.println(); 	
		 System.out.println("----------------------------------");
		 System.out.println();
	}
		     
		     
	
	
	
	
	
	
	public int getSymbolSetSize() {
		return symbolSetSize;
	}

	public void setSymbolSetSize(int symbolSetSize) {
		this.symbolSetSize = symbolSetSize;
	}
	
	public int getNoOfSymbolsPerDNA() {
		return noOfSymbolsPerDNA;
	}

	public void setNoOfSymbolsPerDNA(int noOfSymbolsPerDNA) {
		this.noOfSymbolsPerDNA = noOfSymbolsPerDNA;
	}

	public int getBitLengthPerGeno() {
		return bitLengthPerGeno;
	}

	public void setBitLengthPerGeno(int bitLengthPerGeno) {
		this.bitLengthPerGeno = bitLengthPerGeno;
	}

	public int getNoOfStartingGenoTypes() {
		return noOfStartingGenoTypes;
	}

	public void setNoOfStartingGenoTypes(int noOfStartingGenoTypes) {
		this.noOfStartingGenoTypes = noOfStartingGenoTypes;
	}

	public double getFitnesLimit() {
		return fitnesLimit;
	}

	public void setFitnesLimit(int fitnesLimit) {
		this.fitnesLimit = fitnesLimit;
	}

	public String getAdultSelectionType() {
		return adultSelectionType;
	}

	public void setAdultSelectionType(String adultSelectionType) {
		this.adultSelectionType = adultSelectionType;
	}

	public String getMatingSelectionType() {
		return matingSelectionType;
	}

	public void setMatingSelectionType(String matingSelectionType) {
		this.matingSelectionType = matingSelectionType;
	}

	public int getAdultPoolMaxSize() {
		return adultPoolMaxSize;
	}

	public void setAdultPoolMaxSize(int adultPoolMaxSize) {
		this.adultPoolMaxSize = adultPoolMaxSize;
	}

	public int getChildrenToProduce() {
		return childrenToProduce;
	}

	public void setChildrenToProduce(int childrenToProduce) {
		this.childrenToProduce = childrenToProduce;
	}
	
	public double geteParamForTour() {
		return eParamForTour;
	}

	public void seteParamForTour(double eParamForTour) {
		this.eParamForTour = eParamForTour;
	}

	public double getkParamForTour() {
		return kParamForTour;
	}

	public void setkParamForTour(double kParamForTour) {
		this.kParamForTour = kParamForTour;
	}

	public double getCrossoverRate() {
		return crossoverRate;
	}

	public void setCrossoverRate(double crossoverRate) {
		this.crossoverRate = crossoverRate;
	}

	public double getMutationRate() {
		return mutationRate;
	}

	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}
	
	
	
	
	

}
