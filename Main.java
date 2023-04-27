import javax.swing.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Display display = new Display();
        MiniSimulator mini_simultor = new MiniSimulator(display);
        State.setMini_simulator(mini_simultor);

        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(1200, 1200);
        jFrame.add(display);
        jFrame.setVisible(true);

        Vehicle v1 = new Vehicle("V1", true); v1.setX_position(10);
        ParkingPlace p1 = new ParkingPlace("P1"); p1.setX_position(20);
        Vehicle v4 = new Vehicle("V4", false); v4.setX_position(30);
        Vehicle v5 = new Vehicle("V5", false); v5.setX_position(40);
        ParkingPlace p2 = new ParkingPlace("P2"); p2.setX_position(50);
        Vehicle v2 = new Vehicle("V2", true); v2.setParkedAt(p2);//v2.setX_position(40);
        Vehicle v3 = new Vehicle("V3", true); v3.setX_position(90);

        State s_init = new State();

        s_init.addVehicle(v1);
        s_init.addVehicle(v2);
        s_init.addVehicle(v3);
        s_init.addVehicle(v4);
        s_init.addVehicle(v5);
        s_init.addParkingPlace(p1);
        s_init.addParkingPlace(p2);

        System.out.println(s_init);

        State s_final = new State(); // implicitly: contains no vehicles

        Search search = new Search(s_init, s_final);
        double start = System.currentTimeMillis();
        ArrayList<State> solution = search.AStar();
        double end = System.currentTimeMillis();
        System.out.printf("search time = %.2f\n", (end - start) / 1000);
        System.out.printf("nb explored states = %d\n", search.nb_explored_states);
        System.out.printf("solution length = %d\n", solution.size());

        State s0 = s_init.getCopy();
        s0.assignActions(solution.get(solution.size()-1).getInitial_dw_vehicles());
        System.out.println("- step0: " + solution.get(solution.size()-1).getInitial_dw_vehicles());
        State.mini_simulator.simulate(s0, true);

        for (int i = solution.size() - 1; i > 0; i--) {
            Thread.sleep(1000);
            State s = solution.get(i).getCopy();
            s.assignActions(solution.get(i-1).getInitial_dw_vehicles());
            //display.set_state(s);
            //display.refresh();
            State.mini_simulator.simulate(s, true);
            System.out.println("- step" + (solution.size() - i) + ": " + solution.get(i-1).initial_vehicle_action_str());
        }
        //display.set_state(s_final);
        //display.refresh();

    }
}

/*
Results for g = euclidian distance  and  h = 1 * count_clean_nodes(s)
#nodes      #states     #actions        time
15          2700        27              0.15
30          --------------------------------
45          --------------------------------

Results for g = euclidian distance  and  h = 2 * count_clean_nodes(s)
#nodes      #states     #actions        time
15          100         27              0.01
30          1000        56              0.07
45          40000       88              5.0

Results for g = euclidian distance  and  h = 3 * count_clean_nodes(s)
#nodes      #states     #actions        time
15          80          27              0.01
30          315         56              0.05
45          434         84              0.06

Results for g = euclidian distance  and  h = 4 * count_clean_nodes(s)
#nodes      #states     #actions        time
15          72          27              0.01
30          221         56              0.02
45          313         84              0.02

Results for g = euclidian distance  and  h = 5 * count_clean_nodes(s)
#nodes      #states     #actions        time
15          72          27              0.01
30          206         56              0.02
45          296         84              0.02
 */