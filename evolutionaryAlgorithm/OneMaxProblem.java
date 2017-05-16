package eaProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class OneMaxProblem extends Problem {
	
	// default values:
	private int noOfStartingGenoTypes = 100;
	private int noOfSymbolsPerDNA = 40;
	private int symbolSetSize = 2;  
	private int bitLengthPerGeno = 1;
	
	private int fitnesLimit = 40; 
										//TODO set on input;
	private String adultSelectionType = "full"; //Full, overp and mixing
	private String matingSelectionType = "fp";  //fp, sigma, tour, boltz  
	private int adultPoolMaxSize  = 240; 
	private int childrenToProduce = 240; 
			 // Has to be even number
	
	private double eParamForTour = 0.2;	//% that is random select 0.2
	private double kParamForTour = 0.5; //% that are included in the tournament. 0.5
	
	private double crossoverRate = 0.8; 
	private double mutationRate = 0.10;

	private GOs go; 
	ToolBox tb = new ToolBox();
	private boolean randomTarget = false;
	private boolean[] randomTagetDNA; 
	
	
	

	public OneMaxProblem(boolean rndTarget){
		input(); 
		this.go = new GOs(noOfSymbolsPerDNA, bitLengthPerGeno); 
		fitnesLimit = 1; 
		randomTarget = rndTarget; 	
		if(rndTarget){
			randomTagetDNA = new boolean[noOfSymbolsPerDNA*bitLengthPerGeno]; 
			for(int i = 0; i< randomTagetDNA.length; i++){
				Random rnd = new Random(); 
				randomTagetDNA[i] = rnd.nextBoolean(); 
			}
		}
	}
	
	
	
	public void assignFitness(PhenoType pt){ 
		pt.fitness = getFitness(pt); 
		
	}
	
	public double getFitness(PhenoType pt){
		
		double count = 0; 
		if(randomTarget){
			for(int i = 0; i < pt.genome.dna.length; i++){
				if(pt.genome.dna[i] == randomTagetDNA[i]){
					count++; 	
				}		
			}
		}
		else{
			for(int i = 0; i < pt.genome.dna.length; i++){
				if(pt.genome.dna[i] == false ){
					count++; 
				}
			}	
		}
		return (1 / (count +1)); 	
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
		System.out.println("Enter settings:");
		 try{
		     BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		     System.out.print("Length(L)("+noOfSymbolsPerDNA+"):"); 
		     String inputString = bufferRead.readLine();
		     if(tb.getDigitNumbers(inputString).size() == 1){
		    	 noOfSymbolsPerDNA = tb.getDigitNumbers(inputString).get(0); 
		     }
		     
		     System.out.print("Adult Selection Type(full, overP, mixing)("+adultSelectionType+"):"); 
		     inputString = bufferRead.readLine();
		     String s = inputString.trim();
		     if(s.length() > 0){
		    	 adultSelectionType = s;  
		     }
		     
		     System.out.print("Mating Selection Type(fp, sigma, tour, boltz)("+matingSelectionType+"):"); 
		     inputString = bufferRead.readLine();
		     s = inputString.trim();
		     if(s.length() > 0){
		    	 matingSelectionType = s;  
		     }
		     
			 System.out.print("AdultPop Max Size("+adultPoolMaxSize+"):"); 
			 inputString = bufferRead.readLine();
			 if(tb.getDigitNumbers(inputString).size() == 1){
			     adultPoolMaxSize = tb.getDigitNumbers(inputString).get(0); 
			 }
		     
			 if(!adultSelectionType.equalsIgnoreCase("full")){
			     System.out.print("Children To Produce:("+childrenToProduce+"):"); 
			     inputString = bufferRead.readLine();
			     if(tb.getDigitNumbers(inputString).size() == 1){
			    	 childrenToProduce = tb.getDigitNumbers(inputString).get(0); 
			     }
			 }
			 else{
				 childrenToProduce = adultPoolMaxSize; 
			 }
		     
		     
		     if(adultSelectionType.equalsIgnoreCase("tour")){
			     System.out.print("Tour params:("+eParamForTour+ " , " + +kParamForTour+"):"); 
			     inputString = bufferRead.readLine();
			     if(tb.getDoubleDigitNumbers(inputString).size() == 2){
			    	 eParamForTour = tb.getDoubleDigitNumbers(inputString).get(0);
			    	 kParamForTour = tb.getDoubleDigitNumbers(inputString).get(1); 
			     }
		     }
		     
		     System.out.print("Reproduction params:(cr:"+crossoverRate+ " , mut:" + +mutationRate+"):"); 
		     inputString = bufferRead.readLine();
		     if(tb.getDoubleDigitNumbers(inputString).size() == 2){
		    	 crossoverRate = tb.getDoubleDigitNumbers(inputString).get(0);
		    	 mutationRate = tb.getDoubleDigitNumbers(inputString).get(1); 
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

	public int getFitnesLimit() {
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
	
	public boolean isRandomTarget() {
		return randomTarget;
	}

	public void setRandomTarget(boolean randomTarget) {
		this.randomTarget = randomTarget;
	}

		

}
