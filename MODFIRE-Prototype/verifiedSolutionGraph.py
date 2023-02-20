import csv, re, sqlite3
fy= open("Maps/GraphInputFileW.txt", "w+")
fs= open("Maps/GraphInputFileS.txt", "w+")
fr= open("Maps/GraphInputFileR.txt", "w+")

con = sqlite3.connect("database_modfire.db")
cur = con.cursor()
Filemame = "VerifiedSolution.csv"
f=open(Filemame,"r")
lines=f.readlines()
result=[]
print("Extracting from database")
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
fr.close()
con.close()

import matplotlib.pyplot as plt

Colors = {'Ec':'darkred', 'Pb':'cornflowerblue',
          'Ct':'green', 'Sb':'purple', 
          'Qr':'darkgoldenrod','Rp':'orange','No':'gray'}

fig = plt.figure(figsize=(8,10))
ax = plt.gca()
ax.set_facecolor('lightgray')

#plt.plot([-25000, -25001], [165000, 165001], lw=15, c=Colors['Ec'],   label='Ec')
#plt.plot([-25000, -25001], [165000, 165001], lw=15, c=Colors['Pb'],   label='Pb')
#plt.plot([-25000, -25001], [165000, 165001], lw=15, c=Colors['Ct'],   label='Ct')
#plt.plot([-25000, -25001], [165000, 165001], lw=15, c=Colors['Sb'],   label='Sb')
#plt.plot([-25000, -25001], [165000, 165001], lw=15, c=Colors['Qr'],   label='Qr')
#plt.plot([-25000, -25001], [165000, 165001], lw=15, c=Colors['Rp'],   label='Rp')
#plt.plot([-25000, -25001], [165000, 165001], lw=17, c='lightgray')
#plt.legend() 

Range = range(2020,2070) 

fy= open("Maps/GraphInputFileW.txt", "r")
lines=fy.readlines()


ProdMAD = {'Ct':[], 'Ec':[], 'Pb':[], 'Qr':[], 'Sb':[], 'Rp':[]}


print("Plotting Wood Graph")


for year in range(2020, 2070):
	sumE = 0
	sumC = 0
	sumP = 0
	sumQ = 0
	sumS = 0
	sumR = 0
	
	for line in lines:
		a = line.split(',')
		if(int(a[1]) == year):
			if(a[2] == 'Ec'):
				sumE += int(a[3][:-1])
			elif(a[2] == 'Ct'):
				sumC += int(a[3][:-1])
			elif(a[2] == 'Pb'):
				sumP += int(a[3][:-1])
			elif(a[2] == 'Qr'):
				sumQ += int(a[3][:-1])
			elif(a[2] == 'Sb'):
				sumS += int(a[3][:-1])
			elif(a[2] == 'Rp'):
				sumR += int(a[3][:-1])
	ProdMAD['Ec'].append(sumE)
	ProdMAD['Ct'].append(sumC)
	ProdMAD['Pb'].append(sumP)
	ProdMAD['Qr'].append(sumQ)
	ProdMAD['Sb'].append(sumS)
	ProdMAD['Rp'].append(sumR)
		
plt.stackplot(Range, ProdMAD['Ct'], ProdMAD['Ec'], ProdMAD['Pb'], ProdMAD['Qr'], ProdMAD['Sb'], ProdMAD['Rp'],
              labels = ['Ct', 'Ec', 'Pb', 'Qr', 'Sb','Rp'],
              colors= [Colors['Ct'],Colors['Ec'],Colors['Pb'],Colors['Qr'],Colors['Sb'], Colors['Rp']] )
plt.grid() 
plt.legend(loc='upper left')
plt.xlabel('Year')
plt.ylabel('WoodYield')
#plt.show()
plt.savefig('Maps/VerifiedSolutionMaps/GraphWood',dpi=100)


fs= open("Maps/GraphInputFileS.txt", "r")
lines=fs.readlines()
print("Plotting Soil Loss Graph")
soils = []

for year in range(2020, 2070):
	soilSum = 0
	for line in lines:
		a = line.split(',')
		if(int(a[0]) == year):
			soilSum += float(a[1][:-1])
	soils.append(soilSum)
	
fig7 = plt.figure(figsize=(10,4))
plt.plot(Range, soils, '-o', c="darkgreen")
plt.xlabel('Year')
plt.ylabel('Soil Loss')
plt.savefig('Maps/VerifiedSolutionMaps/GraphSoilLoss',dpi=100)

fr= open("Maps/GraphInputFileR.txt", "r")
lines=fr.readlines()
print("Plotting Fire Risk Protection Graph")
rpercentile = []

for year in range(2020, 2070):
	riskSum = 0
	for line in lines:
		a = line.split(',')
		if(int(a[0]) == year):
			riskSum += float(a[1][:-1])
	rpercentile.append(riskSum)

	
fig8 = plt.figure(figsize=(10,4))
plt.plot(Range, rpercentile, '-o', c="red")
plt.xlabel('Year')
plt.ylabel('Fire Risk Protection')
plt.savefig('Maps/VerifiedSolutionMaps/GraphRiskPercentile',dpi=100)

	
fy.close()
fs.close()
fr.close()
