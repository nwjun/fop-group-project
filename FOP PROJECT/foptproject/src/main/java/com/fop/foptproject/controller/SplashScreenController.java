/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.foptproject.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author WeiXin
 */
public class SplashScreenController implements Initializable {

    @FXML
    private StackPane logo;
    @FXML
    private StackPane progress;
    @FXML
    private Label progressAnimation;

    private int dot = 1;

    public TranslateTransition YlineAnimation(double start, double end, Node node, int delay) {
        TranslateTransition move = new TranslateTransition();
        move.setDelay(Duration.millis(delay));
        move.setNode(node);
        move.setFromY(start);
        move.setToY(end);
        move.setCycleCount(1);
        move.setAutoReverse(false);
        move.setDuration(Duration.millis(1000));
        move.play();
        return move;
    }

    public void FadeAnimation(int milli, boolean reverse, Node node, int delay) {
        int endVal = (reverse) ? 0 : 1;
        Timeline timeline = new Timeline();
        timeline.setDelay(Duration.millis(delay));
        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(milli),
                new KeyValue(node.opacityProperty(), endVal)));
        timeline.play();
    }

    public void startProgress() {
        String init = "Loading";
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200), e -> {
            if (dot == 5) {
                progressAnimation.setText(init);
                dot = 0;
            } else {
                progressAnimation.setText(progressAnimation.getText() + ".");
                dot++;
            }
        }
        ));
        timeline.play();
    }
    
    public void stopProgress() {
        progress.getChildren().clear();
    }
    
    
    public Task getFetchService(){
        // create a Task
            Task<Void> createTask = new Task<>() {

            private void fetchAllMovie() {
                try {
                    RealTimeStorage.setAllMovies();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void fetchLandingFood() {
                try {
                    //query landing food poster
                    RealTimeStorage.setAllLandingFood();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            private void fetchAllProducts(){
                try{
                    //query all products
                    RealTimeStorage.setAllProducts();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            
            private void fetchAllAdmins(){
                try{
                    //query all products
                    RealTimeStorage.setAllAdmins();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            
            private void fetchAllTicketPrice(){
                try{
                    //query all products
                    RealTimeStorage.setAllTickets();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            
            @Override
            protected Void call() throws Exception {
                //Background work                       
                final CountDownLatch latch = new CountDownLatch(1);

                // Access JavaFX UI from background thread
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            startProgress();
                        } finally {
                            latch.countDown();
                        }
                    }
                });

                // Wait for the startProgress to be executed
                latch.await();
                // DB Processes
                // fetch movies
                fetchAllMovie();
                // fetch landing food
                fetchLandingFood();
                // fetch all food price id name for calculation
                fetchAllProducts();
                // fetch all admins
                fetchAllAdmins();
                // fetch all tickets
                fetchAllTicketPrice();

                return null;
            }
        };

        return createTask;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logo.setOpacity(0);
        progressAnimation.setOpacity(0);
        // animation
        FadeAnimation(2000, false, logo, 500);
        YlineAnimation(0, -30, logo, 1500);
        FadeAnimation(200, false, progressAnimation, 2000);
        TranslateTransition move = YlineAnimation(0, -20, progressAnimation, 1500);
        move.setOnFinished(e -> {
            Task<Void> fetchService = getFetchService();
            new Thread(fetchService).start(); // start loading animation and fetch data
            fetchService.setOnSucceeded(eh -> {
                stopProgress();
                // switch to home
                try {
                    SceneController.closeSplashStage();
                    new SceneController().switchToHome(e);
                }
                catch (IOException ex) {
                    // do ntg
                }
            });

        });

    }

}
