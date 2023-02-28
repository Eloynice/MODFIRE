import csv, re, sqlite3

con = sqlite3.connect("database_modfire.db")
cur = con.cursor()
f = open("external_init.txt", "w+")
f2 = open("internal_init.txt", "w+")

toWrite = ''


for i in range(0, 1406):
	cur.execute("SELECT external FROM ug_ids where internal like "+str(i)+ " order by internal")
	results = cur.fetchall()
	toWrite = results[0]
	f.write(toWrite[-1]+"\n")
	f2.write(str(i)+"\n")
