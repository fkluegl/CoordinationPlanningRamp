import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Display display = new Display();
        Graph g = new Graph();
        //g.initGraph();
        //g.initBigGraph();
        g.initHugeGraph();
        display.set_graph(g);

        State s_final = new State(g, true);
        s_final.current_node = 0;
        s_final.clean[0] = false;

        State s_init = new State(g, false);
        s_init.current_node = 0;

        Search search = new Search(s_final, s_init);
        double start = System.currentTimeMillis();
        ArrayList<State> solution = search.AStar();
        double end = System.currentTimeMillis();
        System.out.printf("search time = %.2f\n", (end - start) / 1000);
        System.out.printf("nb explored states = %d\n", search.nb_explored_states);
        System.out.printf("solution length = %d", solution.size());

        for (int i = 0; i < solution.size(); i++) {
            State s = solution.get(i);
            display.set_state(s);
            display.refresh();
            TimeUnit.MILLISECONDS.sleep(1000);
        }
        display.set_state(s_final);
        display.refresh();

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