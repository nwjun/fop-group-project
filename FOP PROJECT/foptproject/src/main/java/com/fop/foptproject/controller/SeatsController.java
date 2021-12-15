package com.fop.foptproject.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fop.Utility.JSONToolSets;
import com.fop.Utility.sqlConnect;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * FXML Controller class
 *
 * @author jun
 */
public class SeatsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private GridPane seatsContainer;
    @FXML
    private Button studentMinus, studentAdd, adultMinus, adultAdd, okuMinus, okuAdd, nextButton;
    @FXML
    private Label studentCount, adultCount, okuCount, selectedTicketLabel, totalLabel;

    @FXML
    public void changeToMovieBooking(ActionEvent event) throws IOException {
        SceneController switchScene = new SceneController();
        switchScene.switchToMovieBooking(event);
    }

    private ArrayList<String[]> selected = new ArrayList<String[]>();
    private sqlConnect sql = new sqlConnect();
    private int selectedLength = 0;
    // no of ticket for adult, student, OKU
    private int[] tickets = new int[]{0, 0, 0};
    private int totalTicket = 0;

    private void minusCount(int index, Label label) {
        // deduct number of tickets
        if (tickets[index] > 0) {
            --tickets[index];
            label.setText(Integer.toString(tickets[index]));
            updateTotalTicket();
        }
        // reset
        if (totalTicket == 0 || totalTicket < selectedLength) {
            ObservableList<Node> children = seatsContainer.getChildren();

            for (Node node : children) {
                if (node instanceof CheckBox) {
                    CheckBox seat = (CheckBox) node;
                    seat.setSelected(false);
                }
                selectedLength = 0;
                selected = new ArrayList<>();
            }
        }
    }

    private void addCount(int index, Label label) {
        // add number of tickets
        if (tickets[index] < 20) {
            ++tickets[index];
            label.setText(Integer.toString(tickets[index]));
            updateTotalTicket();
        }

    }

    // switch to next scene
    @FXML
    void toFnB(ActionEvent event) throws IOException {
        new SceneController().switchToFnB(event);
    }

    // add and minus method for each categories
    @FXML
    void adultMinusCount(ActionEvent event) {
        minusCount(0, adultCount);
    }

    @FXML
    void studentMinusCount(ActionEvent event) {
        minusCount(1, studentCount);
    }

    @FXML
    void okuMinusCount(ActionEvent event) {
        minusCount(2, okuCount);
    }

    @FXML
    void adultAddCount(ActionEvent event) {
        addCount(0, adultCount);
    }

    @FXML
    void studentAddCount(ActionEvent event) {
        addCount(1, studentCount);
    }

    @FXML
    void okuAddCount(ActionEvent event) {
        addCount(2, okuCount);
    }

    private void updateTotalTicket() {
        int sum = 0;
        for (int ticket : tickets) {
            sum += ticket;
        }
        totalTicket = sum;
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double[] prices = new double[]{14.0, 14.0, 14.0};
        double total = 0;
        for (int i = 0; i < prices.length; i++) {
            total += prices[i] * tickets[i];
        }
        totalLabel.setText(String.format("TOTAL: RM %.2f", total));

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final String THEATER_ID, SLOT, DAY;
        THEATER_ID = "1";
        SLOT = "1";
        DAY = "0";

        ArrayList<ArrayList<String>> seatsTemp = getMovieSeats(THEATER_ID, SLOT, DAY);

        int maxRow = seatsTemp.size();
        int maxCol = seatsTemp.get(0).size();
        System.out.println(maxCol);
        // gridPane.add(item, col,row)
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
                    continue;
                }

                if (col >= maxCol) {
                    tempCol = col - 3;

                } else if (col >= 3) {
                    tempCol = col - 2;
                }

                if (row == 0) {
                    if (col != 0) {
                        Label val = new Label(String.valueOf(tempCol+1));
                        val.getStyleClass().add("seatsLabel");
                        val.setAlignment(Pos.CENTER);

                        seatsContainer.add(val, col, row);
                    }

                } else {
                    if (col == 0) {
                        Label val = new Label((char) (65 + (row - 1)) + "");
                        val.getStyleClass().add("seatsLabel");
                        val.setAlignment(Pos.CENTER);
                        seatsContainer.add(val, col, row);
                    } else {
                        CheckBox newSeat = new CheckBox();

                        String val = seatsTemp.get(tempRow).get(tempCol);
                        if (val.equals("1")) {
                            newSeat.setDisable(true);
                            newSeat.setId("soldSeat");
                        } else if (val.equals("-1")) {
                            newSeat.setDisable(true);

                        }
                        seatsContainer.add(newSeat, col, row);
                    }

                }
            }

        }

        ObservableList<Node> seats = seatsContainer.getChildren();
        for (Node node : seats) {
            if (node instanceof CheckBox) {
                CheckBox seat = (CheckBox) node;
                // add listener for checking and unchecking box
                seat.selectedProperty().addListener(
                        (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                            int row = GridPane.getRowIndex(seat) - 1;
                            int col = GridPane.getColumnIndex(seat) - 1;

                            // Selected
                            if (old_val == false && new_val == true && selectedLength < totalTicket) {
                                selected.add(new String[]{String.valueOf(row), String.valueOf(col)});

                            } // Unselected
                            else if (old_val == true && new_val == false) {
                                // new int[]{1,2} == new int[]{1,2} will return false as "==" compare references to objects(address)
                                // have to use equals to compare content
                                // remove [row,col] from ArrayList if unchecked
                                selected.removeIf(n -> Arrays.equals(n, new String[]{String.valueOf(row), String.valueOf(col)}));

                            } else {
                                seat.selectedProperty().set(old_val);
                            }
                            selectedLength = selected.size();
                            selectedTicketLabel.setText(Integer.toString(selectedLength));
                        });
                
            }
        }
        
        nextButton.setOnAction(e->{
            RealTimeStorage.setSelectedSeats(selected);
            RealTimeStorage.setTicketType(tickets);
        });
    }

    public ArrayList<ArrayList<String>> getMovieSeats(String theaterID, String slot, String day) {
        JSONToolSets json = new JSONToolSets(sql.querySeats(theaterID, slot, false), false);
        HashMap<String, ArrayList<String>> seatsHash = json.parseTheaterSeat(Integer.parseInt(day));
        ArrayList<ArrayList<String>> seatsArr = new ArrayList<>();
        Set<String> rowsNo = seatsHash.keySet();

        for (String rowNo : rowsNo) {
            seatsArr.add(Integer.parseInt(rowNo), seatsHash.get(rowNo));
        }

        if (seatsArr.size() == 0) {
            for (int i = 0; i < 10; i++) {
                ArrayList<String> temp = new ArrayList<>();
                for (int j = 0; j < 10; j++) {
                    temp.add("1");
                }
                seatsArr.add(temp);
                System.out.println(seatsArr.size());
            }

        }
        return seatsArr;
    }
}
