import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.events.IntEventType;
import org.chocosolver.util.ESat;

import java.util.ArrayList;

public class MiniPropagator extends Propagator<IntVar> {

    final int ugIndex; // refers to the UG we are starting out from
    final MUG[] nodes;
    final int b; // refers to the sum area limit
    //final IntVar[] woodYield;



    public MiniPropagator(int ugIndex, MUG[] nodes, IntVar[] ugs, int sum, IntVar[] woodYield) {
        super(ugs, PropagatorPriority.LINEAR, false);
        this.ugIndex = ugIndex;
        this.nodes = nodes;
        this.b = sum;

    }

    @Override
    public int getPropagationConditions(int vIdx) { //don't know if this matters
        return IntEventType.combine(IntEventType.INSTANTIATE, IntEventType.INCLOW);
    }


    /*
    method to fill an array list with the prescription values of int ug
    in the year int period
     */
    public ArrayList<Integer> hasCutInYear(int ug, int period) {

        ArrayList<Integer> hasCut = new ArrayList<>();

        for(int i = 0; i < nodes[ug].presc.length; i++) {

            for(int j = 0; j < nodes[ug].years[i].length; j++) {

                if(nodes[ug].years[i][j] == period) {
                    hasCut.add(nodes[ug].presc[i]);
                }
            }
        }
        return hasCut;
    }


    //recursive function that fills an array list with the continuous adjacent UGs that have CUTS.
    public int gPropagate(int first, int ugIndex, int year, int sum) throws Exception{
        if(ugIndex == first){  //if the UG we are on isn't the UG we started out from
            if(nodes[ugIndex].valid && vars[ugIndex].isInstantiated() && nodes[ugIndex].time < year) { //and if this UG is instantiaded and hasn't been gladed yet
                nodes[ugIndex].time = year;
                sum = sum + (int)nodes[ugIndex].area;

                if(sum > b){
                    throw new Exception();
                }

                for (int rec = 0; rec < nodes[ugIndex].adj.length; rec++) { //and check its adjacencies

                    // to access the adjacent ugs we go through the ADJ array in current UG's node.
                    if(nodes[nodes[ugIndex].adj[rec]].valid &&  vars[nodes[ugIndex].adj[rec]].isInstantiated() && nodes[nodes[ugIndex].adj[rec]].time < year) {
                        //we only do this if the adjacent UG's haven't been gladed and if they've been instantiated.
                        sum = gPropagate(first, nodes[ugIndex].adj[rec], year, sum);
                    }
                }
            }
        }
        else {
            if (nodes[ugIndex].valid && vars[ugIndex].isInstantiated() && nodes[ugIndex].time < year) { //check if the UG hasn't been gladed and if it's already instantiated

                int ugPresc = vars[ugIndex].getValue(); //we get the prescription value of the current UG

                ArrayList<Integer> hasCut = hasCutInYear(ugIndex, year); //we get the list of prescriptions of this UG where there is a CUT in this year

                if(hasCut.contains(ugPresc)){ //if the List with all those prescriptions has the prescription the UG has been instantiated with.

                    nodes[ugIndex].time = year;

                    sum = sum + (int)nodes[ugIndex].area;

                    if(sum > b){
                        throw new Exception();
                    }

                    for (int rec = 0; rec < nodes[ugIndex].adj.length; rec++) { //and check its adjacencies

                        if(nodes[nodes[ugIndex].adj[rec]].valid && vars[nodes[ugIndex].adj[rec]].isInstantiated() && nodes[nodes[ugIndex].adj[rec]].time < year) {
                            sum = gPropagate(first, nodes[ugIndex].adj[rec], year, sum);
                        }
                    }
                }
            }
        }
        return sum;
    }

    public void gladePropagate(int ugIndex, int period) throws Exception {


        //the gPropagate function receives the ugIndex twice to differentiate between the first call of the function and the recursive calls
        //then receives the year and an empty array list which will be filled.

        gPropagate(ugIndex, ugIndex, period,0);

    }



