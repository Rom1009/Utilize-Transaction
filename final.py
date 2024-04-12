# -----------------IMPORT LIBRARY-----------------------
import copy
from memory_profiler import profile
import time
import matplotlib.pyplot as plt 

# -----------------MAIN FUNCTION-----------------------

# Function name: CoHui_Miner(Database, minCor, minUtility)
# Input: Database which contains 4 values, mincor(int), miUtility(int)
# Output: return the item fit with request.
# Description: 
# + Firstly, Transform data from Database to Transaction,Pq(Purchase quality),U(Each Utility),uT(totoal Utility)
# + Secondly, COmpute Support and TWu for each item. 
# After that, Remove the values aren't smaller than minUtility and Sort all value in Database without it.
# + Thirdly, Check each item we keep. If Utility of this item are higher than minUtility, 
# It will become CoHUi(item has value). After that, we check all condition
# to remove size of the database and take neccessary elements.
# Finally, we continue find elements with having more 2 items. 
def CoHui_Miner(Database, minCor, minUtility):
    start_time = time.time()
    Transaction, Pq, U, uT = [],[],[],[]
    for row in Database:
        Transaction.append(row[0].split(','))
        Pq.append(row[1].split(','))
        U.append(row[2].split(','))
        uT.append(row[3].split(','))
    combo1 = []
    combo(alphabetic,1,combo1,[])
    SUP = Sup(Transaction, combo1) 
    TWU = Twu(Transaction,uT, combo1)
    # print(SUP, TWU)
    Ikeep, Itrash = [], []
    for i in range(len(alphabetic)):
        if TWU[i] >= minUtility:
            Ikeep.append(alphabetic[i])
        else:
            Itrash.append(alphabetic[i])
    
    #print(Ikeep, Itrash)
    
    for i in range(len(Transaction)):        
        for j in range(len(Itrash)):
            value = Itrash[j]
            if value in Transaction[i]:
                idx = Transaction[i].index(value)
                Transaction[i].pop(idx)
                Pq[i].pop(idx)
                minus = int(U[i].pop(idx))
                uT[i][0] = str(int(uT[i][0]) - minus)
    
        sort(Transaction[i], Pq[i], U[i], SUP)
    CoHuis = []

    for X in Ikeep:
        T_X = []
        pruT_X = []
        U_X = []
        uT_X = []
        if u(X,Transaction,U) >= minUtility:
            CoHuis.append(X)
            print(CoHuis)
            print(u(X,Transaction,U))
        RU = 0
        for i in range(len(Transaction)):
            j = 0
            if X in Transaction[i]:
                uTemp = int(uT[i][0])
                while j < len(Transaction[i]) and compare(Transaction[i][j],X,SUP) :
                    uTemp = uTemp - int(U[i][j])
                    j += 1
                if j == len(Transaction[i]) or compare1(Transaction[i][j],X,SUP):
                    continue
                
                elif j<len(Transaction[i]):
                    T_X.append(Transaction[i][j+1:])
                    pruT_X.append(int(U[i][j]))
                    U_X.append(U[i][j+1:])
                    uT_X.append(uTemp) 
                    RU = RU + int(uTemp)
        
        SearchCoHui(X,u(X,Transaction,U),RU,T_X,pruT_X,uT_X,U_X,2,Ikeep,SUP,minUtility,minCor,Transaction)

