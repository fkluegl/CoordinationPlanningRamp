import java.util.Comparator;

public class Vehicle extends SceneElement {
    private boolean downward;
    private SceneElement parked_at;
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

    public Vehicle getCopy() {
        Vehicle ret = new Vehicle(this.name, this.downward);
        ret.parked_at = this.parked_at;
        ret.x_position = this.x_position;
        ret.current_action = this.current_action.getCopy();
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
            return " [↓] " + this.current_action.name;
        else
            return " [↑] " + this.current_action.name;
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


