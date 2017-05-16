package flatlandv1;

import java.util.ArrayList;
import java.util.Random;

public class EANN {
	
	ArrayList<double[][]> weights = new ArrayList<double[][]>(); 
	int[] hiddenTopolgi;  
	int inputNodesCount; 
	int outputNodesCount; 
	
	int[] weightSizes; 
	int weightRange; 
	
	String actFunc = "sigmoid"; 
	double stepValue = 0.3; 
	double[] rampIntervall = {0.2, 0.5}; 
	
	
	
	public EANN(int[] hiddenTopolgi, int inputNodesCount, int outputNodesCount, boolean biasNodes, int weightRange){
		this.hiddenTopolgi = hiddenTopolgi; 
		this.inputNodesCount = inputNodesCount; 
		this.outputNodesCount = outputNodesCount; 
		this.weightRange = weightRange;
		
		if(!biasNodes){
		
			double[][] inputWeightsLayer = new double[inputNodesCount][hiddenTopolgi[0]]; 
			for(int upp = 0; upp < inputNodesCount; upp++){
				for(int low = 0; low < hiddenTopolgi[0]; low++){  
					Random rnd = new Random(); 
					inputWeightsLayer[upp][low] = rnd.nextInt(weightRange*2) - weightRange; 
				}	
			}
			weights.add(inputWeightsLayer); 
			
			for(int layer = 1; layer < hiddenTopolgi.length; layer++){
				double[][] weightsLayer = new double[hiddenTopolgi[layer-1]][hiddenTopolgi[layer]]; 
				for(int upp = 0; upp < hiddenTopolgi[layer-1]; upp++){
					for(int low = 0; low < hiddenTopolgi[layer]; low++){  
						Random rnd = new Random(); 
						weightsLayer[upp][low] = rnd.nextInt(weightRange*2) - weightRange; 
					}	
				}
				weights.add(weightsLayer); 	
				
			}
			
			double[][] outputWeightsLayer = new double[hiddenTopolgi[hiddenTopolgi.length-1]][outputNodesCount]; 
			for(int upp = 0; upp < hiddenTopolgi.length-1; upp++){
				for(int low = 0; low <outputNodesCount; low++){  
					Random rnd = new Random(); 
					outputWeightsLayer[upp][low] = rnd.nextInt(weightRange*2) - weightRange; 
				}	
			}
			weights.add(outputWeightsLayer);
			
			}
		
		weightSizes = new int[weights.size()];
		weightSizes[0] = weights.get(0).length * weights.get(0)[0].length; 
		for(int i = 1; i < weightSizes.length; i++){
			weightSizes[i] = weightSizes[i-1] + (weights.get(0).length * weights.get(0)[0].length); 	
		}
		System.out.println("weighSizes: " +  weightSizes[0] + "," + weightSizes[1]); 
		
	}
	
	public void updateWeights(int[] phenotype){
		
		for(int i = 0; i < phenotype.length; i++){
			for(int j = 0; j < weightSizes.length; j++){
				if(i < weightSizes[j]){
					int pos = i; 
					if(j > 0){
						pos = pos - weightSizes[j-1]; 		//Usikker på matrise ordningen. Har det noe å si? 
					}
					int upp = pos / weights.get(j)[0].length; 
					int low = pos % weights.get(j)[0].length; 
					double extra = 0; 
					if(phenotype[i] > 0){
						extra = -1; 
					}
					weights.get(j)[upp][low] = ((double) phenotype[i]) / ((double) weightRange + extra) -1 ; 
					break; 
				}
				
			}	
		}
	
	}
	
	
	public double[] propogate(double[] input){
		double[] upperlevel = input; 
		double[] lowerLevel = new double[hiddenTopolgi[0]]; 
		
		for(int i = 0; i < weights.size(); i++){
			for(int low = 0; low < lowerLevel.length; low++){
				double sum = 0; 
				for(int upp = 0; upp < upperlevel.length; upp++){
					//System.out.println("upp:"+upperlevel[upp] + " w:" + weights.get(i)[upp][low]); 
					sum += (upperlevel[upp] * weights.get(i)[upp][low]); 
				}
				if(i < weights.size() -1){
					lowerLevel[low] = getActivationValue(sum); 
				}
				else{
					//System.out.println("lastsum:"  +sum);
					lowerLevel[low] = sum; 
				}
				
			}
			upperlevel = lowerLevel; 
			if(i < weights.size()-2){
				lowerLevel = new double[hiddenTopolgi[i+1]]; 
			}
			else if(i == weights.size()-2){
				lowerLevel = new double[3];//hardcoded
			}
			
		}
		return lowerLevel; 
	}
	
	

	private double getActivationValue(double sum) {
		//System.out.println("sum:"  +sum); 
		if(actFunc.equals("sigmoid")){ // scaling?? Gives out only positive values...
			return 1 / (1 + Math.exp(-sum)); 
		}
		else if(actFunc.equals("tanh")){ 
			return Math.tanh(sum); 
		}
		else if(actFunc.equals("step")){ 
			if(sum >= stepValue){
				return 1; 
			}
			else{
				return 0; 
			}
		}
		else if(actFunc.equals("ramp")){
			if(sum >= rampIntervall[0] && sum <= rampIntervall[1]){
				return ((sum - rampIntervall[0]) / (rampIntervall[1] - rampIntervall[0])); 
			}
			else{
				return 0; 
			}	
		}
		return 0; 
	}
	
	
	public int getMotorAction(int[] sense){
		
		double[] annInn = new double[6]; 
		for(int i = 0; i < 3; i++){
			if(sense[i] == 1){
				annInn[i] = 1.0; 
			}
			else if(sense[i] == -1){
				annInn[i+3] = 1.0; 
			}
		}
		//System.out.println("inn:"  +annInn[0] + "," + annInn[1] + "," +annInn[2] + ","+annInn[3] + "," + annInn[4] + "," +annInn[5] + ",");
		
		
		double[] annOut = propogate(annInn); 
		//System.out.println("out:"  +annOut[0] + ", " + annOut[1] + ", " +annOut[2] + ","); 
		//There might be an idea to add some randomness(with exponential scaling), so that the slightly better choice is not chosen all times.
		int deltaDir = 0; 
		if(annOut[0] == annOut[1] && annOut[0] == annOut[2]){ // dont care; 
			return 0; 
			/*
			Random rnd = new Random(); 
			return rnd.nextInt(3) -1; 
			*/
		}
		//System.out.println("out:"  +annOut[0] + "," + annOut[1] + "," +annOut[2] + ","); 
		
		for(int i = 1; i < annOut.length; i++){
			if(annOut[i] > annOut[deltaDir]){
				
				deltaDir = i;  
			}
		}
		/*
		
		//System.out.println("dir:"+ (deltaDir - 1)); 
		*/
		return deltaDir - 1; 
	
	}
	
	
	
	

}
