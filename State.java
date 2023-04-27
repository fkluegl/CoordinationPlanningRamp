import java.util.ArrayList;
import java.util.Collections;

public class State {
    public final static double parking_time = 3; // s
    public final static double x_max = 100.0; // m
    public final static double safety_distance = 5.0; // m (center-to-center)
    private double start_time = 0;
    public double g_score = 1000;
    public double f_score = 1000;
    public State cameFrom = null;
    private ArrayList<Vehicle> dw_vehicles;
    private ArrayList<Vehicle> up_vehicles;
    private int Ndw = 0;
    private int Nup = 0;
    private int Npp = 0;
    private ArrayList<ParkingPlace> parking_places;
    private static ArrayList<State> states_actions;
    private static MiniSimulator mini_simulator;


    public State() {
        this.dw_vehicles = new ArrayList<Vehicle>();
        this.up_vehicles = new ArrayList<Vehicle>();
        this.parking_places = new ArrayList<ParkingPlace>();
    }

    public static void setMini_simulator(MiniSimulator mini_simulator) {
        State.mini_simulator = mini_simulator;
    }

    public void addVehicle(Vehicle v) {
        if (v.isDownward()) {
            this.dw_vehicles.add(v);
            this.Ndw ++;
        }
        else {
            this.up_vehicles.add(v);
            this.Nup ++;
        }
        v.setParentState(this);
    }
    public void addParkingPlace(ParkingPlace p) {
        this.parking_places.add(p);
        this.Npp ++;
        p.setParentState(this);
    }

    public boolean allVehiclesOut() {
        for (Vehicle v : dw_vehicles)
            if (!v.isOut())
                return false;
        for (Vehicle v : up_vehicles)
            if (!v.isOut())
                return false;
        return true;
    }

    public void removeVehicle(String vname) {
        for (Vehicle x : this.dw_vehicles) {
            if (x.getName().equals(vname)) {
                this.dw_vehicles.remove(x);
                this.Ndw --;
                return;
            }
        }
        for (Vehicle x : this.up_vehicles) {
            if (x.getName().equals(vname)) {
                this.up_vehicles.remove(x);
                this.Nup --;
                return;
            }
        }
    }

    public State getCopy() {
        State ret = new State();
        // copy vehicle lists
        for (Vehicle v : this.dw_vehicles) {
            if (!v.isOut())
                ret.dw_vehicles.add(v.getCopy());
        }
        for (Vehicle v : this.up_vehicles) {
            if (!v.isOut())
                ret.up_vehicles.add(v.getCopy());
        }
        // copy parking places list
        for (ParkingPlace p : this.parking_places) {
            ret.parking_places.add(p.getCopy());
        }
        ret.Ndw = ret.dw_vehicles.size();
        ret.Nup = ret.up_vehicles.size();
        ret.Npp = this.Npp;
        ret.start_time = this.start_time;
        return ret;
    }

