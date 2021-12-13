/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.fop.foptproject.controller;

import com.fop.Utility.sqlConnect;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kuckn
 */
public class EditPricePopUpController implements Initializable {

    private HashMap<String, Double> tickets;
    private sqlConnect sql = new sqlConnect();
    
    @FXML
    private Button closeWindow;
    @FXML
    private TextField TS;
    @FXML
    private TextField TC;
    @FXML
    private TextField TE;
    @FXML
    private TextField TO;
    @FXML
    private TextField TP;
    @FXML
    private Button FinishEdit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getPrice();
    }    

    public void getPrice(){
        tickets = sql.queryTicketPrice();
        TS.setText(Double.toString(tickets.get("TS"))+"0");
        TC.setText(Double.toString(tickets.get("TC"))+"0");
        TE.setText(Double.toString(tickets.get("TE"))+"0");
        TO.setText(Double.toString(tickets.get("TO"))+"0");
        TP.setText(Double.toString(tickets.get("TP"))+"0");
    }
    
    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) closeWindow.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void TS(ActionEvent event) {
        sql.changeTicketPrice("TS",Double.parseDouble(TS.getText()));
        getPrice();
    }

    @FXML
    private void TC(ActionEvent event) {
        sql.changeTicketPrice("TC",Double.parseDouble(TC.getText()));
        getPrice();
    }

    @FXML
    private void TE(ActionEvent event) {
        sql.changeTicketPrice("TE",Double.parseDouble(TE.getText()));
        getPrice();
    }

    @FXML
    private void TO(ActionEvent event) {
        sql.changeTicketPrice("TO",Double.parseDouble(TO.getText()));
        getPrice();
    }

    @FXML
    private void TP(ActionEvent event) {
        sql.changeTicketPrice("TP",Double.parseDouble(TP.getText()));
        getPrice();
    }





    
}
