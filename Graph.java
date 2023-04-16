import java.util.ArrayList;
import java.util.Arrays;

public class Graph {
    public ArrayList<Node> graph;
    public Graph() {}

    public Graph getCopy() {
        Graph ret = new Graph();
        ret.graph = new ArrayList<Node>();
        for (Node n : this.graph) {
            ret.graph.add(n.getCopy_without_neighbours());
        }
        // copy neighbours
        for (int i=0; i<graph.size(); i++) {
            Node orig = this.graph.get(i);
            Node copy = ret.graph.get(i);

            for (Node neibs : orig.neighbours) {
                String lab = neibs.label;
                for (int j=0; j<graph.size(); j++) {
                    if (this.graph.get(j).label == lab)
                        copy.neighbours.add(ret.graph.get(j));
                }
            }
        }
        return ret;
    }
    public void initGraph() {
        graph = new ArrayList<Node>();
        Node n1 = new Node("n1", 1.717, -3.255);
        //Node n2 = new Node("n2", 0, 0);
        Node n3 = new Node("n3", 0.925, -3.023);
        Node n4 = new Node("n4", 0.756, -2.166);
        Node n5 = new Node("n5", 1.291, -1.234);
        Node n6 = new Node("n6", 0.226, -1.258);
        Node n7 = new Node("n7", -0.707, -1.273);
        Node n8 = new Node("n8", -1.465, -1.279);
        Node n9 = new Node("n9", -1.076, -0.698);
        Node n10 = new Node("n10", -0.194, -0.658);
        Node n11 = new Node("n11", 0.699, -0.697);
        Node n12 = new Node("n12", 1.252, -0.782);
        Node n13 = new Node("n13", 1.314, -0.138);
        Node n14 = new Node("n14", 0.528, -0.119);
        Node n15 = new Node("n15", -0.458, 0.003);
        Node n16 = new Node("n16", -0.674, 0.580);
        n1.addNeighbours(n3);
        n3.addNeighbours(n4);
        n4.addNeighbours(n5);
        n4.addNeighbours(n6);
        n5.addNeighbours(n6);
        n5.addNeighbours(n11);
        n5.addNeighbours(n12);
        n6.addNeighbours(n9);
        n6.addNeighbours(n10);
        n6.addNeighbours(n11);
        n7.addNeighbours(n8);
        n7.addNeighbours(n9);
        n7.addNeighbours(n6);
        n7.addNeighbours(n10);
        n9.addNeighbours(n10);
        n10.addNeighbours(n11);
        n10.addNeighbours(n14);
        n10.addNeighbours(n15);
        n11.addNeighbours(n12);
        n11.addNeighbours(n13);
        n11.addNeighbours(n14);
        n11.addNeighbours(n15);
        n12.addNeighbours(n13);
        n13.addNeighbours(n14);
        n14.addNeighbours(n15);
        n15.addNeighbours(n16);
        // reverse
        n3.addNeighbours(n1);
        n4.addNeighbours(n3);
        n5.addNeighbours(n4);
        n6.addNeighbours(n4);
        n6.addNeighbours(n5);
        n11.addNeighbours(n5);
        n12.addNeighbours(n5);
        n9.addNeighbours(n6);
        n10.addNeighbours(n6);
        n11.addNeighbours(n6);
        n8.addNeighbours(n7);
        n9.addNeighbours(n7);
        n6.addNeighbours(n7);
        n10.addNeighbours(n7);
        n10.addNeighbours(n9);
        n11.addNeighbours(n10);
        n14.addNeighbours(n10);
        n15.addNeighbours(n10);
        n12.addNeighbours(n11);
        n13.addNeighbours(n11);
        n14.addNeighbours(n11);
        n15.addNeighbours(n11);
        n13.addNeighbours(n12);
        n14.addNeighbours(n13);
        n15.addNeighbours(n14);
        n16.addNeighbours(n15);
        ArrayList al = new ArrayList (Arrays.asList(n1, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16));
        graph.addAll(al);
    }

