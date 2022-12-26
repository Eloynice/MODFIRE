import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MUG {

    int internalId;
    int externalId;
    float area;
    int[] adj;
    int[] presc;

    int[][] years;

    int[] wood_total;

    int[] perc_r0_total;


    int[] soilloss_total;


    int time;
    boolean valid;

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

    static public float[] returnInsertableFloat(Scanner myReader){
        String data = myReader.nextLine();
        String[] str_split = data.split(",", 0);
        int size = str_split.length;
        float[] toInsert = new float [size];
        for(int i=0; i<size; i++) {
            toInsert[i] = Float.parseFloat(str_split[i]);
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

    static public float[] returnByYear(String data){
        String[] str_split = data.split(",", 0);
        int size = str_split.length;
        float[] toInsert = new float [size];
        for(int i=0; i<size; i++) {
            toInsert[i] = Float.parseFloat(str_split[i]);
        }
        return toInsert;
    }

    static public String[] returnByYearString(String data){
        String[] str_split = data.split(",", 0);
        int size = str_split.length;
        String[] toInsert = new String [size];
        for(int i=0; i<size; i++) {
            toInsert[i] = (str_split[i]);
        }
        return toInsert;
    }


    static public void fillArray(MUG[] array, String dir) {
        int index = 0;

        try {                       // idea: pass a directory, which should contain files called
            // graph_init, ugs_init, ...
            File adj_init = new File(dir + "/adj_init.txt");
            Scanner readerAdj = new Scanner(adj_init);

            File ext_init = new File(dir + "/external_init.txt");
            Scanner readerExt = new Scanner(ext_init);

            File ugs_init = new File(dir + "/ugs_init.txt");
            Scanner readerPresc = new Scanner(ugs_init);

            File years_init = new File(dir + "/years_init.txt");
            Scanner readerYears = new Scanner(years_init);

            File area_init = new File(dir + "/area_init.txt");
            Scanner readerArea = new Scanner(area_init);

            File wood_total_init = new File(dir + "/wood_total_init.txt");
            Scanner readerWoodTotal = new Scanner(wood_total_init);

            File soilloss_init = new File(dir + "/soilloss_total_init.txt");
            Scanner readerSoillossTotal = new Scanner(soilloss_init);

            File perc_r0_init = new File(dir + "/perc_r0_total_init.txt");
            Scanner readerPerc_r0 = new Scanner(perc_r0_init);



            while (readerAdj.hasNextLine()) {
                MUG toInsert = new MUG();
                toInsert.valid = true;
                toInsert.internalId = index;
                toInsert.adj = returnInsertable(readerAdj);

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

                toInsert.soilloss_total = returnInsertable2(readerSoillossTotal);
                toInsert.perc_r0_total = returnInsertable2(readerPerc_r0);
                toInsert.wood_total = returnInsertable2(readerWoodTotal);


                array[index] = toInsert;
                index++;
            }



            readerAdj.close();
            readerPresc.close();
            readerExt.close();
            readerArea.close();
            readerPerc_r0.close();
            readerSoillossTotal.close();
            readerWoodTotal.close();
            readerYears.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}