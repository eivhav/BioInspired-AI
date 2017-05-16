package flatlandv1;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class FlatlandProblem extends Problem {
	
	private int noOfStartingGenoTypes = 500;
	private int noOfSymbolsPerDNA = 90; //135 //hiddennodes x 9
	private int symbolSetSize = 64;  
	private int bitLengthPerGeno = 6;
	
	private double fitnesLimit = 0.20; 
										//TODO set on input;
	private String adultSelectionType = "overp"; //Full, overp and mixing
	private String matingSelectionType = "sigma";  //fp, sigma, tour, boltz  
	private int adultPoolMaxSize = 20; 
	private int childrenToProduce = 200; // Has to be even number
	
	private double eParamForTour = 0.2;	//% that is random select 0.2
	private double kParamForTour = 0.4; //% that are included in the tournament. 0.5
	
	private double crossoverRate = 0.45; //0.45 High CCrate for static (0,45)
	private double mutationRate = 0.15; //0.15

	private GOs go; 
	ToolBox tb = new ToolBox();
	
	private int[] hiddenTopologi = {10}; 
	private int noOfinputNodes = 6; 
	private int noOfOutputNodes = 3; 
	
	private String type = "dynamic"; 
	private int noOfScenarios = 5; 
	private int lastGenerationID = 0; 
	ArrayList<FlatlandGame> flGames = new ArrayList<FlatlandGame>(); 
	EANN agent;  
	
	
	
	
	public FlatlandProblem(){
		//input(); 
		
		for(int i = 0; i < noOfScenarios; i++){
			FlatlandGame flGame = new FlatlandGame(); 
			flGame.createRandomBoard(); 
			flGames.add(flGame); 
		}
	
		agent = new EANN(hiddenTopologi, noOfinputNodes, noOfOutputNodes, false, (symbolSetSize /2)); 
		this.go = new GOs(noOfSymbolsPerDNA, bitLengthPerGeno);  
	}
	
	
	
	
	public void assignFitness(ArrayList<PhenoType> pts){
		pts.get(0).fitness = getFitness(pts.get(0)); 
		
	}
	
	public double getFitness(PhenoType pt){
		if(type.equals("dynamic") && lastGenerationID != pt.generationID){
			lastGenerationID = pt.generationID;
			for(int i = 0; i < noOfScenarios; i++){
				flGames.get(i).createRandomBoard(); 
			}
		}
			
		agent.updateWeights(pt.getSymbols()); 
		double error = 0; 
		for(int i = 0; i < noOfScenarios; i++){
			flGames.get(i).reGenerateBoard();
			double score = flGames.get(i).tryAgent(agent, 60); 
			error += (33 - score); 
		} 
		
		error = error / noOfScenarios; 
		
		return (1 / (1 + error));  
	
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
	
	
	
	
	
	private void input(){
		
		
		
	}
	
	public void simulatePhenoType(PhenoType pt, boolean rnd){
		agent.updateWeights(pt.getSymbols()); 
		if(type.equals("dynamic") || rnd){
			for(int i = 0; i < noOfScenarios; i++){
				flGames.get(i).createRandomBoard(); 
			}
		}
		
		for(int i = 0; i < noOfScenarios; i++){
			flGames.get(i).reGenerateBoard();
			double score = flGames.get(i).tryAgentSimulation(agent, 60); 
			System.out.println("Scenarion:"+i +" -->"+score); 
		} 
		
		
		
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
	
	
	public void setFitnesLimit(double fitnesLimit) {
		this.fitnesLimit = fitnesLimit;
}	

	

	

}
