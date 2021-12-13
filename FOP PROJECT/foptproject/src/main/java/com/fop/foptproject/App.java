package com.fop.foptproject;

import com.fop.Utility.JSONToolSets;
import com.fop.Utility.emailTo;
import com.fop.foptproject.controller.SceneController;
import com.fop.Utility.readConfig;
import com.fop.Utility.sqlConnect;
import com.fop.foptproject.controller.RealTimeStorage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.*;
import java.util.Properties;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application{

    @Override
    public void start(Stage primaryStage) throws IOException {

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        final double WIDTH = screenBounds.getWidth();
        final double HEIGHT = screenBounds.getHeight();

        boolean DEBUG = true;
        String fxmlFile = "AdminMovie.fxml";

        if (!DEBUG) {
            fxmlFile = "App.fxml";
        }

        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));

        if (!DEBUG) {
            //adjust the view to center when hvalue changes
            ScrollPane k = (ScrollPane) root;
            k.hvalueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                        Number old_val, Number new_val) {
                    k.setHvalue(50);
                }
            });
        }
        
        // set Scene's width and height based on screen size
        Scene scene = new Scene(root, HEIGHT, WIDTH);
        SceneController.setPrimaryStage(primaryStage);
        primaryStage.getIcons().add(new Image("/com/fop/foptproject/assets/company/logo2.png"));
        primaryStage.setTitle("Movie Ticketing System");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.show();

    }

    public static void main(String[] args) throws Exception {
        Properties prop = new readConfig().readconfigfile();
        sqlConnect sql = new sqlConnect();
        //query all movie from database and store in local
        RealTimeStorage.setAllMovies();
        //query landing food poster
        RealTimeStorage.setAllLandingFood();
        
//        JSONToolSets json = new JSONToolSets(sql.querySeats("8","1",false),false);
//        System.out.println("1");
//        json.parseTheaterSeat(0);
//        System.out.println("2");
//        json.parseTheaterSeat(1);
//        System.out.println("3");
//        json.parseTheaterSeat(2);
//        System.out.println("4");
//        json.parseTheaterSeat(3);
//        System.out.println("5");
//        json.parseTheaterSeat(4);
//        System.out.println("6");
//        json.parseTheaterSeat(5);
//        System.out.println("7");
//        json.parseTheaterSeat(6);
        
        launch();
    }
}
