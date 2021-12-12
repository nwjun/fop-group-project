/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.fop.foptproject.controller;

import com.fop.Utility.JSONToolSets;
import com.fop.foptproject.App;
import com.fop.foptproject.controller.RealTimeStorage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author shiao, WeiXin
 */
public class MoviesDetailsController implements Initializable {
    
    
    @FXML
    private Label movieTitle;
    @FXML
    private Label director;
    @FXML
    private Label casts;
    @FXML
    private Label language;
    @FXML
    private Label ageRestriction;
    @FXML
    private Label releaseDate;
    @FXML
    private Label imdb;
    @FXML
    private Label rottenTomato;
    @FXML
    private Label length;
    @FXML
    private Label synopsis;
    @FXML
    private ImageView poster;
    
    @FXML
    public void changeToMovieBooking(ActionEvent event) throws IOException{
        SceneController switchScene = new SceneController();
        switchScene.switchToMovieBooking(event);
    }
    
    @FXML
    public void changeToLandingPage(ActionEvent event) throws IOException{
        SceneController switchScene = new SceneController();
        if(RealTimeStorage.getIsLogin()) switchScene.switchToHomeLogined(event);
        else switchScene.switchToHome(event);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String id = RealTimeStorage.getLookingAt();
        ArrayList<String> movieID = RealTimeStorage.getMovieDetail("movieId");
        int i = movieID.indexOf(id);
        // convert hour deci to hours and minutes
        int hr = (int) Math.ceil(Double.parseDouble(RealTimeStorage.getMovieDetail("length").get(i))*60);
        String formatted = String.format("%d %s and %d %s", hr/60,(hr/60 > 1)?"hours":"hour", hr%60, (hr%60 > 1)?"minutes":"minute");
        
        // set Details
        Image img = new Image(App.class.getResource(RealTimeStorage.getMovieDetail("poster").get(i)).toString());
        poster.setImage(img);
        movieTitle.setText(RealTimeStorage.getMovieDetail("movieName").get(i));
        director.setText(new JSONToolSets(RealTimeStorage.getMovieDetail("directorCast").get(i)).parseValue("Director"));
        casts.setText(String.join("\n",new JSONToolSets(RealTimeStorage.getMovieDetail("directorCast").get(i)).parseOneDArray("Cast")));
        language.setText(RealTimeStorage.getMovieDetail("language").get(i));
        ageRestriction.setText(RealTimeStorage.getMovieDetail("ageRestrict").get(i));
        releaseDate.setText(RealTimeStorage.getMovieDetail("releaseDate").get(i));
        imdb.setText(RealTimeStorage.getMovieDetail("iMDB").get(i));
        rottenTomato.setText(RealTimeStorage.getMovieDetail("rottenTomato").get(i));
        length.setText(formatted);
        synopsis.setText(RealTimeStorage.getMovieDetail("synopsis").get(i));
        
    }    
    
}
