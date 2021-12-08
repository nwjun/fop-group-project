package com.fop.foptproject.controller;

import java.util.HashMap;

/**
 * @author WeiXin
 */
public class RealTimeStorage {
    private static String userId;
    private static String userEmail;
    private static String phoneNumber;
    private static String[] linkedCard;
    private static HashMap<String,String> MovieBooking;
    private static HashMap<String,String> FoodnBeverage;
    
    public static void updateMovieBooking(HashMap<String,String> input,boolean clear){
        if(!(clear)){
            RealTimeStorage.MovieBooking = input;
        }
        else{
            RealTimeStorage.MovieBooking = null;
        }
    }
    
    public static void updateFnB(HashMap<String,String> input,boolean clear){
        if(!(clear)){
            RealTimeStorage.FoodnBeverage = input;
        }
        else{
            RealTimeStorage.FoodnBeverage = null;
        }
    }
    
    public static void updateUserCredentials(String userEmail){
        
        
        RealTimeStorage.userEmail = userEmail;
        
    }
}
