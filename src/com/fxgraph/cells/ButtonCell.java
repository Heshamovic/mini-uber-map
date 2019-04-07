package com.fxgraph.cells;

import javafx.scene.control.Button;

import com.fxgraph.graph.Cell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ButtonCell extends Cell {

    public ButtonCell(String id) {
        super(id);

        Button view = new Button(id);
        view.setStyle("-fx-background-color: Darkblue;-fx-text-fill: white;");
        Circle c = new Circle();
        c.setRadius(10000);
        view.setShape(c);
        view.setDisable(true);
        setView(view);

    }

}