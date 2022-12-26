import csv, re, sqlite3

con = sqlite3.connect("database_modfire.db")
cur = con.cursor()
f3 = open("years_init.txt", "w+")
cur2 = con.cursor()

toWrite = ''


for i in range(0, 1406):
	cur.execute("SELECT Presc, Year, V_harv FROM action_internal where Id like "+str(i))
	results = cur.fetchall()
	toWrite = ''
	hasCut = False;
	isFirst = True
	
	if(results == []):
		f3.write(str(i)+"|-1\n")
	else:
	
		currentP = results[0][0]
		for j in range(0, len(results)):
			if(results[j][0] == currentP): #Staying on a Presc
				if(results[j][2] > 0.0):
					toWrite += str(results[j][1])+","
					hasCut = True
					isFirst = False
			
			else: #Changing Presc
				isFirst = False
				toWrite = toWrite[:-1]+"/"
				if(hasCut == False):
					toWrite += str(-1)+"/"
					hasCut = False
				else:
					if(results[j][2] > 0.0):
						toWrite += str(results[j][1])+","
						hasCut = True
					else:
						hasCut = False
				
				currentP = results[j][0]

		if(hasCut == False):
			if(isFirst == True):
				toWrite = toWrite[:-1]
			toWrite += str(-1)+"/"

		f3.write(str(i)+"|"+toWrite[:-1] + "\n")
		toWrite = ''
