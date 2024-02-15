import java.util.ArrayList;
import java.util.Comparator;

public class Vehicle extends SceneElement {
    public static int EVENT_OK = 0;
    public static int EVENT_EXIT_TOP = 1;
    public static int EVENT_EXIT_BOTTOM = 2;
    public static int EVENT_VEHICLE_PARKED = 3;
    public static int EVENT_PASSED_PARKING = 4;
    public static int ACTION_COMPLETED = 5;
    private boolean downward;
    private boolean loaded;
    private double min_speed;
    private double max_speed;
    private double speed;
    private Action current_action;
    private boolean debug_step = false;

    // ---------------------------------------------------------------------------
    private boolean in_ramp;
    private boolean first = false;
    private boolean is_parking = false;
    private boolean is_unparking = false;
    private boolean is_exiting = false;
    private ArrayList<String> has_already_parked;


    public Vehicle(String nam, boolean dwd, boolean lowdid) {
        this.downward = dwd;
        this.loaded = lowdid;
        this.name = nam;
        this.in_ramp = true;
        this.x_position = 0;
        this.y_position = 0;
        this.current_action = new Action(Action.WAIT);
        if (this.downward) {
            this.min_speed = 4.0;
            this.max_speed = 8.0;
            this.speed = 5.0;
        }
        else {
            if (this.loaded) {
                this.speed = 3.0;
            } else {
                this.speed = 4.0;
            }
        }
        this.has_already_parked = new ArrayList<>();
    }

    public int step(double time_step) {          // because we don't want to keep updating while park/unpark actions are completing
        if (current_action.getId() == Action.PREPARK && !current_action.isFinished()) {
            ParkingPlace pp = (ParkingPlace) current_action.getParameter();
            if (downward) {
                if (y_position < pp.y_position - time_step * speed) {   // to make sure that the pp is always "below" v, otherwise the geometric test in enumerate_parking_places() fails
                    double Dpp1 = getDeltaYToClosestParkingPlace();
                    y_position += time_step * speed;                    //
                    double Dpp2 = getDeltaYToClosestParkingPlace();
                    if (Dpp1 * Dpp2 <= 0  &&  Dpp1 != 1000  &&  Dpp2 != 1000) {
                        System.out.println("[EVENT_PASSED_PARKING] " + name + " has passed ↓ a parking place during pre-park.");
                        return EVENT_PASSED_PARKING;
                    }
                } else {
                    System.out.println("[PREPARK] " + name + " has reached pre-parking place ↓.");
                    y_position += time_step * speed;
                    current_action.setFinished(true);
                    return ACTION_COMPLETED;
                }
            } else { // upward
                if (y_position > pp.y_position + time_step * speed) {   // to make sure that the pp is always "above" v, otherwise the geometric test in enumerate_parking_places() fails
                    double Dpp1 = getDeltaYToClosestParkingPlace();
                    y_position -= time_step * speed;                    //
                    double Dpp2 = getDeltaYToClosestParkingPlace();
                    if (Dpp1 * Dpp2 <= 0  &&  Dpp1 != 1000  &&  Dpp2 != 1000) {
                        System.out.println("[EVENT_PASSED_PARKING] " + name + " has passed ↑ a parking place during pre-park.");
                        return EVENT_PASSED_PARKING;
                    }
                } else {
                    System.out.println("[PREPARK] " + name + " has reached pre-parking place ↑.");
                    y_position -= time_step * speed;
                    current_action.setFinished(true);
                    return ACTION_COMPLETED;
                }
            }
        }
        else if (current_action.getId() == Action.PARK && !current_action.isFinished()) {
            if (x_position > -10 + time_step * State.parking_speed) {
                x_position -= time_step * State.parking_speed;
                is_parking = true;
            }
            else {
                //System.out.println("[PARK] " + name + " has parked.");
                current_action.setFinished(true);
                is_parking = false;
                return ACTION_COMPLETED;
            }
        }
        else if (current_action.getId() == Action.UNPARK && !current_action.isFinished()) {
            if (x_position < 0) {
                x_position += time_step * State.parking_speed;
                is_unparking = true;
            }
            else {
                System.out.println("[UNPARK] " + name + " has unparked.");
                current_action.setFinished(true);
                is_unparking = false;
                return ACTION_COMPLETED;
            }
        }
        else if (current_action.getId() == Action.GO_DOWN && !current_action.isFinished()) {
            if (y_position < State.y_max) {
                double Dpp1 = getDeltaYToClosestParkingPlace();
                y_position += time_step * speed;
                is_exiting = true;
                double Dpp2 = getDeltaYToClosestParkingPlace();
                if (Dpp1 * Dpp2 <= 0  &&  Dpp1 != 1000  &&  Dpp2 != 1000) {
                    System.out.println("[EVENT_PASSED_PARKING] " + name + " has passed ↓ a parking place.");
                    return EVENT_PASSED_PARKING;
                }
            }
            else {
                System.out.println("[GO_DOWN] " + name + " has exited bottom.");
                current_action.setFinished(true);
                is_exiting = false;
                return ACTION_COMPLETED;
            }
        }
        else if (current_action.getId() == Action.GO_UP && !current_action.isFinished()) {
            if (y_position > 0) {
                double Dpp1 = getDeltaYToClosestParkingPlace();
                y_position -= time_step * speed;
                is_exiting = true;
                double Dpp2 = getDeltaYToClosestParkingPlace();
                if (Dpp1 * Dpp2 <= 0  &&  Dpp1 != 1000  &&  Dpp2 != 1000) {
                    System.out.println("[EVENT_PASSED_PARKING] " + name + " has passed ↑ a parking place.");
                    return EVENT_PASSED_PARKING;
                }
            }
            else {
                System.out.println("[GO_UP] " + name + " has exited top.");
                current_action.setFinished(true);
                is_exiting = false;
                return ACTION_COMPLETED;
            }
        }
        else if (current_action.getId() == Action.ENTER && !current_action.isFinished()) {
            if (x_position > 0 + time_step * speed) {
                x_position -= time_step * State.parking_speed;
            }
            else {
                System.out.println("[ENTER] " + name + " has entered the ramp.");
                current_action.setFinished(true);
                return ACTION_COMPLETED;
            }
        }
        return EVENT_OK;
    }

