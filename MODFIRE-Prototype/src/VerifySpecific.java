import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class VerifySpecific {

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


        String solFilename = args[3];
        File solutions = new File(solFilename);
        Scanner solutionsReader = new Scanner(solutions);

        int[] prescIndexes = new int[nUgs];
        int[] intExt = new int[nUgs];

        while(solutionsReader.hasNextLine()){
            String line = solutionsReader.nextLine();

            int externalId = Integer.parseInt(line.split(",")[0]);
            int presc = Integer.parseInt(line.split(",")[1]);

            int internalId = returnInternal(nodes, externalId);
            int prescIndex = indexOfPresc(nodes, internalId, presc);

            nodes[internalId].valid = true;

            intExt[internalId] = externalId;
            prescIndexes[internalId] = prescIndex;
        }

        solutionsReader.close();


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
        System.out.println("Solution written to: VerifiedSolution.csv");
        System.out.println("-------------------------------------------------");
    }
}
