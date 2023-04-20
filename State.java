import java.util.ArrayList;
import java.util.Collections;

public class State {
    private final static double park_time = 4.3; // s
    private final static double x_max = 100.0; // m
    private final static double safety_distance = 5.0; // m (center-to-center)
    private final static int EXIT = 0;
    private final static int PARK = 1;
    private final static int WAIT = 2;

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
            ret += se.getTypeString();
            ret += "\n";
        }
        ret += "-------------\n";
        return ret;
    }

    public ArrayList<State> get_next_states() {
        ArrayList<State> ret = new ArrayList<State>();

        // get a copy of reversely sorted downward vehicles (in case the order is changed by other functions)
        ArrayList<Vehicle> dw_vehicles = new ArrayList<>();
        Collections.sort(this.vehicles, new VehicleXPositionComparatorReverse());
        for (Vehicle v : this.vehicles)
            if (v.isDownward())
                dw_vehicles.add(v);

        int[] actions = new int[] {EXIT, PARK, WAIT};
        int Nv = dw_vehicles.size();
        int Na = actions.length;
        int N = (int)Math.pow(Na, Nv);
        int[][] combinations = new int[N][Nv];

        // All possible combinations of actions for each robot
        // [... (k/27)%3  (k/9)%3  (k/3)%3  (k/1)%3]
        for (int k=0; k<N; k++)
            for (int v = Nv-1; v >= 0; v--)
                combinations[N-k-1][v] = (int)(k / Math.pow(Na, v)) % Na;

        // display combinations
        for (int k=0; k<N; k++) {
            for (int i=0; i<Nv; i++)
                System.out.print(combinations[k][i] + " ");
            System.out.println();
        }
        System.out.println();

        for (int k=0; k<N; k++) {
            State s = this.feasible(dw_vehicles, combinations[k]);
            if (s != null)
                ret.add(s);
        }

        ret = null;
        return ret;
    }

    State feasible(ArrayList<Vehicle> dw_list, int[] actions) {
        // return the expected resulting state, or null if infeasible
        State resulting_state = this.getCopy();

        // print current test
        for (int i=0; i<dw_list.size(); i++) {
            Vehicle v = dw_list.get(i);
            int action = actions[i];
            System.out.print(v.getName() + "-" + action + " ");
        }

        // first, check if upward vehicles can move up
        // ...

        // loop on ↓ vehicles, starting from bottom
        for (int i=0; i<dw_list.size(); i++) {
            Vehicle v = dw_list.get(i);
            int action = actions[i];

            if (action == WAIT) {
                // do nothing
            }
            else if (action == EXIT) {
                // test if v cannot exit
                if (resulting_state.exist_upward_vehicle_below(v)) {
                    System.out.print(" --> " + v.getName() + " cannot exit because of upward vehicle below.\n");
                    return null;
                }
                // upd res_state
            }
            else if (action == PARK) {
                ArrayList<ParkingPlace> pp_below = resulting_state.get_parking_places_below(v);
                if (pp_below.size() == 0) {
                    System.out.print(" --> There are no parking places below " + v.getName() + ".\n");
                    return null;
                }
                else {
                    // loop on parking places below v
                    for (ParkingPlace pp : pp_below) {
                        if (resulting_state.exist_upward_vehicle_between(v, pp)) {
                            System.out.print(" --> There is an upward vehicle between " + v.getName() + " and " + pp.getName() + ".\n");
                            return null;
                        }
                    }
                }
                // upd res_state
            }

        }

        System.out.println();

        return resulting_state;
    }

    boolean exist_upward_vehicle_below(Vehicle v) {
        // todo: check for PP!!!
        for (Vehicle uv : this.vehicles)
            if (!uv.isDownward() && uv.x_position > v.x_position)
                return true;

        return false;
    }

    ArrayList<ParkingPlace> get_parking_places_below(Vehicle v) {
        ArrayList<ParkingPlace> ret = new ArrayList<>();
        for (ParkingPlace pp : this.parking_places)
            if (pp.x_position > v.x_position)
                ret.add(pp);

        return ret;
    }

    boolean exist_upward_vehicle_between(Vehicle v, ParkingPlace pp) {
        for (Vehicle uv : this.vehicles)
            if (!uv.isDownward()  &&  uv.x_position > (v.x_position + 0.1)  &&  uv.x_position < (pp.x_position - 0.1))
                return true;

        return false;
    }


}
