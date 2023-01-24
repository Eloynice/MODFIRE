import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class UG {

    int internalId;
    int externalId;
    float area;
    int[] adj;
    int[] presc;

    int[][] years;

    int[] crit0;
    int[] crit1;
    int[] crit2;
    int[] crit3;
    int[] crit4;
    int[] crit5;
    int[] crit6;
    int[] crit7;
    int[] crit8;
    int[] crit9;
    int[] crit10;

    int time;
    boolean valid;
    boolean treated;
    boolean treatedThisYear = false;
    boolean noAdjacencies = false;
    int lasYearOfViolation = 0;
    int lastYearTreated = 0;

    static public int[] returnInsertable(Scanner myReader){
        String data = myReader.nextLine();
        String[] str_split = data.split(",", 0);
        int size = str_split.length;
        int[] toInsert = new int [size];
        for(int i=0; i<size; i++) {
            toInsert[i] = Integer.parseInt(str_split[i]);
        }
        return toInsert;
    }

    static public int[] returnInsertable2(Scanner myReader){
        String data = myReader.nextLine();
        String[] str_split = data.split(",", 0);
        int size = str_split.length;
        int[] toInsert = new int[size];
        for(int i=0; i<size; i++) {
            toInsert[i] = (int)Float.parseFloat(str_split[i]);
        }
        return toInsert;
    }

    static public int[] returnZeroes(int size){
        int[] toInsert = new int[size];
        for(int i=0; i<size; i++) {
            toInsert[i] = 0;
        }
        return toInsert;
    }


    static public int[] adjacencyReader(int id, Scanner adjacencies, Scanner borders, int minBorder, UG[] array){
        String dataAdj = adjacencies.nextLine();
        String[] str_splitA = dataAdj.split(",", 0);

        String dataBorders = borders.nextLine();
        String[] str_splitB = dataBorders.split(",", 0);

        int size = str_splitA.length;


        ArrayList<Integer> toFill = new ArrayList<Integer>();

        int currentIndex = 0;

        for(int i=0; i<size; i++) {
            if(Float.parseFloat(str_splitB[i]) > minBorder){
                int ext_id = Integer.parseInt(str_splitA[i]);

                for(int j = 0; j < array.length; j++){
                    if(array[j].externalId == ext_id){
                        toFill.add(j);
                        break;
                    }
                }
            }
        }

        if(toFill.isEmpty()) {
            int[] toInsert = new int[1];
            toInsert[0] = -1;
            return toInsert;
        }
        else {
            return toFill.stream().mapToInt(Integer::intValue).toArray();
        }
    }


    static public int[] returnPeriods(String data){
        String[] str_split = data.split(",", 0);
        int size = str_split.length;
        int[] toInsert = new int [size];
        for(int i=0; i<size; i++) {
            toInsert[i] = Integer.parseInt(str_split[i]);
        }
        return toInsert;
    }

    static public void setupInternalIds(UG[] array, String dir) throws FileNotFoundException {
        int index = 0;
        File ext_init = new File(dir + "/external_init.txt");
        Scanner readerExt = new Scanner(ext_init);
        while (readerExt.hasNextLine()) {
            UG toInsert = new UG();

            toInsert.internalId = index;
            toInsert.externalId = Integer.parseInt(readerExt.nextLine());
            array[index] = toInsert;
            index++;
        }
        readerExt.close();
    }

    static public void fillArray(UG[] array, String dir, int minBorder, ArrayList<Boolean> flags) {
        int index = 0;

        try {
            File adj_init = new File(dir + "/adj_init.txt"); //Adjacency input file
            Scanner readerAdj = new Scanner(adj_init);

            File borders_init = new File(dir + "/border_init.txt"); //Borders input file
            Scanner readerBorders = new Scanner(borders_init);

            File ext_init = new File(dir + "/external_init.txt");
            Scanner readerExt = new Scanner(ext_init);


            File ugs_init = new File(dir + "/ugs_init.txt");
            Scanner readerPresc = new Scanner(ugs_init);

            File years_init = new File(dir + "/years_init.txt");
            Scanner readerYears = new Scanner(years_init);

            File area_init = new File(dir + "/area_init.txt");
            Scanner readerArea = new Scanner(area_init);

            File critFile0 = new File(dir + "/crit_file0.txt");
            Scanner reader0 = null;
            if(flags.get(0)) reader0 = new Scanner(critFile0);

            File critFile1 = new File(dir + "/crit_file1.txt");
            Scanner reader1 = null;
            if(flags.get(1)) reader1 = new Scanner(critFile1);

            File critFile2 = new File(dir + "/crit_file2.txt");
            Scanner reader2 = null;
            if(flags.get(2)) reader2 = new Scanner(critFile2);


            File critFile3 = new File(dir + "/crit_file3.txt");
            Scanner reader3 = null;
            if(flags.get(3)) reader3 = new Scanner(critFile3);

            File critFile4 = new File(dir + "/crit_file4.txt");
            Scanner reader4 = null;
            if(flags.get(4)) reader4 = new Scanner(critFile4);

            File critFile5 = new File(dir + "/crit_file5.txt");
            Scanner reader5 = null;
            if(flags.get(5)) reader5 = new Scanner(critFile5);

            File critFile6 = new File(dir + "/crit_file6.txt");
            Scanner reader6 = null;
            if(flags.get(6)) reader6 = new Scanner(critFile6);

            File critFile7 = new File(dir + "/crit_file7.txt");
            Scanner reader7 = null;
            if(flags.get(7)) reader7 = new Scanner(critFile7);

            File critFile8 = new File(dir + "/crit_file8.txt");
            Scanner reader8 = null;
            if(flags.get(8)) reader8 = new Scanner(critFile8);

            File critFile9 = new File(dir + "/crit_file9.txt");
            Scanner reader9 = null;
            if(flags.get(9)) reader9 = new Scanner(critFile9);

            File critFile10 = new File(dir + "/crit_file10.txt");
            Scanner reader10 = null;
            if(flags.get(10)) reader10 = new Scanner(critFile10);

            while (readerAdj.hasNextLine()) {
                UG toInsert = array[index];
                toInsert.valid = true;
                toInsert.treated = false;


                toInsert.adj = adjacencyReader(index, readerAdj, readerBorders, minBorder, array);

                toInsert.externalId = Integer.parseInt(readerExt.nextLine());
                toInsert.area = Float.parseFloat(readerArea.nextLine());

                toInsert.presc = returnInsertable(readerPresc);

                String data = readerYears.nextLine();
                //String[] removeVSlash = data.split("\\|", 0);
                String[] str_split = data.split("/", 0);
                int size = str_split.length;
                toInsert.years = new int[size][];

                for(int i = 0; i < size; i++) {
                    toInsert.years[i] = returnPeriods(str_split[i]);
                }

                int[] zeroArray = returnZeroes(toInsert.presc.length);

                if(flags.get(0)) toInsert.crit0 = returnInsertable2(reader0);
                else toInsert.crit0 = zeroArray;

                if(flags.get(1)) toInsert.crit1 = returnInsertable2(reader1);
                else toInsert.crit1 = zeroArray;

                if(flags.get(2)) toInsert.crit2 = returnInsertable2(reader2);
                else toInsert.crit2 = zeroArray;

                if(flags.get(3)) toInsert.crit3 = returnInsertable2(reader3);
                else toInsert.crit3 = zeroArray;

                if(flags.get(4)) toInsert.crit4 = returnInsertable2(reader4);
                else toInsert.crit4 = zeroArray;

                if(flags.get(5)) toInsert.crit5 = returnInsertable2(reader5);
                else toInsert.crit5 = zeroArray;

                if(flags.get(6)) toInsert.crit6 = returnInsertable2(reader6);
                else toInsert.crit6 = zeroArray;

                if(flags.get(7)) toInsert.crit7 = returnInsertable2(reader7);
                else toInsert.crit7 = zeroArray;

                if(flags.get(8)) toInsert.crit8 = returnInsertable2(reader8);
                else toInsert.crit8 = zeroArray;

                if(flags.get(9)) toInsert.crit9 = returnInsertable2(reader9);
                else toInsert.crit9 = zeroArray;

                if(flags.get(10)) toInsert.crit10 = returnInsertable2(reader10);
                else toInsert.crit10 = zeroArray;

                if(toInsert.adj[0] == -1){
                    toInsert.valid = false;
                    toInsert.noAdjacencies = true; //If unit has no adjacencies it's still used but not in the main loop
                }

                array[index] = toInsert;
                index++;
            }



            readerAdj.close();
            readerPresc.close();
            readerExt.close();
            readerBorders.close();
            readerArea.close();
            readerYears.close();

            if(flags.get(0)) reader0.close();
            if(flags.get(1)) reader1.close();
            if(flags.get(2)) reader2.close();
            if(flags.get(3)) reader3.close();
            if(flags.get(4)) reader4.close();
            if(flags.get(5)) reader5.close();
            if(flags.get(6)) reader6.close();
            if(flags.get(7)) reader7.close();
            if(flags.get(8)) reader8.close();
            if(flags.get(9)) reader9.close();
            if(flags.get(10)) reader10.close();



        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    static public void fillVerificationArray(UG[] array, String dir, int minBorder) {
        int index = 0;

        try {
            File adj_init = new File(dir + "/adj_init.txt"); //Adjacency input file
            Scanner readerAdj = new Scanner(adj_init);

            File borders_init = new File(dir + "/border_init.txt"); //Borders input file
            Scanner readerBorders = new Scanner(borders_init);

            File ext_init = new File(dir + "/external_init.txt");
            Scanner readerExt = new Scanner(ext_init);


            File ugs_init = new File(dir + "/ugs_init.txt");
            Scanner readerPresc = new Scanner(ugs_init);

            File years_init = new File(dir + "/years_init.txt");
            Scanner readerYears = new Scanner(years_init);

            File area_init = new File(dir + "/area_init.txt");
            Scanner readerArea = new Scanner(area_init);


            while (readerAdj.hasNextLine()) {
                UG toInsert = array[index];
                toInsert.valid = true;
                toInsert.treated = false;
                toInsert.time = 0;


                toInsert.adj = adjacencyReader(index, readerAdj, readerBorders, minBorder, array);

                toInsert.externalId = Integer.parseInt(readerExt.nextLine());

                toInsert.area = Float.parseFloat(readerArea.nextLine());

                toInsert.presc = returnInsertable(readerPresc);

                String data = readerYears.nextLine();
                //String[] removeVSlash = data.split("\\|", 0);
                String[] str_split = data.split("/", 0);
                int size = str_split.length;
                toInsert.years = new int[size][];

                for(int i = 0; i < size; i++) {
                    toInsert.years[i] = returnPeriods(str_split[i]);
                }

                if(toInsert.adj[0] == -1){
                    toInsert.valid = false;

                    toInsert.noAdjacencies = true; //If unit has no adjacencies it's still used but not in the main loop
                }

                array[index] = toInsert;
                index++;
            }

            readerAdj.close();
            readerPresc.close();
            readerExt.close();
            readerBorders.close();
            readerArea.close();
            readerYears.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}