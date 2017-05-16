package flatlandv1;

public class PhenoType {
	
	public GenoType genome; 
	public int[] symbols; 
	public int generationID; 
	public double fitness; 
	
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
	
	public int[] getSymbols(){
		return symbols; 
	}
	
	public void setSymbols(int[] sym){
		symbols = sym; 
		
	}
	
	
	
	
	

}
