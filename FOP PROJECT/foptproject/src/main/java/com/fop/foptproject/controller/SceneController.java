/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.foptproject.controller;

import com.fop.foptproject.App;
import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SceneController {

    private Scene scene;
    private static Stage primaryStage;
    private final double WIDTH;
    private final double HEIGHT;
    private final double PREF_WIDTH = 1536;
    private final double PREF_HEIGHT = 864;

    public SceneController() {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        this.WIDTH = screenBounds.getWidth();
        this.HEIGHT = screenBounds.getHeight();
    }

    public static void setPrimaryStage(Stage primaryStage) {
        SceneController.primaryStage = primaryStage;
    }

    public static Stage showPopUpStage(String fxmlFile) {
        Parent root;

        try {
            root = FXMLLoader.load(App.class.getResource(fxmlFile));
            
            Scene scene = new Scene(root);
            Stage popupStage = new Stage();

            popupStage.initOwner(SceneController.primaryStage);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(scene);
            popupStage.setResizable(false);

            return popupStage;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(App.class.getResource(fxmlFile));
        //lock landing H scroll
        if (fxmlFile.equals("App.fxml") || fxmlFile.equals("AppLogined.fxml")) {
            ScrollPane k = (ScrollPane) root;
            k.setHvalue(50);
            k.hvalueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                        Number old_val, Number new_val) {
                    k.setHvalue(50);
                }
            });
        }
        // loading screen synchronized movement
        else if(fxmlFile.equals("AdminMovie.fxml")){
            ScrollPane k = (ScrollPane) root;
            StackPane loadingScreen = (StackPane)((StackPane)k.getContent()).getChildren().get(1);
            k.vvalueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                        Number old_val, Number new_val) {
                    System.out.println(loadingScreen.getTranslateY()-(old_val.doubleValue()-new_val.doubleValue())*12.5);
                    loadingScreen.setTranslateY(loadingScreen.getTranslateY()-(old_val.doubleValue()-new_val.doubleValue())*12.5);
                }
            });
        }
        else if(fxmlFile.equals("AdminFood.fxml")){
            ScrollPane k = (ScrollPane) root;
            StackPane loadingScreen = (StackPane)((StackPane)k.getContent()).getChildren().get(1);
            k.vvalueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                        Number old_val, Number new_val) {
                    System.out.println(loadingScreen.getTranslateY()-(old_val.doubleValue()-new_val.doubleValue())*16.5);
                    loadingScreen.setTranslateY(loadingScreen.getTranslateY()-(old_val.doubleValue()-new_val.doubleValue())*16.5);
                }
            });
        
        }

        scene = new Scene(root, PREF_WIDTH, PREF_HEIGHT);
        SceneController.primaryStage.setScene(scene);
        if (WIDTH <= PREF_WIDTH + 10 && HEIGHT <= PREF_HEIGHT + 10) {
            primaryStage.setMaximized(true);
        }
        primaryStage.setResizable(true);
        SceneController.primaryStage.show();
    }

    private void switchScene(MouseEvent event, String fxmlFile) throws IOException {

        Parent root = FXMLLoader.load(App.class.getResource(fxmlFile));
        //lock landing H scroll
        if (fxmlFile.equals("App.fxml") || fxmlFile.equals("AppLogined.fxml")) {
            ScrollPane k = (ScrollPane) root;
            k.setHvalue(50);
            k.hvalueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                        Number old_val, Number new_val) {
                    k.setHvalue(50);
                }
            });
        }
        // loading screen synchronized movement
        else if(fxmlFile.equals("AdminMovie.fxml")){
            ScrollPane k = (ScrollPane) root;
            StackPane loadingScreen = (StackPane)((StackPane)k.getContent()).getChildren().get(1);
            k.vvalueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                        Number old_val, Number new_val) {
                    loadingScreen.setTranslateY(loadingScreen.getTranslateY()-(old_val.doubleValue()-new_val.doubleValue())*12.5);
                }
            });
        }
        else if(fxmlFile.equals("AdminFood.fxml")){
            ScrollPane k = (ScrollPane) root;
            StackPane loadingScreen = (StackPane)((StackPane)k.getContent()).getChildren().get(1);
            k.vvalueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                        Number old_val, Number new_val) {
                    loadingScreen.setTranslateY(loadingScreen.getTranslateY()-(old_val.doubleValue()-new_val.doubleValue())*16.5);
                }
            });
        
        }

        scene = new Scene(root, PREF_WIDTH, PREF_HEIGHT);
        SceneController.primaryStage.setScene(scene);
        if (WIDTH <= PREF_WIDTH + 10 && HEIGHT <= PREF_HEIGHT + 10) {
            primaryStage.setMaximized(true);
        }
        primaryStage.setResizable(true);
        SceneController.primaryStage.show();

    }

    public void switchToOTPScene(ActionEvent event) throws IOException {
        switchScene(event, "OTP.fxml");
    }

    public void switchToRegisterAndLogin(ActionEvent event) throws IOException {
        switchScene(event, "LoginRegister.fxml");
    }

    public void switchToSeats(ActionEvent event) throws IOException {
        switchScene(event, "Seats.fxml");
    }

    public void switchToLandingPage(ActionEvent event) throws IOException {
        switchScene(event, "App.fxml");
    }

    public void switchToMoviesDetails(ActionEvent event) throws IOException {
        switchScene(event, "MoviesDetails.fxml");
    }

    public void switchToMovieBooking(ActionEvent event) throws IOException {
        switchScene(event, "MovieBooking.fxml");
    }

    public void switchToHome(ActionEvent event) throws IOException {
        switchScene(event, "App.fxml");
    }

    public void switchToHome(MouseEvent event) throws IOException {
        switchScene(event, "App.fxml");
    }

    public void switchToHomeLogined(ActionEvent event) throws IOException {
        switchScene(event, "AppLogined.fxml");
    }

    public void switchToHomeLogined(MouseEvent event) throws IOException {
        switchScene(event, "AppLogined.fxml");
    }

    public void switchToAdminMovie(ActionEvent event) throws IOException {
        switchScene(event, "AdminMovie.fxml");
    }

    public void switchToFnB(ActionEvent event) throws IOException {
        switchScene(event, "FoodnBeverage.fxml");
    }

    public void switchToAdminFood(ActionEvent event) throws IOException {
        switchScene(event, "AdminFood.fxml");
    }

    public void switchToAdminMain(ActionEvent event) throws IOException {
        switchScene(event, "AdminMain.fxml");
    }

    public void switchToMovieAllShowTime(ActionEvent event) throws IOException {
        switchScene(event, "MovieAllShowTime.fxml");
    }

    public void switchToCheckOut(ActionEvent event) throws IOException {
        switchScene(event, "CheckOut.fxml");
    }

    public void switchToUserProfile(ActionEvent event) throws IOException {
        switchScene(event, "userProfile.fxml");
    }

    public void switchToAllShowTime(ActionEvent event) throws IOException {
        switchScene(event, "MovieAllShowTime.fxml");
    }

    public void switchToAdminSeats(ActionEvent event) throws IOException {
        switchScene(event, "AdminSeats.fxml");
    }

    public void switchToDonePayment(ActionEvent event) throws IOException {
        switchScene(event, "DonePayment.fxml");
    }

    public static void closeSplashStage() {
        Stage newPrimary = new Stage();
        newPrimary.initStyle(StageStyle.DECORATED);
        newPrimary.setOnHiding(e -> Platform.exit());
        newPrimary.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
        newPrimary.setTitle("Movie Ticketing System");
        newPrimary.setResizable(false);
        newPrimary.centerOnScreen();
        SceneController.primaryStage.hide();
        primaryStage = newPrimary;
    }

}
