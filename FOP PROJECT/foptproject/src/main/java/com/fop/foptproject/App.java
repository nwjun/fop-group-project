package com.fop.foptproject;

import com.fop.EmailUtil.emailTo;
import com.fop.sqlUtil.sqlConnect;
import com.fop.htmlMailTemplate.templateModifie
import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("App.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Movie Ticketing System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException, IOException {
        String content = new templateModifier().readHTML();
        System.out.println(new emailTo("limweixin17@gmail.com,helloworldisagurl@gmail.com").sendBookingConfirmations(content,"Eternals","forInternalTest","54321","12345","11/11/2021","1:30 PM","3 x Preferred (RM51.00 - E11, E10, E9)",52.5));


        //System.out.println(new emailTo("limweixin17@gmail.com,helloworldisagurl@gmail.com").sendNotification("Eternals","11/11/2021","2:00pm","Kuala Lumpur - MidValley"));
        //sqlConnect jc = new sqlConnect();
        //jc.addTestData();
        //jc.createTestQuery();
        //System.out.println("hi");
        launch();
    }
}