    private double getDeltaYToClosestParkingPlace() {
        double shift;
        if (downward) shift = -5.0;
        else          shift =  5.0;
        double DeltaY = 1000;
        for (ParkingPlace pp : parentState.getParking_places()) {
            double delta = y_position + shift - pp.y_position;
            if (Math.abs(delta) < 2) { // to make sure we make 2 consecutive measurements to the same parking place
                if (Math.abs(delta) < Math.abs(DeltaY))
                    DeltaY = delta;
            }
        }
        return DeltaY;
    }

    public Vehicle getCopy(State dady) {
        Vehicle ret = new Vehicle(this.name, this.downward, this.loaded);
        ret.loaded = this.loaded;
        ret.x_position = this.x_position;
        ret.y_position = this.y_position;
        ret.speed = this.speed;
        ret.current_action = this.current_action.getCopy();
        ret.parentState = dady;
        ret.in_ramp = this.in_ramp;
        ret.first = this.first;
        ret.is_parking = this.is_parking;
        ret.is_exiting = this.is_exiting;
        ret.is_unparking = this.is_unparking;
        ret.has_already_parked.addAll(this.has_already_parked);
        return ret;
    }

    public String getName() {
        return name;
    }
    public boolean isDownward() { return downward; }
    public boolean isUpward() { return !downward; }
    public boolean isLoaded() { return loaded; }
    public void setLoaded(boolean loaded) { this.loaded = loaded; }
    public double getSpeed() {
        return speed;
    }

    public boolean is_parking() {
        return is_parking;
    }

    public boolean is_unparking() {
        return is_unparking;
    }

    public boolean is_exiting() {
        return is_exiting;
    }

    public void setCurrent_action(Action current_action) {
        this.current_action = current_action;
    }

    public Action getCurrent_action() {
        return current_action;
    }

    public boolean isVehicle() {return true;}
    public boolean isParkingPlace() {return false;}

    public boolean has_same_orientation_as(Vehicle v) {
        return (this.downward == v.downward);
    }

    public boolean has_opposite_orientation_as(Vehicle v) {
        return (this.downward != v.downward);
    }

