/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fop.foptproject.controller;

import com.fop.foptproject.App;
import com.fop.foptproject.CommonMethod;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author jun
 */
public class profileController implements Initializable {

    @FXML
    Label titleLabel;
    @FXML
    ImageView menuIcon;
    @FXML
    VBox contentContainer;
    @FXML
    StackPane mainContainer;
    @FXML
    ImageView bigLogo;

    @FXML
    StackPane centerContainer;

    CommonMethod commonMethod = new CommonMethod();
    ArrayList<String[]> banks;
    
    //https://stackoverflow.com/questions/28717343/javafx-create-a-vertical-menu-ribbon
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        VBox sideBar = new VBox();
        sideBar.setAlignment(Pos.TOP_LEFT);
        sideBar.getStyleClass().add("sideBar");
        sideBar.setMaxWidth(400);
        sideBar.setPadding(new Insets(50, 40, 0, 40));
        sideBar.setSpacing(60);
        StackPane.setAlignment(sideBar, Pos.TOP_LEFT);

        sideBar.setOnMouseExited(event -> {
            mainContainer.getChildren().remove(sideBar);
        });

        HBox clsBtnContainer = new HBox();
        HBox homeContainer = createRow("assets/profile/home", "Home", 1);
        HBox profileContainer = createRow("assets/profile/profile", "Profile", 2);
        HBox billingContainer = createRow("assets/profile/billing", "Billing", 3);
        HBox historyContainer = createRow("assets/profile/history", "Account History", 4);

        HBox[] containers = new HBox[]{clsBtnContainer, homeContainer, profileContainer, billingContainer, historyContainer};

        Button clsBtn = new Button("X");
        clsBtn.getStyleClass().add("closeBtn");
        clsBtnContainer.getChildren().addAll(clsBtn);
        // Right align items
        clsBtnContainer.setAlignment(Pos.BASELINE_RIGHT);

        clsBtn.setOnAction(event -> {
            FadeTransition hideSideBarTransition = new FadeTransition(Duration.millis(500), sideBar);
            hideSideBarTransition.setFromValue(1.0);
            hideSideBarTransition.setToValue(0.0);

            hideSideBarTransition.play();
            mainContainer.getChildren().remove(sideBar);
        });

        menuIcon.setPickOnBounds(true);
        menuIcon.setOnMouseEntered(event -> {
            FadeTransition showSideBarTransition = new FadeTransition(Duration.millis(500), sideBar);
            showSideBarTransition.setFromValue(0.0);
            showSideBarTransition.setToValue(1.0);

            mainContainer.getChildren().add(sideBar);
            showSideBarTransition.play();
        });

