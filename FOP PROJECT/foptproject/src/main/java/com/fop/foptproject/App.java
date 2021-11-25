package com.fop.foptproject;

import com.fop.EmailUtil.emailTo;
import com.fop.Ticket.TicketGenerator;
import com.fop.sqlUtil.sqlConnect;
import com.fop.htmlMailTemplate.templateModifier;
import com.fop.readConfig.readConfig;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // get computer screen size 
        javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        final double WIDTH = screenBounds.getWidth();
        final double HEIGHT = screenBounds.getHeight();
        
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("App.fxml"));
        // set Scene's width and height based on screen size
        Scene scene = new Scene(fxmlLoader.load(),WIDTH, HEIGHT); 
        
        // get scrollPane from fxml
        ScrollPane scrollPane = (ScrollPane) fxmlLoader.getRoot();
        scrollPane.setPannable(false);
        // hide horizontal and vertical scroll bar
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        // set the content sized with scroll pane
        scrollPane.setFitToWidth(true);
        
        primaryStage.setTitle("Movie Ticketing System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        Properties prop = new readConfig().readconfigfile();
        System.out.println(new emailTo(prop.getProperty("configuration.testSingleEmail")).sendBookingConfirmations("Eternals","forInternalTest","54321","12345","24/11/2021","2:00 PM","3 x Student (RM51.00 - E11, E10, E9)",52.5));
        //System.out.println(new emailTo(prop.getProperty("configuration.testSingleEmail")).sendEmailVerification("Lim",false));
        //System.out.println(new emailTo("limweixin17@gmail.com,helloworldisagurl@gmail.com").sendNotification("Eternals","11/11/2021","2:00pm","Kuala Lumpur - MidValley"));

        //sqlConnect jc = new sqlConnect();
        //jc.addTestData();
        //jc.createTestQuery();
        
        launch();
    }
}