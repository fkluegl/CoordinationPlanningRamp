import java.util.ArrayList;
import java.time.Clock;

public class MiniSimulator {
    Display display;
    State init_state;
    public MiniSimulator(Display disp) {
        display = disp;
    }

    public ArrayList<State> simulate(State s) {
        // initialize finished flags for PARK and EXIT
        for (Vehicle v : s.getDw_vehicles())
            if (v.getCurrent_action().getId() != Action.WAIT)
                v.getCurrent_action().setFinished(false);

        ArrayList<State> ret = new ArrayList<>();
        double simulation_time = 0.0;
        double DT = 0.1;
        int feedback;
        display.set_state(s);

        while (simulation_not_finished(s)) {
            simulation_time += DT;
            for (Vehicle v : s.getDw_vehicles()) {
                feedback = v.step(DT);
                try { Thread.sleep(100); } catch (InterruptedException e) { throw new RuntimeException(e); }
                display.refresh();
                if (feedback == Vehicle.EVENT_FINISHED) {
                    State s_copy = s.getCopy();
                    s_copy.increaseStart_time(simulation_time);
                    ret.add(s_copy);
                }
                // check for collision --> end of the simulation
                if (something_collides(s)) {
                    System.out.println("COLLISION!!!");
                    return ret;
                }
            }
        }
        return ret;
    }

    boolean simulation_not_finished(State s) {
        for (Vehicle v : s.getDw_vehicles())
            if (v.getCurrent_action().getId() != Action.WAIT  &&  !v.getCurrent_action().isFinished())
                return true;
        return false;
    }

    boolean something_collides(State s) {
        ArrayList<Vehicle> elts = new ArrayList<>();
        elts.addAll(s.getDw_vehicles());
        elts.addAll(s.getUp_vehicles());

        for (Vehicle v1 : elts)
            for (Vehicle v2 : elts)
                if (v1 != v2) {
                    if (Math.abs(v1.getX_position() - v2.getX_position()) < State.safety_distance) {
                        if (v1.getParking_progress() <= 0  && v2.getParking_progress() <= 0)
                            return true;
                    }
                }
        return false;
    }

}
