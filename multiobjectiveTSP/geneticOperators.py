
from random import randint
from math import ceil, log2
from utilities import chunk, bitToInt, intToBit
from itertools import chain






def swapMutation(parent):
    
    genomeLength = ceil(log2(len(parent.phenotype)))
    genomeNum = len(parent.phenotype)
    genomes = chunk(parent.genotype, genomeNum)
    
    genomeImage1 = []
    genomeImage2 = []
    
    while(True):
        g = randint(0,genomeNum-1)
        b = randint(0,genomeLength-1)
        genomeImage1 = genomes[g][:]
        genomeImage2 = genomeImage1[:]
        
        if genomeImage1[b] == 0: genomeImage2[b] = 1
        else: genomeImage2[b] = 0
        if bitToInt(genomeImage2) < genomeNum: break
    
    genomeIndex1 = genomes.index(genomeImage1)
    genomeIndex2 = genomes.index(genomeImage2)
    
    genomes[genomeIndex1] = genomeImage2
    genomes[genomeIndex2] = genomeImage1
    
    return list(chain(*genomes))






def insertionMutation(parent):
    
    genomeLength = ceil(log2(len(parent.phenotype)))
    genomeNum = len(parent.phenotype)
    genomes = chunk(parent.genotype, genomeNum)
    
    genomeImage1 = []
    genomeImage2 = []
    
    while(True):
        g = randint(0,genomeNum-1)
        b = randint(0,genomeLength-1)
        genomeImage1 = genomes[g][:]
        genomeImage2 = genomeImage1[:]
        
        if genomeImage1[b] == 0: genomeImage2[b] = 1
        else: genomeImage2[b] = 0
        if bitToInt(genomeImage2) < genomeNum: break
    
    genomeIndex1 = min(genomes.index(genomeImage1),genomes.index(genomeImage2))
    genomeIndex2 = max(genomes.index(genomeImage1),genomes.index(genomeImage2))
    
    tempGenome = genomes[genomeIndex1]
    genomes[genomeIndex1] = genomes[genomeIndex2]
    for i in range(genomeIndex1, genomeIndex2):
        genomeIndex1 += 1
        temp = genomes[genomeIndex1]
        genomes[genomeIndex1] = tempGenome
        tempGenome = temp 
    
    return list(chain(*genomes))






def orderCrossover(parent0, parent1):
    
    l = ceil(log2(len(parent1.phenotype)))
    genomeNum = len(parent1.phenotype)
    
    offspringPhenotype = [-1]*genomeNum
    
    p0 = randint(0,genomeNum-1)
    while(True):
        p1 = randint(0,genomeNum-1)
        if p0 != p1: break
    
    offspringPhenotype[min(p0,p1):max(p0,p1)] = \
    parent0.phenotype[min(p0,p1):max(p0,p1)]
    
    for n in parent1.phenotype:
        if n not in offspringPhenotype:
            offspringPhenotype[offspringPhenotype.index(-1)] = n
    
    offspringGenotype = []
    for n in offspringPhenotype:
        offspringGenotype = offspringGenotype + intToBit(n,l)
        
    return offspringGenotype
    
    