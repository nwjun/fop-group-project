/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.*;
import com.fop.foptproject.controller.RealTimeStorage;

/**
 * 
 * @author WeiXin
 */
public class JSONToolSets {
    private JSONObject jsonObj;
    private int columnSize;
    private int rowSize;
    private boolean isSeat = false;
    private boolean isTemplate = false;
    private static sqlConnect sql = new sqlConnect();
    
    public JSONToolSets(String jsonString){
        this.jsonObj = new JSONObject(jsonString);
    }
    
    public JSONToolSets(String jsonString, boolean isTemplate){
        this.jsonObj = new JSONObject(jsonString);
        this.isSeat = true;
        this.isTemplate = isTemplate;
    }
    
    public JSONObject getJsonObj(){
        return this.jsonObj;    
    }
    
    public HashMap<String,HashMap<String,ArrayList<String>>> parseAllShowTimes(){
        JSONObject temp = this.jsonObj;
        HashMap<String,ArrayList<String>> hall = new HashMap<>();
        HashMap<String,ArrayList<String>> showtime = new HashMap<>();
        HashMap<String,HashMap<String,ArrayList<String>>> week = new HashMap<>();
        
        //initialize the hashmap
        for(int i = 0; i < 7 ; i++){
            hall.put(Integer.toString(i), new ArrayList<String>());
            showtime.put(Integer.toString(i), new ArrayList<String>());
        }
        
        //filling the info
        int j = 0;
        for(int i = 0 ; i < 7 ; i++){
            while(true){
                try{
                    hall.get(Integer.toString(i)).add(temp.getJSONArray(Integer.toString(i)).getJSONArray(j).get(0).toString());
                    showtime.get(Integer.toString(i)).add(temp.getJSONArray(Integer.toString(i)).getJSONArray(j).get(1).toString());
                    j++;
                }
                catch(Exception e){
                    hall.get(Integer.toString(i)).add(null);
                    showtime.get(Integer.toString(i)).add(null);
                    break;
                }
            }
            j=0;
        } 
            
        week.put("hall",hall);
        week.put("showtime",showtime);
        return week;
    }
    
    public ArrayList<String> parseOneDArray(String key){
        ArrayList<String> result = new ArrayList<>();
        JSONObject temp = this.jsonObj;
        int i = 0;
        while(true){
            try{
                result.add(temp.getJSONArray(key).get(i).toString());
                i++;
            }
            catch(Exception e){
                break;  
            }
        }
        
        return result;
    }
    
    public String parseOneObject(String key){
        JSONObject temp = this.jsonObj;
        String value = temp.getJSONObject(key).toString();
        
        return value;
    }
    
    public String parseValue(String key){
        JSONObject temp = this.jsonObj;
        String value = temp.getString(key);
        
        return value;
    }
    
    public String[][] parseLinkedCard(){
        ArrayList<String> toBeProcessed = parseOneDArray("cardDetails");
        String[] temp;
        String[][] result = new String[toBeProcessed.size()][];
        int i = 0;
        for(String item : toBeProcessed){
            temp = item.split("#");
            result[i] = temp;
            i++;
        }
        
        return result;
    }
    
    public HashMap<String,ArrayList<String>> parseTheaterSeat(int day){
        JSONObject temp = this.jsonObj;        
        RealTimeStorage.setAlteringDay(day);
        
        this.rowSize = temp.length();
        if(!this.isTemplate)
            this.columnSize = temp.getJSONObject("0").getJSONArray("0").length();
        else
            this.columnSize = temp.getJSONArray("0").length();
       
        JSONArray temp2;
        HashMap<String,ArrayList<String>> extracted = new HashMap<>();
        int i =0 ,j = 0;
        
        // iterate until empty
        while(true){ 
            try{
                if(!this.isTemplate)
                    temp2 = temp.getJSONObject(Integer.toString(day)).getJSONArray(Integer.toString(i));
                else
                    temp2 = temp.getJSONArray(Integer.toString(i));
                
                extracted.put(Integer.toString(i),new ArrayList<String>());
                j = 0;
                while(true){
                    try{
                        extracted.get(Integer.toString(i)).add(temp2.get(j).toString());
                        j++;
                    }
                    catch(Exception e){
                        break;
                    }
                }
                i++;
            }
            catch(Exception e){
                break;
            }
        }
        
//        for(String key : extracted.keySet()){
//            for(String item : extracted.get(key)){
//                if(item.equals("-1"))System.out.print("X ");
//                else System.out.print(item+" ");
//            }
//            System.out.println("");
//        }
//        System.out.println("\n");
        
        return extracted;
    }
    
