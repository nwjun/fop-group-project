package com.fop.foptproject.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

public class AdminMovieController implements Initializable {

    @FXML
    private ImageView DropImage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleDragOver(DragEvent event) {
        if(event.getDragboard().hasFiles())
        event.acceptTransferModes(TransferMode.ANY);
    }
    
}
