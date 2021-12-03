package com.fop.foptproject;


import com.fop.EmailUtil.emailTo;
import com.fop.Ticket.TicketGenerator;
import com.fop.foptproject.controller.SceneController;
import com.fop.sqlUtil.sqlConnect;
import com.fop.htmlMailTemplate.templateModifier;
import com.fop.sqlUtil.sqlConnect;
import com.fop.htmlMailTemplate.templateModifier;
import com.fop.readConfig.readConfig;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application{

    @Override
    public void start(Stage primaryStage) throws IOException {

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        final double WIDTH = screenBounds.getWidth();
        final double HEIGHT = screenBounds.getHeight();


        boolean DEBUG = true;
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
        primaryStage.setTitle("Movie Ticketing System");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.show();

    }

    public static void main(String[] args) throws Exception {
        Properties prop = new readConfig().readconfigfile();

        launch();
    }
}