    /**
     * @param n number of columns to be added
     * @param isTemplate determine the actions to be proceeded, true for template action, false for non template seat action
     * 
     * <br><br>Add n number of columns to current parsed seat template
     * 
     * <br><br>Precedence of increasing capacity: column > row
     */
    public void addColumn(int n, boolean isTemplate){
        if(this.isSeat){
            // get new number of column
            int newColumnSize = this.columnSize + n;
            
            // generate new row arrangement
            ArrayList<Integer> notLast = new ArrayList<>();
            ArrayList<Integer> last = new ArrayList<>();
            
            if(!isTemplate){
                // arrange not last row
                for(int i = 0 ; i < newColumnSize-4 ; i++){
                    if(i%2 != 0){
                        notLast.add(-1);
                    }
                    else{
                        notLast.add(0);
                    }
                }

                // arrange last row
                int factor = 0;
                for(int i = 0 ; i < newColumnSize-4 ; i++){
                    if((i-2)/3.0 == factor){
                        last.add(-1);
                        factor++;
                    }
                    else{
                        last.add(0);
                    }
                }

                // add in side columns
                for(int j = 0 ; j < 2 ; j++){
                    last.add(0,0);
                    last.add(0);
                    notLast.add(0,0);
                    notLast.add(0);
                }
            }
            else{
                // modify template
                for(int i = 0 ; i < newColumnSize ; i++){
                    last.add(0);
                    notLast.add(0);
                }
            }
            
            // new JSON Object to hold the structure;
            JSONObject obj = new JSONObject();
            for(int i = 0 ; i < this.rowSize ; i++){
                if(i+1 == this.rowSize){
                    obj.put(Integer.toString(i),new JSONArray(last));
                }
                else{
                    obj.put(Integer.toString(i),new JSONArray(notLast));
                }       
            }
            this.jsonObj = obj;
            this.columnSize = newColumnSize;
        }
        else{
            return;
        } 
        
    }
    
    /**
     * @param n number of columns to be added
     * 
     * <br><br>Add n number of rows to current parsed seat template
     * 
     * <br><br>Precedence of increasing capacity: column > row
     */
    public void addRow(int n){
        if(this.isSeat){
            
            ////generate new n x n seats arrangement
            // copy the arrangement of !last row and last row from previous arrangement
            ArrayList<Integer> arr = new ArrayList<>();
            ArrayList<Integer> lastArr = new ArrayList<>();
            for(int i = 0; i < this.columnSize ; i++){
                arr.add(this.jsonObj.getJSONArray("0").getInt(i));
                lastArr.add(this.jsonObj.getJSONArray(Integer.toString(this.rowSize-1)).getInt(i));
            }
            
            // new JSON object to hold the structure
            JSONObject obj = new JSONObject();
            
            int newRowSize = this.rowSize+n;
            for(int i = 0 ; i < newRowSize ; i++){
                if(i == newRowSize-1){
                    obj.put(Integer.toString(i),new JSONArray(lastArr));
                }
                else{
                    obj.put(Integer.toString(i),new JSONArray(arr));
                } 
            }
            
            this.jsonObj = obj;
            this.rowSize = newRowSize;
            
        }
        else{
            return;   
        }
    }
    
