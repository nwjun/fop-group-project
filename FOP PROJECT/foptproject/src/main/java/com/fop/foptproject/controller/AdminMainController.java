package com.fop.foptproject.controller;

import com.fop.foptproject.App;
import com.fop.foptproject.Food;
import com.fop.foptproject.Movie;
import com.fop.foptproject.SetUpLanding;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * 
 * @author kuckn
 */
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
    private ImageView logo;
    
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
    
    @FXML
    public void signOut(ActionEvent event) throws IOException{
        RealTimeStorage.clearAll();
        SceneController switchScene = new SceneController();
        switchScene.switchToHome(event);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(RealTimeStorage.getPermission().equals("3")){
            String path = App.class.getResource("assets/company/master.png").toString(); 
            Image img = new Image(path/*, IMGW, IMGH, false, false*/);
            logo.setImage(img);
        }
        
        ArrayList<Movie> movies = SetUpLanding.getMovies();
        ArrayList<Food> foods = SetUpLanding.getFood();

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
