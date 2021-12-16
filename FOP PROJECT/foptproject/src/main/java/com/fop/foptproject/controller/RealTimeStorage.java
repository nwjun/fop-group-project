package com.fop.foptproject.controller;

import com.fop.Utility.JSONToolSets;
import com.fop.Utility.sqlConnect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.json.*;

/**
 * @author WeiXin
 */
public class RealTimeStorage {
    private static sqlConnect sql = new sqlConnect();
    private static boolean isLogin = false;
    private static final String[] SLOTS = {"10.00AM","03.30PM","08.00PM","01.00PM","11.00PM"}; 
    private static int alteringDay;
    private static String userId;
    private static String userEmail;
    private static String userName;
    private static String phoneNumber;
    private static String permission = "0";
    private static ArrayList<String> linkedCard;
    private static ArrayList<String[]> linkedCardtemp  = new ArrayList<>();
    private static HashMap<String, ArrayList<String>> movieDetails;
    private static HashMap<String,ArrayList<String>> landingFoodPoster;
    private static String lookingAtMovie;
    public static HashMap<String,Object> MovieBooking = new HashMap<>();
    public static HashMap<String,Integer> FoodnBeverage = new HashMap<>();
    private static ArrayList<String[]> linkedCard2D;
    private static ArrayList<String[]> selectedSeats = new ArrayList<>();
    private static int[] ticketType = new int[3]; // no of ticket for adult, student, OKU
    
    // setter for movie booking
    public static void updateMovieBooking(HashMap<String,Object> input,boolean clear){
        if(!(clear)){
            RealTimeStorage.MovieBooking = input;
        }
        else{
            RealTimeStorage.MovieBooking.clear();
        }
    }
    
    // setter for food and beverage
    public static void updateFnB(HashMap<String,Integer> input,boolean clear){
        if(!(clear)){
            RealTimeStorage.FoodnBeverage = input;
        }
        else{
            RealTimeStorage.FoodnBeverage.clear();
        }
    }
    
    // setter for movie booking
    /**
     * This method accepts an object no matter it is a multiple values or single value for each key
     * TypeCast is needed to use the data stored in the HashMap
     * @param key can only be
     * 1. Selected MovieId, a String
     * 2. Selected MovieName, a String
     * 3. Selected Cinema Name, a String
     * 4. Selected Date, a String
     * 5. Selected Theater type, a String
     * 6. Selected ShowTime, a String
     * 7. Selected Ticket Type and its quantity (even index for ticket type odd index for ticket quantity), an ArrayList
     * 8. Selected Seats, an ArrayList
     */     
    public static void updateMovieBookingByKey(String key,ArrayList<String> value){
        if(RealTimeStorage.MovieBooking.containsKey(key)){
            RealTimeStorage.MovieBooking.replace(key,value);
        }
        else{
            RealTimeStorage.MovieBooking.put(key, value);
        }
    }
    
    // setter for movie booking
    /**
     * This method accepts an object no matter it is a multiple values or single value for each key
     * TypeCast is needed to use the data stored in the HashMap
     * @param key can only be
     * 1. Selected MovieId, a String
     * 2. Selected MovieName, a String
     * 3. Selected Cinema Name, a String
     * 4. Selected Date, a String
     * 5. Selected Theater type, a String
     * 6. Selected ShowTime, a String
     * 7. Selected Ticket Type and its quantity (even index for ticket type odd index for ticket quantity), an ArrayList
     * 8. Selected Seats, an ArrayList
     */    
    public static void updateMovieBookingByKey(String key,String value){
           
        if(RealTimeStorage.MovieBooking.containsKey(key)){
            RealTimeStorage.MovieBooking.replace(key,value);
        }
        else{
            RealTimeStorage.MovieBooking.put(key, value);
        }
    }
    
    // clear All
    public static void clearAll(){
        RealTimeStorage.userId = null;
        RealTimeStorage.userEmail = null;
        RealTimeStorage.userName = null;
        RealTimeStorage.phoneNumber = null;
        RealTimeStorage.permission = "0";
        RealTimeStorage.lookingAtMovie = null;
        RealTimeStorage.isLogin = false;
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
        //update login status
        RealTimeStorage.isLogin = true;
        // parse json
        String jsonString = result.get("linkedCard");
        RealTimeStorage.linkedCard = new JSONToolSets(jsonString).parseOneDArray("cardDetails"); 
        
    }
    
