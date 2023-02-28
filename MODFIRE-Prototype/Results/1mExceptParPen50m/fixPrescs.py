import re, sys

def get_trailing_number7(s):
	m = re.search(r'777$', s)
	return m
def get_trailing_number8(s):
	m = re.search(r'888$', s)
	return m
def get_trailing_number9(s):
	m = re.search(r'999$', s)
	return m
	



#filename = sys.argv[1]

solfile = open("3P_PaivaWest-ParetoSolutions.csv", "rt")

data = solfile.read()

solfile.close

fw = open("PaivaWest-ParetoSolutions.csv", "wt")
fw.write(re.sub(r'_C\n',"3\n", re.sub(r'_B\n', "2\n", re.sub(r'_A\n', "1\n", data))))
fw.close()


solfile = open("3P_PaivaEast-ParetoSolutions.csv", "rt")

data = solfile.read()

solfile.close

fw = open("PaivaEast-ParetoSolutions.csv", "wt")
fw.write(re.sub(r'_C\n',"3\n", re.sub(r'_B\n', "2\n", re.sub(r'_A\n', "1\n", data))))
fw.close()

solfile = open("3P_PaivaIslands-ParetoSolutions.csv", "rt")

data = solfile.read()

solfile.close

fw = open("PaivaIslands-ParetoSolutions.csv", "wt")
fw.write(re.sub(r'_C\n',"3\n", re.sub(r'_B\n', "2\n", re.sub(r'_A\n', "1\n", data))))
fw.close()

solfile = open("3P_ParPen-ParetoSolutions.csv", "rt")

data = solfile.read()

solfile.close

fw = open("ParPen-ParetoSolutions.csv", "wt")
fw.write(re.sub(r'_C\n',"3\n", re.sub(r'_B\n', "2\n", re.sub(r'_A\n', "1\n", data))))
fw.close()
