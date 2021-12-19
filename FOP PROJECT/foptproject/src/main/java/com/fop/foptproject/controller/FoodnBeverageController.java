package com.fop.foptproject.controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import com.fop.foptproject.ProductCard;
import com.fop.Utility.sqlConnect;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author WeiXIn
 */
public class FoodnBeverageController implements Initializable{  
    // class attributes
    private sqlConnect sql = new sqlConnect();
    private Object[] productId;
    private Object[] price;
    private Object[] posterPath;
    private Object[] productDesc;
    private Object[] productName;
    private double IMGW = 180.0 ;
    private double IMGH = 185.5;
    private double SCALE = 0.9;
    private double TRANSLATEX = 333;
    private int currentIndex = 0;
    private int currentPage = 0;
    private int maxPage;
    private double totalAmount = 0;
    
    @FXML
    private GridPane productList;
    @FXML
    private BorderPane popUpPane;
    @FXML
    private ScrollPane shoppingList;
    @FXML
    private StackPane boxBlur;
    @FXML
    private VBox priceContainer;
    @FXML
    private VBox nameContainer;
    @FXML
    private VBox quantityContainer;
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
    private Button closePopUp;
    @FXML
    private Button openPopUp;
    @FXML
    private Button backButton;
    @FXML
    private Button checkOutButton;
    @FXML 
    private Label title;
    @FXML
    private Label cartValue;
    @FXML
    private Line highlightLine;
    
    public void getProduct(String category){
        HashMap<String,ArrayList<String>> items = sql.queryProduct(category);
        this.productId = items.get("productId").toArray();
        this.price = items.get("price").toArray();
        this.posterPath = items.get("posterPath").toArray();
        this.productDesc = items.get("productDesc").toArray();
        this.productName = items.get("productName").toArray();
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
                    ProductCard content = new ProductCard((String)productId[currentIndex],(String)posterPath[currentIndex],IMGW,IMGH,SCALE,Double.parseDouble((String)price[currentIndex]),(String)productName[currentIndex],(String)productDesc[currentIndex]);
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
    public void backButton(ActionEvent event) throws IOException{
        new SceneController().switchToSeats(event);
    }
    
    @FXML
    public void checkOut(ActionEvent event) throws IOException{
        if(RealTimeStorage.getMovieBooking().get("theaterType").equals("Premium")){
            String key = "S000005P";
            int quantity = 0;
            for(int i = 0 ; i < RealTimeStorage.ticketTypeQuantity ; i++){
                quantity += RealTimeStorage.getTicketType()[i];
            }
            if(RealTimeStorage.FoodnBeverage.containsKey(key)){
                RealTimeStorage.FoodnBeverage.replace(key, quantity);
            }
            else if(!(RealTimeStorage.FoodnBeverage.containsKey(key))){
                RealTimeStorage.FoodnBeverage.put(key,quantity);                 
            }
        
        }
        new SceneController().switchToCheckOut(event);
    }
    
    @FXML 
    public void openPopUp(){
        BoxBlur b = new BoxBlur(10,10,2);
        boxBlur.setEffect(b);
        lineAnimation(1800,0,popUpPane);
        HashMap<String,Integer> current = RealTimeStorage.getFnB();
        double price;
        String name;
        sqlConnect sql = new sqlConnect();
        int i = 0;
        for(String key:current.keySet()){
           if(i==0){
                Label productName = new Label("Product Name");
                Label quantity = new Label("Quantity");     
                Label productPrice = new Label("Price");
                productName.getStyleClass().add("shoppingCartTitle");
                quantity.getStyleClass().add("shoppingCartTitle");
                productPrice.getStyleClass().add("shoppingCartTitle");
                
                this.priceContainer.getChildren().add(productPrice);
                this.nameContainer.getChildren().add(productName);
                this.quantityContainer.getChildren().add(quantity);
                i++;
           }
           
           if(key.equals("S000005P"))continue; // skip premium gift
           
           price = Double.parseDouble(sql.queryProductInfo(key,"price"));
           name = sql.queryProductInfo(key,"productname");
           
           Label productName = new Label(name);
           Label productPrice = new Label(String.format("RM%.2f",price*current.get(key)));
           Label quantity = new Label(Integer.toString(current.get(key)));         
           productName.getStyleClass().add("shoppingCartContent");
           quantity.getStyleClass().add("shoppingCartContent");
           productPrice.getStyleClass().add("shoppingCartContent");
           
           this.totalAmount += current.get(key)*price;
           this.priceContainer.getChildren().add(productPrice);
           this.nameContainer.getChildren().add(productName);
           this.quantityContainer.getChildren().add(quantity);
        }
        cartValue.setText(String.format("TOTAL:RM%.2f",this.totalAmount));
        
    }
    
    @FXML 
    public void closePopUp(){
        boxBlur.setEffect(null);
        lineAnimation(0,1800,popUpPane);
        this.priceContainer.getChildren().clear();
        this.nameContainer.getChildren().clear();
        this.quantityContainer.getChildren().clear();
        this.totalAmount = 0;
    }
    
    @FXML
    public void popcorn(){
        lineAnimation(TRANSLATEX,484,highlightLine);
        TRANSLATEX = 484;
        title.setText("POPCORNS");
        productList.getChildren().clear();
        getProduct("popcorn");
        checkPage();
    }
    
    @FXML
    public void combo(){
        lineAnimation(TRANSLATEX,333,highlightLine);
        TRANSLATEX = 333;
        title.setText("COMBO SETS");
        productList.getChildren().clear();
        getProduct("combo");
        checkPage();
    }
    
    @FXML
    public void carte(){
        lineAnimation(TRANSLATEX,634,highlightLine);
        TRANSLATEX = 634;
        title.setText("A LA CARTE");
        productList.getChildren().clear();
        getProduct("carte");
        checkPage();
    }
    
    @FXML
    public void beverage(){
        lineAnimation(TRANSLATEX,785,highlightLine);
        TRANSLATEX = 785;
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
    public void initialize(URL url, ResourceBundle rb){
        combo();
    }

    
    
}
