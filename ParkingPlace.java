public class ParkingPlace extends SceneElement {
    private boolean booked;
    private Vehicle parked_vehicle;


    public ParkingPlace(String nam) {
        this.name = nam;
        this.booked = false;
        this.parked_vehicle = null;
        this.x_position = 0;
    }

    public ParkingPlace getCopy() {
        ParkingPlace ret = new ParkingPlace(this.name);
        ret.booked = this.booked;
        ret.x_position = this.x_position;
        ret.parked_vehicle = this.parked_vehicle;
        return ret;
    }

    public void setParked_vehicle(Vehicle parked_vehicle) {
        this.parked_vehicle = parked_vehicle;
    }

    public Vehicle getParked_vehicle() {
        return parked_vehicle;
    }

    public void removeParked_vehicle() {
        this.parked_vehicle = null;
    }

    public boolean hasParkedVehicle() {
        return (this.parked_vehicle != null);
    }

    public boolean isBelow(Vehicle v) {
        return (this.x_position > v.x_position);
    }

    public String getTypeString() {
        return " [parking]";
    }

    public boolean isOut() {
        return false;
    }


    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }
}
