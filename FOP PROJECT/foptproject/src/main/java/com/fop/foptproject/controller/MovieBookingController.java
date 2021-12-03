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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

/**
 * FXML Controller class
 *
 * @author shiao
 */
public class MovieBookingController implements Initializable {
    
    @FXML 
    private ChoiceBox selectCinema;
    @FXML 
    private ChoiceBox selectTime;
    @FXML 
    private DatePicker selectDate;
    @FXML 
    private CheckBox selectClassic;
    @FXML 
    private CheckBox selectPremium;
    
    @FXML
    public void changeToMoviesDetails(ActionEvent event) throws IOException{
        SceneController switchScene = new SceneController();
        switchScene.switchToMoviesDetails(event);
        
    }
    
    @FXML
    public void changeToSeats(ActionEvent event) throws IOException{
        SceneController switchScene = new SceneController();
        switchScene.switchToSeats(event);
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
