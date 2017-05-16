package eaProject;

public class PhenoType {
	
	GenoType genome; 
	int[] symbols; 
	int generationID; 
	double fitness; 
	
	public PhenoType(GenoType gt, int symbolLength, int generationID){
		genome = gt; 
		int noOfSymbols = gt.dna.length / symbolLength;
		symbols = new int[noOfSymbols]; 
		for(int i = 0; i< noOfSymbols; i++){
			int number = 0;  
			for(int j = 0; j < symbolLength; j++){
				if(genome.dna[(i*symbolLength) + j]){
					number = number + (int) Math.pow(2, (symbolLength - j -1)); 
				}
			}
			symbols[i] = number; 
		
		}
		this.generationID = generationID; 
	}
	
	
	
	

}
