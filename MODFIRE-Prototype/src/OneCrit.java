import org.chocosolver.solver.Model;
import org.chocosolver.solver.ParallelPortfolio;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.objective.ParetoOptimizer;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.search.strategy.Search.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OneCrit {

    public static void giveDomains (Model model, IntVar[] ugs, String dir, IntVar[] crit0, UG[] nodes){
        int index = 0;
        try {
            File allUg = new File(dir + "/ugs_init.txt");
            Scanner readerUg = new Scanner(allUg);

            File critFile0 = new File(dir + "/crit_file0.txt");
            Scanner reader0 = null;
            reader0 = new Scanner(critFile0);

            while (readerUg.hasNextLine()) {
                String dataUg = readerUg.nextLine();

                String data0 = null;

                data0 = reader0.nextLine();


                if(nodes[index].valid) {
                    String[] str_split = dataUg.split(",", 0);

                    String[] str_split0 = null;
                    str_split0 = data0.split(",", 0);



                    int size = str_split.length;
                    int[] toInsert = new int[size];
                    int[] toInsert0 = new int[size];

                    for (int i = 0; i < size; i++) {
                        toInsert[i] = Integer.parseInt(str_split[i]);
                        toInsert0[i] = (int) Float.parseFloat(str_split0[i]);
                    }

                    ugs[index] = model.intVar("UG_" + (index), toInsert);
                    crit0[index] = model.intVar("Crit0_" + (index), toInsert0);
                }
                else {
                    ugs[index] = model.intVar("UG_"+(index), 0);
                    crit0[index] = model.intVar("Crit0"+(index), 0);
                }

                index++;
            }

            readerUg.close();
            reader0.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        int areaLimit = Integer.parseInt(args[0]);
        String fileDirectory = args[1];

        ArrayList<Integer> islandUGs = new ArrayList<>();
        String regionFile = args[4];
        File island = new File("subregions/"+regionFile);
        Scanner islandReader = new Scanner(island);
        while (islandReader.hasNextLine()) {
            islandUGs.add(Integer.parseInt(islandReader.nextLine()));
        }

        islandReader.close();

        String criterion = args[3];

        /*Set Flags
        0-woodYield
        1-Soil Loss
        2-Perc_r
        3-Biodiversity
        4-Cashflow
        5-Carbon Stock
        6-NPV
        7-Perc_rait
        8-R
        9-Rait
        10-Sbiom
         */

        ArrayList<Boolean> flags = new ArrayList<>();

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
            UG.fillOneCrit(nodes, fileDirectory, 0, flags);
        else
            UG.fillOneCrit(nodes, fileDirectory, minBorder, flags);

        for(int i = 0; i < nodes.length; i++){
            if(islandUGs.contains(nodes[i].externalId)){
                nodes[i].valid = true;
            }
        }

        IntVar[] ugs = new IntVar[nUgs];
        IntVar[] crit0 = new IntVar[nUgs];

        giveDomains(m, ugs, fileDirectory, crit0, nodes);

        System.out.println("Setting up Constraints");

        for(int ugIndex = 0; ugIndex < nodes.length; ugIndex++) {
            if(nodes[ugIndex].valid && !nodes[ugIndex].noAdjacencies) {
                new Constraint("Area Limit Constraint", new CustomPropagatorFloat(ugIndex, nodes, ugs, areaLimit)).post();
            }
        }

        for(int i = 0; i < nodes.length; i++){
            if(nodes[i].valid) {

                IntVar prescIndex = m.intVar(0, nodes[i].presc.length);

                m.element(ugs[i], nodes[i].presc, prescIndex).post();

                m.element(crit0[i], nodes[i].crit0, prescIndex).post();

            }

            else{
                m.arithm(crit0[i], "=", 0).post();
            }
        }

        IntVar sum0 = m.intVar("CRIT0_SUM",-99999999, 99999999);


        m.sum(crit0,"=", sum0).post();

        FileWriter pairCrits;


        // Single Criterion Optimization
        if(criterion.equalsIgnoreCase("single")) {

            pairCrits = new FileWriter("Results/OC_"+regionFile+"-SingleSolutionDetails.csv");

            m.setObjective(Model.MAXIMIZE, sum0);

            Solver s = m.getSolver();

            System.out.println("Running Solver");

            //s.setSearch(Search.minDomUBSearch(ugs));
            s.setSearch(Search.activityBasedSearch(ugs));

            if (s.solve()) {
                System.out.println("FOUND OPTIMAL SOLUTION!");
                FileWriter outputPairs = new FileWriter("Results/OC_"+regionFile+"-SingleSolution.csv");
                outputPairs.write("Crit0: "+sum0.getValue()+"\n");
                for (int i = 0; i < ugs.length; i++) {
                    if (nodes[i].valid) {
                        pairCrits.write(ugs[i] + ", "+ crit0[i]+"\n");

                        outputPairs.write(nodes[i].externalId + "," + ugs[i].getValue() + "\n");
                    }
                }

                outputPairs.close();

                pairCrits.write(sum0.getValue()+"\n");
                pairCrits.close();
            }
        }
        else {
            pairCrits = new FileWriter("Results/OC_" + regionFile + "-Exception.csv");

            int index = 0;

            Solver solver = m.getSolver();
            solver.setSearch(Search.activityBasedSearch(ugs));

            FileWriter allSolutionPairs = new FileWriter("Results/OC_" + regionFile + "-OneCritSolutons.csv");
            FileWriter crits = new FileWriter("Results/OC_" + regionFile + "-Criteria.csv");

            //BufferedWriter out = new BufferedWriter(allSolutionPairs);
            System.out.println("Running Solver");

            //8 hours  28800000
            //12 hours 43200000
            //13 hours 46800000
            //14 hours 50400000


            int l = 1;
            long startTime = System.currentTimeMillis(); //fetch starting time
            try {
                while (solver.solve()) {
                    allSolutionPairs.write("Solution: " + l + " Crit0: "+sum0.getValue()+ " TimeStamp: " + (System.currentTimeMillis() - startTime) + "ms\n");

                    for (int i = 0; i < nodes.length; i++) {
                        if (nodes[i].valid) {
                            allSolutionPairs.write(nodes[i].externalId + "," + ugs[i].getValue() + "\n");
                        }
                    }
                    crits.write(sum0.getValue()+"\n");
                    crits.flush();
                    allSolutionPairs.flush();
                    System.out.println("Found solution-" + l);
                    l++;
                }
            } catch (OutOfMemoryError e) {
                pairCrits.write("Reached Exception after " + (System.currentTimeMillis() - startTime) + " milliseconds");
            }

            crits.close();

            allSolutionPairs.close();

            pairCrits.close();
        }
    }
}
