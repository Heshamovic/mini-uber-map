package sample;

import javafx.scene.control.ListView;
import javafx.util.Pair;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Vector;

public class Dijkestra {

    public static class dijkstra
    {
        public static FileReader FR;
        public static BufferedReader BR;
        public static int num_nodes , edges , query ;
        private static double totalwalk , totaldrive ;
        public static Vector<Node> nodes;
        public static Vector<Integer> path;
        private static PriorityQueue<pnode> pq;
        private static int [] parent ;
        private static double [] cost, costdis ;
        private static double x1,y1,x2,y2,R;
        private static Vector<Integer> endNodes;
        public static Vector<Pair<Node, Node>>sordes;
        public static Vector<Vector<Integer>>pathes;
        dijkstra(FileReader fr) // O(1)
        {
            FR = fr;//O(1)
            BR = new BufferedReader(FR);//O(1)
            totalwalk = 0;//O(1)
            totaldrive = 0;//O(1)
            nodes = new Vector<>();//O(1)
            nodes.setSize(200005);//O(1)
            pq = new PriorityQueue<>();//O(1)
            parent = new int[200005];//O(1)
            cost = new double[200005];//O(1)
            costdis = new double[200005];//O(1)
            path = new Vector<>();//O(1)
            endNodes = new Vector<>();//O(1)
            sordes = new Vector<>();//O(1)
            pathes = new Vector<>();//O(1)
        }
        private static void init() // O(1)
        {
            Arrays.fill(parent, -1); // O(1)
            Arrays.fill(costdis, Double.MAX_VALUE / 10); // O(1)
            Arrays.fill(cost, Double.MAX_VALUE / 10); // O(1)
            pq.clear();//O(1)
            totalwalk = totaldrive = 0;//O(1)
            path.clear();//O(1)
        }
        public static void getinputnode() throws Exception // O(V|S|)
        {
            String s = BR.readLine();//O(1)
            num_nodes = Integer.parseInt(s);//O(|S|)
            String [] a = new String[3];//O(1)
            for (int i = 0; i < num_nodes; i++) // O(V)
            {
                Node newnode = new Node();//O(1)
                s = BR.readLine();//O(1)
                a = s.split(" ");//O(|S|)
                newnode.id = Integer.parseInt(a[0]);//O(|a[0]|)
                newnode.x = Double.parseDouble(a[1]);//O(|a[1]|)
                newnode.y = Double.parseDouble(a[2]);//O(|a[2]|)
                nodes.add(newnode.id, newnode);//O(1)
            }
        }
        public void getinputedges () throws Exception // O(E)
        {
            String s = BR.readLine();//O(1)
            edges = Integer.parseInt(s);//O(|S|)
            String [] a = new String[4];//O(1)
            int node1, node2;//O(1)
            double len, vel, time;//O(1)
            for (int i = 0; i < edges; i++) // O(E(|S|))
            {
                s = BR.readLine();//O(1)
                a = s.split(" ");//O(|S|)
                node1 = Integer.parseInt(a[0]);//O(|a[0]|)
                node2 = Integer.parseInt(a[1]);//O(|a[1]|)
                len = Double.parseDouble(a[2]);//O(|a[2]|)
                vel = Double.parseDouble(a[3]);//O(|a[3]|)
                time = len / vel;//O(1)
                Pair<Double,Double> pp = new Pair<>(time, len);//O(1)
                Pair<Integer, Pair<Double,Double>> chi = new Pair<>(node2, pp);//O(1)
                nodes.elementAt(node1).child.add(chi);//O(1)
                chi = new  Pair<>(node1, pp);//O(1)
                nodes.elementAt(node2).child.add(chi);//O(1)
            }
        }
        public static ListView<String> LV = new ListView<>();
        public static ListView<String> timeL = new ListView<>();
        public static Vector<String>lines = new Vector<>();
        // reads queray and solves them
        public long solve(FileReader fr) throws Exception // O(Q(E log(V) + V log(V)))
        {
            FR = fr;//O(1)
            BufferedReader BR = new BufferedReader(FR);//O(1)
            String s = BR.readLine();//O(1)
            String []a = new String[5];//O(1)
            query = Integer.parseInt(s);//O(|S|)
            long Totaltime = 0;//O(1)
            for (int i = 0; i < query; i++) // O(Q(E log(V) + V))
            {
                init(); // O(1)
                s = BR.readLine();//O(1)
                a = s.split(" ");//O(|S|)
                x1 = Double.parseDouble(a[0]);//O(|a[0]|)
                y1 = Double.parseDouble(a[1]);//O(|a[1]|)
                x2 = Double.parseDouble(a[2]);//O(|a[2]|)
                y2 = Double.parseDouble(a[3]);//O(|a[3]|)
                sordes.add(new Pair(new Node(x1, y1), new Node(x2, y2)));//O(1) // GUI
                R = Double.parseDouble(a[4]);//O(|a[4]|)
                R /= 1000.0;//O(1)
                long querytime = System.currentTimeMillis();//O(1)
                beready(); // O(V (log(V) + log(res)))
                run(); // O(E log(v))
                end(); // O(V)
                querytime = System.currentTimeMillis() - querytime;//O(1)
                timeL.getItems().add(Long.toString(querytime));//O(1) // GUI
                Totaltime += querytime;//O(1)
            }
            return Totaltime;//O(1)
        }
        //Calculate Ecludian Distance between two points
        private static double displacement(double x11, double y11, double x22, double y22) // O(log((y22 - y11) * (y22 - y11) +(x22 - x11) * (x22 - x11)))
        {
            return Math.sqrt((y22 - y11) * (y22 - y11) +(x22 - x11) * (x22 - x11));
        }
        //Get all nodes that the person can start the ride from
        private static void beready() // O(V (log(V) + log(res)))
        {
            endNodes = new Vector<>();//O(1)
            for (int i = 0; i < num_nodes; i++) // O(V (log(V) + log(res)))
            {
                double xnode = nodes.get(i).x;//O(1)
                double ynode = nodes.get(i).y;//O(1)
                double res = displacement(x1, y1, xnode, ynode);//O(log(res))
                if (res <= R)//O(1)
                {
                    double time = res / 5.0;//O(1)
                    pnode hob = new pnode(time, res, i);//O(1)
                    parent[i] = -1;//O(1)
                    costdis[i] = res;//O(1)
                    cost[i] = res / 5.0;//O(1)
                    pq.add(hob);//O(log(V))
                }
                res = displacement(x2, y2, xnode, ynode);//O(log(res))
                if (res <= R)//O(1)
                {
                    double time = res / 5.0;//O(1)
                    endNodes.add(nodes.get(i).id);//O(1)
                    nodes.get(i).child.add(new Pair(num_nodes, new Pair(time, res)));//O(1)
                }
            }
        }
        //Dijkstra Algorithm
        private static void run() // O(E log(V))
        {
            while (!pq.isEmpty()) // O(E log(V))
            {
                int newnode = pq.peek().nod; // O(1)
                double newnodecost = pq.peek().time; // O(1)
                double newnodedis = pq.peek().distance; // O(1)
                pq.poll(); // O(log(V))
                if (newnode == num_nodes) // O(1)
                    break; // O(1)
                for (int i = 0; i < nodes.elementAt(newnode).child.size(); i++) // O(E log(V))
                {
                    int nei = nodes.elementAt(newnode).child.elementAt(i).getKey(); // O(1)
                    double neicost = nodes.elementAt(newnode).child.elementAt(i).getValue().getKey(); // O(1)
                    double neidest = nodes.elementAt(newnode).child.elementAt(i).getValue().getValue(); // O(1)
                    if ((neicost + newnodecost < cost[nei]) ||
                            (neicost + newnodecost == cost[nei] && neidest + newnodedis < costdis[nei])) // O(1)
                    {
                        cost[nei] = neicost + newnodecost; // O(1)
                        costdis[nei] = neidest + newnodedis; // O(1)
                        pnode NEW = new pnode(cost[nei], costdis[nei], nei); // O(1)
                        parent[nei] = newnode; // O(1)
                        pq.add(NEW); // O(log(V))
                    }
                }
            }
        }

