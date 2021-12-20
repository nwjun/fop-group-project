/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.fop.foptproject.controller;

import com.fop.Utility.sqlConnect;
import com.fop.foptproject.App;
import static com.fop.foptproject.ProductCardAdminMovie.castJsonProcessor;
import static com.fop.foptproject.ProductCardAdminMovie.directorJsonProcesor;
import static com.fop.foptproject.ProductCardAdminMovie.insertString;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.fop.foptproject.controller.MovieAllShowTImeController;
import com.fop.foptproject.controller.AdminMovieController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import org.json.simple.parser.ParseException;
/**
 * FXML Controller class
 *
 * @author shiao
 */
public class MoviesDetailsPopUpController implements Initializable {
    
    sqlConnect sql = new sqlConnect();
    
    private Object[] movieId;
    private Object[] movieName;
    private Object[] length;
    private Object[] directorCast;
    private Object[] posterPath;
    private Object[] synopsis;
    private Object[] rottenTomato;
    private Object[] iMDB;
    private double IMGW = 250 ;
    private double IMGH = 375;
    private double SCALE = 0.9;
    
    
    @FXML
    private Label cinemas;
    @FXML
    private Label premiumPrice;
    @FXML
    private Button booknowButton;
    @FXML
    private Button backBtn;
    @FXML
    private ImageView moviePoster;
    @FXML
    private Label movieTitleT;
    @FXML
    private Label directorT;
    @FXML
    private Label castT;
    @FXML
    private Label iMDBT;
    @FXML
    private Label rottenTomatoT;
    @FXML
    private Label movieLengthT;
    @FXML
    private Label studentPrice;
    @FXML
    private Label adultPrice;
    @FXML
    private Label elderlyPrice;
    @FXML
    private Label OKUPrice;
    @FXML
    private Text synopsisT;
    
    @FXML
    public void changeToMovieBooking(ActionEvent event) throws IOException{
        if(!RealTimeStorage.getIsLogin()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Unauthorized Access");
            a.setContentText("Please log in to book a movie");
            Stage stage = (Stage) a.getDialogPane().getScene().getWindow(); // get the window of alert box and cast to stage to add icons
            stage.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
            stage.showAndWait();
            Stage stage1 = (Stage) booknowButton.getScene().getWindow();
            stage1.close();
            new SceneController().switchToRegisterAndLogin(event);
            return;
        }
        else{
            Stage stage = (Stage) booknowButton.getScene().getWindow();
            stage.close();
            SceneController switchScene = new SceneController();
            switchScene.switchToMovieBooking(event);
        }
        
    }
    
    @FXML
    public void close(ActionEvent event) throws IOException{
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
    }
    
    public void price(){
        HashMap<String, Double> tickets = new HashMap<>();
        tickets = sql.queryTicketPrice();
        studentPrice.setText(Double.toString(tickets.get("TS"))+"0");
        adultPrice.setText(Double.toString(tickets.get("TC"))+"0");
        elderlyPrice.setText(Double.toString(tickets.get("TE"))+"0");
        OKUPrice.setText(Double.toString(tickets.get("TO"))+"0");
        premiumPrice.setText(Double.toString(tickets.get("TP"))+"0");
    }
    
    public void PopUp(String ID) throws ParseException{
        MovieAllShowTImeController x = new MovieAllShowTImeController();
        
        HashMap<String,ArrayList<String>> items = sql.queryAllMovie();
        this.movieId = items.get("movieId").toArray();
        this.movieName = items.get("movieName").toArray();
        this.length = items.get("length").toArray();
        this.directorCast = items.get("directorCast").toArray();
        this.posterPath = items.get("poster").toArray();
        this.synopsis = items.get("synopsis").toArray();
        this.rottenTomato = items.get("rottenTomato").toArray();
        this.iMDB = items.get("iMDB").toArray();
        
        int index=0;
        
        
        for (int i =0; i<this.movieId.length;i++){
            if(((String)this.movieId[i]).equals(ID)){
                index = i;
                break;
            }    
        }
        String path = App.class.getResource((String)this.posterPath[index]).toString(); 
        Image img = new Image(path, IMGW, IMGH, false, false);
        
        String y =(String)this.length[index];
        String hour = y.substring(0, 1);
        String minute = y.substring(2);
        Double z = Double.parseDouble(minute);
        z = z * 60;
        minute = Double.toString(z);
        minute = minute.substring(0, 2);
        
        moviePoster.setImage(img);
        movieTitleT.setText((String)this.movieName[index]);
        movieLengthT.setText(String.format("%sh %sm", hour, minute));
        synopsisT.setText((String)this.synopsis[index]);
        rottenTomatoT.setText((String)this.rottenTomato[index]);
        iMDBT.setText((String)this.iMDB[index]);
        directorT.setText(directorJsonProcesor((String)this.directorCast[index]));
        String s = castJsonProcessor((String)this.directorCast[index]);

        for (int i = 0; i<s.length(); i+=10){
            s = insertString(s, "\n", s.lastIndexOf(", ", i));
        }
        
        castT.setText(s);
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        price();
    }  
    
    public void initData(String ID) throws ParseException{
        PopUp(ID);
    }
    
}
