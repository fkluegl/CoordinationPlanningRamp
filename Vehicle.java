import java.util.Comparator;

public class Vehicle extends SceneElement {
    private boolean downward;
    private SceneElement parked_at;
    private double parking_progress = 0;
    private double speed;
    private Action current_action;


    public Vehicle(String nam, boolean dwd) {
        this.downward = dwd;
        this.name = nam;
        this.parked_at = null;
        this.x_position = 0;
        if (this.downward) this.speed = 5.7;
        else               this.speed = 3.1;
        this.current_action = new Action(Action.WAIT);
    }

    public boolean step(double time_step) {
        if (current_action.getId() == Action.EXIT) {
            if (parking_progress > 0) { // then must leave the parking place first
                parking_progress -= time_step / State.parking_time;
                return false; // not finished
            }
            else {
                if (x_position < State.x_max)
                    x_position += time_step * speed;
                else
                    current_action.setFinished(true);
            }

        }
        else if (this.current_action.getId() == Action.PARK) {

        }
    }

    public Vehicle getCopy() {
        Vehicle ret = new Vehicle(this.name, this.downward);
        ret.parked_at = this.parked_at.getCopy();
        ret.x_position = this.x_position;
        ret.speed = this.speed;
        ret.current_action = this.current_action.getCopy();
        ret.parking_progress = this.parking_progress;
        return ret;
    }

    public void setParked_at(SceneElement pp) {
        this.parked_at = pp;
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


