package eaProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SupSeqProblem extends Problem {
	
	//Optimal LocalSS: 300, 300, 300, mixing, sigma, 0.50, 0,20, -1
	//Best LocalSS: 300, 10, 40, overp, tour, 0.25, 0,50, -1
	//BestBest LocalSS: 300, 15, 50, overp, tour, 0.25, 0,60, -1 eT=0.3, kT=0.5
		
	//Best GlobalSS: 300, 10, 40, overp, tour, 0.25, 0,50, -1
	//std values SS: 300, 15, 50, eT=0,5, eK=05, 0,25, 0.6, -1 
	
	
	
	
	// default value: 
	private int noOfStartingGenoTypes = 300;
	private int noOfSymbolsPerDNA = 100;
	private int symbolSetSize = 10;  
	private int bitLengthPerGeno = 0;
	
	private int fitnesLimit = 0; 
										//TODO set on input;
	private String adultSelectionType = "overp"; //Full, overp and mixing
	private String matingSelectionType = "tour";  //fp, sigma, tour, boltz  
	private int adultPoolMaxSize = 15; 
	private int childrenToProduce = 50; // Has to be even number
	
	private double eParamForTour = 0.5;	//% that is random select 0.2
	private double kParamForTour = 0.5; //% that are included in the tournament. 0.5
	
	private double crossoverRate = 0.25; 
	private double mutationRate = 0.6;

	private GOs go; 
	ToolBox tb = new ToolBox();
	
	private String type = "not set"; 

	
	
	
	public SupSeqProblem(){
		input(); 
		for(int gsl = 1; gsl < 20; gsl++){
			if(Math.pow(gsl, 2) >= symbolSetSize){
				bitLengthPerGeno = gsl; 
				break; 
			}
		}
		this.go = new GOs(noOfSymbolsPerDNA, bitLengthPerGeno);
		fitnesLimit = 1; 
	}
	
	
	
	
	
	
	public void assignFitness(PhenoType pt){ 
		pt.fitness = getFitness(pt); 
		
	}
	
	public double getFitness(PhenoType pt){
		int[] sequence = pt.symbols; 
		for(int i = 0; i < sequence.length; i++){
			if(sequence[i] >= symbolSetSize){
				return 0; 
			}
		}
		if(type.equalsIgnoreCase("local")){
			double error = 0;
			boolean [][] table = new boolean[symbolSetSize][symbolSetSize]; 
			for(int i =0; i< sequence.length-1; i++){		
				int[] syms = {sequence[i], sequence[i+1]};
				if(table[syms[0]][syms[1]] == true){ 
					error++; 
				}
				else{
					table[syms[0]][syms[1]] = true; 
				}		
			}
			if(noOfSymbolsPerDNA - error > 0){
				return (1 / (error +1)); 
				//return noOfSymbolsPerDNA - error;
			}	
		}
		else if(type.equalsIgnoreCase("global")){
			boolean [][][] distanceTable = new boolean[symbolSetSize][symbolSetSize][noOfSymbolsPerDNA];
			double error = 0;
			for(int i =0; i< sequence.length-1; i++){	
				for(int j = i+1; j < sequence.length; j++){ 
					int[] syms = {sequence[i], sequence[j]};
					int dist = j-i; 
					if(distanceTable[syms[0]][syms[1]][dist] == true){ 
						error++; 
					}
					else{
						distanceTable[syms[0]][syms[1]][dist] = true; 
					}		
				}
			}
			if(noOfSymbolsPerDNA - error > 0){
				return (1 / (error +1)); 
				//return noOfSymbolsPerDNA - error;
			}	
		}	
		return 0; 
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
			 System.out.print("Type:("+type+"):"); 
			 String inputString = bufferRead.readLine();
			 String t = inputString.trim();
			 if(t.length() > 0){
			    	 type = t;  
			 }
			 
		     System.out.print("Length(L)("+noOfSymbolsPerDNA+"):"); 
		     inputString = bufferRead.readLine();
		     if(tb.getDigitNumbers(inputString).size() == 1){
		    	 noOfSymbolsPerDNA = tb.getDigitNumbers(inputString).get(0); 
		     }
		     
		     System.out.print("SymbolsetSize(S)("+symbolSetSize+"):"); 
		     inputString = bufferRead.readLine();
		     if(tb.getDigitNumbers(inputString).size() == 1){
		    	 symbolSetSize = tb.getDigitNumbers(inputString).get(0); 
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
		     
		     
		     if(matingSelectionType.equalsIgnoreCase("tour")){
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
}