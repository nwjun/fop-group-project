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
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Priority;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

/**
 *
 * @author xyche, shiaoyin
 */
public class AdminSeatsController implements Initializable {

    @FXML
    private GridPane seatsContainer;
    @FXML
    private Button confirmButton, backButton, rowMinus, rowAdd, columnMinus, columnAdd;
    @FXML
    private Label hallLabel, cinemaLabel, totalSeatsAvailableLabel, totalSeatsUnavailableLabel, totalSeatsLabel, rowCount, columnCount;

    private sqlConnect sql = new sqlConnect();
    private JSONToolSets json;
    private int initialCol = 15, initialRow = 7;
    private ArrayList<int[]> alteredSeats = new ArrayList<>();
    private int addedRow = 0;
    private int addedColumn = 0;
    private int maxCol = initialCol;
    private int maxRow = initialRow;
    private int status = 0;
    private int totalSeats = 105;
    private int unavailableSeats = 0;
    private ArrayList<ArrayList<CheckBox>> addedSeat;

    @FXML
    public void rowAddCount(ActionEvent event) {
        if (addedRow < 6) {
            maxRow++;
            addCountR(rowCount);
            RowConstraints rowConst = createRowConstraints();
            seatsContainer.getRowConstraints().add(rowConst);
            addRowSeats();
            updateSeatsLabel();
        }
    }

    @FXML
    public void rowMinusCount(ActionEvent event) {
        if (addedRow != 0) {
            maxRow--;
            minusCountR(rowCount);
            //System.out.println(seatsContainer.getRowCount());
            minusRowSeats(seatsContainer.getRowCount() - 1);
            seatsContainer.getRowConstraints().remove(seatsContainer.getRowCount() - 1);
            updateSeatsLabel();
        }
    }

    @FXML
    public void columnAddCount(ActionEvent event) {
        if (addedColumn < 4) {
            maxCol++;
            addCountC(columnCount);
            ColumnConstraints colConst = createColumnConstraints();
            seatsContainer.getColumnConstraints().add(colConst);
            addColSeats();
            updateSeatsLabel();
        }
    }

    @FXML
    public void columnMinusCount(ActionEvent event) {
        minusCountC(columnCount);
        ColumnConstraints colConst = createColumnConstraints();
        seatsContainer.getColumnConstraints().remove(colConst);
        if (addedColumn != 0) {
            minusColSeats();
            updateSeatsLabel();
        }

    }

    @FXML
    public void switchToAdminMovie(ActionEvent event) throws IOException {
        SceneController switchScene = new SceneController();
        switchScene.switchToAdminMovie(event);
    }

    private void minusCountR(Label label) {
        // deduct number of row 
        if (addedRow > 0) {
            --addedRow;
            label.setText(Integer.toString(addedRow));
        }

    }

    private void minusCountC(Label label) {
        //deduct number of column
        if (addedColumn > 0) {
            --addedColumn;
            label.setText(Integer.toString(addedColumn));
        }
    }

    private void addCountR(Label label) {
        // add number of row
        ++addedRow;
        label.setText(Integer.toString(addedRow));

    }

    private void addCountC(Label label) {
        //add number of column
        if (addedColumn < 6) {
            ++addedColumn;
            label.setText(Integer.toString(addedColumn));
        }
    }

    private int colIndex(int col, int initialColumn) {
        // change from gridpane col to array col

        if (col >= initialColumn) {
            return col - 3;
        } else if (col >= 3) {
            return col - 2;
        } else {
            return col - 1;
        }
    }

    public HashMap<String, ArrayList<String>> getSeatsTemplate(String theaterID) throws FileNotFoundException {
        boolean isTemplate = true;
        System.out.println("dayum");
//        this.json = new JSONToolSets(sql.querySeats(theaterID, "1", isTemplate), isTemplate);
        this.json = new JSONToolSets(readConfig.readSeatTemplate(), isTemplate);
        HashMap<String, ArrayList<String>> seatArr = json.parseTheaterSeat(5);
        this.initialRow = json.getRow();
        this.initialCol = json.getColumn();
        return seatArr;
    }

