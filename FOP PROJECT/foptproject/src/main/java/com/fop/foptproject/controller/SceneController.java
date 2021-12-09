/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.foptproject.controller;

import com.fop.foptproject.App;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SceneController {

    private Scene scene;
    private static Stage primaryStage;
    private final double WIDTH;
    private final double HEIGHT;

    public SceneController() {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        this.WIDTH = screenBounds.getWidth();
        this.HEIGHT = screenBounds.getHeight();
    }

    public static void setPrimaryStage(Stage primaryStage) {
        SceneController.primaryStage = primaryStage;
    }

    public static Stage showPopUpStage(String fxmlFile) {
        Parent root;

        try {
            root = FXMLLoader.load(App.class.getResource(fxmlFile));
            Scene scene = new Scene(root);
            Stage popupStage = new Stage();

            popupStage.initOwner(SceneController.primaryStage);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(scene);
            popupStage.setResizable(false);
            
            return popupStage;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        
        Parent root = FXMLLoader.load(App.class.getResource(fxmlFile));
        //lock landing H scroll
        if(fxmlFile.equals("App.fxml")){
            ScrollPane k = (ScrollPane) root;
            k.hvalueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                        Number old_val, Number new_val) {
                    k.setHvalue(50);
                }
            });
        
        }
        scene = new Scene(root, WIDTH, HEIGHT);
        this.primaryStage.setScene(scene);
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    private void switchScene(MouseEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(App.class.getResource(fxmlFile));
        //lock landing H scroll
        if(fxmlFile.equals("App.fxml")){
            ScrollPane k = (ScrollPane) root;
            k.hvalueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                        Number old_val, Number new_val) {
                    k.setHvalue(50);
                }
            });
        
        }
        scene = new Scene(root, WIDTH, HEIGHT);
        this.primaryStage.setScene(scene);
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    public void switchToOTPScene(ActionEvent event) throws IOException {
        switchScene(event, "OTP.fxml");
    }

    public void switchToRegisterAndLogin(ActionEvent event) throws IOException {
        switchScene(event, "LoginRegister.fxml");
    }
 
    public void switchToSeats(ActionEvent event) throws IOException{
        switchScene(event,"Seats.fxml");
    }
    
    public void switchToLandingPage(ActionEvent event) throws IOException{
        switchScene(event,"App.fxml");
    }
    
    public void switchToMoviesDetails(ActionEvent event) throws IOException{
        switchScene(event,"MoviesDetails.fxml");
    }
    
    public void switchToMovieBooking(ActionEvent event) throws IOException{
        switchScene(event,"MovieBooking.fxml");         
    }

    public void switchToHome(ActionEvent event) throws IOException {
        switchScene(event, "App.fxml");
    }

    public void switchToHome(MouseEvent event) throws IOException {
        switchScene(event, "App.fxml");
    }
    
    public void switchToAdminMovie(ActionEvent event) throws IOException {
        switchScene(event, "AdminMovie.fxml");
    }

    public void switchToFnB(ActionEvent event) throws IOException{
        switchScene(event,"FoodnBeverage.fxml");         
    }
    
    public void switchToAdminFood(ActionEvent event) throws IOException{
        switchScene(event, "AdminFood.fxml");
    }
    
    public void switchToAdminMain(ActionEvent event) throws IOException{
        switchScene(event, "AdminMain.fxml");
    }
    
    public void switchToCheckOut(ActionEvent event) throws IOException{
        switchScene(event, "CheckOut.fxml");
    }
}
