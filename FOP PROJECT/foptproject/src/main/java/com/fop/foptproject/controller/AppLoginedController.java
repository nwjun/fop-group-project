/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.foptproject.controller;

import com.fop.foptproject.CommonMethod;
import com.fop.foptproject.Food;
import com.fop.foptproject.Movie;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 *
 * @author WeiXin
 */
public class AppLoginedController extends AppController implements Initializable{
    private CommonMethod method = new CommonMethod();      
    @FXML
    private GridPane landingGrid;
    @FXML
    private StackPane landingStackPane;
    @FXML
    private ScrollPane scrollpane;
    @FXML
    private GridPane landingFooter;
    @FXML
    private Hyperlink hyperlink;
    @FXML
    private HBox movieList;
    @FXML
    private HBox foodList;
    @FXML
    private Line landingLine;
    @FXML
    private VBox landingFooterVBox1;
    @FXML
    private VBox landingFooterVBox2;
    @FXML
    private VBox landingFooterVBox3;
    @FXML
    private Hyperlink userProfileLink;
    @FXML
    private Button signOutButton;
    
    @FXML
    public void signOut(ActionEvent event) throws IOException{
        RealTimeStorage.clearAll();
        SceneController switchScene = new SceneController();
        switchScene.switchToHome(event);
    }
    
    @FXML
    public void userProfile(ActionEvent event) throws IOException{
        SceneController switchScene = new SceneController();
        switchScene.switchToUserProfile(event);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url,resourceBundle);
        
        // set welcome text
        userProfileLink.setText(RealTimeStorage.getUsername());
        userProfileLink.setStyle("-fx-text-fill:#ffffff;");
    }
}