    public void updateSeats() {
        // Update actual seats arrangement

    }

    public void updateSeatsLabel() {
        totalSeats = (initialRow + addedRow) * (addedColumn + initialCol);
        totalSeatsLabel.setText(String.valueOf(totalSeats));
    }

    public RowConstraints createRowConstraints() {
        //RowConstraints(double minHeight, double prefHeight, double maxHeight)
        RowConstraints rowConstraint = new RowConstraints(10, 30, Double.MAX_VALUE);
        rowConstraint.setVgrow(Priority.SOMETIMES);
        rowConstraint.setValignment(VPos.CENTER);
        return rowConstraint;
    }

    public ColumnConstraints createColumnConstraints() {
        ColumnConstraints colConstraint = new ColumnConstraints(10, 100, Double.MAX_VALUE);
        colConstraint.setHgrow(Priority.SOMETIMES);
        colConstraint.setHalignment(HPos.CENTER);
        return colConstraint;
    }

    public void addRowSeats() {
        int addSeatsRow = maxRow;
        int maxGridCol = maxCol + 3;
        for (int col = 0; col < maxGridCol; col++) {
            if (col == 0) {
                Label newLab = new Label((char) (65 + (addSeatsRow - 1)) + "");
                newLab.getStyleClass().add("seatsLabel");
                newLab.setAlignment(Pos.CENTER);
                seatsContainer.add(newLab, col, addSeatsRow);
            } else {
                if (col != 3 && col != maxGridCol - 3) {
                    CheckBox newSeat = new CheckBox();
                    seatsContainer.add(newSeat, col, addSeatsRow);
                    System.out.println(GridPane.getRowIndex(newSeat));

                }
            }
        }
    }

    public void addColSeats() {
//        int tempRow = row - 1;
//        int tempCol = column - 1;
//        tempCol = colIndex(column, initialCol);
//        for (column = 0; column <= initialCol; column++) {
//            if (row == 0) {
//                Label newLab = new Label(String.valueOf(tempCol + 1));
//                newLab.getStyleClass().add("seatsLabel");
//                newLab.setAlignment(Pos.CENTER);
//                seatsContainer.add(newLab, column, row);
//            } else {
//                if (column != 3 && column != initialCol - 2) {
//                    CheckBox newSeat = new CheckBox();
//                    seatsContainer.add(newSeat, column, row);
//                }
//            }
//        }
//maxcol = 16
//        int maxGridRow = maxRow + 1;
//        int gridCol;
//        int colLabel;
//        //col = 14, col<17
//        for (int col = maxCol - 2; col < maxCol + 1; col++) {
//            gridCol = col + 1; // gridcol = 14+1=15
//            colLabel = col;//gridlabel = 14
//            for (int row = 0; row < maxGridRow; row++) {
//                //if col == 15
//                if (col == maxCol - 1) {
//                    // remove 15+15*0
//                    seatsContainer.getChildren().remove(col+ (maxCol) * row -1);
//                }
//                // if col>15
////                else if (col > maxCol - 1) {
////                    //col=16
////                    // gridcol = 17
////                    //collabel = 15
////                    gridCol++;
////                    colLabel--;
////                }
//                if (row == 0) {
//                    Label newLab = new Label("");
//                    newLab.getStyleClass().add("seatsLabel");
//                    newLab.setAlignment(Pos.CENTER);
//                    seatsContainer.add(newLab, gridCol, row);
//                } else {
//                    CheckBox newSeat = new CheckBox();
//                    seatsContainer.add(newSeat, gridCol, row);
//                }
//            }
//        }
        
        int maxGridRow = maxRow + 1;
        int gridCol;
        int colLabel;
        for(int i = 0 ; i < 4 ; i++){
            gridCol = maxCol-1+i;
            for(int row = 0 ; row < maxGridRow; row++){
                if(i == 0){
                    if(row == 0){
                        Label newLab = new Label(String.valueOf(gridCol-1));
                        newLab.getStyleClass().add("seatsLabel");
                        newLab.setAlignment(Pos.CENTER);
                        seatsContainer.add(newLab, gridCol, row);
                    }
                    else{
                        CheckBox newSeat = new CheckBox();
                        seatsContainer.add(newSeat, gridCol, row);
                    }
                }
                else if(i == 1){
                    if((row+1)*(maxCol)>maxGridRow*(maxCol)){
                        break;
                    }
                    System.out.println((row+1)*(maxCol+1));
                    seatsContainer.getChildren().remove((row+1)*(maxCol+1));
                    
                }
            
            }
        
        }


    }

