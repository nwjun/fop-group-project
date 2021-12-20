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
    
    // must not be cleared
    private static sqlConnect sql = new sqlConnect();
    private static final String[] SLOTS = {"10.00AM", "03.30PM", "08.00PM", "01.00PM", "11.00PM"};
    private static HashMap<String, ArrayList<String>> movieDetails;
    private static HashMap<String, ArrayList<String>> landingFoodPoster;

    // clear orrest after log out
    private static boolean isLogin = false;
    private static String userId;
    private static String userEmail;
    private static String userName;
    private static String phoneNumber;
    private static String permission = "0";
    private static ArrayList<String> linkedCard;
    private static int alteringDay;
    
    // clear or reset after checkout
    private static String bookingNumber;
    private static String transactionTimestamp;
    private static String typeByQuantity;
    private static String seats;
    private static String fnb;
    private static String toBePaid;
    private static String lookingAtMovie;
    private static boolean isPremium = false;
    public static HashMap<String, Object> MovieBooking = new HashMap<>();
    public static HashMap<String, Integer> FoodnBeverage = new HashMap<>();
    private static ArrayList<String[]> selectedSeats = new ArrayList<>();
    private static int[] ticketType = new int[4]; // no of ticket for elder, adult, student, OKU 
    private static String[] ticketPrices = new String[4];// ticket price for elder, adult, student, OKU 
    public static int ticketTypeQuantity = ticketType.length;
    

    // setter for movie booking
    public static void updateMovieBooking(HashMap<String, Object> input, boolean clear) {
        if (!(clear)) {
            RealTimeStorage.MovieBooking = input;
        } else {
            RealTimeStorage.MovieBooking.clear();
        }
    }

    // setter for food and beverage
    public static void updateFnB(HashMap<String, Integer> input, boolean clear) {
        if (!(clear)) {
            RealTimeStorage.FoodnBeverage = input;
        } else {
            RealTimeStorage.FoodnBeverage.clear();
        }
    }

    // setter for movie booking
    /**
     * This method accepts an object no matter it is a multiple values or single
     * value for each key TypeCast is needed to use the data stored in the
     * HashMap
     *
     * @param key can only be 1. Selected MovieId, a String 2. Selected
     * MovieName, a String 3. Selected Cinema Name, a String 4. Selected Date, a
     * String 5. Selected Theater type, a String 6. Selected ShowTime, a String
     * 7. Selected Ticket Type and its quantity (even index for ticket type odd
     * index for ticket quantity), an ArrayList 8. Selected Seats, an ArrayList
     */
    public static void updateMovieBookingByKey(String key, ArrayList<String> value) {
        if (RealTimeStorage.MovieBooking.containsKey(key)) {
            RealTimeStorage.MovieBooking.replace(key, value);
        } else {
            RealTimeStorage.MovieBooking.put(key, value);
        }
    }

    // setter for movie booking
    /**
     * This method accepts an object no matter it is a multiple values or single
     * value for each key TypeCast is needed to use the data stored in the
     * HashMap
     *
     * @param key can only be 1. Selected MovieId, a String 2. Selected
     * MovieName, a String 3. Selected Cinema Name, a String 4. Selected Date, a
     * String 5. Selected Theater type, a String 6. Selected ShowTime, a String
     * 7. Selected Ticket Type and its quantity (even index for ticket type odd
     * index for ticket quantity), an ArrayList 8. Selected Seats, an ArrayList
     */
    public static void updateMovieBookingByKey(String key, String value) {

        if (RealTimeStorage.MovieBooking.containsKey(key)) {
            RealTimeStorage.MovieBooking.replace(key, value);
        } else {
            RealTimeStorage.MovieBooking.put(key, value);
        }
    }

    // clear All
    public static void clearAll() {
        RealTimeStorage.userId = null;
        RealTimeStorage.userEmail = null;
        RealTimeStorage.userName = null;
        RealTimeStorage.phoneNumber = null;
        RealTimeStorage.permission = "0";
        RealTimeStorage.lookingAtMovie = null;
        RealTimeStorage.isLogin = false;
        RealTimeStorage.isPremium = false;
        RealTimeStorage.bookingNumber = null;
        RealTimeStorage.transactionTimestamp = null;
        RealTimeStorage.seats = null;
        RealTimeStorage.fnb = null;
        RealTimeStorage.toBePaid = null;
        RealTimeStorage.typeByQuantity = null;
        RealTimeStorage.linkedCard.clear();
        RealTimeStorage.MovieBooking.clear();
        RealTimeStorage.FoodnBeverage.clear();
        RealTimeStorage.selectedSeats.clear();
    }
    
    // clear booking details
    public static void clearBookingDetails(){
        RealTimeStorage.lookingAtMovie = null;
        RealTimeStorage.isPremium = false;
        RealTimeStorage.bookingNumber = null;
        RealTimeStorage.transactionTimestamp = null;
        RealTimeStorage.seats = null;
        RealTimeStorage.fnb = null;
        RealTimeStorage.typeByQuantity = null;
        RealTimeStorage.MovieBooking.clear();
        RealTimeStorage.FoodnBeverage.clear();
        RealTimeStorage.selectedSeats.clear();
        RealTimeStorage.ticketType = new int[4];
        RealTimeStorage.ticketPrices = new String[4];
    }

    // setter for username, userId, userEmail, phoneNumber, permission, linkedCard
    public static void updateUserInfos(String userEmail) {

        sqlConnect sql = new sqlConnect();
        HashMap<String, String> result = sql.queryUserCredentials(userEmail);

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
    public static void appendLinkedCards() {
        // update remote database
        // convert to json object
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < RealTimeStorage.linkedCard.size(); i++) {
            jsonArray.put(RealTimeStorage.linkedCard.get(i));
        }

        String jsonString = JSONToolSets.writeJSONString(jsonArray, "cardDetails");
        sql.updateLinkedCard(jsonString, userEmail);
    }

    public static void setAlteringDay(int day) {
        RealTimeStorage.alteringDay = day;
    }

    public static void setLookingAt(String Id) {
        RealTimeStorage.lookingAtMovie = Id;
    }

    public static void setAllMovies() {
        RealTimeStorage.movieDetails = sql.queryAllMovie();
    }

    public static void setAllLandingFood() {
        RealTimeStorage.landingFoodPoster = sql.queryLandingFood("combo", 4);
    }

    public static void setBookingNumber(String bnum) {
        RealTimeStorage.bookingNumber = bnum;
    }

    public static void setTypeByQuantity(String type) {
        RealTimeStorage.typeByQuantity = type;
    }

    public static void setSeats(String seats) {
        RealTimeStorage.seats = seats;
    }

    public static void setfnb(String fnb) {
        RealTimeStorage.fnb = fnb;
    }
    
    public static void setToBePaid(String amount) {
        RealTimeStorage.toBePaid = amount;
    }
    
    public static void setTimestamp(String tstamp) {
        RealTimeStorage.transactionTimestamp = tstamp;
    }

    public static String getSlot(int i) {
        return RealTimeStorage.SLOTS[i];
    }

    public static String[] getSlots(int i) {
        String[] ranged = new String[i];
        for (int j = 0; j < i; j++) {
            ranged[j] = RealTimeStorage.SLOTS[j];
        }
        return ranged;
    }

    public static String[] getAllSlots() {
        return RealTimeStorage.SLOTS;
    }

    public static String getTime(int i) {
        return RealTimeStorage.getMovieDetail("time").get(RealTimeStorage.getMovieDetail("movieId").indexOf(RealTimeStorage.lookingAtMovie)).split(", ")[i];
    }
    
    public static String getTypeByQuantity() {
        return RealTimeStorage.typeByQuantity;
    }

    public static String getSeats() {
        return RealTimeStorage.seats;
    }

    public static String getfnb() {
        return RealTimeStorage.fnb;
    }
    
    public static String getToBePaid() {
        return RealTimeStorage.toBePaid;
    }

    public static String getBookingNumber() {
        return RealTimeStorage.bookingNumber;
    }

    public static String getTimestamp() {
        return RealTimeStorage.transactionTimestamp;
    }

    public static int getAlteringDay() {
        return RealTimeStorage.alteringDay;
    }

    public static String getLookingAt() {
        return RealTimeStorage.lookingAtMovie;
    }

    public static HashMap<String, ArrayList<String>> getAllLandingFood() {
        return RealTimeStorage.landingFoodPoster;
    }

    public static ArrayList<String> getFoodDetail(String key) {
        return RealTimeStorage.landingFoodPoster.get(key);
    }

    public static HashMap<String, ArrayList<String>> getAllMovies() {
        return RealTimeStorage.movieDetails;
    }

    public static ArrayList<String> getMovieDetail(String key) {
        return RealTimeStorage.movieDetails.get(key);
    }

    // getter for userId
    public static String getUserId() {
        if (RealTimeStorage.userId == null) {
            return "U123";
        }
        return RealTimeStorage.userId;
    }

    // getter for userEmail
    public static String getUserEmail() {
        if (RealTimeStorage.userEmail == null) {
            return "test@gmail.com";
        }
        return RealTimeStorage.userEmail;
    }

    // getter for username
    public static String getUsername() {
        if (RealTimeStorage.userName == null) {
            return "Testing";
        }
        return RealTimeStorage.userName;
    }

    // getter for phonenumber
    public static String getPNumber() {
        if (RealTimeStorage.phoneNumber == null) {
            return "";
        }
        return RealTimeStorage.phoneNumber;
    }

    // getter for permission
    public static String getPermission() {
        return RealTimeStorage.permission;
    }

    //getter for isLogin
    public static boolean getIsLogin() {
        return RealTimeStorage.isLogin;
    }

    // getter for movieBooking
    public static HashMap<String, Object> getMovieBooking() {
        return RealTimeStorage.MovieBooking;
    }

    // getter for FnB
    public static HashMap<String, Integer> getFnB() {
        return RealTimeStorage.FoodnBeverage;
    }

    public static ArrayList<String[]> getLinkedCard2D() {

        if (linkedCard == null) {
            return null;
        } else {
            ArrayList<String[]> converted = new ArrayList<>();
            for (int i = 0; i < RealTimeStorage.linkedCard.size(); i++) {
                converted.add(RealTimeStorage.linkedCard.get(i).split("#"));
            }
            return converted;
        }
    }

    public static String[] getTicketPrices(boolean isPremium) {
        final String[] cat = {"TE", "TC", "TS", "TO", "TP"};

        for (int i = 0; i < RealTimeStorage.ticketTypeQuantity; i++) {
            if (!isPremium) {
                RealTimeStorage.ticketPrices[i] = sql.queryTicketPrice().get(cat[i]).toString();
            } else {
                RealTimeStorage.ticketPrices[i] = sql.queryTicketPrice().get(cat[4]).toString();
            }
        }

        return RealTimeStorage.ticketPrices;
    }

    public static int[] getTicketType() {
        return RealTimeStorage.ticketType;
    }

    public static ArrayList<String[]> getSelectedSeats() {
        return RealTimeStorage.selectedSeats;
    }

    public static void updateLinkedCard(String[] bank) {
        String storedBank = String.join("#", bank);
        if (RealTimeStorage.linkedCard.contains(storedBank)) {
            return;
        }
        RealTimeStorage.linkedCard.add(storedBank);
    }

    // setter for linkedCards
    public static void removeLinkedCards(String cardDetails) {
        // remove specified cards
        RealTimeStorage.linkedCard.remove(cardDetails);
    }

    public static void setUsername(String newVal) {
        RealTimeStorage.userName = newVal;
    }

    public static void setEmail(String newVal) {
        RealTimeStorage.userEmail = newVal;
    }

    public static void setPNumber(String newVal) {
        RealTimeStorage.phoneNumber = newVal;
    }

    public static void setSelectedSeats(ArrayList<String[]> newSeats) {
        RealTimeStorage.selectedSeats = newSeats;
    }

    public static void setTicketType(int[] ticketType) {
        RealTimeStorage.ticketType = ticketType;
    }

    public static void setTicketPrice(String[] ticketPrices) {
        RealTimeStorage.ticketPrices = ticketPrices;
    }

    public static String retrievePurchaseDetail(String key) {
        if (RealTimeStorage.FoodnBeverage.containsKey(key)) {
            return Integer.toString(RealTimeStorage.FoodnBeverage.get(key));
        } else {
            return "0";
        }
    }

    public static HashMap<String, Integer> retrieveAllPurchaseDetail() {
        return RealTimeStorage.FoodnBeverage;
    }
}