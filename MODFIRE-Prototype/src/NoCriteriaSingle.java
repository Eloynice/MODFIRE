import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class NoCriteriaSingle {

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

                    ugs[index] = model.intVar(toInsert);
                }
                else {
                    ugs[index] = model.intVar(0);
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

        /*for(int i = 0; i < nodes.length;i++) {
            if(nodes[i].valid) {
                IntVar prescIndex = m.intVar(0, nodes[i].presc.length);

                m.element(ugs[i], nodes[i].presc, prescIndex).post();
            }
        }*/

        System.out.println("Setting up Constraints");

        for(int ugIndex = 0; ugIndex < nodes.length; ugIndex++) { //loops through every UG
            //the propagator takes as parameters the index of the UG we are starting out from
            //the nodes with all the info, the constraint variable array and the area limit
            if(nodes[ugIndex].valid && !nodes[ugIndex].noAdjacencies) {
                new Constraint("Area Limit Constraint", new CustomPropagatorFloat(ugIndex, nodes, ugs, areaLimit)).post();
            }
        }

        System.out.println("Running solver");

        Solver s = m.getSolver();
        //s.setSearch(Search.activityBasedSearch(ugs));
        //s.setSearch(Search.minDomLBSearch(ugs));
        long startTime = System.currentTimeMillis(); //fetch starting time

        s.solve();

        System.out.println("Found solution! Timestamp: " + (System.currentTimeMillis() - startTime) + "\n");

        FileWriter outputPairs = new FileWriter("Results/" + "NC-"+regionFile + "-Single.csv");
        outputPairs.write("Timestamp: " + (System.currentTimeMillis() - startTime) + "\n");

        for (int i = 0; i < ugs.length; i++) {
            if (nodes[i].valid) {
                outputPairs.write(nodes[i].externalId + "," + ugs[i].getValue() + "\n");
            }
        }
        outputPairs.close();

    }
}
