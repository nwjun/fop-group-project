/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.fop.foptproject.controller;

import com.fop.Utility.sqlConnect;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import static com.fop.foptproject.controller.SceneController.showPopUpStage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author kuckn
 */
public class EditAdminPopUpController implements Initializable {

    private sqlConnect sql = new sqlConnect();
    private Object [] Id;
    private Object [] username;
    private Object [] email;
    
    @FXML
    private Button closeWindow;
    @FXML
    private GridPane adminList;
    @FXML
    private TextField addAdmin;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ScrollPane scrollpane;
    @FXML
    private TextField addAdminPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getAdmin();
    }    

    public void getAdmin(){
        HashMap<String, ArrayList<String>> admin = sql.queryAdmin();
        Id = admin.get("Id").toArray();
        username = admin.get("username").toArray();
        email = admin.get("email").toArray();
        
        for(Integer i =0; i<Id.length;i++){
            adminList.add(new Label(Integer.toString(i+1)), 0, i+1, 1, 1);
            adminList.add(new Label((String)Id[i]), 1, i+1, 1, 1);
            adminList.add(new Label((String)username[i]), 2, i+1, 1, 1);
            adminList.add(new Label((String)email[i]), 3, i+1, 1, 1);
            adminList.getRowConstraints().add(new RowConstraints(25));
        }
    }
    
    public void clean(){
        Stage x = showPopUpStage("EditAdmin.fxml");
        x.initStyle(StageStyle.UNDECORATED);
        x.show();
    }
    
    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) closeWindow.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addButton(ActionEvent event) {
        String x = addAdmin.getText();
        sql.addNewUser(x.substring(0, x.lastIndexOf("@")),x, addAdminPassword.getText(), null, 2 );
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
        clean();
        getAdmin();
    }

    @FXML
    private void deleteButton(ActionEvent event) {
        sql.removeAdmin(addAdmin.getText(), addAdminPassword.getText());
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
        clean();
        getAdmin();
    }





    
}
