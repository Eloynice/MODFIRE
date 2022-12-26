import csv, re, sqlite3

fy= open("Maps/GraphInputFileW.txt", "w+")
fs= open("Maps/GraphInputFileS.txt", "w+")
fr= open("Maps/GraphInputFileR.txt", "w+")

con = sqlite3.connect("res/database_modfire.db")
cur = con.cursor()
Filemame = "outputPairs.csv"
f=open(Filemame,"r")
lines=f.readlines()
result=[]

for line in lines:
	MU = line.split(',')[0]
	Pr = line.split(',')[1]
	cur.execute("SELECT Year, Species, V_thin, V_harv FROM action_external where Id = "+str(MU)+" and Presc = "+str(Pr)+" and (V_thin or V_harv) > 0.0")  
	result = cur.fetchall()

	for i in range(0, len(result)):
		year = result[i][0]
		act = ''
		woodSum = int(result[i][2]) + int(result[i][3])
		
			
		toWrite = str(MU)+","+str(year)+","+str(result[i][1])+","+str(woodSum)+"\n"
		fy.write(toWrite)
	
	cur.execute("SELECT Year, Soilloss FROM action_external where Id = "+str(MU)+" and Presc = "+str(Pr))  
	result = cur.fetchall()

	for i in range(0, len(result)):
		year = result[i][0]
					
		toWrite = str(year)+","+str(result[i][1])+"\n"
		fs.write(toWrite)
		
	
	cur.execute("SELECT Year, Perc_r0 FROM action_external where Id = "+str(MU)+" and Presc = "+str(Pr))  
	result = cur.fetchall()

	for i in range(0, len(result)):
		year = result[i][0]
					
		toWrite = str(year)+","+str(result[i][1])+"\n"
		fr.write(toWrite)
		
		
fy.close()
fs.close()
fr.close

	
