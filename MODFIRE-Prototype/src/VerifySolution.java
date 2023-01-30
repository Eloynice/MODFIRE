import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class VerifySolution {

    public static ArrayList<ArrayList<Integer>> violationGraphs = new ArrayList<>();
    public static ArrayList<Integer> sumViolations = new ArrayList<Integer>();
    public static ArrayList<Integer> yearViolations = new ArrayList<Integer>();
    public static ArrayList<Integer> currentViolation = new ArrayList<Integer>();

    public static int returnInternal(UG[] nodes, int external){
        for(int i = 0; i < nodes.length; i++){
            if(nodes[i].externalId == external)
                return i;
        }
        return 0;
    }

    public static int indexOfPresc(UG[] nodes, int internal, int presc){
        for(int i = 0; i < nodes[internal].presc.length; i++){
            if(nodes[internal].presc[i] == presc)
                return i;
        }
        return 0;
    }

    public static int g(int first, int ugIndex, int year, int sum, UG[] nodes, int[] prescIndexes) throws Exception{
        if(ugIndex == first){
            if(nodes[ugIndex].valid && !nodes[ugIndex].treatedThisYear) {
                nodes[ugIndex].treatedThisYear = true;
                sum = sum + (int)nodes[ugIndex].area;

                currentViolation.add(nodes[ugIndex].externalId);

                for (int rec = 0; rec < nodes[ugIndex].adj.length; rec++) {
                    if(nodes[nodes[ugIndex].adj[rec]].valid && !nodes[nodes[ugIndex].adj[rec]].treatedThisYear) {
                        sum = g(-1, nodes[ugIndex].adj[rec], year, sum, nodes, prescIndexes);
                        nodes[nodes[ugIndex].adj[rec]].treatedThisYear = true;
                    }
                }
            }
        }
        else {
            if (nodes[ugIndex].valid && !nodes[ugIndex].treatedThisYear) {
                nodes[ugIndex].treatedThisYear = true;

                for(int t = 0; t < nodes[ugIndex].years[prescIndexes[ugIndex]].length; t++){
                    if(nodes[ugIndex].years[prescIndexes[ugIndex]][t] == year){
                        sum = sum + (int)nodes[ugIndex].area;

                        if(sum > 50){
                            nodes[ugIndex].lasYearOfViolation = year;
                        }
                        currentViolation.add(nodes[ugIndex].externalId);

                        for (int rec = 0; rec < nodes[ugIndex].adj.length; rec++) {

                            if(nodes[nodes[ugIndex].adj[rec]].valid && !nodes[nodes[ugIndex].adj[rec]].treatedThisYear) {
                                sum = g(-1, nodes[ugIndex].adj[rec], year, sum, nodes, prescIndexes);
                                nodes[nodes[ugIndex].adj[rec]].treatedThisYear = true;
                            }
                        }

                    }
                }

            }
        }

        return sum;
    }


    public static void insertCurrentViolation(int sum, int year){
        Collections.sort(currentViolation);
        boolean newViolation = true;

        if (violationGraphs.isEmpty()) {
            violationGraphs.add((ArrayList) currentViolation.clone());
            sumViolations.add(sum);
            yearViolations.add(year);
            newViolation = false;
        }
        else {
            for(int i = 0; i < violationGraphs.size(); i++){
                if (currentViolation.equals(violationGraphs.get(i)) && yearViolations.get(i) == year) {
                    newViolation = false;
                    break;
                }
            }
        }

        if(newViolation) {
            // System.out.println(violationGraphs.toString());
            //System.out.println(yearViolations);
            violationGraphs.add((ArrayList) currentViolation.clone());
            sumViolations.add(sum);
            yearViolations.add(year);
        }
    }


    // same as gladed but without the "isInstatiated" checks because it doesn't call gPropagate
    public static int glade(int ugIndex, int period, UG[] nodes, int[] prescIndexes) throws Exception{
        return g(ugIndex, ugIndex, period, 0, nodes, prescIndexes);
    }

    public static void writeSolution(String reg, UG[] nodes, int solIndex, int regLen, FileWriter outputFile) throws IOException {
        int s = solIndex*regLen + 1;
        int e = s + regLen-1;
        String pairLine = null;



        BufferedReader readerPar = new BufferedReader(new FileReader("RegionSolutions/"+reg));
        for (int i = 0; i < s; i++) {
            readerPar.readLine();
        }

        for(int i = s; i < e; i++ ) {
            pairLine = readerPar.readLine();
            String P = pairLine.substring(0, 1);
            if(P.compareToIgnoreCase("P") == 0){
                continue;
            }
            else{
                int externalId = Integer.parseInt(pairLine.split(",")[0]);
                int presc = Integer.parseInt(pairLine.split(",")[1]);
                outputFile.write(externalId+","+presc+"\n");

            }

        }
        readerPar.close();

    }

    public static void readRegionFile(String reg, UG[] nodes, int solIndex, int regLen, int[] prescIndexes, int[] intExt) throws IOException {

        int s = solIndex*regLen + 1;
        int e = s + regLen-1;
        String pairLine = null;


        BufferedReader readerPar = new BufferedReader(new FileReader("RegionSolutions/"+reg));
        for (int i = 0; i < s; i++) {
            readerPar.readLine();
        }

        for(int i = s; i < e; i++ ) {
            pairLine = readerPar.readLine();
            String P = pairLine.substring(0, 1);
            if(P.compareToIgnoreCase("P") == 0){
                continue;
            }
            else{
                int externalId = Integer.parseInt(pairLine.split(",")[0]);
                int presc = Integer.parseInt(pairLine.split(",")[1]);

                int internalId = returnInternal(nodes, externalId);
                int prescIndex = indexOfPresc(nodes, internalId, presc);

                nodes[internalId].valid = true;

                intExt[internalId] = externalId;
                prescIndexes[internalId] = prescIndex;

            }

        }
        readerPar.close();
    }

    public static void main(String[] args) throws Exception {


        ArrayList<Integer> islandUGs = new ArrayList<>();
        File island = new File("subregions/Full");
        Scanner islandReader = new Scanner(island);
        while (islandReader.hasNextLine()) {
            islandUGs.add(Integer.parseInt(islandReader.nextLine()));
        }

        BufferedReader reader = new BufferedReader(new FileReader("res" + "/ugs_init.txt"));

        int nUgs = 0;
        while (reader.readLine() != null) nUgs++;
        reader.close();

        UG[] nodes = new UG[nUgs];

        int minBorder = 50;
        UG.setupInternalIds(nodes, "res");

        if (minBorder <= 0)
            UG.fillVerificationArray(nodes, "res", 0);
        else
            UG.fillVerificationArray(nodes, "res", minBorder);

        for(int i = 0; i < nodes.length; i++){
            if(islandUGs.contains(nodes[i].externalId)){
                //nodes[i].valid = true;
                continue;
            }
        }

        int solutionNumber = 1;

        File solutions = new File("work-2.vmt");
        Scanner solutionsReader = new Scanner(solutions);

        solutionsReader.nextLine();
        solutionsReader.nextLine();
        solutionsReader.nextLine();
        while(solutionsReader.hasNextLine()){ //for each solution in that file
            currentViolation.clear();
            violationGraphs.clear();
            sumViolations.clear();
            yearViolations.clear();


            int[] prescIndexes = new int[nUgs];
            int[] intExt = new int[nUgs];


            String line = solutionsReader.nextLine();
            String[] arr = line.split("\t");
            String SolName = arr[0];
            float[] Criteria = {Float.parseFloat(arr[1]), Float.parseFloat(arr[2]), Float.parseFloat(arr[3])};

            int parSol = Integer.parseInt(arr[4]);
            int penSol = Integer.parseInt(arr[5]);
            int paiwSol = Integer.parseInt(arr[6]);
            int paieSol = Integer.parseInt(arr[7]);
            int paiiSol = Integer.parseInt(arr[8]);

            int lenPar = 193+1; //real 193
            int lenPen = 526+1; //real 527
            int lenPw = 482+1; // real 487
            int lenPe = 86+1; //real 86
            int lenPi = 109+1; //real 113

            readRegionFile("Par-ParetoSolutions.csv", nodes, parSol, lenPar, prescIndexes, intExt);
            readRegionFile("Pen-ParetoSolutions.csv", nodes, penSol, lenPen, prescIndexes, intExt);
            readRegionFile("PaivaWest-ParetoSolutions.csv", nodes, paiwSol, lenPw, prescIndexes, intExt);
            readRegionFile("PaivaEast-ParetoSolutions.csv", nodes, paieSol, lenPe, prescIndexes, intExt);
            readRegionFile("PaivaIslands-ParetoSolutions.csv", nodes, paiiSol, lenPi, prescIndexes, intExt);


            int noUse = 0;
            for (int internalId = 0; internalId < nodes.length; internalId++) {
                if(nodes[internalId].noAdjacencies && nodes[internalId].valid) {
                    noUse++;
                }
                else if(nodes[internalId].valid){

                    for (int i = 0; i < nodes[internalId].years[prescIndexes[internalId]].length; i++) {
                        int currentYear = nodes[internalId].years[prescIndexes[internalId]][i];

                        if (currentYear == -1) {
                            noUse++;
                        }
                        else {
                            int sum = glade(internalId, currentYear, nodes, prescIndexes);
                            if(sum > 50){
                                insertCurrentViolation(sum, currentYear);
                            }
                            currentViolation.clear();

                            for (UG node : nodes) node.treatedThisYear = false;
                        }
                    }
                }
            }
            if (violationGraphs.isEmpty())
                System.out.println(solutionNumber+"- There were no limit violations in solution "+SolName);
            else{
                System.out.println(solutionNumber+"- There were "+violationGraphs.size()+" violations in solution "+SolName);

                for(int i = 0; i < violationGraphs.size(); i++){
                    System.out.println(sumViolations.get(i)+"ha in year "+yearViolations.get(i)+ " containing MUs: "+violationGraphs.get(i).toString());
                }
            }
            System.out.println("-------------------------------------------------");
            solutionNumber++;
        }
        System.out.println("-------------------------------------------------");
        solutionsReader.close();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter number of solution to output on VerifiedSolution.csv");
        int in = input.nextInt();
        solutionsReader = new Scanner(solutions);

        solutionsReader.nextLine();
        solutionsReader.nextLine();
        solutionsReader.nextLine();
        solutionNumber = 1;

        while(solutionsReader.hasNextLine()) { //for each solution in that file

            solutionsReader.nextLine();
            if(solutionNumber==in){
                int[] prescIndexes = new int[nUgs];
                int[] intExt = new int[nUgs];


                String line = solutionsReader.nextLine();
                String[] arr = line.split("\t");

                int parSol = Integer.parseInt(arr[4]);
                int penSol = Integer.parseInt(arr[5]);
                int paiwSol = Integer.parseInt(arr[6]);
                int paieSol = Integer.parseInt(arr[7]);
                int paiiSol = Integer.parseInt(arr[8]);

                int lenPar = 193+1; //real 193
                int lenPen = 526+1; //real 527
                int lenPw = 482+1; // real 487
                int lenPe = 86+1; //real 86
                int lenPi = 109+1; //real 113

                FileWriter  outputFile = new FileWriter ("VerifiedSolution.csv");

                writeSolution("Par-ParetoSolutions.csv", nodes, parSol, lenPar,outputFile);
                writeSolution("Pen-ParetoSolutions.csv", nodes, penSol, lenPen,outputFile);
                writeSolution("PaivaWest-ParetoSolutions.csv", nodes, paiwSol, lenPw,outputFile);
                writeSolution("PaivaEast-ParetoSolutions.csv", nodes, paieSol, lenPe,outputFile);
                writeSolution("PaivaIslands-ParetoSolutions.csv", nodes, paiiSol, lenPi,outputFile);
                outputFile.close();
            }
            solutionNumber++;
        }
        solutionsReader.close();
        System.out.println("------FINISHED------");
    }
}