        //get all avaliable nodes that can reach the destination
        private static void end() // O(V)
        {
            int ind = num_nodes; // O(1)
            double timecost = cost[num_nodes] * 60; // O(1)
            String ret = new String(); // O(1)
            totaldrive = costdis[ind]; // O(1)
            while (parent[ind] != -1) // O(V)
            {
                path.add(ind); // O(1)
                ind = parent[ind]; // O(1)
            }
            path.add(ind); // O(1)
            pathes.add(new Vector<>()); // O(1) // GUI
            for(int i = 0 ; i < path.size();i++) // O(V) // GUI
                pathes.lastElement().add(path.get(i)); // O(V) //GUI
            String Path = new String(); // O(1)
            for (int i = path.size() - 1; i > 0; i--) // O(V)
            {
                Path += path.elementAt(i); // O(1)
                if(i != 1) // O(1)
                    Path += " "; // O(1)
            }
            ret += "path: " + Path + "."; // O(1)
            lines.add(Path); // GUI
            LV.getItems().add("Test #" + (LV.getItems().size() + 1) + ": " + ret); // GUI
            ret = String.format("%.2f", timecost) + " mins, "; // O(1)
            lines.add(String.format("%.2f", timecost) + " mins"); // O(1)
            totalwalk += displacement(x1, y1, nodes.elementAt(path.lastElement()).x,nodes.elementAt(path.get(path.size()-1)).y);// O(log(Distance)
            totalwalk += displacement(x2,y2,nodes.elementAt(path.get(1)).x,nodes.elementAt(path.get(1)).y);// O(log(Distance)
            totaldrive -= totalwalk;// O(1)
            ret += String.format("%.2f", totalwalk + totaldrive) + " km, ";// O(1)
            ret += String.format("%.2f", totalwalk)+" km, ";// O(1)
            ret += String.format("%.2f", totaldrive) +" km, ";// O(1)
            lines.add(String.format("%.2f", totalwalk + totaldrive) + " km");// O(1)
            lines.add(String.format("%.2f", totalwalk)+" km");// O(1)
            lines.add(String.format("%.2f", totaldrive) +" km");// O(1)
            for (int i = 0; i < endNodes.size(); i++)// O(V)
                nodes.elementAt(endNodes.elementAt(i)).child.removeElementAt(nodes.elementAt(endNodes.elementAt(i)).child.size() - 1);// O(1)
        }
    }
    //priority queue node that overrides the sort of the priority queue
    public static class pnode implements Comparable <pnode> // O(1)
    {
        public Double time, distance;// O(1)
        public Integer nod;// O(1)
        public pnode(Double f, Double x, Integer t)// O(1)
        {
            this.time = f;// O(1)
            this.distance = x;// O(1)
            this.nod = t;// O(1)
        }
        @Override
        public int compareTo(pnode other) {// O(1)
            if (this.time.compareTo(other.time) == 0)// O(1)
                return this.distance.compareTo(other.distance);// O(1)
            return this.time.compareTo(other.time);// O(1)
        }
    }
    //Node class(all the data related to node)
    public static class Node // O(1)
    {
        int id ;// O(1)
        public double x, y;// O(1)
        Vector<Pair<Integer, Pair<Double,Double>>>child;// O(1)
        Node()
        {
            id = 0;// O(1)
            x = 0.0;// O(1)
            y = 0.0;// O(1)
            child = new Vector<>();// O(1)
        }
        Node(double xx, double yy)// O(1)
        {
            x = xx;// O(1)
            y = yy;// O(1)
        }
    }

}