/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.foptproject.controller;

import com.fop.Utility.JSONToolSets;
import com.fop.Utility.readConfig;
import com.fop.Utility.sqlConnect;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import java.lang.String;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

/**
 *
 * @author xyche, shiaoyin
 */
public class AdminSeatsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private GridPane seatsContainer;
    @FXML
    private Button confirmButton, backButton, rowMinus, rowAdd, columnMinus, columnAdd;
    @FXML
    private Label hallLabel, cinemaLabel, totalSeatsAvailableLabel, totalSeatsUnavailableLabel, totalSeatsLabel, rowCount, columnCount;

    @FXML
    public void rowAddCount(ActionEvent event) {
        addCountR(0, rowCount);
    }

    @FXML
    public void rowMinusCount(ActionEvent event) {
        minusCountR(0, rowCount);
    }

    @FXML
    public void columnAddCount(ActionEvent event) {
        addCountC(0, columnCount);
    }

    @FXML
    public void columnMinusCount(ActionEvent event) {
        minusCountC(0, columnCount);

    }

    @FXML
    public void switchToAdminMovie(ActionEvent event) throws IOException {
        SceneController switchScene = new SceneController();
        switchScene.switchToAdminMovie(event);
    }
    
    ArrayList<int[]> alteredSeats = new ArrayList<>();

    private int status = 0;
    private int[] row = new int[]{0};
    private int[] column = new int[]{0};
    private int totalSeats = 105;
    private int unavailableSeats = 0;

    private void minusCountR(int index, Label label) {
        // deduct number of row 
        if (row[index] > 0) {
            --row[index];
            label.setText(Integer.toString(row[index]));

        }

    }

    private void minusCountC(int index, Label label) {
        //deduct number of column
        if (column[index] > 0) {
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

    private void addCountC(int index1, Label label) {
        //add number of column
        if (column[index1] < 6) {
            ++column[index1];
            label.setText(Integer.toString(column[index1]));

        }
    }

    private int colIndex(int col, int maxcolumn) {
        // change from gridpane col to array col

        if (col >= maxcolumn) {
            return col - 3;
        } else if (col >= 3) {
            return col - 2;
        } else {
            return col - 1;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialise 
        totalSeatsLabel.setText(String.valueOf(totalSeats));
        totalSeatsAvailableLabel.setText(String.valueOf(totalSeats));
        totalSeatsUnavailableLabel.setText(String.valueOf(unavailableSeats));
        

        seatsContainer.setAlignment(Pos.CENTER);
        int col_num = 0; //Follow sql, Column index need +2
        ColumnConstraints[] colCons = new ColumnConstraints[col_num];

        for (int i = 0; i < col_num; i++) {
            ColumnConstraints col = new ColumnConstraints(100, 100, Double.MAX_VALUE);
            colCons[i] = col;

        }
        seatsContainer.getColumnConstraints().addAll(colCons);

        int row_num = 0; //Follow sql
        RowConstraints[] rowCons = new RowConstraints[row_num];

        for (int i = 0; i < col_num; i++) {
            RowConstraints row = new RowConstraints(100, 100, Double.MAX_VALUE);
            rowCons[i] = row;
        }

        seatsContainer.getRowConstraints().addAll(rowCons);

//        sqlConnect sql = new sqlConnect();
//        JSONToolSets json = new JSONToolSets(sql.querySeats("8", "1", true), true);
        JSONToolSets json;
        try {
            json = new JSONToolSets(readConfig.readSeatTemplate());
        } catch (FileNotFoundException ex) {
            json = null;
            Logger.getLogger(AdminSeatsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HashMap<String, ArrayList<String>> seatArr = json.parseTheaterSeat(5);
        int maxcolumn = json.getColumn();
        int maxrow = json.getRow();
        json.addColumn(1, true);
        json.addRow(1);
        String jsonString = json.getNewSeatArr().toString();
        final ObservableList<Node> gridPaneChildren = seatsContainer.getChildren();
        for (int i = 0; i < gridPaneChildren.size(); i++) {
            Node node = gridPaneChildren.get(i);
            final int k = i;

            if (node instanceof CheckBox) {
                CheckBox seat = (CheckBox) node;
                final int gridCol = GridPane.getColumnIndex(seat);
                seat.selectedProperty().addListener(
                        (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                            if (old_val == false && new_val == true) {
                                unavailableSeats++;
                            } else {
                                unavailableSeats--;
                            }
                            
                            totalSeatsLabel.setText(String.valueOf(totalSeats));
                            totalSeatsAvailableLabel.setText(String.valueOf(totalSeats - unavailableSeats));
                            totalSeatsUnavailableLabel.setText(String.valueOf(unavailableSeats));
                        });
            }
            
                
                
          
        }
        confirmButton.setOnAction(e->{
                for (int j = 0; j < gridPaneChildren.size(); j++){
                    final Node m = gridPaneChildren.get(j);
                    final int n = j;

                    if (m instanceof CheckBox) {
                        CheckBox seat = (CheckBox) m;
                        final int gridCol = GridPane.getColumnIndex(seat);
                        int row = GridPane.getRowIndex(seat) - 1;
                        int col = colIndex(gridCol, maxcolumn);
                        
                        if (seat.selectedProperty().get()==true){
                            
                        }}
                        
                        
                    
        //sql.updateSeats(jsonString,"1",true);
    }});
                
    }
}
