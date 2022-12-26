import csv, re, sqlite3

con = sqlite3.connect("res/database_modfire.db")
cur = con.cursor()
Filemame = "Results/outputPairsMulti.csv"
f=open(Filemame,"r")
lines=f.readlines()
result=[]

x = []
y = []
i = 1
o = 0
printNext = True
print("Pick solution")
isFirst = True
divisible = 0
for line in lines:
	if(isFirst):
		isFirst = False
	elif(line[0] == "P"):
		break
	divisible = divisible + 1
		

for line in lines:
	if(printNext):
		print(line[:-1])
		printNext = False
	else:	
		if(i % divisible == 0):
			printNext = True
			
	i+=1

val = input("Pick solution: ")
goTo = int(val)*divisible+1
until = (int(val)+1)*divisible


import itertools
years = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
soilByYear = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]

toWrite = ''

f=open("Results/ChosenResultsWoodByYear.csv","w+")

f.write("Wood Obtained by MU/Presc\n")
with open(Filemame, "r") as text_file:
	for line in itertools.islice(text_file, goTo, until):
		ugpresc = line.split(',')
		ug = ugpresc[0]
		presc = ugpresc[1]

		cur.execute("SELECT V_harv, V_thin, Soilloss FROM action_external where Id like "+str(ug)+" and Presc like " + str(presc)) 
		result = cur.fetchall()
		
		toWrite = toWrite + str(ug) + ", " + str(presc) + "\n"
		
		for i in range(0,len(result)):
			years[i] = years[i] + result[i][0]+result[i][1]
			soilByYear[i] = soilByYear[i] + result[i][2]
			
			toWrite = toWrite + str(2020+i) + ": "+ str(result[i][0]+result[i][1]) + "\n"


		cur.execute("SELECT sum(V_harv+V_thin) FROM action_external where Id like "+str(ug)+" and Presc like " + str(presc)) 
		result = cur.fetchall()

		f.write(str(ug)+"/"+ str(presc)+"- "+str(result[0][0]).replace('\n', "")+"\n")
	


f.close()
print("Wood/Soilloss Obtained by year")
for i in range(0,len(years)):
	print(str(2020+i)+": "+ str(years[i])+ ", " + str(soilByYear[i]))
			






