import java.util.ArrayList;
import java.time.Clock;

public class MiniSimulator {
    Display display;
    State init_state;
    public MiniSimulator(Display disp) {
        display = disp;
    }

    public ArrayList<State> simulate(State s, boolean introspection, boolean stop_when_dw_finished) {
        // check if a vehicle intends to park at a parking place with a waiting vehicle
        for (Vehicle v : s.getDw_vehicles())
            if (v.getCurrent_action().getId() == Action.PARK) {
                ParkingPlace pp = (ParkingPlace)v.getCurrent_action().getParameter();
                Vehicle pv = s.getParked_vehicle(pp);
                if (pv != null)
                    if (pv.getCurrent_action().getId() == Action.WAIT) {
                        System.out.println("[PRE-SIM] Bound to failure: " + v.getName() + " cannot park at " + pp.getName() + " because " + pv.getName() + " waits there!");
                        return null;
                    }
            }

        // (todo: also check if a vehicle wants to exit while another Dw vehicle waits unparked)

        // initialize finished flags for vehicles committed to PARK and EXIT
        for (Vehicle v : s.getDw_vehicles())
            if (v.getCurrent_action().getId() != Action.WAIT)
                v.getCurrent_action().setFinished(false);
        // initialize finished flags for vehicles committed to GO_UP
        for (Vehicle v : s.getUp_vehicles())
            v.getCurrent_action().setFinished(false);
        // initialize state duration
        s.setDuration(0);

        ArrayList<State> ret = new ArrayList<>();
        double simulation_time = 0.0;
        double DT = 0.025;
        int feedback = -1;
        display.set_state(s);
        System.out.println("Simulate actions: " + s.current_action_str());
        System.out.println();

        while (simulation_not_finished(s, stop_when_dw_finished)) {
            if (introspection) {
                display.repaint();
                try { Thread.sleep(10); } catch (InterruptedException e) { throw new RuntimeException(e); }
            }
            simulation_time += DT;
            for (Vehicle v : s.getDw_vehicles())
            {
                if (v.getCurrent_action().isFinished())
                    continue;

                if (v.isOut()) feedback = Vehicle.EVENT_OK;
                else           feedback = v.step(DT);
                if (feedback == Vehicle.EVENT_VEHICLE_PARKED  ||  feedback == Vehicle.EVENT_EXIT_BOTTOM  ||  feedback == Vehicle.EVENT_PASSED_PARKING) {
                    if (feedback == Vehicle.EVENT_EXIT_BOTTOM)
                        v.setIs_out(true);
                    State s_copy = s.getCopy();
                    s_copy.setDuration(simulation_time);
                    s_copy.increaseStart_time(simulation_time);
                    if (s_copy.allVehiclesOut()) System.out.println("ALL VEHICLES OUT!!!!!!!!!!!!");
                    ret.add(s_copy);
                }
                // check for collision --> end of the simulation
                if (something_collides(s)) {
                    return null;
                }
            }
            for (Vehicle v : s.getUp_vehicles())
            {
                if (v.getCurrent_action().isFinished())
                    continue;

                if (v.isOut()) feedback = Vehicle.EVENT_OK;
                else           feedback = v.step(DT);
                if (feedback == Vehicle.EVENT_EXIT_TOP  ||  feedback == Vehicle.EVENT_PASSED_PARKING) {
                    if (feedback == Vehicle.EVENT_EXIT_TOP)
                        v.setIs_out(true);
                    State s_copy = s.getCopy();
                    s_copy.setDuration(simulation_time);
                    s_copy.increaseStart_time(simulation_time);
                    if (s_copy.allVehiclesOut()) System.out.println("ALL VEHICLES OUT!!!!!!!!!!!!1");
                    ret.add(s_copy);
                }
                // check for collision --> end of the simulation
                if (something_collides(s)) {
                    return null;
                }
            }
        }
        return ret;
    }

    public ArrayList<State> simulate2(State s) {
        ArrayList<State> ret = new ArrayList<>();

        // initialize state duration
        s.setDuration(0);

        double simulation_time = 0.0;
        double DT = 0.025;
        int feedback = -1;
        display.set_state(s);
        System.out.println("[MINISIMULATOR] Simulate actions: " + s.current_action_str());
        System.out.println();

        main_loop:
        while (!one_action_is_finished(s)) {
            simulation_time += DT;

            display.repaint();
            try { Thread.sleep(10); } catch (InterruptedException e) { throw new RuntimeException(e); }

            for (Vehicle v : s.getDw_vehicles())
            {
                if (v.getCurrent_action().isFinished() && v.getCurrent_action().getId() != Action.WAIT)      //TODO: remove?
                    continue;                                                                                //TODO: remove?

                int event = v.step2(DT);
                if (event == Vehicle.ACTION_COMPLETED) {
                    v.apply_current_action_effects();  // applies effects to v.parentState
                    if (v.getCurrent_action().getId() == Action.EXIT)
                        s.removeVehicle(v.getName()); //TODO: or maybe remove vehicle from the copy...?
                    ret.add(s.getCopy());
                    break main_loop;
                }
                else if (event == Vehicle.EVENT_PASSED_PARKING) {
                    ret.add(s.getCopy());
                    break main_loop;
                }

                // check for collision --> end of the simulation
                if (something_collides(s)) {
                    break main_loop;
                }
            }
            for (Vehicle v : s.getUp_vehicles())
            {
                if (v.getCurrent_action().isFinished() && v.getCurrent_action().getId() != Action.WAIT)
                    continue;

                int event = v.step2(DT);

                if (event == Vehicle.ACTION_COMPLETED) {
                    v.apply_current_action_effects();  // applies effects to v.parentState
                    if (v.getCurrent_action().getId() == Action.GO_UP)
                        s.removeVehicle(v.getName()); //TODO: or maybe remove vehicle from the copy...?
                    ret.add(s.getCopy());
                    break main_loop;
                }
                else if (event == Vehicle.EVENT_PASSED_PARKING) {
                    ret.add(s.getCopy());
                    break main_loop;
                }

                // check for collision --> end of the simulation
                if (something_collides(s)) {
                    break main_loop;
                }
            }
        }
        return ret;
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

    boolean one_action_is_finished(State s) {
        for (Vehicle v : s.getDw_vehicles())
            if (v.getCurrent_action().isFinished() && v.getCurrent_action().getId() != Action.WAIT)
                return true;

        for (Vehicle v : s.getUp_vehicles())
            if (v.getCurrent_action().isFinished() && v.getCurrent_action().getId() != Action.WAIT)
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
