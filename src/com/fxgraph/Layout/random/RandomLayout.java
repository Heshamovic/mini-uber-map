package com.fxgraph.Layout.random;
import java.util.List;
import java.util.Random;

import com.fxgraph.graph.Cell;
import com.fxgraph.graph.Graph;
import com.fxgraph.Layout.base.Layout;
import sample.Controller;
import sample.Dijkestra;

public class RandomLayout extends Layout {

    Graph graph;

    Random rnd = new Random();

    public RandomLayout(Graph graph) {

        this.graph = graph;

    }

    public void execute() {

        List<Cell> cells = graph.getModel().getAllCells();

        for (Cell cell : cells) {
            double x, y;
            if(cell.getCellId() == "Source")
            {
                x = Dijkestra.dijkstra.sordes.get(Controller.query).getKey().x;//rnd.nextDouble() * 500;
                y = Dijkestra.dijkstra.sordes.get(Controller.query).getKey().y;//rnd.nextDouble() * 500;
            }
            else if(cell.getCellId() == "Destination")
            {
                x = Dijkestra.dijkstra.sordes.get(Controller.query).getValue().x;//rnd.nextDouble() * 500;
                y = Dijkestra.dijkstra.sordes.get(Controller.query).getValue().y;//rnd.nextDouble() * 500;
            }
            else
            {
                x = Dijkestra.dijkstra.nodes.get(Integer.parseInt(cell.getCellId())).x;//rnd.nextDouble() * 500;
                y = Dijkestra.dijkstra.nodes.get(Integer.parseInt(cell.getCellId())).y;//rnd.nextDouble() * 500;
            }
            x *= 50;
            y *= 50;
            cell.relocate(x, y);
        }

    }

}