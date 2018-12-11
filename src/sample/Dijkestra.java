package sample;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Vector;

public class Dijkestra {

    public static class dijsktra
    {
        int []parent ;
        double []cost ;
        int no_nodes , edges , query ;
        double totalwalk,totaldrive;
        Double x1 , y1 , x2 ,y2 , R ;
        PriorityQueue<Entry> dij ;
        Vector<node> nodes ;
        Vector<Integer> path ;
        dijsktra()
        {
            edges = 0 ;
            no_nodes = 0 ;
            query = 0 ;
            dij = new PriorityQueue<Entry>();
            nodes = new Vector<node>(100005);
            nodes.setSize(100005);
            path = new Vector<Integer>();
            totalwalk=0;
            totaldrive=0;
        }
        void init()
        {
            parent = new int[no_nodes+2];
            for(int i=0 ; i<no_nodes+2 ; i++)
            {
                parent[i] = i ;
            }
            cost = new double[no_nodes+2] ;
            for(int i=0 ; i<no_nodes+2 ; i++)
            {
                cost[i] = 1000000000 ;
            }
            dij = new PriorityQueue<Entry>() ;
            path.clear();
            totaldrive=0;
            totalwalk=0;
        }
        void node_input()
        {
            for(int i=0 ; i<no_nodes ; i++)
            {
                node newnode = new node();
                Scanner in = new Scanner(System.in);
                newnode.id = in.nextInt();
                newnode.x = in.nextDouble();
                newnode.y = in.nextDouble();
                nodes.add(newnode.id,newnode);

            }
        }
        void displayale ()
        {

        }

        void edge_input()
        {
            for(int i=0 ; i<edges ; i++)
            {
                int node1 , node2 ;
                Double vel , dis , time ;
                Scanner in = new Scanner(System.in);
                node1 = in.nextInt();
                node2 = in.nextInt();
                vel = in.nextDouble();
                dis = in.nextDouble();
                time = (dis/vel);
                nodes.elementAt(node1).distance[node2] = dis ;
                nodes.elementAt(node2).distance[node1] = dis ;
                // nodes.elementAt(node1).distance.elementAt(node2).equals(dis);
                //  nodes.elementAt(node2).distance.elementAt(node1).equals(dis);
                System.out.println(nodes.elementAt(3).distance[2]);
                //  System.out.println(nodes.elementAt(node2).distance.elementAt(node1));
                //  System.out.println(nodes.get(node2).distance.elementAt(node1) + " " + nodes.get(node1).distance.elementAt(node2));
                nodes.elementAt(node1).add(node2,time);
                nodes.elementAt(node2).add(node1,time);
            }
        }

