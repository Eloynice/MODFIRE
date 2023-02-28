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
	





solfile = open("VerifiedSolution.csv", "rt")

data = solfile.read()

#data.replace(r'777$',"_A")
#print(re.sub(r'777\n', "_A\n", data))

solfile.close

fw = open("3PVerifiedSolution.csv", "wt")
fw.write(re.sub(r'999\n',"_C\n", re.sub(r'888\n', "_B\n", re.sub(r'777\n', "_A\n", data))))
fw.close()

fw = open("VerifiedSolution.csv", "wt")
fw.write(re.sub(r'999\n',"\n", re.sub(r'888\n', "\n", re.sub(r'777\n', "\n", data))))
fw.close()
