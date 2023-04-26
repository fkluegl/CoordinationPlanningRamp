import java.util.Comparator;

public class Vehicle extends SceneElement {
    public static int EVENT_OK = 0;
    public static int EVENT_FINISHED = 1;
    public static int EVENT_EXIT_TOP = 2;
    public static int EVENT_EXIT_BOTTOM = 3;
    public static int EVENT_VEHICLE_PARKED = 4;
    private boolean downward;
    private double parking_progress = 0;
    private double speed;
    private boolean is_out;
    private Action current_action;
    private boolean debug_step = false;

    public Vehicle(String nam, boolean dwd) {
        this.downward = dwd;
        this.name = nam;
        this.is_out = false;
        this.x_position = 0;
        if (this.downward) {
            this.speed = 5.7;
            this.current_action = new Action(Action.WAIT);
        }
        else {
            this.speed = 2.1;
            this.current_action = new Action(Action.GO_UP);
        }
    }

    public int step(double time_step) {
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
                if (pp.hasParkedVehicle())
                    pp.removeParked_vehicle();
                return EVENT_OK; // not finished
            }
            else {
                if (x_position < pp.x_position  &&  parking_progress <= 0) {
                    x_position += time_step * speed;
                    if (debug_step) System.out.println(name + " heading to parking " + pp.getName());
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
                    pp.setParked_vehicle(this);
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
            else
                return EVENT_OK;
        }
        else {
            // case action = WAIT
            return EVENT_OK; // not finished
        }
    }

    public Vehicle getCopy() {
        Vehicle ret = new Vehicle(this.name, this.downward);
        ret.x_position = this.x_position;
        ret.speed = this.speed;
        ret.current_action = this.current_action.getCopy();
        ret.parking_progress = this.parking_progress;
        ret.is_out = this.is_out;
        return ret;
    }

    public String getName() {
        return name;
    }

    public double getParking_progress() {
        return parking_progress;
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

    public void setIs_out(boolean is_out) {
        this.is_out = is_out;
    }

    public void setCurrent_action(Action current_action) {
        this.current_action = current_action;
    }

    public Action getCurrent_action() {
        return current_action;
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


