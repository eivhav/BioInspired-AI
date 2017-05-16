
from utilities import *
from math import ceil, log2
from random import shuffle

class Mtsp():
    
    def __init__(self, costFile, distanceFile, extension):
        self.cost = extractFromXlsx(getFilePath(costFile, extension))
        self.distance = extractFromXlsx(getFilePath(distanceFile, extension))
        self.n = len(self.cost)




    def initGenotype(self):
        genotype = []
        genomeLength = ceil(log2(self.n))
        genomeNum = self.n
        
        for i in range(genomeNum):
            genome = intToBit(i,genomeLength)
            for _ in range(len(genome), genomeLength): genome = [0] + genome
            genotype.append(genome)
        shuffle(genotype)
        genotype = [y for x in genotype for y in x]
        return genotype
    
    
    
    
    def development(self, genotype):
        phenotype = []
        genomeLength = ceil(log2(self.n))
        genomeNum = self.n

        for i in range(genomeNum):
            sym = bitToInt(genotype[i*genomeLength:(i+1)*genomeLength])
            phenotype.append(sym%self.n)
        return phenotype
    
    
    
    
    def fEvaluation(self, phenotype):
        return [self.fCost(phenotype), self.fDistance(phenotype)]
    
    
    
    
    def fCost(self, phenotype):
        val = 0
        for i in range(1,len(phenotype)):
            nBig = max(phenotype[i],phenotype[i-1])
            nSmall = min(phenotype[i],phenotype[i-1])
            val += self.cost[nBig][nSmall]
        return val
    
    
    
    
    def fDistance(self, phenotype):
        val = 0
        for i in range(1,len(phenotype)):
            nBig = max(phenotype[i],phenotype[i-1])
            nSmall = min(phenotype[i],phenotype[i-1])
            val += self.distance[nBig][nSmall]
        return val
        
        