    /**
     * @param row integer type
     * @param column integer type
     * @param stat integer type
     * <p>
     * 0 = not booked
     * <br>1 = booked
     * <br>-1 = can't be booked
     * <br><br>Middle section row and column number need to be calibrate
     * <br>This method will update the jsonObj which is stored in the class. No action
     * will be taken if the jsonObject is not seat template.
     * <br>This method will return void at the end
     * </p>
     */
    public void setSeatStat(int row, int column, int stat, String day){
        if(this.isTemplate){
            // parse json
            HashMap<Integer,ArrayList<Integer>> temp = new HashMap<>();
            for(int i = 0 ; i < this.rowSize ; i++){
                temp.put(i,new ArrayList<Integer>());
                for(int j = 0 ; j < this.columnSize ; j++){
                    temp.get(i).add(this.jsonObj.getJSONArray(Integer.toString(i)).getInt(j));
                }
            }
            
            // set seat status
            temp.get(row).set(column, stat);
            
            // convert to json string
            JSONObject result = new JSONObject();
            for(int key : temp.keySet()){
                result.put(Integer.toString(key), new JSONArray(temp.get(key)));
            }
            
            this.jsonObj = result;
        }
        else{
            // handle changes on non template
            JSONObject newJson = new JSONObject();
            for(int i = 0 ; i < 7 ; i++){
                if(i == Integer.parseInt(day)){
                    // get the day seat
                    HashMap<Integer,ArrayList<Integer>> temp = new HashMap<>();
                    for(int j = 0 ; j < this.rowSize ; j++){
                        temp.put(j,new ArrayList<Integer>());
                        for(int k = 0 ; k < this.columnSize ; k++){
                            //System.out.print(this.jsonObj.getJSONObject(Integer.toString(i)).getJSONArray(Integer.toString(j)).getInt(k) + " ");
                            temp.get(j).add(this.jsonObj.getJSONObject(Integer.toString(i)).getJSONArray(Integer.toString(j)).getInt(k));
                        }
                        //System.out.println("");
                    }
                    
                    // set seat status
                    temp.get(row).set(column, stat);
                    
                    JSONObject result = new JSONObject();
                    for(int key : temp.keySet()){
                        result.put(Integer.toString(key), new JSONArray(temp.get(key)));
                    }
                    
                    newJson.put(Integer.toString(i),result);
                    
                }
                else{
                    // copy unaffected day
                    //System.out.println(this.jsonObj.getJSONObject(Integer.toString(i)));
                    newJson.put(Integer.toString(i),this.jsonObj.getJSONObject(Integer.toString(i)));
                }    
            }
            this.jsonObj = newJson;
        } 
    }
    
    
    /**
     * @param row integer ArrayList object 
     * @param column integer ArrayList object
     * @param stat integer ArrayList object
     * Length of the parameters must be equal to each other. Otherwise will cause error
     * <p>
     * 0 = not booked
     * <br>1 = booked
     * <br>-1 = can't be booked
     * <br><br>Middle section row and column number need to be calibrate
     * <br>This method will update the jsonObj which is stored in the class. No action
     * will be taken if the jsonObject is not seat template.
     * <br>This method will return void at the end
     * </p>
     */
    public void setSeatStat(ArrayList<Integer> row, ArrayList<Integer> column, ArrayList<Integer> stat){
        if(this.isTemplate){
            // parse json
            HashMap<Integer,ArrayList<Integer>> temp = new HashMap<>();
            for(int i = 0 ; i < this.rowSize ; i++){
                temp.put(i,new ArrayList<Integer>());
                for(int j = 0 ; j < this.columnSize ; j++){
                    temp.get(i).add(this.jsonObj.getJSONArray(Integer.toString(i)).getInt(j));
                }
            }
            
            // set seat status
            for(int i = 0 ; i < row.size() ; i++)
                temp.get(row.get(i)).set(column.get(i), stat.get(i));
            
            // convert to json string
            JSONObject result = new JSONObject();
            for(int key : temp.keySet()){
                result.put(Integer.toString(key), new JSONArray(temp.get(key)));
            }
            
            this.jsonObj = result;
        }
    }
    
