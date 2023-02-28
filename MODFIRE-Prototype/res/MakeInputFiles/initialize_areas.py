import csv, re, sqlite3

con = sqlite3.connect("database_modfire.db")
cur = con.cursor()
f2 = open("area_init.txt", "w+")


toWrite = ''


for i in range(0, 1406):
	cur.execute("SELECT area FROM area_internal where ug like "+str(i)) 
	result = cur.fetchall()
	toWrite = str(result[0]).replace(')','').replace('(','')
	toWrite = toWrite[:-1]+"\n"
	f2.write(toWrite)
	toWrite = ''
