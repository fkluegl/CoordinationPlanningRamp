import java.util.Comparator;

public class SceneElement {
    protected double x_position;
    protected double y_position;
    protected String name;
    protected State parentState;

    public double getY_position() {
        return y_position;
    }
    public void setY_position(double y_position) {
        this.y_position = y_position;
    }

    public double getX_position() {
        return x_position;
    }
    public void setX_position(double x_position) {
        this.x_position = x_position;
    }

    public String getName() {
        return name;
    }
    public String getTypeString() {
        return this.getTypeString();
    }

    public SceneElement getCopy() {
        SceneElement ret = this.getCopy();
        return ret;
    }

    public boolean isVehicle() {return this.isVehicle();}
    public boolean isParkingPlace() {return this.isParkingPlace();}

    public void setParentState(State parentState) {
        this.parentState = parentState;
    }

    public State getParentState() {
        return parentState;
    }
}


class SceneElementYPositionComparator implements Comparator<SceneElement> {
    @Override
    public int compare(SceneElement v1, SceneElement v2) {
        if (v1.getY_position() < v2.getY_position())
            return -1;
        else if (v1.getY_position() > v2.getY_position())
            return 1;
        return 0;
    }
}

