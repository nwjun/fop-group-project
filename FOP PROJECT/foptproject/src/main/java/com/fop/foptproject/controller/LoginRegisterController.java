package com.fop.foptproject.controller;

import com.fop.Utility.emailTo;
import com.fop.Utility.Checker;
import com.fop.foptproject.App;
import com.fop.Utility.sqlConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author WeiXin
 */
public class LoginRegisterController implements Initializable {

    // class attribute
    private SceneController switchScene = new SceneController();
    private String OTP;
    private int dot = 0;
    private boolean status = false;
    private ActionEvent events = null;
    private int permissionLevel = 0;

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

    // loading screen
    @FXML
    private StackPane loadingScreen;
    @FXML
    private Label loading;

    @FXML
    public void backButton(ActionEvent event) throws IOException {
        SceneController scene = new SceneController();
        scene.switchToHome(event);
    }

    @FXML
    public void loginButton(ActionEvent event) throws SQLException, IOException {
        events = event;
        String email = emailField.getText();
        String password = passwordField.getText();
        if (email.isBlank() || password.isBlank()) {
            if (email.isBlank()) {
                emailFieldWarning.setText("Empty Email Field!");
            }
            if (password.isBlank()) {
                passwordFieldWarning.setText("Empty Password Field!");
            }
        } else {
            if (!(Checker.checkEmail(email))) {
                emailFieldWarning.setText("Invalid Email");
                emailField.clear();
                passwordField.clear();
            } else {
                Task<Void> loginTask = loginTask();
                new Thread(loginTask).start();
                loginTask.setOnSucceeded(eh -> {
                    if (permissionLevel > 0) {
                        closeLoadingScreen();
                        try {
                            switchSceneFromLogin(permissionLevel);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @FXML
    public void registerButton(ActionEvent event) throws IOException, SQLException {
        events = event;

        // get user input
        String userName = usernameField.getText();
        String email = REmailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String password = RPasswordField.getText();
        String confirmPassword = RConfirmPasswordField.getText();

        // check if there is any empty field
        if (userName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            if (userName.isBlank()) {
                usernameFieldWarning.setText("Please create an username!");
            }
            if (email.isBlank()) {
                REmailFieldWarning.setText("Please create an email!");
            }
            if (password.isBlank()) {
                RPasswordFieldWarning.setText("Please create a password!");
            }
            if (confirmPassword.isBlank()) {
                RConfirmPasswordFieldWarning.setText("Please confirm your password!");
            }
            return;
        }

        Task<Void> registerTask = registerTask();
        new Thread(registerTask).start();
        registerTask.setOnSucceeded(eh -> {
            closeLoadingScreen();
            if (status) {
                closeLoadingScreen();
                try {
                    switchSceneFromRegister(email, userName);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    @FXML //link to login email checkfield
    public void checkEmailFormat() {
        String email = emailField.getText();
        if (!(Checker.checkEmail(email))) {
            emailFieldWarning.setText("Invalid email format");
            loginButton.setDisable(true);
        } else {
            emailFieldWarning.setText(null);
            loginButton.setDisable(false);
        }
    }

    @FXML //link to username textfield
    public void checkUsername() {
        String username = usernameField.getText();

        if (username.isBlank()) {
            usernameFieldWarning.setText("Please enter a username");
            registerButton.setDisable(true);
            return;
        }
        registerButton.setDisable(false);
        usernameFieldWarning.setText(null);
        return;
    }

    @FXML // link to register email textfield
    public void checkREmail() {
        String email = REmailField.getText();
        if (email.isBlank()) {
            REmailFieldWarning.setText("Please enter your email");
            registerButton.setDisable(true);
        } else if (!(Checker.checkEmail(email))) {
            REmailFieldWarning.setText("Invalid email format");
            registerButton.setDisable(true);
        } else {
            REmailFieldWarning.setText(null);
            registerButton.setDisable(false);
        }

    }

    @FXML //link to both password textfield
    public void checkIdenticalPass() {
        String password = RPasswordField.getText();
        String confirmPassword = RConfirmPasswordField.getText();

        if (password.isBlank() || confirmPassword.isBlank()) {
            registerButton.setDisable(true);
            return;
        } else if (!(password.equals(confirmPassword))) {
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
    public void sendEmailVerification(String email, String userName, String phoneNumber, String password) {
        String otp = new emailTo(email).sendEmailVerification(userName, false);
        OTP = otp;
        boolean status = sqlConnect.addNewRegisterOTP(userName, email, phoneNumber, password, OTP);

    }

    public Task loginTask() {
        // create a Task
        Task<Void> createTask = new Task<>() {

            @Override
            protected Void call() throws Exception {
                //Background work                       
                final CountDownLatch latch = new CountDownLatch(1);

                // Access JavaFX UI from background thread
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            showLoadingScreen();
                            startProgress();
                        } finally {
                            latch.countDown();
                        }
                    }
                });

                // Wait for the UI animation to be executed
                latch.await();
                String email = emailField.getText();
                String password = passwordField.getText();
                login(email, password, events);

                return null;
            }
        };

        return createTask;
    }

    public void login(String email, String password, ActionEvent event) throws SQLException, IOException {

        permissionLevel = sqlConnect.checkCredentials(email, password);

        switch (permissionLevel) {
            case 1:
                //normal user scene
                RealTimeStorage.updateUserInfos(email);
                break;
            case 2:
                //admin scene
                RealTimeStorage.updateUserInfos(email);
                break;
            case 3:
                //master scene
                RealTimeStorage.updateUserInfos(email);
                break;
            case -1:
                closeLoadingScreen();
                loginAlert(permissionLevel);
                break;
            case -2:
                closeLoadingScreen();
                loginAlert(permissionLevel);
                break;
        }
    }

    public void loginAlert(int error) {

        if (error == -1) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setTitle("Invalid User Credential");
                    passwordField.clear();
                    a.setContentText("Please enter the correct password");
                    Stage stageA = (Stage) a.getDialogPane().getScene().getWindow(); // get the window of alert box and cast to stage to add icons
                    stageA.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
                    stageA.showAndWait();
                }
            });
        } else if (error == -2) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setTitle("Invalid User Credential");
                    passwordField.clear();
                    emailField.clear();
                    a.setContentText("The email is not registered");
                    Stage stageB = (Stage) a.getDialogPane().getScene().getWindow(); // get the window of alert box and cast to stage to add icons
                    stageB.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
                    stageB.showAndWait();
                }
            });
        }
    }

    public void switchSceneFromLogin(int permission) throws IOException {
        if (permission == 1) {
            switchScene.switchToHomeLogined(events);
        } else if (permission == 2) {
            switchScene.switchToAdminMain(events);
        } else {
            switchScene.switchToAdminMain(events);
        }
    }

    public Task registerTask() {
        // create a Task
        Task<Void> createTask = new Task<>() {

            @Override
            protected Void call() throws Exception {
                //Background work                       
                final CountDownLatch latch = new CountDownLatch(1);

                // Access JavaFX UI from background thread
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            showLoadingScreen();
                            startProgress();
                        } finally {
                            latch.countDown();
                        }
                    }
                });

                // Wait for the UI animation to be executed
                latch.await();

                String username = usernameField.getText();
                String email = REmailField.getText();
                String phoneNumber = phoneNumberField.getText();
                String password = RPasswordField.getText();
                String confirmPassword = RConfirmPasswordField.getText();

                register(username, phoneNumber, email, password, confirmPassword);

                if (phoneNumber.isBlank()) {
                    phoneNumber = null;
                }

                if (status) {
                    sendEmailVerification(email, username, phoneNumber, password);
                }

                return null;
            }
        };

        return createTask;
    }

    public void register(String userName, String phoneNumber, String email, String password, String confirmPassword) throws SQLException, InterruptedException {
        // check if the email inputted is registered
        int isDup = sqlConnect.checkDup(email, phoneNumber);

        // process before cut to OTP scene
        status = false;

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    registerAlert(isDup);
                } finally {
                    latch.countDown();
                }
            }
        });

        // wait registerAlert to finish
        latch.await();

    }

    public void registerAlert(int error) {
        switch (error) {
            case 0:
                status = true;
                break;
            case -1:
                closeLoadingScreen();
                REmailFieldWarning.setText("This email is already registered");
                break;
            case -2:
                closeLoadingScreen();
                phoneNumberFieldWarning.setText("This phone number is already registered");
                break;
            case -3:
                closeLoadingScreen();
                REmailFieldWarning.setText("This email is already registered");
                phoneNumberFieldWarning.setText("This phone number is already registered");
                break;
            case -4:
                closeLoadingScreen();
                break;
        }

    }

    public void switchSceneFromRegister(String email, String username) throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(App.class.getResource("OTP.fxml"));
        Parent root = fxmlloader.load();
        OTPController control = fxmlloader.getController();
        control.setDisplayEmail(email, username);
        switchScene.switchToOTPScene(events);
    }

    public void startProgress() {
        String init = "Loading";
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), e -> {
            if (dot == 5) {
                loading.setText(init);
                dot = 0;
            } else {
                loading.setText(loading.getText() + ".");
                dot++;
            }
        }
        ));
        timeline.play();
    }

    public void showLoadingScreen() {
        loadingScreen.setTranslateX(0);
    }

    public void closeLoadingScreen() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loadingScreen.setTranslateX(1800);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
