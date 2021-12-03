package com.fop.foptproject.controller;

import com.fop.foptproject.Food;
import com.fop.foptproject.Movie;
import com.fop.foptproject.ReadCSV;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


public class AdminMainController implements Initializable {

    @FXML
    private HBox movieList;
    @FXML
    private HBox foodList;
    @FXML
    private Button toAdminMovie;
    @FXML
    private Button toAdminFood;
    
    
    @FXML
    private void switchToAdminMovie(ActionEvent event) throws IOException {
        SceneController SwitchScene = new SceneController();
        SwitchScene.switchToAdminMovie(event);
    }

    @FXML
    private void switchToAdminFood(ActionEvent event) throws IOException {
        SceneController SwitchScene = new SceneController();
        SwitchScene.switchToAdminFood(event);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final String sampleMovieCSV = "src/main/resources/com/fop/foptproject/testing/sampleMovie.csv";
        final String sampleFoodCSV = "src/main/resources/com/fop/foptproject/testing/sampleFood.csv";
        Movie[] movies = ReadCSV.csvToMovie(sampleMovieCSV);
        Food[] foods = ReadCSV.csvToFood(sampleFoodCSV);

        for (Movie movie : movies) {
            StackPane card = movie.getCard();
            movieList.getChildren().add(card);
            HBox.setMargin(card, new Insets(0,60,0,0));            
        }
        
        for (Food food : foods) {
            StackPane card = food.getCard();
            foodList.getChildren().add(card);
            HBox.setMargin(card, new Insets(0,100,0,0));
        }
        
    }    
    
}
