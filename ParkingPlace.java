public class ParkingPlace extends SceneElement {
    private boolean occupied = false;


    public ParkingPlace(String nam) {
        this.name = nam;
        this.occupied = false;
        this.x_position = 0;
    }

    public ParkingPlace getCopy() {
        ParkingPlace ret = new ParkingPlace(this.name);
        ret.occupied = this.occupied;
        ret.x_position = this.x_position;
        return ret;
    }

    public String getType() {
        return " [parking]";
    }


    public boolean isOccupied() {
        return occupied;
    }


}
