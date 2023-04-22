import javax.swing.*;
import java.awt.*;

public class Display extends JFrame {
    State display_state;

    public Display() {
        setTitle("Ramp:");
        setSize(1200, 1200);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    public void set_state(State s) {
        display_state = s;
    }

    public void refresh() {
        paint(getGraphics());
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.clearRect(550, 1, 650, 1200);

        if (display_state == null)
            return;

        for (Vehicle v : display_state.getDw_vehicles()) {
            g2d.setStroke(new BasicStroke(3));
            g2d.setPaint(Color.GREEN);
            int Y = (int)v.getX_position() * 10;
            g2d.drawOval(600, Y, 20, 40);
            g2d.setPaint(Color.BLACK);
            g2d.drawString(v.getName(), 600, Y + 20);
        }

        for (Vehicle v : display_state.getUp_vehicles()) {
            g2d.setStroke(new BasicStroke(3));
            g2d.setPaint(Color.RED);
            int Y = (int)v.getX_position() * 10;
            g2d.drawOval(600, Y, 20, 40);
            g2d.setPaint(Color.BLACK);
            g2d.drawString(v.getName(), 600, Y  + 20);
        }

        for (ParkingPlace pp : display_state.getParking_places()) {
            g2d.setStroke(new BasicStroke(3));
            g2d.setPaint(Color.BLACK);
            int Y = (int)pp.getX_position() * 10;
            g2d.drawRect(500, Y, 50, 50);
            g2d.drawString(pp.getName(), 460, Y  + 25);
        }

        //g2d.drawLine(X + n.radius / 2, Y + n.radius / 2, X2 + n.radius / 2, Y2 + n.radius / 2);
    }

}
