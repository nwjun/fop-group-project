package com.fop.foptproject.controller;

import com.fop.Utility.Checker;
import com.fop.foptproject.App;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author jun, WeiXin
 */
public class AddCardPopUpController implements Initializable {

    @FXML
    private ChoiceBox bankChoiceBox;
    @FXML
    private TextField accTextField;
    @FXML
    private TextField expTextField;
    @FXML
    private TextField cvvTextField;
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

        // add listener to input field
        accTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (newValue.length() - oldValue.length() > 0) {
                    if ((newValue.length() % 4 == 0 && newValue.length() == 4 || (newValue.length() + 1) % 5 == 0)) {
                        accTextField.appendText("-");
                    }
                }
                if (newValue.length() > 19) {
                    accTextField.setText(oldValue);
                }
            }
        });

        expTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (newValue.length() - oldValue.length() > 0) {
                    if (newValue.length() == 2) {
                        expTextField.appendText("/");
                    }
                }
                if (newValue.length() > 5) {
                    expTextField.setText(oldValue);
                }

            }
        });

        cvvTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (newValue.length() > 3) {
                    cvvTextField.setText(oldValue);
                }

            }
        });

        // filters
        UnaryOperator<Change> cardFilter = change -> {
            String text = change.getText();

            if (text.matches("[0-9|-]*")) {
                return change;
            }
            return null;
        };

        UnaryOperator<Change> expiryFilter = change -> {
            String text = change.getText();

            if (text.matches("[0-9|/]*")) {
                return change;
            }
            return null;
        };

        UnaryOperator<Change> cvvFilter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        // set filter
        accTextField.setTextFormatter(new TextFormatter<String>(cardFilter));
        expTextField.setTextFormatter(new TextFormatter<String>(expiryFilter));
        cvvTextField.setTextFormatter(new TextFormatter<String>(cvvFilter));

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
            addErrorMsg.setText(null);
            accErrorMsg.setText(null);
            Stage window = (Stage) addBtn.getScene().getWindow();
            String selectedBank = (String) bankChoiceBox.getValue();
            String accNo = accTextField.getText();
            String expNo = expTextField.getText();
            String cvvNo = cvvTextField.getText();
            
            if(!accNo.isBlank()&&!expNo.isBlank()&&!cvvNo.isBlank()){
                boolean checkCardNum = Checker.checkCardValidity(accNo.replaceAll("-", ""));
                boolean checkExpiry = checkExpiry();
                
                if (checkCardNum && checkExpiry) {
                    addToDb(accNo, expNo, cvvNo, selectedBank);
                    addErrorMsg.setStyle("-fx-text-fill:#ffffff;-fx-font-size:13px");
                    addErrorMsg.setText("Added bank account successfully");

                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                        window.close();
                    }));

                    timeline.play();
                }
                else if(!checkCardNum){
                    accErrorMsg.setText("Invalid credit card");
                }
            }
            else{
                addErrorMsg.setText("Please fill in the form completely");
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
        if (accNo.length() <= 19 && selectedBank != null) {
            return true;
        }
        return false;
    }

    private void addToDb(String accNo, String expNo, String cvvNo, String selectedBank) {
        // add accNo and selectedBank to database
        RealTimeStorage.updateLinkedCard(new String[]{selectedBank, accNo, expNo, cvvNo});
    }

    public boolean checkExpiry() {
        String expiry = expTextField.getText();
        boolean isValid = false;
        // check month and year validity
        int MM = Integer.parseInt(expiry.split("/")[0]);
        int YY = Integer.parseInt(expiry.split("/")[1]);
        if (MM > 12 || MM < 1) {
            isValid = false;
            addErrorMsg.setText("Invalid month is entered. Please double check your expiry date input");
            return isValid;
        }
        if (YY < 22) {
            isValid = false;
            addErrorMsg.setText("The card is expired. Please change another card to add");
            return isValid;
        }
        isValid = true;
        return isValid;
    }
}
