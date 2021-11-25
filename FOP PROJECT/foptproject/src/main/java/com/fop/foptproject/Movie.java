/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fop.foptproject;

import java.sql.Blob;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author jun
 */
public class Movie {

    private HashMap<String, Object> movie = new HashMap<>();

    Movie(String movieID, String movieName, String description, String language,
            String releaseDate, String[] allShowTimes, String[] allShowDates, int length, Blob poster,
            double rottenTomato, double iMDB) {
        movie.put("movieID", movieID);
        movie.put("movieName", movieName);
        movie.put("description", description);
        movie.put("language", language);
        movie.put("length", length);
        movie.put("releaseDate", releaseDate);
        movie.put("poster", poster);
        movie.put("allShowTimes", allShowTimes);
        movie.put("allShowDates", allShowDates);
        movie.put("rottenTomato", rottenTomato);
        movie.put("iMDB", iMDB);
    }

    public HashMap<String, Object> getMovie() {
        return movie;
    }

    public void setDetail(String key, Object value) {
        try {
            movie.replace(key, value);
        } catch (Exception e) {
            System.out.println("Value not in hashmap:" + e);
        }
    }
    
    public Object getDetail(String key){
        return movie.get(key);
    }
}
