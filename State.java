import java.util.ArrayList;
import java.util.Collections;

public class State {
    private final static double v_up = 3.1; // m.s-1
    private final static double v_down = 5.7; // m.s-1
    private final static double park_time = 4.3; // s
    private final static double x_max = 100.0; // m
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
            ret += se.getType();
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
        int[] combi = new int[Nv];
        int[][] combinations = new int[N][Nv];

        // All possible combinations of actions for each robot
        // [... (k/27)%3  (k/9)%3  (k/3)%3  (k/1)%3]
        for (int k=0; k<N; k++)
            for (int v = Nv-1; v >= 0; v--)
                combinations[N-k-1][v] = (int)(k / Math.pow(Na, v)) % 3;


            /*System.out.print("[Action=" + action + "] ");
            // loop on vehicles
            for (Vehicle v : dw_vehicles)
                System.out.print(v.getName() + "-");
            System.out.println();*/


        for (int k=0; k<N; k++) {
            for (int i=0; i<Nv; i++)
                System.out.print(combinations[k][i] + " ");
            System.out.println();
        }

        //ret.add(s);

        ret = null;
        return ret;
    }


}
