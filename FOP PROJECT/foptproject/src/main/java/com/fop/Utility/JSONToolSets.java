/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.*;
import com.fop.Utility.sqlConnect;


public class JSONToolSets {
    private JSONObject jsonObj;
    private int columnSize;
    private int rowSize;
    private boolean isSeat = false;
    private static sqlConnect sql = new sqlConnect();
    
    public JSONToolSets(String jsonString){
        this.jsonObj = new JSONObject(jsonString);
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
    
    public HashMap<String,ArrayList<String>> parseTheaterSeat(){
        this.isSeat = true;
        JSONObject temp = this.jsonObj;        
        
        this.rowSize = temp.length();
        this.columnSize = temp.getJSONArray("0").length();
        
       
        JSONArray temp2;
        HashMap<String,ArrayList<String>> extracted = new HashMap<>();
        int i =0 ,j = 0;
        
        // iterate until empty
        while(true){ 
            try{
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
        return extracted;
    }
    
    public void addColumn(int n){
        /**
         * precedence of increasing capacity: column > row
         */
        if(this.isSeat){
            // get new number of column
            int newColumnSize = this.columnSize + n;
            
            // generate new row arrangement
            ArrayList<Integer> notLast = new ArrayList<>();
            ArrayList<Integer> last = new ArrayList<>();
            
            
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
        }
        else{
            return;
        }
        
        
    }
    
    public void addRow(int n){
        /**
         * precedence of increasing capacity: column > row
         */
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
            
            int newRow = this.rowSize+n;
            for(int i = 0 ; i < newRow ; i++){
                if(i == newRow-1){
                    obj.put(Integer.toString(i),new JSONArray(lastArr));
                }
                else{
                    obj.put(Integer.toString(i),new JSONArray(arr));
                } 
            }
            
            this.jsonObj = obj;
            
        }
        else{
            return;   
        }
    }
    
    public JSONObject getNewSeatArr(){
        if(isSeat){
            return this.jsonObj;
        }
        else{
            return null;
        }
    }
        
    public int getRow(){
        return this.rowSize;
    }
    
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
    

}