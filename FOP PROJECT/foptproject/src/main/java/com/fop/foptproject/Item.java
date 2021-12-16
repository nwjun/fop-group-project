/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fop.foptproject;

import com.fop.foptproject.controller.RealTimeStorage;
import com.fop.foptproject.controller.SceneController;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author jun
 */
public class Item {

    /**
     * Main class for other item like movies and food.
     */
    private HashMap<String, Object> item = new HashMap<String, Object>();
    private StackPane card;

    Item(String itemID, String itemName, String posterPath, double posterW, double posterH, double scale) {
        /**
         * Constructor for Item object.
         *
         * @param itemID unique id of item
         * @param itemName name of the item to be shown
         * @param posterPath relative path to the poster of item
         * @param posterW default width of poster
         * @param posterH default height of poster
         * @param scale magnitude of scaling for poster
         */

        // Add key and value to Hashmap "item"
        item.put("itemID", itemID);
        item.put("itemName", itemName);
        item.put("posterPath", posterPath);
        // Create and store StackPane which consist of poster and title in variable "card"
        card = createCard(posterW, posterH, scale);
    }

    public void setDetail(String key, Object value) {
        /**
         * Setter to add key and value into "item".
         *
         * @param key Key of HashMap
         * @param value Value to be set
         */

        item.put(key, value);
    }

    public void updateDetail(String key, Object value) {
        /**
         * Setter to update value of HashMap, will throw error if key doesn't
         * exist.
         *
         * @param key Key that is existed in HashMap
         * @param value Value to be updated
         */

        try {
            item.replace(key, value);
        } catch (Exception e) {
            System.err.println("Key not in hashmap:" + e);
        }
    }

    public HashMap<String, Object> getItem() {
        /**
         * Getter for private variable "item".
         * @return private variable "item"
         */

        return item;
    }

    public StackPane getCard() {
        /**
         * Getter for private variable "card"
         *
         * @return private variable "card"
         */

        return this.card;
    }

    public int getSize() {
        /**
         * Getter for number of field of a item
         *
         * @return number of field of a item
         */

        return this.item.size();
    }

    public Object getDetail(String key) {
        /**
         * Getter for value stored in item. Value is returned in Object. Have to
         * convert it before using.
         *
         * @param key Key that is existed in HashMap
         * 
         * @return Object(value) if key existed; <code>null</code> otherwise
         */
        return item.get(key);
    }

    public String[] getKeys() {
        /**
         * Getter for all the keys of HashMap.
         *
         * @return Keys of HashMap
         */

        // toArray() returns an Object[], regardless of generics.
        // cast it to String
        String[] keys = this.item.keySet().toArray(new String[this.getSize()]);

        return keys;
    }

    private Image getPoster(double posterW, double posterH, double scale) {
        /**
         * Return Image Class of poster from "posterPath"
         *
         * @param posterW default width of poster
         * @param posterH default height of poster
         * @param scale magnitude of scaling for poster
         * 
         * @return poster in Image
         */

        // Get posterPath in String
        String path = getClass().getResource((String) this.getDetail("posterPath")).toString();
        // Image(String url, double requestedWidth, double requestedHeight, boolean preserveRatio, boolean smooth)
        Image image = new Image(path, posterW * scale, posterH * scale, false, false);;

        return image;
    }

