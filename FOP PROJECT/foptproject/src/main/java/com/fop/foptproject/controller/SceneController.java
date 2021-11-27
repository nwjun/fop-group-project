/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.foptproject.controller;

import com.fop.foptproject.App;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SceneController {
    private Scene scene;
    private Stage stage;
    private final double WIDTH;
    private final double HEIGHT;
    
    public SceneController(){
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        this.WIDTH = screenBounds.getWidth();
        this.HEIGHT = screenBounds.getHeight();
    }
    
    public void switchToOTPScene(ActionEvent event) throws IOException{
        BorderPane root = FXMLLoader.load(App.class.getResource("OTP.fxml"));
        stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
        scene = new Scene(root,WIDTH,HEIGHT);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
    
    public void switchToRegisterAndLogin(ActionEvent event) throws IOException{
        AnchorPane root = FXMLLoader.load(App.class.getResource("login.fxml"));
        stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
        scene = new Scene(root,WIDTH,HEIGHT);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
 
}
