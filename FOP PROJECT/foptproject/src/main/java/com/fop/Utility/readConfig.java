/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.Utility;

import java.io.File;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 
 * @author WeiXin
 */
public class readConfig {
    public Properties readconfigfile(){
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/com/fop/config/configuration.config")) {
            prop.load(fis);
        } 
        catch (FileNotFoundException ex){
            return prop;
        } 
        catch (IOException ex){
            return prop;
        }
        return prop;
    }
    
    public static String readSeatTemplate() throws FileNotFoundException{
        FileInputStream in = new FileInputStream("src/main/resources/com/fop/config/seatTemplate.txt");
        Scanner read = new Scanner(in);
        String json = read.nextLine();
        return json;
    }
    
    public static void updateSeatTemplate(String jsonString) throws FileNotFoundException{
        PrintWriter writer = new PrintWriter("src/main/resources/com/fop/config/seatTemplate.txt");
        writer.print(jsonString);
        writer.close();
    }
    
    public static void updateActualSeat(String jsonString) throws FileNotFoundException{
        PrintWriter writer = new PrintWriter("src/main/resources/com/fop/config/actualSeat.txt");
        writer.print(jsonString);
        writer.close();
    }
}
