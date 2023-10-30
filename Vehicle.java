import java.util.Comparator;

public class Vehicle extends SceneElement {
    public static int EVENT_OK = 0;
    public static int EVENT_EXIT_TOP = 1;
    public static int EVENT_EXIT_BOTTOM = 2;
    public static int EVENT_VEHICLE_PARKED = 3;
    public static int EVENT_PASSED_PARKING = 4;
    public static int ACTION_COMPLETED = 5;
    private boolean downward;
    private double min_speed;
    private double max_speed;
    private double speed;
    private boolean is_out; // result of EXIT or GO_UP, not the same as !in_ramp --> queued vehicles
    private Action current_action;
    private boolean debug_step = false;
    // ---------------------------------------------------------------------------
    private boolean in_ramp = true;
    private boolean first = false;
    private boolean is_parking = false;
    private boolean is_unparking = false;


    public Vehicle(String nam, boolean dwd) {
        this.downward = dwd;
        this.name = nam;
        this.is_out = false;
        this.in_ramp = true;
        this.x_position = 0;
        this.y_position = 0;
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

    public int step(double time_step) {          // because we don't want to keep updating while park/unpark actions are completing
        if (current_action.getId() == Action.PREPARK && !current_action.isFinished()) {
            ParkingPlace pp = (ParkingPlace) current_action.getParameter();
            if (y_position < pp.y_position - time_step * speed) {   // to make sure that the pp is always "below" v, otherwise
                y_position += time_step * speed;                    // the geometric test in enumerate_parking_places() fails
            }
            else {
                System.out.println("[PREPARK] " + name + " has reached pre-parking place.");
                current_action.setFinished(true);
                return ACTION_COMPLETED;
            }
        }
        else if (current_action.getId() == Action.PARK && !current_action.isFinished()) {
            if (x_position > -10 + time_step * State.parking_speed) {
                x_position -= time_step * State.parking_speed;
                is_parking = true;
            }
            else {
                System.out.println("[PARK] " + name + " has parked.");
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
        else if (current_action.getId() == Action.EXIT && !current_action.isFinished()) {
            if (y_position < State.y_max) {
                y_position += time_step * speed;
            }
            else {
                System.out.println("[EXIT] " + name + " has exited bottom.");
                current_action.setFinished(true);
                return ACTION_COMPLETED;
            }
        }
        else if (current_action.getId() == Action.GO_UP && !current_action.isFinished()) {
            if (y_position > 0) {
                double Dpp1 = getDeltaYToClosestParkingPlace();
                y_position -= time_step * speed;
                double Dpp2 = getDeltaYToClosestParkingPlace();
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
        Vehicle ret = new Vehicle(this.name, this.downward);
        ret.x_position = this.x_position;
        ret.y_position = this.y_position;
        ret.speed = this.speed;
        ret.current_action = this.current_action.getCopy();
        ret.is_out = this.is_out;
        ret.parentState = dady;
        ret.in_ramp = this.in_ramp;
        ret.first = this.first;
        ret.id = this.id;
        ret.is_parking = this.is_parking;
        ret.is_unparking = this.is_unparking;
        return ret;
    }

    public String getName() {
        return name;
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

    public boolean is_parking() {
        return is_parking;
    }

    public void setIs_parking(boolean is_parking) {
        this.is_parking = is_parking;
    }

    public boolean is_unparking() {
        return is_unparking;
    }

    public void setIs_unparking(boolean is_unparking) {
        this.is_unparking = is_unparking;
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
        else if (current_action.getId() == Action.PARK && !is_parking) {
            parentState.setParked_vehicle(this, getPreParkingPlace());
            parentState.removePreparked_vehicle(this);
            x_position = -10;
            y_position = getParkingPlace().y_position;
            is_parking = false;
        }
        else if (current_action.getId() == Action.PREPARK) {
            parentState.setPreparked_vehicle(this, (ParkingPlace)this.current_action.getParameter());
        }
        else if (current_action.getId() == Action.UNPARK && !is_unparking) {
            parentState.setPreparked_vehicle(this, getParkingPlace());
            parentState.removeParked_vehicle(this);
            x_position = 0;
            y_position = getPreParkingPlace().y_position;
            is_unparking = false;
        }
        else if (current_action.getId() == Action.GO_UP) {
            in_ramp = false;
            is_out = true;
        }
        else if (current_action.getId() == Action.ENTER) {
            x_position = 0;
            in_ramp = true;
            first = false;
            parentState.update_first_in_queue();
         }
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


