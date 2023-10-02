import java.util.Comparator;

public class Vehicle extends SceneElement {
    public static int EVENT_OK = 0;
    public static int EVENT_EXIT_TOP = 1;
    public static int EVENT_EXIT_BOTTOM = 2;
    public static int EVENT_VEHICLE_PARKED = 3;
    public static int EVENT_PASSED_PARKING = 4;
    private boolean downward;
    private double parking_progress = 0;
    private double min_speed;
    private double max_speed;
    private double speed;
    private boolean is_out;
    private Action current_action;
    private boolean debug_step = false;
    // ---------------------------------------------------------------------------
    private boolean in_ramp;
    private boolean first = false;


    public Vehicle(String nam, boolean dwd) {
        this.downward = dwd;
        this.name = nam;
        this.is_out = false;
        this.in_ramp = true;
        this.x_position = 0;
        if (this.downward) {
            this.min_speed = 4.0;
            this.max_speed = 8.0;
            this.speed = 5.7;
            this.current_action = new Action(Action.WAIT);
        }
        else {
            this.speed = 2.1;
            this.current_action = new Action(Action.GO_UP);
        }
    }

    public int step(double time_step) {
        double Dpp1 = getDeltaXToClosestParkingPlace();
        if (current_action.getId() == Action.EXIT) {
            if (parking_progress > 0) { // then must leave the parking place first
                parking_progress -= time_step / State.parking_time;
                if (debug_step) System.out.println(name + " leaving parking");
                return EVENT_OK; // not finished
            }
            else {
                if (x_position < State.x_max) {
                    x_position += time_step * speed;
                    if (debug_step) System.out.println(name + " heading to exit");
                    // check if parking place was passed
                    double Dpp2 = getDeltaXToClosestParkingPlace();
                    if (Dpp1 * Dpp2 < 0  &&  Dpp1 != 1000  &&  Dpp2 != 1000) {
                        System.out.println("[EVENT_PASSED_PARKING] " + name + " has passed a parking place.");
                        return EVENT_PASSED_PARKING;
                    }
                    else
                        return EVENT_OK;
                }
                else {
                    current_action.setFinished(true);
                    System.out.println("[EVENT_EXIT_BOTTOM] " + name + " has exited bottom.");
                    return EVENT_EXIT_BOTTOM;
                }
            }
        }
        else if (this.current_action.getId() == Action.PARK) {
            // first we need to possibly unpark...!
            ParkingPlace pp = (ParkingPlace) current_action.getParameter();
            if (parking_progress > 0  &&  Math.abs(pp.x_position - x_position) > 1.0) { // case already in a parking place other than pp
                parking_progress -= time_step / State.parking_time;
                if (debug_step) System.out.println(name + " leaving previous parking");
                if (parentState.hasParkedVehicle(pp))
                    parentState.removeParked_vehicle(this);
                return EVENT_OK; // not finished
            }
            else {
                if (x_position < pp.x_position  &&  parking_progress <= 0) {
                    x_position += time_step * speed;
                    if (debug_step) System.out.println(name + " heading to parking " + pp.getName());
                    // check if parking place was passed
                    double Dpp2 = getDeltaXToClosestParkingPlace();
                    if (Dpp1 * Dpp2 < 0  &&  Dpp1 != 1000  &&  Dpp2 != 1000) {
                        System.out.println("[EVENT_PASSED_PARKING] " + name + " has passed a parking place.");
                        return EVENT_PASSED_PARKING;
                    }
                    else
                        return EVENT_OK;
                }
                else if (parking_progress < 1) {
                    parking_progress += time_step / State.parking_time;
                    if (debug_step) System.out.println(name + " parking at " + pp.getName());
                    return EVENT_OK; // not finished
                }
                else {
                    current_action.setFinished(true);
                    System.out.println("[EVENT_VEHICLE_PARKED] " + name + " has parked.");
                    parentState.setParked_vehicle(this, pp);
                    return EVENT_VEHICLE_PARKED;
                }
            }
        }
        else if (!downward) {
            x_position -= time_step * speed;
            if (x_position < 0) {
                current_action.setFinished(true);
                System.out.println("[EVENT_EXIT_TOP] " + name + " has exited top.");
                return EVENT_EXIT_TOP;
            }
            else {
                // check if parking place was passed
                double Dpp2 = getDeltaXToClosestParkingPlace();
                if (Dpp1 * Dpp2 < 0  &&  Dpp1 != 1000  &&  Dpp2 != 1000) {
                    System.out.println("[EVENT_PASSED_PARKING] " + name + " has passed a parking place.");
                    return EVENT_PASSED_PARKING;
                }
                else
                    return EVENT_OK;
            }
        }
        else {
            // case action = WAIT
            return EVENT_OK; // not finished
        }
    }

