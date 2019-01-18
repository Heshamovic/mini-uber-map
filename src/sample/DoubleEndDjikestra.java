package sample;

import javafx.scene.control.ListView;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Vector;

public class DoubleEndDjikestra {
    // o(1)
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
    public static double calcdis(Double x1, Double y1, Double x2, Double y2) // O(log(Distance)
    {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    //Calculating Ecludian distance between 2 points squared
    public static double calcdis2(Double x1, Double y1, Double x2, Double y2) // O(log(Distance)
    {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }
    //Building the graph (Nodes and Edges)
    public static void build(FileReader FR) throws Exception // O(E + V)
    {
        BufferedReader BR = new BufferedReader(FR);// O(1)
        String s = BR.readLine();// O(1)
        String a[] = new String[3];// O(1)
        numberOfNodes = Integer.parseInt(s);// O(S)
        for (int i = 0; i < numberOfNodes; i++) // O(V)
        {
            s = BR.readLine();// O(1)
            a = s.split(" ");// O(1)
            node n = new node(Integer.parseInt(a[0]), Double.parseDouble(a[1]), Double.parseDouble(a[2]));// O(1)
            nodes.add(n);// O(1)
        }
        s = BR.readLine();// O(1)
        numberOfEdges = Integer.parseInt(s);// O(1)
        a = new String[4];// O(1)
        edges.setSize(numberOfNodes + 1); // O(1)
        for (int i = 0; i < numberOfNodes; i++) // O(V)
            edges.add(i, new Vector<enode>());// O(1)
        enode n1;// O(1)
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
        String ret; // O(1)
        PriorityQueue<pnode> pqf = new PriorityQueue<>();// O(1)
        PriorityQueue<pnode> pqb = new PriorityQueue<>();// O(1)
        Integer parentf[] = new Integer[numberOfNodes + 1];// O(1)
        Integer parentb[] = new Integer[numberOfNodes + 1];// O(1)
        Boolean visf[] = new Boolean[numberOfNodes + 1];// O(1)
        Boolean visb[] = new Boolean[numberOfNodes + 1];// O(1)
        double costf[] = new double[numberOfNodes + 1];// O(1)
        double costb[] = new double[numberOfNodes + 1];// O(1)
        double costfd[] = new double[numberOfNodes + 1];// O(1)
        double costbd[] = new double[numberOfNodes + 1];// O(1)
        Arrays.fill(parentf, -1); // O(V)
        Arrays.fill(parentb, -1); // O(V)
        Arrays.fill(visf, false); // O(V)
        Arrays.fill(visb, false); // O(V)
        Arrays.fill(costf, Double.MAX_VALUE / 10.0); // O(V)
        Arrays.fill(costb, Double.MAX_VALUE / 10.0); // O(V)
        Arrays.fill(costfd, Double.MAX_VALUE / 10.0); // O(V)
        Arrays.fill(costbd, Double.MAX_VALUE / 10.0); // O(V)
        pnode fnode, bnode;// O(1)
        double dis1, dis2, mue = Double.MAX_VALUE;// O(1)
        for (int i = 0; i < nodes.size(); i++) // O(V log(V))
        {
            dis1 = calcdis2(nodes.elementAt(i).X, nodes.elementAt(i).Y, xSrc, ySrc);// O(log(Distance))
            if (dis1 <= radius * radius)
            {
                dis1 = calcdis(nodes.elementAt(i).X, nodes.elementAt(i).Y, xSrc, ySrc);// O(log(Distance))
                pqf.add(new pnode(dis1 / 5.0, dis1, -1, nodes.elementAt(i).ID)); // O(Log(V))
                costf[nodes.elementAt(i).ID] = dis1 / 5.0;// O(1)
                costfd[nodes.elementAt(i).ID] = dis1;// O(1)
            }
            dis2 = calcdis2(nodes.elementAt(i).X, nodes.elementAt(i).Y, xDest, yDest);// O(log(Distance))
            if (dis2 <= radius * radius)// O(1)
            {
                dis2 = calcdis(nodes.elementAt(i).X, nodes.elementAt(i).Y, xDest, yDest);// O(log(Distance))
                pqb.add(new pnode(dis2 / 5.0, dis2, -1, nodes.elementAt(i).ID)); // O(log(V))
                costb[nodes.elementAt(i).ID] = dis2 / 5.0;// O(1)
                costbd[nodes.elementAt(i).ID] = dis2;// O(1)
            }
        }
        while (!pqb.isEmpty() && !pqf.isEmpty()) // O(E log(V))
        {
            fnode = pqf.peek();// O(1)
            pqf.poll(); // O(log(V))
            bnode = pqb.peek();// O(1)
            pqb.poll(); // O(Log(V))
            if (costb[bnode.nod] + costf[fnode.nod] >= mue)// O(1)
                break;
            if (costf[fnode.nod] + costb[bnode.nod] + costb[fnode.nod] + costf[bnode.nod] < mue)// O(1)
                mue = costf[fnode.nod] + costb[bnode.nod] + costb[fnode.nod] + costf[bnode.nod];
            if (!visf[fnode.nod])
            {
                for (int i = 0; i < edges.elementAt(fnode.nod).size(); i++) // O(E log(V))
                {
                    dis2 = edges.elementAt(fnode.nod).elementAt(i).distance;// O(1)
                    dis1 = edges.elementAt(fnode.nod).elementAt(i).distance /
                            edges.elementAt(fnode.nod).elementAt(i).velocity;// O(1)
                    if (dis1 + fnode.time < costf[edges.elementAt(fnode.nod).elementAt(i).id] ||
                            (DCMP(dis1 + fnode.time, costf[edges.elementAt(fnode.nod).elementAt(i).id]) &&
                             dis2 + fnode.distance < costfd[edges.elementAt(fnode.nod).elementAt(i).id]))// O(log(v))
                    {
                        costf[edges.elementAt(fnode.nod).elementAt(i).id] = dis1 + fnode.time;// O(1)
                        costfd[edges.elementAt(fnode.nod).elementAt(i).id] = dis2 + fnode.distance;// O(1)
                        pqf.add(new pnode(dis1 + fnode.time, dis2 + fnode.distance, fnode.nod, edges.elementAt(fnode.nod).elementAt(i).id)); // O(log(V))
                        parentf[edges.elementAt(fnode.nod).elementAt(i).id] = fnode.nod;// O(1)
                    }
                }
            }
            if (!visb[bnode.nod])
            {
                for (int i = 0; i < edges.elementAt(bnode.nod).size(); i++) // O(E log(V))
                {
                    dis2 = edges.elementAt(bnode.nod).elementAt(i).distance;// O(1)
                    dis1 = edges.elementAt(bnode.nod).elementAt(i).distance /
                            edges.elementAt(bnode.nod).elementAt(i).velocity;// O(1)
                    if (dis1 + bnode.time < costb[edges.elementAt(bnode.nod).elementAt(i).id] ||
                            (DCMP(dis1 + bnode.time, costb[edges.elementAt(bnode.nod).elementAt(i).id]) &&
                                    dis2 + bnode.distance < costbd[edges.elementAt(bnode.nod).elementAt(i).id]))// O(log(V))
                    {
                        costb[edges.elementAt(bnode.nod).elementAt(i).id] = dis1 + bnode.time;// O(1)
                        costbd[edges.elementAt(bnode.nod).elementAt(i).id] = dis2 + bnode.distance;// O(1)
                        pqb.add(new pnode(dis1 + bnode.time, dis2 + bnode.distance, bnode.nod, edges.elementAt(bnode.nod).elementAt(i).id)); // O(log(V))
                        parentb[edges.elementAt(bnode.nod).elementAt(i).id] = bnode.nod;// O(1)
                    }
                }
            }
            visf[fnode.nod] = true;// O(1)
            visb[bnode.nod] = true;// O(1)
        }
        fnode = new pnode(0.0, 0.0, 0, 0);// O(1)
        QueryAns QA = new QueryAns();// O(1)
        for (int i = 0; i < numberOfNodes; i++) // O(V)
        {
            if (QA.shortestTime > costb[i] + costf[i])
            {
                QA.shortestTime = costb[i] + costf[i];// O(1)
                QA.intersectionNode = i;// O(1)
                QA.totalDist = costbd[i] + costfd[i];// O(1)
            }
            else if (QA.shortestTime == costb[i] + costf[i] && QA.totalDist >= costbd[i] + costfd[i])// O(1)
            {
                QA.shortestTime = costb[i] + costf[i];// O(1)
                QA.intersectionNode = i;// O(1)
                QA.totalDist = costbd[i] + costfd[i];// O(1)
            }
        }
        Integer internode = QA.intersectionNode;// O(1)
        while (internode != -1) // O(V)
        {
            QA.path.add(internode);// O(1)
            internode = parentf[internode];// O(1)
        }
        for (int i = 0; i < QA.path.size() / 2; i++) // O(V)
        {
            internode = QA.path.elementAt(i);// O(1)
            QA.path.set(i, QA.path.elementAt(QA.path.size() - i - 1));// O(1)
            QA.path.set(QA.path.size() - i - 1, internode);// O(1)
        }
        internode = QA.intersectionNode;// O(1)
        while (parentb[internode] != -1) // O(V)
        {
            QA.path.add(parentb[internode]);// O(1)
            internode = parentb[internode];// O(1)
        }
        QA.totalWalkingDest += calcdis(nodes.elementAt(QA.path.lastElement()).X, nodes.elementAt(QA.path.lastElement()).Y, xDest, yDest);//o(log(distance))
        QA.totalWalkingDest += calcdis(nodes.elementAt(QA.path.firstElement()).X, nodes.elementAt(QA.path.firstElement()).Y, xSrc, ySrc);//o(log(distance))
        ret = String.format("%.2f", QA.shortestTime * 60.0) + " mins, "
                + String.format("%.2f", QA.totalDist) + " km, "
                + String.format("%.2f", QA.totalWalkingDest) + " km, "
                + String.format("%.2f", QA.totalDist - QA.totalWalkingDest) + " km";// O(1)

        String s = new String();
        for (int i = 0; i < QA.path.size(); i++) // O(V)
        {
            s += QA.path.elementAt(i);// O(1)
            if(i != QA.path.size() - 1)// O(1)
                s += " ";// O(1)
        }
        lines.add(s);//GUI
        lines.add(String.format("%.2f", QA.shortestTime * 60.0) + " mins");//GUI
        lines.add(String.format("%.2f", QA.totalDist) + " km");//GUI
        lines.add(String.format("%.2f", QA.totalWalkingDest) + " km");//GUI
        lines.add(String.format("%.2f", QA.totalDist - QA.totalWalkingDest) + " km");//GUI

        return ret;//O(1)
    }
    //Runs queries
    public static long query(FileReader FR) throws Exception // O(Q)
    {
        long totalTime = 0;//O(1)
        BufferedReader BR = new BufferedReader(FR);//O(1)
        String s = BR.readLine(), a[] = new String[5];//O(1)
        numberOfQueries = Integer.parseInt(s);//O(1)
        long now;//O(1)
        for (int i = 0; i < numberOfQueries; i++) // O(Q)
        {
            s = BR.readLine();//O(1)
            a = s.split(" ");//O(S)
            now = System.currentTimeMillis();//O(1)
            LV.getItems().add("Test #" + (i + 1) + ": " +
            DEDIJ(Double.parseDouble(a[4]) / 1000.0, Double.parseDouble(a[0]), Double.parseDouble(a[1])
                    , Double.parseDouble(a[2]), Double.parseDouble(a[3])) + ", path: " + lines.get(lines.size() - 1) + ".");
            now = System.currentTimeMillis() - now;
            timeL.getItems().add(Long.toString(now));
            totalTime += now;//O(1)
        }
        return totalTime;//O(1)
    }
}
