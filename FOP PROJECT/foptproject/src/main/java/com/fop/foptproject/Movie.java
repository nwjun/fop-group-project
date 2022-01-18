package com.fop.foptproject;

import javafx.scene.input.MouseEvent;

/**
 *
 * @author jun
 */
public class Movie extends Item{
  
    Movie(String movieID, String movieName,String poster,double posterW, double posterH, double scale) {
        super(movieID, movieName, poster, posterW, posterH, scale);
    }
 
    
    @Override
    public void startAnimation(MouseEvent event){
        super.startAnimation(event);
    }
       
}
