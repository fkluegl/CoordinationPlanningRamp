public class ParkingPlace extends SceneElement {

    public ParkingPlace(String nam) {
        this.name = nam;
        this.x_position = -10;
        this.y_position = 0;
    }

    public ParkingPlace getCopy() {
        ParkingPlace ret = new ParkingPlace(this.name);
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
}
