package com.fop.foptproject.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fop.Utility.JSONToolSets;
import com.fop.Utility.sqlConnect;
import com.fop.foptproject.App;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jun
 */
public class SeatsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private ArrayList<String[]> selected = new ArrayList<>();
    private sqlConnect sql = new sqlConnect();
    private int selectedLength = 0;
    // no of ticket for elder, adult, student, OKU
    private int[] tickets = new int[]{0, 0, 0, 0};
    private int totalTicket = 0;
    private final String[] ticketPrices = RealTimeStorage.getTicketPrices(RealTimeStorage.getMovieBooking().get("theaterType").equals("Premium"));
    //private final String[] ticketPrices = RealTimeStorage.getTicketPrices(false);

    @FXML
    private GridPane seatsContainer;
    @FXML
    private Button studentMinus, studentAdd, adultMinus, adultAdd, okuMinus, okuAdd, elderAdd, elderMinus, nextButton;
    @FXML
    private Label studentCount, adultCount, okuCount, elderCount, selectedTicketLabel,
            totalLabel, elderPrice, adultPrice, studentPrice, OKUPrice;

    private Label[] priceLabels;

    @FXML
    public void changeToMovieBooking(ActionEvent event) throws IOException {
        SceneController switchScene = new SceneController();
        switchScene.switchToMovieBooking(event);
    }

    // switch to next scene
    @FXML
    public void toFnB(ActionEvent event) throws IOException {
        RealTimeStorage.setSelectedSeats(selected);
        RealTimeStorage.setTicketType(tickets);
        new SceneController().switchToFnB(event);
    }

    // add and minus method for each categories
    @FXML
    public void elderMinusCount(ActionEvent event) {
        minusCount(0, elderCount);
    }

    @FXML
    public void adultMinusCount(ActionEvent event) {
        minusCount(1, adultCount);
    }

    @FXML
    public void studentMinusCount(ActionEvent event) {
        minusCount(2, studentCount);
    }

    @FXML
    public void okuMinusCount(ActionEvent event) {
        minusCount(3, okuCount);
    }

    @FXML
    public void elderAddCount(ActionEvent event) {
        addCount(0, elderCount);
    }

    @FXML
    public void adultAddCount(ActionEvent event) {
        addCount(1, adultCount);
    }

    @FXML
    public void studentAddCount(ActionEvent event) {
        addCount(2, studentCount);
    }

    @FXML
    public void okuAddCount(ActionEvent event) {
        addCount(3, okuCount);
    }

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

    private void updateTotalTicket() {
        int sum = 0;
        for (int ticket : tickets) {
            sum += ticket;
        }
        totalTicket = sum;
        updateTotalPrice();
        toggleNextBtn();
    }

    private void updateTotalPrice() {
        double total = 0;

        for (int i = 0; i < ticketPrices.length; i++) {
            total += Double.parseDouble(ticketPrices[i]) * tickets[i];
        }
        totalLabel.setText(String.format("TOTAL: RM %.2f", total));

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final String THEATER_ID, SLOT, DAY;
        THEATER_ID = RealTimeStorage.getMovieBooking().get("theaterId").toString();
        SLOT = RealTimeStorage.getMovieBooking().get("slots").toString();
        DAY = RealTimeStorage.getMovieBooking().get("chosenDay").toString();

//        THEATER_ID = "1";
//        SLOT = "1";
//        DAY = "1";
        ArrayList<ArrayList<String>> seatsTemp = getMovieSeats(THEATER_ID, SLOT, DAY);

        priceLabels = new Label[]{elderPrice, adultPrice, studentPrice, OKUPrice};

        int maxRow = seatsTemp.size();
        int maxCol = seatsTemp.get(0).size();

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
                        CheckBox newSeat = new CheckBox();
                        // Set seat availability
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
                            int col = colIndex(GridPane.getColumnIndex(seat), maxCol);

                            // Selected
                            if (old_val == false && new_val == true && selectedLength < totalTicket) {
                                selected.add(new String[]{String.valueOf(row), String.valueOf(col)});

                            } // Unselected
                            else if (old_val == true && new_val == false) {
                                // new string[]{1,2} == new string[]{1,2} will return false as "==" compare references to objects(address)
                                // have to use equals to compare content
                                // remove [row,col] from ArrayList if unchecked
                                selected.removeIf(n -> Arrays.equals(n, new String[]{String.valueOf(row), String.valueOf(col)}));

                            } else {
                                seat.selectedProperty().set(old_val);
                            }
                            selectedLength = selected.size();
                            selectedTicketLabel.setText(Integer.toString(selectedLength));
                            toggleNextBtn();
                        });

            }
        }

        for (int i = 0; i < ticketPrices.length; i++) {
            
            priceLabels[i].setText(String.format("RM%.2f",Double.parseDouble(ticketPrices[i])));
        }
    }

    public ArrayList<ArrayList<String>> getMovieSeats(String theaterID, String slot, String day) {
        JSONToolSets json = new JSONToolSets(sql.querySeats(theaterID, slot, false), false);
        HashMap<String, ArrayList<String>> seatsHash = json.parseTheaterSeat(Integer.parseInt(day));
        ArrayList<ArrayList<String>> seatsArr = new ArrayList<>();
        Set<String> rowsNo = seatsHash.keySet();

        for (String rowNo : rowsNo) {
            seatsArr.add(Integer.parseInt(rowNo), seatsHash.get(rowNo));
        }

        if (seatsArr.isEmpty()) {
            for (int i = 0; i < 10; i++) {
                ArrayList<String> temp = new ArrayList<>();
                for (int j = 0; j < 10; j++) {
                    temp.add("1");
                }
                seatsArr.add(temp);
            }

        }
        return seatsArr;
    }

    private int colIndex(int col, int maxCol) {
        // change from gridpane col to array col

        if (col >= maxCol) {
            return col - 3;
        } else if (col >= 3) {
            return col - 2;
        } else {
            return col - 1;
        }
    }

    private void toggleNextBtn() {
        // Only can proceed if the number of tickets added and the seats chosen match
        if (totalTicket == selectedLength && totalTicket != 0) {
            nextButton.setDisable(false);
        } else {
            nextButton.setDisable(true);
        }
    }
}
