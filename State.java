import java.util.*;

public class State {
    public final static double parking_speed = 3.34; // m/s
    public final static double y_min = 0.0;   // m
    public final static double y_max = 100; // m
    public final static double SAFETY_DISTANCE = 5.0; // m (center-to-center)
    public final static double METRIC_EQUALITY_THRESHOLD = 3.0; // m (center-to-center) larger => fewer states => faster, but less complete
    private double start_time = 0;
    public double g_score = 1000;
    public double f_score = 1000;
    public State cameFrom = null;
    private ArrayList<Vehicle> vehicles;
    private int Nv = 0;
    private int Npp = 0;
    private double duration;
    private ArrayList<ParkingPlace> parking_places;
    private static ArrayList<State> next_states;
    public static MiniSimulator mini_simulator;
    // ----------------------------------------------------------------------
    public int depth = 0;


    public State() {
        this.vehicles = new ArrayList<Vehicle>();
        this.parking_places = new ArrayList<ParkingPlace>();
        this.duration = 0;
    }

    public static void setMini_simulator(MiniSimulator mini_simulator) {
        State.mini_simulator = mini_simulator;
    }

    public void addVehicle(Vehicle v, boolean inQueue) {
        vehicles.add(v);
        Nv ++;

        if (inQueue) {
            v.setIn_ramp(false);
            v.setX_position(15);
            if (v.isDownward()) {
                v.setY_position(0);
                update_first_in_top_queue();
            } else {
                v.setY_position(State.y_max);
                update_first_in_bottom_queue();
            }
        } else {
            v.setIn_ramp(true);
        }
        
        v.setParentState(this);
    }

    public void update_first_in_top_queue() {
        // Sets the "first" flag to TRUE for the first downward vehicle in the list, and FALSE for other downwards vehicles
        boolean found_first = false;
        for (Vehicle v : vehicles) {
            if (!v.isIn_ramp()) {
                if (v.isDownward()) {
                    if (!found_first) {
                        v.setFirst(true);
                        found_first = true;
                    } else {
                        v.setFirst(false);
                    }
                }
            }
        }
    }

    public void update_first_in_bottom_queue() {
        // Sets the "first" flag to TRUE for the first upward vehicle in the list, and FALSE for other upward vehicles
        boolean found_first = false;
        for (Vehicle v : vehicles) {
            if (!v.isIn_ramp()) {
                if (v.isUpward()) {
                    if (!found_first) {
                        v.setFirst(true);
                        found_first = true;
                    } else {
                        v.setFirst(false);
                    }
                }
            }
        }
    }

    public void addParkingPlace(ParkingPlace pp) {
        parking_places.add(pp);
        pp.id = Npp;
        Npp ++;
        pp.setParentState(this);
    }
    
    public void setParked_vehicle(String vname, String ppname) {
        if (ppname.equals("virtual"))
            return;

        ParkingPlace pp = getParkingPlaceByName(ppname);
        pp.setParked_vehicle(vname);
        Vehicle v = getVehicleByName(vname);
        v.setX_position(pp.x_position);
        v.setY_position(pp.y_position);
    }

    public void removeParked_vehicle(String ppname) {
        if (ppname.equals("virtual"))
            return;

        getParkingPlaceByName(ppname).setParked_vehicle(null);
    }

    public void setPreparked_vehicle(String vname, String ppname) {
        if (ppname.equals("virtual"))
            return;

        ParkingPlace pp = getParkingPlaceByName(ppname);
        pp.setPre_parked_vehicle(vname);
        Vehicle v = getVehicleByName(vname);
        v.setX_position(0);
        v.setY_position(pp.y_position);
    }

    public void removePreparked_vehicle(String ppname) {
        if (ppname.equals("virtual"))
            return;

        getParkingPlaceByName(ppname).setPre_parked_vehicle(null);
    }

    public boolean allVehiclesWaiting() {
        for (Vehicle v : vehicles)
            if (!(v.getCurrent_action().getId() == Action.WAIT))
                return false;

        return true;
    }

