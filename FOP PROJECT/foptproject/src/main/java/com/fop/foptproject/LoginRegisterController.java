/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.foptproject;
import com.fop.checker.Checker;
import com.fop.sqlUtil.sqlConnect;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

/**
 *
 * @author User
 */
public class LoginRegisterController implements Initializable {
    // login side
    @FXML
    private Button loginButton;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label emailFieldWarning;
    @FXML
    private Label passwordFieldWarning; // both are empty and invisible at first
    
    // register side
    @FXML
    private Button registerButton;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField REmailField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField RPasswordField;
    @FXML
    private TextField RConfirmPasswordField;
    @FXML
    private Label usernameFieldWarning;
    @FXML
    private Label REmailFieldWarning;
    @FXML
    private Label phoneNumberFieldWarning;
    @FXML
    private Label RPasswordFieldWarning;
    @FXML
    private Label RConfirmPasswordFieldWarning;
    
    
    @FXML
    public void loginButton(){
        String email = emailField.getText();
        String password = passwordField.getText();
        login(email,password);
    }
    
    public void login(String email, String password){        
        
        if (email.isBlank() || password.isBlank()){
            if (email.isBlank()){
                emailFieldWarning.setText("Empty Email Field!");
             }
             if (password.isBlank()){
                passwordFieldWarning.setText("Empty Password Field!");
             }
        }
        else {           
            if(!(Checker.checkEmail(email))){
                emailFieldWarning.setText("Invalid Email");
                emailField.clear();
                passwordField.clear();
            }
            else{
                int permission = sqlConnect.checkCredentials(email, password);

                switch(permission){
                    case 1:
                        //normal user scene
                    case 2:
                        //admin scene
                    case 3:
                        //master scene
                    case -1:
                        passwordField.clear();
                    case -2:
                        passwordField.clear();
                        emailField.clear();
                }
            }   
        }
    }
   
    @FXML
    public void registerButton(){
        String username = usernameField.getText();
        String email = REmailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String password = RPasswordField.getText();
        String confirmPassword = RConfirmPasswordField.getText();
        
        boolean status = register(username,phoneNumber,email,password,confirmPassword);
        
        if (status){
            // s
        }
    
    }
    
    @FXML
    public void checkEmailFormat(){
        String email = emailField.getText();
        if(!(Checker.checkEmail(email))){
            emailFieldWarning.setText("Invalid email format");
           loginButton.setDisable(true);
        }
        loginButton.setDisable(false);
    }
    
    @FXML
    public void checkUsername(){
        String username = usernameField.getText();
        
        if(username.isBlank()){
            usernameFieldWarning.setText("Please enter a username");
            registerButton.setDisable(true);
        }
        registerButton.setDisable(false);
    }
    
    @FXML
    public void checkREmail(){
        String email = REmailField.getText();
        
        if (email.isBlank()){
            REmailFieldWarning.setText("Please enter your email");
            registerButton.setDisable(true);
        }
        else if(!(Checker.checkEmail(email))){
            REmailFieldWarning.setText("Invalid email format");
            registerButton.setDisable(true);
        }
        registerButton.setDisable(false);
    }
    
    @FXML
    public void checkIdenticalPass(){
        String password = RPasswordField.getText();
        String confirmPassword = RConfirmPasswordField.getText();
        
        if(password.isBlank() || confirmPassword.isBlank()){
            registerButton.setDisable(true);
        }
        else if(!(password.equals(confirmPassword))){
            RPasswordFieldWarning.setText("Both of the password entered are not identical!");
            RConfirmPasswordFieldWarning.setText("Both of the password entered are not identical!");
            registerButton.setDisable(true);
        }
        
        registerButton.setDisable(false);
    }
    
    
    public boolean register(String userName, String phoneNumber, String email, String password, String confirmPassword){
        // check if there is any empty field
        if (userName.isBlank() || phoneNumber.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()){
            if (userName.isBlank()){
                usernameFieldWarning.setText("Please create a password!");
            }
            if (email.isBlank()){
                REmailFieldWarning.setText("Please create a password!");
            }
            if (password.isBlank()){
                RPasswordFieldWarning.setText("Please create a password!");
            }
            if (confirmPassword.isBlank()){
                RConfirmPasswordFieldWarning.setText("Please confirm your password!");
            }
            return false;
        }
       
        // check if the email inputted is registered
        int isDup = sqlConnect.checkDup(email, phoneNumber);
        
        boolean status = false;
        switch(isDup){
            case 0:
                status = sqlConnect.addNewUser(userName,email,password,phoneNumber,1);
            case -1:
                REmailFieldWarning.setText("This email is already registered");
            case -2:
                phoneNumberFieldWarning.setText("This phone number is already registered");
            case -3:
                REmailFieldWarning.setText("This email is already registered");
                phoneNumberFieldWarning.setText("This phone number is already registered");
            case -4:
                System.out.println("SQL error");
        }
 
        return status;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
