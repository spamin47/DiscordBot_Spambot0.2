package Discord_bot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MyFileReader {

    private File file;
    private BufferedReader br;

    public MyFileReader(String path){
        try{
            file = new File(path);
            if(file.exists()){
                br = new BufferedReader(new FileReader(file));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getText()  {
        String txt = "";
        try{
            do{
                txt = br.readLine();
            }while(br.readLine()!=null);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Text read: " + txt);
        return txt;
    }

    public void closeFile(){
        try{
            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
