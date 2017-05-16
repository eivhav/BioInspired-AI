package flatlandv1;

import java.util.ArrayList;
import java.util.Random;

public class GOs {
	
	int noOfSymbolsPerDNA; 
	int geneSymbolLength; 
	
	public GOs(int noOfSymbolsPerDNA, int geneSymbolLength){
		this.noOfSymbolsPerDNA = noOfSymbolsPerDNA; 
		this.geneSymbolLength = geneSymbolLength; 
	}

	
	
	public ArrayList<GenoType> crossover1(PhenoType pt1, PhenoType pt2){
		boolean[] gt1DNA = new boolean[noOfSymbolsPerDNA*geneSymbolLength]; 
		boolean[] gt2DNA = new boolean[noOfSymbolsPerDNA*geneSymbolLength];
		Random rnd = new Random(); 
		int pos = rnd.nextInt(pt1.genome.dna.length); 
		for(int i = 0; i < noOfSymbolsPerDNA*geneSymbolLength; i++){
			if(i <= pos){
				gt1DNA[i] = pt1.genome.dna[i]; 
				gt2DNA[i] = pt2.genome.dna[i]; 
			}
			else{
				gt1DNA[i] = pt2.genome.dna[i];
				gt2DNA[i] = pt1.genome.dna[i]; 
			}
		}
		ArrayList<GenoType> gts = new ArrayList<GenoType>();  
		gts.add(new GenoType(gt1DNA));
		gts.add(new GenoType(gt2DNA));
		return gts; 
	
	}
	
	
	
	public GenoType mutate(PhenoType pt1, double mutationFactor){
		boolean[] gt1DNA = new boolean[noOfSymbolsPerDNA*geneSymbolLength]; 
		if(mutationFactor == -1){
			Random rnd1 = new Random();
			int n = rnd1.nextInt(gt1DNA.length);
			for(int i = 0; i < noOfSymbolsPerDNA*geneSymbolLength; i++){
				if(n == i){
					if(pt1.genome.dna[i] = true ){
						gt1DNA[i] = false; 
					}
					else{
						gt1DNA[i] = true; 
					}
				}
				else{
					gt1DNA[i] = pt1.genome.dna[i];
				}
			}	
		}
		else{
			for(int i = 0; i < noOfSymbolsPerDNA*geneSymbolLength; i++){
				Random rnd1 = new Random();
				int n = rnd1.nextInt(100); 
				if(n <= mutationFactor){
					Random rnd2 = new Random();
					gt1DNA[i] = rnd2.nextBoolean(); 
				}
				else{
					gt1DNA[i] = pt1.genome.dna[i]; 
				}
			}
		}
		return new GenoType(gt1DNA); 
	}
	
	
	
	public GenoType copy(PhenoType pt1){
		boolean[] dna = new boolean[noOfSymbolsPerDNA*geneSymbolLength]; 
		for(int i = 0; i < dna.length; i++){
			dna[i] = pt1.genome.dna[i]; 
		}
		return new GenoType(dna);
	}
	
	
	
}
