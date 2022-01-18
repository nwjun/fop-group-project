package com.fop.foptproject.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author WeiXin
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
        RealTimeStorage.clearBookingDetails();
        SceneController scene = new SceneController();
        scene.switchToHomeLogined(event);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // initialize the scene
        movieName.setText(RealTimeStorage.getMovieBooking().get("movieName").toString());
        cinema.setText(RealTimeStorage.getMovieBooking().get("cinemaName").toString());
        showDateTime.setText(RealTimeStorage.getMovieBooking().get("showdate").toString()+" "+RealTimeStorage.getMovieBooking().get("showTime").toString().split(" - ")[0]);
        transactionId.setText(RealTimeStorage.getBookingNumber());
        transactionTime.setText(RealTimeStorage.getTimestamp());
        amount.setText(RealTimeStorage.getToBePaid());
        
    }    
    
}
