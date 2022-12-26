import csv, re, sqlite3

fy= open("Maps/MapInputFile.txt", "w+")

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


Filemame = "Results/outputPairsMulti.csv"
f=open(Filemame,"r")
lines=f.readlines()
result=[]

with open(Filemame, "r") as text_file:
	for line in itertools.islice(text_file, goTo, until):

		MU = line.split(',')[0]
		Pr = line.split(',')[1]
		cur.execute("SELECT Year, Species, V_thin, V_harv FROM action_external where Id = "+str(MU)+" and Presc = "+str(Pr)+" and (V_thin or V_harv) > 0.0")  
		result = cur.fetchall()

		for i in range(0, len(result)):
			year = result[i][0]
			act = ''
			if(int(result[i][2]) > 0):
				act = 't'
			else:
				act = 'h'
				
			toWrite = str(MU)+","+str(year)+","+str(result[i][1])+"-"+act+"\n"
			fy.write(toWrite)
		
fy.close()
	
	
	
	
	

