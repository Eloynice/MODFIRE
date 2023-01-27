DISTS = {1000..3000..1000}
CHOCO_HOME = $(HOME)/IdeaProjects/Modfire/modfire/MODFIRE-Prototype
CHOCO_HOME = .
CHOCO = $(CHOCO_HOME)/$(wildcard choco-solver-4.*-jar-with-dependencies.jar)
CLASSES = $(shell find -name \*.class | sed -e 's;[^/]*$$;;' | sort -u | tr '\n' :)
JFLAGS = -cp $(CHOCO):$(CLASSES)

all: make

#Multi runs for 8 hours
#MainWithSpecific [AREALIMIT] res [MINIMUM BORDER] [TYPE(MULTI, SINGLE)] [REGION FILE] [CRITERIA FLAGS]

#AREALIMIT is the maximum ammount of forest area that can be contiguously harvested in a single year
#res is the folder with the input files
#[MINIMUM BORDER] is the minimum border lenght between adjacent Management Units for the adjacency to be taken into account
#[Multi, Single] Whether the software does multi or single criteria optimization
#[REGION FILE] Filename in the subregions folder with the MUs that are taken into account

#[CRITERIA FLAGS] List on by one which criteria should be optimized, in single criteria optimization only the first criteria is used

#Criteria flags
#0-Wood Yield
#1-Soil Loss
#2-Perc_r
#3-Biodiversity
#4-Cashflow
#5-Carbon Stock
#6-NPV
#7-Perc_rait
#8-R
#9-Rait
#10-Sbiom

make: 	
	java $(JFLAGS) MainWithSpecific 50 res 50 Multi PaivaIslands 0 1 2

paredesSingle:	
	java $(JFLAGS) MainWithSpecific 50 res 50 Single Paredes 0

paredesMulti:
	java $(JFLAGS) MainWithSpecific 50 res 50 Multi Paredes 0 1 2
	
penafielSingle:
	java $(JFLAGS) MainWithSpecific 50 res 50 Single Penafiel 0

penafielMulti:
	java $(JFLAGS) MainWithSpecific 50 res 50 Multi Penafiel 0 1
	
paivaWSingle:
	java $(JFLAGS) MainWithSpecific 50 res 50 Single PaivaWest 0
	
paivaWMulti:
	java $(JFLAGS) MainWithSpecific 50 res 50 Multi PaivaWest 0 1

paivaESingle:
	java $(JFLAGS) MainWithSpecific 50 res 50 Single PaivaEast 0
	
paivaEMulti:
	java $(JFLAGS) MainWithSpecific 50 res 50 Multi PaivaEast 0 1
	
paivaISingle:
	java $(JFLAGS) MainWithSpecific 50 res 50 Single PaivaIslands 0
	
paivaIMulti:
	java $(JFLAGS) MainWithSpecific 50 res 50 Multi PaivaIslands 0 1

parpenSingle:
	java $(JFLAGS) MainWithSpecific 50 res 50 Single ParedesPenafiel 0

parpenMulti:
	java $(JFLAGS) MainWithSpecific 50 res 50 Single ParedesPenafiel 0 1
	
time:
	java $(JFLAGS) MainTime 50 res 50 28800000 Paredes 0 1
