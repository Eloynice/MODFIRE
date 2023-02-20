# Forest Management

The purpose of this implementation is to use Constraint Programming to find valid solutions for problems of forest management.

Here, Management Units (MUs) are associated with possible prescriptions which define which years the MUs are to be harvested.

But in any given year only 50ha of contiguous forest area can be harvested at once.


Therefore the implementation needs input defining:

1-The existing MUs in the forest

2-The area of each MU

3-The MUs adjacent to each MU

4-The border lenght of each adjacency between MUs

5-The IDs of the Prescriptions of each MU

6-Relative to each Prescription, the years where the Prescription defines that the MU should be harvested.

7-Relative to each Prescription, the rewards each Prescription gives for being applied.

# Step 1: Necessary Setup Files.

In general for every line in one of the files we assume a new Management Unit, a file with 4 lines would imply that there are 4 MUs, and since this has to be consistent then that means all input files should have 4 lines.

6 files need to be prepared in the following manner


## external_init.txt 
### This file serves to indicate how many Management Units exist in the forest, so each line should have a number to serve as an External identifier of each MU. There should be as many lines as there are MUs in the forest.

Each of these External IDs have an Internal ID which corresponds to the index of their row starting from 0

........................EXAMPLE:

30

201

333

335

.........................

So they have the following Internal IDs

.........................

0

1

2

3

.........................


## area_init.txt 
### This file sets up the area of each MU.

-----------------Example:

42.60178

8.26

6.0315

26.94

------------------------

## adj_init.txt 
### This file sets up the adjacencies of each MU each line should list the External IDs of the MUs adjacent to the respective MU separated by a comma. If the MU has no adjacent MUs the line should just have a -1

-----------------Example:

201,333

30

30

-1

------------------------

So this forest might look like this:


|333|
 
 |

|30|     |335|

 |

|201|


## border_init.txt 
### This file sets up the lenght of the border of each adjacent MU, each line should list the lenght of the borders of each adjacency separated by a comma. If the MU has no adjacent MUs the should just have a -1

-----------------Example:

259,151

490

102,653

-1

------------------------

If borders aren't a factor is your data just put use 0s in place of real border values


## ugs_init.txt 
### This file sets up the Prescriptions associated with each MU, each line should list the IDs of the Prescriptions associated with each MU

-----------------Example:

1,2,3

19

19,20,21

500,501,502,503,500600,500601

------------------------

## years_init.txt 
### This file sets up the relation between Prescriptions and the years where those Prescriptions have Harvest actions. 

### Each line lists the years where the corresponding Prescription has Harvest actions separated by commas, if the MU has more than one Prescription then a "/" is followed by the next Prescription's harvest years. If a Prescription has no years of Harvest there should be a -1

Before listing the years the line should have the Internal ID of the MU followed by a "|"

 -----------------Example:

0|2026,2056,2066/2027,2049,2060/2028,2040,2052,2064

1|2022,2032,2042

2|2026,2046/2060/-1

3|2052/2057/-1/2067/2052/2052

------------------------

# Step 2: Criteria Files

There can be a maximum of 10 optimizable criteria in the program represented by these files, these files are crit_file0-10.txt and their format is the same.

Each line corresponds to an MU similarly to previous files and the contents are separated by commas, the contents are the values of the objective criteria associated with each Prescription of the respective MU

-----------------Example:

34,30.99,3.363

1568.630

565.79,146,1692

4966,6310,7,87,49,496

------------------------

# Step 3: Running the software

The program is run through the make file and requires Java 11.

Open up the Makefile in this folder and modify the parameters of the "make" label, then run the "make" command in the terminal.

## MainFloat
The MainFloat command is what should be run and its parameters can be changed in the following manner.

The examples shown are related to the Vale de Sousa forest which was used in the MODFIRE project.

## MainFloat parameters:
1st- Maximum Area Limit (50)

2nd- Directory with data about forest (res)

3rd- Minimum Border Lenght (50)

4th- Single or Multi Criteria

5th- Name of file with the UGs to be considered (in subregions folder)

6th and onward- Choose the criterias to be optimized, if the 4th parameter is Single only the first criteria will be optimized


0-Criteria0 (Wood Yield)

1-Criteria1 (Soil Loss)

2-Criteria2 (Perc_r)

3-Criteria3 (Biodiversity)

4-Criteria4 (Cashflow)

5-Criteria5 (Carbon Stock)

6-Criteria6 (NPV)

7-Criteria7 (Perc_rait)

8-Criteria8 (R)

9-Criteria9 (Rait)

10-Criteria10(Sbiom)

#### Example to optimize Wood Yield and Soil Loss in Paredes-> MainFloat 50 res 50 Multi Paredes 0 1

The program will try to find valid solutions for 8 hours (28800000 miliseconds) or until there's a memory error.

## MainTime Paremeters
Same as MainFloat but the 4th parameter sets the time limit for finding solutions in miliseconds

Example: MainTime 50 res 50 28800000 Paredes 0 1


# Step 4: Output files
In Multi Criteria optimization the following files are written to the Results folder:

Output files Multi-Criteria:


AllSolutions.csv:- MU/Presc pairs for every valid solution found 

ParetoSolutions.csv:- MU/Presc pairs for every pareto solution (determined by choco solver)

NonParetoPoints.csv:- Values of the optimizable criteria for every valid solution found 

ParetoPoints.csv:- Values of the optimizable criteria for every pareto solution found 

SolutionDETAILS.csv:- MU/Presc pairs for every pareto solution along with associated values of objective criteria.


In Single Criteria optimization the following file is written to the Results folder:

SingleSolution.csv:- MU/Presc pairs for the solution found 

# Step 5: Visualizing the example.


Run seeMap.py to see the Vale de Sousa Forest and it's sub-divisions

Run plot2DPareto.py to plot a 2d Pareto frontier using the Results/parento.csv file (Only works if there are only 2 criteria in the pareto.csv file)

Run seeResults.py to observe the contents of the solutions in the ParetoSolutions.csv file

Run the makeSolFile.py and then the Maps/maps.py file to paint the solution in the SingleSolution.csv onto files in the Maps/MapOutput folder

Run the makeSolFileMulti.py and then the Maps/maps.py file to pick a solution from the outputPairsSingleMult.csv file and paint the solution onto files in the Maps/MapOutput folder


