import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.objective.ParetoOptimizer;
import org.chocosolver.solver.variables.IntVar;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class MiniTest {
    public static void giveDomains (Model model, IntVar[] ugs, String dir, IntVar[] woodYield, IntVar[] soillossTotal,
                                    IntVar[] perc_r_Total, MUG[] nodes, int R_option){
        int index = 0;
        try {
            File allUg = new File(dir + "/ugs_init.txt");
            Scanner readerUg = new Scanner(allUg);

            File myObjWood = new File(dir + "/wood_total_init.txt");
            Scanner myReaderWood = new Scanner(myObjWood);

            File myObjSoil = new File(dir + "/soilloss_total_init.txt");
            Scanner myReaderSoil = new Scanner(myObjSoil);

            String percR_dir;
            int R;
            switch(R_option) {
                case 1:
                    percR_dir = "/perc_r5_total_init.txt";
                    R = 5;
                    break;
                case 2:
                    percR_dir = "/perc_r10_total_init.txt";
                    R = 10;
                    break;
                default:
                    percR_dir = "/perc_r0_total_init.txt";
                    R = 0;
            }


            File myObjPerc_r = new File(dir + percR_dir);
            Scanner myReaderPerc_r = new Scanner(myObjPerc_r);

            while (readerUg.hasNextLine()) {
                String dataUg = readerUg.nextLine();
                String dataWood = myReaderWood.nextLine();
                String dataSoil = myReaderSoil.nextLine();

                String dataPerc_r = myReaderPerc_r.nextLine();



                if(nodes[index].valid) {
                    String[] str_split = dataUg.split(",", 0);

                    String[] str_split_wood = dataWood.split(",", 0);

                    String[] str_split_soil = dataSoil.split(",", 0);

                    String[] str_split_percr = dataPerc_r.split(",", 0);



                    int size = str_split.length;
                    int[] toInsert = new int[size];
                    int[] toInsertWood = new int[size];
                    int[] toInsertSoil = new int[size];

                    int[] toInsertPercR = new int[size];

                    for (int i = 0; i < size; i++) {
                        toInsert[i] = Integer.parseInt(str_split[i]);
                        toInsertWood[i] = (int) Float.parseFloat(str_split_wood[i]);
                        toInsertSoil[i] = (int) Float.parseFloat(str_split_soil[i]);

                        //toInsertPercR[i] = (int) Float.parseFloat(str_split_percr[i]);

                    }

                    ugs[index] = model.intVar("UG_" + (index), toInsert);
                    woodYield[index] = model.intVar("Wood_Yield_" + (index), toInsertWood);
                    soillossTotal[index] = model.intVar("Soilloss_" + (index), toInsertSoil);

                    //perc_r_Total[index] = model.intVar("RiskPercentileR"+R+":" + (index), toInsertPercR);


                }
                else {
                    ugs[index] = model.intVar("UG_"+(index), 0);
                    woodYield[index] = model.intVar("Wood_Yield_"+(index), 0);
                    soillossTotal[index] = model.intVar("Soilloss_" + (index), 0);

                    //perc_r_Total[index] = model.intVar("RiskPercentileR"+R+":" + (index), 0);


                }

                index++;
            }

            readerUg.close();
            myReaderWood.close();
            myReaderSoil.close();

            myReaderPerc_r.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        int areaLimit = Integer.parseInt(args[0]);
        String fileDirectory = args[1];

        String criterion = args[2];

        int R_option = 0;
        int R = 0;

        Model m = new Model("Forest Management");

        BufferedReader reader = new BufferedReader(new FileReader(fileDirectory + "/ugs_init.txt"));

        int nUgs = 0;
        while (reader.readLine() != null) nUgs++;
        reader.close();

        MUG[] nodes = new MUG[nUgs];
        MUG.fillArray(nodes, fileDirectory);

        IntVar[] ugs = new IntVar[nUgs];

        IntVar[] woodYield = new IntVar[nUgs];

        IntVar[] soillossTotal = new IntVar[nUgs];

        IntVar[] perc_r_Total = new IntVar[nUgs];


        giveDomains(m, ugs, fileDirectory, woodYield, soillossTotal, perc_r_Total, nodes,  R_option); // reads the ugs_init file and initializes each variable with its possible prescription values as domain


        System.out.println("running");


        for(int ugIndex = 0; ugIndex < nodes.length; ugIndex++) {
            if(nodes[ugIndex].valid) {
                new Constraint("Area Limit Constraint", new MiniPropagator(ugIndex, nodes, ugs, areaLimit, woodYield)).post();
            }
        }

        int valids = 0;
        for(int i = 0; i < nodes.length; i++){
            if(nodes[i].valid) {
                //System.out.println(ugs[i]);
                valids++;
            }
        }

        for(int i = 0; i < nodes.length; i++){
            if(nodes[i].valid) {
                IntVar prescIndex = m.intVar(0, 255);
                m.element(ugs[i], nodes[i].presc, prescIndex).post();
                m.element(woodYield[i], nodes[i].wood_total, prescIndex).post();
                m.element(soillossTotal[i], nodes[i].soilloss_total, prescIndex).post();
                //m.element(perc_r_Total[i], nodes[i].perc_r0_total, prescIndex).post();
            }
            else{
                m.arithm(woodYield[i], "=", 0).post();
                m.arithm(soillossTotal[i], "=", 0).post();


                //m.arithm(perc_r_Total[i], "=", 0).post();

            }
        }

        IntVar woodSum = m.intVar("WOOD_SUM", 0, 99999999);
        IntVar soilSum = m.intVar("SOIL_SUM", -99999999, 99999999);

        IntVar percRSum = m.intVar("RiskPercentile_R"+R, 0, 99999999);

        m.sum(woodYield,"=", woodSum).post();
        m.sum(soillossTotal,"=", soilSum).post();
        //m.sum(perc_r_Total,"=", percRSum).post();

        if(criterion.equalsIgnoreCase("single")) {
            m.setObjective(Model.MAXIMIZE, woodSum);
            //m.setObjective(Model.MINIMIZE, soilSum);
            //m.setObjective(Model.MINIMIZE, percRSum);

            Solver s = m.getSolver();

            if (s.solve()) {

                FileWriter outputPairs = new FileWriter("outputPairs.csv");
                FileWriter woodByYear = new FileWriter("woodByYear.csv");

                for (int i = 0; i < ugs.length; i++) {
                    if (nodes[i].valid) {
                        //System.out.println(woodYield[i]);
                        System.out.println(ugs[i] + ", wy:" + woodYield[i] + ", sl:" + soillossTotal[i]);
                        outputPairs.write(nodes[i].externalId + "," + ugs[i].getValue() + "\n");
                    }

                }
                outputPairs.close();
                System.out.println(woodSum);
                System.out.println(soilSum);
                //System.out.println(percRSum);


            }
        }
        else {

            //Multi Criterion with Pareto

            IntVar percToMaximize = m.intVar(-9999999,9999999);
            //m.arithm(percToMaximize, "=", one, "-", percRSum).post();

            ParetoOptimizer po = new ParetoOptimizer(Model.MAXIMIZE, new IntVar[]{woodSum, soilSum});

            Solver solver = m.getSolver();
            solver.plugMonitor(po);


            FileWriter nonParetoWS = new FileWriter("nonParetoWS.csv");
            FileWriter nonParetoWR = new FileWriter("nonParetoWR.csv");
            FileWriter nonParetoSR = new FileWriter("nonParetoSR.csv");
            FileWriter nonParetoWSR = new FileWriter("nonParetoWSR.csv");


            // optimization
            int l = 1;
            try{
                while(solver.solve())
                    {
                        nonParetoWS.write(woodSum.getValue()+","+soilSum.getValue()+"\n");
                        nonParetoWR.write(woodSum.getValue()+","+percToMaximize.getValue()+"\n");
                        nonParetoSR.write(soilSum.getValue()+","+percToMaximize.getValue()+"\n");
                        nonParetoWSR.write(woodSum.getValue()+","+soilSum.getValue()+","+percRSum.getValue()+"\n");
                        l++;
                    }
                    nonParetoWS.write(woodSum.getValue()+","+soilSum.getValue()+"\n");
                    nonParetoWR.write(woodSum.getValue()+","+percToMaximize.getValue()+"\n");
                    nonParetoSR.write(soilSum.getValue()+","+percToMaximize.getValue()+"\n");
                    nonParetoWSR.write(woodSum.getValue()+","+soilSum.getValue()+","+percRSum.getValue()+"\n");
                }
            catch (OutOfMemoryError e){
                System.out.println("Reached Exception");
            }

            nonParetoWS.close();
            nonParetoWR.close();
            nonParetoSR.close();
            nonParetoWSR.close();

            List<Solution> paretoFront = po.getParetoFront();

            System.out.println("The pareto front has " + paretoFront.size() + " solutions : ");
            FileWriter outputPairs = new FileWriter("outputPairsMulti.csv");

            FileWriter paretoWS = new FileWriter("paretoWS.csv");
            FileWriter paretoWR = new FileWriter("paretoWR.csv");
            FileWriter paretoSR = new FileWriter("paretoSR.csv");
            FileWriter paretoWSR = new FileWriter("paretoWSR.csv");


            int l2 = 0;
            for (Solution so : paretoFront) {
                System.out.println("Wood = " + so.getIntVal(woodSum) + ", Soil = " + so.getIntVal(soilSum));

                outputPairs.write("Pareto Solution: " + l2 + ", Wood: "+so.getIntVal(woodSum)+", Soil:"+so.getIntVal(soilSum));

                for (int i = 0; i < ugs.length; i++) {
                    System.out.println(i+"_"+so.getIntVal(ugs[i]) + ", wy:" + so.getIntVal(woodYield[i]) + ", sl:" + so.getIntVal(soillossTotal[i]));
                    outputPairs.write(nodes[i].externalId + "," +so.getIntVal(ugs[i])  + "\n");
                }

                //PARETO POINTS
                paretoWS.write(so.getIntVal(woodSum)+","+so.getIntVal(soilSum)+"\n");
                //paretoWR.write(so.getIntVal(woodSum)+","+so.getIntVal(percRSum)+"\n");
                //paretoSR.write(so.getIntVal(soilSum)+","+so.getIntVal(percRSum)+"\n");
                //paretoWSR.write(so.getIntVal(woodSum)+","+so.getIntVal(soilSum)+","+so.getIntVal(percRSum)+"\n");



                l2++;
            }
            outputPairs.close();
            paretoWS.close();
            paretoWR.close();
            paretoSR.close();
            paretoWSR.close();
            System.out.println(l + " solutions");
        }
    }
}
