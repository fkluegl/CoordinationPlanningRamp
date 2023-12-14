import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;

public class ParkingPlace extends SceneElement {
    public int id;
    private String parked_vehicle;
    private String pre_parked_vehicle;
    private ArrayList<String> has_already_parked;

    public ParkingPlace(String nam) {
        this.parked_vehicle = null;
        this.pre_parked_vehicle = null;
        this.has_already_parked = new ArrayList<>();
        this.name = nam;
        this.x_position = -10;
        this.y_position = 0;
    }

    public ParkingPlace getCopy() {
        ParkingPlace ret = new ParkingPlace(this.name);
        ret.parked_vehicle = this.parked_vehicle;
        ret.pre_parked_vehicle = this.pre_parked_vehicle;
        ret.has_already_parked.addAll(this.has_already_parked);
        ret.x_position = this.x_position;
        ret.y_position = this.y_position;
        ret.parentState = this.parentState;
        ret.id = this.id;
        return ret;
    }

    public boolean isBelow(Vehicle v) {
        return (this.y_position > v.y_position);
    }

    public boolean isAbove(Vehicle v) {
        return (this.y_position < v.y_position);
    }

    public String getTypeString() {
        return " [parking]";
    }

    public String getParked_vehicle() { return parked_vehicle; }
    public void setParked_vehicle(String vname) { this.parked_vehicle = vname; }
    public String getPre_parked_vehicle() { return pre_parked_vehicle; }
    public void setPre_parked_vehicle(String vname) { this.pre_parked_vehicle = vname; }

    public void set_has_already_parked(String vname) {
        if (has_already_parked.contains(vname)) {
            System.out.println("The key was already there : impossible!");
            System.exit(0);
        } else {
            has_already_parked.add(vname);
        }
    }

    public boolean get_has_already_parked(String vname) {
        return has_already_parked.contains(vname);
    }
}
