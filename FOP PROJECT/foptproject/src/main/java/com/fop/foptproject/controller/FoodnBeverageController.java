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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

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
    private int currentIndex = 0;
    private int currentPage = 0;
    
    @FXML
    private GridPane productList;
    @FXML
    private Button comboButton;
    @FXML
    private Button carteButton;
    @FXML
    private Button popcornButton;
    @FXML
    private Button beverageButton;
    @FXML 
    private Label title;
    
    public void getProduct(String category){
        HashMap<String,ArrayList<String>> items = sql.queryProduct(category);
        this.productId = items.get("productId").toArray();
        this.price = items.get("price").toArray();
        this.posterPath = items.get("posterPath").toArray();
        this.productDesc = items.get("productDesc").toArray();
        this.currentPage = 0;
        this.currentIndex = 0;
                  
        loadCard();   
    }
    
    @FXML
    public void popcorn(){
        popcornButton.setStyle("-fx-border-width: 0 0 1 0;-fx-border-color:#FFEE00");
        carteButton.setStyle("-fx-border-width: 0 0 0 0;");
        beverageButton.setStyle("-fx-border-width: 0 0 0 0;");
        comboButton.setStyle("-fx-border-width: 0 0 0 0;");
        title.setText("POPCORNS");
        productList.getChildren().clear();
        getProduct("popcorn");
    }
    
    @FXML
    public void combo(){
        comboButton.setStyle("-fx-border-width: 0 0 1 0;-fx-border-color:#FFEE00");
        carteButton.setStyle("-fx-border-width: 0 0 0 0;");
        beverageButton.setStyle("-fx-border-width: 0 0 0 0;");
        popcornButton.setStyle("-fx-border-width: 0 0 0 0;");
        title.setText("COMBO SETS");
        productList.getChildren().clear();
        getProduct("combo");
    }
    @FXML
    public void carte(){
        carteButton.setStyle("-fx-border-width: 0 0 1 0;-fx-border-color:#FFEE00");
        comboButton.setStyle("-fx-border-width: 0 0 0 0;");
        beverageButton.setStyle("-fx-border-width: 0 0 0 0;");
        popcornButton.setStyle("-fx-border-width: 0 0 0 0;");
        title.setText("A LA CARTE");
        productList.getChildren().clear();
        getProduct("carte");
    }
    @FXML
    public void beverage(){
        beverageButton.setStyle("-fx-border-width: 0 0 1 0;-fx-border-color:#FFEE00");
        carteButton.setStyle("-fx-border-width: 0 0 0 0;");
        comboButton.setStyle("-fx-border-width: 0 0 0 0;");
        popcornButton.setStyle("-fx-border-width: 0 0 0 0;");
        title.setText("BEVERAGES");
        productList.getChildren().clear();
        System.out.println("in progress");
        return;
    }
    
    @FXML
    public void prevPage(){
        if (currentPage == 0)return;
        productList.getChildren().clear();
        currentIndex = (currentPage-1)*4;
        loadCard();
        currentPage--;
    }      
        
    @FXML
    public void nextPage(){
        if(currentIndex==productId.length)return;
        productList.getChildren().clear();
        loadCard();
        currentPage++;
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
        
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        combo();
    }

    
    
}
