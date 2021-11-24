package com.fop.SQLTempTable;

import java.util.Properties;

public class posTemp {
    private Properties pos;
    
    public posTemp(){
        Properties prop = new Properties();
        
        prop.put("posterID", "1"); //String
        prop.put("poster", "src\\main\\resources\\com\\fop\\foptproject\\assets\\movies\\anita.jpg"); //String
        
        this.pos=prop;
    }
    
    public String[] getPos(){
        String [] get = new String [2];
        
        get[0]= pos.getProperty("posterID") ; //String
        get[1]= pos.getProperty("poster") ; 
        
        return get;
    }
}


//import com.fop.SQLTempTable.posTemp;
//
//public class Main{
//    public static void main(String[] args) {
//        posTemp x = new posTemp();
//        
//        for (String x1: x.getPos()){
//            System.out.print(x1 + " ");
//        }
//    }
//    
//}