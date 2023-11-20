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

        /*Vehicle v1 = new Vehicle("V1", true); v1.setY_position(5);
        ParkingPlace p1 = new ParkingPlace("P1"); p1.setY_position(10);
        Vehicle v4 = new Vehicle("V4", false); v4.setY_position(30);
        Vehicle v5 = new Vehicle("V5", false); v5.setY_position(40);
        ParkingPlace p2 = new ParkingPlace("P2"); p2.setY_position(50);
        Vehicle v6 = new Vehicle("V6", false); v6.setY_position(60);
        Vehicle v2 = new Vehicle("V2", true); v1.setY_position(65);
        Vehicle v3 = new Vehicle("V3", true); v3.setY_position(70);
        ParkingPlace p3 = new ParkingPlace("P3"); p3.setY_position(80);
        Vehicle v7 = new Vehicle("V7", false); v7.setY_position(90);
        s_init.addVehicle(v1, true);
        s_init.addVehicle(v2, false);
        s_init.addVehicle(v3, false);
        s_init.addVehicle(v4, false);
        s_init.addVehicle(v5, false);
        s_init.addVehicle(v6, false);
        s_init.addVehicle(v7, false);
        s_init.addParkingPlace(p1);
        s_init.addParkingPlace(p2);
        s_init.addParkingPlace(p3);
        s_init.setParked_vehicle(v2, p2);*/

        Vehicle va = new Vehicle("Va", true);
        Vehicle vb = new Vehicle("Vb", true);
        //Vehicle vc = new Vehicle("Vc", true);
        s_init.addVehicle(va, true);
        s_init.addVehicle(vb, true);
        //s_init.addVehicle(vc, true);
        Vehicle v1 = new Vehicle("V1", true); v1.setY_position(5);
        ParkingPlace p1 = new ParkingPlace("P1"); p1.setY_position(10);
        Vehicle v6 = new Vehicle("V6", false); v6.setY_position(40);
        ParkingPlace p2 = new ParkingPlace("P2"); p2.setY_position(35); //35
        ParkingPlace p3 = new ParkingPlace("P3"); p3.setY_position(80);
        Vehicle v7 = new Vehicle("V7", false); v7.setY_position(100);
        s_init.addVehicle(v1, false);
        s_init.addVehicle(v6, false);
        s_init.addVehicle(v7, false);
        s_init.addParkingPlace(p1);
        s_init.addParkingPlace(p2);
        s_init.addParkingPlace(p3);

        /*Vehicle va = new Vehicle("Va", true);
        Vehicle vb = new Vehicle("Vb", true);
        Vehicle vc = new Vehicle("Vc", true);
        Vehicle vd = new Vehicle("Vd", true);
        Vehicle ve = new Vehicle("Ve", true);
        s_init.addVehicle(va, true);
        s_init.addVehicle(vb, true);
        s_init.addVehicle(vc, true);
        s_init.addVehicle(vd, true);
        s_init.addVehicle(ve, true);
        Vehicle v1 = new Vehicle("V1", true); v1.setY_position(5);
        ParkingPlace p1 = new ParkingPlace("P1"); p1.setY_position(15);
        ParkingPlace p2 = new ParkingPlace("P2"); p2.setY_position(30);
        Vehicle v7 = new Vehicle("V7", false); v7.setY_position(40);
        s_init.addVehicle(v1, false);
        s_init.addVehicle(v7, false);
        s_init.addParkingPlace(p1);*/



        System.out.println(s_init);
        State s_final = new State(); // implicitly: contains no vehicles

        Search search = new Search(s_init, s_final);
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

            for (int i = 0; i < solution.size() - 1; i++)
                System.out.println("- step" + i + ": " + solution.get(i + 1).vehicles_action_str());

            for (int i = 0; i < solution.size() - 1; i++) {
                State s = solution.get(i);
                s.assignActions(solution.get(i + 1).getDw_vehicles());
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

