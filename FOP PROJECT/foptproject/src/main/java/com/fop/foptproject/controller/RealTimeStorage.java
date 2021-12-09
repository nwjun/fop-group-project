package com.fop.foptproject.controller;

import com.fop.Utility.JSONToolSets;
import com.fop.Utility.sqlConnect;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.*;

/**
 * @author WeiXin
 */
public class RealTimeStorage {
    private static sqlConnect sql = new sqlConnect();
    private static String userId;
    private static String userEmail;
    private static String userName;
    private static String phoneNumber;
    private static String permission;
    private static ArrayList<String> linkedCard;
    public static HashMap<String,String> MovieBooking = new HashMap<>();
    public static HashMap<String,Integer> FoodnBeverage = new HashMap<>();
    
    public static void updateMovieBooking(HashMap<String,String> input,boolean clear){
        if(!(clear)){
            RealTimeStorage.MovieBooking = input;
        }
        else{
            RealTimeStorage.MovieBooking = null;
        }
    }
    
    public static void updateFnB(HashMap<String,Integer> input,boolean clear){
        if(!(clear)){
            RealTimeStorage.FoodnBeverage = input;
        }
        else{
            RealTimeStorage.FoodnBeverage = null;
        }
    }
    
    // clear All
    public static void clearAll(){
        RealTimeStorage.userId = null;
        RealTimeStorage.userEmail = null;
        RealTimeStorage.userName = null;
        RealTimeStorage.phoneNumber = null;
        RealTimeStorage.permission = null;
        RealTimeStorage.linkedCard.clear();
        RealTimeStorage.MovieBooking.clear();
        RealTimeStorage.FoodnBeverage.clear();
        System.out.println("all cleared");
    }
    
    // setter for username, userId, userEmail, phoneNumber, permission, linkedCard
    public static void updateUserInfos(String userEmail){
        
        sqlConnect sql = new sqlConnect();
        HashMap<String,String> result = sql.queryUserCredentials(userEmail);
        
        // store string value
        RealTimeStorage.userEmail = userEmail;
        RealTimeStorage.userId = result.get("userId");
        RealTimeStorage.userName = result.get("username");
        RealTimeStorage.phoneNumber = result.get("phoneNumber");
        RealTimeStorage.permission = result.get("permission");
        // parse json
        String jsonString = result.get("linkedCard");
        RealTimeStorage.linkedCard = new JSONToolSets(jsonString).parseOneDArray("cardDetail");
        
        
    }
    
    // setter for linked cards
    public static void appendLinkedCards(String cardDetails){
        // add to real time database
        RealTimeStorage.linkedCard.add(cardDetails);
        
        // update remote database
        // convert to json object
        JSONArray jsonArray = new JSONArray();
        
        for(int i = 0 ; i < RealTimeStorage.linkedCard.size() ; i++){
            jsonArray.put(RealTimeStorage.linkedCard.get(i));
        }
        
        String jsonString = JSONToolSets.writeJSONString(jsonArray, "cardDetail");
        sql.updateLinkedCard(jsonString,userEmail);
    }
    
    // setter for linkedCards
    public static void removeLinkedCards(String cardDetails){
        // remove specified cards
       boolean isExist = RealTimeStorage.linkedCard.remove(cardDetails);
       if(isExist){
           JSONArray jsonArray = new JSONArray();
           
           for(int i = 0 ; i < RealTimeStorage.linkedCard.size() ; i++){
               jsonArray.put(RealTimeStorage.linkedCard.get(i));
           }
           
           String jsonString = JSONToolSets.writeJSONString(jsonArray, "cardDetail");
           sql.updateLinkedCard(jsonString,userEmail);
       }
       else{
           return;
       }
       
    }
    
    // getter for userId
    public static String getUserId(){
        return RealTimeStorage.userId;
    }
    
    // getter for userEmail
    public static String getUserEmail(){
        return RealTimeStorage.userEmail;
    }
    
    // getter for username
    public static String getUsername(){
        return RealTimeStorage.userName;
    }
    
    // getter for phonenumber
    public static String getPNumber(){
        return RealTimeStorage.phoneNumber;
    }
    
    // getter for permission
    public static String getPermission(){
        return RealTimeStorage.permission;
    }
    
    // getter for movieBooking
    public static HashMap<String,String> getMovieBooking(){
        return RealTimeStorage.MovieBooking;
    }
    
    // getter for FnB
    public static HashMap<String,Integer> getFnB(){
        return RealTimeStorage.FoodnBeverage;
    }
    
    
    
}

// reference for iterating thru the result set

//        HashMap<String,HashMap<String,ArrayList<String>>> resultset = json.parseAllShowTimes();
//        
//        int j = 0;
//        for(int i = 0; i < 7 ; i++){
//            j = 0;
//            System.out.println();
//            while(true){
//                try{
//                    System.out.printf("%s | %s \n",resultset.get("hall").get(Integer.toString(i)).get(j),resultset.get("showtime").get(Integer.toString(i)).get(j));
//                    j++;
//                }
//                catch(Exception e){
//                    break;
//                }
//            }
//        }
//        
//        ArrayList<String> casts = json.parseOneDArray("Cast");
//        int i = 0;
//        while(true){
//            try{
//                System.out.println(casts.get(i));
//                i++;
//            }
//            catch(Exception e){
//                break;
//            }
//        }