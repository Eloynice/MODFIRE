import csv, re, sqlite3

con = sqlite3.connect("database_modfire.db")
cur = con.cursor()
cur2 = con.cursor()
cur3 = con.cursor()
cur4 = con.cursor()
cur5 = con.cursor()
cur6 = con.cursor()
cur7 = con.cursor()
cur8 = con.cursor()
cur9 = con.cursor()
cur10 = con.cursor()
cur11 = con.cursor()
cur12 = con.cursor()
cur13 = con.cursor()
cur14 = con.cursor()
cur15 = con.cursor()
cur16 = con.cursor()
cur17 = con.cursor()
cur18 = con.cursor()
cur19 = con.cursor()
cur20 = con.cursor()
cur21 = con.cursor()
cur22 = con.cursor()
cur23 = con.cursor()
cur24 = con.cursor()
cur25 = con.cursor()
cur26 = con.cursor()
cur27 = con.cursor()
cur28 = con.cursor()
cur29 = con.cursor()

f0 = open("crit_file0.txt", "w+") #wood
f1 = open("crit_file1.txt", "w+") #soil
f2 = open("crit_file2.txt", "w+") #perc_r
f3 = open("crit_file3.txt", "w+") #biodiversity
f4 = open("crit_file4.txt", "w+") #cashflow
f5 = open("crit_file5.txt", "w+") #carbon stock
f6 = open("crit_file6.txt", "w+") #npv
f7 = open("crit_file7.txt", "w+") #perc_rait
f8 = open("crit_file8.txt", "w+") #r
f9 = open("crit_file9.txt", "w+") #rait
f10 = open("crit_file10.txt", "w+") #sbiom