        sideBar.getChildren().addAll(containers);
        profile();

    }

    HBox createRow(String iconFile, String text, int sceneCode) {
        Image img = new Image(commonMethod.getPathToResources(iconFile + "-white.png"));
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(42);
        imgView.setFitWidth(42);

        Label textLabel = new Label(text);
        HBox hBox = new HBox();
        hBox.setSpacing(30);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(imgView, textLabel);

        // hover effect
        hBox.setOnMouseEntered(event -> {
            imgView.setImage(new Image(commonMethod.getPathToResources(iconFile + "-yellow.png")));
            textLabel.setStyle("-fx-text-fill:#FFEE00");
        });
        hBox.setOnMouseExited(event -> {
            imgView.setImage(new Image(commonMethod.getPathToResources(iconFile + "-white.png")));
            textLabel.setStyle("-fx-text-fill:#FFFFFF");
        });

        SceneController sceneController = new SceneController();

        switch (sceneCode) {
            case 1:
                hBox.setOnMouseClicked(event -> {
                    try {
                        sceneController.switchToHomeLogined(event);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                break;
            case 2:
                hBox.setOnMouseClicked(event -> profile());
                break;
            case 3:
                hBox.setOnMouseClicked(event -> billing());
                break;
            case 4:
                hBox.setOnMouseClicked(event -> history());
                break;
        }

        return hBox;
    }

    private void profile() {
        titleLabel.setText("Profile");
        contentContainer.getChildren().clear();
        addRemoveLogo("profile");
        final double MAX_HEIGHT = 45;
        final double MAX_WIDTH = 380;

        Label usernameLabel = new Label("Username");
        Label emailLabel = new Label("Email");
        Label hpLabel = new Label("Phone number");
        Label[] labels = new Label[]{usernameLabel, emailLabel, hpLabel};

        for (Label label : labels) {
            label.getStyleClass().add("profileLabel");
            VBox.setMargin(label, new Insets(50, 0, 20, 0));
        }

        TextField usernameField = new TextField();
        TextField emailField = new TextField();
        TextField hpField = new TextField();
        TextField[] textFields = new TextField[]{usernameField, emailField, hpField};
        String[] textFieldIds = new String[]{"profileField", "emailField", "hpField"};

        String[] fieldValues = getProfileValues();

        for (int i = 0; i < textFields.length; i++) {
            textFields[i].getStyleClass().add("profileText");
            textFields[i].setId(textFieldIds[i]);
            textFields[i].setMaxSize(MAX_WIDTH, MAX_HEIGHT);
            textFields[i].setText(fieldValues[i]);
        }

        VBox wrapper = new VBox();

        Button confirmBtn = new Button("Confirm");
        Button resetBtn = new Button("Reset");
        resetBtn.getStyleClass().add("transparentBtn");

        confirmBtn.setOnAction(e -> {
            String[] newFieldValues = new String[textFields.length];
            
            for (int i = 0; i < textFields.length; i++) {
                newFieldValues[i] = textFields[i].getText();
                if(!newFieldValues[i].equals(fieldValues[i])){
                    updateProfile(newFieldValues[i], i);
                }
            }

        });

        resetBtn.setOnAction(e -> {
            String[] vals = getProfileValues();
            for (int i = 0; i < textFields.length; i++) {
                textFields[i].setText(vals[i]);
            }
        });

        HBox btnBox = new HBox(confirmBtn, resetBtn);
        btnBox.setSpacing(30);
        btnBox.setMaxWidth(MAX_WIDTH);
        VBox.setMargin(btnBox, new Insets(70, 0, 0, 0));

        wrapper.getChildren().addAll(usernameLabel, usernameField, emailLabel, emailField, hpLabel, hpField, btnBox);
        wrapper.getStyleClass().add("wrapper");
        contentContainer.getChildren().addAll(wrapper);

    }

    private void billing() {

        titleLabel.setText("Billing");
        contentContainer.getChildren().clear();
        addRemoveLogo("billing");

        ScrollPane scrollPane = new ScrollPane();
        VBox scrollPaneContainer = new VBox();
        VBox banksContainer = new VBox();
        
        scrollPane.setFitToWidth(true);
       
        Button addBtn = new Button("Add");
        Button saveBtn = new Button("Save");

        scrollPane.setContent(banksContainer);
        scrollPaneContainer.getChildren().addAll(scrollPane, addBtn);
        
        
        VBox wrapper = new VBox(scrollPaneContainer, saveBtn);
        contentContainer.getChildren().addAll(wrapper);
        
        // Layout
        scrollPaneContainer.setAlignment(Pos.TOP_CENTER);
        saveBtn.setAlignment(Pos.BOTTOM_CENTER);
        banksContainer.setAlignment(Pos.CENTER);
        wrapper.setSpacing(40);
        
        // Style
        scrollPane.setStyle("-fx-background-color:transparent");
        banksContainer.setStyle("-fx-background-color:#252525");
        scrollPaneContainer.setId("billingScrollPaneContainer");

        banks = getBanks();
        
//        ArrayList<HBox> bankButtonContainer = new ArrayList<>();

        for (int i = 0; i < banks.size(); i++) {
            String[] bank = banks.get(i);
            Label bankLabel = new Label(bank[0]);
            Label accLabel = new Label(bank[1]);
            
            VBox detailsContainer = new VBox(bankLabel, accLabel);
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            Button removeBtn = new Button("-");
            HBox bankButtonRow = new HBox(detailsContainer, region, removeBtn);
            bankButtonRow.setAlignment(Pos.CENTER_LEFT);
            banksContainer.getChildren().add(bankButtonRow);
            
            removeBtn.setOnAction(e -> {
                banksContainer.getChildren().remove(bankButtonRow);
            });
        }

        addBtn.setOnAction(e -> {
            Stage popupStage = SceneController.showPopUpStage("AddCardPopUp.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("AddCardPopUp.fxml"));
            AddCardPopUpController addCardPopUpController = fxmlLoader.getController();

            
            if (popupStage != null) {
                
                popupStage.showAndWait();
            }
        });

        banksContainer.setSpacing(30);

        saveBtn.setOnAction(e -> {
            updateBank(banksContainer.getChildren());
        });
    }

    private void history() {
        titleLabel.setText("History");
        contentContainer.getChildren().clear();
        addRemoveLogo("history");

        ScrollPane scrollPane = new ScrollPane();
        VBox wrapper = new VBox(scrollPane);
        wrapper.getStyleClass().add("wrapper");
        contentContainer.getChildren().add(wrapper);

        scrollPane.setStyle("-fx-background-color:transparent");
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxHeight(600);

        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color:#252525");
        scrollPane.setContent(gridPane);
        gridPane.setHgap(10);
        gridPane.setVgap(30);
        gridPane.getStyleClass().addAll("grid");

        ColumnConstraints col1 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        ColumnConstraints col2 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        ColumnConstraints[] cols = new ColumnConstraints[]{col1, col2};

        for (ColumnConstraints col : cols) {
            col.setHgrow(Priority.ALWAYS);
            col.setHalignment(HPos.CENTER);
        }

        gridPane.getColumnConstraints().addAll(cols);

        String[][] histories = getHistories();
        ArrayList<VBox> historyContainers = new ArrayList<>();

        for (int i = 0; i < histories.length; i++) {
            String[] history = histories[i];
            VBox historyContainer = new VBox();

            for (int j = 0; j < history.length; j++) {
                Label label = new Label(history[j]);

                if (j == 0) {
                    label.getStyleClass().add("historyHeading");
                } else {
                    label.getStyleClass().add("historyText");
                }

                historyContainer.getChildren().add(label);
            }
            historyContainers.add(historyContainer);

        }
        for (int i = 0; i < historyContainers.size(); i++) {
            gridPane.add(historyContainers.get(i), i % 2, i / 2);
        }
    }

    public String[][] getHistories() {
        final int NUM = 30;
        String[][] histories = new String[NUM][4];
        for (int i = 0; i < NUM; i++) {
            histories[i][0] = "Eternals";
            histories[i][1] = "21/11/2021 11:45am";
            histories[i][2] = "Sunway Pyramid";
            histories[i][3] = "E11, E12, E13";
        }

        return histories;
    }

    private ArrayList<String[]> getBanks() {
        return RealTimeStorage.getLinkedCard2D();
    }

    private void updateBank(ObservableList<Node> bankContainers) {
        // send back to server
        ArrayList<String[]> tempBanks = new ArrayList<>();
        
        for(int i =0; i<bankContainers.size();i++){
            HBox bankContainer = (HBox) bankContainers.get(i);
            VBox bank = (VBox)bankContainer.getChildren().get(0);
            String bankAcc = ((Label)bank.getChildren().get(0)).getText();
            String bankNo = ((Label)bank.getChildren().get(1)).getText();
            tempBanks.add(new String[]{bankAcc,bankNo});
        }
        RealTimeStorage.setLinkedCard2D(tempBanks);
    }

    private void addRemoveLogo(String section) {

        if (section.equals("billing")) {
            centerContainer.getChildren().remove(bigLogo);
            StackPane.setAlignment(contentContainer, Pos.CENTER);

        } else if (!centerContainer.getChildren().contains(bigLogo)) {
            centerContainer.getChildren().add(bigLogo);
            StackPane.setAlignment(contentContainer, Pos.TOP_LEFT);

        }

    }

    private Stage popUpStage() {
        VBox vbox = new VBox();
        Scene scene = new Scene(vbox);
        Stage stage = new Stage();
        return stage;
    }

    private String[] getProfileValues() {
        // retrieve field values from db
        return new String[]{RealTimeStorage.getUsername(),RealTimeStorage.getUserEmail(), RealTimeStorage.getPNumber()};
    }

    private void updateProfile(String newFieldValue, int item) {
        // update data in database
        switch(item){
            case 0:
                RealTimeStorage.setUsername(newFieldValue);
                break;
            case 1:
                RealTimeStorage.setEmail(newFieldValue);
                break;
            case 2:
                RealTimeStorage.setPNumber(newFieldValue);
                break;
                       
        }
    }

}
