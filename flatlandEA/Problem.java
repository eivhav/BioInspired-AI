package flatlandv1;

import java.util.ArrayList;
import java.util.Random;

public class Problem {
	
	
	private int noOfStartingGenoTypes; 
	private int noOfSymbolsPerDNA; 
	private int symbolSetSize;  
	private int bitLengthPerGeno; 
	
	private double fitnesLimit;  


	//TODO set on input;
	private String adultSelectionType = "overp"; //Full, overp and mixing
	private String matingSelectionType = "tour";  //fp, sigma, tour, boltz  
	private int adultPoolMaxSize; 
	private int childrenToProduce;  // Has to be even number
	
	private double eParamForTour; 	//% that is random select 0.2
	private double kParamForTour;  //% that are included in the tournament. 0.5
	
	private double crossoverRate; 
	private double mutationRate;  
	private boolean randomTarget; 
	

	
	
	public void assignFitness(ArrayList<PhenoType> pts){ 
		System.out.println("Problem superClass reached"); 	
	}
	
	public GenoType crossover(PhenoType pt1, PhenoType pt2){
		System.out.println("Problem superClass reached"); 
		return null; 
	}
	
	public double getFitness(PhenoType pt){
		System.out.println("Problem superClass reached"); 
		return 0; 	
	}
	
	public void setFitnesLimit(double fitnesLimit) {
this.fitnesLimit = fitnesLimit;
}
	
	public ArrayList<GenoType> crossover1(PhenoType pt1, PhenoType pt2){
		System.out.println("Problem superClass reached"); 
	
		return null; 
	
	}
	
	public GenoType mutate(PhenoType pt1, double mutationFactor){
		System.out.println("Problem superClass reached"); 

		return null;  
	}
	
	
	
	public GenoType copy(PhenoType pt1){
		System.out.println("Problem superClass reached"); 

		return null;
	}

	public int getSymbolSetSize() {
		System.out.println("Problem superClass reached"); 
		return symbolSetSize;
	}

	public void setSymbolSetSize(int symbolSetSize) {
		System.out.println("Problem superClass reached"); 
		this.symbolSetSize = symbolSetSize;
	}
	
	public int getNoOfSymbolsPerDNA() {
		return noOfSymbolsPerDNA;
	}

	public void setNoOfSymbolsPerDNA(int noOfSymbolsPerDNA) {
		System.out.println("Problem superClass reached"); 
		this.noOfSymbolsPerDNA = noOfSymbolsPerDNA;
	}



	public int getNoOfStartingGenoTypes() {
		System.out.println("Problem superClass reached"); 
		return noOfStartingGenoTypes;
	}

	public void setNoOfStartingGenoTypes(int noOfStartingGenoTypes) {
		System.out.println("Problem superClass reached"); 
		this.noOfStartingGenoTypes = noOfStartingGenoTypes;
	}

	public int getBitLengthPerGeno() {
		System.out.println("Problem superClass reached"); 
		return bitLengthPerGeno;
	}

	public void setBitLengthPerGeno(int bitLengthPerGeno) {
		System.out.println("Problem superClass reached"); 
		this.bitLengthPerGeno = bitLengthPerGeno;
	}
	
	public void simulatePhenoType(PhenoType pt, boolean rnd){
		System.out.println("Problem superClass reached");
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
	
	public int getZ() {
		return 0;
	}

	public void setZ(int z) {
		
	}
	
	public boolean isRandomTarget() {
		return randomTarget;
	}

	public void setRandomTarget(boolean randomTarget) {
		this.randomTarget = randomTarget;
	}

}
