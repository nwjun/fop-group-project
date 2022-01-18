package com.fop.Utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author WeiXin
 */
public class templateModifier {
    private String content = "";
    
    public String readHTML(String path) throws IOException{
        try {
            String str;
            BufferedReader in = new BufferedReader(new FileReader(path));
            while ((str = in.readLine())!= null) {
                content +=str;
            }
            in.close();
            return content;
        }
        catch (IOException e){
            e.printStackTrace();
            return "false";
        }
    }
}
