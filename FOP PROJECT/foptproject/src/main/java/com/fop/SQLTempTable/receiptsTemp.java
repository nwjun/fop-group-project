package com.fop.SQLTempTable;

import java.util.Properties;

public class receiptsTemp {
    private Properties receipts;
    
    public receiptsTemp(){
        Properties prop = new Properties();
        
        prop.put("receiptNumber","00001");
        prop.put("userId","U00000");
        prop.put("purchasedItem","{\"Food\": \"S00001\", \"Ticket\": \"TS00001\"}");
        prop.put("seatNumber","A1");
        prop.put("theaterId","1");
        prop.put("showDate","2021-12-17");
        prop.put("showTime","20:00:00");
        
        this.receipts=prop;
    }
    
    public String [] getReceipts(){
        String get [] = new String [7];
        
        get[0] = receipts.getProperty("receiptNumber");
        get[1] = receipts.getProperty("userId");
        get[2] = receipts.getProperty("purchasedItem");
        get[3] = receipts.getProperty("seatNumber");
        get[4] = receipts.getProperty("theaterId");
        get[5] = receipts.getProperty("showDate");
        get[6] = receipts.getProperty("showTime");
        
        
        return get;
    }
    
}


//import com.fop.SQLTempTable.receiptsTemp;
//
//public class Main{
//    public static void main(String[] args) {
//        receiptsTemp x = new receiptsTemp();
//                for (String x1: x.getReceipts()){
//            System.out.print(x1 + " ");
//        }
//        
//    }
//}