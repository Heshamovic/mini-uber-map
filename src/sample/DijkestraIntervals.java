package sample;

import javafx.scene.control.ListView;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class DijkestraIntervals {
    public static class dijkestraIntervals{

        //Try largeinput
        private static double[][] inlines;
        private static Dictionary<Pair<Integer, Integer>, Integer> mp;
        public static FileReader FR;
        public static BufferedReader BR;
        private static int num_nodes, edges, query, speedCount, interval, newinterval;
        private static double totalwalk, totaldrive, intervalRange;
        private static Vector<DijkestraIntervals.Node> nodes;
        private static Vector<Integer> path;
        private static PriorityQueue<DijkestraIntervals.pnode> pq;
        private static int[][][] parent;
        private static double[][][] cost, costdis;
        private static double x1, y1, x2, y2, R, timecost = Double.MAX_VALUE;
        private static Vector<Integer> endNodes;
        //GUI
        public static ListView <String> LV = new ListView<>();
        public static ListView <String> timeL = new ListView<>();
        public static Vector <String> lines = new Vector<>();

        dijkestraIntervals(FileReader fr) // O(1)
        {
            FR = fr;
            mp = new Hashtable<>();
            BR = new BufferedReader(FR);
            totalwalk = 0;
            totaldrive = 0;
            nodes = new Vector<>();
            pq = new PriorityQueue<>();
            path = new Vector<>();
            endNodes = new Vector<>();
        }
        private static void initsizes(int siz)
        {
            nodes.setSize(siz);
            parent = new int[6200][62][62];
            cost = new double[6200][62][62];
            costdis = new double[6200][62][62];
        }
        private static void init() // O(1)
        {
            for (int i = 0; i < 6200; i++)
                for (int j = 0; j < 62; j++)
                    for (int k = 0; k < 62; k++)
                    {
                        parent[i][j][k] = -1;
                        cost[i][j][k] = costdis[i][j][k] = Double.MAX_VALUE / 100;
                    }
            pq.clear();
            totalwalk = totaldrive = 0;
            path.clear();
        }
        public static void getinputnode() throws Exception // O(V)
        {
            String s = BR.readLine();
            num_nodes = Integer.parseInt(s);
            initsizes(num_nodes + 3);
            String [] a;
            for (int i = 0; i < num_nodes; i++) // O(V)
            {
                DijkestraIntervals.Node newnode = new DijkestraIntervals.Node();
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
            String [] a;
            a = s.split(" ");
            edges = Integer.parseInt(a[0]);
            speedCount = Integer.parseInt(a[1]);
            intervalRange = Double.parseDouble(a[2]) / 60.0;
            int node1, node2;
            double len, vel, time;
            inlines = new double[edges + 1][speedCount + 1];
            for (int i = 0; i < edges; i++) //O(E)
            {
                s = BR.readLine();
                a = s.split(" ");
                node1 = Integer.parseInt(a[0]);
                node2 = Integer.parseInt(a[1]);
                len = Double.parseDouble(a[2]);
                nodes.elementAt(node1).child.add(new Edge(node2, len));
                nodes.elementAt(node2).child.add(new Edge(node1, len));
                mp.put(new Pair(node1, node2), i);
                mp.put(new Pair(node2, node1), i);
                for(int j = 0 ; j < speedCount ; j++)
                {
                    vel = Double.parseDouble(a[j + 3]);
                    time = len / vel;
                    inlines[i][j] = time;
                }
            }
        }
        //reads query and solves them
        public long solve(FileReader fr) throws Exception // O(Q(E log(V) + V log(V)))
        {
            FR = fr;
            BufferedReader BR = new BufferedReader(FR);
            String s = BR.readLine();
            String []a;
            query = Integer.parseInt(s);
            long Totaltime = 0;
            for (int i = 0; i < query; i++) // O(Q(E log(V) + V))
            {
                init(); // O(1)
                s = BR.readLine();
                a = s.split(" ");
                x1 = Double.parseDouble(a[0]);
                y1 = Double.parseDouble(a[1]);
                x2 = Double.parseDouble(a[2]);
                y2 = Double.parseDouble(a[3]);
                R = Double.parseDouble(a[4]);
                R /= 1000.0;
                long querytime = System.currentTimeMillis();
                beready(); // O(V log(V))
                run(); // O(E log(v))
                end(); // O(V)
                querytime = System.currentTimeMillis() - querytime;
                timeL.getItems().add(Long.toString(querytime));
                Totaltime += querytime;
            }
            return Totaltime;
        }
        //Get all nodes that the person can start the ride from
        private static void beready() // O(V)
        {
            endNodes = new Vector<>();
            for (int i = 0; i < num_nodes; i++) // O(V log(V))
            {
                double xnode = nodes.get(i).x;
                double ynode = nodes.get(i).y;
                double res = displacement(x1, y1, xnode, ynode);
                if (res <= R)
                {
                    double time = res / 5.0;
                    pnode hob = new pnode(time, res, i);
                    newinterval = (int)(time / intervalRange) % speedCount;
                    parent[i][0][newinterval] = -1;
                    cost[i][0][newinterval] = time;
                    costdis[i][0][newinterval] = res;
                    pq.add(hob);
                }
                res = displacement(x2, y2, xnode, ynode);
                if (res <= R)
                {
                    double time = res / 5.0;
                    endNodes.add(i);
                    nodes.get(i).child.add(new Edge(num_nodes, res));
                    nodes.get(i).child.lastElement().finalTime = time;
                    nodes.get(i).child.lastElement().isfinal = true;
                }
            }
        }
        //Dijkstra Algorithm
        private static void run() // O(E log(V))
        {
            while (!pq.isEmpty()) // O(E log(V))
            {
                int newnode = pq.peek().nod;
                double newnodecost = pq.peek().time, newnodedis = pq.peek().distance;
                pq.poll();
                interval = (int)(newnodecost / intervalRange);
                interval %= speedCount;
                if (newnode == num_nodes)
                {
                    timecost = Math.min(newnodecost * 60, timecost);
                    continue;
                }
                for (int i = 0; i < nodes.get(newnode).child.size(); i++)
                {
                    int id = nodes.get(newnode).child.get(i).id;
                    double time = nodes.get(newnode).child.get(i).finalTime, dist = nodes.get(newnode).child.get(i).distance;
                    if(!nodes.get(newnode).child.get(i).isfinal)
                        time = inlines[mp.get(new Pair(newnode, id))][interval];
                    newinterval = (int)((time + newnodecost) / intervalRange) % speedCount;
                    if(cost[id][interval][newinterval] > newnodecost + time ||
                            (cost[id][interval][newinterval] == newnodecost + time && costdis[id][interval][newinterval] > dist + newnodedis))
                    {
                        cost[id][interval][newinterval] = newnodecost + time;
                        costdis[id][interval][newinterval] = dist + newnodedis;
                        parent[id][interval][newinterval] = newnode;
                        pq.add(new pnode(cost[id][interval][newinterval], costdis[id][interval][newinterval], id));
                    }
                }
            }
        }
        //get all avaliable nodes that can reach the destination
        private static void end() // O(V)
        {
            int ind = num_nodes;
            String ret = new String();
            System.out.println(timecost);
            timecost = Double.MAX_VALUE;
            /*totaldrive = costdis[ind][interval];
            while (parent[ind][interval] != -1) // O(V)
            {
                path.add(ind);
                ind = parent[ind][interval];
            }
            path.add(ind);
            String Path = new String();
            for (int i = path.size() - 1; i > 0; i--) // O(V)
            {
                Path += path.elementAt(i);
                if(i != 1)
                    Path += " ";
            }
            ret += "path: " + Path + ", ";
            lines.add(Path);
            ret += String.format("%.2f", timecost) + " mins, ";
            lines.add(String.format("%.2f", timecost) + " mins");
            totalwalk += displacement(x1, y1, nodes.elementAt(path.lastElement()).x, nodes.elementAt(path.get(path.size()-1)).y);
            totalwalk += displacement(x2, y2, nodes.elementAt(path.get(1)).x, nodes.elementAt(path.get(1)).y);
            totaldrive -= totalwalk;
            ret += String.format("%.2f", totalwalk + totaldrive) + " km, ";
            ret += String.format("%.2f", totalwalk)+" km, ";
            ret += String.format("%.2f", totaldrive) +" km.";
            LV.getItems().add("Test #" + (LV.getItems().size() + 1) + ": " + ret);
            lines.add(String.format("%.2f", totalwalk + totaldrive) + " km");
            lines.add(String.format("%.2f", totalwalk)+" km");
            lines.add(String.format("%.2f", totaldrive) +" km");*/
            for (int i = 0; i < endNodes.size(); i++)
                nodes.get(endNodes.get(i)).child.removeElementAt(nodes.get(endNodes.get(i)).child.size() - 1);
        }
        //Calculate Ecludian Distance between two points
        private static double displacement(double x11, double y11, double x22, double y22) // O(1)
        {
            return Math.sqrt((y22 - y11) * (y22 - y11) +(x22 - x11) * (x22 - x11));
        }
    }

    //priority queue node that overrides the sort of the priority queue
    public static class pnode implements Comparable <DijkestraIntervals.pnode> // O(1)
    {
        public Double time, distance;
        public Integer nod;
        public pnode(Double f, Double x, Integer t)
        {
            time = f;
            distance = x;
            nod = t;
        }
        @Override
        public int compareTo(DijkestraIntervals.pnode other) {
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
        Vector<Edge>child;
        Node()
        {
            id = 0;
            x = 0.0;
            y = 0.0;
            child = new Vector<>();
        }
    }
    public static class Edge
    {
        int id;
        double distance, finalTime;
        Boolean isfinal;
        Edge(int i, double d)
        {
            id = i;
            distance = d;
            finalTime = 0.0;
            isfinal = false;
        }
    }
}
