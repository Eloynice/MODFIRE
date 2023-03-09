import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class VerifyAllSolutions {
    public static ArrayList<ArrayList<Integer>> violationGraphs = new ArrayList<>();
    public static ArrayList<Float> sumViolations = new ArrayList<Float>();
    public static ArrayList<Integer> yearViolations = new ArrayList<Integer>();
    public static ArrayList<Integer> currentViolation = new ArrayList<Integer>();
    public static ArrayList<Float> largestClearCutInYear = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> largestCCInYearMUS = new ArrayList<ArrayList<Integer>>();


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

    public static float g(int first, int ugIndex, int year, float sum, UG[] nodes, int[] prescIndexes) throws Exception{
        if(ugIndex == first){
            if(nodes[ugIndex].valid && !nodes[ugIndex].treatedThisYear) {
                nodes[ugIndex].treatedThisYear = true;
                sum = sum + nodes[ugIndex].area;

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
                        sum = sum + nodes[ugIndex].area;

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


    public static void insertCurrentViolation(float sum, int year){
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
    public static float glade(int ugIndex, int period, UG[] nodes, int[] prescIndexes) throws Exception{
        return g(ugIndex, ugIndex, period, 0, nodes, prescIndexes);
    }

    public static void readRegionFile(String reg, UG[] nodes, int solIndex, int regLen, int[] prescIndexes, int[] intExt) throws IOException {

        int s = (solIndex-1)*(regLen+1)+ 1;
        int e = s + regLen;
        String pairLine = null;


        BufferedReader readerPar = new BufferedReader(new FileReader(reg));
        for (int i = 1; i < s; i++) {
            readerPar.readLine();
        }

        for(int i = s; i <= e; i++ ) {
            pairLine = readerPar.readLine();
            String P = pairLine.substring(0, 1);
            if(P.compareToIgnoreCase("S") == 0){
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

    public static void writeSolution(String reg, UG[] nodes, int solIndex, int regLen, FileWriter outputFile) throws IOException {
        int s = (solIndex-1)*(regLen+1)+ 1;
        int e = s + regLen;
        String pairLine = null;



        BufferedReader readerPar = new BufferedReader(new FileReader("RegionSolutions/"+reg));
        for (int i = 1; i < s; i++) {
            readerPar.readLine();
        }

        for(int i = s; i <= e; i++ ) {
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

    public static void main(String[] args) throws Exception {
        int areaLimit = Integer.parseInt(args[0]);
        String fileDirectory = args[1];
        int minBorder = Integer.parseInt(args[2]);

        BufferedReader reader = new BufferedReader(new FileReader(fileDirectory + "/ugs_init.txt"));

        int nUgs = 0;
        while (reader.readLine() != null) nUgs++;
        reader.close();

        UG[] nodes = new UG[nUgs];

        UG.setupInternalIds(nodes, fileDirectory);

        if (minBorder <= 0)
            UG.fillVerificationArray(nodes, fileDirectory, 0);
        else
            UG.fillVerificationArray(nodes, fileDirectory, minBorder);


        //String solFilename = args[3];
        String solFilename = "OC_Full-OneCritSolutons.csv";
        //String solFilename = "Full-AllSolutions.csv";
        File solutions = new File(solFilename);
        Scanner solutionsReader = new Scanner(solutions);

        int[] prescIndexes = new int[nUgs];
        int[] intExt = new int[nUgs];
        int lineI = 1;
        int solutionNumber = 0;
        int len = 1406;
        String line;

        while(solutionsReader.hasNextLine()){
            line = solutionsReader.nextLine();
            solutionNumber++;
            while(lineI < len * solutionNumber+1){
                line = solutionsReader.nextLine();
                //System.out.println(line);
                lineI++;
                //System.out.println(solutionNumber);
                //System.out.println(lineI);

                int externalId = Integer.parseInt(line.split(",")[0]);
                int presc = Integer.parseInt(line.split(",")[1]);

                int internalId = returnInternal(nodes, externalId);
                int prescIndex = indexOfPresc(nodes, internalId, presc);

                nodes[internalId].valid = true;

                intExt[internalId] = externalId;
                prescIndexes[internalId] = prescIndex;
            }
            int noUse = 0;

            for(int i = 0; i<50;i++){
                largestClearCutInYear.add(i,0f);
                largestCCInYearMUS.add(i,null);
            }

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
                            float sum = glade(internalId, currentYear, nodes, prescIndexes);
                            if(sum > largestClearCutInYear.get(currentYear-2020)){
                                largestClearCutInYear.set(currentYear-2020, sum);
                                largestCCInYearMUS.set(currentYear - 2020, (ArrayList) currentViolation.clone());
                            }
                            if(sum > areaLimit){
                                insertCurrentViolation(sum, currentYear);
                            }
                            currentViolation.clear();

                            for (UG node : nodes) node.treatedThisYear = false;
                        }
                    }
                }
            }

            float largest = 0;
            int yearIndex = 0;
            for(int i = 0; i < largestClearCutInYear.size();i++){
                if(largestClearCutInYear.get(i) > largest){
                    largest = largestClearCutInYear.get(i);
                    yearIndex = i;
                }
            }

            if (violationGraphs.isEmpty())
                System.out.println(solutionNumber+"- There were no limit violations in this solution/ Largest CC was "+largest+" in year "+(yearIndex+2020)
                +" containing MUS: "+largestCCInYearMUS.get(yearIndex).toString());
            else{
                System.out.println(solutionNumber+"- There were "+violationGraphs.size()+" violations in solution "+solutionNumber);

                for(int i = 0; i < violationGraphs.size(); i++){
                    System.out.println(sumViolations.get(i)+"ha in year "+yearViolations.get(i)+ " containing MUs: "+violationGraphs.get(i).toString());
                }
            }
            largestClearCutInYear.clear();
            largestCCInYearMUS.clear();
            sumViolations.clear();
            violationGraphs.clear();
            yearViolations.clear();
            violationGraphs.clear();
        }
        System.out.println("-------------------------------------------------");

        solutionsReader.close();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter number of solution to output on VerifiedSolution.csv");
        int in = input.nextInt();
        solutionsReader = new Scanner(solutions);


        boolean validPick = false;

        int until = 1406 * (in-1) + 2;
        lineI = 1;

        readRegionFile(solFilename,nodes,in,1406,prescIndexes,intExt);
        /*
        while(solutionsReader.hasNextLine()) { //for each solution in that file

            if(lineI == until){
                line = solutionsReader.nextLine();
                System.out.println(line);
                for(int i = 0; i < nodes.length; i++){
                    line = solutionsReader.nextLine();
                    System.out.println(line);
                    int externalId = Integer.parseInt(line.split(",")[0]);
                    int presc = Integer.parseInt(line.split(",")[1]);

                    int internalId = returnInternal(nodes, externalId);
                    int prescIndex = indexOfPresc(nodes, internalId, presc);

                    nodes[internalId].valid = true;

                    intExt[internalId] = externalId;
                    prescIndexes[internalId] = prescIndex;
                }
                validPick = true;
                break;
            }
            solutionsReader.nextLine();
            lineI++;
        }
        solutionsReader.close();
        if(!validPick)
            System.out.println("Invalid input");
*/
        int noUse = 0;
        for(int i = 0; i<50;i++){
            largestClearCutInYear.add(i,0f);
            largestCCInYearMUS.add(i,null);
        }
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
                        float sum = glade(internalId, currentYear, nodes, prescIndexes);
                        if(sum > largestClearCutInYear.get(currentYear-2020)){
                            largestClearCutInYear.set(currentYear-2020, sum);
                            largestCCInYearMUS.set(currentYear - 2020, (ArrayList) currentViolation.clone());
                        }
                        if(sum > areaLimit){
                            insertCurrentViolation(sum, currentYear);
                        }
                        currentViolation.clear();

                        for (UG node : nodes) node.treatedThisYear = false;
                    }
                }
            }
        }

        FileWriter largestCCs = new FileWriter("YearlyLargestCC");
        FileWriter largestCCMUs = new FileWriter("YearlyLargestCCMUs");
        for(int i = 0; i<largestClearCutInYear.size();i++){
            largestCCs.write(String.valueOf(largestClearCutInYear.get(i)+"\n"));
            largestCCMUs.write(String.valueOf(largestCCInYearMUS.get(i)+"\n"));
        }
        largestCCs.close();
        largestCCMUs.close();

        if (violationGraphs.isEmpty()) {
            System.out.println("There were no limit violations in this solution");
        }
        else{
            System.out.println("There were "+violationGraphs.size()+" violations in this solution ");

            for(int i = 0; i < violationGraphs.size(); i++){
                System.out.println(sumViolations.get(i)+"ha in year "+yearViolations.get(i)+ " containing MUs: "+violationGraphs.get(i).toString());
            }
        }

        FileWriter  outputFile = new FileWriter ("VerifiedSolution.csv");
        for(int j = 0; j < nodes.length; j++){
            outputFile.write(nodes[j].externalId+","+nodes[j].presc[prescIndexes[j]]+"\n");
        }
        outputFile.close();

        System.out.println("Solution written to: VerifiedSolution.csv");
        System.out.println("Largest clear-cut by year values written to YearlyLargestCC");
        System.out.println("-------------------------------------------------");
    }
}
