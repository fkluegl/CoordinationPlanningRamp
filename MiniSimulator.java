import java.util.ArrayList;

public class MiniSimulator {
    State init_state;
    public MiniSimulator() {

    }

    public ArrayList<State> simulate(State s) {
        ArrayList<State> ret = new ArrayList<>();
        double tick = 0;

        while (simulation_not_finished(s)) {
            tick += 1;
            for (Vehicle v : s.getDw_vehicles()) {
                boolean finished = v.step(1);
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
}
