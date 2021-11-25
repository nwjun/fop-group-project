/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.SQLTempTable;

import java.util.Properties;

/**
 *
 * @author kuckn
 */
public class usercredentialsTemp {
    private Properties usercredentials;
    
    public usercredentialsTemp(){
        Properties prop = new Properties();
        
        // Properties acts like dictionary, call UserId -> get U20000
        prop.put("UserId", "U2000"); //String
        prop.put("username","Test"); //String 
        prop.put("password","testing12345"); //String /*SHA-256 encrypt*/
        prop.put("email","test@email.com"); //String
        prop.put("phoneNumber","0123456789"); //String
        prop.put("permission","4"); //int
        prop.put("linkedCards","null"); //Json
        
        this.usercredentials = prop;
    }
    
    public String[] getUserCredential(){
        String [] get = new String [7];
        
        get [0]= usercredentials.getProperty("UserId");
        get [1]= usercredentials.getProperty("username");
        get [2]= usercredentials.getProperty("password");
        get [3]= usercredentials.getProperty("email");
        get [4]= usercredentials.getProperty("phoneNumber");
        get [5]= usercredentials.getProperty("permission");
        get [6]= usercredentials.getProperty("linkedCards");
                
        return get;
    }
}

/*
package com.fop.SQLTempTable;

import com.fop.SQLTempTable.usercredentialsTemp;

public class Main {

    public static void main(String[] args) {
        usercredentialsTemp x = new usercredentialsTemp();
        
        for (String y1 : x.getUserCredential()) {
            System.out.print(y1 + " ");
        }
    }
    
}

*/