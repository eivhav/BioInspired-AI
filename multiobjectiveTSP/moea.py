
from utilities import strToFloat
from random import random, sample
from matplotlib import pyplot
from operator import attrgetter
from geneticOperators import *
from tkinter import *

def nsgaii(problem, populationSize, maxGen,
           mutation, mutationRate, crossover, crossoverRate,
           tourSize):
    
    
    children = []
    population = []
    parents = []
    
    mutation = globals()[mutation]
    crossover = globals()[crossover]
    
    for _ in range(populationSize): 
        child = Solution()
        child.genotype = problem.initGenotype()
        children.append(child)
        
    currentGen = 0
    while True:
        
        for child in children:
            child.phenotype = problem.development(child.genotype)
            child.fitness = problem.fEvaluation(child.phenotype)
        
        population = children + parents
        population = removeDuplicates(population)
        nonDominationSorting(population)
        crowdingDistanceAssignment(population)
        survivorSelection(population, populationSize)
        
        print('--------------------------------')
        print('Generation ' + str(currentGen) + '\n')
        printStats(population)
        if currentGen >= maxGen-1: break
        
        parents = crowdingTournamentSelection(population, 
                                              populationSize,
                                              tourSize)
        children = createOffspring(parents,
                                   mutation, crossover, 
                                   mutationRate, crossoverRate)
        
        currentGen += 1


    printParetoSolutions(population)
    plotPopulation(population)
    plotParetoFront(population)
    
    return population
    
    
    
    
    
    
def removeDuplicates(solutions):
    return list(set(solutions))

        
    
    
    
    
def nonDominationSorting(solutions):
    
    for s in solutions: s.rank = 0
    solutions.sort(key=lambda x: x.fitness[0], reverse=False)
    
    rank = 1
    for i in range(len(solutions)):
        
        cur = solutions[i]
        if cur.rank > 0: continue
        else: cur.rank = rank
        
        for j in range(i+1,len(solutions)):
            if solutions[j].rank == 0 and \
               solutions[j].fitness[1] < cur.fitness[1]:
                cur = solutions[j]
                cur.rank = rank
        
        rank += 1
        
    solutions.sort(key=lambda x: x.rank, reverse=False)
    
    
    
    
    
    
def crowdingDistanceAssignment(solutions):
    
    for s in solutions: s.cd = float('inf')
    
    feasableRegion = []
    front = []
    rank = 1
    
    for s in solutions:
        if s.rank > rank:
            rank += 1
            feasableRegion.append(front)
            front = []
        front.append(s)
        
    for front in feasableRegion:
        if len(front) < 2: continue
        for i in range(1,len(front)-1):
            front[i].cd = \
            (abs(front[i+1].fitness[0] - front[i-1].fitness[0]) / \
            (front[-1].fitness[0] - front[0].fitness[0])) + \
            (abs(front[i+1].fitness[1] - front[i-1].fitness[1]) / \
            (front[0].fitness[1] - front[-1].fitness[1]))
            
            
            
            
            
            
def survivorSelection(solutions, size):
    if len(solutions) > size: solutions = solutions[:size]






def crowdingTournamentSelection(solutions, size, tourSize):
    
    def selectionOperator(subGroup):
        
        bestRank = float('inf')
        for s in subGroup: 
            if s.rank < bestRank: bestRank = s.rank
        contestants = [s for s in subGroup if s.rank == bestRank]
    
        if len(contestants) == 1: 
            return contestants[0]
        return max(contestants, key=attrgetter('cd'))
    
    
    parents = []
    if len(solutions) < tourSize: tourSize = len(solutions)
    for _ in range(size):
        parents.append(selectionOperator(sample(solutions, tourSize)))
    return parents






def createOffspring(parents, mutation, crossover, 
                    mutationRate, crossoverRate):
    
    children = []
    for i in range(len(parents)):
        
        c = Solution()
        if i%2 == 0: p0, p1 = parents[i], parents[i+1]
        else: p0, p1 = parents[i], parents[i-1]
        
        if random() < crossoverRate:
            c.genotype = crossover(p0,p1)
            
        else:
            if random() < mutationRate: c.genotype = mutation(p0)
            else: c.genotype = p0.genotype
            
        children = children + [c]
        
    return children






