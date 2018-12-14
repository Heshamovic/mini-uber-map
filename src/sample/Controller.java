package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.FileReader;

public class Controller {

    @FXML
    private ListView<String> timeList = new ListView<String>();

    @FXML
    private ListView<String> resultslist = new ListView<String>();

    public void runSampleCase1()throws Exception{
        timeList.getItems().clear();
        resultslist.getItems().clear();
        FileReader FR = new FileReader("Samples/SampleCases/map1.txt");
        FileReader FR2 = new FileReader("Samples/SampleCases/queries1.txt");
        DoubleEndDjikestra DEJ = new DoubleEndDjikestra();
        DEJ.build(FR);
        try{
            timeList.getItems().add(DEJ.query(FR2) + " ms");
            for (int i = 0 ; i < DEJ.LV.getItems().size() ; i++)
                resultslist.getItems().add(DEJ.LV.getItems().get(i));
            DEJ.LV.getItems().clear();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
