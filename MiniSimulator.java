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

    public double reactively_simulate(State s) {
        // make a copy of s because we'll change the actions assigned to vehicles
        State scopy = s.getCopy();

        double simulation_time = 0.0;
        double DT = 0.01;

        //display.set_state(scopy);
        //display.repaint();
        //try { Thread.sleep(1); } catch (InterruptedException e) { throw new RuntimeException(e); }

        while (true) {
            simulation_time += DT;
            //display.repaint();
            //try { Thread.sleep(1); } catch (InterruptedException e) { throw new RuntimeException(e); }

            for (Vehicle v : scopy.getVehicles())
            {
                assignHeuristicBehavior(scopy, v);
                int event = v.step(DT);

                if (event == Vehicle.ACTION_COMPLETED) {
                    v.apply_current_action_effects();
                    if ((v.getCurrent_action().getId() == Action.GO_DOWN || v.getCurrent_action().getId() == Action.GO_UP) && v.getCurrent_action().isFinished()) {
                        scopy.removeVehicle(v.name);
                        break;
                    }
                    continue;
                }

                // check for collision --> park
                Vehicle collv = scopy.collides_with(v);
                if (collv != null && v.isDownward() && collv.isUpward()) {
                    ParkingPlace virtualpp = new ParkingPlace("virtual");
                    virtualpp.parentState = scopy;
                    virtualpp.y_position = v.y_position;
                    v.setCurrent_action(new Action(Action.PARK, virtualpp));
                    continue;
                }
            }

            if (scopy.getNv() == 0)
                break;
        }

        System.out.printf("reactive simulation time = %.2f\n", simulation_time);
        return simulation_time;
    }


    private void assignHeuristicBehavior(State s, Vehicle v) {
        // optimistic unparking
        if (v.getCurrent_action().getId() == Action.PARK && v.getCurrent_action().isFinished()) {
            v.setCurrent_action(new Action(Action.UNPARK, v.getCurrent_action().getParameter()));
            return;
        }

        // GO UP/DOWN
        if (v.isIn_ramp() && (v.getCurrent_action().isFinished() || v.getCurrent_action().getId() == Action.WAIT)) {
            if (v.isDownward()) v.setCurrent_action(new Action(Action.GO_DOWN));
            else                v.setCurrent_action(new Action(Action.GO_UP));
            return;
        }

        // ENTER
        if (!v.isIn_ramp() && v.isFirst()) {
            Vehicle cv = s.get_closest_vehicle_ahead(v);

            if (cv == null) {
                v.setCurrent_action(new Action(Action.ENTER));
                return;
            }

            //SceneElement cse = s.get_closest_sceneelement_ahead(v);


            // case optimistic platooning
            if (cv != null) {
                if (cv.has_same_orientation_as(v)) {
                    v.setCurrent_action(new Action(Action.ENTER));
                    return;
                }
            }

            // case optimistic quick parking
            //if (cse.isParkingPlace()) {
            //    Vehicle cv = s.get_closest_vehicle_ahead(v);
            //    if (cv != null) {
            //        if (cv.has_opposite_orientation_as(v)) {
            //            v.setCurrent_action(new Action(Action.ENTER));
            //            return;
            //        }
            //    }
            //}
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
