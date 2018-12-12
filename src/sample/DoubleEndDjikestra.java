package sample;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Vector;

public class DoubleEndDjikestra {
    private static Vector<Vector<Integer>> graph = new Vector<Vector<Integer>>();
    private static int numberOfNodes, numberOfEdges, numberOfQueries, to;
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
        public int shortestTime;
        public int totalDist;
        public int totalWalkingDest;
        public int totalVehicleDest;
        public Vector<Integer> path;
        QueryAns()
        {
            shortestTime = 0;
            totalDist = 0;
            totalWalkingDest = 0;
            totalVehicleDest = 0;
            path = new Vector<Integer>();
        }
    }
    public static void build() throws Exception
    {
        FileReader FR = new FileReader("Samples/SampleCases/map1.txt");
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
    public static void DEJ(int radius, int xSrc, int ySrc, int xDest, int yDest)
    {
        PriorityQueue<Pair<Integer, Pair<Integer, Integer>>> pqf = new PriorityQueue<Pair<Integer, Pair<Integer, Integer>>>();
        PriorityQueue<Pair<Integer, Pair<Integer, Integer>>> pqb = new PriorityQueue<Pair<Integer, Pair<Integer, Integer>>>();
        Integer parentf[] = new Integer[numberOfNodes + 2];
        Integer parentb[] = new Integer[numberOfNodes + 2];
        Arrays.fill(parentf, -1);
        Arrays.fill(parentb, -1);
    }
    public static void query() throws Exception
    {
        FileReader FR = new FileReader("Samples/LargeCases/SFQueries.txt");
        BufferedReader BR = new BufferedReader(FR);
        String s = BR.readLine(), a[] = new String[5];
        numberOfQueries = Integer.parseInt(s);
        for (int i = 0; i < numberOfQueries; i++)
        {
            s = BR.readLine();
            a = s.split(" ");
            DEJ(Integer.parseInt(a[4]), Integer.parseInt(a[0]), Integer.parseInt(a[2]), Integer.parseInt(a[3]), Integer.parseInt(a[4]));
        }
    }

}
