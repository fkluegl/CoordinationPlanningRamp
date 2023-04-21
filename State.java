import java.util.ArrayList;
import java.util.Collections;

public class State {
    private final static double park_time = 4.3; // s
    private final static double x_max = 100.0; // m
    private final static double safety_distance = 5.0; // m (center-to-center)

    public double g_score = 1000;
    public double f_score = 1000;
    public State cameFrom = null;
    private ArrayList<Vehicle> dw_vehicles;
    private ArrayList<Vehicle> up_vehicles;
    private int Ndw = 0;
    private int Nup = 0;
    private ArrayList<ParkingPlace> parking_places;
    private static ArrayList<State> next_states;


    public State() {
        this.dw_vehicles = new ArrayList<Vehicle>();
        this.up_vehicles = new ArrayList<Vehicle>();
        this.parking_places = new ArrayList<ParkingPlace>();
    }

    public void addVehicle(Vehicle v) {
        if (v.isDownward()) this.dw_vehicles.add(v);
        else                this.up_vehicles.add(v);
    }
    public void addParkingPlace(ParkingPlace p) {
        this.parking_places.add(p);
    }

    public void removeVehicle(String vname) {
        for (Vehicle x : this.dw_vehicles) {
            if (x.getName().equals(vname)) {
                this.dw_vehicles.remove(x);
                return;
            }
        }
        for (Vehicle x : this.up_vehicles) {
            if (x.getName().equals(vname)) {
                this.up_vehicles.remove(x);
                return;
            }
        }
    }

    public State getCopy() {
        State ret = new State();
        // copy vehicle lists
        for (Vehicle v : this.dw_vehicles) {
            ret.dw_vehicles.add(v.getCopy());
        }
        for (Vehicle v : this.up_vehicles) {
            ret.up_vehicles.add(v.getCopy());
        }
        // copy parking places list
        for (ParkingPlace p : this.parking_places) {
            ret.parking_places.add(p.getCopy());
        }
        ret.Ndw = this.Ndw;
        ret.Nup = this.Nup;
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
        //elts.addAll(this.up_vehicles);
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
        ArrayList<Vehicle> elts = new ArrayList<Vehicle>();
        elts.addAll(this.dw_vehicles);
        Collections.sort(elts, new SceneElementXPositionComparator());
        String ret = "";
        for (Vehicle se : elts) {
            int id = se.getCurrent_action().getId();
            ret += id;
            if (id == Action.PARK) ret += "(" + se.getCurrent_action().parameter.name + ") ";
            else                   ret += "     ";
            ret += " ";
        }
        ret += "\n";
        return ret;
    }

    public ArrayList<State> get_next_states() {
        this.next_states = new ArrayList<State>();
        this.compute_next_states_rec(0, this.dw_vehicles.size());
        return this.next_states;
    }


    public void compute_next_states_rec(int id_vehicle, int Nv) {
        if (id_vehicle == Nv) {
            this.next_states.add(this);
        }
        else {
            State state_copy_1 = this.getCopy();
            state_copy_1.dw_vehicles.get(id_vehicle).setCurrent_action(new Action(Action.EXIT));
            state_copy_1.compute_next_states_rec(id_vehicle + 1, Nv);

            //state_copy_2.dw_vehicles.get(id_vehicle).setCurrent_action(new Action(Action.PARK));
            Vehicle v = this.dw_vehicles.get(id_vehicle);
            ArrayList<ParkingPlace> pp_below = this.get_parking_places_below(v);
            for (ParkingPlace pp : pp_below) {
                State state_copy_2 = this.getCopy();
                state_copy_2.compute_next_states_parking_rec(id_vehicle, pp, Nv);
            }

            State state_copy_3 = this.getCopy();
            state_copy_3.dw_vehicles.get(id_vehicle).setCurrent_action(new Action(Action.WAIT));
            state_copy_3.compute_next_states_rec(id_vehicle + 1, Nv);
        }
    }

    public void compute_next_states_parking_rec(int id_vehicle, ParkingPlace pp, int Nv) {
        if (id_vehicle == Nv) {
            this.next_states.add(this);
        }
        else {
            State state_copy_1 = this.getCopy();
            state_copy_1.dw_vehicles.get(id_vehicle).setCurrent_action(new Action(Action.PARK, pp));
            state_copy_1.compute_next_states_rec(id_vehicle + 1, Nv);
        }
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

            if (action == Action.WAIT) {
                // do nothing
            }
            else if (action == Action.EXIT) {
                // test if v cannot exit
                if (resulting_state.exist_upward_vehicle_below(v)) {
                    System.out.print(" --> " + v.getName() + " cannot exit because of upward vehicle below.\n");
                    return null;
                }
                // upd res_state
            }
            else if (action == Action.PARK) {
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
        for (Vehicle uv : this.dw_vehicles)
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
        for (Vehicle uv : this.dw_vehicles)
            if (!uv.isDownward()  &&  uv.x_position > (v.x_position + 0.1)  &&  uv.x_position < (pp.x_position - 0.1))
                return true;

        return false;
    }


}
