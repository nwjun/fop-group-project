package com.fop.foptproject;

import com.fop.foptproject.controller.SceneController;
import com.fop.Utility.sqlConnect;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        boolean DEBUG = false;
        String fxmlFile = "CheckOut.fxml";

        if (!DEBUG) {
            fxmlFile = "SplashScreen.fxml";
        }

        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));

        // set Scene's width and height based on screen size
        Scene scene = new Scene(root);
        SceneController.setPrimaryStage(primaryStage);
        primaryStage.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
        primaryStage.setTitle("Movie Ticketing System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.centerOnScreen();
        primaryStage.show();

    }

    public static void main(String[] args) throws Exception {
        sqlConnect sql = new sqlConnect();
        launch();
        System.out.println("launched");
    }
}
