import java.util.Comparator;

public class SceneElement {
    protected double x_position;
    protected String name;
    protected State parentState;
    public int id;

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
    public boolean isOut() {
        return this.isOut();
    }

    public void setParentState(State parentState) {
        this.parentState = parentState;
    }

    public State getParentState() {
        return parentState;
    }
}


class SceneElementXPositionComparator implements Comparator<SceneElement> {
    @Override
    public int compare(SceneElement v1, SceneElement v2) {
        if (v1.getX_position() < v2.getX_position())
            return -1;
        else if (v1.getX_position() > v2.getX_position())
            return 1;
        return 0;
    }
}

