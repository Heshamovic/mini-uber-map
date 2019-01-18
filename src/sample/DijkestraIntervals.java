package sample;

import javafx.scene.control.ListView;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class DijkestraIntervals {
    public static class dijkestraIntervals{

        public static FileReader FR;
        public static BufferedReader BR;
        private static int num_nodes, edges, query, speedCount, interval, curNode;
        private static double totalwalk, totaldrive, intervalRange;
        private static Vector<Node> nodes;
        private static Vector<Integer> path;
        private static PriorityQueue<pnode> pq;
        private static Map<Pair<Integer, Integer>, Integer> parent;
        private static Map<Pair<Integer, Integer>, Double> cost, costdis;
        private static double x1, y1, x2, y2, R, timecost = Double.MAX_VALUE;
        private static Vector<Integer> endNodes;
        //GUI
        public static ListView <String> LV = new ListView<>();
        public static ListView <String> timeL = new ListView<>();
        public static Vector <String> lines = new Vector<>();

        dijkestraIntervals(FileReader fr) // O(1)
        {
            FR = fr;
            BR = new BufferedReader(FR);
            totalwalk = 0;
            totaldrive = 0;
            nodes = new Vector<>();
            pq = new PriorityQueue<>();
            path = new Vector<>();
            endNodes = new Vector<>();
        }
        private static void init() // O(1)
        {
            pq.clear();
            totalwalk = totaldrive = 0;
            path.clear();
            parent = new HashMap<>();
            cost = new HashMap<>();
            costdis = new HashMap<>();
        }
        public static void getinputnode() throws Exception // O(V)
        {
            String s = BR.readLine();
            num_nodes = Integer.parseInt(s);
            nodes.setSize(num_nodes + 2);
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
            for (int i = 0; i < edges; i++) //O(E)
            {
                s = BR.readLine();
                a = s.split(" ");
                node1 = Integer.parseInt(a[0]);
                node2 = Integer.parseInt(a[1]);
                len = Double.parseDouble(a[2]);
                nodes.elementAt(node1).child.add(new Edge(node2, len));
                nodes.elementAt(node2).child.add(new Edge(node1, len));
                for(int j = 0 ; j < speedCount ; j++)
                {
                    vel = Double.parseDouble(a[j + 3]);
                    time = len / vel;
                    nodes.elementAt(node1).child.lastElement().intervals.add(time);
                    nodes.elementAt(node2).child.lastElement().intervals.add(time);
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
                    pnode hob = new pnode(time, res, i, -1);
                    Pair<Integer, Integer> ck = new Pair(-1, i);
                    parent.put(ck, -1);
                    costdis.put(ck, res);
                    cost.put(ck, time);
                    pq.add(hob);
                }
                res = displacement(x2, y2, xnode, ynode);
                if (res <= R)
                {
                    double time = res / 5.0;
                    endNodes.add(i);
                    nodes.get(i).child.add(new Edge(num_nodes, res));
                    nodes.get(i).child.lastElement().intervals.add(time);
                    nodes.get(i).child.lastElement().isfinal = true;
                }
            }
        }
        //Dijkstra Algorithm
        private static void run() // O(E log(V))
        {
            double curtime, curdis;
            while (!pq.isEmpty()) // O(E log(V))
            {
                int curparent = pq.peek().parent;
                curNode = pq.peek().nod;
                curdis = pq.peek().distance;
                curtime = pq.peek().time;
                pq.poll();
                if (curNode == num_nodes)
                {
                    timecost = curtime;
                    curNode = curparent;
                    break;
                }
                interval = (int)(curtime / intervalRange) % speedCount;
                for (int i = 0; i < nodes.get(curNode).child.size(); i++)
                {
                    int id = nodes.get(curNode).child.get(i).id;
                    double time = curtime + nodes.get(curNode).child.get(i).intervals.lastElement();
                    double dis = curdis + nodes.get(curNode).child.get(i).distance;
                    Pair<Integer, Integer> ck = new Pair(curNode, id);
                    if (!nodes.get(curNode).child.get(i).isfinal)
                        time = curtime + nodes.get(curNode).child.get(i).intervals.get(interval);
                    if (!cost.containsKey(ck) || cost.get(ck) > time || (cost.get(ck) == time && costdis.get(ck) > dis))
                    {
                        if (!cost.containsKey(ck))
                        {
                            cost.put(ck, time);
                            costdis.put(ck, dis);
                            parent.put(ck, curparent);
                        }
                        else
                        {
                            cost.replace(ck, time);
                            costdis.replace(ck, dis);
                            parent.replace(ck, curparent);
                        }
                        pq.add(new pnode(time, dis, id, curNode));
                    }
                }
            }
        }
        //get all avaliable nodes that can reach the destination
        private static void end() // O(V)
        {
            int ind1 = num_nodes, ind = curNode;
            String ret = new String();
            totaldrive = costdis.get(new Pair(ind, ind1));
            path.add(ind1);
            while (ind != -1 && parent.containsKey(new Pair(ind, ind1))) // O(V)
            {
                path.add(ind);
                int x = ind;
                ind = parent.get(new Pair(x, ind1));
                ind1 = x;
            }
            String Path = new String();
            for (int i = path.size() - 1; i > 0; i--) // O(V)
            {
                Path += path.elementAt(i);
                if(i != 1)
                {
                    Path += " ";
                }
            }
            timecost *= 60;
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
            lines.add(String.format("%.2f", totaldrive) +" km");
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
        public Integer nod, parent;
        public pnode(Double f, Double x, Integer t, Integer p)
        {
            time = f;
            distance = x;
            nod = t;
            parent = p;
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
        double distance;
        Boolean isfinal;
        Vector<Double> intervals;
        Edge(int i, double d)
        {
            id = i;
            distance = d;
            isfinal = false;
            intervals = new Vector<>();
        }
    }
}