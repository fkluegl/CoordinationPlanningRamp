import javax.swing.Timer;
import javax.swing.JPanel;
import java.awt.*;

public class Display extends JPanel {
    State display_state;

    public Display() {
        setVisible(true);
        setBackground(Color.WHITE);
    }

    public void set_state(State s) {
        display_state = s;
    }

    public void refresh() {
        paintComponent(getGraphics());
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(500, 1, 700, 1200);

        if (display_state == null)
            return;

        for (ParkingPlace pp : display_state.getParking_places()) {
            g2d.setStroke(new BasicStroke(3));
            g2d.setPaint(Color.BLACK);
            int Y = (int)pp.getX_position() * 10;
            g2d.drawRect(500, Y, 50, 50);
            g2d.drawString(pp.getName(), 460, Y  + 25);
        }

        for (Vehicle v : display_state.getDw_vehicles()) {
            g2d.setStroke(new BasicStroke(6));
            g2d.setPaint(Color.GREEN);
            int X = 600 - (int)(v.getParking_progress() * 80);
            int Y = (int)v.getX_position() * 10;
            g2d.drawOval(X, Y, 20, 40);
            g2d.setPaint(Color.BLACK);
            g2d.drawString(v.getName(), X, Y + 20);
        }

        for (Vehicle v : display_state.getUp_vehicles()) {
            g2d.setStroke(new BasicStroke(6));
            g2d.setPaint(Color.RED);
            int Y = (int)v.getX_position() * 10;
            g2d.drawOval(600, Y, 20, 40);
            g2d.setPaint(Color.BLACK);
            g2d.drawString(v.getName(), 600, Y  + 20);
        }

        g2d.drawLine(500, 1000, 700, 1000);


    }


}
