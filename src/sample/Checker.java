package sample;

import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Vector;
import java.io.FileWriter;
public class Checker {
    public static Vector<String>lines = new Vector<String>();
    public static Vector<String>diff = new Vector<String>();

    Checker(String file, int fileNo) throws Exception{
        lines.clear();
        if(file == "Sample"){
            FileReader FR = new FileReader("Samples/SampleCases/output" + fileNo + ".txt");
            BufferedReader BR = new BufferedReader(FR);
            String s;
            int i = 0;
            do{
                s = BR.readLine();
                if(i%7 == 5 || i%7 == 6)
                {
                    i++;
                    continue;
                }
                lines.add(s);
                i++;
            }while(s != null);
        }
        else if(file == "medium"){
            FileReader FR = new FileReader("Samples/MediumCases/OLOutput.txt");
            BufferedReader BR = new BufferedReader(FR);
            String s;
            int i = 0;
            do{
                s = BR.readLine();
                if(i%7 == 5 || i%7 == 6)
                {
                    i++;
                    continue;
                }
                lines.add(s);
                i++;
            }while(s != null);
        }
        else{
            FileReader FR = new FileReader("Samples/LargeCases/SFOutput.txt");
            BufferedReader BR = new BufferedReader(FR);
            String s;
            int i = 0;
            do{
                s = BR.readLine();
                if(i%7 == 5 || i%7 == 6)
                {
                    i++;
                    continue;
                }
                lines.add(s);
                i++;
            }while(s != null);
        }
    }

    public static boolean check(Vector<String> v, ListView<String> timeL, String txt)throws Exception{
        boolean ret = true;
        diff.clear();
      /* try{
            String[] s = new String[5];
            FileWriter file = new FileWriter("Samples/sample7.txt", false);
            PrintWriter writer = new PrintWriter(file);
            int j = 0;
            for(int i = 0; i < v.size();i++)
            {
                writer.write(v.get(i) + "\n");
                if(i % 5 == 4 && i != 0&& j < timeL.getItems().size())
                {
                    s = new String[5];
                    s = timeL.getItems().get(j).split(" ");
                    writer.write(s[2] + " " + s[3]+ '\n' + '\n');
                    j++;
                }
            }
            writer.write(txt);
            writer.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        */
      try{
            for (int i = 0 ; i < v.size() ; i++) {
                if(v.get(i).length() != lines.get(i).length()){
                    diff.add("line number #" + i + ": " + lines.get(i) + " -> " + v.get(i));
                    ret = false;
                    continue;
                }
                for(int j = 0 ; j < lines.get(i).length() ; j++){
                    if(v.get(i).charAt(j) != lines.get(i).charAt(j)){
                        diff.add("line number #" + i + ": " + lines.get(i) + " -> " + v.get(i));
                        ret = false;
                        break;
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return ret;
    }
}
