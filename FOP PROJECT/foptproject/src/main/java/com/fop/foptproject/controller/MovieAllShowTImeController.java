/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.fop.foptproject.controller;

import com.fop.Utility.sqlConnect;
import com.fop.foptproject.App;
import com.fop.foptproject.ProductCardAdminMovie;
import static com.fop.foptproject.ProductCardAdminMovie.castJsonProcessor;
import static com.fop.foptproject.ProductCardAdminMovie.directorJsonProcesor;
import static com.fop.foptproject.controller.SceneController.showPopUpStage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private sqlConnect sql=new sqlConnect();

    private Object[] movieId;
    private Object[] movieName;
    private Object[] length;
    private Object[] releaseDate;
    private Object[] directorCast;
    private Object[] language;
    private Object[] posterPath;
    private Object[] allShowTime;
    private Object[] synopsis;
    private Object[] rottenTomato;
    private Object[] iMDB;
    private Object[] ageRestrict;
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
            Logger.getLogger(MovieAllShowTImeController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Fail");
        }
    }    
    
    public void getProduct() throws ParseException{
        HashMap<String,ArrayList<String>> items = sql.queryAllMovie();
        this.movieId = items.get("movieId").toArray();
        this.movieName = items.get("movieName").toArray();
        this.length = items.get("length").toArray();
        this.releaseDate = items.get("releaseDate").toArray();
        this.directorCast = items.get("directorCast").toArray();
        this.language = items.get("language").toArray();
        this.posterPath = items.get("poster").toArray();
        this.allShowTime = items.get("allShowTime").toArray();
        this.synopsis = items.get("synopsis").toArray();
        this.rottenTomato = items.get("rottenTomato").toArray();
        this.iMDB = items.get("iMDB").toArray();
        this.ageRestrict = items.get("ageRestrict").toArray();
        
        this.currentPage = 0;
        this.maxPage = (int) Math.ceil(movieId.length/8.0);
        this.currentIndex = 0;
                  
        loadCard();   
    }
    
    public void loadCard() throws ParseException{
        int n = movieId.length;
        stop:{
            for(int i = 0; i < 2;i++){
                for(int j = 0; j < 4 ; j++){
                    this.content = new ProductCardAdminMovie((String)movieId[currentIndex], (String)movieName[currentIndex], Double.parseDouble((String)length[currentIndex]), (String)releaseDate[currentIndex], (String)directorCast[currentIndex], (String)language[currentIndex], (String)posterPath[currentIndex], (String)allShowTime[currentIndex],(String)synopsis[currentIndex], Double.parseDouble((String)rottenTomato[currentIndex]),Double.parseDouble((String)iMDB[currentIndex]), Integer.parseInt((String)ageRestrict[currentIndex]),IMGW,IMGH,SCALE);
                    AnchorPane MB = MakeButton((String)movieId[currentIndex],Double.parseDouble((String)length[currentIndex]), currentIndex);
                    
                    VBox img = content.getImgCard();
                    ScrollPane DETAILS = content.getDetailCard();
                    DETAILS.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    DETAILS.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    DETAILS.setStyle("-fx-padding: 0 0 0 0; -fx-background-color:rgba(0,0,0,0);");
                    DETAILS.setPrefHeight(650);
//                    HBox card = content.getCard();

                    VBox details = new VBox();
                    details.setStyle("-fx-padding:0 0 0 50 ");                
                    details.setPrefHeight(650);
                    details.setPrefWidth(483);
                    details.getChildren().addAll(DETAILS, MB);
                    
                    HBox card = new HBox();
                    card.getChildren().addAll(img, details);

                    movieList.add(card,i,j);
                    currentIndex++;
                    if (currentIndex>=n){
                        break stop;
                    }     
                }
            }
            
        }
    }
    
    private AnchorPane MakeButton(String movieId, double length, int x){
        Button booknow = new Button();
        
        booknow.setId(movieId);
        booknow.setText("Book Now");
        booknow.setLayoutX(304);
        booknow.setLayoutY(20);
        booknow.setPrefWidth(100);
        booknow.setPrefHeight(31);
        booknow.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
        
        booknow.setOnMouseEntered(new EventHandler<MouseEvent>(){
            
            @Override
            public void handle(MouseEvent t){
                booknow.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px;-fx-opacity : 0.6");  
            }
        });
        booknow.setOnMouseExited(new EventHandler<MouseEvent>(){
            
            @Override
            public void handle(MouseEvent t){
                booknow.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
            }
        });
        
        booknow.setOnAction((e)->{ 
            if(RealTimeStorage.getPermission()==null){
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Unauthorized Access");
                a.setContentText("Please login to book a movie");
                a.showAndWait();
                try {
                    new SceneController().switchToRegisterAndLogin(e);
                } catch (IOException ex) {
                    return;
                }
                return;
            }
            this.movieID = booknow.getId();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("MoviesDetailsPopUp.fxml"));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setX(250);
            stage.setY(120);
            
            try {
                stage.setScene(new Scene(loader.load()));
                MoviesDetailsPopUpController controller = loader.getController();
                controller.initData(booknow.getId());
            } catch (ParseException | IOException ex) {
                Logger.getLogger(MovieAllShowTImeController.class.getName()).log(Level.SEVERE, null, ex);
            }
            stage.show();
        });
        
        AnchorPane booknowbtn = new AnchorPane();
        booknowbtn.setStyle("-fx-padding: 0 0 45 0");
        booknowbtn.setPrefWidth(401);
        booknowbtn.setPrefHeight(0);
        booknowbtn.getChildren().addAll(booknow);
        
        return booknowbtn;       
    }
    
    public String getMovieID(){
        System.out.println(this.movieID);
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
    private void switchToAdminMain(ActionEvent event) throws IOException {
        SceneController switchScene = new SceneController();
        switchScene.switchToLandingPage(event);
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