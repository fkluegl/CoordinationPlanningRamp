import java.util.Comparator;

public class Vehicle extends SceneElement {
    public static int EVENT_OK = 0;
    public static int EVENT_EXIT_TOP = 1;
    public static int EVENT_EXIT_BOTTOM = 2;
    public static int EVENT_VEHICLE_PARKED = 3;
    public static int EVENT_PASSED_PARKING = 4;
    public static int ACTION_COMPLETED = 5;
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
    private boolean ongoing_parking_operation = false;


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
            this.speed = 1.6;
            this.current_action = new Action(Action.GO_UP);
        }
    }

    public int step2(double time_step) {          // because we don't want to keep updating while park/unpark actions are completing
        if (current_action.getId() == Action.EXIT && !current_action.isFinished()) {
            if (x_position < State.x_max) {
                x_position += time_step * speed;
            }
            else {
                System.out.println("[EXIT] " + name + " has exited bottom.");
                current_action.setFinished(true);
                return ACTION_COMPLETED;
            }
        }
        else if (current_action.getId() == Action.PREPARK && !current_action.isFinished()) {
            ParkingPlace pp = (ParkingPlace) current_action.getParameter();
            if (x_position < pp.x_position - time_step * speed) {   // to make sure that the pp is always "below" v, otherwise
                x_position += time_step * speed;                    // the geometric test in enumerate_parking_places() fails
            }
            else {
                System.out.println("[PREPARK] " + name + " has reached pre-parking place.");
                current_action.setFinished(true);
                return ACTION_COMPLETED;
            }
        }
        else if (current_action.getId() == Action.PARK && !current_action.isFinished()) {
            if (parking_progress < 1) {
                parking_progress += time_step / State.parking_time;
            }
            else {
                System.out.println("[PARK] " + name + " has parked.");
                current_action.setFinished(true);
                return ACTION_COMPLETED;
            }
        }
        else if (current_action.getId() == Action.UNPARK && !current_action.isFinished()) {
            if (parking_progress > 0) {
                parking_progress -= time_step / State.parking_time;
            }
            else {
                System.out.println("[UNPARK] " + name + " has unparked.");
                current_action.setFinished(true);
                return ACTION_COMPLETED;
            }
        }
        else if (current_action.getId() == Action.GO_UP && !current_action.isFinished()) {
            if (x_position > 0) {
                double Dpp1 = getDeltaXToClosestParkingPlace();
                x_position -= time_step * speed;
                double Dpp2 = getDeltaXToClosestParkingPlace();
                if (Dpp1 * Dpp2 < 0  &&  Dpp1 != 1000  &&  Dpp2 != 1000) {
                    System.out.println("[EVENT_PASSED_PARKING] " + name + " has passed a parking place.");
                    return EVENT_PASSED_PARKING;
                }
            }
            else {
                System.out.println("[GO_UP] " + name + " has exited top.");
                current_action.setFinished(true);
                return ACTION_COMPLETED;
            }
        }
        return EVENT_OK;
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

    public Vehicle getCopy(State dady) {
        Vehicle ret = new Vehicle(this.name, this.downward);
        ret.x_position = this.x_position;
        ret.speed = this.speed;
        ret.current_action = this.current_action.getCopy();
        ret.parking_progress = this.parking_progress;
        ret.is_out = this.is_out;
        ret.parentState = dady;
        ret.in_ramp = this.in_ramp;
        ret.first = this.first;
        ret.id = this.id;
        ret.ongoing_parking_operation = this.ongoing_parking_operation;
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

    public boolean isOngoing_parking_operation() {
        return ongoing_parking_operation;
    }
    public void setOngoing_parking_operation(boolean ongoing_parking_operation) {
        this.ongoing_parking_operation = ongoing_parking_operation;
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
            parentState.setParked_vehicle(this, getPreParkingPlace());
            parentState.removePreparked_vehicle(this);
            parking_progress = 1;
            ongoing_parking_operation = false;
        }
        else if (current_action.getId() == Action.PREPARK) {
            parentState.setPreparked_vehicle(this, (ParkingPlace)this.current_action.getParameter());
        }
        else if (current_action.getId() == Action.UNPARK) {
            parentState.setPreparked_vehicle(this, getParkingPlace());
            parentState.removeParked_vehicle(this);
            parking_progress = 0;
            ongoing_parking_operation = false;
        }
        else if (current_action.getId() == Action.GO_UP) {
            in_ramp = false;
            is_out = true;
        }
        //TODO: ENTER !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getTypeString() {
        if (this.downward)
            return "       [↓] " + this.current_action.getName();
        else
            return "       [↑] " + this.current_action.getName();
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

    public ParkingPlace getPreParkingPlace() {
        int pppid = parentState.preparked_at[id];
        if (pppid != -1)
            return parentState.getParking_places().get(pppid);
        else
            return null;
    }

    public ParkingPlace getParkingPlace() {
        int pppid = parentState.parked_at[id];
        if (pppid != -1)
            return parentState.getParking_places().get(pppid);
        else
            return null;
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


