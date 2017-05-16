package beerTracker;

import java.util.ArrayList;
import java.util.Random;

public class CTRNN {
	 
	int[] hiddenTopolgi;  
	int inputNodesCount; 
	int outputNodesCount; 
	
	int[] weightSizes; 
	int weightRange; 
	
	String actFunc = "sigmoid"; 
	double stepValue = 0.3; 
	double[] rampIntervall = {0.2, 0.5};
	
	ArrayList<double[][]> weights = new ArrayList<double[][]>(); 
	ArrayList<double[]> biasWeights = new ArrayList<double[]>(); 
	ArrayList<double[][]> neighWeights = new ArrayList<double[][]>();
	ArrayList<double[]> gains = new ArrayList<double[]>();
	ArrayList<double[]> timeConst = new ArrayList<double[]>();
	
	ArrayList<double[]> internalState = new ArrayList<double[]>(); 
	ArrayList<double[]> lastOutput = new ArrayList<double[]>();
	
	boolean sim = false; 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public CTRNN(int[] hiddenTopolgi, int inputNodesCount, int outputNodesCount, int weightRange){
		this.hiddenTopolgi = hiddenTopolgi; 
		this.inputNodesCount = inputNodesCount; 
		this.outputNodesCount = outputNodesCount; 
		this.weightRange = weightRange;		
			
		for(int layer = 0; layer < hiddenTopolgi.length; layer++){
			double[][] weightsLayer; 
			if(layer == 0){
				weightsLayer = new double[inputNodesCount][hiddenTopolgi[layer]]; 
			}
			else{
				weightsLayer = new double[hiddenTopolgi[layer-1]][hiddenTopolgi[layer]]; 
			}
			weights.add(weightsLayer);
			biasWeights.add(new double[hiddenTopolgi[layer]]); 
			internalState.add(new double[hiddenTopolgi[layer]]);	
			lastOutput.add(new double[hiddenTopolgi[layer]]);
			neighWeights.add(new double[hiddenTopolgi[layer]][hiddenTopolgi[layer]]); 
			gains.add(new double[hiddenTopolgi[layer]]);
			timeConst.add(new double[hiddenTopolgi[layer]]); 	
		}
			
		double[][] outputWeightsLayer = new double[hiddenTopolgi[hiddenTopolgi.length-1]][outputNodesCount]; 
		weights.add(outputWeightsLayer);
		biasWeights.add(new double[outputNodesCount]); 
		internalState.add(new double[outputNodesCount]);
		lastOutput.add(new double[outputNodesCount]);
		neighWeights.add(new double[outputNodesCount][outputNodesCount]); 
		gains.add(new double[outputNodesCount]);
		timeConst.add(new double[outputNodesCount]); 
		
			
			
		
		weightSizes = new int[weights.size()];
		weightSizes[0] = weights.get(0).length * weights.get(0)[0].length; 
		for(int i = 1; i < weightSizes.length; i++){
			weightSizes[i] = weightSizes[i-1] + (weights.get(0).length * weights.get(0)[0].length); 	
		}
		System.out.println("weighSizes: " +  weightSizes[0] + "," + weightSizes[1]); 
		
		
	}
	
	

	
	public void updateWeights(int[] phenotype){
		
		int phenoIndex = 0; 
		for(int i = 0; i < weights.size(); i++){
			
			for(int j = 0; j < weights.get(i).length; j++){
				for(int k = 0; k < weights.get(i)[j].length; k++){
					if(j > 4){
						weights.get(i)[j][k] = ((((double) phenotype[phenoIndex]) / ((double) weightRange -1)) -0.5)*(15); 
					}
					else{
						weights.get(i)[j][k] = ((((double) phenotype[phenoIndex]) / ((double) weightRange -1)) -0.5)*10; 
					}
					phenoIndex++; 
				}
			}
			
			for(int j = 0; j < biasWeights.get(i).length; j++){
				biasWeights.get(i)[j] = ((((double) phenotype[phenoIndex]) / ((double) weightRange -1)) - 1)*10; 
				phenoIndex++;
			}
			
			for(int j = 0; j < neighWeights.get(i).length; j++){
				for(int k = 0; k < neighWeights.get(i)[j].length; k++){
					neighWeights.get(i)[j][k] = ((((double) phenotype[phenoIndex]) / ((double) weightRange -1)) -0.5)*5;  // 5 for task 1
					phenoIndex++;
				}
			}
				
			for(int j = 0; j < gains.get(i).length; j++){
				gains.get(i)[j] = (((double) phenotype[phenoIndex]) / ((double) weightRange -1) *4) +1; 
				phenoIndex++;
			}
			
			for(int j = 0; j < timeConst.get(i).length; j++){
				timeConst.get(i)[j] = ((((double) phenotype[phenoIndex]) / ((double) weightRange -1))+1); 
				phenoIndex++;
			}
		}
	}
	
	
	public double[] propagate(double[] input){
		double[] upperlevel = input; 
		double lowerLevel = hiddenTopolgi[0]; 
		
		if(sim){
			for(int i = 0; i< 5; i++){System.out.print(String.format("%.3f",upperlevel[i]) + " : ");}
			System.out.print("\n");
		}
		
		for(int i = 0; i < weights.size(); i++){
			//double output[] = new double[(int)lowerLevel]; 
			
			for(int low = 0; low < lowerLevel; low++){
				
				double dy = 0;
				double t = timeConst.get(i)[low];
				double y = internalState.get(i)[low];
				double theta = biasWeights.get(i)[low];
				double s = 0;		
				
				for(int upp = 0; upp < upperlevel.length; upp++){
					s += (upperlevel[upp] * weights.get(i)[upp][low]); 
				}
				
				for(int neigh = 0; neigh < lastOutput.get(i).length; neigh++){
					//System.out.println("lastOutput.get(i):"+lastOutput.get(i).length + " : weights.get(i):"+weights.get(i).length);
					s += (lastOutput.get(i)[neigh] * neighWeights.get(i)[neigh][low]);
				}
				
				dy = (-y + s + theta) / t;
				y += dy;
				
				internalState.get(i)[low] = y;
				//output[low] = getActivationValue(gains.get(i)[low], y); 
				lastOutput.get(i)[low] = getActivationValue(gains.get(i)[low], y);
				
			}
			
			//lastOutput.set(i, output); 
			upperlevel = lastOutput.get(i);
			if(i < weights.size()-2)
				lowerLevel = hiddenTopolgi[i+1]; 
			else if(i == weights.size()-2)
				lowerLevel = outputNodesCount;
		}
		if(sim){
			for(int i = 0; i< 2; i++){System.out.print(String.format("%.3f",lastOutput.get(lastOutput.size()-1)[i])+ " : ");}
			System.out.print("\n");
			System.out.print("\n");
		}
		
		
	
		return lastOutput.get(lastOutput.size()-1); 
	}

	
	
	
	
