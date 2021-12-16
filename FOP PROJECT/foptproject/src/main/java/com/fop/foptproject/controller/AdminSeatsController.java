/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.foptproject.controller;

/**
 *
 * @author xyche
 */

import com.fop.Utility.JSONToolSets;
import com.fop.Utility.sqlConnect;
import com.sun.tools.javac.Main;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;


public class AdminSeatsController implements Initializable{
    
    /**
     * Initializes the controller class.
     */
@FXML
private GridPane seatsContainer;
@FXML
private Button changeStatusButton, nextButton, backButton, rowMinus, rowAdd, columnMinus, columnAdd, modifyButton;
@FXML
private Label hallLabel, cinemaLabel, totalSeatsAvailableLabel, totalSeatsUnavailableLabel, totalSeatsLabel, statusLabel, rowCount, columnCount; 
@FXML
private ComboBox<String> hallComboBox;

    @FXML
    public void rowAddCount(ActionEvent event){
        addCount(0,rowCount);
    }
    
    @FXML
    public void rowMinusCount(ActionEvent event){
        minusCount(0,rowCount);
    }
    
    @FXML
    public void columnAddCount(ActionEvent event){
        addCount(0,columnCount);
    }
    
    @FXML
    public void columnMinusCount(ActionEvent event){
        minusCount(0,columnCount);
        
    }
    
   

    

@FXML
public void switchToAdminMain(ActionEvent event) throws IOException{
    SceneController switchScene = new SceneController();
    switchScene.switchToAdminMain(event);
}
private int[] rowColumns=new int[]{0,0};
private int totalRowColumn=0;

private void minusCount(int index, Label label) {
        // deduct number of tickets
        if (rowColumns[index] > 0) {
            --rowColumns[index];
            label.setText(Integer.toString(rowColumns[index]));
            updateTotalRowColumn();
        }
      
    }

   private void addCount(int index, Label label) {
        // add number of tickets
        if (rowColumns[index] < 20) {
            ++rowColumns[index];
            label.setText(Integer.toString(rowColumns[index]));
            updateTotalRowColumn();
        }

    }
   
        private void updateTotalRowColumn() {
        int sum = 0;
        for (int rowColumn : rowColumns) {
            sum += rowColumn;
        }
        totalRowColumn = sum;
    }
 @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        seatsContainer.setAlignment(Pos.CENTER);
        int col_num = 0; //Follow sql, Column index need +2
        ColumnConstraints[] colCons = new ColumnConstraints[col_num];

        for(int i =0; i<col_num; i++){
            ColumnConstraints col = new ColumnConstraints(100, 100, Double.MAX_VALUE);
            colCons[i]=col;
    
 }
        seatsContainer.getColumnConstraints().addAll(colCons);


        int row_num = 0; //Follow sql
        RowConstraints[] rowCons = new RowConstraints[row_num];

        for(int i =0; i<col_num; i++){
            RowConstraints row = new RowConstraints(100, 100, Double.MAX_VALUE);
            rowCons[i]=row;
  }

        seatsContainer.getRowConstraints().addAll(rowCons);   



//8 Hall options ComboBox
        hallComboBox.getItems().addAll("Hall 01", "Hall 02", "Hall 03","Hall 04","Hall 05","Hall 06","Hall 07","Hall 08");
        
          

    }

  

   
  
}


  
   