    public void apply_current_action_effects() {
        if (current_action.getId() == Action.GO_DOWN && !is_exiting) {
            in_ramp = false;
            is_exiting = false;
        }
        else if (current_action.getId() == Action.GO_UP && !is_exiting) {
            in_ramp = false;
            is_exiting = false;
        }
        else if (current_action.getId() == Action.WAIT) {
            return;  // WAIT changes nothing
        }
        else if (current_action.getId() == Action.PARK && !is_parking) {
            ParkingPlace pp = (ParkingPlace)this.current_action.getParameter();
            parentState.setParked_vehicle(this.name, pp.name);
            parentState.removePreparked_vehicle(pp.name);
            set_has_already_parked(pp.name);
            x_position = -10;
            y_position = pp.y_position;
            is_parking = false;
        }
        else if (current_action.getId() == Action.PREPARK) {
            ParkingPlace pp = (ParkingPlace)this.current_action.getParameter();
            parentState.setPreparked_vehicle(this.name, pp.name);
            x_position = 0;
            y_position = pp.y_position;
        }
        else if (current_action.getId() == Action.UNPARK && !is_unparking) {
            ParkingPlace pp = (ParkingPlace)this.current_action.getParameter();
            parentState.removeParked_vehicle(pp.name);
            x_position = 0;
            y_position = pp.y_position;
            is_unparking = false;
        }
        else if (current_action.getId() == Action.ENTER) {
            x_position = 0;
            in_ramp = true;
            first = false;
            if (downward)
                parentState.update_first_in_top_queue();
            else
                parentState.update_first_in_bottom_queue();
         }
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getTypeString() {
        if (this.downward)
            return "       [↓] " + this.current_action.getName();
        else {
            if (loaded) return "       [↑] " + this.current_action.getName() + "  (LOADED)";
            else        return "       [↑] " + this.current_action.getName();
        }
    }

    public boolean isParked() {
        for (ParkingPlace pp : parentState.getParking_places()) {
            String pvname = pp.getParked_vehicle();
            if (pvname != null)
                if (pvname.equals(this.name))
                    return true;
        }
        return false;
    }

    public boolean isParkedAt(int pp_id) {
        String vname = parentState.getParking_places().get(pp_id).getParked_vehicle();
        if (vname == null)
            return false;
        if (vname.equals(this.name))
            return true;
        else
            return false;
    }

    public boolean isPreparkedAt(int pp_id) {
        String vname = parentState.getParking_places().get(pp_id).getPre_parked_vehicle();
        if (vname == null)
            return false;
        if (vname.equals(this.name))
            return true;
        else
            return false;
    }

    public boolean isPreparked() {
        for (ParkingPlace pp : parentState.getParking_places()) {
            String vname = pp.getPre_parked_vehicle();
            if (vname != null) {
                if (vname.equals(this.name))
                    return true;
            }
        }
        return false;
    }

    public ParkingPlace getPreParkingPlace() {
        for (ParkingPlace pp : parentState.getParking_places()) {
            String ppvname = pp.getPre_parked_vehicle();
            if (ppvname != null)
                if (ppvname.equals(this.name))
                    return pp;
        }

        return null;
    }

    public ParkingPlace getParkingPlace() {
        for (ParkingPlace pp : parentState.getParking_places()) {
            String pvname = pp.getParked_vehicle();
            if (pvname != null)
                if (pvname.equals(this.name))
                    return pp;
        }

        return null;
    }

    public void set_has_already_parked(String ppname) {
        if (ppname.equals("virtual"))
            return;

        if (has_already_parked.contains(ppname)) {
            System.out.println("The key was already there : impossible!");
            System.exit(0);
        } else {
            has_already_parked.add(ppname);
        }
    }

    public boolean has_already_parked(String ppname) {
        return has_already_parked.contains(ppname);
    }

    public boolean isIn_ramp() {
        return in_ramp;
    }

    public void setIn_ramp(boolean in_ramp) {
        this.in_ramp = in_ramp;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }
}

class VehicleXPositionComparatorReverse implements Comparator<Vehicle> {
    @Override
    public int compare(Vehicle v1, Vehicle v2) {
        if (v1.getY_position() > v2.getY_position())
            return -1;
        else if (v1.getY_position() < v2.getY_position())
            return 1;
        return 0;
    }
}


