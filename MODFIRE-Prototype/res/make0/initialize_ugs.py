import csv, re, sqlite3

con = sqlite3.connect("database_modfire.db")
cur = con.cursor()
f = open("ugs_init.txt", "w+")

toWrite = ''

for i in range(0, 1406):
	cur.execute("SELECT distinct presc FROM action_internal where Id like "+str(i)) 
	result = cur.fetchall()
	if(result == []):
		toWrite = "-1\n"
	else:
		for j in range(0, len(result)):	
			toWrite += str(result[j]).replace(')','').replace('(','')
		toWrite = "0,"+ toWrite[:-1]+"\n"
	f.write(toWrite.replace('\'',''))
	#print(toWrite.replace('\'',''))
	toWrite = ''