    public StackPane createCard(double posterW, double posterH, double scale) {
        /**
         * Create card that consist of poster and title.
         *
         * @param posterW default width of poster
         * @param posterH default height of poster
         * @param scale magnitude of scaling for poster
         * 
         * @return StackPane that act as a card which consists of poster and title
         */
        
        StackPane card = new StackPane();
        String name = (String) this.getDetail("itemName");
        String id = (String) this.getDetail("itemID");

        Label title = new Label(name);
        title.setTextFill(Color.color(1, 1, 1));
        // Add id for title
        title.setId(id + "Title");
        // Make title settle below poster
        StackPane.setAlignment(title, Pos.BOTTOM_CENTER);
        
        StackPane imageContainer = new StackPane();
        Image poster = this.getPoster(posterW, posterH, scale);
        ImageView imageView = new ImageView(poster);
        // Add id for poster
        imageView.setId(id+"Img");
        imageContainer.setId(id);
        // Add animation to mouse event of movie card only
        try{
            if(!(RealTimeStorage.getPermission().equals("2") || RealTimeStorage.getPermission().equals("3"))){
                Integer.parseInt(id.substring(0,1));
                imageContainer.setOnMouseEntered(event -> startAnimation(event));
                imageContainer.setOnMouseExited(event -> stopAnimation(event));
            }      
        }
        catch(Exception e){
            // Do nothing
        }
        imageContainer.getChildren().add(imageView);
        
        // Add title and poster to card
        card.getChildren().addAll(title, imageContainer);

        return card;
    }

    public void startAnimation(MouseEvent event) {
        final StackPane source = (StackPane) event.getSource();
        source.getChildren().get(0).setEffect(new BoxBlur(5,5,2));
        source.getChildren().get(0).setOpacity(0.4);
        VBox buttonContainer = new VBox();
        Button book = new Button();
        Region seperator = new Region();
        Button info = new Button();

        info.setText("Info");
        info.setId(source.getId());
        info.setMinSize(70,30);
        book.setText("Book");
        book.setId(source.getId());
        book.setMinSize(70,30);
        seperator.setMinHeight(15);
        info.getStyleClass().add("InfoButton");
        book.getStyleClass().add("BookButton");
        info.setOnAction(e->{
            try {
                toMovieDetails(e);
            } catch (IOException ex) {
                Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        book.setOnAction(e->{
            if(!RealTimeStorage.getIsLogin()){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Unauthorized Access");
                a.setContentText("Please log in to book a movie");
                Stage stage = (Stage) a.getDialogPane().getScene().getWindow(); // get the window of alert box and cast to stage to add icons
                stage.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
                stage.showAndWait();
                try {
                    new SceneController().switchToRegisterAndLogin(e);
                } catch (IOException ex) {
                    return;
                }
                return;
            }
            try {
                toMovieBooking(e);
            } catch (IOException ex) {
                Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        buttonContainer.getChildren().addAll(info,seperator,book);
        buttonContainer.setAlignment(Pos.CENTER);
        source.getChildren().add(buttonContainer);

    }

    public void stopAnimation(MouseEvent event) {
            final StackPane source = (StackPane) event.getSource();
            source.getChildren().get(0).setEffect(null);
            source.getChildren().get(0).setOpacity(1);
            source.getChildren().remove(1,2);
    }
    
    public void toMovieBooking(ActionEvent e) throws IOException{
        Button book = (Button)e.getSource();
        String id = book.getId();
        System.out.println(id);
        RealTimeStorage.setLookingAt(id);
        new SceneController().switchToMovieBooking(e);
    }
    
    public void toMovieDetails(ActionEvent e) throws IOException{
        Button info = (Button)e.getSource();
        String id = info.getId();
        System.out.println(id);
        RealTimeStorage.setLookingAt(id);
        new SceneController().switchToMoviesDetails(e);
    }
    
    @Override
    public String toString() {
        /**
         * Return string to be printed that shows all the keys and values in HashMap.
         * 
         * @return String that describes HashMap.
         */
        String id = (String) this.getDetail("itemID");
        String name = (String) this.getDetail("itemName");
        String path = (String) this.getDetail("posterPath");
        String[] keys = this.getKeys();
        String statement = "";

        for (String key : keys) {
            Object obj = this.getDetail(key);
            String objClass = obj.getClass().getName();
            String detail = "";

            if (objClass.equals("[Ljava.lang.String;")) {
                detail = Arrays.toString((Object[]) obj);
            } else if (objClass.equals("java.lang.Double") || objClass.equals("java.lang.Integer")) {
                detail = String.valueOf(obj);
            } else {
                detail = (String) this.getDetail(key);
            }
            statement += key + ": " + detail + "\n";
        }

        return statement;
    }

}
