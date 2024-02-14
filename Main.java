import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        Display display = new Display();
        MiniSimulator mini_simultor = new MiniSimulator(display);
        State.setMini_simulator(mini_simultor);

        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(1200, 1200);
        jFrame.add(display);
        jFrame.setVisible(true);

        State s_init = new State();

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

        /*Vehicle va = new Vehicle("Va", true, false);
        Vehicle vb = new Vehicle("Vb", true, false);
        s_init.addVehicle(va, true);
        s_init.addVehicle(vb, true);
        Vehicle v1 = new Vehicle("V1", false, true);
        Vehicle v2 = new Vehicle("V2", false, false);
        s_init.addVehicle(v1, true);
        s_init.addVehicle(v2, true);
        ParkingPlace p1 = new ParkingPlace("P1"); p1.setY_position(30);
        ParkingPlace p2 = new ParkingPlace("P2"); p2.setY_position(100);
        ParkingPlace p3 = new ParkingPlace("P3"); p3.setY_position(150);
        s_init.addParkingPlace(p1);
        s_init.addParkingPlace(p2);
        s_init.addParkingPlace(p3);*/

/*
        Vehicle va = new Vehicle("Va", true, false);
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
        //s_init.addParkingPlace(p5);
*/
        // difference with H_react : YES !!!
        Vehicle va = new Vehicle("Va", true, false);
        Vehicle vb = new Vehicle("Vb", true, false);
        s_init.addVehicle(va, true);
        s_init.addVehicle(vb, true);
        Vehicle v1 = new Vehicle("V1", false, false);
        s_init.addVehicle(v1, true);
        ParkingPlace p1 = new ParkingPlace("P1"); p1.setY_position(30);
        ParkingPlace p2 = new ParkingPlace("P2"); p2.setY_position(80);
        s_init.addParkingPlace(p1);
        s_init.addParkingPlace(p2);


        System.out.println(s_init);
        State s_final = new State(); // implicitly: contains no vehicles

        //mini_simultor.reactively_simulate(s_init);
        //System.exit(0);

        Search search = new Search(s_init, s_final);

        mini_simultor.displayState(s_init);

        double start = System.currentTimeMillis();
        ArrayList<State> solution = search.AStar();
        double end = System.currentTimeMillis();
        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("search time = %.2f\n", (end - start) / 1000);
        System.out.printf("nb explored states = %d\n", search.nb_explored_states);
        if (solution == null) {
            System.out.printf("!!! No solution !!!\n");
        }
        else
        {
            System.out.println("---------------------------------------------------------------------------------");
            System.out.printf("solution length = %d\n", solution.size());
            Collections.reverse(solution);

            for (int i = 0; i < solution.size() - 1; i++) {
                String duration = String.format("%.1f", solution.get(i + 1).getDuration());
                System.out.println("- step" + i + ": " + solution.get(i + 1).vehicles_action_str() + "        (" + duration + "s)");
            }

            for (int i = 0; i < solution.size() - 1; i++) {
                State s = solution.get(i);
                s.assignActions(solution.get(i + 1).getVehicles());
                double duration = solution.get(i + 1).getDuration();
                /*System.out.println("-------------------");
                System.out.println("- step" + i + ", state:");
                System.out.println("Actions: " + solution.get(i + 1).vehicles_action_str());
                System.out.print(s);
                System.out.printf("--> duration = %.2fs\n", duration);
                System.out.println("-------------------");*/
                State.mini_simulator.replay(s, duration);
            }
        }

    }
}

