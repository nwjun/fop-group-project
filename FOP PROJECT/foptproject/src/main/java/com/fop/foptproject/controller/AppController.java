package com.fop.foptproject.controller;

// import classes from external library
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.geometry.Insets;

// import user-defined classes
import com.fop.foptproject.CommonMethod;
import com.fop.foptproject.Food;
import com.fop.foptproject.Movie;
import com.fop.foptproject.SetUpLanding;

/**
 * 
 * @author jun
 */
public class AppController implements Initializable {
    private final CommonMethod method = new CommonMethod();
    
    @FXML
    private ImageView imgBanner; 
        
    @FXML
    private ScrollPane scrollpane;
    
    @FXML
    private HBox movieList;
    
    @FXML
    private HBox foodList;
    
    @FXML
    private Line landingLine;
    
    @FXML
    void scrollToMovies(ActionEvent event) throws IOException {
        new SceneController().switchToAllShowTime(event);
    }

    @FXML
    void scrollToFB(ActionEvent event) {
        scrollpane.setVvalue(0.7);
    }

    @FXML
    void openNwjun(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.instagram.com/nw_jun/?hl=en"));
    }

    @FXML
    void openWxinlim(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.instagram.com/wxinlim/?hl=en"));
    }

    @FXML
    void openKuck(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.instagram.com/kuck.nien_s/?hl=en"));
    }

    @FXML
    void openXyu27(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.instagram.com/xyu_27/?hl=en"));
    }

    @FXML
    void openShxao(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.instagram.com/shxao.yxn_/?hl=en"));
    }
    
    @FXML
    public void changeToLoginAndRegister(ActionEvent event) throws IOException{
        SceneController switchScene = new SceneController();
        switchScene.switchToRegisterAndLogin(event);
    }
    
    private int count=0; 
        
    public void slideshow(){                         
        ArrayList<Image> image= new ArrayList<>();
        image.add(new Image (method.getPathToResources("assets/banner/banner2.jpg")));
        image.add(new Image (method.getPathToResources("assets/banner/banner3.jpg")));
        image.add(new Image (method.getPathToResources("assets/banner/banner4.jpg")));
        image.add(new Image (method.getPathToResources("assets/banner/banner5.jpg")));
        image.add(new Image (method.getPathToResources("assets/banner/banner6.jpg")));
        image.add(new Image (method.getPathToResources("assets/banner/banner7.jpg")));
        image.add(new Image (method.getPathToResources("assets/banner/banner8.jpg")));
        image.add(new Image (method.getPathToResources("assets/banner/banner1.jpg")));
       
        Timeline timeline= new Timeline(new KeyFrame(Duration.seconds(5),event->{
            imgBanner.setImage(image.get(count));
            count++;
            if (count==8){
                count=0;
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slideshow();
        // Location to sample csv
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
            HBox.setMargin(card, new Insets(0,60,0,0));
        }
        // Set poster to center alignment
        foodList.setAlignment(Pos.CENTER);
        // Set landingLine to center
        landingLine.setStartX(0);
        landingLine.setEndX(1450);
        StackPane.setAlignment(landingLine, Pos.BOTTOM_CENTER);
    }
}

