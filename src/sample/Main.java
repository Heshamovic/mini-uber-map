package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Mini uber map");
        primaryStage.setScene(new Scene(root, 750, 550));
        primaryStage.show();
        DoubleEndDjikestra DEJ = new DoubleEndDjikestra();
       // DEJ.build();
        //System.out.println(DEJ.query() + " ms");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
