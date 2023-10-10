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

        /*Vehicle v1 = new Vehicle("V1", true); v1.setX_position(10);
        ParkingPlace p1 = new ParkingPlace("P1"); p1.setX_position(15);
        Vehicle v4 = new Vehicle("V4", false); v4.setX_position(30);
        Vehicle v5 = new Vehicle("V5", false); v5.setX_position(40);
        ParkingPlace p2 = new ParkingPlace("P2"); p2.setX_position(50);
        Vehicle v2 = new Vehicle("V2", true);
        Vehicle v6 = new Vehicle("V6", false); v6.setX_position(60);
        Vehicle v3 = new Vehicle("V3", true); v3.setX_position(70);
        ParkingPlace p3 = new ParkingPlace("P3"); p3.setX_position(80);
        Vehicle v7 = new Vehicle("V7", false); v7.setX_position(90);
        s_init.addVehicle(v1);
        s_init.addVehicle(v2);
        s_init.addVehicle(v3);
        s_init.addVehicle(v4);
        s_init.addVehicle(v5);
        s_init.addVehicle(v6);
        s_init.addVehicle(v7);
        s_init.addParkingPlace(p1);
        s_init.addParkingPlace(p2);
        s_init.addParkingPlace(p3);
        s_init.setParked_vehicle(v2, p2);*/

        /*Vehicle v1 = new Vehicle("V1", true); v1.setX_position(10);
        ParkingPlace p1 = new ParkingPlace("P1"); p1.setX_position(15);
        ParkingPlace p2 = new ParkingPlace("P2"); p2.setX_position(30);
        ParkingPlace p3 = new ParkingPlace("P3"); p3.setX_position(45);
        Vehicle v4 = new Vehicle("V4", false); v4.setX_position(70);
        Vehicle v6 = new Vehicle("V6", false); v6.setX_position(80);
        Vehicle v7 = new Vehicle("V7", false); v7.setX_position(90);
        s_init.addVehicle(v1);
        s_init.addVehicle(v4);
        s_init.addVehicle(v6);
        s_init.addVehicle(v7);
        s_init.addParkingPlace(p1);
        s_init.addParkingPlace(p2);
        s_init.addParkingPlace(p3);/*

        Vehicle v1 = new Vehicle("V1", true); v1.setX_position(10);
        ParkingPlace p1 = new ParkingPlace("P1"); p1.setX_position(20);
        Vehicle v6 = new Vehicle("V6", false); v6.setX_position(40);
        Vehicle v2 = new Vehicle("V2", true); v2.setX_position(60);
        ParkingPlace p3 = new ParkingPlace("P3"); p3.setX_position(70);
        Vehicle v7 = new Vehicle("V7", false); v7.setX_position(95);
        s_init.addVehicle(v1);
        s_init.addVehicle(v2);
        s_init.addVehicle(v6);
        s_init.addVehicle(v7);
        s_init.addParkingPlace(p1);
        s_init.addParkingPlace(p3);

        /*Vehicle v1 = new Vehicle("V1", true); v1.setX_position(10);
        ParkingPlace p1 = new ParkingPlace("P1"); p1.setX_position(20);
        ParkingPlace p2 = new ParkingPlace("P2"); p2.setX_position(50);
        Vehicle v5 = new Vehicle("V5", false); v5.setX_position(55);
        Vehicle v6 = new Vehicle("V6", false); v6.setX_position(65);
        Vehicle v7 = new Vehicle("V7", false); v7.setX_position(75);
        ParkingPlace p3 = new ParkingPlace("P3"); p3.setX_position(80);
        s_init.addVehicle(v1);
        s_init.addVehicle(v5);
        s_init.addVehicle(v6);
        s_init.addVehicle(v7);
        s_init.addParkingPlace(p1);
        s_init.addParkingPlace(p2);
        s_init.addParkingPlace(p3);*/

        Vehicle v1 = new Vehicle("V1", true); v1.setX_position(10);
        ParkingPlace p1 = new ParkingPlace("P1"); p1.setX_position(15);
        ParkingPlace p2 = new ParkingPlace("P2"); p2.setX_position(30);
        Vehicle v7 = new Vehicle("V7", false); v7.setX_position(40);
        s_init.addVehicle(v1);
        s_init.addVehicle(v7);
        s_init.addParkingPlace(p1);

        System.out.println(s_init);
        State s_final = new State(); // implicitly: contains no vehicles

        Search search = new Search(s_init, s_final);
        double start = System.currentTimeMillis();
        ArrayList<State> solution = search.AStar();
        double end = System.currentTimeMillis();
        System.out.printf("search time = %.2f\n", (end - start) / 1000);
        System.out.printf("nb explored states = %d\n", search.nb_explored_states);
        if (solution == null) {
            System.out.printf("!!! No solution !!!\n");
        }
        else
        {
            System.out.printf("solution length = %d\n", solution.size());

            State s0 = s_init.getCopy();
            s0.assignActions(solution.get(1).getDw_vehicles());
            solution.add(s0);
            Collections.reverse(solution);

            for (int i = 0; i < solution.size() - 1; i++) {
                State s = solution.get(i);
                System.out.println("- step" + i + ": " + solution.get(i + 1).vehicles_action_str());
            }

            for (int i = 0; i < solution.size() - 1; i++) {
                State s = solution.get(i);
                s.assignActions(solution.get(i + 1).getDw_vehicles());
                System.out.println("- step" + i + ", state:");
                System.out.println("Actions: " + solution.get(i + 1).vehicles_action_str());
                System.out.print(s);
                State.mini_simulator.replay(s);
            }
        }

    }
}

