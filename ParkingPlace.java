public class ParkingPlace extends SceneElement {

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
        ret.parked_vehicle = this.parked_vehicle;
        ret.pre_parked_vehicle = this.pre_parked_vehicle;
        ret.x_position = this.x_position;
        ret.y_position = this.y_position;
        ret.parentState = this.parentState;
        ret.id = this.id;
        return ret;
    }

    public boolean isBelow(Vehicle v) {
        return (this.y_position > v.y_position);
    }

    public String getTypeString() {
        return " [parking]";
    }

    public boolean isOut() {
        return false;
    }

    public String getParked_vehicle() { return parked_vehicle; }
    public void setParked_vehicle(String parked_vehicle) { this.parked_vehicle = parked_vehicle; }
    public String getPre_parked_vehicle() { return pre_parked_vehicle; }
    public void setPre_parked_vehicle(String pre_parked_vehicle) { this.pre_parked_vehicle = pre_parked_vehicle; }
}
