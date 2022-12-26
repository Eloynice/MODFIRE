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




plt.plot([-25000, -25010], [165000, 165010], lw=15, c='royalblue', label='Paredes')
plt.plot([-25000, -25010], [165000, 165010], lw=15, c='lime',  label='Penafiel')
plt.plot([-25000, -25010], [165000, 165010], lw=15, c='fuchsia',  label='PaivaWest')
plt.plot([-25000, -25010], [165000, 165010], lw=15, c='yellow',  label='PaivaIslands')
plt.plot([-25000, -25010], [165000, 165010], lw=15, c='red',  label='PaivaEast')
plt.plot([-25000, -25010], [165000, 165010], lw=17, c='lightgray')
plt.legend() 


toPaint = [1]


file = open('subregions/Penafiel')
for line in file:        
    xx = []
    yy = []
    for v in vert4MU[line[:-1]]:
        xx.append(v[0])
        yy.append(v[1])
    plt.fill(xx, yy, facecolor='lime', edgecolor='black', linewidth=1)
    
file = open('subregions/Paredes')
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




plt.show()



