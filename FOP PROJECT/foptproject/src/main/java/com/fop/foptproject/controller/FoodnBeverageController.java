package com.fop.foptproject.controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import com.fop.foptproject.ProductCard;
import com.fop.sqlUtil.sqlConnect;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author WeiXIn
 */
public class FoodnBeverageController implements Initializable {
    private HashMap<String,Integer> ShoppingCart= new HashMap<>();
    private sqlConnect sql = new sqlConnect();
    private Object[] productId;
    private Object[] price;
    private Object[] posterPath;
    private Object[] productDesc;
    private double IMGW = 180.0 ;
    private double IMGH = 185.5;
    private double SCALE = 0.9;
    private double TRANSLATEX = 293;
    private int currentIndex = 0;
    private int currentPage = 0;
    private int maxPage;
    
    @FXML
    private GridPane productList;
    @FXML
    private Button comboButton;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button nextPageButton;
    @FXML
    private Button carteButton;
    @FXML
    private Button popcornButton;
    @FXML
    private Button beverageButton;
    @FXML 
    private Label title;
    @FXML
    private Line highlightLine;
    
    public void getProduct(String category){
        HashMap<String,ArrayList<String>> items = sql.queryProduct(category);
        this.productId = items.get("productId").toArray();
        this.price = items.get("price").toArray();
        this.posterPath = items.get("posterPath").toArray();
        this.productDesc = items.get("productDesc").toArray();
        this.currentPage = 0;
        this.maxPage = (int) Math.ceil(productId.length/4.0);
        this.currentIndex = 0;
                  
        loadCard();   
    }
    
    public void loadCard(){
        int n = productId.length;
        stop:{
            for(int i = 0; i < 2;i++){
                for(int j = 0; j < 2 ; j++){
                    ProductCard content = new ProductCard((String)productId[currentIndex],(String)posterPath[currentIndex],IMGW,IMGH,SCALE,Double.parseDouble((String)price[currentIndex]),"Product Title",(String)productDesc[currentIndex]);
                    HBox card = content.getCard();
                    productList.add(card,j,i);
                    currentIndex++;
                    if (currentIndex>=n){
                        break stop;
                    }   
           
                }
            }
        }
    }
    
    public void lineAnimation(double start, double end, Node node){
        TranslateTransition move = new TranslateTransition();
        move.setNode(node);
        move.setFromX(start);
        move.setToX(end);
        move.setCycleCount(1);
        move.setAutoReverse(false);
        move.setDuration(Duration.millis(200));
        move.play();
        
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
    public void popcorn(){
        lineAnimation(TRANSLATEX,444,highlightLine);
        TRANSLATEX = 444;
        title.setText("POPCORNS");
        productList.getChildren().clear();
        getProduct("popcorn");
        checkPage();
    }
    
    @FXML
    public void combo(){
        lineAnimation(TRANSLATEX,293,highlightLine);
        TRANSLATEX = 293;
        title.setText("COMBO SETS");
        productList.getChildren().clear();
        getProduct("combo");
        checkPage();
    }
    @FXML
    public void carte(){
        lineAnimation(TRANSLATEX,595,highlightLine);
        TRANSLATEX = 595;
        title.setText("A LA CARTE");
        productList.getChildren().clear();
        getProduct("carte");
        checkPage();
    }
    @FXML
    public void beverage(){
        lineAnimation(TRANSLATEX,744,highlightLine);
        TRANSLATEX = 744;
        title.setText("BEVERAGES");
        productList.getChildren().clear();
        getProduct("beverage");
        checkPage();
    }
    
    @FXML
    public void prevPage(){
        if (currentPage == 0)return;
        productList.getChildren().clear();
        currentIndex = (currentPage-1)*4;
        loadCard();
        currentPage--;
        checkPage();
    }      
        
    @FXML
    public void nextPage(){
        if(currentIndex==productId.length)return;
        productList.getChildren().clear();
        loadCard();
        currentPage++;
        checkPage();
    }
    
    
    
        
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        combo();
    }

    
    
}
