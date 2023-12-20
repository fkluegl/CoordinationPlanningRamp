public class MiniSimulator {
    Display display;
    State init_state;
    public MiniSimulator(Display disp) {
        display = disp;
    }

    public State simulate(State s) {
        boolean debug = false;
        double simulation_time = 0.0;
        double DT = 0.1;
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

            for (Vehicle v : s.getVehicles())
            {
                int event = v.step(DT);
                if (event == Vehicle.ACTION_COMPLETED || event == Vehicle.EVENT_PASSED_PARKING) {
                    s.apply_finished_actions_effects();  // applies effects to v.parentState
                    if ((v.getCurrent_action().getId() == Action.GO_DOWN || v.getCurrent_action().getId() == Action.GO_UP) && v.getCurrent_action().isFinished())
                        s.removeVehicle(v.name);
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
        double DT = 0.035;

        display.set_state(s);
        display.repaint();
        try { Thread.sleep(1); } catch (InterruptedException e) { throw new RuntimeException(e); }

        while (true) {
            simulation_time += DT;

            display.repaint();
            try { Thread.sleep(10); } catch (InterruptedException e) { throw new RuntimeException(e); }

            for (Vehicle v : s.getVehicles())
            {
                v.step(DT);
                if (simulation_time >= duration)
                    return;
            }
        }
    }

    public void displayState(State s) {
        display.set_state(s);
        display.repaint();
        for (int i=0; i<20; i++) {
            display.repaint();
            try { Thread.sleep(100); } catch (InterruptedException e) { throw new RuntimeException(e); }
        }
    }

    boolean something_collides(State s) {
        //ArrayList<Vehicle> elts = new ArrayList<>();
        //elts.addAll(s.getVehicles());

        for (Vehicle v1 : s.getVehicles())
            for (Vehicle v2 : s.getVehicles())                    // TODO: can an ENTERing vehicle collide with a GO_UPing vehicle?
                if (v1 != null && v2 != null) {
                    if (v1 != v2 && v1.isIn_ramp() && v2.isIn_ramp()) {
                        if (Math.abs(v1.getY_position() - v2.getY_position()) < State.SAFETY_DISTANCE) {
                            if (Math.abs(v1.getX_position() - v2.getX_position()) < State.SAFETY_DISTANCE) {
                                System.out.println("((( COLLISION ))) between " + v1.getName() + " and " + v2.getName() + " !!!");
                                return true;
                            }
                        }
                    }
                }
        return false;
    }

}
