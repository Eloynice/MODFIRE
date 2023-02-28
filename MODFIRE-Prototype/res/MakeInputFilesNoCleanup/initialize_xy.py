import csv, re, sqlite3

con = sqlite3.connect("database_modfire.db")
cur = con.cursor()
fX = open("x_init.txt", "w+")
fY = open("y_init.txt", "w+")


toWrite = ''


for i in range(0, 1406):
	cur.execute("SELECT sum(x)/count(x) FROM xy_int where ug like "+str(i))
	result = cur.fetchall()
	toWriteX = str(result[0]).replace(')','').replace('(','')	
	
	cur.execute("SELECT sum(y)/count(y) FROM xy_int where ug like "+str(i))
	result = cur.fetchall()
	toWriteY = str(result[0]).replace(')','').replace('(','')

	fX.write(toWriteX[:-1]+"\n")
	fY.write(toWriteY[:-1]+"\n")
