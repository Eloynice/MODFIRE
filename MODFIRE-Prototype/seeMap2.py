file = open('Maps/UG_Vertice_Concelho.txt')
nlin = -1
vert4MU = {}
conc4MU = {}

MUNames = []
MU4conc = {'Paiva':[], 'Penafiel':[], 'Paredes':[]}

xmin =  100000000000
xmax = -100000000000
ymin =  100000000000
ymax = -100000000000

for line in file:        
    nlin += 1
    if nlin == 0:
        continue      
    lex = line.split()    
    
    if nlin < 5 or nlin > 54405:
        #print(lex) 
        continue
        
    if lex[0] not in vert4MU:
        vert4MU[lex[0]] = []
        conc4MU[lex[0]] = lex[4]
        MUNames.append(lex[0])        
    
    x = float(lex[2])
    y = float(lex[3])
    vert4MU[lex[0]].append((x,y))   
    
    if xmin > x : xmin = x 
    if xmax < x : xmax = x 
    if ymin > y : ymin = y 
    if ymax < y :
        ymax = y
        nymax = lex[0]
        
    MU4conc[lex[4]].append(lex[0])        
        
file.close()

import matplotlib.pyplot as plt




fig = plt.figure(figsize=(8,10))
ax = plt.gca()
ax.set_facecolor('lightgray')




plt.plot([-25000, -25010], [165000, 165010], lw=15, c='royalblue', label='NorthWest')
plt.plot([-25000, -25010], [165000, 165010], lw=15, c='lime',  label='NorthEast')
plt.plot([-25000, -25010], [165000, 165010], lw=15, c='fuchsia',  label='PaivaWest')
plt.plot([-25000, -25010], [165000, 165010], lw=15, c='yellow',  label='PaivaIslands')
plt.plot([-25000, -25010], [165000, 165010], lw=15, c='red',  label='PaivaEast')
plt.plot([-25000, -25010], [165000, 165010], lw=10, c='cyan', label='ContactZones')
plt.plot([-25000, -25010], [165000, 165010], lw=17, c='lightgray')
plt.legend() 


toPaint = [1]


#file = open('subregions/Penafiel')
file = open('subregions/NorthEast')
for line in file:        
    xx = []
    yy = []
    for v in vert4MU[line[:-1]]:
        xx.append(v[0])
        yy.append(v[1])
    plt.fill(xx, yy, facecolor='lime', edgecolor='black', linewidth=1)
    
#file = open('subregions/Paredes')
file = open('subregions/NorthWest')
for line in file:        
    xx = []
    yy = []
    for v in vert4MU[line[:-1]]:
        xx.append(v[0])
        yy.append(v[1])
    plt.fill(xx, yy, facecolor='royalblue', edgecolor='black', linewidth=1)

file = open('subregions/PaivaWest')
for line in file:        
    xx = []
    yy = []
    for v in vert4MU[line[:-1]]:
        xx.append(v[0])
        yy.append(v[1])
    plt.fill(xx, yy, facecolor='fuchsia', edgecolor='black', linewidth=1)    
    
file = open('subregions/PaivaIslands')
for line in file:        
    xx = []
    yy = []
    for v in vert4MU[line[:-1]]:
        xx.append(v[0])
        yy.append(v[1])
    plt.fill(xx, yy, facecolor='yellow', edgecolor='black', linewidth=1)    
    
file = open('subregions/PaivaEast')
for line in file:        
    xx = []
    yy = []
    for v in vert4MU[line[:-1]]:
        xx.append(v[0])
        yy.append(v[1])
    plt.fill(xx, yy, facecolor='red', edgecolor='black', linewidth=1)    



v1 = [1113, 1118, 1119, 1120, 1121,1264]
#969,970,971, 1505
for i in range(0, len(v1)):
    xx = []
    yy = []
    for v in vert4MU[str(v1[i])]:
        xx.append(v[0])
        yy.append(v[1])
    plt.fill(xx, yy, facecolor='cyan', edgecolor='black', linewidth=1)

v2 = [1107, 1108, 1110, 1111, 1117, 1431, 1432]
for i in range(0, len(v2)):
    xx = []
    yy = []
    for v in vert4MU[str(v2[i])]:
        xx.append(v[0])
        yy.append(v[1])
    plt.fill(xx, yy, facecolor='cyan', edgecolor='black', linewidth=1)
    
v3 = [296, 400, 662,705, 670, 706, 770]

for i in range(0, len(v3)):
    xx = []
    yy = []
    for v in vert4MU[str(v3[i])]:
        xx.append(v[0])
        yy.append(v[1])
    plt.fill(xx, yy, facecolor='Cyan', edgecolor='black', linewidth=1)

v4 = [523, 666, 671, 665, 524, 566, 32]

for i in range(0, len(v4)):
    xx = []
    yy = []
    for v in vert4MU[str(v4[i])]:
        xx.append(v[0])
        yy.append(v[1])
    plt.fill(xx, yy, facecolor='Cyan', edgecolor='black', linewidth=1)


plt.show()



