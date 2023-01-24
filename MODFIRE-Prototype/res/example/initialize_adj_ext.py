import csv, re, sqlite3

con = sqlite3.connect("database_modfire.db")
cur = con.cursor()
cur2 = con.cursor()
f = open("adj_ext_init.txt", "w+")
f2 = open("border_init.txt", "w+")

toWrite = ''


for i in range(0, 1406):
	cur.execute("Select ug2 from adj_int where ug1 like "+str(i))
	result = cur.fetchall()
	if(result == []):
		toWrite = "-1\n"
	else:
		for j in range(0, len(result)):
			ug_int = str(result[j]).replace(')','').replace('(','').replace(',','')
			cur2.execute("Select external from ug_ids where internal like "+str(ug_int))
			result2 = cur2.fetchall()
			ext_int = str(result2[0]).replace(')','').replace('(','')
			toWrite += ext_int.replace("'", '')

		
		toWrite = toWrite[:-1]+"\n"
	f.write(toWrite)
	toWrite = ''
	
	cur.execute("Select front from adj_int where ug1 like "+str(i))
	result = cur.fetchall()
	if(result == []):
		toWrite = "-1\n"
	else:
		for j in range(0, len(result)):	
			toWrite += str(result[j]).replace(')','').replace('(','')
		toWrite = toWrite[:-1]+"\n"
	f2.write(toWrite)
	toWrite = ''