    public void minusRowSeats(int removeRow) {
        int endIndex = seatsContainer.getChildren().size();
        int startIndex = endIndex - maxCol - 1;
        seatsContainer.getChildren().remove(startIndex, endIndex);
    }

    public void minusColSeats() {
        int tempRow = row - 1;
        int tempCol = column - 1;
        tempCol = colIndex(column, initialCol);
        for (column = 0; column <= initialCol; column++) {
            if (row == 0) {
                Label newLab = new Label(String.valueOf(tempCol + 1));
                newLab.getStyleClass().remove("seatsLabel");
                newLab.setAlignment(Pos.CENTER);
                seatsContainer.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == initialCol - 3);
                seatsContainer.add(newLab, initialCol - 2, row);

            } else {
                if (column != 3 && column != initialCol - 2) {
                    CheckBox newSeat = new CheckBox();
                    seatsContainer.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == initialCol - 3);
                    seatsContainer.add(newSeat, initialCol - 2, row);
                }
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        final String THEATER_ID;
//        THEATER_ID = RealTimeStorage.getMovieBooking().get("theaterId").toString();
        THEATER_ID = "8";
        // Initialise 
        totalSeatsLabel.setText(String.valueOf(totalSeats));
        totalSeatsAvailableLabel.setText(String.valueOf(totalSeats));
        totalSeatsUnavailableLabel.setText(String.valueOf(unavailableSeats));

        hallLabel.setText("HALL " + THEATER_ID);

        HashMap<String, ArrayList<String>> seatsTemp = getSeatsTemplate(THEATER_ID);
//        json.addColumn(1, true);
//        json.addRow(1);
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
        confirmButton.setOnAction(e -> {
            for (int j = 0; j < gridPaneChildren.size(); j++) {
                final Node m = gridPaneChildren.get(j);
                final int n = j;

                if (m instanceof CheckBox) {
                    CheckBox seat = (CheckBox) m;
                    final int gridCol = GridPane.getColumnIndex(seat);
                    int row = GridPane.getRowIndex(seat) - 1;
                    int col = colIndex(gridCol, initialCol);

                    if (seat.selectedProperty().get() == true) {

                    }
                }

                //sql.updateSeats(jsonString,"1",true);
            }
        });

//        for (int row = 0; row < initialRow + 1; row++) {
//            
//            for (int col = 0; col < initialCol + 3; col++) {
//
//                int tempRow = row - 1;
//                int tempCol = col - 1;
//
//                tempCol = colIndex(col, initialCol);
//
//                if (row == 0) {
//                    // Add column label, starting from 1
//                    if (col != 0) {
//                        Label val = new Label(String.valueOf(tempCol + 1));
//                        val.getStyleClass().add("seatsLabel");
//                        val.setAlignment(Pos.CENTER);
//                        // gridPane.add(item, col,row)
//                        seatsContainer.add(val, col, row);
//                    }
//
//                } else {
//                    if (col == 0) {
//                        // Add row label, starting from A
//                        Label val = new Label((char) (65 + (row - 1)) + "");
//                        val.getStyleClass().add("seatsLabel");
//                        val.setAlignment(Pos.CENTER);
//                        seatsContainer.add(val, col, row);
//                    } else {
//
//                        // Set seat availability
//                        String val = seatsTemp.get(tempRow+"").get(tempCol);
//                        CheckBox newSeat = new CheckBox();
//                        if (val.equals("-1")) {
//                            newSeat.setDisable(true);
//                            newSeat.getStyleClass().add("availableSeat");
//                        } else {
//                            newSeat.getStyleClass().add("availableSeat");
//                        }
//                        seatsContainer.add(newSeat, col, row);
//                    }
//
//                }
//            }
//
//        }
    }
}