        String lines[];
        public void getInput()throws Exception
        {
            try{
                FileReader file = new FileReader("/C:/Users/user/Desktop/map1.txt");
                BufferedReader bf = new BufferedReader(file);

                no_nodes = Integer.parseInt(bf.readLine());
                String s = bf.readLine();
                for(int i = 0 ; i < no_nodes;i++)
                {
                    System.out.println(s);
                    lines = new String[3];
                    lines = s.split(" ");
                    nodes.get(Integer.parseInt(lines[0])).x = Double.parseDouble(lines[1]);
                    nodes.get(Integer.parseInt(lines[0])).y = Double.parseDouble(lines[2]);
                    //  System.out.println(s);
                    s = bf.readLine();
                }
                for(int i = 0 ; i < no_nodes;i++)
                    System.out.println(nodes.get(i).x + ' ' + nodes.get(i).y);
            }catch (Exception e)
            {

                System.out.println(e + "a7a");
            }
        }
        int solve()throws Exception
        {
            getInput();
           /* System.out.println("Enter id , X-axis and Y-axis of nodes : ");
            node_input();
            System.out.println("Enter the number of edges : ");
            edges = in.nextInt();
            edge_input();
            System.out.println("Enter the number of queries : ");
            query = in.nextInt();

            for(int i=0 ; i<query ; i++)
            {
                init();
                System.out.println("Enter X1 , Y1 , X2 , Y2 and R : ");
                x1 = in.nextDouble() ;
                y1 = in.nextDouble() ;
                x2 = in.nextDouble() ;
                y2 = in.nextDouble() ;
                R = in.nextDouble() ;
                getsources();
                build();
                getpath(getdest());
              //  displayale();
                display();

            }*/
            return 1;
        }
        void display()
        {
            for(int i=path.size()-1 ; i>=0 ; i--)
            {
                System.out.print(path.elementAt(i)+" " );
            }
            System.out.println();
            //  System.out.println(nodes.elementAt(3).distance.elementAt(2));
            for(int i=0;i<path.size()-1;i++)
            {
                // System.out.println(path.get(i+1));
                // System.out.println(nodes.elementAt(path.get(i)).distance.get(path.get(i+1)));
                System.out.println();
                totaldrive+=(nodes.get(path.elementAt(i)).distance[path.elementAt(i+1)]);
            }
            System.out.println(totaldrive);
            System.out.println();
        }
        void getpath(int end)
        {
            totalwalk = Math.sqrt( Math.pow( Math.abs(x2-nodes.elementAt(end).x) ,2) + Math.pow( Math.abs(y2-nodes.elementAt(end).y) ,2) );
            path.add(end);
            while(parent[end]!=end)
            {
                path.add(parent[end]);

                end = parent[end];
            }
            totalwalk+= Math.sqrt( Math.pow( Math.abs(x2-nodes.elementAt(end).x) ,2) + Math.pow( Math.abs(y2-nodes.elementAt(end).y) ,2) );
        }
        int  getdest()
        {
            double mn = 1000000000 ;
            int ind = -1 ;
            for(int i=0 ; i<no_nodes ; i++)
            {
                double dis = Math.sqrt( Math.pow( Math.abs(x2-nodes.elementAt(i).x) ,2) + Math.pow( Math.abs(y2-nodes.elementAt(i).y) ,2) );
                // System.out.println(dis);
                if(dis<=R)
                {
                    // System.out.println(ind + " index here "+ mn);
                    double time = dis / 5.0 ;
                    if(time<mn)
                    {
                        ind=i;
                        mn=time;
                    }
                }
            }
            //  System.out.println(ind);
            return ind;
        }
        void build()
        {
            while(dij.size() > 0)
            {
                int newnode=dij.peek().getValue();
                double nodecost=dij.peek().getKey();
                dij.poll();
                cost[newnode]=nodecost;
                //  System.out.println(newnode);
                for (int i=0;i<nodes.elementAt(newnode).child.size();i++)
                {
                    int nei=nodes.elementAt(newnode).child.elementAt(i).getKey();
                    double neicost=nodes.get(newnode).child.elementAt(i).getValue();
                    if (neicost + nodecost < cost[nei])
                    {
                        cost[nei]=neicost + nodecost;
                        Entry p= new Entry(neicost + nodecost,nei);
                        dij.add(p);
                        parent[nei] = newnode ;
                    }

                }

            }

        }
        void getsources()
        {
            for(int i=0 ; i<no_nodes ; i++)
            {
                double dis = Math.sqrt( Math.pow( Math.abs(x1-nodes.get(i).x) ,2) + Math.pow( Math.abs(y1-nodes.get(i).y) ,2) );
                if(dis<=R)
                {
                    double time = dis / 5.0 ;
                    Entry p = new Entry(time,i);
                    dij.add(p);
                }
            }
        }
    }
    public static class node
    {
        int id ;
        double x , y ;
        Vector<Pair<Integer,Double>> child ;
        double []distance ;
        node()
        {
            x = 0 ;
            y = 0 ;
            distance = new double [10005];
            child = new Vector<Pair<Integer,Double>>();
        }
        void add(int element,Double cost)
        {
            Pair<Integer,Double> p = new Pair<Integer, Double>(element,cost);
            child.add(p);
        }

    }
    public static class Entry implements Comparable<Entry> {
        public double key;
        public int value;

        public Entry(double key, int value) {
            this.key = key;
            this.value = value;
        }
        public double getKey()
        {
            return this.key;
        }
        public int getValue()
        {
            return this.value;
        }
        // getters

        @Override
        public int compareTo(Entry other) {
            if (this.key==other.key)
                return  0;
            if (this.key>other.key)
                return 1;
            else
                return -1;
        }
    }
    public static void main(String[] args)
    {

        dijsktra problem;
        problem = new dijsktra();
        try{
            problem.solve();
        }catch (Exception e){
            System.out.println(e);
        }
    }

}
