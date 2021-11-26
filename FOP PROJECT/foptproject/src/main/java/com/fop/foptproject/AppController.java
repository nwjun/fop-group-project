/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.foptproject;

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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;

public class AppController implements Initializable {
    CommonMethod method = new CommonMethod();
    
    @FXML
    private ImageView logo;
    @FXML
    private Button logInBtn;
    //banner slideshow
    private Button signInBtn;
    @FXML
    private ImageView imgBanner;
    
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
    
    @FXML
    private GridPane landingGrid;
    @FXML
    private StackPane landingStackPane;
    @FXML
    private ScrollPane scrollpane;
    @FXML
    private GridPane landingFooter;
    @FXML
    private Hyperlink hyperlink;
    @FXML
    private HBox movieList;
    @FXML
    private HBox foodList;
    @FXML
    private Line landingLine;
    @FXML
    private VBox landingFooterVBox1;
    @FXML
    private VBox landingFooterVBox2;
    @FXML
    private VBox landingFooterVBox3;

    @FXML
    void scrollToMovies(ActionEvent event) {
        scrollpane.setVvalue(0.3);
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
    void openCinemas(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.google.com/"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slideshow();
        // Location to sample csv
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
            HBox.setMargin(card, new Insets(0,60,0,0));
        }
        
        // Set landingLine to center
        landingLine.setStartX(0);
        landingLine.setEndX(1700);
        StackPane.setAlignment(landingLine, Pos.BOTTOM_CENTER);
    }

}

