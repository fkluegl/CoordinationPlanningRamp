import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.lang.Math;

public class Search {
    State init_state;
    State final_state;
    PriorityQueue<State> openSet;
    int nb_explored_states;

    public Search(State s0, State sN) {
        init_state = s0;
        final_state = sN;
    }

    public ArrayList<State> AStar() {
        nb_explored_states = 1;
        openSet = new PriorityQueue<State>(16, new StateComparator());
        openSet.add(init_state);
        init_state.g_score = 0;
        init_state.f_score = h(init_state);

        while (openSet.size() > 0) {
            State current = openSet.poll();
            nb_explored_states ++;
            if (current.equals(final_state)) {
                return reconstruct_path(current);
            }

            ArrayList<State> successors = current.get_previous_states();
            for (State succ : successors) {
                //double D = 1;
                double D = euclidian_distance(current, succ);
                double tentative_gScore = current.g_score + D;
                if (tentative_gScore < succ.g_score) {
                    succ.cameFrom = current;
                    succ.g_score = tentative_gScore;
                    succ.f_score = tentative_gScore + h(succ);
                    if (!is_in_openSet(succ)) {
                        openSet.add(succ);
                        //if (openSet.size() % 1000 == 0)
                        //    System.out.println("openSet.size = " + openSet.size());
                    }
                }
            }
        }
        return null;
    }

    private double h(State s) {
        return 5 * count_clean_nodes(s);
        //return euclidian_distance(s, final_state);
    }

    private double euclidian_distance(State s1, State s2) {
        if (s1.current_node == s2.current_node)
            return 0;
        double x1 = s1.graph.graph.get(s1.current_node).x;
        double y1 = s1.graph.graph.get(s1.current_node).y;
        double x2 = s1.graph.graph.get(s2.current_node).x;
        double y2 = s1.graph.graph.get(s2.current_node).y;
        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }

    private boolean is_in_openSet(State x) {
        for (State s : openSet)
            if (x.equals(s))
                return true;
        return false;
    }

    private ArrayList<State> reconstruct_path(State s) {
        ArrayList<State> ret = new ArrayList<State>();
        State x = s;
        while (x.cameFrom != null) {
            ret.add(x);
            x = x.cameFrom;
        }
        return ret;
    }

    private int count_dirty_nodes(State s) {
        int ret = 0;
        for (int i=0; i<s.size; i++)
            if (!s.clean[i])
                ret ++;
        return ret;
    }

    private int count_clean_nodes(State s) {
        int ret = 0;
        for (int i=0; i<s.size; i++)
            if (s.clean[i])
                ret ++;
        return ret;
    }
}


class StateComparator implements Comparator<State> {
    public int compare(State s1, State s2) {
        if (s1.f_score < s2.f_score)
            return -1;
        else if (s1.f_score > s2.f_score)
            return 1;
        return 0;
    }
}