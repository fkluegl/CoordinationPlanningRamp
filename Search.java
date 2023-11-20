import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

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
        init_state.f_score = 1000; //h(init_state);

        while (openSet.size() > 0) {
            State current = openSet.poll();
            nb_explored_states ++;
            if (current.equals(final_state)) {
                return reconstruct_path(current);
            }

            System.out.println("     [ASTAR] get_next_states()");
            ArrayList<State> successors = current.get_next_states();

            for (State succ : successors) {
                double tentative_gScore = current.g_score + G(succ);
                if (tentative_gScore < succ.g_score) {
                    succ.cameFrom = current;
                    succ.g_score = tentative_gScore;
                    succ.f_score = tentative_gScore + H(succ);
                    succ.depth = current.depth + 1;
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

    private double G(State s) {
        //return 1;
        return s.getDuration();
    }
    private double H(State s) {
        //return 0;
        return time_to_goal2(s);
    }

    private double time_to_goal(State s) {
        // under-estimate time_to_goal
        double maxt = 0;
        double t;
        for (Vehicle v : s.getDw_vehicles())
            if (!v.isOut()) {
                t = (State.y_max - v.getY_position()) / v.getSpeed();
                if (t > maxt) {
                    maxt = t;
                }
            }

        for (Vehicle v : s.getUp_vehicles())
            if (!v.isOut()) {
                t = v.getY_position() / v.getSpeed();
                if (t > maxt) {
                    maxt = t;
                }
            }

        return maxt;
    }

    private double time_to_goal2(State s) {
        // over-estimates time_to_goal
        double t = 0;
        for (Vehicle v : s.getDw_vehicles())
            if (!v.isOut()) {
                t += (State.y_max - v.getY_position()) / v.getSpeed();
            }

        for (Vehicle v : s.getUp_vehicles())
            if (!v.isOut()) {
                t += v.getY_position() / v.getSpeed();
            }

        return t;
    }


    private double distance_to_goal(State s) {
        double D = 0;
        for (Vehicle v : s.getDw_vehicles())
            if (!v.isOut())
                D += (State.y_max - v.getY_position());

        for (Vehicle v : s.getUp_vehicles())
            if (!v.isOut())
                D += v.getY_position();

        return D;
    }

    private boolean is_in_openSet(State x) {
        for (State s : openSet) {
            //if (!x.equals(s) && x.toString().equals(s.toString()))
            //    System.out.println("hihihi");
            if (x.equals(s))
                return true;
        }
        return false;
    }

    private ArrayList<State> reconstruct_path(State s) {
        ArrayList<State> ret = new ArrayList<State>();
        State x = s;
        while (x.cameFrom != null) {
            ret.add(x);
            x = x.cameFrom;
        }
        //TODO: add s0 ?
        ret.add(x);
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