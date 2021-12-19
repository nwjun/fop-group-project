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
import java.lang.String;


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
        addCountR(0,rowCount);
    }
    
    @FXML
    public void rowMinusCount(ActionEvent event){
        minusCountR(0,rowCount);
    }
    
    @FXML
    public void columnAddCount(ActionEvent event){
        addCountC(0,columnCount);
    }
    
    @FXML
    public void columnMinusCount(ActionEvent event){
        minusCountC(0,columnCount);
        
    }
    
   

    

@FXML
public void switchToAdminMovie(ActionEvent event) throws IOException{
    SceneController switchScene = new SceneController();
    switchScene.switchToAdminMovie(event);
}


private int totalRowColumn=0;
private int[]row= new int[]{0};
private int[]column= new int []{0};

private void minusCountR(int index, Label label) {
        // deduct number of row 
        if (row[index] > 0) {
            --row[index];
            label.setText(Integer.toString(row[index]));
            
        }
      
    }

private void minusCountC(int index, Label label){
    //deduct number of column
    if (column[index]>0){
        --column[index];
        label.setText(Integer.toString(column[index]));
       
    }
}

   private void addCountR(int index, Label label) {
        // add number of row
        if (row[index] < 4) {
            ++row[index];
            label.setText(Integer.toString(row[index]));
            
        }

    }
   
   private void addCountC(int index1, Label label){
       //add number of column
       if (column[index1]<6){
           ++column[index1];
           label.setText(Integer.toString(column[index1]));
         
       }
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
        
        
        
        
        sqlConnect sql = new sqlConnect();
        JSONToolSets json = new JSONToolSets(sql.querySeats("8","1",true),true);
        HashMap<String,ArrayList<String>> seatArr = json.parseTheaterSeat(5);
        int row = json.getRow();
        int column = json.getColumn();
        json.addColumn(1,true);
        json.addRow(1);
        String jsonString = json.getNewSeatArr().toString();
        sql.updateSeats(jsonString,"1",true);
    }

  

   
  
}


  
   