def printStats(population):
    stats = [float('inf'),float('inf'),0,0,0,0]
    for p in population:
        if p.fitness[0] < stats[0]: stats[0] = p.fitness[0]
        if p.fitness[1] < stats[1]: stats[1] = p.fitness[1]
        if p.fitness[0] > stats[2]: stats[2] = p.fitness[0]
        if p.fitness[1] > stats[3]: stats[3] = p.fitness[1]
        stats[4] += p.fitness[0]
        stats[5] += p.fitness[1]
        
    print('Best f0: ' + str(stats[0]))
    print('Best f1: ' + str(stats[1]) + '\n')
    print('Worst f0: ' + str(stats[2]))
    print('Worst f1: ' + str(stats[3]) + '\n')
    print('Avg f0: ' + str(stats[4]/len(population)))
    print('Avg f1: ' + str(stats[5]/len(population)) + '\n')
    
    
    
    
    
    
def printParetoSolutions(population):
    print('Pareto optimal solutions:')
    for p in population:
        if p.rank > 1: break
        print(p.phenotype)
        
            
      
      
            
            
def plotParetoFront(solutions):
    
    bestf0 = [float('inf'),float('inf')]
    poorf0 = [0,0]
    bestf1 = [float('inf'),float('inf')]
    poorf1 = [0,0]
    
    f0, f1 = [], []
    for s in solutions:
        if s.rank > 1:
            pyplot.plot(f0, f1, 'g')
            pyplot.plot(f0, f1, 'go')
            bestf0 = [f0[0],f1[0]]
            poorf0 = [f0[-1],f1[-1]]
            bestf1 = [f0[-1],f1[-1]]
            poorf1 = [f0[0],f1[0]]
            break
        f0.append(s.fitness[0])
        f1.append(s.fitness[1])
    
    axes = pyplot.gca()
    axes.set_xlim([axes.get_xlim()[0]-30,axes.get_xlim()[1]+30])
    axes.set_ylim([axes.get_ylim()[0]-10000,axes.get_ylim()[1]+10000])
    pyplot.hlines(y=bestf1[1], xmin=axes.get_xlim()[0], 
                  xmax=bestf1[0], color='blue', zorder=1)
    pyplot.hlines(y=poorf1[1], xmin=axes.get_xlim()[0], 
                  xmax=poorf1[0], color='gray', zorder=1)
    pyplot.vlines(x=bestf0[0], ymin=axes.get_ylim()[0], 
                  ymax=bestf0[1], color='blue', zorder=2)
    pyplot.vlines(x=poorf0[0], ymin=axes.get_ylim()[0], 
                  ymax=poorf0[1], color='gray', zorder=2)
    
    pyplot.xlabel('Cost (minimize)')
    pyplot.ylabel('Distance (minimize)')
    pyplot.savefig('plotPareto')
    pyplot.show()
    
    
    
    
    
    
def plotPopulation(solutions):
    
    bestf0 = [float('inf'),float('inf')]
    poorf0 = [0,0]
    bestf1 = [float('inf'),float('inf')]
    poorf1 = [0,0]
    
    rank = 1
    f0, f1 = [], []
    for s in solutions:
        if s.rank > rank:
            if rank == 1: 
                pyplot.plot(f0, f1, 'g')
            pyplot.plot(f0, f1, 'go')
            rank += 1
            f0, f1 = [], []
        if s.fitness[0] < bestf0[0]: bestf0 = [s.fitness[0],s.fitness[1]]
        if s.fitness[1] < bestf1[1]: bestf1 = [s.fitness[0],s.fitness[1]]
        if s.fitness[0] > poorf0[0]: poorf0 = [s.fitness[0],s.fitness[1]]
        if s.fitness[1] > poorf1[1]: poorf1 = [s.fitness[0],s.fitness[1]]
        f0.append(s.fitness[0])
        f1.append(s.fitness[1])
    
    if rank == 1: pyplot.plot(f0, f1, 'g')
    pyplot.plot(f0, f1, 'go')
    
    axes = pyplot.gca()
    axes.set_xlim([axes.get_xlim()[0]-30,axes.get_xlim()[1]+30])
    axes.set_ylim([axes.get_ylim()[0]-10000,axes.get_ylim()[1]+10000])
    pyplot.hlines(y=bestf1[1], xmin=axes.get_xlim()[0], 
                  xmax=bestf1[0], color='blue', zorder=1)
    pyplot.hlines(y=poorf1[1], xmin=axes.get_xlim()[0], 
                  xmax=poorf1[0], color='gray', zorder=1)
    pyplot.vlines(x=bestf0[0], ymin=axes.get_ylim()[0], 
                  ymax=bestf0[1], color='blue', zorder=2)
    pyplot.vlines(x=poorf0[0], ymin=axes.get_ylim()[0], 
                  ymax=poorf0[1], color='gray', zorder=2)
    
    pyplot.xlabel('Cost (minimize)')
    pyplot.ylabel('Distance (minimize)')
    pyplot.savefig('plotPop')
    pyplot.show()
            





