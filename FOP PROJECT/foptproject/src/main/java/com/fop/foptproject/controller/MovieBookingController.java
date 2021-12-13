/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.fop.foptproject.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

/**
 * FXML Controller class
 *
 * @author shiao
 */
public class MovieBookingController implements Initializable {
    
    @FXML 
    private ComboBox selectCinema;
    @FXML 
    private ComboBox selectTime;
    @FXML 
    private DatePicker selectDate;
    @FXML 
    private RadioButton selectClassic;
    @FXML 
    private RadioButton selectPremium;
    @FXML
    private Button homeButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button backButton;
    @FXML
    private Label cinemaFieldWarningText;
    @FXML
    private Label theaterTypeWarningText;
    @FXML
    private Label dateWarningText;
    @FXML
    private Label showTimeWarningText;
    
    private String chosenDay;
    private String chosenDate;
    private String chosenCinema;
    private String chosenTime;
    private String chosenType;
    
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
    
    @FXML
    public void validateCinemaField(){
        getCinema();
        if(this.chosenCinema.isEmpty()){
            cinemaFieldWarningText.setVisible(true);
            nextButton.setDisable(true);
        }
        else{
            cinemaFieldWarningText.setVisible(false);
        nextButton.setDisable(false);        
        }
    }

    @FXML
    public void validateDateField(){
        getDate();
        if(this.chosenDate.isEmpty()){
            dateWarningText.setVisible(true);
            nextButton.setDisable(true);
        }
        else{
            dateWarningText.setVisible(false);
        nextButton.setDisable(false);        
        }
    }

    @FXML
    public void validateShowtimeField(){
        //getTime();
        if(this.chosenTime.isEmpty()){
            showTimeWarningText.setVisible(true);
            nextButton.setDisable(true);    
        }
        else{
            showTimeWarningText.setVisible(false);
            nextButton.setDisable(false);
        }
    }
    
    @FXML
    public void next(ActionEvent event){
        
    }
    
    public void getTheaterType(){
        this.chosenType = (selectClassic.isPressed())?"Classic":"Premium";
    }
    
    public void getCinema(){
        this.chosenCinema = selectCinema.getValue().toString();
    }
    
    public void getDate(){
        this.chosenDate = selectDate.getValue().toString();
        LocalDate date = selectDate.getValue();
        Calendar c = Calendar.getInstance();
        // localdate -> date only
        // date -> localdate+atStartOfDay()+default time zone+toInstant()
        c.setTime(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        int day = (c.get(Calendar.DAY_OF_WEEK)==1)?6:c.get(Calendar.DAY_OF_WEEK)-2;
        String Day = Integer.toString(day);     
        this.chosenDay = Day;
    }
    
    public void getTime(){
        this.chosenTime = selectTime.getValue().toString();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nextButton.setDisable(true);
        
        // populate cinema choice
        selectCinema.getItems().add("KL - MidValley");
        // populate showtime choice
        
    }    
    
}