    /**
     * @param row integer array type 
     * @param column integer array type
     * @param stat integer array type
     * Length of the parameters must be equal to each other. Otherwise will cause error
     * <p>
     * 0 = not booked
     * <br>1 = booked
     * <br>-1 = can't be booked
     * <br><br>Middle section row and column number need to be calibrate
     * <br>This method will update the jsonObj which is stored in the class. No action
     * will be taken if the jsonObject is not seat template.
     * <br>This method will return void at the end
     * </p>
     */
    public void setSeatStat(int[] row, int[] column, int[] stat){
        if(this.isTemplate){
            // parse json
            HashMap<Integer,ArrayList<Integer>> temp = new HashMap<>();
            for(int i = 0 ; i < this.rowSize ; i++){
                temp.put(i,new ArrayList<Integer>());
                for(int j = 0 ; j < this.columnSize ; j++){
                    temp.get(i).add(this.jsonObj.getJSONArray(Integer.toString(i)).getInt(j));
                }
            }
            
            // set seat status
            for(int i = 0 ; i < row.length ; i++)
                temp.get(row[i]).set(column[i], stat[i]);
            
            // convert to json string
            JSONObject result = new JSONObject();
            for(int key : temp.keySet()){
                result.put(Integer.toString(key), new JSONArray(temp.get(key)));
            }
            
            this.jsonObj = result;
        }
    }
    
    /**
     * Apply changes to current seat arrangement immediately
     */
//    public void commitToCurrentSeats(){
//        int ind = RealTimeStorage.getMovieDetail("movieId").indexOf(RealTimeStorage.getLookingAt());
//        String theaterId = RealTimeStorage.getMovieDetail("theaterId").get(ind);
//        
//        HashMap<String,ArrayList<String>> = new JSONToolSets(sql.querySeats(, ,false),false);
//    }
    
    /**
     *  Getter for new seat arrangement
     *  
     *  <br><br>Return a JSONObject
     */
    public JSONObject getNewSeatArr(){  
        if(isSeat){
            return this.jsonObj;
        }
        else{
            return null;
        }
    }
        
    /**
     * Getter for current number of rows of seat template
     * 
     * <br><br>return an Integer 
     */
    public int getRow(){
        return this.rowSize;
    }
    
    /**
     * Getter for current number of columns of seat template
     * 
     * <br><br>return an Integer 
     */
    public int getColumn(){
        return this.columnSize;
    }
    
    public static String writeJSONString(JSONArray jsonArray,String key){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, jsonArray);
        String jsonString = jsonObject.toString();
        
        return jsonString;
    }
    
    
    public static String writeJSONString(JSONObject jsonObjectIn,String key){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, jsonObjectIn);
        String jsonString = jsonObject.toString();
        
        return jsonString;
    }
    
    public static String writeReceiptJSON(HashMap<String,Integer> FnB, int[] tickets, boolean isPremium){
        final String[] category = {"Elder","Adult","Student","Disabled"};
        final String[] ticketId = {"TE","TC","TS","TO","TP"};
        JSONObject receiptObject = new JSONObject();
        JSONObject ticket = new JSONObject();
        JSONObject purchasedFnB = new JSONObject();
        for(int i = 0 ; i < RealTimeStorage.ticketTypeQuantity ; i++){
            if(RealTimeStorage.getMovieBooking().get("theaterType").equals("Classic")){
                double price = Double.parseDouble(sql.queryProductInfo(ticketId[i],"price"));
                String[] concat = {ticketId[i],tickets[i]+"",price+""};
                ticket.put(category[i], String.join(",",concat));
            }
            else{
                double price = Double.parseDouble(sql.queryProductInfo(ticketId[4],"price"));
                int quantity = tickets[3]+tickets[2]+tickets[1]+tickets[0];
                ticket.put("Premium",String.join(",",new String[]{ticketId[4],quantity+"",price+""}));
            }
        }
        for(String productId : FnB.keySet()){
            if(productId.equals("S000005P")){
                String[] concat = {FnB.get(productId)+"","FREE"};
                purchasedFnB.put(productId,String.join(",",concat));
                continue;
            }
            double price = Double.parseDouble(sql.queryProductInfo(productId,"price"));
            String[] concat = {FnB.get(productId)+"",price+""};
            purchasedFnB.put(productId,String.join(",",concat));
        }
        receiptObject.put("ticket",ticket);
        receiptObject.put("fnb",purchasedFnB);
        
        
        return receiptObject.toString();
    }
    
 
}