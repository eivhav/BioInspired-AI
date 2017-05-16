package eaProject;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class EAmain {
	
	static int generationID = 0; 
	static int noOfGenarations = 30000; 
	static int maxTimeInMin = 3; 
	static double mutationFactor = -1;
	static boolean rndOneMAXDna = false; 
	
	static int noOfStartingGenoTypes; 
	static int fitnesLimit;  
	static int noOfSymbolsPerDNA; 									
	static int bitLengthPerGeno;
	static int symbolsetSize;  
	
	static String adultSelectionType; 
	static String matingSelectionType;  
 	static int adultPoolMaxSize;  
	static int childrenToProduce; 
	
	static double eParamForTour; 
	static double kParamForTour;  
	
	static double crossoverRate; 
	static double mutationRate; 
	
	
	
	
	static ArrayList<GenoType> genoTypes = new ArrayList<GenoType>(); 
	static ArrayList<PhenoType> phenoTypes = new ArrayList<PhenoType>(); 
	static ArrayList<PhenoType> adultPool = new ArrayList<PhenoType>();
	static ArrayList<PhenoType> reproductionCandidates = new ArrayList<PhenoType>();
	
	static double avgAdultPopFitnesValue = 0; 
	static double sd; 
	
	
	
	public static void main(String[] args){
		
		boolean running = true; 
		while(running){
			Problem problem = null; 
			String inputString = ""; 
			System.out.println("Enter problem: (oneMax, Lolz or Supseq) ");
			 try{
			     BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			     inputString = bufferRead.readLine(); 
			 }
			 catch(IOException ex)
			 {
			    ex.printStackTrace();
			}
			  
			String input = inputString.trim(); 
			boolean run = true;  
			
			if(input.equalsIgnoreCase("oneMax")){
				problem = new OneMaxProblem(rndOneMAXDna); 
			}
			else if(input.equalsIgnoreCase("lolz")){
				problem = new LolzProblem(); 
			}
			else if(input.equalsIgnoreCase("supseq")){
				problem = new SupSeqProblem(); 
			}
			
			if(problem != null){
			
				
				noOfStartingGenoTypes = problem.getNoOfStartingGenoTypes();
				noOfSymbolsPerDNA = problem.getNoOfSymbolsPerDNA(); 
				symbolsetSize = problem.getSymbolSetSize();  
				bitLengthPerGeno = problem.getBitLengthPerGeno(); 
				fitnesLimit = problem.getFitnesLimit();  
													
				adultSelectionType = problem.getAdultSelectionType(); 
				matingSelectionType = problem.getMatingSelectionType();  
				adultPoolMaxSize = problem.getAdultPoolMaxSize(); 
				childrenToProduce = problem.getChildrenToProduce();  
				
				eParamForTour = problem.geteParamForTour(); 	
				kParamForTour = problem.getkParamForTour(); 
				
				crossoverRate = problem.getCrossoverRate();  
				mutationRate = problem.getMutationRate(); 
				
				while(run){
					eaAlgorithm(problem);
					System.out.println("Run again? (y) ");
					 try{
					     BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
					     inputString = bufferRead.readLine(); 
					 }
					 catch(IOException ex)
					 {
					    ex.printStackTrace();
					}
					if(!inputString.trim().equalsIgnoreCase("y")){
						run = false; 
					}
			}
		}
		}
		
	}





	private static void eaAlgorithm(Problem problem) {
		
		genoTypes = new ArrayList<GenoType>(); 
		phenoTypes = new ArrayList<PhenoType>(); 
		adultPool = new ArrayList<PhenoType>();
		reproductionCandidates = new ArrayList<PhenoType>();
		double bestFitnes = 0; 
		int count = 0;
		generationID = 0; 
		createRandomGenoTypes();
		System.out.println("initGenoes: " + genoTypes.size());
		long timeStart = (System.currentTimeMillis()); 
		
		JPanel sliderPanel = new JPanel(); 
		GridLayout sliderLayout = new GridLayout(1,0);
		sliderPanel.setLayout(sliderLayout);
	    
	    JSlider genSlider = new JSlider(0,50000);
	    genSlider.setValue(noOfGenarations); 
	    genSlider.setPaintLabels(true);
	    genSlider.setMajorTickSpacing(5000);
	    genSlider.setMinorTickSpacing(1000);
	    sliderPanel.add(genSlider);
	    JFrame sliderFrame = new JFrame(); 
	    sliderFrame.add(sliderPanel); 
	    sliderFrame.setSize(500, 150);
	    sliderFrame.setVisible(true);
		
		
		
		while(bestFitnes < fitnesLimit && count < noOfGenarations){
			count++;
			
			noOfGenarations = genSlider.getValue(); 
			
			createAndTestPhenoTypes(problem);
			if(phenoTypes.get(0).fitness > bestFitnes){
				bestFitnes = phenoTypes.get(0).fitness; 
			}
			
			selectAdults(); 
			selectReproductionCandidates(bestFitnes);
			reproduce(problem); 
			
			if(noOfSymbolsPerDNA < 150){
				System.out.println("Generation:" + generationID + "   best:" +String.format("%.3f",bestFitnes) + "   Avg:" + String.format("%.3f", avgAdultPopFitnesValue) +"   SD:" + String.format("%.3f", sd) + " : " + Arrays.toString(phenoTypes.get(0).symbols));
				 
			}
			else{
				System.out.println("Generation:" + generationID + "   best:" +String.format("%.3f",bestFitnes) + "   Avg:" + String.format("%.3f", avgAdultPopFitnesValue) +"   SD:" + String.format("%.3f", sd)); 
			}
			generationID++; 
			
			if(count == 500){
				long timePer = (System.currentTimeMillis() - timeStart +100) / 500;
				if(timePer != 0){
					noOfGenarations = (int) ((maxTimeInMin*60000) / timePer); 
				}
				genSlider.setValue(noOfGenarations);
				System.out.println("max Generations: is now" + noOfGenarations);
			}
				
			
			
		}
		System.out.println("The winner: Generation:" + (generationID-1) + "   best:" +String.format("%.3f",bestFitnes) + "   Avg:" + String.format("%.3f", avgAdultPopFitnesValue) +"   SD:" + String.format("%.3f", sd) + " : " + Arrays.toString(phenoTypes.get(0).symbols)); 
		
		System.out.println(); 
		System.out.println(); 
		System.out.println("******************************************************************"); 
		for(int i = 0; i< 10; i++){System.out.println();};
		sliderFrame.dispose(); 
	
	}
	
	
	private static void createRandomGenoTypes() {
		genoTypes = new ArrayList<GenoType>(); 
		for(int i = 0; i < noOfStartingGenoTypes; i++){
			boolean[] dna = new boolean[noOfSymbolsPerDNA*bitLengthPerGeno]; 
			for(int j = 0; j < noOfSymbolsPerDNA; j++){
				Random rnd = new Random(); 
				int symbol = rnd.nextInt(symbolsetSize);
			
				String binary = Integer.toString(symbol,2);
				while(binary.length() < bitLengthPerGeno){
					binary = "0" + binary; 
				}
			
				
				for(int s = 0; s < binary.length(); s++){
					if(binary.charAt(s) == '1'){
						dna[(j*bitLengthPerGeno) + s] = true; 
					}	
				}
				
			}
			GenoType genome = new GenoType(dna); 
			genoTypes.add(genome); 		
		}		
	}
		
	
	
	private static void createAndTestPhenoTypes(Problem problem) {
		phenoTypes = new ArrayList<PhenoType>();
		for(int i = 0; i < genoTypes.size(); i++){
			PhenoType pt = new PhenoType(genoTypes.get(i), bitLengthPerGeno, generationID); 
			problem.assignFitness(pt); 
			sortedAdd(phenoTypes, pt); 
			
		}
	}
	
	
	
	private static void selectAdults() {
		ArrayList<PhenoType> newAdults = new ArrayList<PhenoType>(); 
		double totalFitnesValue = 0; 
		
		if(adultSelectionType.equalsIgnoreCase("full")){
			for(int i = 0; i < phenoTypes.size(); i++){
				newAdults.add(phenoTypes.get(i)); 
				totalFitnesValue += phenoTypes.get(i).fitness; 
			}
		}
		
		else if(adultSelectionType.equalsIgnoreCase("overP")){
			for(int i = 0; i < adultPoolMaxSize; i++){
				if(i < phenoTypes.size()){
					newAdults.add(phenoTypes.get(i)); 
					totalFitnesValue += phenoTypes.get(i).fitness; 
				}
			}
		}

		else if(adultSelectionType.equalsIgnoreCase("mixing")){
			int ptlistPos = 0; 
			int oldAdultsListPos = 0;
			 
			for(int i = 0; i < adultPoolMaxSize; i++){
				if(ptlistPos < phenoTypes.size() && oldAdultsListPos < adultPool.size()){
					if(phenoTypes.get(ptlistPos).fitness >= adultPool.get(oldAdultsListPos).fitness){
						newAdults.add(phenoTypes.get(ptlistPos)); 
						totalFitnesValue += phenoTypes.get(ptlistPos).fitness; 
						ptlistPos++; 
					}
					else{
						newAdults.add(adultPool.get(oldAdultsListPos)); 
						totalFitnesValue += adultPool.get(oldAdultsListPos).fitness; 
						oldAdultsListPos++; 
					}
				}
				else if(ptlistPos < phenoTypes.size()){
					newAdults.add(phenoTypes.get(ptlistPos)); 
					totalFitnesValue += phenoTypes.get(ptlistPos).fitness; 
					ptlistPos++; 
				}
				else if(oldAdultsListPos < adultPool.size()){
					newAdults.add(adultPool.get(oldAdultsListPos));
					totalFitnesValue += adultPool.get(oldAdultsListPos).fitness; 
					oldAdultsListPos++;
				}
			}		
		}
		if(newAdults.size() > 0){
			avgAdultPopFitnesValue = totalFitnesValue / newAdults.size(); 
		}
		adultPool = newAdults; 
	}
	
	
	
	private static void selectReproductionCandidates(double bf) {
		reproductionCandidates = new ArrayList<PhenoType>();
		double totalSigma = 0; 
		double totalBoltz = 0; 
		double T = 200 * (1- ((double) generationID / (double) noOfGenarations)); 
		
		sd = 0; 
		for(int p = 0; p < adultPool.size(); p++){ 
			sd += (adultPool.get(p).fitness - avgAdultPopFitnesValue) * (adultPool.get(p).fitness - avgAdultPopFitnesValue);   
		}
		sd = Math.sqrt(sd);
		
		if(matingSelectionType.equalsIgnoreCase("sigma")){
			for(int p = 0; p < adultPool.size(); p++){ 
				totalSigma += 1 + ((adultPool.get(p).fitness - avgAdultPopFitnesValue) / (2*sd));   
			}
		}
		
		else if(matingSelectionType.equalsIgnoreCase("tour")){ 
			for(int i = 0; i < (childrenToProduce); i++){
				ArrayList<PhenoType> candiates = new ArrayList<PhenoType>();
				for(int k = 0; k < ((kParamForTour)*childrenToProduce);  k++){
					Random rnd = new Random(); 
					int n = rnd.nextInt(adultPool.size());
					sortedAdd(candiates, adultPool.get(n)); 
				}
				Random rnd2 = new Random(); 
				int n = rnd2.nextInt(100);
				if(n > (eParamForTour*100) && candiates.size() > 1){
					reproductionCandidates.add(candiates.get(0));
					reproductionCandidates.add(candiates.get(1)); 
				}
				else{
					Random rnd3 = new Random();
					Random rnd4 = new Random();
					int n3 = rnd3.nextInt(adultPool.size());
					int n4 = rnd4.nextInt(adultPool.size());
					reproductionCandidates.add(adultPool.get(n3));
					reproductionCandidates.add(adultPool.get(n4));
				}
				i++; 
			}
			return; 
		}
		
		else if(matingSelectionType.equalsIgnoreCase("boltz")){	
			for(int p = 0; p < adultPool.size(); p++){ 
				totalBoltz += Math.exp(adultPool.get(p).fitness/T);   
			}
		}
			
		
		
				
		for(int i = 0; i < childrenToProduce; i++){
			Random rnd = new Random(); 
			int n = rnd.nextInt(10000); 
			double nValue = ((double) n) / 10000; 
			double count = 0; 
			for(int p = 0; p < adultPool.size(); p++){ 
				if(count >= nValue || p == adultPool.size()-1){ 
					reproductionCandidates.add(adultPool.get(p));
					break; 
				}
				else{
					if(matingSelectionType.equalsIgnoreCase("fp")){
						count += (adultPool.get(p).fitness / (avgAdultPopFitnesValue*adultPool.size())); 
					}
					else if(matingSelectionType.equalsIgnoreCase("sigma")){
						count += (1 + ((adultPool.get(p).fitness - avgAdultPopFitnesValue) / (2*sd))) / (totalSigma);
					}
					else if(matingSelectionType.equalsIgnoreCase("boltz")){	
						count += (Math.exp(adultPool.get(p).fitness/T) / totalBoltz);  
					}
				}	
			}
		}
	
	}

	

	private static void reproduce(Problem problem) {
		ArrayList<GenoType> children = new ArrayList<GenoType>(); 
		for(int i = 0; i < (reproductionCandidates.size())-1 ; i++){
				Random rnd = new Random(); 
				int cr = rnd.nextInt(100); 
				if(cr <= (crossoverRate*100)){
					ArrayList<GenoType> childs = problem.crossover1(reproductionCandidates.get(i), reproductionCandidates.get(i+1)); 
					children.add(childs.get(0)); 
					children.add(childs.get(1));  
				}
				else if(cr <= (crossoverRate*100) + (mutationRate*100)){
					GenoType child1 = problem.mutate(reproductionCandidates.get(i), mutationFactor);  
					children.add(child1);	
					GenoType child2 = problem.mutate(reproductionCandidates.get(i+1), mutationFactor);  
					children.add(child2);
				}
				else{
					GenoType child1 = problem.copy(reproductionCandidates.get(i));  
					children.add(child1);
					GenoType child2 = problem.copy(reproductionCandidates.get(i+1));  
					children.add(child2);
				}
				i++; 
		}
		genoTypes = children; 
	}
	
	




	
	private static void sortedAdd(ArrayList<PhenoType> list, PhenoType pt){
		boolean added = false; 
		for(int j = 0; j < list.size(); j++){
			if(pt.fitness > list.get(j).fitness){
				list.add(j, pt); 
				added = true; 
				break; 
			}
		}
		if(!added){
			list.add(pt); 
		}
		
	}


	





	



	
	
	
	
	
	

}
