package com.fop.foptproject.controller;

/**
 * Author:WeiXin
 */

import com.fop.EmailUtil.emailTo;
import com.fop.Utility.sqlConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;



public class OTPController implements Initializable{
    @FXML
    private Button resendButton;
    @FXML
    private TextField OTPField;
    @FXML
    private Label OTPFieldWarning;
    @FXML
    private Button verifyButton;
    @FXML
    private Label sentToEmail;

    // class side
    private int i = 60;
    private String text = "Resend (60)";
    private static String email;
    private static String firstName;
    

    @FXML
    public void resendOTP(ActionEvent event){
        String OTP = new emailTo(email).sendEmailVerification(firstName,false);
        sqlConnect.updateOTP(email, OTP);
        startCountDown();
        
    }
    
    @FXML 
    public void backButton(ActionEvent event) throws IOException{
        new SceneController().switchToLandingPage(event);
    }
    
    @FXML
    public void verifyPrimer(ActionEvent event) throws IOException{
        Date dateObj = new Date();
        Timestamp currentTime = new Timestamp(dateObj.getTime());        
        String OTPEntered = OTPField.getText();
        String OTPRequired = sqlConnect.queryOTP(email);
        Timestamp timeIssued = sqlConnect.queryTimestamp(email);
        
        long differenceInSeconds = ((int)(currentTime.getTime() - timeIssued.getTime()))/1000;
        double differenceInMinutes = differenceInSeconds/3600.0;
        
        if(OTPEntered.isBlank()){
            OTPFieldWarning.setText("Please enter your OTP"); 
            OTPField.setStyle("-fx-border-color:#FF0000;-fx-border-width:1px;-fx-pref-height:39px;-fx-pref-width:200px;-fx-background-radius:8 0 0 8;-fx-border-radius:8 0 0 8;");
        }
        else if(differenceInMinutes > 5){
            OTPField.clear();
            OTPFieldWarning.setText("This OTP is expired"); 
            OTPField.setStyle("-fx-border-color:#FF0000;-fx-border-width:1px;-fx-pref-height:39px;-fx-pref-width:200px;-fx-background-radius:8 0 0 8;-fx-border-radius:8 0 0 8;");     
        }
        else if(OTPRequired.equals(OTPRequired)){
            boolean status = sqlConnect.removeNewRegisterOTP(email, false);
            if (status){ 
                SceneController switchScene = new SceneController();
                switchScene.switchToRegisterAndLogin(event);
                System.out.println("Done registration");
            }
        }
        else if(!(OTPRequired.equals(OTPRequired))){
            OTPField.clear();
            OTPFieldWarning.setText("Please enter the correct OTP"); 
            OTPField.setStyle("-fx-border-color:#FF0000;-fx-border-width:1px;-fx-pref-height:39px;-fx-pref-width:200px;-fx-background-radius:8 0 0 8;-fx-border-radius:8 0 0 8;");
        }
    }
    
    // resend cool down
    public void startCountDown(){
        i = 60;
        resendButton.setDisable(true);
        resendButton.setText(text);
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), e->{
            i--;
            text = String.format("Resend (%02d)",i);
            resendButton.setText(text);
            
            if (i == 0){
                resendButton.setDisable(false);
                resendButton.setText("Resend OTP");
                text = "Resend (60)";
            }
        }));
        
        timer.setCycleCount(60);
        timer.play();   
    }
    
    public void setDisplayEmail(String useremail, String username){
        email = useremail;
        firstName = username;
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sentToEmail.setText("A one-time-password has been sent to "+email);
        startCountDown();
    }    
    
}
