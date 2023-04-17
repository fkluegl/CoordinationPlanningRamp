import java.util.ArrayList;
import java.util.Collections;

public class State {
    private final double v_up = 3; // m.s-1
    private final double v_down = 5; // m.s-1
    private final double park_time = 4; // s

    public double g_score = 1000;
    public double f_score = 1000;
    public State cameFrom = null;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<ParkingPlace> parking_places;


    public State() {
        this.vehicles = new ArrayList<Vehicle>();
        this.parking_places = new ArrayList<ParkingPlace>();
    }

    public void addVehicle(Vehicle v) {
        this.vehicles.add(v);
    }
    public void addParkingPlace(ParkingPlace p) {
        this.parking_places.add(p);
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

        // copy parking places list
        for (ParkingPlace p : this.parking_places) {
            ret.parking_places.add(p.getCopy());
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
        ArrayList<SceneElement> elts = new ArrayList<SceneElement>();
        elts.addAll(this.vehicles);
        elts.addAll(this.parking_places);
        Collections.sort(elts, new SceneElementXPositionComparator());
        String str = "";
        for (SceneElement se : elts) {
            str += se.getName();
        }
        return str;
    }

    public String toString() {
        ArrayList<SceneElement> elts = new ArrayList<SceneElement>();
        elts.addAll(this.vehicles);
        elts.addAll(this.parking_places);
        Collections.sort(elts, new SceneElementXPositionComparator());
        String ret = "";
        for (SceneElement se : elts) {
            ret += se.getName();
            ret += " ";
            ret += se.getX_position();
            ret += se.getType();
            ret += "\n";
        }
        ret += "-------------\n";
        return ret;
    }

    public ArrayList<State> get_next_states() {
        ArrayList<State> ret = new ArrayList<State>();



        //ret.add(s);

        ret = null;
        return ret;
    }


}
