/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.fop.foptproject.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author shiao
 */
public class MoviesDetailsController implements Initializable {

    @FXML
    private Label movieTitle;
    @FXML
    private Label casts;
    @FXML
    private Label movieLanguage;
    @FXML
    private Label cinemas;
    @FXML
    private Label movieLength;
    @FXML
    private Label classicPrice;
    @FXML
    private Label premiumPrice;
    @FXML
    private Button booknowButton;
    
    @FXML
    public void changeToMovieBooking(ActionEvent event) throws IOException{
        SceneController switchScene = new SceneController();
        switchScene.switchToMovieBooking(event);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
