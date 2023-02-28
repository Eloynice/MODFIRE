import csv, re, sqlite3

con = sqlite3.connect("database_modfire.db")
cur = con.cursor()
f2 = open("region_init.txt", "w+")


toWrite = ''


for i in range(0, 1406):
	cur.execute("SELECT region FROM region_int where ug like "+str(i)) 
	result = cur.fetchall()
	toWrite = str(result[0]).replace(')','').replace('(','')
	toWrite = toWrite[:-2]+"\n"
	toWrite = toWrite[1:]
	f2.write(toWrite)
	toWrite = ''

f2.close()
