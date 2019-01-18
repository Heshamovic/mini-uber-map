package sample;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.fxgraph.graph.CellType;

import com.fxgraph.graph.CellType;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.Model;
import com.fxgraph.Layout.base.Layout;
import com.fxgraph.Layout.random.RandomLayout;

import java.awt.*;

public class Simulate {
    Graph graph = new Graph();

    public void display(){
        Stage window = new Stage();
        window.setTitle("Map");
        window.setMinWidth(450);
        window.setMinHeight(450);

        BorderPane root = new BorderPane();

        root.setCenter(graph.getScrollPane());

        Scene scene = new Scene(root, 1000, 768,Color.RED);
       // scene.setFill(Color.WHITE);

        addGraphComponents();
        Layout layout = new RandomLayout(graph);
        layout.execute();

        window.setScene(scene);
        window.showAndWait();
    }
    private void addGraphComponents() {

        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("Source", CellType.BUTTON);
        model.addCell("Destination", CellType.BUTTON);
        for(int i = 0 ; i < Dijkestra.dijkstra.num_nodes;i++)
        {
            String s = Integer.toString(Dijkestra.dijkstra.nodes.get(i).id);
            model.addCell(s, CellType.BUTTON);
        }
        for(int i = 0  ; i < Dijkestra.dijkstra.num_nodes ; i++) {
            for (int j = 0; j < Dijkestra.dijkstra.nodes.elementAt(i).child.size(); j++)
                model.addEdge(Integer.toString(Dijkestra.dijkstra.nodes.get(i).id), Integer.toString(Dijkestra.dijkstra.nodes.get(i).child.get(j).getKey()), Color.BLACK);
        }
        for(int i = Dijkestra.dijkstra.pathes.get(Controller.query).size() - 1 ; i > 0; i--)
        {
            String a = Integer.toString(Dijkestra.dijkstra.pathes.get(Controller.query).get(i));
            String b = Integer.toString(Dijkestra.dijkstra.pathes.get(Controller.query).get(i - 1));
            if(i == 1)
                b = "Destination";
            model.addEdge(a, b, Color.GREEN.brighter().brighter().brighter());
        }
        model.addEdge("Source", Integer.toString(Dijkestra.dijkstra.pathes.get(Controller.query).lastElement()), Color.GREEN.brighter().brighter().brighter());
        graph.endUpdate();


    }
}
