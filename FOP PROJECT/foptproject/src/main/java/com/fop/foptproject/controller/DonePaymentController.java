package com.fop.foptproject.controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author User
 */
public class DonePaymentController implements Initializable {

    @FXML
    private Label movieName;
    @FXML
    private Label cinema;
    @FXML
    private Label showDateTime;
    @FXML
    private Label transactionId;
    @FXML
    private Label transactionTime;
    @FXML
    private Label amount;
    @FXML 
    private Button toHomeButton;
    
    @FXML
    public void toHome(ActionEvent event) throws IOException{
        SceneController scene = new SceneController();
        scene.switchToHomeLogined(event);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // initialize the scene
//        movieName.setText();
//        cinema.setText("Kuala Lumpur - Mid Valley");
//        showDateTime.setText();
//        transactionId.setText();
//        transactionTime.setText();
//        amount.setText();
        
    }    
    
}
