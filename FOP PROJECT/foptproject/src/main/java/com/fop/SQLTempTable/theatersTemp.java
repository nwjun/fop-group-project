package com.fop.SQLTempTable;

import java.util.Properties;

public class theatersTemp {
    private Properties theaters;
    
    public theatersTemp(){
        Properties prop = new Properties();
        
        prop.put("theaterId", "1"); //String
        prop.put("theaterName", "Hall1"); //String
        prop.put("movieId", "00001");//String
        prop.put("seat", "{\"A\": {\"C1\": 1, \"C3\": 3, \"C5\": 5}, \"B\": {\"C1\": 1, \"C3\": 3, \"C5\": 5}}"); //JSON
        
        //seat has got problem, dk the operation for storing seat
        
        this.theaters = prop;
    }
    
    public String[] getTheaters(){
        String [] get = new String [4];
        
        get[0]=theaters.getProperty("theaterId");
        get[1]=theaters.getProperty("theaterName");
        get[2]=theaters.getProperty("movieId");
        get[3]=theaters.getProperty("seat");
        
        return get;
    }
}

//import com.fop.SQLTempTable.theatersTemp;
//
//public class Main{
//    public static void main(String[] args) {
//        theatersTemp x = new theatersTemp();
//        for (String x1: x.getTheaters()){ 
//            System.out.print(x1 + " ");
//        }
//    }
//}