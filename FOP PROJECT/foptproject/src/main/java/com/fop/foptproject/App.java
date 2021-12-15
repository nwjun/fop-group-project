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


        boolean DEBUG = false;
        String fxmlFile = "userProfile.fxml";

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
        primaryStage.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
        primaryStage.setTitle("Movie Ticketing System");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.show();

    }

    public static void main(String[] args) throws Exception {
        Properties prop = new readConfig().readconfigfile();
        System.out.println("yay");
        sqlConnect sql = new sqlConnect();
        //query all movie from database and store in local
        System.out.println("yay");
        RealTimeStorage.setAllMovies();
        //query landing food poster
        RealTimeStorage.setAllLandingFood();
        
//        JSONToolSets json = new JSONToolSets(sql.querySeats("1","1",true),true);
//        json.parseTheaterSeat(0);
//        json.setSeatStat(new int[]{0,1,2}, new int[]{0,1,2}, new int[]{-1,1,-1});
//        System.out.println("After modifying:");
//        System.out.println(json.getNewSeatArr().toString());
//        
//        System.out.println(0/0);
        launch();
        System.out.println("launched");
    }
}
