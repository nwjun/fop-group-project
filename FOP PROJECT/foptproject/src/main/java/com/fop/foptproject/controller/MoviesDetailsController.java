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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
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
    public void toMovieBooking(ActionEvent event) throws IOException{
        SceneController scene = new SceneController();
        if(!RealTimeStorage.getIsLogin()){
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Unauthorized Access");
            a.setContentText("Please log in to book a movie");
            Stage stage = (Stage) a.getDialogPane().getScene().getWindow(); // get the window of alert box and cast to stage to add icons
            stage.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
            stage.showAndWait();
            scene.switchToRegisterAndLogin(event);
            return;
        }
        scene.switchToMovieBooking(event);
    }
    
    @FXML
    public void changeToLandingPage(ActionEvent event) throws IOException{
        SceneController scene = new SceneController();
        if(RealTimeStorage.getIsLogin()){
            if(RealTimeStorage.getPermission().equals("1")){
                RealTimeStorage.updateMovieBooking(null, true); // clear all movieBooking
                scene.switchToHomeLogined(event);
            }
            else{
                scene.switchToAdminMain(event);
            }
        }
        else{
            scene.switchToHome(event);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String id = RealTimeStorage.getLookingAt();
        ArrayList<String> movieID = RealTimeStorage.getMovieDetail("movieId");
        int i = movieID.indexOf(id);
        // convert hour deci to hours and minutes
        int hr = (int) Math.ceil(Double.parseDouble(RealTimeStorage.getMovieDetail("length").get(i))*60);
        //String formatted = String.format("%d %s and %d %s", hr/60,(hr/60 > 1)?"hours":"hour", hr%60, (hr%60 > 1)?"minutes":"minute");
        String formatted = String.format("%d%s and %d%s", hr/60,"h", hr%60, "m");
        
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
