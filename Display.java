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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (display_state == null)
            return;

        for (ParkingPlace pp : display_state.getParking_places()) {
            g.setColor(Color.BLACK);
            int Y = (int)pp.getX_position() * 10;
            g.drawRect(500, Y, 50, 50);
            g.drawString(pp.getName(), 460, Y  + 25);
        }

        for (Vehicle v : display_state.getDw_vehicles()) {
            g.setColor(Color.GREEN);
            int X = 600 - (int)(v.getParking_progress() * 80);
            //System.out.printf("parking_progress = %.2f    X = %d\n", v.getParking_progress(), X);
            int Y = (int)v.getX_position() * 10;
            g.fillOval(X, Y, 20, 40);
            g.setColor(Color.BLACK);
            g.drawString(v.getName(), X, Y + 20);
        }

        for (Vehicle v : display_state.getUp_vehicles()) {
            g.setColor(Color.RED);
            int Y = (int)v.getX_position() * 10;
            g.fillOval(600, Y, 20, 40);
            g.setColor(Color.BLACK);
            g.drawString(v.getName(), 600, Y  + 20);
        }

        g.drawLine(500, 1000, 700, 1000);


    }


}