    private double getDeltaXToClosestParkingPlace() {
        double shift;
        if (downward) shift = -5.0;
        else          shift =  5.0;
        double DeltaX = 1000;
        for (ParkingPlace pp : parentState.getParking_places()) {
            double delta = x_position + shift - pp.x_position;
            if (Math.abs(delta) < 2) { // to make sure we make 2 consecutive measurements to the same parking place
                if (Math.abs(delta) < Math.abs(DeltaX))
                    DeltaX = delta;
            }
        }
        return DeltaX;
    }

    public Vehicle getCopy() {
        Vehicle ret = new Vehicle(this.name, this.downward);
        ret.x_position = this.x_position;
        ret.speed = this.speed;
        ret.current_action = this.current_action.getCopy();
        ret.parking_progress = this.parking_progress;
        ret.is_out = this.is_out;
        ret.parentState = this.parentState;
        ret.in_ramp = this.in_ramp;
        ret.first = this.first;
        ret.id = this.id;
        return ret;
    }

    public String getName() {
        return name;
    }

    public double getParking_progress() {
        return parking_progress;
    }

    public void setParking_progress(double pprog) {
        parking_progress = pprog;
    }

    public boolean isDownward() {
        return downward;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isOut() {
        return is_out;
    }

    public void setIs_out(boolean io) {
        is_out = io;
        in_ramp = !is_out;
    }

    public void setCurrent_action(Action current_action) {
        this.current_action = current_action;
    }

    public Action getCurrent_action() {
        return current_action;
    }

    public void apply_current_action_effects() {
        if (current_action.getId() == Action.EXIT) {
            in_ramp = false;
            is_out = true;
        }
        else if (current_action.getId() == Action.WAIT) {
            return;  // WAIT changes nothing
        }
        else if (current_action.getId() == Action.PARK) {
            parentState.setParked_vehicle(this, (ParkingPlace)this.current_action.getParameter());
            parentState.removePreparked_vehicle(this);
        }
        else if (current_action.getId() == Action.PREPARK) {
            parentState.setPreparked_vehicle(this, (ParkingPlace)this.current_action.getParameter());
        }
        else if (current_action.getId() == Action.UNPARK) {
            parentState.removeParked_vehicle(this);
            parentState.setPreparked_vehicle(this, (ParkingPlace)this.current_action.getParameter());
        }

    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getTypeString() {
        if (this.downward)
            return " [↓] " + this.current_action.getName();
        else
            return " [↑] " + this.current_action.getName();
    }

    public boolean isParked() {
        if (parentState.parked_at[id] != -1)
            return true;
        else
            return false;
    }

    public boolean isPreparked() {
        if (parentState.preparked_at[id] != -1)
            return true;
        else
            return false;
    }

    public boolean isIn_ramp() {
        return in_ramp;
    }

    public boolean isFirst() {
        return first;
    }

}

class VehicleXPositionComparatorReverse implements Comparator<Vehicle> {
    @Override
    public int compare(Vehicle v1, Vehicle v2) {
        if (v1.getX_position() > v2.getX_position())
            return -1;
        else if (v1.getX_position() < v2.getX_position())
            return 1;
        return 0;
    }
}


