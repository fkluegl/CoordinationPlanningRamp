public class ParkingPlace extends SceneElement {
    private boolean booked = false;


    public ParkingPlace(String nam) {
        this.name = nam;
        this.booked = false;
        this.x_position = 0;
    }

    public ParkingPlace getCopy() {
        ParkingPlace ret = new ParkingPlace(this.name);
        ret.booked = this.booked;
        ret.x_position = this.x_position;
        return ret;
    }

    public boolean isBelow(Vehicle v) {
        return (this.x_position > v.x_position);
    }

    public String getTypeString() {
        return " [parking]";
    }


    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }
}
