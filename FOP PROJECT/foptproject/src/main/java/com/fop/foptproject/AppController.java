/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.foptproject;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class AppController implements Initializable {

    @FXML
    private ImageView logo;
    @FXML
    private Button logInBtn;
    @FXML
    private ImageView imgBanner;
    @FXML
    private GridPane landingGrid;
    @FXML
    private StackPane landingStackPane;
    @FXML
    private HBox movieList;
    @FXML
    private HBox foodList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Location to sample csv
        final String sampleMovieCSV = "src/main/resources/com/fop/testing/sampleMovie.csv";
        final String sampleFoodCSV = "src/main/resources/com/fop/testing/sampleFood.csv";
        Movie[] movies = ReadCSV.csvToMovie(sampleMovieCSV);
        Food[] foods = ReadCSV.csvToFood(sampleFoodCSV);
        
        for (Movie movie: movies) {
            StackPane card = movie.getCard();
            movieList.getChildren().add(card);
            HBox.setHgrow(card, Priority.ALWAYS);
        }
        
        for (Food food: foods) {
            StackPane card = food.getCard();
            foodList.getChildren().add(card);
            HBox.setHgrow(card, Priority.ALWAYS);
        }
    }
}
