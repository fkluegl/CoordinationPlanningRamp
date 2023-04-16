import java.util.ArrayList;

public class State {
    public Graph graph;
    public boolean[] clean;
    public int current_node;
    public int size;
    public double g_score = 1000;
    public double f_score = 1000;
    public State cameFrom = null;

    public State(Graph g, boolean val) {
        graph = g;
        size = graph.graph.size();
        clean = new boolean[size];
        for (int i=0; i<size; i++) clean[i] = val;
    }

    public State getCopy() {
        State ret = new State(graph, false);
        for (int i=0; i<size; i++)
            if (clean[i])
                ret.clean[i] = true;
        ret.current_node = current_node;
        return ret;
    }

    public boolean equals(State s) {
        if (this.current_node != s.current_node)
            return false;

        for (int i=0; i<size; i++)
            if (this.clean[i] != s.clean[i])
                return false;

        return true;
    }

    public ArrayList<State> get_previous_states_OLD() {  // (cleans where the robot is located)
        ArrayList<State> ret = new ArrayList<State>();

        // 2 possible actions: (1) CLEAN or (2) MOVE to a non-clean node
        // (1) previous state = if current_node is clean, current_node is the same, but not clean
        if (clean[current_node]) {
            State s2 = this.getCopy();
            s2.clean[current_node] = false;
            ret.add(s2);
        }

        // (2) previous state = if current_node is not clean, current_node = all neighbours, clean or not
        if (!clean[current_node]) {
            ArrayList<Node> current_neighbours = graph.graph.get(current_node).neighbours;
            for (Node n : current_neighbours) {
                int idx = graph.graph.indexOf(n);
                State s2 = this.getCopy();
                s2.current_node = idx;
                ret.add(s2);
            }
        }
        return ret;
    }

    public ArrayList<State> get_previous_states() {  // (cleans neighbour locations)
        ArrayList<State> ret = new ArrayList<State>();

        // 2 possible actions: (1) CLEAN a neighbour or (2) MOVE to a dirty node
        // (1) previous state = one of the clean neighbours becomes dirty
        ArrayList<Node> current_neighbours = graph.graph.get(current_node).neighbours;
        for (Node n : current_neighbours) {
            int idx = graph.graph.indexOf(n);
            if (clean[idx]) {
                State s2 = this.getCopy();
                s2.clean[idx] = false;
                ret.add(s2);
            }
        }

        // (2) previous state = if current_node is not clean, current_node = all dirty neighbours (cannot be on a clean node)
        if (!clean[current_node]) {
            for (Node n : current_neighbours) {
                int idx = graph.graph.indexOf(n);
                if (!clean[idx]) {
                    State s2 = this.getCopy();
                    s2.current_node = idx;
                    ret.add(s2);
                }
            }
        }
        return ret;
    }


}
