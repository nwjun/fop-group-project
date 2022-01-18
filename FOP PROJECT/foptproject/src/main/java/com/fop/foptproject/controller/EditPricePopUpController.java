package com.fop.foptproject.controller;

import com.fop.Utility.sqlConnect;
import com.fop.foptproject.App;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 *
 * @author kuckn
 */
public class EditPricePopUpController implements Initializable {

    private HashMap<String, Double> tickets;
    
    @FXML
    private Button closeWindow;
    @FXML
    private TextField TS;
    @FXML
    private TextField TC;
    @FXML
    private TextField TE;
    @FXML
    private TextField TO;
    @FXML
    private TextField TP;
    @FXML
    private Button FinishEdit;
    @FXML
    private ImageView logo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(RealTimeStorage.getPermission().equals("3")){
            String path = App.class.getResource("assets/company/master.png").toString(); 
            Image img = new Image(path/*, IMGW, IMGH, false, false*/);
            logo.setImage(img);
        }
        getPrice();
    }    

    public void getPrice(){
        tickets = sqlConnect.queryTicketPrice();
        TS.setText(Double.toString(tickets.get("TS"))+"0");
        TC.setText(Double.toString(tickets.get("TC"))+"0");
        TE.setText(Double.toString(tickets.get("TE"))+"0");
        TO.setText(Double.toString(tickets.get("TO"))+"0");
        TP.setText(Double.toString(tickets.get("TP"))+"0");
    }
    
    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) closeWindow.getScene().getWindow();
        stage.close();
        RealTimeStorage.setAllTickets();
    }

    @FXML
    private void TS(ActionEvent event) {
        sqlConnect.changeTicketPrice("TS",Double.parseDouble(TS.getText()));
        getPrice();
    }

    @FXML
    private void TC(ActionEvent event) {
        sqlConnect.changeTicketPrice("TC",Double.parseDouble(TC.getText()));
        getPrice();
    }

    @FXML
    private void TE(ActionEvent event) {
        sqlConnect.changeTicketPrice("TE",Double.parseDouble(TE.getText()));
        getPrice();
    }

    @FXML
    private void TO(ActionEvent event) {
        sqlConnect.changeTicketPrice("TO",Double.parseDouble(TO.getText()));
        getPrice();
    }

    @FXML
    private void TP(ActionEvent event) {
        sqlConnect.changeTicketPrice("TP",Double.parseDouble(TP.getText()));
        getPrice();
    }





    
}
