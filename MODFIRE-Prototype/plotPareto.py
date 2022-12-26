import numpy as np
import matplotlib.pyplot as plt

print("Plotting 2d frontier based on pareto and nonPareto files in the Results folder")

f=open("Results/pareto.csv","r")

f2 =open("Results/nonPareto.csv","r")

first = f.readline()
first2 = f2.readline()


lines=f.readlines()
lines2 = f2.readlines()


x = []
y = []
lst = []
scatterx = []
scattery = []

for i in lines:
	result = i.split(',')
	x.append(int(result[0]))
	y.append(int(result[1]))
	lst.append([int(result[0]),int(result[1])])
f.close()


lim = 0
for j in lines2:
	lim = lim + 1
	#if(lim == 200):
		#break
	result = j.split(',')
	if([int(result[0]), [int(result[1])]] in lst):
		continue
	else:
		scatterx.append(int(result[0]))
		scattery.append(int(result[1]))

f2.close()


xmin = str(int(min(x)) - 1000)
xmax = str(int(max(x)) + 1000)
ymin = str(int(min(y)) - 1000)
ymax = str(int(max(y)) + 1000)

plt.figure(figsize=(100,100))


plt.scatter(scatterx, scattery, marker = 'x', color = 'r', label = "non-pareto")
plt.scatter(x, y, label = 'pareto frontier')
#plt.plot(x, y)
#plt.plot(x, y, linestyle='--', marker = 'o', color = 'b', label = 'pareto frontier')


#plt.xlim([xmin, xmax])
#plt.ylim([ymin, ymax])

#plt.xticks(x[::10],  rotation='vertical')
#plt.yticks(y[::10],  rotation='horizontal')



plt.legend()
plt.show()



