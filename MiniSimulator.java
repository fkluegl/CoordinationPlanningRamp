import java.util.ArrayList;

public class MiniSimulator {
    Display display;
    State init_state;
    public MiniSimulator(Display disp) {
        display = disp;
    }

    public State simulate2(State s) {
        double simulation_time = 0.0;
        double DT = 0.025;
        display.set_state(s);
        System.out.print("[MINISIMULATOR] Simulate actions: " + s.current_action_str());

        //display.repaint();
        //try { Thread.sleep(20); } catch (InterruptedException e) { throw new RuntimeException(e); }
        //System.out.println();

        //while (!at_least_one_action_is_finished(s) || parking_operation_ongoing(s)) {
        while (true) {
            simulation_time += DT;

            //display.repaint();
            //try { Thread.sleep(20); } catch (InterruptedException e) { throw new RuntimeException(e); }

            for (Vehicle v : s.getDw_vehicles())
            {
                int event = v.step2(DT);
                if ((event == Vehicle.ACTION_COMPLETED || event == Vehicle.EVENT_PASSED_PARKING) && !parking_operation_ongoing(s)) {
                    s.finish_wait_actions();
                    s.apply_finished_actions_effects();  // applies effects to v.parentState
                    if (v.getCurrent_action().getId() == Action.EXIT && v.getCurrent_action().isFinished())
                        s.removeVehicle(v.getName()); //TODO: or maybe remove vehicle from the copy...?
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
                int event = v.step2(DT);

                if ((event == Vehicle.ACTION_COMPLETED || event == Vehicle.EVENT_PASSED_PARKING) && !parking_operation_ongoing(s)) {
                    s.finish_wait_actions();
                    s.apply_finished_actions_effects();  // applies effects to v.parentState
                    if (v.getCurrent_action().getId() == Action.GO_UP && v.getCurrent_action().isFinished())
                        s.removeVehicle(v.getName()); //TODO: or maybe remove vehicle from the copy...?
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

    public void replay(State s) {
        double simulation_time = 0.0;
        double DT = 0.025;

        display.set_state(s);
        display.repaint();

        while (true) {
            simulation_time += DT;

            display.repaint();
            try { Thread.sleep(10); } catch (InterruptedException e) { throw new RuntimeException(e); }

            for (Vehicle v : s.getDw_vehicles())
            {
                int event = v.step2(DT);
                if (simulation_time >= s.getDuration()) {
                //if (event != Vehicle.EVENT_OK) {
                    return;
                }
            }
            for (Vehicle v : s.getUp_vehicles())
            {
                int event = v.step2(DT);
                if (simulation_time >= s.getDuration()) {
                    //if (event != Vehicle.EVENT_OK) {
                    return;
                }
            }
        }
        //System.out.println("[REPLAY EVENT] end loop");
    }

    boolean simulation_not_finished(State s, boolean stop_when_dw_finished) {
        for (Vehicle v : s.getDw_vehicles())
            if (v.getCurrent_action().getId() != Action.WAIT  &&  !v.getCurrent_action().isFinished())
                return true;
        if (!stop_when_dw_finished)
        {
            for (Vehicle v : s.getUp_vehicles())
                if (!v.getCurrent_action().isFinished())
                    return true;
        }
        return false;
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

    boolean at_least_one_action_is_finished(State s) {
        for (Vehicle v : s.getDw_vehicles())
            if (v.getCurrent_action().isFinished() /*&& v.getCurrent_action().getId() != Action.WAIT*/)
                return true;

        for (Vehicle v : s.getUp_vehicles())
            if (v.getCurrent_action().isFinished())
                return true;

        return false;
    }

    boolean parking_operation_ongoing(State s) {
        for (Vehicle v : s.getDw_vehicles())
            if ((v.getCurrent_action().getId() == Action.PARK || v.getCurrent_action().getId() == Action.UNPARK) && !v.getCurrent_action().isFinished())
                return true;

        return false;
    }

    boolean something_collides(State s) {
        ArrayList<Vehicle> elts = new ArrayList<>();
        elts.addAll(s.getDw_vehicles());
        elts.addAll(s.getUp_vehicles());

        for (Vehicle v1 : elts)
            for (Vehicle v2 : elts)
                if (v1 != v2  &&  !v1.isOut()  &&  !v2.isOut()) {
                    if (Math.abs(v1.getX_position() - v2.getX_position()) < State.safety_distance) {
                        if (v1.getParking_progress() <= 0  && v2.getParking_progress() <= 0) {
                            System.out.println("((( COLLISION ))) between " + v1.getName() + " and " + v2.getName() + " !!!");
                            return true;
                        }
                    }
                }
        return false;
    }

}
