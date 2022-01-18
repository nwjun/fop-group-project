/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.fop.foptproject.controller;

import com.fop.Utility.sqlConnect;
import com.fop.foptproject.App;
import com.fop.foptproject.ProductCardAdminMovie;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.simple.parser.ParseException;

/**
 * FXML Controller class
 *
 * @author kuckn
 */
public class MovieAllShowTImeController implements Initializable {

    private ProductCardAdminMovie content;
   // private sqlConnect sql=new sqlConnect();

    private Object[] movieId;
    private Object[] movieName;
    private Object[] length;
    private Object[] releaseDate;
    private Object[] directorCast;
    private Object[] language;
    private Object[] posterPath;
    private Object[] synopsis;
    private Object[] rottenTomato;
    private Object[] iMDB;
    private Object[] ageRestrict;
    private Object[] theaterId;
    private Object[] time;
    private double IMGW = 250 ;
    private double IMGH = 375;
    private double SCALE = 0.9;
    private int currentIndex = 0;
    private int currentPage = 0;
    private int maxPage;
    
    public String movieID;
    
    @FXML
    private Button backToMain;
    @FXML
    private Button nextPageButton;
    @FXML
    private Button prevPageButton;
    @FXML
    private GridPane movieList;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            getProduct();
        } catch (ParseException ex) {
            System.out.println("Fail");
        }
    }    
    
    public void getProduct() throws ParseException{
        HashMap<String,ArrayList<String>> items = RealTimeStorage.getAllMovies();
//        HashMap<String,ArrayList<String>> items = sql.queryAllMovie();
        this.movieId = items.get("movieId").toArray();
        this.movieName = items.get("movieName").toArray();
        this.length = items.get("length").toArray();
        this.releaseDate = items.get("releaseDate").toArray();
        this.directorCast = items.get("directorCast").toArray();
        this.language = items.get("language").toArray();
        this.posterPath = items.get("poster").toArray();
        this.synopsis = items.get("synopsis").toArray();
        this.rottenTomato = items.get("rottenTomato").toArray();
        this.iMDB = items.get("iMDB").toArray();
        this.ageRestrict = items.get("ageRestrict").toArray();
        this.theaterId = items.get("theaterId").toArray();
        this.time = items.get("time").toArray();
        
        this.currentPage = 0;
        this.maxPage = (int) Math.ceil(movieId.length/6.0);
        this.currentIndex = 0;
        loadCard();
        checkPage();
    }
    
    public void loadCard() throws ParseException{
        int n = movieId.length;
        stop:{
            for(int i = 0; i < 3;i++){
                for(int j = 0; j < 2 ; j++){
                    this.content = new ProductCardAdminMovie((String)movieId[currentIndex], (String)movieName[currentIndex], Double.parseDouble((String)length[currentIndex]), (String)releaseDate[currentIndex], (String)directorCast[currentIndex], (String)language[currentIndex], (String)posterPath[currentIndex],(String)synopsis[currentIndex], Double.parseDouble((String)rottenTomato[currentIndex]),Double.parseDouble((String)iMDB[currentIndex]), Integer.parseInt((String)ageRestrict[currentIndex]),IMGW,IMGH,SCALE, (String)theaterId[currentIndex], (String)time[currentIndex]);
                    AnchorPane MB = MakeButton((String)movieId[currentIndex],Double.parseDouble((String)length[currentIndex]), currentIndex);
                    
                    VBox img = content.getImgCard();
                    ScrollPane DETAILS = content.getDetailCard();
                    DETAILS.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    DETAILS.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    DETAILS.setStyle("-fx-padding: 0 0 0 0; -fx-background-color:rgba(0,0,0,0);");
                    DETAILS.setPrefHeight(600);

                    VBox details = new VBox();
                    details.setStyle("-fx-padding:8 0 0 20 ");                
                    details.setPrefHeight(650);
                    details.setPrefWidth(450);
                    details.getChildren().addAll(DETAILS, MB);
                    
                    HBox card = new HBox();
                    card.getChildren().addAll(img, details);
                    card.getStyleClass().add("card");

                    movieList.add(card,j,i);
                    currentIndex++;
                    if (currentIndex>=n){
                        break stop;
                    }     
                }
            }
            
        }
    }
    
    private AnchorPane MakeButton(String movieId, double length, int x){
        Button info = new Button();
        
        info.setId(movieId);
        info.setText("Info");
        info.setLayoutX(304);
        info.setLayoutY(20);
        info.setPrefWidth(100);
        info.setPrefHeight(31);
        info.getStyleClass().add("yellowButton");
        
        info.setOnAction((e)->{ 
            this.movieID = info.getId();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("MoviesDetailsPopUp.fxml"));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            String path1 = App.class.getResource("assets/company/logo2.png").toString(); 
            Image img1 = new Image(path1);
            stage.getIcons().add(img1);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setX(250);
            stage.setY(120);
            
            try {
                Parent root = loader.load();

                Scene scene = new Scene(root);
                stage.setScene(scene);
                scene.getStylesheets().add(App.class.getResource("style.css").toExternalForm());
                MoviesDetailsPopUpController controller = loader.getController();
                controller.initData(info.getId());
                RealTimeStorage.setLookingAt(info.getId());
            } catch (ParseException | IOException ex) {
                Logger.getLogger(MovieAllShowTImeController.class.getName()).log(Level.SEVERE, null, ex);
            }
            stage.show();
        });
        
        AnchorPane infobtn = new AnchorPane();
        infobtn.setStyle("-fx-padding: 0 0 45 0");
        infobtn.setPrefWidth(401);
        infobtn.setPrefHeight(0);
        infobtn.getChildren().addAll(info);
        
        return infobtn;       
    }
    
    public String getMovieID(){
        return this.movieID;
    }
    
    public void checkPage(){
        if(currentPage == maxPage-1){
            nextPageButton.setDisable(true);
        }
        else{
            nextPageButton.setDisable(false);
        }
        if(currentPage == 0){
            prevPageButton.setDisable(true);
        }
        else{
            prevPageButton.setDisable(false);
        }
    }
    
    @FXML
    private void switchToHome(ActionEvent event) throws IOException {
        if(RealTimeStorage.getIsLogin()){
            new SceneController().switchToHomeLogined(event);
        }
        else{
            new SceneController().switchToHome(event);
        }
    }

    @FXML
    private void nextPage(ActionEvent event) throws ParseException {
        if(currentIndex==movieId.length)return;
        movieList.getChildren().clear();
        loadCard();
        currentPage++;
        checkPage();
    }

    @FXML
    private void prevPage(ActionEvent event) throws ParseException {
        if (currentPage == 0)return;
        movieList.getChildren().clear();
        currentIndex = (currentPage-1)*8;
        loadCard();
        currentPage--;
        checkPage();
    }
    
}
