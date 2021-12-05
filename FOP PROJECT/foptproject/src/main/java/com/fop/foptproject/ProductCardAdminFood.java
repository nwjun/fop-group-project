/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.foptproject;

import com.fop.foptproject.controller.FoodnBeverageController;
import com.fop.sqlUtil.sqlConnect;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 *
 * @author WeiXin
 */
public class ProductCardAdminFood{
    private HashMap<String,Object> productDetails = new HashMap<>();
    private HBox productCard;
    private Label quantity;
    private static HashMap<String,Integer> tempStorage = new HashMap<>();
    private static double totalAmount = 0;
    private boolean deletestatus = false;
    
    public ProductCardAdminFood(String productID,String imgPath,double imgW,double imgH,double scale,double price,String productName,String productDesc, String category){
        productDetails.put("productID",productID);
        productDetails.put("imgPath",imgPath);
        productDetails.put("imgW",imgW*scale);
        productDetails.put("imgH",imgH*scale);
        productDetails.put("price",price);
        productDetails.put("productName",productName);
        productDetails.put("productDesc",productDesc);
        productDetails.put("category", category);
               
        this.productCard = makeCard(productID,price);
        HBox.setMargin(this.productCard,new Insets(0,45,0,0));   
    }
        
    public HashMap<String,Object> getProductDetails(){
        return productDetails;
    }
    
    public Object getValue(String key){
        return this.productDetails.get(key);
    }
    
    public HBox getCard(){
        
        return this.productCard;
    }
        
    private HBox makeCard(String productId, double price){
        HBox card = new HBox();
        
        VBox img = productImage(productId);
        img.setPrefHeight(250);
        img.setPrefHeight(250);
        
        VBox productDetails = makeProductDescription(productId, price);
        productDetails.setPrefHeight(250);
        productDetails.setPrefWidth(401);
        
        card.getChildren().addAll(img,productDetails);
        return card;
    }
    
    private Image getImage(){
        String path = getClass().getResource((String)getValue("imgPath")).toString();
            Image img = new Image(path,(double)getValue("imgW"),(double)getValue("imgH"),false,false);
        return img;
    }

    
    private VBox makeProductDescription(String productId, double price){
        
        Label productName = new Label();
        Label productDesc = new Label();
        Label priceTag = new Label();    
        Label category = new Label();
        Label path = new Label();
        VBox labelContainer = new VBox();
        
        path.setId(productId+"_path");
        category.setId(productId+"_category");
        productName.setId(productId+"_name");
        productDesc.setId(productId+"_desc");
        priceTag.setId(productId+"_priceTag");    
        path.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        category.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        productName.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        productDesc.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        priceTag.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px;-fx-alignment:center"); 
        path.setText("Path: "+ (String)this.productDetails.get("imgPath"));
        category.setText("Category: "+(String)this.productDetails.get("category"));
        productName.setText("Name: "+(String)this.productDetails.get("productName"));
        priceTag.setText("Price: "+String.format("RM%.2f",price));
        productDesc.setText("Description: "+(String)this.productDetails.get("productDesc"));           

        AnchorPane editdelete = MakeButton();
        
        labelContainer.setPrefWidth(652);
        labelContainer.setPrefHeight(166);
        labelContainer.setStyle("-fx-padding:0 0 0 20");
        labelContainer.getChildren().addAll(path, category, productName, priceTag, productDesc, editdelete);
        

        return labelContainer;
    }
    
    private boolean DeletePopUp(){ 
        Stage window = new Stage();
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Delete \""+ this.productDetails.get("productName")+ "\"");
        window.setMinWidth(300);
        window.setMinHeight(185);
        window.setY(370);
        window.setX(600);
        
        
        Label label = new Label();
        label.setText("Confirm Deletion of \"" + this.productDetails.get("productName")+ "\"");
        label.setStyle("-fx-font-size: 16px");
        label.setPrefHeight(1);
        
        Label instruction = new Label("Type \"DELETE\"");
        
        TextField deleteText = new TextField();
        deleteText.setPromptText("Type \"DELETE\"");
        
        Button deleteButton = new Button();
        deleteButton.setText("Delete");
        deleteButton.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
        deleteButton.setOnAction(e->{
            if(deleteText.getText().equals("DELETE")){
                this.deletestatus = true;
                window.close();
        }
        });
        
        Button cancel = new Button("Cancel");
        cancel.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
        cancel.setOnAction(e -> window.close());
        
        Label spacing = new Label("     ");
        Label spacing1 = new Label("     ");
                
        
        HBox button = new HBox();
        button.setAlignment(Pos.BASELINE_RIGHT);
        button.setStyle("-fx-padding: 10 0 0 0");
        button.getChildren().addAll(deleteButton,spacing, cancel, spacing1);
        
        VBox layout = new VBox(10);
        layout.setStyle("-fx-background: #141414;-fx-font-family: \"Montserrat\";-fx-text-fill: #ffffff;");
        layout.getChildren().addAll(label,instruction, deleteText, button);
        
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        
        return this.deletestatus;
    }
    
    private AnchorPane MakeButton(){
        Button edit = new Button();
        Button delete = new Button();
        
        edit.setText("Edit");
        edit.setLayoutX(45);
        edit.setLayoutY(88);
        edit.setPrefWidth(100);
        edit.setPrefHeight(31);    
        edit.setStyle("-fx-border-radius:20px;-fx-border-color:#FFEE00;-fx-border-width:1px;-fx-background-color:tranparent;-fx-text-fill:#FFEE00");
        
        edit.setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t){
                edit.setStyle("-fx-opacity: 0.6;-fx-background-radius:15px;-fx-border-radius:20px;-fx-border-color:#FFEE00;-fx-border-width:1px;-fx-background-color:#FFEE00;-fx-text-fill:#000000");
                
            }
        });
        edit.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t){
                edit.setStyle("-fx-border-radius:20px;-fx-border-color:#FFEE00;-fx-border-width:1px;-fx-background-color:tranparent;-fx-text-fill:#FFEE00");
        
            }
        });
        
        delete.setText("Delete");
        delete.setLayoutX(193);
        delete.setLayoutY(88);
        delete.setPrefWidth(100);
        delete.setPrefHeight(31);        
        delete.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
        
        delete.setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t){
                delete.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px;-fx-opacity : 0.6");
            
            }
        });
        delete.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t){
                delete.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
        
            }
        });  

        delete.setOnAction(e->{
            this.deletestatus = DeletePopUp();
            if(this.deletestatus) 
                System.out.println("Link to MySQL to delete");
        }); 

        AnchorPane editdelete = new AnchorPane();
        editdelete.setStyle("-fx-padding: 0 0 0 0");
        editdelete.setPrefWidth(401);
        editdelete.setPrefHeight(84);
        editdelete.getChildren().addAll(edit, delete);
        
        return editdelete;       
    }
    
    private VBox productImage(String productId){
        Image productImg = getImage();
        ImageView img = new ImageView(productImg);
        img.setId(productId+"_img");
        img.setFitWidth((double)this.productDetails.get("imgW"));
        img.setFitHeight((double)this.productDetails.get("imgH"));
        img.setPreserveRatio(true);
        img.setSmooth(true);
        
        
        VBox container = new VBox();
        container.setStyle("-fx-padding:14 0 0 13");
        container.getChildren().addAll(img);
        
        return container;
    }
}
