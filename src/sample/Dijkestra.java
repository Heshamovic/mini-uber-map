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
        private static int num_nodes , edges , query ;
        private static double totalwalk , totaldrive ;
        private static Vector<Node> nodes;
        private static Vector<Integer> path;
        private static PriorityQueue<pnode> pq;
        private static int [] parent ;
        private static double [] cost, costdis ;
        private static double x1,y1,x2,y2,R;
        dijkstra(FileReader fr) // O(1)
        {
            FR = fr;
            BR = new BufferedReader(FR);
            totalwalk = 0;
            totaldrive = 0;
            nodes = new Vector<Node>();
            nodes.setSize(200005);
            pq = new PriorityQueue<pnode>();
            parent = new int[200005];
            cost = new double[200005];
            costdis = new double[200005];
            path = new Vector<Integer>();
        }
        private static void init() // O(1)
        {
            Arrays.fill(parent, -1); // O(1)
            Arrays.fill(costdis, Double.MAX_VALUE / 10); // O(1)
            Arrays.fill(cost, Double.MAX_VALUE / 10); // O(1)
            pq.clear();
            totalwalk = totaldrive = 0;
            path.clear();
        }
        public static void getinputnode() throws Exception // O(V)
        {
            String s = BR.readLine();
            num_nodes = Integer.parseInt(s);
            String [] a = new String[3];
            for (int i = 0; i < num_nodes; i++) // O(V)
            {
                Node newnode = new Node();
                s = BR.readLine();
                a = s.split(" ");
                newnode.id = Integer.parseInt(a[0]);
                newnode.x = Double.parseDouble(a[1]);
                newnode.y = Double.parseDouble(a[2]);
                nodes.add(newnode.id, newnode);
            }
        }
        public void getinputedges () throws Exception // O(E)
        {
            String s = BR.readLine();
            edges = Integer.parseInt(s);
            String [] a = new String[4];
            int node1, node2;
            double len, vel, time;
            for (int i = 0; i < edges; i++) // O(E)
            {
                s = BR.readLine();
                a = s.split(" ");
                node1 = Integer.parseInt(a[0]);
                node2 = Integer.parseInt(a[1]);
                len = Double.parseDouble(a[2]);
                vel = Double.parseDouble(a[3]);
                time = len / vel;
                Pair<Double,Double> pp = new Pair<Double,Double>(time, len);
                Pair<Integer, Pair<Double,Double>> chi = new Pair<Integer, Pair<Double,Double>>(node2, pp);
                nodes.elementAt(node1).child.add(chi);
                chi = new  Pair<Integer, Pair<Double,Double>>(node1, pp);
                nodes.elementAt(node2).child.add(chi);
            }
        }
        public static ListView<String> LV = new ListView<String>();
        public static ListView<String> timeL = new ListView<String>();
        public static Vector<String>lines = new Vector<String>();
        // reads queray and solves them
        public  long solve(FileReader fr) throws Exception // O(Q(E log(V) + V log(V)))
        {
            FR = fr;
            BufferedReader BR = new BufferedReader(FR);
            String s = BR.readLine();
            String []a = new String[5];
            query = Integer.parseInt(s);
            long Totaltime = 0;
            for (int i=0;i<query; i++) // O(Q(E log(V) + V))
            {
                long querytime = System.currentTimeMillis();
                init(); // O(1)
                s = BR.readLine();
                a = s.split(" ");
                x1 = Double.parseDouble(a[0]);
                y1 = Double.parseDouble(a[1]);
                x2 = Double.parseDouble(a[2]);
                y2 = Double.parseDouble(a[3]);
                R = Double.parseDouble(a[4]);
                R /= 1000.0;
                beready(); // O(V log(V))
                run(); // O(E log(v))
                end(); // O(V)
                querytime = System.currentTimeMillis() - querytime;
                timeL.getItems().add(Long.toString(querytime));
                Totaltime += querytime;
            }
            return Totaltime;
        }
        //Calculate Ecludian Distance between two points
        private static double displacement(double x11, double y11, double x22, double y22) // O(1)
        {
            return Math.sqrt((y22 - y11) * (y22 - y11) +(x22 - x11) * (x22 - x11));
        }
        //Get all nodes that the person can start the ride from
        private static void beready() // O(V)
        {
            for (int i = 0; i < num_nodes; i++) // O(V log(V))
            {
                double xnode = nodes.get(i).x;
                double ynode = nodes.get(i).y;
                double res = displacement(x1, y1, xnode, ynode);
                if (res <= R)
                {
                    double time = res/5.0;
                    pnode hob = new pnode(time, res, i);
                    parent[i] = -1;
                    costdis[i] = res;
                    cost[i] = res / 5.0;
                    pq.add(hob);
                }
            }
        }
        //Dijkstra Algorithm
        private static void run() // O(E log(V))
        {
            while (!pq.isEmpty()) // O(E log(V))
            {
                int newnode = pq.peek().nod;
                double newnodecost = pq.peek().time;
                double newnodedis = pq.peek().distance;
                pq.poll();
                for (int i = 0; i < nodes.elementAt(newnode).child.size(); i++) // O(E log(V))
                {
                    int nei = nodes.elementAt(newnode).child.elementAt(i).getKey();
                    double neicost = nodes.elementAt(newnode).child.elementAt(i).getValue().getKey();
                    double neidest = nodes.elementAt(newnode).child.elementAt(i).getValue().getValue();
                    if ((neicost + newnodecost < cost[nei]) ||
                            (neicost + newnodecost == cost[nei] && neidest + newnodedis < costdis[nei]))
                    {
                        cost[nei] = neicost + newnodecost;
                        costdis[nei] = neidest + newnodedis;
                        pnode NEW = new pnode(cost[nei], costdis[nei], nei);
                        parent[nei] = newnode;
                        pq.add(NEW);
                    }
                }
            }
        }
        //get all avaliable nodes that can reach the destination
        private static void end() // O(V)
        {
            int ind = 0;
            double mn = 1000000005;
            for (int i = 0; i < num_nodes; i++) // O(V)
            {
                double xnode = nodes.get(i).x;
                double ynode = nodes.get(i).y;
                double res = displacement(x2, y2, xnode, ynode);
                if (res <= R)
                {
                    double time = res/5.0;
                    if (time + cost[i] < mn)
                    {
                        ind=i;
                        mn=time+cost[i];
                    }
                }
            }
            double timecost = mn * 60;
            String ret = new String();
            ret += String.format("%.2f", timecost) + " mins, ";
            lines.add(String.format("%.2f", timecost) + " mins");
            totaldrive = costdis[ind];
            while (parent[ind] != -1) // O(V)
            {
                path.add(ind);
                ind = parent[ind];
            }
            path.add(ind);
            totalwalk += displacement(x1, y1, nodes.elementAt(path.lastElement()).x,nodes.elementAt(path.get(path.size()-1)).y);
            totaldrive -= totalwalk;
            totalwalk+=displacement(x2,y2,nodes.elementAt(path.get(0)).x,nodes.elementAt(path.get(0)).y);
            ret += String.format("%.2f", totalwalk + totaldrive) + " km, ";
            ret += String.format("%.2f", totalwalk)+" km, ";
            ret += String.format("%.2f", totaldrive) +" km, ";
            lines.add(String.format("%.2f", totalwalk + totaldrive) + " km");
            lines.add(String.format("%.2f", totalwalk)+" km");
            lines.add(String.format("%.2f", totaldrive) +" km");
            String Path = new String();
            for (int i = path.size()-1;i>=0;i--) // O(V)
            {
                Path += path.elementAt(i);
                if(i != 0)
                    Path += " ";
            }
            ret += "path: " + Path + ".";
            lines.add(Path);
            LV.getItems().add("Test #" + (LV.getItems().size() + 1) + ": " + ret);
        }
    }
    //priority queue node that overrides the sort of the priority queue
    public static class pnode implements Comparable <pnode> // O(1)
    {
        public Double time, distance;
        public Integer nod;
        public pnode(Double f, Double x, Integer t)
        {
            this.time = f;
            this.distance = x;
            this.nod = t;
        }
        @Override
        public int compareTo(pnode other) {
            if (this.time.compareTo(other.time) == 0)
                return this.distance.compareTo(other.distance);
            return this.time.compareTo(other.time);
        }
    }
    //Node class(all the data related to node)
    public static class Node // O(1)
    {
        int id ;
        double x, y;
        Vector<Pair<Integer, Pair<Double,Double>>>child;
        Node()
        {
            id = 0;
            x = 0.0;
            y = 0.0;
            child = new Vector<Pair<Integer,Pair<Double,Double>>>();
        }
    }

}