    public boolean equals(State s) {
        // TODO!!!
        System.out.println("State.equals() not implemented!");
        System.exit(0);
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
        elts.addAll(this.dw_vehicles);
        elts.addAll(this.up_vehicles);
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
        elts.addAll(this.dw_vehicles);
        elts.addAll(this.up_vehicles);
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

    public String current_action_str() {
        ArrayList<Vehicle> elts = new ArrayList<>();
        elts.addAll(this.dw_vehicles);
        Collections.sort(elts, new SceneElementXPositionComparator());
        String ret = "";
        for (Vehicle se : elts) {
            int id = se.getCurrent_action().getId();
            String id_str = se.getCurrent_action().getName();
            ret += id_str;
            if (id == Action.PARK) ret += "(" + se.getCurrent_action().getParameter().name + ") ";
            else                   ret += "     ";
            ret += " ";
        }
        ret += "\n";
        return ret;
    }

    public ArrayList<State> get_next_states() {
        ArrayList<State> ret = new ArrayList<State>();
        this.states_actions = new ArrayList<State>();

        // enumerated possible actions to be applied to this state
        this.enumerate_actions(0, this.dw_vehicles.size());

        // get states resulting from events occurring during simulation
        for (State s : this.states_actions) {
            ArrayList<State> event_based_states = mini_simulator.simulate(s.getCopy()); // because simulate(x) modifies x
            if (event_based_states != null)
                ret.addAll(event_based_states);
        }

        System.out.println("Simulation has generated " + ret.size() + " states.");
        return this.states_actions; // todo: return ret;
    }


    public void enumerate_actions(int id_vehicle, int Nv) {
        if (id_vehicle == Nv) {
            this.states_actions.add(this);
        }
        else {
            Vehicle v = this.dw_vehicles.get(id_vehicle);

            // enumerate EXIT actions
            boolean exit_feasible = !this.is_upward_vehicle_below(v);
            if (exit_feasible) {
                State state_copy_1 = this.getCopy();
                state_copy_1.dw_vehicles.get(id_vehicle).setCurrent_action(new Action(Action.EXIT));
                state_copy_1.enumerate_actions(id_vehicle + 1, Nv);
            }
            else
                System.out.println("[ENUM FILTER!!!] " + v.getName() + " cannot EXIT because of vehicle Up vehicle below.");

            // enumerate PARK actions
            State state_copy_2 = this.getCopy();
            for (int p=0; p<this.Npp; p++)
                state_copy_2.enumerate_parking_places(id_vehicle, p, Nv);

            // enumerate WAIT actions
            State state_copy_3 = this.getCopy();
            state_copy_3.dw_vehicles.get(id_vehicle).setCurrent_action(new Action(Action.WAIT));
            state_copy_3.enumerate_actions(id_vehicle + 1, Nv);
        }
    }

    public void enumerate_parking_places(int id_vehicle, int id_parking, int Nv) {
        if (id_vehicle == Nv) {
            this.states_actions.add(this);
        }
        else {
            // Assigns parking places, prunes out parking places which are:
            //  - above
            //  - booked
            //  - with an upward vehicle in-between
            State state_copy_22 = this.getCopy();
            ParkingPlace pp = state_copy_22.parking_places.get(id_parking);
            Vehicle v = state_copy_22.dw_vehicles.get(id_vehicle);
            if (!pp.isBooked()) {
                if (pp.isBelow(v)) {
                    if (!state_copy_22.exist_upward_vehicle_between(v, pp)) {
                        v.setCurrent_action(new Action(Action.PARK, pp));
                        pp.setBooked(true);
                        state_copy_22.enumerate_actions(id_vehicle + 1, Nv);
                    }
                    else
                        System.out.println("[ENUM FILTER!!!] " + v.getName() + " cannot PARK because of Up vehicle in-between.");
                }
            }
        }
    }


    public double getStart_time() {
        return start_time;
    }

    public ArrayList<Vehicle> getDw_vehicles() {
        return dw_vehicles;
    }
    public ArrayList<Vehicle> getUp_vehicles() {
        return up_vehicles;
    }

    public ArrayList<ParkingPlace> getParking_places() {
        return parking_places;
    }

    public void increaseStart_time(double start_time) {
        this.start_time += start_time;
    }

    Vehicle get_closest_upward_vehicle_below(Vehicle v) {
        Vehicle closest = null;
        double min_dist = 1000;
        for (Vehicle upv : this.up_vehicles)
            if (upv.x_position > v.x_position) {
                double d = upv.x_position - v.x_position;
                if (d < min_dist) {
                    min_dist = d;
                    closest = upv;
                }
            }
        return closest;
    }

    boolean is_upward_vehicle_below(Vehicle v) {
        for (Vehicle upv : this.up_vehicles)
            if (upv.x_position > v.x_position)
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

    ArrayList<Vehicle> get_dw_vehicles_below(Vehicle v) {
        ArrayList<Vehicle> ret = new ArrayList<>();
        for (Vehicle dwv : this.dw_vehicles)
            if (dwv.x_position > v.x_position)
                ret.add(dwv);

        return ret;
    }

    boolean exist_upward_vehicle_between(Vehicle v, ParkingPlace pp) {
        for (Vehicle upv : this.up_vehicles)
            if (upv.x_position > (v.x_position + 0.1)  &&  upv.x_position < (pp.x_position - 0.1))
                return true;

        return false;
    }

    boolean exist_enough_parking_place_between(Vehicle dwv, Vehicle upv) {
        int Ndw = 1;
        int Npp = 0;

        for (Vehicle v : this.dw_vehicles)
            if (v.x_position > (dwv.x_position + 0.1) && v.x_position < (upv.x_position - 0.1))
                Ndw ++;

        for (ParkingPlace pp : this.parking_places)
            if (pp.x_position > (dwv.x_position + 0.1) && pp.x_position < (upv.x_position - 0.1))
                Npp ++;

        if (Ndw > Npp)
            return false;
        else
            return true;
    }

}
