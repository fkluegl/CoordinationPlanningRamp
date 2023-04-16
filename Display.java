import javax.swing.*;
import java.awt.*;

public class Display extends JFrame {
    private Graph graph;
    boolean[] state;
    Node current_node;

    public Display() {
        setTitle("Graph:");
        setSize(1200, 1200);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void  set_graph(Graph g) {
        this.graph = g;
    }

    public void  set_state(State s) {
        this.state = s.clean;
        current_node = graph.graph.get(s.current_node);
    }

    public void refresh() {
        paint(getGraphics());
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.clearRect(1, 1, 1200, 1200);
        for(int i=0; i<graph.graph.size(); i++) {
            Node n = graph.graph.get(i);
            int X = (int)((n.x + 2) * 120);
            int Y = (int)((n.y + 4) * 120);
            g2d.setStroke(new BasicStroke(4));

            if (state != null) {
                if (state[i])
                    g2d.setPaint(new Color(0, 0.7f, 0));
            }
            else
                g2d.setPaint(Color.BLACK);
            g2d.drawOval(X, Y, n.radius, n.radius);

            if (current_node != null)
                if (n == current_node) {
                    g2d.setStroke(new BasicStroke(5));
                    g2d.setPaint(Color.RED);
                    g2d.drawOval(X - 5, Y - 5, n.radius + 10, n.radius + 10);
                }

            // edges to neighbours
            g2d.setStroke(new BasicStroke(1));
            g2d.setPaint(Color.BLACK);
            g2d.drawString(n.label, (int)(X + n.radius / 2 - 20), Y + n.radius / 2);
            for(Node ngb : n.neighbours) {
                int X2 = (int)((ngb.x + 2) * 120);
                int Y2 = (int)((ngb.y + 4) * 120);
                g2d.drawLine(X + n.radius / 2, Y + n.radius / 2, X2 + n.radius / 2, Y2 + n.radius / 2);
            }
        }

    }

}
