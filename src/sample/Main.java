package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.Dijkestra.dijkstra;

import java.time.LocalDateTime;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Mini uber map");
        primaryStage.setScene(new Scene(root, 1050, 650));
        primaryStage.getIcons().add(new Image("/sample/route_points-512.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
