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
	



filename = sys.argv[1]

solfile = open("Results/"+filename, "rt")

data = solfile.read()

#data.replace(r'777$',"_A")
#print(re.sub(r'777\n', "_A\n", data))

solfile.close

fw = open("Results/3P_"+filename, "wt")
fw.write(re.sub(r'3\n',"_C\n", re.sub(r'2\n', "_B\n", re.sub(r'1\n', "_A\n", data))))
fw.close()

fw = open("Results/"+filename, "wt")
fw.write(re.sub("R\n", '\n', re.sub(r'3\n',"R\n", re.sub(r'2\n', "R\n", re.sub(r'1\n', "R\n", data)))))
fw.close()
