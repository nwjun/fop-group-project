package com.fop.foptproject.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author WeiXin
 */
public class CheckOutController implements Initializable {

    @FXML
    public void back(ActionEvent event) throws IOException{
        new SceneController().switchToFnB(event);
    }
    
    @FXML
    public void pay(ActionEvent event){
        System.out.println("Clicked Pay");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
