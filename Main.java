import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println("No command line arguments found.");
            System.exit(0);
        }

        Display display = new Display();
        MiniSimulator mini_simultor = new MiniSimulator(display);
        State.setMini_simulator(mini_simultor);

        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(1200, 1200);
        jFrame.add(display);
        jFrame.setVisible(true);

        State s_init = new State();

        // PROBLEM 1
        /*Vehicle va = new Vehicle("Va", true, false); //va.setY_position(5);
        Vehicle vb = new Vehicle("Vb", true, false); //vb.setY_position(30);
        Vehicle vc = new Vehicle("Vc", true, false); vc.setY_position(30);
        Vehicle vd = new Vehicle("Vd", true, false);
        ParkingPlace p1 = new ParkingPlace("P1"); p1.setY_position(20);
        ParkingPlace p2 = new ParkingPlace("P2"); p2.setY_position(40);
        ParkingPlace p3 = new ParkingPlace("P3"); p3.setY_position(120);
        Vehicle v1 = new Vehicle("V1", false, false); //v1.setY_position(60);
        Vehicle v2 = new Vehicle("V2", false, true); //v2.setY_position(65);
        Vehicle v3 = new Vehicle("V3", false, false); v3.setY_position(90);
        Vehicle v4 = new Vehicle("V4", false, true);
        s_init.addVehicle(va, true);
        s_init.addVehicle(vb, true);
        s_init.addVehicle(vc, false);
        s_init.addVehicle(vd, true);
        s_init.addVehicle(v1, true);
        s_init.addVehicle(v2, true);
        s_init.addVehicle(v3, false);
        s_init.addVehicle(v4, true);
        s_init.addParkingPlace(p1);
        s_init.addParkingPlace(p2);
        s_init.addParkingPlace(p3);*/

        // PROBLEM 2
        //LONG!!!
        /*Vehicle vb = new Vehicle("Vb", true, false);
        Vehicle vc = new Vehicle("Vc", true, false);
        Vehicle vd = new Vehicle("Vd", true, false);
        s_init.addVehicle(vb, true);
        s_init.addVehicle(vc, false); vc.setY_position(10);
        s_init.addVehicle(vd, false); vd.setY_position(40);
        ParkingPlace p1 = new ParkingPlace("P1"); p1.setY_position(25);
        ParkingPlace p2 = new ParkingPlace("P2"); p2.setY_position(50);
        ParkingPlace p3 = new ParkingPlace("P3"); p3.setY_position(75);
        Vehicle v6 = new Vehicle("V6", false, true);
        Vehicle v7 = new Vehicle("V7", false, true);
        Vehicle v8 = new Vehicle("V8", false, true); v8.setY_position(70);
        s_init.addVehicle(v6, true);
        s_init.addVehicle(v7, true);
        s_init.addVehicle(v8, false); v8.setY_position(90);
        s_init.addParkingPlace(p1);
        s_init.addParkingPlace(p2);
        s_init.addParkingPlace(p3);*/

        if (args[0].equals("a")) {
            double ramp_length = Double.valueOf(args[1]);
            State.y_max = ramp_length;
            // PROBLEM A - Length of the ramp
            Vehicle va = new Vehicle("Va", true, false);
            Vehicle vb = new Vehicle("Vb", true, false);
            s_init.addVehicle(va, true);
            s_init.addVehicle(vb, true);
            Vehicle v1 = new Vehicle("V1", false, true);
            Vehicle v2 = new Vehicle("V2", false, false);
            s_init.addVehicle(v1, true);
            s_init.addVehicle(v2, true);
            ParkingPlace p1 = new ParkingPlace("P1"); p1.setY_position(State.y_max / 10 * 2);
            ParkingPlace p2 = new ParkingPlace("P2"); p2.setY_position(State.y_max / 10 * 3);
            s_init.addParkingPlace(p1);
            s_init.addParkingPlace(p2);
        } else if (args[0].equals("b")) { // 1234 6789 2389 2468
            // PROBLEM B - PP positions
            State.y_max = 200;
            Vehicle va = new Vehicle("Va", true, false);
            Vehicle vb = new Vehicle("Vb", true, false);
            s_init.addVehicle(va, true);
            s_init.addVehicle(vb, true);
            Vehicle v1 = new Vehicle("V1", false, true);
            Vehicle v2 = new Vehicle("V2", false, false);
            s_init.addVehicle(v1, true);
            s_init.addVehicle(v2, true);
            ParkingPlace p1 = new ParkingPlace("P1");
            ParkingPlace p2 = new ParkingPlace("P2");
            ParkingPlace p3 = new ParkingPlace("P3");
            ParkingPlace p4 = new ParkingPlace("P4");
            if (args[1].equals("1234")) {
                p1.setY_position(State.y_max / 10 * 1);
                p2.setY_position(State.y_max / 10 * 2);
                p3.setY_position(State.y_max / 10 * 3);
                p4.setY_position(State.y_max / 10 * 4);
            } else if (args[1].equals("6789")) {
                p1.setY_position(State.y_max / 10 * 6);
                p2.setY_position(State.y_max / 10 * 7);
                p3.setY_position(State.y_max / 10 * 8);
                p4.setY_position(State.y_max / 10 * 9);
            } else if (args[1].equals("2389")) {
                p1.setY_position(State.y_max / 10 * 2);
                p2.setY_position(State.y_max / 10 * 3);
                p3.setY_position(State.y_max / 10 * 8);
                p4.setY_position(State.y_max / 10 * 9);
            } else if (args[1].equals("2468")) {
                p1.setY_position(State.y_max / 10 * 2);
                p2.setY_position(State.y_max / 10 * 4);
                p3.setY_position(State.y_max / 10 * 6);
                p4.setY_position(State.y_max / 10 * 8);
            } else {
                System.out.println("wrong program args for pp positions");
                System.exit(0);
            }

            s_init.addParkingPlace(p1);
            s_init.addParkingPlace(p2);
            s_init.addParkingPlace(p3);
            s_init.addParkingPlace(p4);
        } else if (args[0].equals("c")) {
            int nb_vehicles = Integer.valueOf(args[1]);
            // PROBLEM C - # vehicles
            Vehicle va = new Vehicle("Va", true, false);
            Vehicle vb = new Vehicle("Vb", true, false);
            Vehicle vc = new Vehicle("Vc", true, false);
            Vehicle vd = new Vehicle("Vd", true, false);
            Vehicle ve = new Vehicle("Ve", true, false);
            Vehicle vf = new Vehicle("Vf", true, false);
            Vehicle vg = new Vehicle("Vg", true, false);
            Vehicle vh = new Vehicle("Vh", true, false);
            Vehicle vi = new Vehicle("Vi", true, false);
            Vehicle vj = new Vehicle("Vj", true, false);
            if (nb_vehicles >= 1) s_init.addVehicle(va, true);
            if (nb_vehicles >= 3) s_init.addVehicle(vb, true);
            if (nb_vehicles >= 5) s_init.addVehicle(vc, true);
            if (nb_vehicles >= 7) s_init.addVehicle(vd, true);
            if (nb_vehicles >= 9) s_init.addVehicle(ve, true);
            if (nb_vehicles >= 11) s_init.addVehicle(vf, true);
            if (nb_vehicles >= 13) s_init.addVehicle(vg, true);
            if (nb_vehicles >= 15) s_init.addVehicle(vh, true);
            if (nb_vehicles >= 17) s_init.addVehicle(vi, true);
            if (nb_vehicles >= 19) s_init.addVehicle(vj, true);
            Vehicle v1 = new Vehicle("V1", false, true);
            Vehicle v2 = new Vehicle("V2", false, true);
            Vehicle v3 = new Vehicle("V3", false, true);
            Vehicle v4 = new Vehicle("V4", false, true);
            Vehicle v5 = new Vehicle("V5", false, true);
            Vehicle v6 = new Vehicle("V6", false, true);
            Vehicle v7 = new Vehicle("V7", false, true);
            Vehicle v8 = new Vehicle("V8", false, true);
            Vehicle v9 = new Vehicle("V9", false, true);
            Vehicle v10 = new Vehicle("V10", false, true);
            if (nb_vehicles >= 2) s_init.addVehicle(v1, true);
            if (nb_vehicles >= 4) s_init.addVehicle(v2, true);
            if (nb_vehicles >= 6) s_init.addVehicle(v3, true);
            if (nb_vehicles >= 8) s_init.addVehicle(v4, true);
            if (nb_vehicles >= 10) s_init.addVehicle(v5, true);
            if (nb_vehicles >= 12) s_init.addVehicle(v6, true);
            if (nb_vehicles >= 14) s_init.addVehicle(v7, true);
            if (nb_vehicles >= 16) s_init.addVehicle(v8, true);
            if (nb_vehicles >= 18) s_init.addVehicle(v9, true);
            if (nb_vehicles >= 20) s_init.addVehicle(v10, true);
            ParkingPlace p1 = new ParkingPlace("P1");
            p1.setY_position(State.y_max / 10 * 3);
            ParkingPlace p2 = new ParkingPlace("P2");
            p2.setY_position(State.y_max / 10 * 6);
            s_init.addParkingPlace(p1);
            s_init.addParkingPlace(p2);
        } else {
            System.out.println("wrong program args");
            System.exit(0);
        }

        // PROBLEM 4
        /*Vehicle va = new Vehicle("Va", true, false);
        //Vehicle vb = new Vehicle("Vb", true, false);
        //Vehicle vc = new Vehicle("Vc", true, false);
        //Vehicle vd = new Vehicle("Vd", true, false);
        //Vehicle ve = new Vehicle("Ve", true, false);
        //Vehicle vf = new Vehicle("Vf", true, false);
        s_init.addVehicle(va, true);
        //s_init.addVehicle(vb, true);
        //s_init.addVehicle(vc, true);
        //s_init.addVehicle(vd, true);
        //s_init.addVehicle(ve, true);
        //s_init.addVehicle(vf, true);
        Vehicle v1 = new Vehicle("V1", false, true);
        Vehicle v2 = new Vehicle("V2", false, true);
        //Vehicle v3 = new Vehicle("V3", false, true);
        //Vehicle v4 = new Vehicle("V4", false, true);
        //Vehicle v5 = new Vehicle("V5", false, true);
        v1.setSpeed(2.0);
        v2.setSpeed(2.0);
        //v3.setSpeed(2.0);
        //v4.setSpeed(2.0);
        //v5.setSpeed(1.5);
        s_init.addVehicle(v1, true);
        s_init.addVehicle(v2, true);
        //s_init.addVehicle(v3, true);
        //s_init.addVehicle(v4, true);
        //s_init.addVehicle(v5, true);
        ParkingPlace p1 = new ParkingPlace("P1"); p1.setY_position(50);
        ParkingPlace p2 = new ParkingPlace("P2"); p2.setY_position(90);
        //ParkingPlace p3 = new ParkingPlace("P3"); p3.setY_position(130);
        //ParkingPlace p4 = new ParkingPlace("P4"); p4.setY_position(80);
        //ParkingPlace p5 = new ParkingPlace("P5"); p5.setY_position(100);
        s_init.addParkingPlace(p1);
        s_init.addParkingPlace(p2);
        //s_init.addParkingPlace(p3);
        //s_init.addParkingPlace(p4);
        //s_init.addParkingPlace(p5);*/

        // PROBLEM 5
        // difference with H_react : YES !!!
        /*Vehicle va = new Vehicle("Va", true, false);
        Vehicle vb = new Vehicle("Vb", true, false);
        s_init.addVehicle(va, true);
        s_init.addVehicle(vb, true);
        Vehicle v1 = new Vehicle("V1", false, false);
        s_init.addVehicle(v1, true);
        ParkingPlace p1 = new ParkingPlace("P1"); p1.setY_position(20);
        ParkingPlace p2 = new ParkingPlace("P2"); p2.setY_position(50);
        s_init.addParkingPlace(p1);
        s_init.addParkingPlace(p2);*/


        System.out.println(s_init);
        State s_final = new State(); // implicitly: contains no vehicles
        Search search = new Search(s_init, s_final);

        mini_simultor.displayState(s_init);

        double start = System.currentTimeMillis();
        ArrayList<State> solution = search.AStar();
        double end = System.currentTimeMillis();

        if (solution == null) {
            System.out.printf("!!! No solution !!!\n");
            // write results to file
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(args[0] + "_Hreact.result", true));
                writer.append(String.format("args[1]=%s 999\n", args[1]));
                writer.close();
            } catch (IOException e) {throw new RuntimeException(e);}
        }
        else
        {
            System.out.println("total_duration = " + solution.get(0).total_duration);
            System.out.println("---------------------------------------------------------------------------------");
            System.out.printf("search time = %.2f\n", (end - start) / 1000);
            System.out.printf("nb explored states = %d\n", search.nb_explored_states);

            // write results to file
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(args[0] + "_Hreact.result", true));
                writer.append(String.format("%s %.2f %.2f %d %d\n", args[1], solution.get(0).total_duration, (end - start) / 1000, search.nb_explored_states, solution.size()));
                writer.close();
            } catch (IOException e) {throw new RuntimeException(e);}

            System.out.println("---------------------------------------------------------------------------------");
            System.out.printf("solution length = %d\n", solution.size());
            Collections.reverse(solution);

            for (int i = 0; i < solution.size() - 1; i++) {
                String duration = String.format("%.1f", solution.get(i + 1).getDuration());
                System.out.println("- step" + i + ": " + solution.get(i + 1).vehicles_action_str() + "        (" + duration + "s)");
            }

            if (true) {
                for (int i = 0; i < solution.size() - 1; i++) {
                    State s = solution.get(i);
                    s.assignActions(solution.get(i + 1).getVehicles());
                    double duration = solution.get(i + 1).getDuration();
                    State.mini_simulator.replay(s, duration);
                }
            }
        }
        System.exit(0);
    }
}

