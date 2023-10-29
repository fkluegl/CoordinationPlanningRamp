import java.util.ArrayList;

public class MiniSimulator {
    Display display;
    State init_state;
    public MiniSimulator(Display disp) {
        display = disp;
    }

    public State simulate(State s) {
        boolean debug = false;
        double simulation_time = 0.0;
        double DT = 0.025;
        display.set_state(s);
        System.out.print("[MINISIMULATOR] Simulate actions: " + s.current_action_str());

        if (debug) {
            display.repaint();
            try { Thread.sleep(200); } catch (InterruptedException e) { throw new RuntimeException(e); }
        }

        while (true) {
            simulation_time += DT;

            if (debug) {
                display.repaint();
                try { Thread.sleep(10); } catch (InterruptedException e) { throw new RuntimeException(e); }
            }

            for (Vehicle v : s.getDw_vehicles())
            {
                int event = v.step(DT);
                if (event == Vehicle.ACTION_COMPLETED || event == Vehicle.EVENT_PASSED_PARKING) {
                    s.apply_finished_actions_effects();  // applies effects to v.parentState
                    if (v.getCurrent_action().getId() == Action.EXIT && v.getCurrent_action().isFinished())
                        s.removeVehicle(v.getName());
                    s.setDuration(simulation_time);
                    return s.getCopy();
                }

                // check for collision --> end of the simulation
                if (something_collides(s)) {
                    return null;
                }
            }
            for (Vehicle v : s.getUp_vehicles())
            {
                int event = v.step(DT);
                if (event == Vehicle.ACTION_COMPLETED || event == Vehicle.EVENT_PASSED_PARKING) {
                    s.apply_finished_actions_effects();  // applies effects to v.parentState
                    if (v.getCurrent_action().getId() == Action.GO_UP && v.getCurrent_action().isFinished())
                        s.removeVehicle(v.getName());
                    s.setDuration(simulation_time);
                    return s.getCopy();
                }

                // check for collision --> end of the simulation
                if (something_collides(s)) {
                    return null;
                }
            }
        }
        //return null;
    }

    public void replay(State s, double duration) {
        double simulation_time = 0.0;
        double DT = 0.025;

        display.set_state(s);
        display.repaint();
        try { Thread.sleep(2); } catch (InterruptedException e) { throw new RuntimeException(e); }

        while (true) {
            simulation_time += DT;

            display.repaint();
            try { Thread.sleep(10); } catch (InterruptedException e) { throw new RuntimeException(e); }

            for (Vehicle v : s.getDw_vehicles())
            {
                int event = v.step(DT);
                if (simulation_time >= duration) {
                //if (event != Vehicle.EVENT_OK) {
                    return;
                }
            }
            for (Vehicle v : s.getUp_vehicles())
            {
                int event = v.step(DT);
                if (simulation_time >= duration) {
                    //if (event != Vehicle.EVENT_OK) {
                    return;
                }
            }
        }
    }

    boolean all_actions_finished(State s) {
        for (Vehicle v : s.getDw_vehicles())
            if (!v.getCurrent_action().isFinished())
                return false;

        for (Vehicle v : s.getUp_vehicles())
            if (!v.getCurrent_action().isFinished())
                return false;

        return true;
    }

    boolean something_collides(State s) {
        ArrayList<Vehicle> elts = new ArrayList<>();
        elts.addAll(s.getDw_vehicles());
        elts.addAll(s.getUp_vehicles());

        for (Vehicle v1 : elts)
            for (Vehicle v2 : elts)
                if (v1 != v2  &&  !v1.isOut()  &&  !v2.isOut()) {
                    if (Math.abs(v1.getY_position() - v2.getY_position()) < State.safety_distance) {
                        if (Math.abs(v1.getX_position() - v2.getX_position()) < State.safety_distance) {
                            System.out.println("((( COLLISION ))) between " + v1.getName() + " and " + v2.getName() + " !!!");
                            return true;
                        }
                    }
                }
        return false;
    }

}
