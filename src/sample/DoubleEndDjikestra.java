package sample;

import javafx.scene.control.ListView;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Vector;

public class DoubleEndDjikestra {
    private static Integer numberOfNodes, numberOfEdges, numberOfQueries;
    private static Vector<node> nodes;
    private static Vector<Vector<enode>> edges;
    public static Vector<String>lines = new Vector<>();
    public static ListView<String> LV = new ListView<>();
    public static ListView<String> timeL = new ListView<>();
    DoubleEndDjikestra() // O(1)
    {
        nodes = new Vector<node>();
        edges = new Vector<Vector<enode>>();
        numberOfEdges = numberOfNodes = numberOfQueries = 0;
    }
    //priority_queue node (overrides on the sort of the priority queue)
    public static class pnode implements Comparable<pnode> // O(1)
    {
        public Double time, distance;
        public Integer parent, nod;
        public pnode(Double f, Double x, Integer s, Integer t)
        {
            this.time = f;
            this.distance = x;
            this.parent = s;
            this.nod = t;
        }
        @Override
        public int compareTo(pnode other) {
            if (this.time.compareTo(other.time) == 0)
                return this.distance.compareTo(other.distance);
            return this.time.compareTo(other.time);
        }
    }
    //edge node (stores each of the edge's id, velocity, and distance)
    public static class enode // O(1)
    {
        public double velocity, distance;
        public Integer id;
        public enode(Integer i, double d, double v)
        {
            this.velocity = v;
            this.distance = d;
            this.id = i;
        }
    }
    //node (stores each of the node's id, X-coordinate, Y-coordinate)
    public static class node // O(1)
    {
        int ID;
        double X;
        double Y;
        node(int id, double x, double y)
        {
            this.ID = id;
            this.X = x;
            this.Y = y;
        }
        node()
        {}
    }
    //Query class that saves the query answers
    public static class QueryAns // O(1)
    {
        public Integer intersectionNode;
        public Double shortestTime;
        public Double totalDist;
        public Double totalWalkingDest;
        public Double totalVehicleDest;
        public Vector<Integer> path;
        QueryAns()
        {
            intersectionNode = 0;
            shortestTime = Double.MAX_VALUE;
            totalDist = Double.MAX_VALUE;
            totalWalkingDest = 0.0;
            totalVehicleDest = 0.0;
            path = new Vector<Integer>();
        }
    }
    //Calculating Ecludian distance between 2 points
    public static double calcdis(Double x1, Double y1, Double x2, Double y2) // O(1)
    {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    //Calculating Ecludian distance between 2 points squared
    public static double calcdis2(Double x1, Double y1, Double x2, Double y2) // O(1)
    {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }
    //Building the graph (Nodes and Edges)
    public static void build(FileReader FR) throws Exception // O(E + V)
    {
        BufferedReader BR = new BufferedReader(FR);
        String s = BR.readLine();
        String a[] = new String[3];
        numberOfNodes = Integer.parseInt(s);
        for (int i = 0; i < numberOfNodes; i++) // O(V)
        {
            s = BR.readLine();
            a = s.split(" ");
            node n = new node(Integer.parseInt(a[0]), Double.parseDouble(a[1]), Double.parseDouble(a[2]));
            nodes.add(n);
        }
        s = BR.readLine();
        numberOfEdges = Integer.parseInt(s);
        a = new String[4];
        edges.setSize(numberOfNodes + 1); // O(E)
        for (int i = 0; i < numberOfNodes; i++) // O(V)
            edges.add(i, new Vector<enode>());
        enode n1;
        for (int i = 0; i < numberOfEdges; i++) // O(E)
        {
            s = BR.readLine();
            a = s.split(" ");
            n1 = new enode(Integer.parseInt(a[1]), Double.parseDouble(a[2]), Double.parseDouble(a[3]));
            edges.elementAt(Integer.parseInt(a[0])).add(n1);
            n1 = new enode(Integer.parseInt(a[0]), Double.parseDouble(a[2]), Double.parseDouble(a[3]));
            edges.elementAt(Integer.parseInt(a[1])).add(n1);
        }
    }
    //Double compare
    public static boolean DCMP(double a, double b) // O(1)
    {
        if(Math.abs(a - b) <= 1e-6)
            return true;
        return false;
    }
    //Bi-directional Dijkstra
    public static String DEDIJ(Double radius, Double xSrc, Double ySrc, Double xDest, Double yDest) // O(Elog(V))
    {
        String ret;
        PriorityQueue<pnode> pqf = new PriorityQueue<>();
        PriorityQueue<pnode> pqb = new PriorityQueue<>();
        Integer parentf[] = new Integer[numberOfNodes + 1];
        Integer parentb[] = new Integer[numberOfNodes + 1];
        Boolean visf[] = new Boolean[numberOfNodes + 1];
        Boolean visb[] = new Boolean[numberOfNodes + 1];
        double costf[] = new double[numberOfNodes + 1];
        double costb[] = new double[numberOfNodes + 1];
        double costfd[] = new double[numberOfNodes + 1];
        double costbd[] = new double[numberOfNodes + 1];
        Arrays.fill(parentf, -1); // O(V)
        Arrays.fill(parentb, -1); // O(V)
        Arrays.fill(visf, false); // O(V)
        Arrays.fill(visb, false); // O(V)
        Arrays.fill(costf, Double.MAX_VALUE / 10.0); // O(V)
        Arrays.fill(costb, Double.MAX_VALUE / 10.0); // O(V)
        Arrays.fill(costfd, Double.MAX_VALUE / 10.0); // O(V)
        Arrays.fill(costbd, Double.MAX_VALUE / 10.0); // O(V)
        pnode fnode, bnode;
        double dis1, dis2, mue = Double.MAX_VALUE;
        for (int i = 0; i < nodes.size(); i++) // O(V log(V))
        {
            dis1 = calcdis2(nodes.elementAt(i).X, nodes.elementAt(i).Y, xSrc, ySrc);
            if (dis1 <= radius * radius)
            {
                dis1 = calcdis(nodes.elementAt(i).X, nodes.elementAt(i).Y, xSrc, ySrc);
                pqf.add(new pnode(dis1 / 5.0, dis1, -1, nodes.elementAt(i).ID)); // O(Log(V))
                costf[nodes.elementAt(i).ID] = dis1 / 5.0;
                costfd[nodes.elementAt(i).ID] = dis1;
            }
            dis2 = calcdis2(nodes.elementAt(i).X, nodes.elementAt(i).Y, xDest, yDest);
            if (dis2 <= radius * radius)
            {
                dis2 = calcdis(nodes.elementAt(i).X, nodes.elementAt(i).Y, xDest, yDest);
                pqb.add(new pnode(dis2 / 5.0, dis2, -1, nodes.elementAt(i).ID)); // O(log(V))
                costb[nodes.elementAt(i).ID] = dis2 / 5.0;
                costbd[nodes.elementAt(i).ID] = dis2;
            }
        }
        while (!pqb.isEmpty() && !pqf.isEmpty()) // O(E log(V))
        {
            fnode = pqf.peek();
            pqf.poll(); // O(log(V))
            bnode = pqb.peek();
            pqb.poll(); // O(Log(V))
            if (costb[bnode.nod] + costf[fnode.nod] >= mue)
                break;
            if (costf[fnode.nod] + costb[bnode.nod] + costb[fnode.nod] + costf[bnode.nod] < mue)
                mue = costf[fnode.nod] + costb[bnode.nod] + costb[fnode.nod] + costf[bnode.nod];
            if (!visf[fnode.nod])
            {
                for (int i = 0; i < edges.elementAt(fnode.nod).size(); i++) // O(E)
                {
                    dis2 = edges.elementAt(fnode.nod).elementAt(i).distance;
                    dis1 = edges.elementAt(fnode.nod).elementAt(i).distance /
                            edges.elementAt(fnode.nod).elementAt(i).velocity;
                    if (dis1 + fnode.time < costf[edges.elementAt(fnode.nod).elementAt(i).id] ||
                            (DCMP(dis1 + fnode.time, costf[edges.elementAt(fnode.nod).elementAt(i).id]) &&
                             dis2 + fnode.distance < costfd[edges.elementAt(fnode.nod).elementAt(i).id]))
                    {
                        costf[edges.elementAt(fnode.nod).elementAt(i).id] = dis1 + fnode.time;
                        costfd[edges.elementAt(fnode.nod).elementAt(i).id] = dis2 + fnode.distance;
                        pqf.add(new pnode(dis1 + fnode.time, dis2 + fnode.distance, fnode.nod, edges.elementAt(fnode.nod).elementAt(i).id)); // O(log(V))
                        parentf[edges.elementAt(fnode.nod).elementAt(i).id] = fnode.nod;
                    }
                }
            }
            if (!visb[bnode.nod])
            {
                for (int i = 0; i < edges.elementAt(bnode.nod).size(); i++) // O(E)
                {
                    dis2 = edges.elementAt(bnode.nod).elementAt(i).distance;
                    dis1 = edges.elementAt(bnode.nod).elementAt(i).distance /
                            edges.elementAt(bnode.nod).elementAt(i).velocity;
                    if (dis1 + bnode.time < costb[edges.elementAt(bnode.nod).elementAt(i).id] ||
                            (DCMP(dis1 + bnode.time, costb[edges.elementAt(bnode.nod).elementAt(i).id]) &&
                                    dis2 + bnode.distance < costbd[edges.elementAt(bnode.nod).elementAt(i).id]))
                    {
                        costb[edges.elementAt(bnode.nod).elementAt(i).id] = dis1 + bnode.time;
                        costbd[edges.elementAt(bnode.nod).elementAt(i).id] = dis2 + bnode.distance;
                        pqb.add(new pnode(dis1 + bnode.time, dis2 + bnode.distance, bnode.nod, edges.elementAt(bnode.nod).elementAt(i).id)); // O(log(V))
                        parentb[edges.elementAt(bnode.nod).elementAt(i).id] = bnode.nod;
                    }
                }
            }
            visf[fnode.nod] = true;
            visb[bnode.nod] = true;
        }
        fnode = new pnode(0.0, 0.0, 0, 0);
        QueryAns QA = new QueryAns();
        for (int i = 0; i < numberOfNodes; i++) // O(V)
        {
            if (QA.shortestTime > costb[i] + costf[i])
            {
                QA.shortestTime = costb[i] + costf[i];
                QA.intersectionNode = i;
                QA.totalDist = costbd[i] + costfd[i];
            }
            else if (QA.shortestTime == costb[i] + costf[i] && QA.totalDist >= costbd[i] + costfd[i])
            {
                QA.shortestTime = costb[i] + costf[i];
                QA.intersectionNode = i;
                QA.totalDist = costbd[i] + costfd[i];
            }
        }
        Integer internode = QA.intersectionNode;
        while (internode != -1) // O(V)
        {
            QA.path.add(internode);
            internode = parentf[internode];
        }
        for (int i = 0; i < QA.path.size() / 2; i++) // O(V)
        {
            internode = QA.path.elementAt(i);
            QA.path.set(i, QA.path.elementAt(QA.path.size() - i - 1));
            QA.path.set(QA.path.size() - i - 1, internode);
        }
        internode = QA.intersectionNode;
        while (parentb[internode] != -1) // O(V)
        {
            QA.path.add(parentb[internode]);
            internode = parentb[internode];
        }
        QA.totalWalkingDest += calcdis(nodes.elementAt(QA.path.lastElement()).X, nodes.elementAt(QA.path.lastElement()).Y, xDest, yDest);
        QA.totalWalkingDest += calcdis(nodes.elementAt(QA.path.firstElement()).X, nodes.elementAt(QA.path.firstElement()).Y, xSrc, ySrc);
        ret = String.format("%.2f", QA.shortestTime * 60.0) + " mins, "
                + String.format("%.2f", QA.totalDist) + " km, "
                + String.format("%.2f", QA.totalWalkingDest) + " km, "
                + String.format("%.2f", QA.totalDist - QA.totalWalkingDest) + " km";

        String s = new String();
        for (int i = 0; i < QA.path.size(); i++) // O(V)
        {
            s += QA.path.elementAt(i);
            if(i != QA.path.size() - 1)
                s += " ";
        }
        lines.add(s);
        lines.add(String.format("%.2f", QA.shortestTime * 60.0) + " mins");
        lines.add(String.format("%.2f", QA.totalDist) + " km");
        lines.add(String.format("%.2f", QA.totalWalkingDest) + " km");
        lines.add(String.format("%.2f", QA.totalDist - QA.totalWalkingDest) + " km");

        return ret;
    }
    //Runs queries
    public static long query(FileReader FR) throws Exception // O(Q)
    {
        long totalTime = 0;
        BufferedReader BR = new BufferedReader(FR);
        String s = BR.readLine(), a[] = new String[5];
        numberOfQueries = Integer.parseInt(s);
        long now;
        for (int i = 0; i < numberOfQueries; i++) // O(Q)
        {
            s = BR.readLine();
            a = s.split(" ");
            now = System.currentTimeMillis();
            LV.getItems().add("Test #" + (i + 1) + ": " +
            DEDIJ(Double.parseDouble(a[4]) / 1000.0, Double.parseDouble(a[0]), Double.parseDouble(a[1])
                    , Double.parseDouble(a[2]), Double.parseDouble(a[3])) + ", path: " + lines.get(lines.size() - 1) + ".");
            now = System.currentTimeMillis() - now;
            timeL.getItems().add(Long.toString(now));
            totalTime += now;
        }
        return totalTime;
    }
}
