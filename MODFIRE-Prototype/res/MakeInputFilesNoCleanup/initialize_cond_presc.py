import csv, re, sqlite3

con = sqlite3.connect("database_modfire.db")
cur = con.cursor()
f3 = open("wood_presc_init.txt", "w+")
f4 = open("thin_harv_init.txt", "w+")
f5 = open("soilloss_presc_init.txt", "w+")
cur2 = con.cursor()


for i in range(0, 1406):
	cur.execute("SELECT Presc, V_thin, V_harv, Soilloss FROM action_internal where Id like "+str(i))
	results = cur.fetchall()
	toWrite = ''
	toWrite2 = ''
	toWrite3 = ''
	
	if(results == []):
		f3.write(str(i)+"|-1\n")
	else:
	
		currentP = results[0][0]
		for j in range(0, len(results)):
			if(results[j][0] == currentP):
				wood = results[j][1] + results[j][2]
				toWrite += str(wood)+","
				
				if(results[j][1] != 0.0 and results[j][2] != 0.0):
					toWrite2 += "th,"
				elif(results[j][1] != 0.0 and results[j][2] == 0.0):
					toWrite2 += "T,"
				elif(results[j][1] == 0.0 and results[j][2] != 0.0):
					toWrite2 += "H,"
				else:
					toWrite2 += "N,"
				toWrite3 += str(results[j][3])+","
			else:
				toWrite = toWrite[:-1]+"/"

				currentP = results[j][0]
				wood = results[j][1] + results[j][2]
				toWrite += str(wood)+","
				
				toWrite2 = toWrite2[:-1]+"/"
				if(results[j][1] != 0.0 and results[j][2] != 0.0):
					toWrite2 += "th,"
				elif(results[j][1] != 0.0 and results[j][2] == 0.0):
					toWrite2 += "T,"
				elif(results[j][1] == 0.0 and results[j][2] == 0.0):
					toWrite2 += "H,"
				else:
					toWrite2 += "N,"
					
				toWrite3 = toWrite3[:-1]+"/"
				toWrite3 += str(results[j][3])+","
				
		f3.write(toWrite[:-1] + "\n")
		f4.write(toWrite2[:-1] + "\n")
		f5.write(toWrite3[:-1] + "\n")
		
		toWrite = ''
		toWrite2 = ''
		toWrite3 = ''
