package com.fop.foptproject.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.imageio.ImageIO;

public class AdminMovieController implements Initializable {
    
    List<String> lsFile = Arrays.asList("*.jpg", "*.png", "*.jpeg");
    String poster;
    String save;
    String desktopPath;

    @FXML
    private ImageView DropImage;
    @FXML
    private Button backToMain;
    @FXML
    private TextField Poster;
    @FXML
    private Button FileChooser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    } 
    
    @FXML
    private void singleImagePathRead(ActionEvent event) throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("Image Files", lsFile));
        File f = fc.showOpenDialog(null);
        String path = "";
        String ext = "";
        
        //Getting Path of the Image and the Saving Path
        if (f!= null){
            path = f.getAbsolutePath();
            Poster.setText(path);
            Image img = new Image(new FileInputStream(path));
            DropImage.setImage(img);      
            this.poster = path.substring(path.lastIndexOf("\\")+1);
            ext = path.substring(path.lastIndexOf(".")+1);
            this.save = "assets\\movies\\" + this.poster;
            
            // Tolong change to your Path
            this.desktopPath = "C:\\Users\\kuckn\\Documents\\GitHub\\fop-group-project\\FOP PROJECT\\foptproject\\src\\main\\resources\\com\\fop\\foptproject\\assets\\movies\\" + this.poster;
        }
        
        //Move to Upload Button OnAction
        BufferedImage img = null;
        try{
            img = ImageIO.read(new File(path));
        }catch (IOException e){
        }
        try{
        File outputfile = new File(this.desktopPath);
        ImageIO.write(img, ext, outputfile);
        }catch(IOException e){
            System.out.println(e);
        }
    }
    
    @FXML
    private void handleDragOver(DragEvent event) {
        if(event.getDragboard().hasFiles())
        event.acceptTransferModes(TransferMode.ANY);
    }

    @FXML
    private void handleDrop(DragEvent event) throws FileNotFoundException {
        List<File> files = event.getDragboard().getFiles();
        Image img = new Image(new FileInputStream(files.get(0)));        
        DropImage.setImage(img);   
    }

    @FXML
    private void switchToAdminMain(ActionEvent event) throws IOException {
        SceneController SwitchScene = new SceneController();
        SwitchScene.switchToAdminMain(event);
    }
    
    @FXML
    private void PreText (ActionEvent event){
        
    }
}
