DISTS = {1000..3000..1000}
CHOCO_HOME = $(HOME)/IdeaProjects/Modfire/modfire/MODFIRE-Prototype
CHOCO_HOME = .
CHOCO = $(CHOCO_HOME)/$(wildcard choco-solver-4.*-jar-with-dependencies.jar)
CLASSES = $(shell find -name \*.class | sed -e 's;[^/]*$$;;' | sort -u | tr '\n' :)
JFLAGS = -cp $(CHOCO):$(CLASSES)

all: make

#Multi runs for 8 hours
#MainFloat [AREALIMIT] res [MINIMUM BORDER] [TYPE(MULTI, SINGLE)] [REGION FILE] [CRITERIA FLAGS]

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
	java $(JFLAGS) MainFloat 50 res 25 Multi Full 0 1 2

	
sequence:
	java $(JFLAGS) MainFloat 50 res 1 Multi PaivaIslands 0 1 2
	java $(JFLAGS) MainFloat 50 res 1 Multi ParPen 0 1 2
	java $(JFLAGS) MainFloat 50 res 1 Multi PaivaWest 0 1 2
	java $(JFLAGS) MainFloat 50 res 1 Multi PaivaEast 0 1 2
	
contactzone:
	java $(JFLAGS) MainFloat 50 res 1 Single PaivaWestEastContact 0 1 2
	
locks:
	java $(JFLAGS) MainWithSolutionFile 50 res 1 Multi PaivaEast WestEastContactSolution 0 1 2
	java $(JFLAGS) MainWithSolutionFile 50 res 1 Multi PaivaWest WestEastContactSolution 0 1 2
	
verify: 
	java $(JFLAGS) VerifySolutionsFloat 50 res 1 work-1.vmt
	
verifySingle:
	java $(JFLAGS) VerifySingleSolutionFloat 50 res 1 1mWithLock17141.vmt

paredesSingle:	
	java $(JFLAGS) MainFloat 50 res 50 Single Paredes 0 1 2

paredesMulti:
	java $(JFLAGS) MainFloat 50 res 50 Multi Paredes 0 1 2
	
penafielSingle:
	java $(JFLAGS) MainFloat 50 res 50 Single Penafiel 0 1 2

penafielMulti:
	java $(JFLAGS) MainFloat 50 res 50 Multi Penafiel 0 1 2
	
paivaWSingle:
	java $(JFLAGS) MainFloat 50 res 50 Single PaivaWest 0 1 2
	
paivaWMulti:
	java $(JFLAGS) MainFloat 50 res 50 Multi PaivaWest 0 1 2

paivaESingle:
	java $(JFLAGS) MainFloat 50 res 50 Single PaivaEast 0 1 2
	
paivaEMulti:
	java $(JFLAGS) MainFloat 50 res 50 Multi PaivaEast 0 1 2
	
paivaISingle:
	java $(JFLAGS) MainFloat 50 res 50 Single PaivaIslands 0 1 2
	
paivaIMulti:
	java $(JFLAGS) MainFloat 50 res 50 Multi PaivaIslands 0 1 2

parpenSingle:
	java $(JFLAGS) MainFloat 50 res 50 Single ParPen 0 1 2

parpenMulti:
	java $(JFLAGS) MainFloat 50 res 50 Single ParPen 0 1 2
	
time:
	java $(JFLAGS) MainTime 50 res 50 28800000 Paredes 0 1 2