# Function name: SearchCoHui(X,U,RU,T,pru,u,UU,k,Ikeep,SUP,minUtility,minCor,Transac)
# Input: X(each item), U(Utility of each item), RU(Remain Utility), pru(Prefix Utility),
# T(Transaction after reduce), u(Total Utility after reduce), UU(Utility after reduce),k (each item)
# IKeep(item we keep), SUP(SUpport value), minUtility, mincor, Transaction
# Output: return the item fit with request
# Description:
# Firstly, Create Itemset from 2 and up such as (a,b), (a,c), ... .
# Secondly, Loop into Itemset and check Item exist. After that, we continue to reduce database and each Utility.
# Finally, Return CoHUi and Recursion until the length of Item.
def SearchCoHui(X,U,RU,T,pru,u,UU,k,Ikeep,SUP,minUtility,minCor,Transac):
    Transaction = T
    Trans = Transac
    pruT = pru
    uT = u
    Uitily = UU
    combo2 = []
    combo(Ikeep,k,combo2,[])
    
    for Lastitem in combo2:
        if Check(k,X,Lastitem):
            TX = []
            UTX = []
            pruTX = []
            UT = []
            CoHuis = []
            RU_ = 0
            SUP_ = 0

            UX_ = U
            ULA = U + RU
            for i in range(len(Transaction)):
                j = 0
                uTemp = int(uT[i])
                while j<len(Transaction[i]) and compare(Transaction[i][j],Lastitem,SUP):
                    uTemp = uTemp - int(Uitily[i][j])
                    j = j+1
                
                if j == len(Transaction[i]) or compare1(Transaction[i][j],Lastitem,SUP):
                    UX_ = UX_ - int(pruT[i])
                    ULA = ULA - (int(pruT[i]) + int(uT[i]))

                    if ULA < minUtility:
                        continue
                else:
                    UX_ = UX_ + int(Uitily[i][j])
                    SUP_ = SUP_ + 1
                    
                    if j < len(Transaction[i]) :
                        TX.append(Transaction[i][j+1:])
                        pruTX.append(int(pruT[i]) + int(Uitily[i][j]))
                        UTX.append(uTemp )
                        UT.append(Uitily[i][j+1:])
                        RU_ = RU_ + (uTemp )
                    # for i in UT:
                    #     if i == []:
                    #         i.append(0)
        
            if SUP_ > 0:
                if kulc(Lastitem,Trans,SUP)>=minCor:
                    if UX_>= minUtility:
                        CoHuis.append(Lastitem)
                        print(CoHuis)
                        print(UX_)
                    if UX_ + RU >= minUtility:
                        SearchCoHui(Lastitem,UX_,RU_,TX,pruTX,UTX,UT,k+1,Ikeep,SUP,minUtility,minCor,Trans)



# -----------------SUPPORT FUNCTION-----------------------

# Function name: preprocessingData(input)
# Input: input(Link)
# Output: Data after split ("|")
# Description: Loop in the data and split each.
def preprocessingData(input):
    data = input.split('\n')
    for i in range(len(data)):
        data[i] = data[i].split('|')
    
    return data


# Function name: Sup(Transaction, target)
# Input: Transaction(Array), Target(Item value we want to find)
# Output: Dictionary each item correpoding to each value
# Description: Loop to target if the value don't exist we add to dict , else increment count and add to dict
def Sup(Transaction, target):
    # Transaction a,b,d,c - a,b,d,e,f - ...
    # array [a,b]
    SUP = {}
    # [[a,b,c,d], [a,b,d,e,f], [...]] 
    # [ a,b,c,d,e,f ]
    for t in target:
        count = 0
        for a in Transaction:
            if all(value in a for value in t):
                count += 1
        SUP[str(t)] = count
    return SUP

# Function name: Twu(Transaction,Utility, target)
# Input: Transaction(Array), Target(Item value we want to find)
# Output: The array contain value which compute based on Utility.
# Description:Loop in Target, check if target exist in Transaction we will increase value 
def Twu(Transaction,Utility, target):
    twu = []
    for t in target:
        sum = 0
        for i in range(len(Transaction)):
            if all(value in Transaction[i] for value in t):
                sum += int(Utility[i][0])
        twu.append(sum)

    return twu


# Function name: sort(Trans, Pq, U, SUP)
# Input:Transaction(array), Purchase quality(array), Utility(array), SUP(Dict includes value of each items)
# Output: The array after sort
# Description: Loop into each value if value after higher than before we swap it.
def sort(Trans, Pq, U, SUP):
    for i in range(len(Trans)):
        keyTemp1 = str([Trans[i]])
        for j in range(i+1, len(Trans)):
            keyTemp2 = str([Trans[j]])
            if(SUP[keyTemp2] < SUP[keyTemp1]):
                Trans[i], Trans[j] = Trans[j], Trans[i]
                Pq[i], Pq[j] = Pq[j], Pq[i]
                U[i], U[j] = U[j], U[i]

                keyTemp1 = str([Trans[i]])
                keyTemp2 = str([Trans[j]])
            elif (SUP[keyTemp2] == SUP[keyTemp1]):   
                if(ord(Trans[j]) < ord(Trans[i])):
                    Trans[i], Trans[j] = Trans[j], Trans[i]
                    Pq[i], Pq[j] = Pq[j], Pq[i]
                    U[i], U[j] = U[j], U[i]

                    keyTemp1 = str([Trans[i]])
                    keyTemp2 = str([Trans[j]])


