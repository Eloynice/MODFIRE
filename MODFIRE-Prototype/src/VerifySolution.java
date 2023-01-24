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
            if(!islandUGs.contains(nodes[i].externalId)){
                nodes[i].valid = false;
            }
        }

        File solFile = new File("fullSolution");
        Scanner solReader = new Scanner(solFile);

        int[] prescIndexes = new int[nUgs];
        int[] intExt = new int[nUgs];

        while (solReader.hasNextLine()) {
            String line = solReader.nextLine();
            int externalId = Integer.parseInt(line.split(",")[0]);
            int presc = Integer.parseInt(line.split(",")[1]);

            int internalId = returnInternal(nodes, externalId);
            int prescIndex = indexOfPresc(nodes, internalId, presc);

            intExt[internalId] = externalId;
            prescIndexes[internalId] = prescIndex;

        }


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
            System.out.println("There were no limit violations in this solution");
        else{
            System.out.println("There were "+violationGraphs.size()+" violations in this solution");

            for(int i = 0; i < violationGraphs.size(); i++){
                System.out.println(sumViolations.get(i)+" in year "+yearViolations.get(i)+ " containing MUs: "+violationGraphs.get(i).toString());
            }
        }
    }
}
