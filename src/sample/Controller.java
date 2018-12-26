package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.io.FileReader;
import java.util.Vector;

public class Controller {

    @FXML
    private Circle statusBall;
    @FXML
    private ListView<String> timeList = new ListView<String>();
    @FXML
    private ListView<String> resultslist = new ListView<String>();
    @FXML
    private Label allTimelbl;
    @FXML
    private Label maxTimelbl;
    @FXML
    private Label avgTimelbl;
    @FXML
    private ComboBox sampleBtn;
    @FXML
    private Label status;
    @FXML
    private Button showDiff;
    @FXML
    private Label runlbl;
    @FXML
    public void initialize() {
        status.setText("Not tests yet!");
        statusBall.setFill(Color.GHOSTWHITE);
        showDiff.setDisable(true);
        sampleBtn.getItems().addAll("Sample Case #1", "Sample Case #2", "Sample Case #3", "Sample Case #4", "Sample Case #5");
    }
    public static String timeAll;
    public static Vector<String>vec = new Vector<>();
    public static ListView<String> resultsTMP = new ListView<>();
    public static boolean tog = true, showTog = false, bonus = false;
    public void toggleRadio(){
        tog = !tog;
        bonus = false;
    }
    public void toggleBonusRadio(){
        bonus = true;
    }
    public void runSampleCase1()throws Exception
    {
        long tmp = System.currentTimeMillis();
        resultsTMP.getItems().clear();
        showDiff.setText("Show Differences");
        if(sampleBtn.getValue() == "Sample Case #1")
            runSampleCase(1);
        else if(sampleBtn.getValue() == "Sample Case #2")
            runSampleCase(2);
        else if(sampleBtn.getValue() == "Sample Case #3")
            runSampleCase(3);
        else if(sampleBtn.getValue() == "Sample Case #4")
            runSampleCase(4);
        else if(sampleBtn.getValue() == "Sample Case #5")
            runSampleCase(5);
        timeAll = Long.toString(System.currentTimeMillis() - tmp);
    }
    public void runSampleCase(int fileNo)throws Exception
    {
        if(bonus)
        {
            runSampleCaseBonus(fileNo);
            return;
        }
        if(tog)
        {
            runSampleCaseNormal(fileNo);
            return;
        }
        vec.clear();
        timeList.getItems().clear();
        resultslist.getItems().clear();
        FileReader FR = new FileReader("Samples/SampleCases/map" + fileNo + ".txt");
        FileReader FR2 = new FileReader("Samples/SampleCases/queries" + fileNo + ".txt");
        DoubleEndDjikestra DEJ = new DoubleEndDjikestra();
        DEJ.build(FR);
        DEJ.lines.clear();
        DEJ.timeL.getItems().clear();
        DEJ.LV.getItems().clear();
        long mx = Long.MIN_VALUE, sum = 0;
        try{
            allTimelbl.setText(DEJ.query(FR2) + " ms");
            for(int i = 0 ; i < DEJ.timeL.getItems().size() ; i++){
                timeList.getItems().add("Test #" + (i + 1) + " " + DEJ.timeL.getItems().get(i) + " ms");
                mx = Long.max(mx, Long.parseLong(DEJ.timeL.getItems().get(i)));
                sum += Long.parseLong(DEJ.timeL.getItems().get(i));
            }
            sum /= DEJ.timeL.getItems().size();
            avgTimelbl.setText(sum + " ms");
            maxTimelbl.setText(mx + " ms");
            for (int i = 0 ; i < DEJ.LV.getItems().size() ; i++)
            {
                resultslist.getItems().add(DEJ.LV.getItems().get(i));
                resultsTMP.getItems().add(DEJ.LV.getItems().get(i));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        Checker check = new Checker("Sample", fileNo);
        if(check.check(DEJ.lines, timeList, allTimelbl.getText())){
            status.setText("All Tests Passed");
            statusBall.setFill(Color.web("#64f594"));
            showDiff.setDisable(true);
        }
        else{
            status.setText("There're some differences");
            statusBall.setFill(Color.RED);
            showDiff.setDisable(false);
            vec = check.diff;
        }
    }
    public void runMediumCases()throws Exception
    {
        long tmp = System.currentTimeMillis();
        resultsTMP.getItems().clear();
        showDiff.setText("Show Differences");
        if(bonus)
        {
            runMediumCaseBonus(0);
            timeAll = Long.toString(System.currentTimeMillis() - tmp);
            return;
        }
        if(tog)
        {
            runMediumCaseNormal(0);
            timeAll = Long.toString(System.currentTimeMillis() - tmp);
            return;
        }
        vec.clear();
        runlbl.setVisible(true);
        timeList.getItems().clear();
        resultslist.getItems().clear();
        FileReader FR = new FileReader("Samples/MediumCases/OLMap.txt");
        FileReader FR2 = new FileReader("Samples/MediumCases/OLQueries.txt");
        DoubleEndDjikestra DEJ = new DoubleEndDjikestra();
        DEJ.build(FR);
        DEJ.lines.clear();
        DEJ.timeL.getItems().clear();
        DEJ.LV.getItems().clear();
        long mx = Long.MIN_VALUE, sum = 0;
        try{
            allTimelbl.setText(DEJ.query(FR2) + " ms");
            for(int i = 0 ; i < DEJ.timeL.getItems().size() ; i++){
                timeList.getItems().add("Test #" + (i + 1) + ": " + DEJ.timeL.getItems().get(i) + " ms");
                mx = Long.max(mx, Long.parseLong(DEJ.timeL.getItems().get(i)));
                sum += Long.parseLong(DEJ.timeL.getItems().get(i));
            }
            sum /= DEJ.timeL.getItems().size();
            avgTimelbl.setText(sum + " ms");
            maxTimelbl.setText(mx + " ms");
            for (int i = 0 ; i < DEJ.LV.getItems().size() ; i++)
            {
                resultslist.getItems().add(DEJ.LV.getItems().get(i));
                resultsTMP.getItems().add(DEJ.LV.getItems().get(i));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        runlbl.setVisible(false);
        Checker check = new Checker("medium", 0);
        if(check.check(DEJ.lines, timeList, allTimelbl.getText())){
            status.setText("All Tests Passed");
            statusBall.setFill(Color.web("#64f594"));
            showDiff.setDisable(true);
        }
        else{
            status.setText("There're some differences");
            statusBall.setFill(Color.RED);
            showDiff.setDisable(false);
            vec = check.diff;
        }
        timeAll = Long.toString(System.currentTimeMillis() - tmp);
    }
    public void runLargeCases()throws Exception
    {
        long tmp = System.currentTimeMillis();
        resultsTMP.getItems().clear();
        showDiff.setText("Show Differences");
        if(bonus)
        {
            runLargeCaseBonus(0);
            return;
        }
        if(tog)
        {
            runLargeCaseNormal(0);
            return;
        }
        vec.clear();
        timeList.getItems().clear();
        resultslist.getItems().clear();
        FileReader FR = new FileReader("Samples/LargeCases/SFMap.txt");
        FileReader FR2 = new FileReader("Samples/LargeCases/SFQueries.txt");
        DoubleEndDjikestra DEJ = new DoubleEndDjikestra();
        DEJ.build(FR);
        DEJ.lines.clear();
        DEJ.timeL.getItems().clear();
        DEJ.LV.getItems().clear();
        long mx = Long.MIN_VALUE, sum = 0;
        try{
            allTimelbl.setText(DEJ.query(FR2) + " ms");
            for(int i = 0 ; i < DEJ.timeL.getItems().size() ; i++){
                timeList.getItems().add("Test #" + (i + 1) + " " + DEJ.timeL.getItems().get(i) + " ms");
                mx = Long.max(mx, Long.parseLong(DEJ.timeL.getItems().get(i)));
                sum += Long.parseLong(DEJ.timeL.getItems().get(i));
            }
            sum /= DEJ.timeL.getItems().size();
            avgTimelbl.setText(sum + " ms");
            maxTimelbl.setText(mx + " ms");
            for (int i = 0 ; i < DEJ.LV.getItems().size() ; i++)
            {
                resultslist.getItems().add(DEJ.LV.getItems().get(i));
                resultsTMP.getItems().add(DEJ.LV.getItems().get(i));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        Checker check = new Checker("large", 0);
        if(check.check(DEJ.lines, timeList, allTimelbl.getText())){
            status.setText("All Tests Passed");
            statusBall.setFill(Color.web("#64f594"));
            showDiff.setDisable(true);
        }
        else{
            status.setText("There're some differences");
            statusBall.setFill(Color.RED);
            showDiff.setDisable(false);
            vec = check.diff;
        }
        timeAll = Long.toString(System.currentTimeMillis() - tmp);
    }
    public void showDiffrences()
    {
        resultslist.getItems().clear();
        if(!showTog)
        {
            for (String s : vec) {
                resultslist.getItems().add(s);
            }
            showDiff.setText("Return to tests");
        }
        else
        {
            for(int i = 0 ; i < resultsTMP.getItems().size() ;i++){
                resultslist.getItems().add(resultsTMP.getItems().get(i));
            }
            showDiff.setText("Show Differences");
        }
        showTog = !showTog;
    }
    //Normal:
    public void runSampleCaseNormal(int fileNo)throws Exception
    {
        vec.clear();
        timeList.getItems().clear();
        resultslist.getItems().clear();
        FileReader FR = new FileReader("Samples/SampleCases/map" + fileNo + ".txt");
        FileReader FR2 = new FileReader("Samples/SampleCases/queries" + fileNo + ".txt");

        Dijkestra.dijkstra DEJ = new Dijkestra.dijkstra(FR);
        DEJ.getinputnode();
        DEJ.getinputedges();

        DEJ.lines.clear();
        DEJ.timeL.getItems().clear();
        DEJ.LV.getItems().clear();


        long mx = Long.MIN_VALUE, sum = 0;
        try{
            allTimelbl.setText(DEJ.solve(FR2) + " ms");
            for(int i = 0 ; i < DEJ.timeL.getItems().size() ; i++){
                timeList.getItems().add("Test #" + (i + 1) + ": " + DEJ.timeL.getItems().get(i) + " ms");
                mx = Long.max(mx, Long.parseLong(DEJ.timeL.getItems().get(i)));
                sum += Long.parseLong(DEJ.timeL.getItems().get(i));
            }
            sum /= DEJ.timeL.getItems().size();
            avgTimelbl.setText(sum + " ms");
            maxTimelbl.setText(mx + " ms");
            for (int i = 0 ; i < DEJ.LV.getItems().size() ; i++)
            {
                resultslist.getItems().add(DEJ.LV.getItems().get(i));
                resultsTMP.getItems().add(DEJ.LV.getItems().get(i));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        Checker check = new Checker("Sample", fileNo);
        if(check.check(DEJ.lines, timeList, allTimelbl.getText())){
            status.setText("All Tests Passed");
            statusBall.setFill(Color.web("#64f594"));
            showDiff.setDisable(true);
        }
        else{
            status.setText("There're some differences");
            statusBall.setFill(Color.RED);
            showDiff.setDisable(false);
            vec = check.diff;
        }
    }
    public void runMediumCaseNormal(int fileNo)throws Exception
    {
        vec.clear();
        timeList.getItems().clear();
        resultslist.getItems().clear();
        FileReader FR = new FileReader("Samples/MediumCases/OLMap.txt");
        FileReader FR2 = new FileReader("Samples/MediumCases/OLQueries.txt");

        Dijkestra.dijkstra DEJ = new Dijkestra.dijkstra(FR);
        DEJ.getinputnode();
        DEJ.getinputedges();

        DEJ.lines.clear();
        DEJ.timeL.getItems().clear();
        DEJ.LV.getItems().clear();


        long mx = Long.MIN_VALUE, sum = 0;
        try{
            allTimelbl.setText(DEJ.solve(FR2) + " ms");
            for(int i = 0 ; i < DEJ.timeL.getItems().size() ; i++){
                timeList.getItems().add("Test #" + (i + 1) + ": " + DEJ.timeL.getItems().get(i) + " ms");
                mx = Long.max(mx, Long.parseLong(DEJ.timeL.getItems().get(i)));
                sum += Long.parseLong(DEJ.timeL.getItems().get(i));
            }
            sum /= DEJ.timeL.getItems().size();
            avgTimelbl.setText(sum + " ms");
            maxTimelbl.setText(mx + " ms");
            for (int i = 0 ; i < DEJ.LV.getItems().size() ; i++)
            {
                resultslist.getItems().add(DEJ.LV.getItems().get(i));
                resultsTMP.getItems().add(DEJ.LV.getItems().get(i));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        Checker check = new Checker("medium", fileNo);
        if(check.check(DEJ.lines, timeList, allTimelbl.getText())){
            status.setText("All Tests Passed");
            statusBall.setFill(Color.web("#64f594"));
            showDiff.setDisable(true);
        }
        else{
            status.setText("There're some differences");
            statusBall.setFill(Color.RED);
            showDiff.setDisable(false);
            vec = check.diff;
        }
    }
    public void runLargeCaseNormal(int fileNo)throws Exception
    {
        vec.clear();
        timeList.getItems().clear();
        resultslist.getItems().clear();
        FileReader FR = new FileReader("Samples/LargeCases/SFMap.txt");
        FileReader FR2 = new FileReader("Samples/LargeCases/SFQueries.txt");

        Dijkestra.dijkstra DEJ = new Dijkestra.dijkstra(FR);
        DEJ.getinputnode();
        DEJ.getinputedges();

        DEJ.lines.clear();
        DEJ.timeL.getItems().clear();
        DEJ.LV.getItems().clear();


        long mx = Long.MIN_VALUE, sum = 0;
        try{
            allTimelbl.setText(DEJ.solve(FR2) + " ms");
            for(int i = 0 ; i < DEJ.timeL.getItems().size() ; i++){
                timeList.getItems().add("Test #" + (i + 1) + ": " + DEJ.timeL.getItems().get(i) + " ms");
                mx = Long.max(mx, Long.parseLong(DEJ.timeL.getItems().get(i)));
                sum += Long.parseLong(DEJ.timeL.getItems().get(i));
            }
            sum /= DEJ.timeL.getItems().size();
            avgTimelbl.setText(sum + " ms");
            maxTimelbl.setText(mx + " ms");
            for (int i = 0 ; i < DEJ.LV.getItems().size() ; i++)
            {
                resultslist.getItems().add(DEJ.LV.getItems().get(i));
                resultsTMP.getItems().add(DEJ.LV.getItems().get(i));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        Checker check = new Checker("large", fileNo);
        if(check.check(DEJ.lines, timeList, allTimelbl.getText())){
            status.setText("All Tests Passed");
            statusBall.setFill(Color.web("#64f594"));
            showDiff.setDisable(true);
        }
        else{
            status.setText("There're some differences");
            statusBall.setFill(Color.RED);
            showDiff.setDisable(false);
            vec = check.diff;
        }
    }

    //Bonus:
    public void runSampleCaseBonus(int fileNo)throws Exception
    {
        vec.clear();
        timeList.getItems().clear();
        resultslist.getItems().clear();
        FileReader FR = new FileReader("BonusSamples/SampleCases/map1B.txt");
        FileReader FR2 = new FileReader("BonusSamples/SampleCases/queries1.txt");

        DijkestraIntervals.dijkestraIntervals DEJ = new DijkestraIntervals.dijkestraIntervals(FR);
        DEJ.getinputnode();
        DEJ.getinputedges();

        DEJ.lines.clear();
        DEJ.timeL.getItems().clear();
        DEJ.LV.getItems().clear();


        long mx = Long.MIN_VALUE, sum = 0;
        try{
            allTimelbl.setText(DEJ.solve(FR2) + " ms");
            for(int i = 0 ; i < DEJ.timeL.getItems().size() ; i++){
                timeList.getItems().add("Test #" + (i + 1) + ": " + DEJ.timeL.getItems().get(i) + " ms");
                mx = Long.max(mx, Long.parseLong(DEJ.timeL.getItems().get(i)));
                sum += Long.parseLong(DEJ.timeL.getItems().get(i));
            }
            sum /= DEJ.timeL.getItems().size();
            avgTimelbl.setText(sum + " ms");
            maxTimelbl.setText(mx + " ms");
            for (int i = 0 ; i < DEJ.LV.getItems().size() ; i++)
            {
                resultslist.getItems().add(DEJ.LV.getItems().get(i));
                resultsTMP.getItems().add(DEJ.LV.getItems().get(i));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        Checker check = new Checker("SampleB", fileNo);
        if(check.check(DEJ.lines, timeList, allTimelbl.getText())){
            status.setText("All Tests Passed");
            statusBall.setFill(Color.web("#64f594"));
            showDiff.setDisable(true);
        }
        else{
            status.setText("There're some differences");
            statusBall.setFill(Color.RED);
            showDiff.setDisable(false);
            vec = check.diff;
        }
    }
    public void runMediumCaseBonus(int fileNo)throws Exception
    {
        vec.clear();
        timeList.getItems().clear();
        resultslist.getItems().clear();
        FileReader FR = new FileReader("BonusSamples/MediumCases/OLMapB.txt");
        FileReader FR2 = new FileReader("BonusSamples/MediumCases/OLQueries.txt");

        DijkestraIntervals.dijkestraIntervals DEJ = new DijkestraIntervals.dijkestraIntervals(FR);
        DEJ.getinputnode();
        DEJ.getinputedges();

        DEJ.lines.clear();
        DEJ.timeL.getItems().clear();
        DEJ.LV.getItems().clear();


        long mx = Long.MIN_VALUE, sum = 0;
        try{
            allTimelbl.setText(DEJ.solve(FR2) + " ms");
            for(int i = 0 ; i < DEJ.timeL.getItems().size() ; i++){
                timeList.getItems().add("Test #" + (i + 1) + ": " + DEJ.timeL.getItems().get(i) + " ms");
                mx = Long.max(mx, Long.parseLong(DEJ.timeL.getItems().get(i)));
                sum += Long.parseLong(DEJ.timeL.getItems().get(i));
            }
            sum /= DEJ.timeL.getItems().size();
            avgTimelbl.setText(sum + " ms");
            maxTimelbl.setText(mx + " ms");
            for (int i = 0 ; i < DEJ.LV.getItems().size() ; i++)
            {
                resultslist.getItems().add(DEJ.LV.getItems().get(i));
                resultsTMP.getItems().add(DEJ.LV.getItems().get(i));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        Checker check = new Checker("mediumB", fileNo);
        if(check.check(DEJ.lines, timeList, allTimelbl.getText())){
            status.setText("All Tests Passed");
            statusBall.setFill(Color.web("#64f594"));
            showDiff.setDisable(true);
        }
        else{
            status.setText("There're some differences");
            statusBall.setFill(Color.RED);
            showDiff.setDisable(false);
            vec = check.diff;
        }
    }
    public void runLargeCaseBonus(int fileNo)throws Exception
    {
        vec.clear();
        timeList.getItems().clear();
        resultslist.getItems().clear();
        FileReader FR = new FileReader("BonusSamples/LargeCases/SFMapB.txt");
        FileReader FR2 = new FileReader("BonusSamples/LargeCases/SFQueries.txt");

        DijkestraIntervals.dijkestraIntervals DEJ = new DijkestraIntervals.dijkestraIntervals(FR);
        DEJ.getinputnode();
        DEJ.getinputedges();

        DEJ.lines.clear();
        DEJ.timeL.getItems().clear();
        DEJ.LV.getItems().clear();


        long mx = Long.MIN_VALUE, sum = 0;
        try{
            allTimelbl.setText(DEJ.solve(FR2) + " ms");
            for(int i = 0 ; i < DEJ.timeL.getItems().size() ; i++){
                timeList.getItems().add("Test #" + (i + 1) + ": " + DEJ.timeL.getItems().get(i) + " ms");
                mx = Long.max(mx, Long.parseLong(DEJ.timeL.getItems().get(i)));
                sum += Long.parseLong(DEJ.timeL.getItems().get(i));
            }
            sum /= DEJ.timeL.getItems().size();
            avgTimelbl.setText(sum + " ms");
            maxTimelbl.setText(mx + " ms");
            for (int i = 0 ; i < DEJ.LV.getItems().size() ; i++)
            {
                resultslist.getItems().add(DEJ.LV.getItems().get(i));
                resultsTMP.getItems().add(DEJ.LV.getItems().get(i));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        Checker check = new Checker("LargeB", fileNo);
        if(check.check(DEJ.lines, timeList, allTimelbl.getText())){
            status.setText("All Tests Passed");
            statusBall.setFill(Color.web("#64f594"));
            showDiff.setDisable(true);
        }
        else{
            status.setText("There're some differences");
            statusBall.setFill(Color.RED);
            showDiff.setDisable(false);
            vec = check.diff;
        }
    }
}
