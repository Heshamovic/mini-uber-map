package sample;

import javafx.scene.control.ListView;
import javafx.util.Pair;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.stream.StreamSupport;

public class DijkestraIntervals {
    public static class dijkestraIntervals{

        public static FileReader FR;
        public static BufferedReader BR;
        private static int num_nodes, edges, query, speedCount, interval;
        private static double totalwalk, totaldrive, intervalRange;
        private static Vector<DijkestraIntervals.Node> nodes;
        private static Vector<Integer> path;
        private static PriorityQueue<DijkestraIntervals.pnode> pq;
        private static Pair<Integer, Integer>[][] parent;
        private static double[][] cost, costdis;
        private static double x1,y1,x2,y2,R;
        private static Vector<Integer> endNodes;
        private static boolean vis[];
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
            nodes.setSize(200005);
            pq = new PriorityQueue<>();
            parent = new Pair[200005][201];
            cost = new double[200005][201];
            costdis = new double[200005][201];
            path = new Vector<>();
            endNodes = new Vector<>();
        }
        private static void init() // O(1)
        {
            for (Pair<Integer, Integer>[] row: parent)
                Arrays.fill(row, new Pair<>(-1, -1));
            for (double[] row: costdis)
                Arrays.fill(row, Double.MAX_VALUE / 10);
            for (double[] row: cost)
                Arrays.fill(row, Double.MAX_VALUE / 10);
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
            String [] a = new String[4];
            a = s.split(" ");
            edges = Integer.parseInt(a[0]);
            speedCount = Integer.parseInt(a[1]);
            intervalRange = Double.parseDouble(a[2]) / 60;
            String[] tmp = new String[speedCount + 5];
            int node1, node2;
            double len, vel, time;
            for (int i = 0; i < edges; i++) //O(E)
            {
                s = BR.readLine();
                tmp = s.split(" ");
                node1 = Integer.parseInt(tmp[0]);
                node2 = Integer.parseInt(tmp[1]);
                len = Double.parseDouble(tmp[2]);

                nodes.elementAt(node1).child.add(new Edge(node2, len));
                nodes.elementAt(node2).child.add(new Edge(node1, len));
                for(int j = 0 ; j < speedCount ; j++)
                {
                    vel = Double.parseDouble(tmp[j + 3]);
                    time = len / vel;
                    nodes.elementAt(node1).child.lastElement().intervals.add(time);
                    nodes.elementAt(node2).child.lastElement().intervals.add(time);
                }
            }
        }
        //reads queray and solves them
        public long solve(FileReader fr) throws Exception // O(Q(E log(V) + V log(V)))
        {
            FR = fr;
            BufferedReader BR = new BufferedReader(FR);
            String s = BR.readLine();
            String []a = new String[5];
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
        //Calculate Ecludian Distance between two points
        private static double displacement(double x11, double y11, double x22, double y22) // O(1)
        {
            return Math.sqrt((y22 - y11) * (y22 - y11) +(x22 - x11) * (x22 - x11));
        }

        //private int getIndex(double time)
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
                    interval = (int)(time / (double)intervalRange);
                    interval %= speedCount;
                    DijkestraIntervals.pnode hob = new DijkestraIntervals.pnode(time, res, i, 0);
                    parent[i][interval] = new Pair(-1, 0);
                    costdis[i][interval] = res;
                    cost[i][interval] = res / 5.0;
                    pq.add(hob);
                }
                res = displacement(x2, y2, xnode, ynode);
                if (res <= R)
                {
                    double time = res / 5.0;
                    endNodes.add(nodes.get(i).id);
                    nodes.get(i).child.add(new Edge(num_nodes, res));
                    nodes.get(i).child.lastElement().intervals.add(-1 * time);
                }
            }
        }
        //Dijkstra Algorithm
        private static void run() // O(E log(V))
        {
            while (!pq.isEmpty()) // O(E log(V))
            {
                int newnode = pq.peek().nod;
                int parentinterval = pq.peek().interval;
                double newnodecost = pq.peek().time;
                double newnodedis = pq.peek().distance;
                pq.poll();
                interval = (int)(newnodecost / intervalRange);
                interval %= speedCount;
                if (newnode == num_nodes)
                {
                    interval = parentinterval;
                    break;
                }
                for (int i = 0; i < nodes.get(newnode).child.size(); i++) // O(E log(V))
                {
                    double time = nodes.get(newnode).child.get(i).intervals.lastElement(), dist = nodes.get(newnode).child.get(i).distance;
                    int id = nodes.get(newnode).child.get(i).id;
                    if(time < 0)
                    {
                        if(cost[id][interval] > newnodecost - time || (cost[id][interval] == newnodecost - time && costdis[id][interval] > dist + newnodedis))
                        {
                            cost[id][interval] = newnodecost - time;
                            costdis[id][interval] = dist + newnodedis;
                            parent[id][interval] = new Pair(newnode, parentinterval);
                            pq.add(new pnode(cost[id][interval], dist + newnodedis, id, interval));
                        }
                    }
                    else
                    {
                        time = nodes.get(newnode).child.get(i).intervals.get(interval);
                        if(cost[id][interval] > newnodecost + time || (cost[id][interval] == newnodecost + time && costdis[id][interval] > dist))
                        {
                            cost[id][interval] = newnodecost + time;
                            costdis[id][interval] = dist + newnodedis;
                            parent[id][interval] = new Pair(newnode, parentinterval);
                            pq.add(new pnode(cost[id][interval], dist + newnodedis, id, interval));
                        }
                    }
                }
            }
        }

        //get all avaliable nodes that can reach the destination
        private static void end() // O(V)
        {
            int ind = num_nodes;
            double timecost = cost[num_nodes][interval] * 60;
            System.out.println(timecost);
            String ret = new String();
            totaldrive = costdis[ind][interval];
            while (parent[ind][interval].getKey() != -1) // O(V)
            {
                System.out.println(ind);
                path.add(ind);
                int x = ind;
                ind = parent[x][interval].getKey();
                interval = parent[x][interval].getValue();
            }
            path.add(ind);
            String Path = new String();
            for (int i = path.size() - 1; i > 0; i--) // O(V)
            {
                Path += path.elementAt(i);
                if(i != 1) {
                    Path += " ";
                }
            }
            ret += "path: " + Path + ", ";
            lines.add(Path);
            ret += String.format("%.2f", timecost) + " mins, ";
            lines.add(String.format("%.2f", timecost) + " mins");
            totalwalk += displacement(x1, y1, nodes.elementAt(path.lastElement()).x,nodes.elementAt(path.get(path.size()-1)).y);
            totalwalk += displacement(x2,y2,nodes.elementAt(path.get(1)).x,nodes.elementAt(path.get(1)).y);
            totaldrive -= totalwalk;
            ret += String.format("%.2f", totalwalk + totaldrive) + " km, ";
            ret += String.format("%.2f", totalwalk)+" km, ";
            ret += String.format("%.2f", totaldrive) +" km.";
            LV.getItems().add("Test #" + (LV.getItems().size() + 1) + ": " + ret);
            lines.add(String.format("%.2f", totalwalk + totaldrive) + " km");
            lines.add(String.format("%.2f", totalwalk)+" km");
            lines.add(String.format("%.2f", totaldrive) +" km");
            for (int i = 0; i < endNodes.size(); i++)
                nodes.elementAt(endNodes.elementAt(i)).child.removeElementAt(nodes.elementAt(endNodes.elementAt(i)).child.size() - 1);
        }
    }

    //priority queue node that overrides the sort of the priority queue
    public static class pnode implements Comparable <DijkestraIntervals.pnode> // O(1)
    {
        public Double time, distance;
        public Integer nod, interval;
        public pnode(Double f, Double x, Integer t, Integer inter)
        {
            time = f;
            distance = x;
            nod = t;
            interval = inter;
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
        Vector<Double>intervals;
        Edge(int i, double d)
        {
            id = i;
            distance = d;
            intervals = new Vector<>();
        }
    }
}
