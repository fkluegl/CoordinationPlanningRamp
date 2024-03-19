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

            //if (simulation_time >= 15.0)  //==> creates too many steps!
            //    return s.getCopy();

            if (s.allVehiclesWaiting())
                return s.getCopy();

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
                    if (simulation_time == 0.1)
                        System.out.println("DEBUG1");
                    return s.getCopy();
                }

                // check for collision --> end of the simulation
                if (something_collides(s)) {
                    if (simulation_time == 0.1)
                        System.out.println("DENUG2");
                    return null;
                }
            }
        }
        //return null;
    }

    public void replay(State s, double duration) {
        double simulation_time = 0.0;
        double DT = 0.01;

        display.set_state(s);
        display.repaint();
        try { Thread.sleep(1); } catch (InterruptedException e) { throw new RuntimeException(e); }

        while (true) {
            simulation_time += DT;

            display.repaint();
            try { Thread.sleep(1); } catch (InterruptedException e) { throw new RuntimeException(e); }

            for (Vehicle v : s.getVehicles())
            {
                v.step(DT);
                if (simulation_time >= duration)
                    return;
            }
        }
    }

    public double reactively_simulate(State s) { // (park virtual)
        boolean debug_heuristic = false;
        // make a copy of s because we'll change the actions assigned to vehicles
        State scopy = s.getCopy();

        double simulation_time = 0.0;
        double DT = 0.01;

        if (debug_heuristic) {
            display.set_state(scopy);
            display.repaint();
            try {Thread.sleep(1);} catch (InterruptedException e) {throw new RuntimeException(e);}
        }

        while (true) {
            simulation_time += DT;
            if (debug_heuristic) {
                display.repaint();
                try {Thread.sleep(1);} catch (InterruptedException e) {throw new RuntimeException(e);}
            }

            for (Vehicle v : scopy.getVehicles())
            {

                int event = v.step(DT);

                if (event == Vehicle.ACTION_COMPLETED) {
                    v.apply_current_action_effects();
                    if ((v.getCurrent_action().getId() == Action.GO_DOWN || v.getCurrent_action().getId() == Action.GO_UP) && v.getCurrent_action().isFinished()) {
                        scopy.removeVehicle(v.name);
                        break;
                    }
                    continue;
                }

                if (v.getCurrent_action().getId() == Action.UNPARK && v.getCurrent_action().isFinished()) {
                    if (v.isDownward())
                        v.setCurrent_action(new Action(Action.GO_DOWN));
                    else
                        v.setCurrent_action(new Action(Action.GO_UP));
                }

                if (v.getCurrent_action().getId() == Action.ENTER && v.getCurrent_action().isFinished()) {
                    if (v.isDownward())
                        v.setCurrent_action(new Action(Action.GO_DOWN));
                    else
                        v.setCurrent_action(new Action(Action.GO_UP));
                }

                // check for collision --> park virtual
                // TODO: comment for H_react_2
                /*Vehicle collv = scopy.collides_with(v);
                if (collv != null && v.isDownward() && collv.isUpward()) {
                    ParkingPlace vpp = new ParkingPlace("virtual");
                    vpp.y_position = v.y_position;
                    v.setCurrent_action(new Action(Action.PARK, vpp));
                    continue;
                }*/

                //assignHeuristicBehavior(scopy, v);
                assignHeuristicBehavior_2(scopy, v);
            }

            if (scopy.getNv() == 0)
                break;
        }

        //System.out.printf("reactive simulation time = %.2f\n", simulation_time);
        return simulation_time;
    }


    private void assignHeuristicBehavior(State s, Vehicle v) {
        // optimistic unparking
        if (v.getCurrent_action().getId() == Action.PARK && v.getCurrent_action().isFinished() || v.getCurrent_action().getId() == Action.WAIT) {
            ParkingPlace vpp = new ParkingPlace("virtual");
            vpp.y_position = v.y_position;
            v.setCurrent_action(new Action(Action.UNPARK, vpp));
            return;
        }

        // GO UP/DOWN
        if (v.isIn_ramp() && (v.getCurrent_action().isFinished() || v.getCurrent_action().getId() == Action.WAIT) && !v.is_parking() && !v.isParked()) {
            if (v.isDownward())
                v.setCurrent_action(new Action(Action.GO_DOWN));
            else
                v.setCurrent_action(new Action(Action.GO_UP));
            return;
        }

        // ENTER
        if (!v.isIn_ramp() && v.isFirst()) {
            Vehicle cv = s.get_closest_facing_vehicle(v);

            if (cv == null) { // if the ramp is empty or cv is in the same direction (=> platooning)
                v.setCurrent_action(new Action(Action.ENTER));
                return;
            }

            if (cv != null) {
                ParkingPlace cpp = s.get_closest_parkingplace_ahead(v);
                if (cpp != null) {
                    if (Math.abs(v.y_position - cpp.y_position) < Math.abs(v.y_position - cv.y_position)) {
                        v.setCurrent_action(new Action(Action.ENTER));
                        return;
                    }
                }
            }
        }
    }

    private void assignHeuristicBehavior_2(State s, Vehicle v) {
        if (v.getCurrent_action().getId() == Action.GO_DOWN) {
            Vehicle cv = s.get_closest_facing_vehicle(v);
            if (cv != null) {
                int npp = s.get_nb_pp_between(v, cv);
                if (npp == 1) {
                    ParkingPlace cpp = s.get_closest_parkingplace_ahead(v);
                    double dv_cv = Math.abs(v.y_position - cv.y_position);
                    double dv_cpp = Math.abs(v.y_position - cpp.y_position);
                    if (dv_cpp < dv_cv) { // if cpp in-between v and cv
                        double dcpp_cv = Math.abs(cpp.y_position - cv.y_position);
                        if (dv_cpp / v.getSpeed() < dcpp_cv / cv.getSpeed())
                            v.setCurrent_action(new Action(Action.PREPARK, cpp));
                        else
                            cv.setCurrent_action(new Action(Action.PREPARK, cpp));
                        return;
                    }
                }
            }
        }

        if (v.getCurrent_action().getId() == Action.PREPARK && v.getCurrent_action().isFinished()) {
            v.setCurrent_action(new Action(Action.PARK, v.getCurrent_action().getParameter()));
            return;
        }

        if (v.getCurrent_action().getId() == Action.PARK && v.getCurrent_action().isFinished()) {
            v.setCurrent_action(new Action(Action.UNPARK, v.getCurrent_action().getParameter()));
            return;
        }

        if (v.isIn_ramp() && v.getCurrent_action().isFinished() && v.getCurrent_action().getId() == Action.PARK) {
            v.setCurrent_action(new Action(Action.UNPARK, v.getCurrent_action().getParameter()));
            return;
        }

        // GO UP/DOWN
        if (v.isIn_ramp() && (v.getCurrent_action().isFinished() || v.getCurrent_action().getId() == Action.WAIT)) {
            if (v.isDownward())
                v.setCurrent_action(new Action(Action.GO_DOWN));
            else
                v.setCurrent_action(new Action(Action.GO_UP));
            return;
        }

        // ENTER
        if (!v.isIn_ramp() && v.isFirst()) {
            Vehicle cv = s.get_closest_facing_vehicle(v);

            if (cv == null) { // if the ramp is empty or cv is in the same direction (=> platooning)
                v.setCurrent_action(new Action(Action.ENTER));
                return;
            } else {
                ParkingPlace cpp = s.get_closest_parkingplace_ahead(v);
                if (cpp != null) {
                    if (Math.abs(v.y_position - cpp.y_position) / v.getSpeed()  + v.x_position / v.getSpeed() < Math.abs(cpp.y_position - cv.y_position) / cv.getSpeed()) {
                        v.setCurrent_action(new Action(Action.ENTER));
                        return;
                    }
                }
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
