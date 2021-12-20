/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.Utility;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    
}
