package com.fop.foptproject.controller;

import com.fop.Utility.emailTo;
import com.fop.Utility.sqlConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
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
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * FXML Controller Class
 *
 * @author WeiXin
 */
public class OTPController implements Initializable {

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
    @FXML
    private StackPane loadingScreen;
    @FXML
    private Label loading;

    // class side
    private int i = 60;
    private int dot = 0;
    private ActionEvent events = null;
    private boolean status = false;
    private String text = "Resend (60)";
    private static String email;
    private static String firstName;

    @FXML
    public void resendOTP(ActionEvent event) {
        Task<Void> resend = resendOperation();
        new Thread(resend).start();
        resend.setOnSucceeded(eh->{
            startCountDown();
        });
        
    }

    @FXML
    public void backButton(ActionEvent event) throws IOException {
        events = event;
        Task<Void> back = backOperation();
        new Thread(back).start();
        back.setOnSucceeded(eh -> {
            try {
                new SceneController().switchToLandingPage(events);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

    }

    @FXML
    public void verifyPrimer(ActionEvent event) throws IOException {
        events = event;

        // background process
        Date dateObj = new Date();
        Timestamp currentTime = new Timestamp(dateObj.getTime());
        String OTPEntered = OTPField.getText();
        String OTPRequired = sqlConnect.queryOTP(email);
        Timestamp timeIssued = sqlConnect.queryTimestamp(email);

        long differenceInSeconds = ((int) (currentTime.getTime() - timeIssued.getTime())) / 1000;
        double differenceInMinutes = differenceInSeconds / 3600.0;

        if (OTPEntered.isBlank()) {
            OTPFieldWarning.setText("Please enter your OTP");
            OTPField.setStyle("-fx-border-color:#FF0000;-fx-border-width:1px;-fx-pref-height:39px;-fx-pref-width:200px;-fx-background-radius:8 0 0 8;-fx-border-radius:8 0 0 8;");
        } else if (differenceInMinutes > 5) {
            OTPField.clear();
            OTPFieldWarning.setText("This OTP is expired");
            OTPField.setStyle("-fx-border-color:#FF0000;-fx-border-width:1px;-fx-pref-height:39px;-fx-pref-width:200px;-fx-background-radius:8 0 0 8;-fx-border-radius:8 0 0 8;");
        } else if (OTPRequired.equals(OTPEntered)) {
            Task<Void> successfulOTP = successfulOTP();
            new Thread(successfulOTP).start();
            successfulOTP.setOnSucceeded(eh -> {
                if (status) {
                    SceneController switchScene = new SceneController();
                    try {
                        closeLoadingScreen();
                        switchScene.switchToHomeLogined(events);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } else if (!(OTPRequired.equals(OTPEntered))) {
            OTPField.clear();
            OTPFieldWarning.setText("Please enter the correct OTP");
            OTPField.setStyle("-fx-border-color:#FF0000;-fx-border-width:1px;-fx-pref-height:39px;-fx-pref-width:200px;-fx-background-radius:8 0 0 8;-fx-border-radius:8 0 0 8;");
        }
    }

    // resend cool down
    public void startCountDown() {
        i = 60;
        resendButton.setDisable(true);
        resendButton.setText(text);
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            i--;
            text = String.format("Resend (%02d)", i);
            resendButton.setText(text);

            if (i == 0) {
                resendButton.setDisable(false);
                resendButton.setText("Resend OTP");
                text = "Resend (60)";
            }
        }));

        timer.setCycleCount(60);
        timer.play();
    }

    public void setDisplayEmail(String useremail, String username) {
        email = useremail;
        firstName = username;
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

    public Task successfulOTP() {
        Task<Void> createTask = new Task<>() {
            @Override
            protected Void call() throws Exception {

                CountDownLatch latch = new CountDownLatch(1);

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

                // wait for the loading screen animation to be invoked
                latch.await();

                // background process
                status = sqlConnect.removeNewRegisterOTP(email, false);
                if (status) {
                    RealTimeStorage.updateUserInfos(email);
                }

                return null;
            }
        };

        return createTask;
    }

    public Task backOperation() {
        Task<Void> createTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                CountDownLatch latch = new CountDownLatch(1);

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

                latch.await();
                sqlConnect.removeNewRegisterOTP(email, true);
                return null;
            }

        };
        return createTask;
    }

    public Task resendOperation() {
        Task<Void> createTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                CountDownLatch latch = new CountDownLatch(1);

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

                latch.await();
                String OTP = new emailTo(email).sendEmailVerification(firstName, false);
                sqlConnect.updateOTP(email, OTP);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        closeLoadingScreen();
                    }
                });

                return null;
            }

        };
        return createTask;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sentToEmail.setText("A one-time-password has been sent to " + email);
        startCountDown();
    }

}
