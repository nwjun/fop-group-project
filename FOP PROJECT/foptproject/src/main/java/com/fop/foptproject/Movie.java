/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fop.foptproject;

import javafx.scene.input.MouseEvent;

/**
 *
 * @author jun
 */
public class Movie extends Item{
  
    Movie(String movieID, String movieName,String poster, String description, String language,
            String releaseDate, String[] allShowTimes, String[] allShowDates, int length,
            double rottenTomato, double iMDB, double posterW, double posterH, double scale) {
        super(movieID, movieName, poster, posterW, posterH, scale);
        this.setDetail("description", description);
        this.setDetail("language", language);
        this.setDetail("length", length);
        this.setDetail("releaseDate", releaseDate);
        this.setDetail("allShowTimes", allShowTimes);
        this.setDetail("allShowDates", allShowDates);
        this.setDetail("rottenTomato", rottenTomato);
        this.setDetail("iMDB", iMDB);
    }
 
    
    @Override
    public void startAnimation(MouseEvent event){
        super.startAnimation(event);
    }
       
}