# Function name: compare(x,X,SUP)
# Input: x(character), X (array) , SUP(Dict includes value of each items)
# Output: True ,False
# Description:Loop in X and check SUP of X > x.If conditions are true we increase count and if count equals
# len(X) we will return True else False. 
def compare(x,X,SUP):
    count = 0
    for i in X:
        if SUP[str([x])] < SUP[str([i])] or  ( SUP[str([x])] == SUP[str([i])] and ord(x) < ord(i)) :
            count +=1
    if count == len(X):
        return True
    return False


# Function name: compare1(x,X,SUP)
# Input: x(character), X (array) , SUP(Dict includes value of each items)
# Output: True ,False
# Description:Loop in X and check SUP of X < x.If conditions are true we increase count and if count equals
# len(X) we will return True else False.
def compare1(x,X,SUP):
    count = 0
    for i in X:
        if SUP[str([x])] > SUP[str([i])] or  ( SUP[str([x])] == SUP[str([i])] and ord(x) > ord(i)) :
            count +=1
    if count == len(X):
        return True
    return False

# Function name: combo(S,r,result,x)
# Input: S(array), r(int), result(array), x(array)
# Output: The list of value after combination
# Description: Check condition and finding combination
def combo(S,r,result,x):
    if len(x) < r:
        a = ord(S[0]) if len(x) == 0 else ord(x[-1]) + 1
        b = ord(S[-1]) + 1
        candidates = list(range(a,b))
        
        for c in candidates:
            t = copy.copy(x)
            t.append(chr(c))
            
            if len(t) == r:
                result.append(t)
            combo(S,r,result,t)
        
# Function name: kulc(Item,Transaction,SUP)
# Input:Item(array), Transaction(array), SUP(Dict includes value of each items)
# Output: sum/len(Item)
# Description: Check if Item in Transaction increase count. After that compute algorithm
def kulc(Item,Transaction,SUP):
    count = 0
    for i in range(len(Transaction)):
        if all(value in Transaction[i] for value in Item):
            count = count + 1
    res = copy.deepcopy(Item)
    sum = 0
    while res:
        a = res.pop(0)
        sum = sum + (count/SUP[str([a])])
    return sum/len(Item)

# Function name: u(Items,Trans,Utilitys)
# Input: Items(Character), Trans(array), Utilitys(array)
# Output: sum
# Description: Loop in Trans if elememnt in Trans equals Items. we increase sum based on Utility 
def u(Items,Trans,Utilitys):
    sum = 0
    for i in range(len(Trans)):
        for j in range(len(Trans[i])):
            if Trans[i][j] == Items:
                sum += int(Utilitys[i][j])
    return sum

# Function name: Check(k,character1,character2)
# Input:k(int) character1(array), character2
# Output: True, False
# Description: check if k>2 we compute this.
def Check(k,character1,character2):
    if k >= 2:
        for i in range(k-1):
            if character1[i] != character2[i]:
                return False
    return True



# -----------------ANSWER-----------------------

if __name__ == '__main__':
    f = open("520H0675_520H0464_52000808/data.txt","r")
    data = f.read()
    f1 = open('520H0675_520H0464_52000808/profit.txt',"r")
    data1 = f1.read()
    nmlData = preprocessingData(data)
    alphabetic = []
    alphabetic.extend(preprocessingData(data1)[0][0].split(","))
    CoHui_Miner(nmlData,0.52,70)
    # starting the monitoring
    # tracemalloc.start()

    # # function call
    # app()

    # # displaying the memory
    # print(tracemalloc.get_traced_memory())

    # # stopping the library
    # tracemalloc.stop()