    // setter for linked cards
    public static void appendLinkedCards(){
        // update remote database
        // convert to json object
        JSONArray jsonArray = new JSONArray();
        
        for(int i = 0 ; i < RealTimeStorage.linkedCard.size() ; i++){
            jsonArray.put(RealTimeStorage.linkedCard.get(i));
        }
        
        String jsonString = JSONToolSets.writeJSONString(jsonArray, "cardDetails");
        sql.updateLinkedCard(jsonString,userEmail);
    }
    
    
    
    public static void setAlteringDay(int day){
        RealTimeStorage.alteringDay = day;
    }
    
    public static void setLookingAt(String Id){
        RealTimeStorage.lookingAtMovie = Id;
    }
    
    public static void setAllMovies(){
        RealTimeStorage.movieDetails = sql.queryAllMovie();
    }
    
    public static void setAllLandingFood(){
        RealTimeStorage.landingFoodPoster = sql.queryLandingFood("combo",4);
    }
    
    public static String getSlot(int i){
        return RealTimeStorage.SLOTS[i];
    }
    
    public static String[] getSlots(int i){
        String[] ranged = new String[i];
        for(int j = 0 ; j < i ; j++){
            ranged[j] = RealTimeStorage.SLOTS[j];
        }
        return ranged;
    }
    
    public static String[] getAllSlots(){
        return RealTimeStorage.SLOTS;
    }
    
    public static String getTime(int i){
        return RealTimeStorage.getMovieDetail("time").get(RealTimeStorage.getMovieDetail("movieId").indexOf(RealTimeStorage.lookingAtMovie)).split(", ")[i];
    }
    
    public static int getAlteringDay(){
        return RealTimeStorage.alteringDay;
    }
    
    public static String getLookingAt(){
        return RealTimeStorage.lookingAtMovie;
    }
    
    public static HashMap<String,ArrayList<String>> getAllLandingFood(){
        return RealTimeStorage.landingFoodPoster;
    }
    
    public static ArrayList<String> getFoodDetail(String key){
        return RealTimeStorage.landingFoodPoster.get(key);
    }
    
    public static HashMap<String,ArrayList<String>> getAllMovies(){
        return RealTimeStorage.movieDetails;
    }
    
    public static ArrayList<String> getMovieDetail(String key){
        return RealTimeStorage.movieDetails.get(key);
    }
    
    // getter for userId
    public static String getUserId(){
        if (RealTimeStorage.userId == null){
            return "U123";
        }
        return RealTimeStorage.userId;
    }
    
    // getter for userEmail
    public static String getUserEmail(){
        if (RealTimeStorage.userEmail == null){
            return "test@gmail.com";
        }
        return RealTimeStorage.userEmail;
    }
    
    // getter for username
    public static String getUsername(){
        if (RealTimeStorage.userName == null){
            return "Testing";
        }
        return RealTimeStorage.userName;
    }
    
    // getter for phonenumber
    public static String getPNumber(){
        if (RealTimeStorage.phoneNumber == null){
            return "";
        }
        return RealTimeStorage.phoneNumber;
    }
    
    // getter for permission
    public static String getPermission(){
        return RealTimeStorage.permission;
    }
    
    //getter for isLogin
    public static boolean getIsLogin(){
        return RealTimeStorage.isLogin;
    }
    
    // getter for movieBooking
    public static HashMap<String,Object> getMovieBooking(){
        return RealTimeStorage.MovieBooking;
    }
    
    // getter for FnB
    public static HashMap<String,Integer> getFnB(){
        return RealTimeStorage.FoodnBeverage;
    }
    
    public static ArrayList<String[]> getLinkedCard2D(){
        
        if(linkedCard == null){
            return null;
        }
        else{
            ArrayList<String[]> converted = new ArrayList<>();
            for(int i = 0 ; i < RealTimeStorage.linkedCard.size() ; i++){
                converted.add(RealTimeStorage.linkedCard.get(i).split("#"));
            }
            return converted;
        }
    }
    
    public static void updateLinkedCard(String[] bank){
        String storedBank = String.join("#", bank);
        RealTimeStorage.linkedCard.add(storedBank);
    }
    
    // setter for linkedCards
    public static void removeLinkedCards(String cardDetails){
        // remove specified cards
       RealTimeStorage.linkedCard.remove(cardDetails);
    }
    
    public static void setUsername(String newVal){
        RealTimeStorage.userName = newVal;
    }
    
    public static void setEmail(String newVal){
        RealTimeStorage.userEmail=newVal;
    }
    
    public static void setPNumber(String newVal){
        RealTimeStorage.phoneNumber=newVal;
    }
    
    public static void setSelectedSeats(ArrayList<String[]> newSeats){
        RealTimeStorage.selectedSeats = newSeats;
    }
    
    public static void setTicketType(int[] ticketType){
        RealTimeStorage.ticketType = ticketType;
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