    public void initBigGraph() {
        graph = new ArrayList<Node>();
        Node n1 = new Node("n1", 1.717, -3.255);
        Node n3 = new Node("n3", 0.925, -3.023);
        Node n4 = new Node("n4", 0.756, -2.166);
        Node n5 = new Node("n5", 1.291, -1.234);
        Node n6 = new Node("n6", 0.226, -1.258);
        Node n7 = new Node("n7", -0.707, -1.273);
        Node n8 = new Node("n8", -1.465, -1.279);
        Node n9 = new Node("n9", -1.076, -0.698);
        Node n10 = new Node("n10", -0.194, -0.658);
        Node n11 = new Node("n11", 0.699, -0.697);
        Node n12 = new Node("n12", 1.252, -0.782);
        Node n13 = new Node("n13", 1.314, -0.138);
        Node n14 = new Node("n14", 0.528, -0.119);
        Node n15 = new Node("n15", -0.458, 0.003);
        Node n16 = new Node("n16", -0.674, 0.580);
        n1.addNeighbours(n3);
        n3.addNeighbours(n4);
        n4.addNeighbours(n5);
        n4.addNeighbours(n6);
        n5.addNeighbours(n6);
        n5.addNeighbours(n11);
        n5.addNeighbours(n12);
        n6.addNeighbours(n9);
        n6.addNeighbours(n10);
        n6.addNeighbours(n11);
        n7.addNeighbours(n8);
        n7.addNeighbours(n9);
        n7.addNeighbours(n6);
        n7.addNeighbours(n10);
        n9.addNeighbours(n10);
        n10.addNeighbours(n11);
        n10.addNeighbours(n14);
        n10.addNeighbours(n15);
        n11.addNeighbours(n12);
        n11.addNeighbours(n13);
        n11.addNeighbours(n14);
        n11.addNeighbours(n15);
        n12.addNeighbours(n13);
        n13.addNeighbours(n14);
        n14.addNeighbours(n15);
        n15.addNeighbours(n16);
        // reverse
        n3.addNeighbours(n1);
        n4.addNeighbours(n3);
        n5.addNeighbours(n4);
        n6.addNeighbours(n4);
        n6.addNeighbours(n5);
        n11.addNeighbours(n5);
        n12.addNeighbours(n5);
        n9.addNeighbours(n6);
        n10.addNeighbours(n6);
        n11.addNeighbours(n6);
        n8.addNeighbours(n7);
        n9.addNeighbours(n7);
        n6.addNeighbours(n7);
        n10.addNeighbours(n7);
        n10.addNeighbours(n9);
        n11.addNeighbours(n10);
        n14.addNeighbours(n10);
        n15.addNeighbours(n10);
        n12.addNeighbours(n11);
        n13.addNeighbours(n11);
        n14.addNeighbours(n11);
        n15.addNeighbours(n11);
        n13.addNeighbours(n12);
        n14.addNeighbours(n13);
        n15.addNeighbours(n14);
        n16.addNeighbours(n15);

        Node n21 = new Node("n21", 5.717, -3.255);
        Node n23 = new Node("n23", 4.925, -3.023);
        Node n24 = new Node("n24", 4.756, -2.166);
        Node n25 = new Node("n25", 5.291, -1.234);
        Node n26 = new Node("n26", 4.226, -1.258);
        Node n27 = new Node("n27", 3.293, -1.273);
        Node n28 = new Node("n28", 2.535, -1.279);
        Node n29 = new Node("n29", 2.924, -0.698);
        Node n210 = new Node("n210", 3.806, -0.658);
        Node n211 = new Node("n211", 4.699, -0.697);
        Node n212 = new Node("n212", 5.252, -0.782);
        Node n213 = new Node("n213", 5.314, -0.138);
        Node n214 = new Node("n214", 4.528, -0.119);
        Node n215 = new Node("n215", 3.542, 0.003);
        Node n216 = new Node("n216", 3.326, 0.580);
        n21.addNeighbours(n23);
        n23.addNeighbours(n24);
        n24.addNeighbours(n25);
        n24.addNeighbours(n26);
        n25.addNeighbours(n26);
        n25.addNeighbours(n211);
        n25.addNeighbours(n212);
        n26.addNeighbours(n29);
        n26.addNeighbours(n210);
        n26.addNeighbours(n211);
        n27.addNeighbours(n28);
        n27.addNeighbours(n29);
        n27.addNeighbours(n26);
        n27.addNeighbours(n210);
        n29.addNeighbours(n210);
        n210.addNeighbours(n211);
        n210.addNeighbours(n214);
        n210.addNeighbours(n215);
        n211.addNeighbours(n212);
        n211.addNeighbours(n213);
        n211.addNeighbours(n214);
        n211.addNeighbours(n215);
        n212.addNeighbours(n213);
        n213.addNeighbours(n214);
        n214.addNeighbours(n215);
        n215.addNeighbours(n216);
        // connect graphs
        n5.addNeighbours(n28);
        n12.addNeighbours(n28);
        n12.addNeighbours(n29);
        n13.addNeighbours(n29);
        n28.addNeighbours(n29);
        n5.addNeighbours(n29);
        n215.addNeighbours(n29);

        // reverse
        n23.addNeighbours(n21);
        n24.addNeighbours(n23);
        n25.addNeighbours(n24);
        n26.addNeighbours(n24);
        n26.addNeighbours(n25);
        n211.addNeighbours(n25);
        n212.addNeighbours(n25);
        n29.addNeighbours(n26);
        n210.addNeighbours(n26);
        n211.addNeighbours(n26);
        n28.addNeighbours(n27);
        n29.addNeighbours(n27);
        n26.addNeighbours(n27);
        n210.addNeighbours(n27);
        n210.addNeighbours(n29);
        n211.addNeighbours(n210);
        n214.addNeighbours(n210);
        n215.addNeighbours(n210);
        n212.addNeighbours(n211);
        n213.addNeighbours(n211);
        n214.addNeighbours(n211);
        n215.addNeighbours(n211);
        n213.addNeighbours(n212);
        n214.addNeighbours(n213);
        n215.addNeighbours(n214);
        n216.addNeighbours(n215);
        // connect graphs
        n28.addNeighbours(n5);
        n28.addNeighbours(n12);
        n29.addNeighbours(n12);
        n29.addNeighbours(n13);
        n29.addNeighbours(n28);
        n29.addNeighbours(n5);
        n29.addNeighbours(n215);

        ArrayList al = new ArrayList (Arrays.asList(n1, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16,
                                                    n21, n23, n24, n25, n26, n27, n28, n29, n210, n211, n212, n213, n214, n215, n216));
        graph.addAll(al);
    }

