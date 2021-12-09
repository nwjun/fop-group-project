package com.fop.foptproject.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AddCardPopUpController implements Initializable {

    @FXML
    private ChoiceBox bankChoiceBox;
    @FXML
    private TextField accTextField;
    @FXML
    private HBox buttonContainer;
    @FXML
    private Button addBtn;
    @FXML
    private Button cclBtn;
    @FXML
    private Label accErrorMsg;
    @FXML
    private Label addErrorMsg;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        String[] banks = new String[]{"Ambank", "Maybank", "Public Bank"};

        for (String bank : banks) {
            bankChoiceBox.getItems().add(bank);
        }

        accTextField.setOnKeyPressed(e -> {
            String accNo = accTextField.getText();
            String selectedBank = (String) bankChoiceBox.getValue();

            if (validation(accNo, selectedBank)) {
                addBtn.setDisable(false);
            } else {
                addBtn.setDisable(true);
            }
        });

        addBtn.setOnAction(e -> {
            Stage window = (Stage) addBtn.getScene().getWindow();
            String selectedBank = (String) bankChoiceBox.getValue();
            String accNo = accTextField.getText();

            if (addToDb(accNo, selectedBank)) {
                addErrorMsg.setText("Added bank account successfully");

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev->{
                    window.close();
                    // TODO: Link back to DB and update the list
                }));

                timeline.play();

            } else {
                addErrorMsg.setText("Unable to add bank account");
            }
        });

        cclBtn.setOnAction(e -> {
            Stage window = (Stage) addBtn.getScene().getWindow();
            window.close();
        });

    }

    private boolean validation(String accNo, String selectedBank) {
        // TODO: need to validate according to bank? or jst leave it?
        accNo = accNo.trim();
        if (accNo.matches("\\d+") && accNo.length() >= 10 && accNo.length() <= 17 && selectedBank != null) {
            return true;
        }
        return false;
    }

    private boolean addToDb(String accNo, String selectedBank) {
        // add accNo and selectedBank to database
        return true;
    }
}