    @Override
    public void propagate(int evtmask) throws ContradictionException {

        if(nodes[ugIndex].valid && vars[ugIndex].isInstantiated()) { //if the UG we give as parameter to the propagator is already instantiated

            int ugPresc = vars[ugIndex].getValue(); //we get it's prescription value

            int prescIndex = 0;

            for (int i = 0; i < nodes[ugIndex].presc.length; i++) {
                if (nodes[ugIndex].presc[i] == ugPresc) {
                    prescIndex = i; //and we find the index of this prescription in the UG's PRESC array
                    //we need this index to access the right set of years in the PERIODS array
                }
            }

            //System.out.println(ugIndex + " " + prescIndex);
            if (nodes[ugIndex].years[prescIndex][0] > 0) { //if the first index of the PERIODS array is larger than 0
                //that means that if the UG take this PRESC value then it has cuts,
                // if this value was 0 that means the UG with this presc has no cuts

                for(int i = 0; i < nodes.length; i++)
                    nodes[i].time = 0;


                for (int p = 0; p < nodes[ugIndex].years[prescIndex].length; p++) { //for every year that our UG has cuts

                    int year = nodes[ugIndex].years[prescIndex][p]; //current year

                    try {
                        gladePropagate(ugIndex, year); //call the gladePropagate function which will return
                    } catch (Exception e) {
                        fails();
                    }
                }
            }
        }

    }



    // same as gPropagate but without the "isInstatiated" checks
    public int g(int first, int ugIndex, int year, int sum) throws Exception{
        if(ugIndex == first){
            if(nodes[ugIndex].valid && nodes[ugIndex].time < year) {
                nodes[ugIndex].time = year;

                sum = sum + (int)nodes[ugIndex].area;

                if(sum > b){
                    throw new Exception();
                }

                for (int rec = 0; rec < nodes[ugIndex].adj.length; rec++) {


                    if(nodes[nodes[ugIndex].adj[rec]].valid && nodes[nodes[ugIndex].adj[rec]].time < year) {
                        sum = g(first, nodes[ugIndex].adj[rec], year, sum);
                    }
                }
            }
        }
        else {
            if (nodes[ugIndex].valid && nodes[ugIndex].time < year) {

                int ugPresc = vars[ugIndex].getValue();

                ArrayList<Integer> hasCut = hasCutInYear(ugIndex, year);

                if(hasCut.contains(ugPresc)){
                    nodes[ugIndex].time = year;

                    sum = sum + (int)nodes[ugIndex].area;

                    if(sum > b){
                        throw new Exception();
                    }

                    for (int rec = 0; rec < nodes[ugIndex].adj.length; rec++) {

                        if(nodes[nodes[ugIndex].adj[rec]].valid && nodes[nodes[ugIndex].adj[rec]].time < year) {
                            sum = g(first, nodes[ugIndex].adj[rec], year, sum);
                        }
                    }
                }
            }
        }
        return sum;
    }



    // same as gladed but without the "isInstatiated" checks because it doesn't call gPropagate
    public int glade(int ugIndex, int period) throws Exception{


        return g(ugIndex, ugIndex, period, 0);

    }



    @Override
    public ESat isEntailed() { //When it gets here everything is already instantiated, but it doesn't backtrack

        //this works basically the same way as the propagate function but it doesn't check for instantiations
        //and returns ESat.FALSE instead of fails()

        int ugPresc = vars[ugIndex].getValue();

        int prescIndex = 0;

        ArrayList<Integer> SUMS = new ArrayList<>(); //for testing purposes

        for (int i = 0; i < nodes[ugIndex].presc.length; i++) {
            if (nodes[ugIndex].presc[i] == ugPresc) {
                prescIndex = i; //we get the index of the PRESC this UG is taking
            }
        }

        if (nodes[ugIndex].years[prescIndex][0] > 0) { //if this UG with this PREC has cuts

            for(int i = 0; i < nodes.length; i++)
                nodes[i].time = 0;

            for (int p = 0; p < nodes[ugIndex].years[prescIndex].length; p++) { //for every year that our UG has cuts

                int year = nodes[ugIndex].years[prescIndex][p]; //current year
                try {
                    int sum = glade(ugIndex, year); //we get the sum
                    SUMS.add(sum);
                } catch (Exception e) {
                    return ESat.FALSE;
                }

                //SUMS.add(sum);
            }
        }


        /*uses the SUMS list to print the continuous sum of area cut starting from each UG for each year where that UG has a cut
        for(int i = 0; i < SUMS.size(); i++){
            if(SUMS.get(i) > 50)
                System.out.println("UG_"+(ugIndex+1)+" PR_"+nodes[ugIndex].presc[prescIndex]+" Y_"+nodes[ugIndex].years[prescIndex][i]+" SUM_"+SUMS.get(i));
        }*/

        //woodYield[ugIndex] = model.intVar((int)nodes[ugIndex].wood_total[prescIndex]);
        return ESat.TRUE;
    }
}
