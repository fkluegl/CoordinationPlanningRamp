import java.util.Comparator;

public class Vehicle extends SceneElement {
    private boolean downward;
    private boolean parked = false;


    public Vehicle(String nam, boolean dwd) {
        this.downward = dwd;
        this.name = nam;
        this.parked = false;
        this.x_position = 0;
    }

    public Vehicle getCopy() {
        Vehicle ret = new Vehicle(this.name, this.downward);
        ret.parked = this.parked;
        ret.x_position = this.x_position;
        return ret;
    }

    public void setParked(boolean parked) {
        this.parked = parked;
    }

    public String getName() {
        return name;
    }

    public boolean isParked() {
        return parked;
    }

    public boolean isDownward() {
        return downward;
    }

    public String getType() {
        return "";
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


