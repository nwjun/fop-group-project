
package com.fop.foptproject.controller;
import com.fop.Utility.emailTo;
import com.fop.Utility.Checker;
import com.fop.foptproject.App;
import com.fop.Utility.sqlConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author WeiXin
 */
public class LoginRegisterController implements Initializable {
    // class attribute
    private SceneController switchScene = new SceneController();
    private String OTP;
    
    // login side
    @FXML
    private Button loginButton;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
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
    private PasswordField RPasswordField;
    @FXML
    private PasswordField RConfirmPasswordField;
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
    public void backButton(ActionEvent event) throws IOException{
        SceneController scene = new SceneController();
        scene.switchToHome(event);
    }
    
    @FXML
    public void loginButton(ActionEvent event) throws SQLException, IOException{
        String email = emailField.getText();
        String password = passwordField.getText();
        login(email,password,event);
    }
    
    public void login(String email, String password, ActionEvent event) throws SQLException, IOException{        
        
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
                int permission = new sqlConnect().checkCredentials(email, password);
                
                // for reminder
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Invalid User Credential");
                switch(permission){
                    case 1:
                        //normal user scene
                        RealTimeStorage.updateUserInfos(email);
                        switchScene.switchToHomeLogined(event);
                        break;
                    case 2:
                        //admin scene
                        RealTimeStorage.updateUserInfos(email);
                        switchScene.switchToAdminMain(event);
                        break;
                    case 3:
                        //master scene
                        RealTimeStorage.updateUserInfos(email);
                        switchScene.switchToAdminMain(event);
                        break;
                    case -1:
                        passwordField.clear();
                        a.setContentText("Please enter the correct password");
                        Stage stageA = (Stage) a.getDialogPane().getScene().getWindow(); // get the window of alert box and cast to stage to add icons
                        stageA.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
                        stageA.showAndWait();
                        break;
                    case -2:
                        passwordField.clear();
                        emailField.clear();
                        a.setContentText("The email is not registered");
                        Stage stageB = (Stage) a.getDialogPane().getScene().getWindow(); // get the window of alert box and cast to stage to add icons
                        stageB.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
                        stageB.showAndWait();
                        break;
                }
            }   
        }
    }
   
    @FXML
    public void registerButton(ActionEvent event) throws IOException, SQLException{
        String username = usernameField.getText();
        String email = REmailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String password = RPasswordField.getText();
        String confirmPassword = RConfirmPasswordField.getText();
        
        boolean status = register(username,phoneNumber,email,password,confirmPassword);
        
        if (status){
            sendEmailVerification(email,username,phoneNumber,password);
            FXMLLoader fxmlloader = new FXMLLoader(App.class.getResource("OTP.fxml"));
            Parent root = fxmlloader.load();
            OTPController control = fxmlloader.getController();
            control.setDisplayEmail(email,username);
            switchScene.switchToOTPScene(event);     
        }
    
    }
    
    @FXML //link to login email checkfield
    public void checkEmailFormat(){
        String email = emailField.getText();
        if(!(Checker.checkEmail(email))){
           emailFieldWarning.setText("Invalid email format");
           loginButton.setDisable(true);
        }
        else{
            emailFieldWarning.setText(null);
            loginButton.setDisable(false);   
        }
    }
    
    @FXML //link to username textfield
    public void checkUsername(){
        String username = usernameField.getText();
        
        if(username.isBlank()){
            usernameFieldWarning.setText("Please enter a username");
            registerButton.setDisable(true);
            return;
        }
        registerButton.setDisable(false);
        usernameFieldWarning.setText(null);
        return;
    }
    
    @FXML // link to register email textfield
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
        else{
            REmailFieldWarning.setText(null);
            registerButton.setDisable(false);
        }
        
    }
    
    @FXML //link to both password textfield
    public void checkIdenticalPass(){
        String password = RPasswordField.getText();
        String confirmPassword = RConfirmPasswordField.getText();
        
        if(password.isBlank() || confirmPassword.isBlank()){
            registerButton.setDisable(true);
            return;
        }
        else if(!(password.equals(confirmPassword))){
            RPasswordFieldWarning.setText("Both of the password entered are not identical!");
            RConfirmPasswordFieldWarning.setText("Both of the password entered are not identical!");
            registerButton.setDisable(true);
            return;
        }
        
        registerButton.setDisable(false);
        RPasswordFieldWarning.setText(null);
        RConfirmPasswordFieldWarning.setText(null);
        return;
    }
    
    // helper method
    public void sendEmailVerification(String email,String userName, String phoneNumber, String password){
        String otp =  new emailTo(email).sendEmailVerification(userName,false);
        OTP = otp;
        boolean status = sqlConnect.addNewRegisterOTP(userName, email, phoneNumber, password, OTP); 
        
    }
    
    public boolean register(String userName, String phoneNumber, String email, String password, String confirmPassword) throws SQLException{
        sqlConnect conn2db = new sqlConnect();
        // check if there is any empty field
        if (userName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()){
            if (userName.isBlank()){
                usernameFieldWarning.setText("Please create an username!");
            }
            if (email.isBlank()){
                REmailFieldWarning.setText("Please create an email!");
            }
            if (password.isBlank()){
                RPasswordFieldWarning.setText("Please create a password!");
            }
            if (confirmPassword.isBlank()){
                RConfirmPasswordFieldWarning.setText("Please confirm your password!");
            }
            return false;
        }
        
        // if phonenumber is not filled
        if (phoneNumber.isBlank()){
            phoneNumber = null;
        }
       
        // check if the email inputted is registered
        int isDup = conn2db.checkDup(email, phoneNumber);

        // cut to OTP scene
        boolean status = false;
        
        switch(isDup){
            case 0:
                status = true;
                break;
            case -1:
                REmailFieldWarning.setText("This email is already registered");
                break;
            case -2:
                phoneNumberFieldWarning.setText("This phone number is already registered");
                break;
            case -3:
                REmailFieldWarning.setText("This email is already registered");
                phoneNumberFieldWarning.setText("This phone number is already registered");
                break;
            case -4:
                System.out.println("SQL error");
                break;
        }
 
        return status; // if everything is okay and verification email is sent. will return true to proceed to OTP
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
