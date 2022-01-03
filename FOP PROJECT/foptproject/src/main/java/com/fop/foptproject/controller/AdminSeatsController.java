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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.json.JSONObject;

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
    private int testCol = 0;
    private int testRow = 0;
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
            minusRowSeats(seatsContainer.getRowCount() - 1);
            seatsContainer.getRowConstraints().remove(seatsContainer.getRowCount() - 1);
            updateSeatsLabel();
        }
    }

    @FXML
    public void columnAddCount(ActionEvent event) {
        if (addedColumn < 6) {
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
        if (addedColumn != 0) {
            minusCountC(columnCount);
            minusColSeats();
            seatsContainer.getColumnConstraints().remove(seatsContainer.getColumnCount() - 1);       
            updateSeatsLabel();
            maxCol--;
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

    public HashMap<String, ArrayList<String>> getSeatsTemplate(String theaterID){
        boolean isTemplate = true;
        this.json = new JSONToolSets(sql.querySeats(theaterID, "1", isTemplate), isTemplate);
//        this.json = new JSONToolSets(readConfig.readSeatTemplate(), isTemplate);
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
                newLab.setId(addSeatsRow + "," + col);
                seatsContainer.add(newLab, col, addSeatsRow);
            } else {
                if (col != 3 && col != maxGridCol - 3) {
                    CheckBox newSeat = new CheckBox();
                    newSeat.setId(addSeatsRow + "," + col);
                    setSeatListener(newSeat);
                    seatsContainer.add(newSeat, col, addSeatsRow);
                }
            }
        }
    }

    public void addColSeats() {
        int maxGridRow = maxRow + 1;
        int gridCol;

        for (int i = 0; i < 3; i++) {
            gridCol = maxCol - 1 + i;
            for (int row = 0; row < maxGridRow; row++) {
                if (i == 0) {
                    if (row == 0) {
                        Label newLab = new Label(String.valueOf(gridCol - 1));
                        newLab.getStyleClass().add("seatsLabel");
                        newLab.setAlignment(Pos.CENTER);
                        newLab.setId(row + "," + gridCol);
                        seatsContainer.add(newLab, gridCol, row);
                    } else {
                        CheckBox newSeat = new CheckBox();
                        newSeat.setId(row + "," + gridCol);
                        setSeatListener(newSeat);
                        seatsContainer.add(newSeat, gridCol, row);
                    }

                } else if (i == 1) {
                    ArrayList<Object> toBeRemoved = new ArrayList<>();
                    final ObservableList<Node> gridPaneChildren = seatsContainer.getChildren();
                    for (int j = 0; j < gridPaneChildren.size(); j++) {
                        if (gridPaneChildren.get(j).getId().split(",")[1].equals(gridCol + "")) {
                            toBeRemoved.add(gridPaneChildren.get(j));
                        }
                    }
                    for (Object obj : toBeRemoved) {
                        seatsContainer.getChildren().remove(obj);

                    }
                    
                    break;

                } else if (i == 2) {
                    int col = gridCol + 1;

                    if (row == 0) {
                        Label newLab = new Label(String.valueOf(gridCol - 1));
                        newLab.getStyleClass().add("seatsLabel");
                        newLab.setAlignment(Pos.CENTER);
                        newLab.setId(row + "," + col);
                        seatsContainer.add(newLab, col, row);
                    } else {
                        CheckBox newSeat = new CheckBox();
                        newSeat.setId(row + "," + col);
                        setSeatListener(newSeat);
                        seatsContainer.add(newSeat, col, row);

                    }
                }
            }
        }
    }

    public void minusRowSeats(int removeRow) {
        ArrayList<Object> toBeRemoved = new ArrayList<>();
        final ObservableList<Node> gridPaneChildren = seatsContainer.getChildren();
        for (int j = 0; j < gridPaneChildren.size(); j++) {
            if (gridPaneChildren.get(j).getId().split(",")[0].equals(removeRow + "")) {
                toBeRemoved.add(gridPaneChildren.get(j));
            }
        }
        for (Object obj : toBeRemoved) {
            seatsContainer.getChildren().remove(obj);
        }
    }

    public void minusColSeats() {
        int maxGridRow = maxRow + 1;
        int gridCol;

        for (int i = 0; i < 2; i++) {
            gridCol = maxCol - 1 + i;
            for (int row = 0; row < maxGridRow; row++) {
                if (i == 0) {
                    ArrayList<Object> toBeRemoved = new ArrayList<>();
                    final ObservableList<Node> gridPaneChildren = seatsContainer.getChildren();
                    for (int j = 0; j < gridPaneChildren.size(); j++) {
                        if (gridPaneChildren.get(j).getId().split(",")[1].equals(gridCol + "") || gridPaneChildren.get(j).getId().split(",")[1].equals(String.format("%s", gridCol+3))) {
                            toBeRemoved.add(gridPaneChildren.get(j));
                        }
                    }
                    for (Object obj : toBeRemoved) {
                        seatsContainer.getChildren().remove(obj);
                    }
                    break;

                } else if (i == 1) {
                    if (row == 0) {
                        Label newLab = new Label(String.valueOf(gridCol - 2));
                        newLab.getStyleClass().add("seatsLabel");
                        newLab.setAlignment(Pos.CENTER);
                        newLab.setId(row + "," + gridCol);
                        seatsContainer.add(newLab, gridCol, row);
                    } else {
                        CheckBox newSeat = new CheckBox();
                        newSeat.setId(row + "," + gridCol);
                        setSeatListener(newSeat);
                        seatsContainer.add(newSeat, gridCol, row);
                    }
                }
            }
        }
    }

    public void setSeatListener(GridPane pane) {
        final ObservableList<Node> paneChildren = pane.getChildren();
        for (int i = 0; i < paneChildren.size(); i++) {
            Node node = paneChildren.get(i);

            if (node instanceof CheckBox) {
                CheckBox seat = (CheckBox) node;
                seat.selectedProperty().addListener(
                        (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                            if (old_val == false && new_val == true) {
                                unavailableSeats++;
                                System.out.println(seat.getId());
                            } else {
                                unavailableSeats--;
                            }

                            totalSeatsLabel.setText(String.valueOf(totalSeats));
                            totalSeatsAvailableLabel.setText(String.valueOf(totalSeats - unavailableSeats));
                            totalSeatsUnavailableLabel.setText(String.valueOf(unavailableSeats));
                        });
            }
        }
    }

    public void setSeatListener(CheckBox node) {
        node.selectedProperty().addListener(
                (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                    if (old_val == false && new_val == true) {
                        unavailableSeats++;
                        System.out.println(node.getId());
                    } else {
                        unavailableSeats--;
                    }

                    totalSeatsLabel.setText(String.valueOf(totalSeats));
                    totalSeatsAvailableLabel.setText(String.valueOf(totalSeats - unavailableSeats));
                    totalSeatsUnavailableLabel.setText(String.valueOf(unavailableSeats));
                });
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

        // fetch seat template
        HashMap<String, ArrayList<String>> seatsTemp = new HashMap<>();

        seatsTemp = getSeatsTemplate(THEATER_ID);
        // load if have cache
        if(RealTimeStorage.getAdminSelected().size() > 0 && THEATER_ID.equals(RealTimeStorage.getAdminTheaterId())){
            for(String xy : RealTimeStorage.getAdminSelected()){
                int row = Integer.parseInt(xy.split(",")[0]) -1;
                int column = colIndex(Integer.parseInt(xy.split(",")[1]),Integer.parseInt(RealTimeStorage.getAdminCol()));
                seatsTemp.get(row+"").set(column, "-1");
            }
        }

        // generate seat arrangement in gridpane
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
                        val.setId(row + "," + col);
                        seatsContainer.add(val, col, row);
                    }

                } else {
                    if (col == 0) {
                        // Add row label, starting from A
                        Label val = new Label((char) (65 + (row - 1)) + "");
                        val.getStyleClass().add("seatsLabel");
                        val.setAlignment(Pos.CENTER);
                        val.setId(row + "," + col);
                        seatsContainer.add(val, col, row);
                    } else {
                        CheckBox newSeat = new CheckBox();
                        // Set seat availability
                        String val = seatsTemp.get(tempRow + "").get(tempCol);
                        if (val.equals("-1")) {
                            newSeat.setSelected(true);
                            unavailableSeats++;
                        } else {
                            newSeat.getStyleClass().add("availableSeat");
                        }
                        newSeat.setId(row + "," + col);
                        seatsContainer.add(newSeat, col, row);
                    }

                }
            }

        }
        // set label value
        totalSeatsLabel.setText(String.valueOf(totalSeats));
        totalSeatsAvailableLabel.setText(String.valueOf(totalSeats - unavailableSeats));
        totalSeatsUnavailableLabel.setText(String.valueOf(unavailableSeats));
        
        // set listener to checkboxes
        setSeatListener(seatsContainer);
        final ObservableList<Node> gridPaneChildren = seatsContainer.getChildren();

        confirmButton.setOnAction(e -> {
            RealTimeStorage.clearAdminSelected();
            for (int j = 0; j < gridPaneChildren.size(); j++) {
                final Node m = gridPaneChildren.get(j);
                final int n = j;

                if (m instanceof CheckBox) {
                    CheckBox seat = (CheckBox) m;
                    
                    // store selected seat to array list
                    if (seat.selectedProperty().get() == true) {
                        RealTimeStorage.addAdminSelected(seat.getId());
                    }
                }
            }
            RealTimeStorage.setAdminCol(maxCol+"");
            RealTimeStorage.setAdminRow(maxRow+"");
            RealTimeStorage.setAdminTheaterId(THEATER_ID);
            
            try {
                new SceneController().switchToAdminMovie(e);
            } catch (IOException ex) {
                Logger.getLogger(AdminSeatsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }
}
