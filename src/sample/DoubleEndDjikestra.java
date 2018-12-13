package sample;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.function.DoubleBinaryOperator;

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
    private static Integer numberOfNodes, numberOfEdges, numberOfQueries, to;
    private static Vector<node> nodes = new Vector<node>();
    private static Vector<Vector<Pair<Integer, Pair<Double, Double>>>> edges = new Vector<Vector<Pair<Integer, Pair<Double, Double>>>>();
    DoubleEndDjikestra(){}
    public static class node {
        int ID;
        Double X;
        Double Y;
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
            totalDist = 0.0;
            totalWalkingDest = 0.0;
            totalVehicleDest = 0.0;
            path = new Vector<Integer>();
        }
    }
    public static Double calcdis(Double x1, Double y1, Double x2, Double y2)
    {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }
    public static void build() throws Exception
    {
        FileReader FR = new FileReader("Samples/SampleCases/map3.txt");
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
            edges.add(i, new Vector<Pair<Integer, Pair<Double, Double>>>());
        for (int i = 0; i < numberOfEdges; i++)
        {
            s = BR.readLine();
            a = s.split(" ");
            Pair<Integer, Pair<Double, Double>> n1;
            n1 = new Pair(Integer.parseInt(a[1]), new Pair(Double.parseDouble(a[2]), Double.parseDouble(a[3])));
            edges.elementAt(Integer.parseInt(a[0])).add(n1);
            n1 = new Pair(Integer.parseInt(a[0]), new Pair(Double.parseDouble(a[2]), Double.parseDouble(a[3])));
            edges.elementAt(Integer.parseInt(a[1])).add(n1);
        }
    }
    public static void DEDIJ(Double radius, Double xSrc, Double ySrc, Double xDest, Double yDest)
    {
        PriorityQueue<pnode> pqf = new PriorityQueue<pnode>();
        PriorityQueue<pnode> pqb = new PriorityQueue<pnode>();
        Integer parentf[] = new Integer[numberOfNodes + 2];
        Integer parentb[] = new Integer[numberOfNodes + 2];
        Boolean visf[] = new Boolean[numberOfNodes + 2];
        Boolean visb[] = new Boolean[numberOfNodes + 2];
        Double costf[] = new Double[numberOfNodes + 2];
        Double costb[] = new Double[numberOfNodes + 2];
        Double costfd[] = new Double[numberOfNodes + 2];
        Double costbd[] = new Double[numberOfNodes + 2];
        Arrays.fill(parentf, -1);
        Arrays.fill(parentb, -1);
        Arrays.fill(visf, false);
        Arrays.fill(visb, false);
        Arrays.fill(costf, 1000000000.0);
        Arrays.fill(costb, 1000000000.0);
        Arrays.fill(costfd, 1000000000.0);
        Arrays.fill(costbd, 1000000000.0);
        pnode fnode, bnode;
        Double dis1, dis2;
        for (int i = 0; i < nodes.size(); i++)
        {
            dis1 = calcdis(nodes.elementAt(i).X, nodes.elementAt(i).Y, xSrc, ySrc);
            if (dis1 <= radius * radius)
            {
                pqf.add(new pnode(Math.sqrt(dis1) / 5.0, Math.sqrt(dis1), -1, nodes.elementAt(i).ID));
                costf[nodes.elementAt(i).ID] = Math.sqrt(dis1) / 5.0;
                costfd[nodes.elementAt(i).ID] = Math.sqrt(dis1);
            }
            dis2 = calcdis(nodes.elementAt(i).X, nodes.elementAt(i).Y, xDest, yDest);
            if (dis2 <= radius * radius)
            {
                pqb.add(new pnode(Math.sqrt(dis2) / 5.0, Math.sqrt(dis2), -1, nodes.elementAt(i).ID));
                costb[nodes.elementAt(i).ID] = Math.sqrt(dis2) / 5.0;
                costbd[nodes.elementAt(i).ID] = Math.sqrt(dis2);
            }
        }
        while (!pqb.isEmpty() && !pqf.isEmpty())
        {
            fnode = pqf.peek();
            pqf.poll();
            bnode = pqb.peek();
            pqb.poll();
            if (costb[fnode.nod] != 1000000000.0 && costf[fnode.nod] != 1000000000.0)
                break;
            else if (costb[bnode.nod] != 1000000000.0 && costf[bnode.nod] != 1000000000.0)
                break;
            if (!visf[fnode.nod])
            {
                for (int i = 0; i < edges.elementAt(fnode.nod).size(); i++)
                {
                    dis2 = edges.elementAt(fnode.nod).elementAt(i).getValue().getKey();
                    dis1 = edges.elementAt(fnode.nod).elementAt(i).getValue().getKey() /
                            edges.elementAt(fnode.nod).elementAt(i).getValue().getValue();
                    if (dis1 + fnode.time < costf[edges.elementAt(fnode.nod).elementAt(i).getKey()] ||
                            (dis1 + fnode.time == costf[edges.elementAt(fnode.nod).elementAt(i).getKey()] &&
                             dis2 + fnode.distance < costfd[edges.elementAt(fnode.nod).elementAt(i).getKey()]))
                    {
                        costf[edges.elementAt(fnode.nod).elementAt(i).getKey()] = dis1 + fnode.time;
                        costfd[edges.elementAt(fnode.nod).elementAt(i).getKey()] = dis2 + fnode.distance;
                        pqf.add(new pnode(dis1 + fnode.time, dis2 + fnode.distance, fnode.nod, edges.elementAt(fnode.nod).elementAt(i).getKey()));
                        parentf[edges.elementAt(fnode.nod).elementAt(i).getKey()] = fnode.nod;
                    }
                }
            }
            if (!visb[bnode.nod])
            {
                for (int i = 0; i < edges.elementAt(bnode.nod).size(); i++)
                {
                    dis2 = edges.elementAt(bnode.nod).elementAt(i).getValue().getKey();
                    dis1 = edges.elementAt(bnode.nod).elementAt(i).getValue().getKey() /
                            edges.elementAt(bnode.nod).elementAt(i).getValue().getValue();
                    if (dis1 + bnode.time < costb[edges.elementAt(bnode.nod).elementAt(i).getKey()] ||
                            (dis1 + bnode.time == costb[edges.elementAt(bnode.nod).elementAt(i).getKey()] &&
                                    dis2 + bnode.distance < costbd[edges.elementAt(bnode.nod).elementAt(i).getKey()]))
                    {
                        costb[edges.elementAt(bnode.nod).elementAt(i).getKey()] = dis1 + bnode.time;
                        costbd[edges.elementAt(bnode.nod).elementAt(i).getKey()] = dis2 + bnode.distance;
                        pqb.add(new pnode(dis1 + bnode.time, dis2 + bnode.distance, bnode.nod, edges.elementAt(bnode.nod).elementAt(i).getKey()));
                        parentb[edges.elementAt(bnode.nod).elementAt(i).getKey()] = bnode.nod;
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
        dis1 = 0.0;
        for (int i = 0; i < QA.path.size() - 1; i++)
        {
            for (int j = 0; j < edges.elementAt(QA.path.elementAt(i)).size(); j++)
                if (edges.elementAt(QA.path.elementAt(i)).elementAt(j).getKey() == QA.path.elementAt(i + 1))
                {
                    dis1 = edges.elementAt(QA.path.elementAt(i)).elementAt(j).getValue().getKey();
                    break;
                }
            QA.totalDist += dis1;
        }
        QA.totalWalkingDest += Math.sqrt(calcdis(nodes.elementAt(QA.path.lastElement()).X, nodes.elementAt(QA.path.lastElement()).Y, xDest, yDest));
        QA.totalWalkingDest += Math.sqrt(calcdis(nodes.elementAt(QA.path.firstElement()).X, nodes.elementAt(QA.path.firstElement()).Y, xSrc, ySrc));
        QA.totalDist += QA.totalWalkingDest;
        System.out.println(String.format("%.2f", QA.shortestTime * 60.0));
        System.out.println(String.format("%.2f", QA.totalDist));
        System.out.println(String.format("%.2f", QA.totalWalkingDest));
        System.out.println(String.format("%.2f", QA.totalDist - QA.totalWalkingDest));
        for (int i = 0; i < QA.path.size(); i++)
            System.out.print(QA.path.elementAt(i) + " ");
        System.out.println();
    }
    public static void query() throws Exception
    {
        FileReader FR = new FileReader("Samples/SampleCases/queries3.txt");
        BufferedReader BR = new BufferedReader(FR);
        String s = BR.readLine(), a[] = new String[5];
        numberOfQueries = Integer.parseInt(s);
        for (int i = 0; i < numberOfQueries; i++)
        {
            LocalDateTime now = LocalDateTime.now();
            s = BR.readLine();
            a = s.split(" ");
            DEDIJ(Double.parseDouble(a[4]) / 1000, Double.parseDouble(a[0]), Double.parseDouble(a[1])
                    , Double.parseDouble(a[2]), Double.parseDouble(a[3]));
            System.out.println((LocalDateTime.now().getSecond() - now.getSecond()) * 1000.0);
            System.out.println();
        }
    }

}
