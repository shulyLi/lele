#coding:utf-8

import re

path='/home/shuly/IdeaProjects/lele/src/main/resources/'
fileName=['dataConvert.me','dataConvert.other']

def readOne(name):
    ans = {}
    for line in open(path+name, 'r').xreadlines():
        words = line.strip().split('|')
        if len(words) >= 1:
            ans[words[0]] = words[1:]
    return ans

def readTwo(name):
    ans = {}
    data =  open(path+name, 'r').read().splitlines();
    idx = 0
    while idx < len(data):
        if (idx + 1) < len(data) and data[idx+1][:4] == "    ":
            ans[data[idx].strip()] =  re.split(',', data[idx+1].strip())

            idx = idx + 1
        else :
            ans[data[idx].strip()] = []
        idx = idx + 1
    return ans;

def out(dic):
    with open(path  + 'merge.result', 'w+') as outFile:
        for k,v in dic.items():
            line = k + '|' + '|'.join(v)
            print v
            outFile.write(line + '\n')
        outFile.flush()
        
def solve():
    dicOne = readOne(fileName[0])
    dicTwo = readTwo(fileName[1])
    for k,v in dicTwo.items():
        if k not in dicOne:
            dicOne[k] = v
    out(dicOne) 
if __name__ == '__main__' :
    solve()