	private double getActivationValue(double g, double y) {
		return 1 / (1 + Math.exp(-(g*y))); 
	}
	
	
	
	
	
	public int[] getMotorAction(double[] annInn, boolean print){
		sim = print; 
		double[] annOut = propagate(annInn); 
		int deltaDir = 0;
		
		/*
		if(print){
			System.out.println(String.format("%.3f",annOut[0]) +":"+String.format("%.3f",annOut[1])); 
		}
		*/
	
		if(annOut[0] > annOut[1] +0.1 && annOut[0] > 0.6){
			double value = ((annOut[0] - annOut[1])*4) ;
			//System.out.println("value:" +value);
			deltaDir = (int) value ;
			deltaDir = -deltaDir; 
			if(deltaDir < -4){
				deltaDir = -4; 
			}
		}
		else if(annOut[1] > annOut[0] +0.1  && annOut[1] > 0.6){
			double value = ((annOut[1] - annOut[0])*4) ;
			deltaDir = (int) value ;
			if(deltaDir > 4 ){
				deltaDir = 4; 
			}
		}
		int pullAction = 0; 
		if(outputNodesCount == 3){
			if(annOut[2] > 0.83 && annOut[2] < 0.90){
				pullAction = 1; 
			}
	
		}
		
		int[] action = {deltaDir, pullAction};
		return action; 
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/*
	public int[] getMotorAction(double[] annInn, boolean print){
	
		double[] annOut = propagate(annInn); 
		int deltaDir = 0;

		if(print){
			System.out.println(String.format("%.3f",annOut[0]) +":"+String.format("%.3f",annOut[1])); 
		}
	
		if(annOut[0] > annOut[1] +0.1 && annOut[0] > 0.5){
			double value = ((annOut[0] - 0.4)*10);
			//System.out.println("value:" +value);
			deltaDir = (int) -value -1 ; 
			if(deltaDir < -4){
				deltaDir = -4; 
			}
		}
		else if(annOut[1] > annOut[0] +0.1  && annOut[1] > 0.5){
			double value = ((annOut[1] - 0.4)*10);
			deltaDir = (int) value +1 ;
			if(deltaDir > 4 ){
				deltaDir = 4; 
			}
		}
		
		int[] action = {deltaDir, 0};
		if(print){
			System.out.println("deltaDir:" +deltaDir);
		}
		return action; 
		
		
	}
	*/
	

}
