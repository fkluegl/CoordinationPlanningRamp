import java.awt.*;
import java.util.ArrayList;

public class Node {
    public double x;
    public double y;
    int radius = 30;
    String label;
    ArrayList<Node> neighbours;

    public Node(String l, double x, double y) {
        this.x = x;
        this.y = y;
        this.label = l;
        neighbours = new ArrayList<Node>();
    }

    public void addNeighbours(Node...neighbs) {
        for(Node n : neighbs) {
            neighbours.add(n);
        }
    }

    public Node getCopy_without_neighbours() {
        Node ret = new Node(this.label, this.x, this.y);
        ret.radius = this.radius;
        return ret;
    }

}