    public void removeVehicle(String vname) {
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle x = vehicles.get(i);
            if (x.getName().equals(vname)) {
                vehicles.remove(i);
                Nv --;
                return;
            }
        }
    }

    public State getCopy() {
        State ret = new State();
        // copy vehicle lists
        for (Vehicle v : this.vehicles)
            ret.vehicles.add(v.getCopy(ret));

        // copy parking places list
        for (ParkingPlace pp : this.parking_places) {
            ret.parking_places.add(pp.getCopy());
        }

        ret.Nv = this.Nv;
        ret.Npp = this.Npp;
        ret.start_time = this.start_time;
        ret.duration = this.duration;
        return ret;
    }

    /*public boolean equals(State s) {
        if (this.Nv != s.Nv)
            return false;
        for (int i = 0; i < Nv; i++) {
            Vehicle v1 = vehicles.get(i);
            Vehicle v2 = s.getVehicles().get(i);

            // the order of vehicles is preserved in .vehicles:
            // - when the state is copied
            // - when a vehicle is removed (and we never add vehicles during planning)
            // BUT, when there is only
            if (Nv == 1 && !v1.name.equals(v2.name))
                return false;

            if (!v1.name.equals(v2.name)) {
                System.out.println("Hello Franziska : the order is not preserved (remember?)");
                System.exit(0);
            }

            //if (v1.getCurrent_action().getId() != v2.getCurrent_action().getId())  // Dramatically increases the number of states !
            //    return false;

            if (Math.abs(v1.y_position - v2.y_position) > METRIC_EQUALITY_THRESHOLD)
                return false;
            if (Math.abs(v1.x_position - v2.x_position) > METRIC_EQUALITY_THRESHOLD)
                return false;

            // The vehicles have the same position, but they perform opposite actions, hence the state is different.
            if (v1.getCurrent_action().getId() == Action.PARK && v2.getCurrent_action().getId() == Action.UNPARK)
                return false;
            if (v1.getCurrent_action().getId() == Action.UNPARK && v2.getCurrent_action().getId() == Action.PARK)
                return false;
        }
        return true;
    }*/

    // Alternative equals function: checking the name of the vehicle instead of relying on the order in the list.
    // Slower, but could avoid a bug in the future!
    public boolean equals(State s) {
        if (this.Nv != s.Nv)
            return false;

        int count_matches = 0;
        for (Vehicle v1 : vehicles) {
            for (Vehicle v2 : s.vehicles) {
                if (v1.name == v2.name) {
                    count_matches ++;

                    //if (v1.getCurrent_action().getId() != v2.getCurrent_action().getId())  // Dramatically increases the number of states !
                    //    return false;

                    if (Math.abs(v1.y_position - v2.y_position) > METRIC_EQUALITY_THRESHOLD)
                        return false;
                    if (Math.abs(v1.x_position - v2.x_position) > METRIC_EQUALITY_THRESHOLD)
                        return false;

                    // The vehicles have the same position, but they perform opposite actions, hence the state is different.
                    if (v1.getCurrent_action().getId() == Action.PARK && v2.getCurrent_action().getId() == Action.UNPARK)
                        return false;
                    if (v1.getCurrent_action().getId() == Action.UNPARK && v2.getCurrent_action().getId() == Action.PARK)
                        return false;
                }
            }
        }

        if (count_matches < Nv)
            return false;

        return true;
    }

    public String getOrderedNames() {
        ArrayList<SceneElement> elts = new ArrayList<SceneElement>();
        elts.addAll(this.vehicles);
        elts.addAll(this.parking_places);
        Collections.sort(elts, new SceneElementYPositionComparator());
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
        Collections.sort(elts, new SceneElementYPositionComparator());
        String ret = "";
        for (SceneElement se : elts) {
            ret += se.getName();
            ret += " ";
            ret += String.format("(%.1f, %.1f)", se.getX_position(), se.getY_position());
            ret += se.getTypeString();

            if (!se.getTypeString().equals(" [parking]")) {
                if (((Vehicle)se).is_parking())
                    ret += "   parking operation in progress!";
                if (((Vehicle)se).is_preparking())
                    ret += "   PREparking operation in progress!";
                if (((Vehicle)se).is_unparking())
                    ret += "   UNparking operation in progress!";
                if (((Vehicle)se).is_exiting())
                    ret += "   Exiting operation in progress!";

                ParkingPlace ppp = ((Vehicle)se).getPreParkingPlace();
                ParkingPlace pp = ((Vehicle)se).getParkingPlace();
                if (ppp != null)
                    ret += "  (PREPARKED at " + ppp.name + ")";
                if (pp != null)
                    ret += "  (PARKED at " + pp.name + ")";
            }
            ret += "\n";
        }
        return ret;
    }

    public String current_action_str() {
        ArrayList<Vehicle> elts = new ArrayList<>();
        elts.addAll(this.vehicles);
        Collections.sort(elts, new SceneElementYPositionComparator());
        String ret = "";
        for (Vehicle se : elts) {
            int id = se.getCurrent_action().getId();
            String id_str = se.getName() + "." + se.getCurrent_action().getName();
            ret += id_str;
            if (id == Action.PREPARK)
                ret += "(" + se.getCurrent_action().getParameter().name + ") ";
            else if (id == Action.PARK)
                ret += "(" + se.getPreParkingPlace().getName() + ") ";
            else if (id == Action.UNPARK)
                ret += "(" + se.getParkingPlace().getName() + ") ";
            else                   ret += "     ";
            ret += " ";
        }
        ret += "\n";
        return ret;
    }

    public String vehicles_action_str() {
        String ret = "";
        for (Vehicle v : this.vehicles) {
            int id = v.getCurrent_action().getId();
            String id_str = v.getCurrent_action().getName();
            ret += v.getName() + ": " + id_str;
            if (id == Action.PARK) ret += "(" + v.getCurrent_action().getParameter().name + ") ";
            else if (id == Action.PREPARK) ret += "(" + v.getCurrent_action().getParameter().name + ") ";
            else                   ret += "     ";
            ret += " ";
        }
        return ret;
    }

    public ArrayList<State> get_next_states() {
        ArrayList<State> ret = new ArrayList<State>();
        next_states = new ArrayList<State>();

        // enumerated possible actions to be applied to this state, and store resulting states in this.next_states
        enumerate_actions(0, vehicles.size());
        System.out.println("     [STATE] Enumeration led to " + next_states.size() + " candidate states.");

        System.out.println("------------------------------------- <");
        System.out.println(this);
        System.out.println("--------------------");
        System.out.printf("Result of enumerate: (depth=%d)\n", depth);
        for (State s : next_states) {
            try {
                System.out.print(s.current_action_str());
            } catch (Exception e) {
                System.out.print(e);
            }
        }
        System.out.println("------------------------------------- >");

        //TODO here: remove all states with logical conflicts:
        //  * PARK / PREPARK / UNPARK with same destination and different vehicles
        //  * ...or is everything handled by the preconditions? --> depends on the order?

        int ins = 0;
        for (State s : next_states) {
            ins ++;
            System.out.printf("[get_next_states] next_state %d / %d\n", ins, next_states.size());
            if (!s.only_wait_actions()) {
                State state_resulting_from_simulation = mini_simulator.simulate(s);  // simulate(x) modifies x, so s is the geometrical result of applying current_actions on s' parent
                if (state_resulting_from_simulation != null)
                    ret.add(state_resulting_from_simulation);
            }
        }

        System.out.println("     [STATE] Simulation has kept " + ret.size() + " states.");
        return ret;
    }


    public void assignActions(ArrayList<Vehicle> vehicles_list) {
        for (Vehicle v1 : this.vehicles) {
            for (Vehicle v2 : vehicles_list) {
                if (v1.name.equals(v2.name)) {
                    v1.setCurrent_action(v2.getCurrent_action());
                    v1.getCurrent_action().setFinished(false);
                    if (v1.getCurrent_action().getId() == Action.PARK) {
                        v1.setX_position(0);         // initial position for park action
                        v1.setY_position(v1.getCurrent_action().getParameter().getY_position());
                    }
                    if (v1.getCurrent_action().getId() == Action.UNPARK) {
                        v1.setX_position(-10);     // initial position for unpark action
                        v1.setY_position(v1.getCurrent_action().getParameter().getY_position());
                    }
                }
            }
        }
    }

    public void finish_wait_actions() {
        for (Vehicle v : vehicles) {
            if (v.getCurrent_action().getId() == Action.WAIT)
                v.getCurrent_action().setFinished(true);
        }
    }

    public void apply_finished_actions_effects() {
        for (Vehicle v : vehicles)
            if (v.getCurrent_action().isFinished())
                v.apply_current_action_effects();
    }

    public boolean only_wait_actions() {
        for (Vehicle v : vehicles) {
            if (v.getCurrent_action().getId() != Action.WAIT)
                return false;
        }

        return true;
    }


    public void enumerate_actions(int vehicle_index, int Nv) {
        if (vehicle_index == Nv) {
            next_states.add(this);
        }
        else {
            Vehicle v = vehicles.get(vehicle_index);

            // ------------------  special case for ongoing PARK, UNPARK and GO_UP/DOWN actions : cannot be interrupted ------------------//
            if (v.is_parking()) {
                // we assume that preconditions are still fulfilled
                State state_copy_2 = this.getCopy();
                state_copy_2.vehicles.get(vehicle_index).setCurrent_action(new Action(Action.PARK, v.getPreParkingPlace()));
                state_copy_2.enumerate_actions(vehicle_index + 1, Nv);
                return; // because this vehicle in this state cannot do any other action
            }

            if (v.is_unparking()) {
                // we assume that preconditions are still fulfilled
                State state_copy_4 = this.getCopy();
                state_copy_4.vehicles.get(vehicle_index).setCurrent_action(new Action(Action.UNPARK, v.getParkingPlace()));
                state_copy_4.enumerate_actions(vehicle_index + 1, Nv);
                return; // because this vehicle in this state cannot do any other action
            }

            if (v.is_exiting()) {
                // we assume that preconditions are still fulfilled
                State state_copy_5 = this.getCopy();
                if (v.isDownward()) state_copy_5.vehicles.get(vehicle_index).setCurrent_action(new Action(Action.GO_DOWN));
                else                state_copy_5.vehicles.get(vehicle_index).setCurrent_action(new Action(Action.GO_UP));
                state_copy_5.enumerate_actions(vehicle_index + 1, Nv);
                return; // because this vehicle in this state cannot do any other action
            }

            // ------------------  normal enumeration : the vehicle may do other actions ------------------//
            // enumerate GO_DOWN actions
            boolean exit_bottom_precondition = fulfills_preconditions(v, Action.GO_DOWN);
            if (exit_bottom_precondition) {
                boolean exit_bottom_feasible = !this.is_upward_vehicle_below_going_up(v);       // GEOMETRIC  TODO: but should we keep it here?

                if (exit_bottom_feasible) {
                    State state_copy_1 = this.getCopy();
                    state_copy_1.vehicles.get(vehicle_index).setCurrent_action(new Action(Action.GO_DOWN));
                    state_copy_1.enumerate_actions(vehicle_index + 1, Nv);
                } else {
                    //System.out.println("[ENUM FILTER!!!" + vehicle_index + "] " + v.getName() + " cannot GO_DOWN because of Upw vehicle below.");
                }
            } else {
                //System.out.println("[ENUM FILTER!!!" + vehicle_index + "] " + v.getName() + " cannot GO_DOWN because of precondition false.");
            }

            // enumerate GO_UP actions
            boolean exit_top_precondition = fulfills_preconditions(v, Action.GO_UP);
            if (exit_top_precondition) {
                boolean exit_top_feasible = !this.is_downward_vehicle_above_going_down(v);       // GEOMETRIC  TODO: but should we keep it here?

                if (exit_top_feasible) {
                    State state_copy_1 = this.getCopy();
                    state_copy_1.vehicles.get(vehicle_index).setCurrent_action(new Action(Action.GO_UP));
                    state_copy_1.enumerate_actions(vehicle_index + 1, Nv);
                } else {
                    //System.out.println("[ENUM FILTER!!!" + vehicle_index + "] " + v.getName() + " cannot GO_UP because of Dw vehicle above.");
                }
            } else {
                //System.out.println("[ENUM FILTER!!!" + vehicle_index + "] " + v.getName() + " cannot GO_UP because of precondition false.");
            }

            // enumerate PARK actions
            if (v.getPreParkingPlace() != null) { // necessary to check preconditions!
                boolean park_precondition = fulfills_preconditions(v, Action.PARK, v.getPreParkingPlace().id);
                if (park_precondition) {
                    State state_copy_2 = this.getCopy();
                    state_copy_2.vehicles.get(vehicle_index).setCurrent_action(new Action(Action.PARK, v.getPreParkingPlace()));
                    state_copy_2.enumerate_actions(vehicle_index + 1, Nv);
                } else {
                    //System.out.println("[ENUM FILTER!!!" + vehicle_index + "] " + v.getName() + " cannot PARK because of precondition false.");
                }
            }
            else {
                //System.out.println("[ENUM FILTER!!!" + vehicle_index + "] " + v.getName() + " cannot PARK because NO PREPARKING PLACE WAS FOUND !!!!!!!!!!!!!!!");
            }

            // enumerate PREPARK actions
            for (int pp_id = 0; pp_id < this.Npp; pp_id++) {
                boolean prepark_precondition = fulfills_preconditions(v, Action.PREPARK, pp_id);
                if (prepark_precondition) {
                    boolean prepark_feasible = !is_vehicle_going_opposite_in_between(v, pp_id);
                    if (prepark_feasible) {
                        State state_copy_3 = this.getCopy();
                        state_copy_3.enumerate_parking_places(vehicle_index, v.name, pp_id, Nv, Action.PREPARK);
                    } else {
                        //System.out.println("[ENUM FILTER!!!" + vehicle_index + "] " + v.getName() + " cannot PREPARK because of vehicle between it and pp[" + pp_id + "].");
                    }
                } else {
                    //System.out.println("[ENUM FILTER!!!" + vehicle_index + "] " + v.getName() + " cannot PREPARK because of precondition false.");
                }
            }

            // enumerate UNPARK actions
            if (v.getParkingPlace() != null) { // necessary to check preconditions!
                boolean unpark_precondition = fulfills_preconditions(v, Action.UNPARK, v.getParkingPlace().id);
                if (unpark_precondition) {
                    State state_copy_4 = this.getCopy();
                    state_copy_4.vehicles.get(vehicle_index).setCurrent_action(new Action(Action.UNPARK, v.getParkingPlace()));
                    state_copy_4.enumerate_actions(vehicle_index + 1, Nv);
                }
            }

            // enumerate ENTER actions
            boolean enter_precondition = fulfills_preconditions(v, Action.ENTER);
            if (enter_precondition) {
                State state_copy_6 = this.getCopy();
                state_copy_6.vehicles.get(vehicle_index).setCurrent_action(new Action(Action.ENTER));
                state_copy_6.enumerate_actions(vehicle_index + 1, Nv);
            } else {
                //System.out.println("[ENUM FILTER!!!" + vehicle_index + "] " + v.getName() + " cannot ENTER because of precondition false.");
            }

            // enumerate WAIT actions
            boolean wait_precondition = fulfills_preconditions(v, Action.WAIT);
            if (wait_precondition) {
                State state_copy_5 = this.getCopy();
                state_copy_5.vehicles.get(vehicle_index).setCurrent_action(new Action(Action.WAIT));
                state_copy_5.enumerate_actions(vehicle_index + 1, Nv);
            } else {
                //System.out.println("[ENUM FILTER!!!" + vehicle_index + "] " + v.getName() + " cannot WAIT because of precondition false.");
            }

            // if no action applies to this vehicle, we assign the WAIT action and continue with the next vehicle
            //State state_copy_7 = this.getCopy();
            //state_copy_7.enumerate_actions(vehicle_index + 1, Nv);
        }
    }

    public void enumerate_parking_places(int vehicle_index, String vname, int id_parking, int Nv, int id_action) {
        if (vehicle_index == Nv) {
            this.next_states.add(this);
        }
        else {
            // Assigns parking places, prunes out parking places which are above/below depending on vehicle's direction
            State state_copy_22 = this.getCopy();
            ParkingPlace pp = state_copy_22.parking_places.get(id_parking);
            Vehicle v = state_copy_22.getVehicleByName(vname);

            boolean feasible;
            if (v.isDownward()) feasible = pp.isBelow(v);   // GEOMETRIC
            else                feasible = pp.isAbove(v);

            if (feasible) {
                v.setCurrent_action(new Action(id_action, pp));
                state_copy_22.enumerate_actions(vehicle_index + 1, Nv);
            }
        }
    }

    public boolean fulfills_preconditions(Vehicle v, int action, int... param) {
        if (action == Action.GO_DOWN) {
            if (!v.isPreparked() && !v.isParked() && v.isIn_ramp() && v.isDownward())
                return true;
            else
                return false;
        }
        else if (action == Action.GO_UP) {
            if (!v.isPreparked() && !v.isParked() && v.isIn_ramp() && v.isUpward())
                return true;
            else
                return false;
        }
        else if (action == Action.PREPARK) {
            int pp_id = param[0];
            if (!v.is_preparking() && !v.isPreparked() && !v.isParked() && v.isIn_ramp() && !v.isLoaded() && is_prepark_clear(pp_id))
                return true;
            else
                return false;
        }
        else if (action == Action.PARK) {
            int pp_id = param[0];                                                                   // to avoid multiple parking-unparking
            String pname = parking_places.get(pp_id).name;
            if (!v.isParked() && is_park_clear(pp_id) && v.isPreparkedAt(pp_id) && v.isIn_ramp() && !v.has_already_parked(pname))
                return true;
            else
                return false;
        }
        else if (action == Action.UNPARK) {
            int pp_id = param[0];
            if (v.isParkedAt(pp_id) && is_prepark_clear(pp_id))
                return true;
            else
                return false;
        }
        else if (action == Action.WAIT) {
            //if (v.isLoaded() && v.isIn_ramp())   // loaded vehicles cannot stop!
            //    return false;
            if (v.isParked() || !v.isIn_ramp())
                return true;
            else
                return false;
        }
        else if (action == Action.ENTER) {
            if (!v.isIn_ramp() && v.isFirst())
                return true;
            else
                return false;
        }
        else {
            System.out.println("This action has a weird id!");
            System.exit(0);
            return false;
        }
    }

    public boolean is_park_clear(int pp_id) {
        if (parking_places.get(pp_id).getParked_vehicle() == null)
            return true;
        else
            return false;
    }

    public boolean is_prepark_clear(int pp_id) {
        if (parking_places.get(pp_id).getPre_parked_vehicle() == null)
            return true;
        else
            return false;
    }

    public double getStart_time() {
        return start_time;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public Vehicle getVehicleByName(String vname) {
        for (Vehicle v : vehicles)
            if (v.name.equals(vname))
                return v;
        return null;
    }

    public ParkingPlace getParkingPlaceByName(String ppname) {
        for (ParkingPlace pp : parking_places)
            if (pp.name.equals(ppname))
                return pp;
        return null;
    }

    public ArrayList<ParkingPlace> getParking_places() {
        return parking_places;
    }

    public void increaseStart_time(double start_time) {
        this.start_time += start_time;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    Vehicle get_closest_upward_vehicle_below(Vehicle v) {
        Vehicle closest = null;
        double min_dist = 1000;
        for (Vehicle upv : this.vehicles)
            if (!v.isDownward()) {
                if (upv.y_position > v.y_position) {
                    double d = upv.y_position - v.y_position;
                    if (d < min_dist) {
                        min_dist = d;
                        closest = upv;
                    }
                }
            }
        return closest;
    }

    boolean is_loaded_upward_vehicle_below(Vehicle v) {
        for (Vehicle upv : this.vehicles)
            if (upv.isUpward() && v.isLoaded()) {
                if (upv.y_position > v.y_position)
                    return true;
            }
        return false;
    }

    boolean is_upward_vehicle_below_going_up(Vehicle v) {
        for (Vehicle upv : this.vehicles)
            if (upv.isUpward() && upv.getCurrent_action().getId() == Action.GO_UP) {
                if (upv.y_position > v.y_position)
                    return true;
            }
        return false;
    }

    boolean is_downward_vehicle_above_going_down(Vehicle v) {
        for (Vehicle dwv : this.vehicles)
            if (dwv.isDownward() && dwv.getCurrent_action().getId() == Action.GO_DOWN) {
                if (dwv.y_position < v.y_position)
                    return true;
            }
        return false;
    }

    boolean is_vehicle_going_opposite_in_between(Vehicle v, int pp_id) {
        // Checks if there is a vehicle between v and the target parking place,
        // coming in opposite direction, assigned with the action GO_UP or GO_DOWN
        // And also checks if the parking place is ahead w.r.t the direction of the vehicle
        int opposite_action;
        if (v.isDownward()) opposite_action = Action.GO_UP;
        else                opposite_action = Action.GO_DOWN;
        ParkingPlace pp = parking_places.get(pp_id);
        for (Vehicle vopp : this.vehicles) {
            if (v.isDownward() == !vopp.isDownward() && vopp.getCurrent_action().getId() == opposite_action) {
                if (v.isDownward()) {
                    if (v.getY_position() < vopp.getY_position() && vopp.getY_position() < pp.getY_position())
                        return true;
                } else {
                    if (v.getY_position() > vopp.getY_position() && vopp.getY_position() > pp.getY_position())
                        return true;
                }
            }
        }
        return false;
    }

    boolean is_downward_vehicle_above(Vehicle v) {
        for (Vehicle dwv : this.vehicles)
            if (dwv.isDownward()) {
                if (dwv.y_position < v.y_position)
                    return true;
            }
        return false;
    }

    public Vehicle get_closest_vehicle_ahead(Vehicle v) { // (in-ramp, non-parked)
        Vehicle closest = null;
        double dist;
        double min_dist = 1000;
        for (Vehicle cv : this.vehicles) {
            if (cv.isIn_ramp() && !cv.is_parking() && !cv.isParked()) {
                if (v.isDownward()) dist = cv.y_position - v.y_position;
                else dist = v.y_position - cv.y_position;
                if (dist > 0 && dist < min_dist) {
                    min_dist = dist;
                    closest = cv;
                }
            }
        }
        return closest;
    }

    public Vehicle get_closest_opposite_vehicle_ahead(Vehicle v) { // (in-ramp, non-parked)
        Vehicle closest = null;
        double dist;
        double min_dist = 1000;
        for (Vehicle cv : this.vehicles) {
            if (cv.isIn_ramp() && !cv.is_parking() && !cv.isParked() && cv.has_opposite_orientation_as(v)) {
                if (v.isDownward()) dist = cv.y_position - v.y_position;
                else dist = v.y_position - cv.y_position;
                if (dist > 0 && dist < min_dist) {
                    min_dist = dist;
                    closest = cv;
                }
            }
        }
        return closest;
    }

    public ParkingPlace get_closest_parkingplace_ahead(Vehicle v) { // (in-ramp, non-parked)
        ParkingPlace closest = null;
        double dist;
        double min_dist = 1000;
        for (ParkingPlace cpp : this.parking_places) {
            if (v.isDownward()) dist = cpp.y_position - v.y_position;
            else                dist = v.y_position - cpp.y_position;
            if (dist > 0 && dist < min_dist) {
                min_dist = dist;
                closest = cpp;
            }
        }
        return closest;
    }

    public SceneElement get_closest_sceneelement_ahead(Vehicle v) { // (in-ramp, non-parked)
        SceneElement closest = null;
        Vehicle cv = get_closest_vehicle_ahead(v);
        ParkingPlace cpp = get_closest_parkingplace_ahead(v);

        if (cv == null && cpp == null)
            return null;
        else if (cv == null)
            return cpp;
        else if (cpp == null)
            return cv;
        else {
            if (v.isDownward()) {
                if (cv.y_position < cpp.y_position) return cv;
                else return cpp;
            } else {
                if (cv.y_position > cpp.y_position) return cv;
                else return cpp;
            }
        }
    }

    public Vehicle collides_with(Vehicle v) {
        for (Vehicle v2 : vehicles)
            if (v != v2 && v2.isIn_ramp()) {
                if (Math.abs(v.getY_position() - v2.getY_position()) < State.SAFETY_DISTANCE) {
                    if (Math.abs(v.getX_position() - v2.getX_position()) < State.SAFETY_DISTANCE) {
                        //System.out.println("((( COLLISION ))) between " + v.getName() + " and " + v2.getName() + " !!!");
                        return v2;
                    }
                }
            }
        return null;
    }

    ArrayList<ParkingPlace> get_parking_places_below(Vehicle v) {
        ArrayList<ParkingPlace> ret = new ArrayList<>();
        for (ParkingPlace pp : this.parking_places)
            if (pp.y_position > v.y_position)
                ret.add(pp);

        return ret;
    }

    public int getNupVehicles() {
        int ret = 0;
        for (Vehicle v : vehicles)
            if (v.isUpward())
                ret ++;
        return ret;
    }

    public int getNdownVehicles() {
        int ret = 0;
        for (Vehicle v : vehicles)
            if (v.isDownward())
                ret ++;
        return ret;
    }

    public int getNv_in_ramp() {
        int nvir = 0;
        for (Vehicle v : vehicles)
            if (v.isIn_ramp())
                nvir ++;
        return nvir;
    }

    public int get_N_waiting_vehicles() {
        int ret = 0;
        for (Vehicle v : vehicles) {
            if (v.getCurrent_action().getId() != Action.WAIT)
                ret ++;
        }

        return ret;
    }

    public int getNdownVehicles_in_ramp() {
        int ret = 0;
        for (Vehicle v : vehicles)
            if (v.isDownward() && v.isIn_ramp())
                ret ++;
        return ret;
    }

    public int getNv() {
        return Nv;
    }
}
