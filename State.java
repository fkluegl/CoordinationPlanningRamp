import java.util.ArrayList;
import java.util.Collections;

public class State {
    public double g_score = 1000;
    public double f_score = 1000;
    public State cameFrom = null;
    private ArrayList<Vehicle> vehicles;

    public State() {
        this.vehicles = new ArrayList<Vehicle>();
    }

    public void addVehicle(Vehicle v) {
        this.vehicles.add(v);
    }

    public void removeVehicle(String vname) {
        for (Vehicle x : this.vehicles) {
            if (x.getName().equals(vname)) {
                this.vehicles.remove(x);
            }
        }
    }

    public State getCopy() {
        State ret = new State();

        // copy vehicle list
        for (Vehicle v : this.vehicles) {
            ret.vehicles.add(v.getCopy());
        }

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

    public String getOrderedNames() {
        Collections.sort(this.vehicles, new VehicleXPositionComparator());
        String str = "";
        for (Vehicle v : this.vehicles) {
            str += v.getName();
        }
        return str;
    }

    public String toString() {
        String ret = "";
        for (Vehicle v : this.vehicles) {
            ret += v.getName();
            ret += " ";
            ret += v.getX_position();
            ret += "\n";
        }
        return ret;
    }

    public ArrayList<State> get_next_states() {
        ArrayList<State> ret = new ArrayList<State>();

        //ret.add(s);

        ret = null;
        return ret;
    }


}
