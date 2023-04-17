import javax.swing.*;
import java.awt.*;

public class Display extends JFrame {


    public Display() {
        setTitle("Graph:");
        setSize(1200, 1200);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    public void  set_state(State s) {

    }

    public void refresh() {
        paint(getGraphics());
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        //g.clearRect(1, 1, 1200, 1200);
        //g2d.setStroke(new BasicStroke(4));
        //g2d.setPaint(Color.RED);
        //g2d.drawOval(X - 5, Y - 5, n.radius + 10, n.radius + 10);
        //g2d.drawLine(X + n.radius / 2, Y + n.radius / 2, X2 + n.radius / 2, Y2 + n.radius / 2);

    }

}
