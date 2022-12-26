import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.objective.ParetoOptimizer;
import org.chocosolver.solver.variables.IntVar;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainTime {

    public static void giveDomains (Model model, IntVar[] ugs, String dir, IntVar[] crit0, IntVar[] crit1,
                                    IntVar[] crit2, IntVar[] crit3, IntVar[] crit4, IntVar[] crit5,
                                    IntVar[] crit6, IntVar[] crit7, IntVar[] crit8,IntVar[] crit9,IntVar[] crit10, UG[] nodes){
        int index = 0;
        try {
            File allUg = new File(dir + "/ugs_init.txt");
            Scanner readerUg = new Scanner(allUg);

            File critFile0 = new File(dir + "/crit_file0.txt");
            Scanner reader0 = new Scanner(critFile0);

            File critFile1 = new File(dir + "/crit_file1.txt");
            Scanner reader1 = new Scanner(critFile1);

            File critFile2 = new File(dir + "/crit_file2.txt");
            Scanner reader2 = new Scanner(critFile2);

            File critFile3 = new File(dir + "/crit_file3.txt");
            Scanner reader3 = new Scanner(critFile3);

            File critFile4 = new File(dir + "/crit_file4.txt");
            Scanner reader4 = new Scanner(critFile4);

            File critFile5 = new File(dir + "/crit_file5.txt");
            Scanner reader5 = new Scanner(critFile5);

            File critFile6 = new File(dir + "/crit_file6.txt");
            Scanner reader6 = new Scanner(critFile6);

            File critFile7 = new File(dir + "/crit_file7.txt");
            Scanner reader7 = new Scanner(critFile7);

            File critFile8 = new File(dir + "/crit_file8.txt");
            Scanner reader8 = new Scanner(critFile8);

            File critFile9 = new File(dir + "/crit_file9.txt");
            Scanner reader9 = new Scanner(critFile9);

            File critFile10 = new File(dir + "/crit_file10.txt");
            Scanner reader10 = new Scanner(critFile10);

            while (readerUg.hasNextLine()) {
                String dataUg = readerUg.nextLine();

                String data0 = reader0.nextLine();
                String data1 = reader1.nextLine();
                String data2 = reader2.nextLine();
                String data3 = reader3.nextLine();
                String data4 = reader4.nextLine();
                String data5 = reader5.nextLine();
                String data6 = reader6.nextLine();
                String data7 = reader7.nextLine();
                String data8 = reader8.nextLine();
                String data9 = reader9.nextLine();
                String data10 = reader10.nextLine();




                if(nodes[index].valid) {
                    String[] str_split = dataUg.split(",", 0);

                    String[] str_split0 = data0.split(",", 0);
                    String[] str_split1 = data1.split(",", 0);
                    String[] str_split2 = data2.split(",", 0);
                    String[] str_split3 = data3.split(",", 0);
                    String[] str_split4 = data4.split(",", 0);
                    String[] str_split5 = data5.split(",", 0);
                    String[] str_split6 = data6.split(",", 0);
                    String[] str_split7 = data7.split(",", 0);
                    String[] str_split8 = data8.split(",", 0);
                    String[] str_split9 = data9.split(",", 0);
                    String[] str_split10 = data10.split(",", 0);



                    int size = str_split.length;
                    int[] toInsert = new int[size];
                    int[] toInsert0 = new int[size];
                    int[] toInsert1 = new int[size];
                    int[] toInsert2 = new int[size];
                    int[] toInsert3 = new int[size];
                    int[] toInsert4 = new int[size];
                    int[] toInsert5 = new int[size];
                    int[] toInsert6 = new int[size];
                    int[] toInsert7 = new int[size];
                    int[] toInsert8 = new int[size];
                    int[] toInsert9 = new int[size];
                    int[] toInsert10 = new int[size];


                    for (int i = 0; i < size; i++) {
                        toInsert[i] = Integer.parseInt(str_split[i]);
                        toInsert0[i] = (int) Float.parseFloat(str_split0[i]);
                        toInsert1[i] = (int) Float.parseFloat(str_split1[i]);

                        toInsert2[i] = (int) Float.parseFloat(str_split2[i]);
                        toInsert3[i] = (int) Float.parseFloat(str_split3[i]);
                        toInsert4[i] = (int) Float.parseFloat(str_split4[i]);
                        toInsert5[i] = (int) Float.parseFloat(str_split5[i]);
                        toInsert6[i] = (int) Float.parseFloat(str_split6[i]);
                        toInsert7[i] = (int) Float.parseFloat(str_split7[i]);
                        toInsert8[i] = (int) Float.parseFloat(str_split8[i]);
                        toInsert9[i] = (int) Float.parseFloat(str_split9[i]);
                        toInsert10[i] = (int) Float.parseFloat(str_split10[i]);


                    }

                    ugs[index] = model.intVar("UG_" + (index), toInsert);
                    crit0[index] = model.intVar("Crit0_" + (index), toInsert0);
                    crit1[index] = model.intVar("Crit1_" + (index), toInsert1);

                    crit2[index] = model.intVar("Crit2_" + (index), toInsert2);
                    crit3[index] = model.intVar("Crit3_" + (index), toInsert3);
                    crit4[index] = model.intVar("Crit4_" + (index), toInsert4);
                    crit5[index] = model.intVar("Crit5_" + (index), toInsert5);
                    crit6[index] = model.intVar("Crit6_" + (index), toInsert6);
                    crit7[index] = model.intVar("Crit7_" + (index), toInsert7);
                    crit8[index] = model.intVar("Crit8_" + (index), toInsert8);
                    crit9[index] = model.intVar("Crit9_" + (index), toInsert9);
                    crit10[index] = model.intVar("Crit10_" + (index), toInsert10);





                }
                else {
                    ugs[index] = model.intVar("UG_"+(index), 0);
                    crit0[index] = model.intVar("Crit0"+(index), 0);
                    crit1[index] = model.intVar("Crit1" + (index), 0);

                    crit2[index] = model.intVar("Crit2" + (index), 0);
                    crit3[index] = model.intVar("Crit3" + (index), 0);
                    crit4[index] = model.intVar("Crit4" + (index), 0);
                    crit5[index] = model.intVar("Crit5" + (index), 0);
                    crit6[index] = model.intVar("Crit6" + (index), 0);
                    crit7[index] = model.intVar("Crit7" + (index), 0);
                    crit8[index] = model.intVar("Crit8" + (index), 0);
                    crit9[index] = model.intVar("Crit9" + (index), 0);
                    crit10[index] = model.intVar("Crit10" + (index), 0);

                }

                index++;
            }

            readerUg.close();
            reader0.close();
            reader1.close();
            reader2.close();
            reader3.close();
            reader4.close();
            reader5.close();
            reader6.close();
            reader7.close();
            reader8.close();
            reader9.close();
            reader10.close();


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

        String[] varNames = new String[]{"Crit0","Crit1","Crit2","Crit3","Crit4","Crit5","Crit6","Crit7","Crit8","Crit9","Crit10"};
        ArrayList<Boolean> flags = new ArrayList<>();

        for(int i = 0; i < 11; i++) {
            flags.add(false);
        }

        int toOptimizeLen = 0;
        for(int i = 5; i < args.length; i++) {
            flags.set(Integer.parseInt(args[i]), true);
            toOptimizeLen++;
        }

        Model m = new Model("Forest Management");

        BufferedReader reader = new BufferedReader(new FileReader(fileDirectory + "/ugs_init.txt"));

        int nUgs = 0;
        while (reader.readLine() != null) nUgs++;
        reader.close();

        UG[] nodes = new UG[nUgs];
        //if 0 ignore
        int minBorder = Integer.parseInt(args[2]);
        if(minBorder <= 0)
            UG.fillArray(nodes, fileDirectory, 0);
        else
            UG.fillArray(nodes, fileDirectory, minBorder);

        for(int i = 0; i < nodes.length; i++){
            if(!islandUGs.contains(nodes[i].externalId)){
                nodes[i].valid = false;
            }
        }

        IntVar[] ugs = new IntVar[nUgs]; // same for the constraint variable array


        IntVar[] crit0 = new IntVar[nUgs];

        IntVar[] crit1 = new IntVar[nUgs];

        IntVar[] crit2 = new IntVar[nUgs];

        IntVar[] crit3 = new IntVar[nUgs];
        IntVar[] crit4 = new IntVar[nUgs];
        IntVar[] crit5 = new IntVar[nUgs];
        IntVar[] crit6 = new IntVar[nUgs];
        IntVar[] crit7 = new IntVar[nUgs];
        IntVar[] crit8 = new IntVar[nUgs];
        IntVar[] crit9 = new IntVar[nUgs];
        IntVar[] crit10 = new IntVar[nUgs];

        giveDomains(m, ugs, fileDirectory, crit0, crit1, crit2, crit3, crit4, crit5, crit6,
                crit7, crit8, crit9, crit10, nodes); // reads the ugs_init file and initializes each variable with its possible prescription values as domain


        System.out.println("running");


        for(int ugIndex = 0; ugIndex < nodes.length; ugIndex++) { //loops through every UG
            //the propagator takes as parameters the index of the UG we are starting out from
            //the nodes with all the info, the constraint variable array and the area limit
            if(nodes[ugIndex].valid) {
                new Constraint("Area Limit Constraint", new CustomPropagator2(ugIndex, nodes, ugs, areaLimit)).post();
            }
        }


        int valids = 0;
        for(int i = 0; i < nodes.length; i++){
            if(nodes[i].valid) {
                valids++;
            }
        }


        for(int i = 0; i < nodes.length; i++){
            if(nodes[i].valid) {
                IntVar prescIndex = m.intVar(0, 255);

                m.element(ugs[i], nodes[i].presc, prescIndex).post();

                m.element(crit0[i], nodes[i].crit0, prescIndex).post();
                m.element(crit1[i], nodes[i].crit1, prescIndex).post();
                m.element(crit2[i], nodes[i].crit2, prescIndex).post();
                m.element(crit3[i], nodes[i].crit3, prescIndex).post();
                m.element(crit4[i], nodes[i].crit4, prescIndex).post();
                m.element(crit5[i], nodes[i].crit5, prescIndex).post();
                m.element(crit6[i], nodes[i].crit6, prescIndex).post();
                m.element(crit7[i], nodes[i].crit7, prescIndex).post();
                m.element(crit8[i], nodes[i].crit8, prescIndex).post();
                m.element(crit9[i], nodes[i].crit9, prescIndex).post();
                m.element(crit10[i], nodes[i].crit10, prescIndex).post();

            }

            else{
                m.arithm(crit0[i], "=", 0).post();
                m.arithm(crit1[i], "=", 0).post();
                m.arithm(crit2[i], "=", 0).post();

                m.arithm(crit3[i], "=",0).post();
                m.arithm(crit4[i], "=",0).post();
                m.arithm(crit5[i], "=",0).post();
                m.arithm(crit6[i], "=",0).post();
                m.arithm(crit7[i], "=",0).post();
                m.arithm(crit8[i], "=",0).post();
                m.arithm(crit9[i], "=",0).post();
                m.arithm(crit10[i], "=",0).post();


            }
        }

        IntVar sum0 = m.intVar("CRIT0_SUM",-99999999, 99999999);
        IntVar sum1 = m.intVar("CRIT1_SUM", -99999999, 99999999);

        IntVar sum2 = m.intVar("CRIT2_SUM", -99999999, 99999999);
        IntVar sum3 = m.intVar("CRIT3_SUM", -99999999, 99999999);
        IntVar sum4 = m.intVar("CRIT4_SUM", -99999999, 99999999);
        IntVar sum5 = m.intVar("CRIT5_SUM", -99999999, 99999999);
        IntVar sum6 = m.intVar("CRIT6_SUM", -99999999, 99999999);
        IntVar sum7 = m.intVar("CRIT7_SUM", -99999999, 99999999);
        IntVar sum8 = m.intVar("CRIT8_SUM", -99999999, 99999999);
        IntVar sum9 = m.intVar("CRIT9_SUM", -99999999, 99999999);
        IntVar sum10 = m.intVar("CRIT10_SUM", -99999999, 99999999);


        if(flags.get(0))
            m.sum(crit0,"=", sum0).post();
        if(flags.get(1))
            m.sum(crit1,"=", sum1).post();
        if(flags.get(2))
            m.sum(crit2,"=", sum2).post();
        if(flags.get(3))
            m.sum(crit3, "=", sum3).post();
        if(flags.get(4))
            m.sum(crit4, "=", sum4).post();
        if(flags.get(5))
            m.sum(crit5, "=", sum5).post();
        if(flags.get(6))
            m.sum(crit6, "=", sum6).post();
        if(flags.get(7))
            m.sum(crit7, "=", sum7).post();
        if(flags.get(8))
            m.sum(crit8, "=", sum8).post();
        if(flags.get(9))
            m.sum(crit9, "=", sum9).post();
        if(flags.get(10))
            m.sum(crit10, "=", sum10).post();


        IntVar[] toOptimize = new IntVar[toOptimizeLen];
        IntVar[] allSums = new IntVar[]{sum0, sum1, sum2, sum3,sum4,sum5,sum6,sum7,sum8,sum9,sum10};
        IntVar[][] allTotals = new IntVar[][]{crit0, crit1,crit2,crit3,crit4,crit5,crit6,crit7,crit8,crit9,crit10};

        //Multi Criterion with Pareto

        int index = 0;
        for(int i = 0; i<flags.toArray().length; i++){
            if(flags.get(i)){
                toOptimize[index] = allSums[i];
                index++;
            }
        }

        ParetoOptimizer po = new ParetoOptimizer(Model.MAXIMIZE, toOptimize);

        Solver solver = m.getSolver();
        solver.plugMonitor(po);

        FileWriter nonPareto = new FileWriter("nonPareto.csv");
        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i)) {
                nonPareto.write(varNames[i]+",");
            }
        }
        nonPareto.write("end\n");

        FileWriter allSolutionPairs = new FileWriter("allSolutionPairs.csv");

        // optimization
        //(System.currentTimeMillis()-startTime)<18000000
        int l = 1;
        long startTime = System.currentTimeMillis(); //fetch starting time
        try{
            while(solver.solve() & (System.currentTimeMillis()-startTime)<28800000)
            {
                //System.out.println(l);
                for (int i = 0; i < flags.size(); i++) {
                    if(flags.get(i))
                        nonPareto.write(allSums[i].getValue()+",");
                }
                nonPareto.write("end\n");

                allSolutionPairs.write(l+", ");
                for (int i = 0; i < flags.size(); i++) {
                    if(flags.get(i))
                        allSolutionPairs.write(varNames[i] +":"+ allSums[i].getValue()+",");
                }
                allSolutionPairs.write("end\n");

                for(int i = 0; i < nodes.length; i++){
                    if(nodes[i].valid){
                        allSolutionPairs.write(nodes[i].externalId+","+ugs[i].getValue()+"\n");
                    }
                }
                l++;

                for(int i = 0; i < nodes.length; i++){
                    if(nodes[i].valid){

                        System.out.print(ugs[i] + ", ");

                        for (int j = 0; j < flags.size(); j++) {
                            if(flags.get(j))
                                System.out.print(varNames[j] +":"+ allTotals[j][i].getValue()+",");
                        }
                        System.out.println("end");
                    }
                }

                l++;
            }
        }
        catch (OutOfMemoryError e){
            System.out.println("Reached Exception");
        }

        nonPareto.close();
        //allSolutionPairs.close();

        List<Solution> paretoFront = po.getParetoFront();

        System.out.println("The pareto front has " + paretoFront.size() + " solutions : ");
        FileWriter outputPairs = new FileWriter("outputPairsMulti.csv");

        FileWriter pareto = new FileWriter("pareto.csv");

        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i)) {
                pareto.write(varNames[i]+",");
            }
        }

        pareto.write("end\n");

        int l2 = 0;
        for (Solution so : paretoFront) {

            for (int i = 0; i < flags.size(); i++) {
                if (flags.get(i))
                    System.out.print(varNames[i]+"="+so.getIntVal(allSums[i])+",");
            }
            System.out.println("end");

            outputPairs.write("Pareto Solution: " + l2+",");
            for (int i = 0; i < flags.size(); i++) {
                if (flags.get(i))
                    outputPairs.write(varNames[i]+":"+so.getIntVal(allSums[i])+",");
            }

            outputPairs.write("end\n");

            for (int i = 0; i < ugs.length; i++) {
                if (nodes[i].valid) {

                    System.out.print(i+"_"+so.getIntVal(ugs[i])+", ");

                    for (int j = 0; j < flags.size(); j++) {
                        if (flags.get(j))
                            System.out.print(varNames[j]+":"+so.getIntVal(allTotals[j][i])+",");
                    }
                    System.out.println("end");

                    outputPairs.write(nodes[i].externalId + "," +so.getIntVal(ugs[i])  + "\n");
                }
            }

            for (int i = 0; i < flags.size(); i++) {
                if(flags.get(i))
                    pareto.write(so.getIntVal(allSums[i])+",");
            }
            pareto.write("end\n");


            l2++;
        }
        outputPairs.close();

        pareto.close();

        System.out.println(l + " solutions");
    }
}
