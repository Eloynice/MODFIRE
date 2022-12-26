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

Range = range(2020,2071) 

fy= open("GraphInputFileW.txt", "r")
lines=fy.readlines()


ProdMAD = {'Ct':[], 'Ec':[], 'Pb':[], 'Qr':[], 'Sb':[], 'Rp':[]}

for year in range(2020, 2071):
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
              colors= [Colors['Ec'],Colors['Pb'],Colors['Ct'],Colors['Sb'],Colors['Qr'], Colors['Rp']] )
plt.grid() 
plt.legend(loc='upper left')
plt.xlabel('Year')
plt.ylabel('WoodYield')
#plt.show()
plt.savefig('MapOutput/GraphWood',dpi=100)


fs= open("GraphInputFileS.txt", "r")
lines=fs.readlines()

soils = []

for year in range(2020, 2071):
	soilSum = 0
	for line in lines:
		a = line.split(',')
		if(int(a[0]) == year):
			soilSum += float(a[1][:-1])
	soils.append(soilSum)
	
fig7 = plt.figure(figsize=(10,4))
plt.plot(Range, soils, '-o', c="darkgreen")
plt.xlabel('Year')
plt.ylabel('SoilLoss')
plt.savefig('MapOutput/GraphSoilLoss',dpi=100)

fr= open("GraphInputFileR.txt", "r")
lines=fr.readlines()

rpercentile = []

for year in range(2020, 2071):
	riskSum = 0
	for line in lines:
		a = line.split(',')
		if(int(a[0]) == year):
			riskSum += float(a[1][:-1])
	rpercentile.append(riskSum)
	
fig8 = plt.figure(figsize=(10,4))
plt.plot(Range, rpercentile, '-o', c="red")
plt.xlabel('Year')
plt.ylabel('RiskPercentiles')
plt.savefig('MapOutput/GraphRiskPercentile',dpi=100)
	
