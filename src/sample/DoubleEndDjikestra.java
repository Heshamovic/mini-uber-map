package sample;

import javafx.scene.control.ListView;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Vector;

public class DoubleEndDjikestra {
    public static class pnode implements Comparable<pnode>
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
    public static class enode
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
    public static class node
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
    public static class QueryAns
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
    private static Integer numberOfNodes, numberOfEdges, numberOfQueries;
    private static Vector<node> nodes;
    private static Vector<Vector<enode>> edges;
    DoubleEndDjikestra(){
        nodes = new Vector<node>();
        edges = new Vector<Vector<enode>>();
        numberOfEdges = numberOfNodes = numberOfQueries = 0;
    }
    public static double calcdis(Double x1, Double y1, Double x2, Double y2)
    {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    public static void build(FileReader FR) throws Exception
    {
        BufferedReader BR = new BufferedReader(FR);
        String s = BR.readLine();
        String a[] = new String[3];
        numberOfNodes = Integer.parseInt(s);
        for (int i = 0; i < numberOfNodes; i++)
        {
            s = BR.readLine();
            a = s.split(" ");
            node n = new node(Integer.parseInt(a[0]), Double.parseDouble(a[1]), Double.parseDouble(a[2]));
            nodes.add(n);
        }
        s = BR.readLine();
        numberOfEdges = Integer.parseInt(s);
        a = new String[4];
        edges.setSize(numberOfNodes + 1);
        for (int i = 0; i < numberOfNodes; i++)
            edges.add(i, new Vector<enode>());
        for (int i = 0; i < numberOfEdges; i++)
        {
            s = BR.readLine();
            a = s.split(" ");
            enode n1;
            n1 = new enode(Integer.parseInt(a[1]), Double.parseDouble(a[2]), Double.parseDouble(a[3]));
            edges.elementAt(Integer.parseInt(a[0])).add(n1);
            n1 = new enode(Integer.parseInt(a[0]), Double.parseDouble(a[2]), Double.parseDouble(a[3]));
            edges.elementAt(Integer.parseInt(a[1])).add(n1);
        }
    }
    public static String DEDIJ(Double radius, Double xSrc, Double ySrc, Double xDest, Double yDest)
    {
        String ret;
        PriorityQueue<pnode> pqf = new PriorityQueue<pnode>();
        PriorityQueue<pnode> pqb = new PriorityQueue<pnode>();
        Integer parentf[] = new Integer[numberOfNodes + 2];
        Integer parentb[] = new Integer[numberOfNodes + 2];
        Boolean visf[] = new Boolean[numberOfNodes + 2];
        Boolean visb[] = new Boolean[numberOfNodes + 2];
        double costf[] = new double[numberOfNodes + 2];
        double costb[] = new double[numberOfNodes + 2];
        double costfd[] = new double[numberOfNodes + 2];
        double costbd[] = new double[numberOfNodes + 2];
        Arrays.fill(parentf, -1);
        Arrays.fill(parentb, -1);
        Arrays.fill(visf, false);
        Arrays.fill(visb, false);
        Arrays.fill(costf, Double.MAX_VALUE / 10.0);
        Arrays.fill(costb, Double.MAX_VALUE / 10.0);
        Arrays.fill(costfd, Double.MAX_VALUE / 10.0);
        Arrays.fill(costbd, Double.MAX_VALUE / 10.0);
        pnode fnode, bnode;
        double dis1, dis2, mue = Double.MAX_VALUE;
        for (int i = 0; i < nodes.size(); i++)
        {
            dis1 = calcdis(nodes.elementAt(i).X, nodes.elementAt(i).Y, xSrc, ySrc);
            if (dis1 <= radius)
            {
                pqf.add(new pnode(dis1 / 5.0, dis1, -1, nodes.elementAt(i).ID));
                costf[nodes.elementAt(i).ID] = dis1 / 5.0;
                costfd[nodes.elementAt(i).ID] = dis1;
            }
            dis2 = calcdis(nodes.elementAt(i).X, nodes.elementAt(i).Y, xDest, yDest);
            if (dis2 <= radius)
            {
                pqb.add(new pnode(dis2 / 5.0, dis2, -1, nodes.elementAt(i).ID));
                costb[nodes.elementAt(i).ID] = dis2 / 5.0;
                costbd[nodes.elementAt(i).ID] = dis2;
            }
        }
        while (!pqb.isEmpty() && !pqf.isEmpty())
        {
            fnode = pqf.peek();
            pqf.poll();
            bnode = pqb.peek();
            pqb.poll();
            if (costb[bnode.nod] + costf[fnode.nod] >= mue)
                break;
            if (costf[fnode.nod] + costb[bnode.nod] + costb[fnode.nod] + costf[bnode.nod] < mue)
                mue = costf[fnode.nod] + costb[bnode.nod] +  + costb[fnode.nod] + costf[bnode.nod];
            if (!visf[fnode.nod])
            {
                for (int i = 0; i < edges.elementAt(fnode.nod).size(); i++)
                {
                    dis2 = edges.elementAt(fnode.nod).elementAt(i).distance;
                    dis1 = edges.elementAt(fnode.nod).elementAt(i).distance /
                            edges.elementAt(fnode.nod).elementAt(i).velocity;
                    if (dis1 + fnode.time < costf[edges.elementAt(fnode.nod).elementAt(i).id] ||
                            (dis1 + fnode.time == costf[edges.elementAt(fnode.nod).elementAt(i).id] &&
                             dis2 + fnode.distance < costfd[edges.elementAt(fnode.nod).elementAt(i).id]))
                    {
                        costf[edges.elementAt(fnode.nod).elementAt(i).id] = dis1 + fnode.time;
                        costfd[edges.elementAt(fnode.nod).elementAt(i).id] = dis2 + fnode.distance;
                        pqf.add(new pnode(dis1 + fnode.time, dis2 + fnode.distance, fnode.nod, edges.elementAt(fnode.nod).elementAt(i).id));
                        parentf[edges.elementAt(fnode.nod).elementAt(i).id] = fnode.nod;
                    }
                }
            }
            if (!visb[bnode.nod])
            {
                for (int i = 0; i < edges.elementAt(bnode.nod).size(); i++)
                {
                    dis2 = edges.elementAt(bnode.nod).elementAt(i).distance;
                    dis1 = edges.elementAt(bnode.nod).elementAt(i).distance /
                            edges.elementAt(bnode.nod).elementAt(i).velocity;
                    if (dis1 + bnode.time < costb[edges.elementAt(bnode.nod).elementAt(i).id] ||
                            (dis1 + bnode.time == costb[edges.elementAt(bnode.nod).elementAt(i).id] &&
                                    dis2 + bnode.distance < costbd[edges.elementAt(bnode.nod).elementAt(i).id]))
                    {
                        costb[edges.elementAt(bnode.nod).elementAt(i).id] = dis1 + bnode.time;
                        costbd[edges.elementAt(bnode.nod).elementAt(i).id] = dis2 + bnode.distance;
                        pqb.add(new pnode(dis1 + bnode.time, dis2 + bnode.distance, bnode.nod, edges.elementAt(bnode.nod).elementAt(i).id));
                        parentb[edges.elementAt(bnode.nod).elementAt(i).id] = bnode.nod;
                    }
                }
            }
            visf[fnode.nod] = visb[bnode.nod] = true;
        }
        fnode = new pnode(0.0, 0.0, 0, 0);
        QueryAns QA = new QueryAns();
        for (int i = 0; i < numberOfNodes; i++)
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
        while (internode != -1)
        {
            QA.path.add(internode);
            internode = parentf[internode];
        }
        for (int i = 0; i < QA.path.size() / 2; i++)
        {
            internode = QA.path.elementAt(i);
            QA.path.set(i, QA.path.elementAt(QA.path.size() - i - 1));
            QA.path.set(QA.path.size() - i - 1, internode);
        }
        internode = QA.intersectionNode;
        while (parentb[internode] != -1)
        {
            QA.path.add(parentb[internode]);
            internode = parentb[internode];
        }
        QA.totalWalkingDest += calcdis(nodes.elementAt(QA.path.lastElement()).X, nodes.elementAt(QA.path.lastElement()).Y, xDest, yDest);
        QA.totalWalkingDest += calcdis(nodes.elementAt(QA.path.firstElement()).X, nodes.elementAt(QA.path.firstElement()).Y, xSrc, ySrc);
        ret = String.format("%.2f", QA.shortestTime * 60.0) + " mins, "
                + String.format("%.2f", QA.totalDist) + " km, "
                + String.format("%.2f", QA.totalWalkingDest) + " km, "
                + String.format("%.2f", QA.totalDist - QA.totalWalkingDest) + " km.";
       /* System.out.println(String.format("%.2f", QA.shortestTime * 60.0) + " mins");
        System.out.println(String.format("%.2f", QA.totalDist) + " km");
        System.out.println(String.format("%.2f", QA.totalWalkingDest) + " km");
        System.out.println(String.format("%.2f", QA.totalDist - QA.totalWalkingDest) + " km");
        for (int i = 0; i < QA.path.size(); i++)
            System.out.print(QA.path.elementAt(i) + " ");
        System.out.println();*/
       return ret;
    }
    public static ListView<String> LV= new ListView<String>();
    public static long query(FileReader FR) throws Exception
    {
        long totalTime = 0;
        BufferedReader BR = new BufferedReader(FR);
        String s = BR.readLine(), a[] = new String[5];
        numberOfQueries = Integer.parseInt(s);
        for (int i = 0; i < numberOfQueries; i++)
        {
            s = BR.readLine();
            a = s.split(" ");
            long now = System.currentTimeMillis();
            LV.getItems().add("Test #" + (i + 1) + ": " +
            DEDIJ(Double.parseDouble(a[4]) / 1000.0, Double.parseDouble(a[0]), Double.parseDouble(a[1])
                    , Double.parseDouble(a[2]), Double.parseDouble(a[3])));
            now = System.currentTimeMillis() - now;
            System.out.println(now + " ms");
            System.out.println();
            totalTime += now;
        }
        return totalTime;
    }
}
