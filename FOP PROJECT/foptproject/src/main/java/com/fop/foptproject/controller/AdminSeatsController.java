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
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Priority;
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

    @FXML
    private GridPane seatsContainer;
    @FXML
    private Button confirmButton, backButton, rowMinus, rowAdd, columnMinus, columnAdd;
    @FXML
    private Label hallLabel, cinemaLabel, totalSeatsAvailableLabel, totalSeatsUnavailableLabel, totalSeatsLabel, rowCount, columnCount;
    
    private sqlConnect sql = new sqlConnect();
    private JSONToolSets json;
    private int maxCol = 15, maxRow = 7;
    private ArrayList<int[]> alteredSeats = new ArrayList<>();
    private int row = 0;
    private int column = 0;
    private int status = 0;
    private int totalSeats = 105;
    private int unavailableSeats = 0;


    @FXML
    public void rowAddCount(ActionEvent event) {
        addCountR(0, rowCount);
        RowConstraints rowConst = createRowConstraints();
        seatsContainer.getRowConstraints().add(rowConst);
        addRowSeats();
    }

    @FXML
    public void rowMinusCount(ActionEvent event) {
        minusCountR(0, rowCount);
        RowConstraints rowConst = createRowConstraints();
        seatsContainer.getRowConstraints().remove(rowConst);
        if (row != 0) {
            minusRowSeats();
        }
    }

    @FXML
    public void columnAddCount(ActionEvent event) {
        addCountC(0, columnCount);
        ColumnConstraints colConst = createColumnConstraints();
        seatsContainer.getColumnConstraints().add(colConst);
        addColSeats();
    }

    @FXML
    public void columnMinusCount(ActionEvent event) {
        minusCountC(0, columnCount);
        ColumnConstraints colConst = createColumnConstraints();
        seatsContainer.getColumnConstraints().remove(colConst);
        if (column != 0) {
            minusColSeats();
        }

    }

    @FXML
    public void switchToAdminMovie(ActionEvent event) throws IOException {
        SceneController switchScene = new SceneController();
        switchScene.switchToAdminMovie(event);
    }

    private void minusCountR(int index, Label label) {
        // deduct number of row 
        if (row > 0) {
            --row;
            label.setText(Integer.toString(row));
        }

    }

    private void minusCountC(int index, Label label) {
        //deduct number of column
        if (column > 0) {
            --column;
            label.setText(Integer.toString(column));
        }
    }

    private void addCountR(int index, Label label) {
        // add number of row
        if (row < 4) {
            ++row;
            label.setText(Integer.toString(row));
        }

    }

    private void addCountC(int index1, Label label) {
        //add number of column
        if (column < 6) {
            ++column;
            label.setText(Integer.toString(column));
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
    
    public HashMap<String, ArrayList<String>> getSeatsTemplate(String theaterID) {
        boolean isTemplate = true;
        System.out.println("dayum");
        this.json = new JSONToolSets(sql.querySeats(theaterID, "1", isTemplate), isTemplate);
        HashMap<String, ArrayList<String>> seatArr = json.parseTheaterSeat(5);
        this.maxRow = json.getRow();
        this.maxCol = json.getColumn();
        return seatArr;
    }

    public void updateSeats() {
        // Update actual seats arrangement

    }

    public RowConstraints createRowConstraints() {
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
        int tempRow = row - 1;
        int tempCol = column - 1;
        tempCol = colIndex(column, maxCol);
        for (row = 0; row <= maxRow; row++) {
            if (column == 0) {
                Label newLab = new Label((char) (65 + (row - 1)) + "");
                newLab.getStyleClass().add("seatsLabel");
                newLab.setAlignment(Pos.CENTER);
                seatsContainer.add(newLab, column, row);

            } else {
                if (column != 3 && column != maxCol - 2) {
                    CheckBox newSeat = new CheckBox();
                    seatsContainer.add(newSeat, column, row);

                }
            }
        }
    }

    public void addColSeats() {
        int tempRow = row - 1;
        int tempCol = column - 1;
        tempCol = colIndex(column, maxCol);
        for (column = 0; column <= maxCol; column++) {
            if (row == 0) {
                Label newLab = new Label(String.valueOf(tempCol + 1));
                newLab.getStyleClass().add("seatsLabel");
                newLab.setAlignment(Pos.CENTER);
                seatsContainer.add(newLab, column, row);
            } else {
                if (column != 3 && column != maxCol - 2) {
                    CheckBox newSeat = new CheckBox();
                    seatsContainer.add(newSeat, column, row);
                }
            }
        }
    }

    public void minusRowSeats() {
        int tempRow = row - 1;
        int tempCol = column - 1;
        tempCol = colIndex(column, maxCol);
        for (row = 0; row <= maxRow; row++) {
            if (column == 0) {
                Label newLab = new Label((char) (65 + (row - 1)) + "");
                newLab.getStyleClass().remove("seatsLabel");
                newLab.setAlignment(Pos.CENTER);
                seatsContainer.getChildren().removeIf(node -> GridPane.getRowIndex(node) == row);

            } else {
                if (column != 3 && column != maxCol - 2) {
                    seatsContainer.getChildren().removeIf(node -> GridPane.getRowIndex(node) == row);

                }
            }
        }

    }

    public void minusColSeats() {
        int tempRow = row - 1;
        int tempCol = column - 1;
        tempCol = colIndex(column, maxCol);
        for (column = 0; column <= maxCol; column++) {
            if (row == 0) {
                Label newLab = new Label(String.valueOf(tempCol + 1));
                newLab.getStyleClass().remove("seatsLabel");
                newLab.setAlignment(Pos.CENTER);
                seatsContainer.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == maxCol - 3);
                seatsContainer.add(newLab, maxCol - 2, row);

            } else {
                if (column != 3 && column != maxCol - 2) {
                    CheckBox newSeat = new CheckBox();
                    seatsContainer.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == maxCol - 3);
                    seatsContainer.add(newSeat, maxCol - 2, row);
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
                    int col = colIndex(gridCol, maxCol);

                    if (seat.selectedProperty().get() == true) {

                    }
                }

                //sql.updateSeats(jsonString,"1",true);
            }
        });

        for (int row = 0; row < maxRow + 1; row++) {
            // RowConstraints(double minHeight, double prefHeight, double maxHeight)
            RowConstraints rowConstraint = new RowConstraints(10, 30, Double.MAX_VALUE);
            rowConstraint.setVgrow(Priority.SOMETIMES);
            rowConstraint.setValignment(VPos.CENTER);
            seatsContainer.getRowConstraints().add(rowConstraint);
            for (int col = 0; col < maxCol + 3; col++) {

                int tempRow = row - 1;
                int tempCol = col - 1;

                if (row == 0) {
                    // ColumnConstraints(double minWidth, double prefWidth, double maxWidth)
                    ColumnConstraints colConstraint = new ColumnConstraints(10, 100, Double.MAX_VALUE);
                    colConstraint.setHgrow(Priority.SOMETIMES);
                    colConstraint.setHalignment(HPos.CENTER);
                    seatsContainer.getColumnConstraints().add(colConstraint);
                }

                if (col == 3 || col == maxCol) {
                    // skip lane
                    continue;
                }

                tempCol = colIndex(col, maxCol);

                if (row == 0) {
                    // Add column label, starting from 1
                    if (col != 0) {
                        Label val = new Label(String.valueOf(tempCol + 1));
                        val.getStyleClass().add("seatsLabel");
                        val.setAlignment(Pos.CENTER);
                        // gridPane.add(item, col,row)
                        seatsContainer.add(val, col, row);
                    }

                } else {
                    if (col == 0) {
                        // Add row label, starting from A
                        Label val = new Label((char) (65 + (row - 1)) + "");
                        val.getStyleClass().add("seatsLabel");
                        val.setAlignment(Pos.CENTER);
                        seatsContainer.add(val, col, row);
                    } else {

                        // Set seat availability
                        String val = seatsTemp.get(tempRow+"").get(tempCol);
                        CheckBox newSeat = new CheckBox();
                        if (val.equals("-1")) {
                            newSeat.setDisable(true);
                            newSeat.getStyleClass().add("availableSeat");
                        } else {
                            newSeat.getStyleClass().add("availableSeat");
                        }
                        seatsContainer.add(newSeat, col, row);
                    }

                }
            }

        }

    }
}
