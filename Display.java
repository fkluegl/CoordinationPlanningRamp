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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (display_state == null)
            return;

        for (ParkingPlace pp : display_state.getParking_places()) {
            g.setColor(Color.BLACK);
            int X = 400;
            int Y = (int)pp.getY_position() * 5;
            g.drawRect(X, Y, 25, 25);
            g.drawString(pp.getName(), 360, Y  + 13);
        }

        for (Vehicle v : display_state.getVehicles()) {
            if (v.isDownward()) {
                g.setColor(Color.GREEN);
            } else {
                if (v.isLoaded())   g.setColor(Color.RED);
                else                g.setColor(Color.CYAN);
            }
            int X;
            if (v.isIn_ramp()) X = 500 + (int)v.getX_position() * 10; // 500 pixels <--> Vehicle.x = 0 meters
            else {
                if (v.isFirst())  X = 500 + (int)v.getX_position() * 10;
                else              X = 500 + (int)v.getX_position() * 10 + display_state.getVehicles().indexOf(v) * 30; // 500 pixels <--> Vehicle.x = 0 meters + 30 pixels between each vehicle
            }
            int Y = (int)v.getY_position() * 5;
            g.fillOval(X, Y, 10, 20);
            g.setColor(Color.BLACK);
            g.drawString(v.getName(), X, Y + 10);
        }

        g.drawLine(500, 1, 700, 1);
        g.drawLine(500, 1000, 700, 1000);
    }


}
