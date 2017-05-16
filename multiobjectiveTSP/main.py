
from mtsp import Mtsp
from moea import Screen

state = []

'''
state.append(Mtsp('Cost', 'Distance', 'xlsx'))
state.append(200)
state.append(500)
state.append('swapMutation')
state.append(0.65)
state.append('orderCrossover')
state.append(0.85)
state.append(7)
'''
'''
state.append(Mtsp('Cost', 'Distance', 'xlsx'))
state.append(300)
state.append(2000)
state.append('swapMutation')
state.append(1.00)
state.append('orderCrossover')
state.append(0.90)
state.append(12)
'''
state.append(Mtsp('Cost', 'Distance', 'xlsx'))
state.append(300)
state.append(500)
state.append('insertionMutation')
state.append(0.50)
state.append('orderCrossover')
state.append(0.90)
state.append(12)

screen = Screen(state)
screen.persistScreen()