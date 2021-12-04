package com.fop.foptproject.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

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
    public void changeToMovieBooking(ActionEvent event) throws IOException{
        SceneController switchScene = new SceneController();
        switchScene.switchToMovieBooking(event);
    }
  
    private ArrayList<int[]> selected = new ArrayList<int[]>();
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
        if (totalTicket == 0) {
            ObservableList<Node> children = seatsContainer.getChildren();

            for (Node node : children) {
                if (node instanceof CheckBox) {
                    CheckBox seat = (CheckBox) node;
                    seat.setSelected(false);
                }
                selectedLength = 0;
                selected = new ArrayList<int[]>();
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
    void toFnB(ActionEvent event) throws IOException{
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
                                selected.add(new int[]{row, col});

                            } // Unselected
                            else if (old_val == true && new_val == false) {
                                // new int[]{1,2} == new int[]{1,2} will return false as "==" compare references to objects(address)
                                // have to use equals to compare content
                                // remove [row,col] from ArrayList if unchecked
                                selected.removeIf(n -> Arrays.equals(n, new int[]{row, col}));

                            } else {
                                seat.selectedProperty().set(old_val);
                            }
                            selectedLength = selected.size();
                            selectedTicketLabel.setText(Integer.toString(selectedLength));
                        });

            }
        }
    }

}
