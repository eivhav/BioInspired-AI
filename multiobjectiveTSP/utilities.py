import os
import xlrd
from math import sqrt
from random import randint
from re import findall

def getFilePath(fileName, fileExtension):
    path = os.path.abspath(__file__)
    dir_path = os.path.dirname(path)
    return os.path.join(dir_path,fileName+'.'+fileExtension)



def extractFromXlsx(path):
    workbook = xlrd.open_workbook(path)
    sheet = workbook.sheet_by_index(0)
    
    data = []
    for r in range(1, sheet.nrows):
        row = []
        for c in range(1, sheet.ncols):
            val = sheet.cell_value(r,c)
            if val == '': row.append(0)
            else: row.append(int(sheet.cell_value(r,c)))
        data.append(row)   
         
    return data



def standardDeviation(X):
    mean = sum(X)/len(X)
    deviation = 0
    for x in X: deviation += pow(x-mean,2)
    return sqrt(deviation/len(X))



def bitToInt(bitStr):
    integer = 0
    for i in range(len(bitStr)): integer += bitStr[i]*pow(2,len(bitStr)-1-i)
    return integer



def intToBit(n,l):
    return [int(digit) for digit in bin(n)[2:].zfill(l)]



def randBitStr(length):
    bitStr = []
    for _ in range(length): bitStr.append(randint(0,1))
    return bitStr



def isFloat(x):
    '''Utility function: Check if input is float
    @param x:   Input, can be anything
    @return:    True if input is float, false if not
    '''
    try: float(x)
    except: return False
    else: return True



def isInt(x):
    '''Utility function: Check if input is integer
    @param x:   Input, can be anything
    @return:    True if input is integer, false if not
    '''
    try:
        a = float(x)
        b = int(a)
    except: return False
    else: return a == b 
    

    
def strToInt(line):
    '''Utility function: Extract all integers in string
    @param line:    String of mixed numbers and letters
    @return:        List containing integers found in order
    '''
    outStr = findall(r'\d+', line)
    intList = [int(x) for x in outStr]
    return intList



def strToFloat(line):
    '''Utility function: Extract all floats in string
    @param line:    String of mixed numbers and letters
    @return:        List containing floats found in order
    '''
    inter = findall('[-+]?\d*\.\d+|\d+', line)
    outStr = []
    for i in inter: outStr += i.split()
    floatList = [float(x) for x in outStr]
    return floatList



def chunk(seq, num):
    avg = len(seq) / float(num)
    out = []
    last = 0.0

    while last < len(seq):
        out.append(seq[int(last):int(last + avg)])
        last += avg

    return out