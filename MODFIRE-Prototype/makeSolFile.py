import csv, re, sqlite3

fy= open("Maps/MapInputFile.txt", "w+")

con = sqlite3.connect("res/database_modfire.db")
cur = con.cursor()
Filemame = "Results/outputPairs.csv"
f=open(Filemame,"r")
lines=f.readlines()
result=[]
#0-Year,1-Spec,2-Thin,3-Harv

for line in lines:
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
	
	
	
	
	

