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