for i in range(0, 1406):
	cur.execute("SELECT Presc, wood FROM wood_rewards where ug like "+str(i))
	cur2.execute("SELECT Presc, Soilloss FROM soilloss where ug like "+str(i))
	cur3.execute("SELECT Presc, perc_r0_total FROM perc_r0 where ug like "+str(i))
	cur4.execute("SELECT Presc, perc_r5_total FROM perc_r5 where ug like "+str(i))
	cur5.execute("SELECT Presc, perc_r10_total FROM perc_r10 where ug like "+str(i))
	
	cur18.execute("SELECT Presc, sbiom0_total FROM sbiom0 where ug like "+str(i))
	cur19.execute("SELECT Presc, sbiom5_total FROM sbiom5 where ug like "+str(i))
	cur20.execute("SELECT Presc, sbiom10_total FROM sbiom10 where ug like "+str(i))
	cur9.execute("SELECT Presc, r0_total FROM r0 where ug like "+str(i))
	cur10.execute("SELECT Presc, r5_total FROM r5 where ug like "+str(i))
	cur11.execute("SELECT Presc, r10_total FROM r10 where ug like "+str(i))
	cur12.execute("SELECT Presc, npv0_total FROM npv0 where ug like "+str(i))
	cur13.execute("SELECT Presc, npv5_total FROM npv5 where ug like "+str(i))
	cur14.execute("SELECT Presc, npv10_total FROM npv10 where ug like "+str(i))
	cur15.execute("SELECT Presc, rait0_total FROM rait0 where ug like "+str(i))
	cur16.execute("SELECT Presc, rait5_total FROM rait5 where ug like "+str(i))
	cur17.execute("SELECT Presc, rait10_total FROM rait10 where ug like "+str(i))
	cur6.execute("SELECT Presc, biodiversity0_total FROM biodiversity0 where ug like "+str(i))
	cur7.execute("SELECT Presc, biodiversity5_total FROM biodiversity5 where ug like "+str(i))
	cur8.execute("SELECT Presc, biodiversity10_total FROM biodiversity10 where ug like "+str(i))
	cur21.execute("SELECT Presc, perc_rait0_total FROM perc_rait0 where ug like "+str(i))
	cur22.execute("SELECT Presc, perc_rait5_total FROM perc_rait5 where ug like "+str(i))
	cur23.execute("SELECT Presc, perc_rait10_total FROM perc_rait10 where ug like "+str(i))
	cur24.execute("SELECT Presc, cashflow0_total FROM cashflow0 where ug like "+str(i))
	cur25.execute("SELECT Presc, cashflow5_total FROM cashflow5 where ug like "+str(i))
	cur26.execute("SELECT Presc, cashflow10_total FROM cashflow10 where ug like "+str(i))
	cur27.execute("SELECT Presc, cstock0_total FROM cstock0 where ug like "+str(i))
	cur28.execute("SELECT Presc, cstock5_total FROM cstock5 where ug like "+str(i))
	cur29.execute("SELECT Presc, cstock10_total FROM cstock10 where ug like "+str(i))
	
	results = cur.fetchall() #wood
	results2 = cur2.fetchall() #soil
	results3 = cur3.fetchall() #percr0
	results4 = cur4.fetchall() #percr5
	results5 = cur5.fetchall() #percr10
	
	results6 = cur6.fetchall() #biodiversity
	results7 = cur7.fetchall() 
	results8 = cur8.fetchall() 
	results9 = cur9.fetchall() #r
	results10 = cur10.fetchall() 
	results11 = cur11.fetchall()
	results12 = cur12.fetchall() #npv
	results13 = cur13.fetchall() 
	results14 = cur14.fetchall()
	results15 = cur15.fetchall() #rait
	results16 = cur16.fetchall() 
	results17 = cur17.fetchall()
	results18 = cur18.fetchall() #sbiom
	results19 = cur19.fetchall() 
	results20 = cur20.fetchall() 
	results21 = cur21.fetchall() #percrait
	results22 = cur22.fetchall() 
	results23 = cur23.fetchall() 
	results24 = cur24.fetchall() #cashflow
	results25 = cur25.fetchall() 
	results26 = cur26.fetchall() 
	results27 = cur27.fetchall() #cstock
	results28 = cur28.fetchall() 
	results29 = cur29.fetchall() 
	
	toWrite = '' #wood
	toWrite2 = '' #soil
	toWrite3 = '' #percr0
	toWrite4 = '' #percr5
	toWrite5 = '' #percr10
	
	toWrite6 = '' #biodiversity
	toWrite7 = '' #
	toWrite8 = '' #
	toWrite9 = '' #r
	toWrite10 = '' #
	toWrite11 = '' #
	toWrite12 = '' #npv
	toWrite13 = '' #
	toWrite14 = '' #
	toWrite15 = '' #rait
	toWrite16 = '' #
	toWrite17 = '' #
	toWrite18 = '' #sbiom
	toWrite19 = '' #
	toWrite20 = '' #
	toWrite21 = '' #percrait
	toWrite22 = '' #
	toWrite23 = '' #
	toWrite24 = '' #cashflow
	toWrite25 = '' #
	toWrite26 = '' #
	toWrite27 = '' #cstock
	toWrite28 = '' #
	toWrite29 = '' #
	
	

	currentP = results[0][0]
	
	for j in range(0, len(results)):
		if(results[j][0] == currentP):
			toWrite += str(results[j][1]) #wood
			toWrite2 += str(-1*results2[j][1]) #soil
			toWrite3 += str(1*results3[j][1]) #percr0
			toWrite4 += str(1*results4[j][1]) #percr5
			toWrite5 += str(1*results5[j][1]) #percr10
			
			toWrite6 += str(1*results6[j][1]) #biodiversity
			toWrite7 += str(1*results7[j][1]) #
			toWrite8 += str(1*results8[j][1]) #
			toWrite9 += str(1*results9[j][1]) #r
			toWrite10 += str(1*results10[j][1]) #
			toWrite11 += str(1*results11[j][1]) #
			toWrite12 += str(1*results12[j][1]) #npv
			toWrite13 += str(1*results13[j][1]) #
			toWrite14 += str(1*results14[j][1]) #
			toWrite15 += str(1*results15[j][1]) #rait
			toWrite16 += str(1*results16[j][1]) #
			toWrite17 += str(1*results17[j][1]) #
			toWrite18 += str(1*results18[j][1]) #sbiom
			toWrite19 += str(1*results19[j][1]) #
			toWrite20 += str(1*results20[j][1]) #
			toWrite21 += str(1*results21[j][1]) #percrait
			toWrite22 += str(1*results22[j][1]) #
			toWrite23 += str(1*results23[j][1]) #
			toWrite24 += str(1*results24[j][1]) #cashflow
			toWrite25 += str(1*results25[j][1]) #
			toWrite26 += str(1*results26[j][1]) #
			toWrite27 += str(1*results27[j][1]) #cstock
			toWrite28 += str(1*results28[j][1]) #
			toWrite29 += str(1*results29[j][1]) #
		else:
			toWrite = toWrite[:-1]+"," #wood
			toWrite2 = toWrite2[:-1]+"," #soil
			toWrite3 = toWrite3[:-1]+"," #percr0
			toWrite4 = toWrite4[:-1]+"," #percr5
			toWrite5 = toWrite5[:-1]+"," #percr10
			
			toWrite6 = toWrite6[:-1]+"," #biodiversity
			toWrite7 = toWrite7[:-1]+"," #
			toWrite8 = toWrite8[:-1]+"," #
			toWrite9 = toWrite9[:-1]+"," #r
			toWrite10 = toWrite10[:-1]+"," #
			toWrite11 = toWrite11[:-1]+"," #
			toWrite12 = toWrite12[:-1]+"," #npv
			toWrite13 = toWrite13[:-1]+"," #
			toWrite14 = toWrite14[:-1]+"," #
			toWrite15 = toWrite15[:-1]+"," #rait
			toWrite16 = toWrite16[:-1]+"," #
			toWrite17 = toWrite17[:-1]+"," #
			toWrite18 = toWrite18[:-1]+"," #sbiom
			toWrite19 = toWrite19[:-1]+"," #
			toWrite20 = toWrite20[:-1]+"," #
			toWrite21 = toWrite21[:-1]+"," #percrait
			toWrite22 = toWrite22[:-1]+"," #
			toWrite23 = toWrite23[:-1]+"," #
			toWrite24 = toWrite24[:-1]+"," #cashflow
			toWrite25 = toWrite25[:-1]+"," #
			toWrite26 = toWrite26[:-1]+"," #
			toWrite27 = toWrite27[:-1]+"," #cstock
			toWrite28 = toWrite28[:-1]+"," #
			toWrite29 = toWrite29[:-1]+"," #

			currentP = results[j][0]
			
			toWrite += str(results[j][1]) #wood
			toWrite2 += str(-1*results2[j][1]) #soil
			toWrite3 += str(1*results3[j][1]) #percr0
			toWrite4 += str(1*results4[j][1]) #percr5
			toWrite5 += str(1*results5[j][1]) #perct10
			
			toWrite6 += str(1*results6[j][1]) #biodiversity
			toWrite7 += str(1*results7[j][1]) #
			toWrite8 += str(1*results8[j][1]) #
			toWrite9 += str(1*results9[j][1]) #r
			toWrite10 += str(1*results10[j][1]) #
			toWrite11 += str(1*results11[j][1]) #
			toWrite12 += str(1*results12[j][1]) #npv
			toWrite13 += str(1*results13[j][1]) #
			toWrite14 += str(1*results14[j][1]) #
			toWrite15 += str(1*results15[j][1]) #rait
			toWrite16 += str(1*results16[j][1]) #
			toWrite17 += str(1*results17[j][1]) #
			toWrite18 += str(1*results18[j][1]) #sbiom
			toWrite19 += str(1*results19[j][1]) #
			toWrite20 += str(1*results20[j][1]) #
			toWrite21 += str(1*results21[j][1]) #percrait
			toWrite22 += str(1*results22[j][1]) #
			toWrite23 += str(1*results23[j][1]) #
			toWrite24 += str(1*results24[j][1]) #cashflow
			toWrite25 += str(1*results25[j][1]) #
			toWrite26 += str(1*results26[j][1]) #
			toWrite27 += str(1*results27[j][1]) #cstock
			toWrite28 += str(1*results28[j][1]) #
			toWrite29 += str(1*results29[j][1]) #
			
	f0.write(toWrite + "," + toWrite + "," + toWrite + "\n") #wood
	toWrite = ''

	f1.write(toWrite2 + "," + toWrite2 + "," + toWrite2 + "\n") #soil
	toWrite2 = ''
	
	f2.write(toWrite3.replace(".",'') + "," + toWrite4.replace(".",'') + "," + toWrite5.replace(".",'') + "\n") #percr0
	toWrite3 = ''
	
	#f4.write(toWrite4 + "\n") #percr5
	toWrite4 = ''
	
	#f5.write(toWrite5 + "\n") #percr10
	toWrite5 = ''
	
	f3.write(toWrite6 + "," + toWrite7 + "," + toWrite8 + "\n") #biodiversity
	toWrite6 = ''
	#f7.write(toWrite7 + "\n") #
	toWrite7 = ''	
	#f8.write(toWrite8 + "\n") #
	toWrite8 = ''
	
	f8.write(toWrite9 + "," + toWrite10 + "," + toWrite11 + "\n") #r
	toWrite9 = ''
	#f10.write(toWrite10 + "\n") #
	toWrite10 = ''	
	#f11.write(toWrite11 + "\n") #
	toWrite11 = ''
	
	f6.write(toWrite12 + "," + toWrite13 + "," + toWrite14 + "\n") #npv
	toWrite12 = ''
	#f13.write(toWrite13 + "\n") #
	toWrite13 = ''	
	#f14.write(toWrite14 + "\n") #
	toWrite14 = ''
	
	f9.write(toWrite15 + "," + toWrite16 + "," + toWrite17 + "\n") #rait
	toWrite15 = ''
	#f16.write(toWrite16 + "\n") #
	toWrite16 = ''	
	#f17.write(toWrite17 + "\n") #
	toWrite17 = ''
	
	f10.write(toWrite18 + "," + toWrite19 + "," + toWrite20 + "\n") #sbiom
	toWrite18 = ''
	#f19.write(toWrite19 + "\n") #
	toWrite19 = ''	
	#f20.write(toWrite20 + "\n") #
	toWrite20 = ''
	
	f7.write(toWrite21.replace(".",'') + "," + toWrite22.replace(".",'') + "," + toWrite23.replace(".",'') + "\n") #percrait
	toWrite21 = ''
	#f22.write(toWrite22 + "\n") #
	toWrite22 = ''	
	#f23.write(toWrite23 + "\n") #
	toWrite23 = ''
	
	
	f4.write(toWrite24 + "," + toWrite25 + "," + toWrite26 + "\n") #cashflow
	toWrite24 = ''
	#f25.write(toWrite25 + "\n") #
	toWrite25 = ''	
	#f26.write(toWrite26 + "\n") #
	toWrite26 = ''
	
	f5.write(toWrite27 + "," + toWrite28 + "," + toWrite29 + "\n") #cstock
	toWrite27 = ''
	#f28.write(toWrite28 + "\n") #
	toWrite28 = ''	
	#f29.write(toWrite29 + "\n") #
	toWrite29 = ''
	