    public void initHugeGraph() {
        graph = new ArrayList<Node>();
        Node n1 = new Node("n1", 1.717, -3.255);
        Node n3 = new Node("n3", 0.925, -3.023);
        Node n4 = new Node("n4", 0.756, -2.166);
        Node n5 = new Node("n5", 1.291, -1.234);
        Node n6 = new Node("n6", 0.226, -1.258);
        Node n7 = new Node("n7", -0.707, -1.273);
        Node n8 = new Node("n8", -1.465, -1.279);
        Node n9 = new Node("n9", -1.076, -0.698);
        Node n10 = new Node("n10", -0.194, -0.658);
        Node n11 = new Node("n11", 0.699, -0.697);
        Node n12 = new Node("n12", 1.252, -0.782);
        Node n13 = new Node("n13", 1.314, -0.138);
        Node n14 = new Node("n14", 0.528, -0.119);
        Node n15 = new Node("n15", -0.458, 0.003);
        Node n16 = new Node("n16", -0.674, 0.580);
        n1.addNeighbours(n3);
        n3.addNeighbours(n4);
        n4.addNeighbours(n5);
        n4.addNeighbours(n6);
        n5.addNeighbours(n6);
        n5.addNeighbours(n11);
        n5.addNeighbours(n12);
        n6.addNeighbours(n9);
        n6.addNeighbours(n10);
        n6.addNeighbours(n11);
        n7.addNeighbours(n8);
        n7.addNeighbours(n9);
        n7.addNeighbours(n6);
        n7.addNeighbours(n10);
        n9.addNeighbours(n10);
        n10.addNeighbours(n11);
        n10.addNeighbours(n14);
        n10.addNeighbours(n15);
        n11.addNeighbours(n12);
        n11.addNeighbours(n13);
        n11.addNeighbours(n14);
        n11.addNeighbours(n15);
        n12.addNeighbours(n13);
        n13.addNeighbours(n14);
        n14.addNeighbours(n15);
        n15.addNeighbours(n16);
        // reverse
        n3.addNeighbours(n1);
        n4.addNeighbours(n3);
        n5.addNeighbours(n4);
        n6.addNeighbours(n4);
        n6.addNeighbours(n5);
        n11.addNeighbours(n5);
        n12.addNeighbours(n5);
        n9.addNeighbours(n6);
        n10.addNeighbours(n6);
        n11.addNeighbours(n6);
        n8.addNeighbours(n7);
        n9.addNeighbours(n7);
        n6.addNeighbours(n7);
        n10.addNeighbours(n7);
        n10.addNeighbours(n9);
        n11.addNeighbours(n10);
        n14.addNeighbours(n10);
        n15.addNeighbours(n10);
        n12.addNeighbours(n11);
        n13.addNeighbours(n11);
        n14.addNeighbours(n11);
        n15.addNeighbours(n11);
        n13.addNeighbours(n12);
        n14.addNeighbours(n13);
        n15.addNeighbours(n14);
        n16.addNeighbours(n15);

        Node n21 = new Node("n21", 5.717, -3.255);
        Node n23 = new Node("n23", 4.925, -3.023);
        Node n24 = new Node("n24", 4.756, -2.166);
        Node n25 = new Node("n25", 5.291, -1.234);
        Node n26 = new Node("n26", 4.226, -1.258);
        Node n27 = new Node("n27", 3.293, -1.273);
        Node n28 = new Node("n28", 2.535, -1.279);
        Node n29 = new Node("n29", 2.924, -0.698);
        Node n210 = new Node("n210", 3.806, -0.658);
        Node n211 = new Node("n211", 4.699, -0.697);
        Node n212 = new Node("n212", 5.252, -0.782);
        Node n213 = new Node("n213", 5.314, -0.138);
        Node n214 = new Node("n214", 4.528, -0.119);
        Node n215 = new Node("n215", 3.542, 0.003);
        Node n216 = new Node("n216", 3.326, 0.580);
        n21.addNeighbours(n23);
        n23.addNeighbours(n24);
        n24.addNeighbours(n25);
        n24.addNeighbours(n26);
        n25.addNeighbours(n26);
        n25.addNeighbours(n211);
        n25.addNeighbours(n212);
        n26.addNeighbours(n29);
        n26.addNeighbours(n210);
        n26.addNeighbours(n211);
        n27.addNeighbours(n28);
        n27.addNeighbours(n29);
        n27.addNeighbours(n26);
        n27.addNeighbours(n210);
        n29.addNeighbours(n210);
        n210.addNeighbours(n211);
        n210.addNeighbours(n214);
        n210.addNeighbours(n215);
        n211.addNeighbours(n212);
        n211.addNeighbours(n213);
        n211.addNeighbours(n214);
        n211.addNeighbours(n215);
        n212.addNeighbours(n213);
        n213.addNeighbours(n214);
        n214.addNeighbours(n215);
        n215.addNeighbours(n216);
        // connect graphs
        n5.addNeighbours(n28);
        n12.addNeighbours(n28);
        n12.addNeighbours(n29);
        n13.addNeighbours(n29);
        n28.addNeighbours(n29);
        n5.addNeighbours(n29);
        n215.addNeighbours(n29);

        // reverse
        n23.addNeighbours(n21);
        n24.addNeighbours(n23);
        n25.addNeighbours(n24);
        n26.addNeighbours(n24);
        n26.addNeighbours(n25);
        n211.addNeighbours(n25);
        n212.addNeighbours(n25);
        n29.addNeighbours(n26);
        n210.addNeighbours(n26);
        n211.addNeighbours(n26);
        n28.addNeighbours(n27);
        n29.addNeighbours(n27);
        n26.addNeighbours(n27);
        n210.addNeighbours(n27);
        n210.addNeighbours(n29);
        n211.addNeighbours(n210);
        n214.addNeighbours(n210);
        n215.addNeighbours(n210);
        n212.addNeighbours(n211);
        n213.addNeighbours(n211);
        n214.addNeighbours(n211);
        n215.addNeighbours(n211);
        n213.addNeighbours(n212);
        n214.addNeighbours(n213);
        n215.addNeighbours(n214);
        n216.addNeighbours(n215);
        // connect graphs
        n28.addNeighbours(n5);
        n28.addNeighbours(n12);
        n29.addNeighbours(n12);
        n29.addNeighbours(n13);
        n29.addNeighbours(n28);
        n29.addNeighbours(n5);
        n29.addNeighbours(n215);

        Node n31 = new Node("n31", 5.717, 0.8);
        Node n33 = new Node("n33", 4.925, 1.0);
        Node n34 = new Node("n34", 3.756, 0.84);
        Node n35 = new Node("n35", 4.291, 1.8);
        Node n36 = new Node("n36", 3.226, 1.8);
        Node n37 = new Node("n37", 2.3, 1.8);
        Node n38 = new Node("n38", 1.5, 1.8);
        Node n39 = new Node("n39", 2.0, 2.3);
        Node n310 = new Node("n310", 2.8, 2.3);
        Node n311 = new Node("n311", 3.699, 2.3);
        Node n312 = new Node("n312", 4.252, 2.2);
        Node n313 = new Node("n313", 4.314, 2.9);
        Node n314 = new Node("n314", 3.528, 2.9);
        Node n315 = new Node("n315", 2.5, 3.0);
        Node n316 = new Node("n316", 2.3, 3.580);
        n31.addNeighbours(n33);
        n33.addNeighbours(n34);
        n34.addNeighbours(n35);
        n34.addNeighbours(n36);
        n35.addNeighbours(n36);
        n35.addNeighbours(n311);
        n35.addNeighbours(n312);
        n36.addNeighbours(n39);
        n36.addNeighbours(n310);
        n36.addNeighbours(n311);
        n37.addNeighbours(n38);
        n37.addNeighbours(n39);
        n37.addNeighbours(n36);
        n37.addNeighbours(n310);
        n39.addNeighbours(n310);
        n310.addNeighbours(n311);
        n310.addNeighbours(n314);
        n310.addNeighbours(n315);
        n311.addNeighbours(n312);
        n311.addNeighbours(n313);
        n311.addNeighbours(n314);
        n311.addNeighbours(n315);
        n312.addNeighbours(n313);
        n313.addNeighbours(n314);
        n314.addNeighbours(n315);
        n315.addNeighbours(n316);
        // connect
        n34.addNeighbours(n214);
        n34.addNeighbours(n215);
        n34.addNeighbours(n216);
        n36.addNeighbours(n216);

        // reverse
        n33.addNeighbours(n31);
        n34.addNeighbours(n33);
        n35.addNeighbours(n34);
        n36.addNeighbours(n34);
        n36.addNeighbours(n35);
        n311.addNeighbours(n35);
        n312.addNeighbours(n35);
        n39.addNeighbours(n36);
        n310.addNeighbours(n36);
        n311.addNeighbours(n36);
        n38.addNeighbours(n37);
        n39.addNeighbours(n37);
        n36.addNeighbours(n37);
        n310.addNeighbours(n37);
        n310.addNeighbours(n39);
        n311.addNeighbours(n310);
        n314.addNeighbours(n310);
        n315.addNeighbours(n310);
        n312.addNeighbours(n311);
        n313.addNeighbours(n311);
        n314.addNeighbours(n311);
        n315.addNeighbours(n311);
        n313.addNeighbours(n312);
        n314.addNeighbours(n313);
        n315.addNeighbours(n314);
        n316.addNeighbours(n315);
        // connect
        n214.addNeighbours(n34);
        n215.addNeighbours(n34);
        n216.addNeighbours(n34);
        n216.addNeighbours(n36);


        ArrayList al = new ArrayList (Arrays.asList(n1, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16,
                n21, n23, n24, n25, n26, n27, n28, n29, n210, n211, n212, n213, n214, n215, n216,
                n31, n33, n34, n35, n36, n37, n38, n39, n310, n311, n312, n313, n314, n315, n316));
        graph.addAll(al);
    }
}
