import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class NoCriteriaWithSolFile {

    public static void giveDomains (Model model, IntVar[] ugs, String dir, UG[] nodes){
        int index = 0;
        try {
            File allUg = new File(dir + "/ugs_init.txt");
            Scanner readerUg = new Scanner(allUg);

            while (readerUg.hasNextLine()) {
                String dataUg = readerUg.nextLine();

                if(nodes[index].valid) {
                    String[] str_split = dataUg.split(",", 0);

                    int size = str_split.length;
                    int[] toInsert = new int[size];

                    for (int i = 0; i < size; i++) {
                        toInsert[i] = Integer.parseInt(str_split[i]);
                    }

                    ugs[index] = model.intVar("UG_" + (index), toInsert);
                }
                else {
                    ugs[index] = model.intVar("UG_"+(index), 0);
                }
                index++;
            }
            readerUg.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        int areaLimit = Integer.parseInt(args[0]);
        String fileDirectory = args[1];

        ArrayList<Integer> islandUGs = new ArrayList<>();
        String regionFile = args[3];
        File island = new File("subregions/"+regionFile);
        Scanner islandReader = new Scanner(island);
        while (islandReader.hasNextLine()) {
            islandUGs.add(Integer.parseInt(islandReader.nextLine()));
        }

        islandReader.close();

        Model m = new Model("Forest Management");

        System.out.println("Reading Input Files");
        BufferedReader reader = new BufferedReader(new FileReader(fileDirectory + "/ugs_init.txt"));

        int nUgs = 0;
        while (reader.readLine() != null) nUgs++;
        reader.close();

        UG[] nodes = new UG[nUgs];
        //if 0 ignore
        int minBorder = Integer.parseInt(args[2]);
        UG.setupInternalIds(nodes, fileDirectory);
        if(minBorder <= 0)
            UG.fillNoCriteria(nodes, fileDirectory, 0);
        else
            UG.fillNoCriteria(nodes, fileDirectory, minBorder);

        for(int i = 0; i < nodes.length; i++){
            if(islandUGs.contains(nodes[i].externalId)){
                nodes[i].valid = true;
            }
        }

        IntVar[] ugs = new IntVar[nUgs]; // same for the constraint variable array

        giveDomains(m, ugs, fileDirectory, nodes); // reads the ugs_init file and initializes each variable with its possible prescription values as domain

        String solutionFileName = args[4];
        File solutionFile = new File("subregions/"+solutionFileName);
        Scanner solFileReader = new Scanner(solutionFile);
        while (solFileReader.hasNextLine()) {
            String solLine = solFileReader.nextLine();
            int externalId = Integer.parseInt(solLine.split(",")[0]);
            int presc = Integer.parseInt(solLine.split(",")[1]);
            for(int i = 0; i < nodes.length; i++){
                if(nodes[i].externalId == externalId && nodes[i].valid) {
                    m.arithm(ugs[i], "=", presc).post();
                }
            }
        }
        solFileReader.close();

        System.out.println("Setting up Constraints");

        for(int ugIndex = 0; ugIndex < nodes.length; ugIndex++) { //loops through every UG
            //the propagator takes as parameters the index of the UG we are starting out from
            //the nodes with all the info, the constraint variable array and the area limit
            if(nodes[ugIndex].valid && !nodes[ugIndex].noAdjacencies) {
                new Constraint("Area Limit Constraint", new CustomPropagatorFloat(ugIndex, nodes, ugs, areaLimit)).post();
            }
        }

        FileWriter pairCrits;

        pairCrits = new FileWriter("Results/"+regionFile+"-Exception.csv");

        Solver solver = m.getSolver();
        //solver.setSearch(Search.activityBasedSearch(ugs));

        FileWriter allSolutionPairs = new FileWriter("Results/"+"NC-"+regionFile+"-AllSolutions.csv");

        System.out.println("Running Solver");

        //8 hours  28800000
        //12 hours 43200000
        //13 hours 46800000
        //14 hours 50400000

        int l = 1;
        long startTime = System.currentTimeMillis(); //fetch starting time
        try{
            while(solver.solve() & (System.currentTimeMillis()-startTime)<46800000)
            {
                allSolutionPairs.write("Solution: "+l+", TimeStamp: "+ (System.currentTimeMillis()-startTime)+"ms\n");

                for(int i = 0; i < nodes.length; i++){
                    if(nodes[i].valid){
                        allSolutionPairs.write(nodes[i].externalId+","+ugs[i].getValue()+"\n");
                    }
                }

                allSolutionPairs.flush();
                System.out.println("Found solution-" + l);
                l++;
            }
        }
        catch (OutOfMemoryError e){
            pairCrits.write("Reached Exception after "+(System.currentTimeMillis()-startTime)+" milliseconds");
        }

        System.out.println("Here");

        pairCrits.close();
    }
}