class Solution():
    
    def __init__(self):
        self.genotype = None
        self.phenotype = None
        self.fitness = []
        self.rank = 0
        self.cd = float('inf')
        
    def __eq__(self, other):
        return self.fitness[0] == other.fitness[0]\
           and self.fitness[1] == other.fitness[1]
           
    def __hash__(self):
        return hash(('f0', self.fitness[0],
                     'f1', self.fitness[1]))
        
    def __str__(self):
        return self.phenotype
        
        
        
        
        
        
class Screen():
        
    def __init__(self, state):
        
        self.state = state
        self.master = Tk()
        self.labels = []
        self.entries = []
        
        l = Label(text='Population size')
        l.grid(row=1,column=0)
        e = Entry(self.master, width=7)
        e.insert(0, str(state[1]))
        e.grid(row=1,column=1)
        self.labels.append(l)
        self.entries.append(e)
        
        l = Label(text='Generations')
        l.grid(row=2,column=0)
        e = Entry(self.master, width=7)
        e.insert(0, str(state[2]))
        e.grid(row=2,column=1)
        self.labels.append(l)
        self.entries.append(e)
        
        l = Label(text='Mutation')
        l.grid(row=3,column=0)
        e1 = Entry(self.master, width=20)
        e1.insert(0, str(state[3]))
        e1.grid(row=3,column=2)
        e2 = Entry(self.master, width=7)
        e2.insert(0, str(state[4]))
        e2.grid(row=3,column=1)
        self.labels.append(l)
        self.entries.append(e1)
        self.entries.append(e2)
        
        l = Label(text='Crossover')
        l.grid(row=4,column=0)
        e1 = Entry(self.master, width=20)
        e1.insert(0, str(state[5]))
        e1.grid(row=4,column=2)
        e2 = Entry(self.master, width=7)
        e2.insert(0, str(state[6]))
        e2.grid(row=4,column=1)
        self.labels.append(l)
        self.entries.append(e1)
        self.entries.append(e2)
        
        l = Label(text='Tournament size')
        l.grid(row=5,column=0)
        e = Entry(self.master, width=7)
        e.insert(0, str(state[7]))
        e.grid(row=5,column=1)
        self.labels.append(l)
        self.entries.append(e)
        
        self.oAddButton = Button(self.master, text="run", command=self.runAlgorithm)
        self.oAddButton.grid(row=6,column=0)
        
        
        
        
    def runAlgorithm(self):
        self.state[1] = int(self.entries[0].get())
        self.state[2] = int(self.entries[1].get())
        self.state[3] = self.entries[2].get()
        self.state[4] = strToFloat(self.entries[3].get())[0]
        self.state[5] = self.entries[4].get()
        self.state[6] = strToFloat(self.entries[5].get())[0]
        self.state[7] = int(self.entries[6].get())
        
        nsgaii(self.state[0],
               self.state[1],
               self.state[2],
               self.state[3],
               self.state[4],
               self.state[5],
               self.state[6],
               self.state[7])
        
        
        
        
    def persistScreen(self):
        self.master.mainloop()
        

        
        

        