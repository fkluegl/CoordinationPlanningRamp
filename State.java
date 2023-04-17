import java.util.ArrayList;

public class State {
    public double g_score = 1000;
    public double f_score = 1000;
    public State cameFrom = null;
    public ArrayList<Vehicle> vehicles;

    public State() {

    }

    public State getCopy() {
        State ret = new State();

        return ret;
    }

    public boolean equals(State s) {
        /*if (this.current_node != s.current_node)
            return false;

        for (int i=0; i<size; i++)
            if (this.clean[i] != s.clean[i])
                return false;
*/
        return true;
    }

    public ArrayList<State> get_next_states() {  // (cleans neighbour locations)
        ArrayList<State> ret = new ArrayList<State>();

        //ret.add(s);

        return ret;
    }


}
