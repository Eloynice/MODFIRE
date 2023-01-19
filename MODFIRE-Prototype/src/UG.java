import java.io.File;
import java.io.FileNotFoundException;
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


    static public int[] adjacencyReader(Scanner adjacencies, Scanner borders, int minBorder){
        String dataAdj = adjacencies.nextLine();
        String[] str_splitA = dataAdj.split(",", 0);

        String dataBorders = borders.nextLine();
        String[] str_splitB = dataBorders.split(",", 0);

        int size = str_splitA.length;


        int[] toInsert = new int [size];
        toInsert[0] = -1;
        int currentIndex = 0;

        for(int i=0; i<size; i++) {
            if(Float.parseFloat(str_splitB[i]) > minBorder){
                toInsert[currentIndex] = Integer.parseInt(str_splitA[i]);
                currentIndex++;
            }
        }
        return toInsert;
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

    static public void fillArray(UG[] array, String dir, int minBorder) {
        int index = 0;

        try {                       // idea: pass a directory, which should contain files called
            // graph_init, ugs_init, ...
            File adj_init = new File(dir + "/adj_init.txt");
            Scanner readerAdj = new Scanner(adj_init);

            File borders_init = new File(dir + "/border_init.txt");
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

            while (readerAdj.hasNextLine()) {
                UG toInsert = new UG();
                toInsert.valid = true;
                toInsert.treated = false;
                toInsert.internalId = index;
                toInsert.adj = adjacencyReader(readerAdj, readerBorders, minBorder);

                toInsert.externalId = Integer.parseInt(readerExt.nextLine());
                toInsert.area = Float.parseFloat(readerArea.nextLine());

                toInsert.presc = returnInsertable(readerPresc);

                String data = readerYears.nextLine();
                String[] removeVSlash = data.split("\\|", 0);
                String[] str_split = removeVSlash[1].split("/", 0);
                int size = str_split.length;
                toInsert.years = new int[size][];

                for(int i = 0; i < size; i++) {
                    toInsert.years[i] = returnPeriods(str_split[i]);
                }

                toInsert.crit0 = returnInsertable2(reader0);
                toInsert.crit1 = returnInsertable2(reader1);
                toInsert.crit2 = returnInsertable2(reader2);

                toInsert.crit3 = returnInsertable2(reader3);

                toInsert.crit4 = returnInsertable2(reader4);

                toInsert.crit5 = returnInsertable2(reader5);

                toInsert.crit6 = returnInsertable2(reader6);

                toInsert.crit7 = returnInsertable2(reader7);

                toInsert.crit8 = returnInsertable2(reader8);

                toInsert.crit9 = returnInsertable2(reader9);

                toInsert.crit10 = returnInsertable2(reader10);

                if(toInsert.adj[0] == -1){
                    toInsert.valid = false;
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

}
