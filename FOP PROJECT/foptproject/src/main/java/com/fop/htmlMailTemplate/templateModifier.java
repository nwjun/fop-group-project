/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.htmlMailTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class templateModifier {
    private String content = "";
    public String readHTML() throws IOException{
        try {
            String str;
            BufferedReader in = new BufferedReader(new FileReader("src\\main\\resources\\com\\fop\\htmlEmailTemplates\\bookingConfirmationTemplate.html"));
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
