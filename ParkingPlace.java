import java.util.ArrayList;

public class ParkingPlace extends SceneElement {
    public int id;
    private String parked_vehicle;
    private String pre_parked_vehicle;


    public ParkingPlace(String nam) {
        this.parked_vehicle = null;
        this.pre_parked_vehicle = null;
        this.name = nam;
        this.x_position = -10;
        this.y_position = 0;
    }

    public ParkingPlace getCopy() {
        ParkingPlace ret = new ParkingPlace(this.name);

        if (this.parked_vehicle == null) ret.parked_vehicle = null;
        else ret.parked_vehicle = String.valueOf(this.parked_vehicle.toCharArray(), 0, this.parked_vehicle.length()); // deep copy !

        if (this.pre_parked_vehicle == null) ret.pre_parked_vehicle = null;
        else ret.pre_parked_vehicle = String.valueOf(this.pre_parked_vehicle.toCharArray(), 0, this.pre_parked_vehicle.length()); // deep copy !

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

    public boolean isVehicle() {return false;}
    public boolean isParkingPlace() {return true;}

    public String getParked_vehicle() { return parked_vehicle; }
    public void setParked_vehicle(String vname) { this.parked_vehicle = vname; }
    public String getPre_parked_vehicle() { return pre_parked_vehicle; }
    public void setPre_parked_vehicle(String vname) { this.pre_parked_vehicle = vname; }


}
