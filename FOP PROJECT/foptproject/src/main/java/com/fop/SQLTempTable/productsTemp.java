package com.fop.SQLTempTable;

import java.util.Properties;

public class productsTemp {
    private Properties products;
    
    public productsTemp(){
        Properties prop = new Properties();
        
        prop.put("productId", "S000001"); //String
        prop.put("price", "9.90"); //Decimal
        prop.put("productDescription", "Limited-Time Combo Menu 1 - Popcorn, Hotdog or Hash or Karipap, add-on soft drink"); //String
        
        this.products=prop;
    
        //dont forget to add product ID for ticket
    }
    
    public String[] getProducts(){
        String [] get = new String [3];
        
        get[0]=products.getProperty("productId");
        get[1]=products.getProperty("price");
        get[2]=products.getProperty("productDescription");
        
        return get;
    }
}

//import com.fop.SQLTempTable.productsTemp;
//
//public class Main{
//    public static void main(String[] args) {
//        productsTemp x = new productsTemp();
//        for (String x1: x.getProducts()){
//            System.out.print(x1 + " ");
//        }
//    }
//}