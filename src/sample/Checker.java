package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

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

    public static boolean check(Vector<String> v){
        boolean ret = true;
        diff.clear();
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
            System.out.println(e+"zebyyyyy");
        }
        return ret;
    }
}
