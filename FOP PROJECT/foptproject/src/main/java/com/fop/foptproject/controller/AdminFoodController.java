package com.fop.foptproject.controller;

import com.fop.foptproject.ProductCardAdminFood;
import com.fop.Utility.sqlConnect;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

public class AdminFoodController implements Initializable {
    
    List<String> lsFile = Arrays.asList("*.jpg", "*.png", "*.jpeg");
    String poster;
    String save;
    String desktopURL;
    String desktopPath;
    private sqlConnect sql = new sqlConnect();
    private Object[] productId;
    private Object[] price;
    private Object[] posterPath;
    private Object[] productDesc;
    private Object[] productName;
    private Object[] category;
    private double IMGW = 250 ;
    private double IMGH = 250;
    private double SCALE = 0.9;
    private int currentIndex = 0;
    private int currentPage = 0;
    private int maxPage;
    
    @FXML
    private ImageView DropImage;
    @FXML
    private Button backToMain;
    @FXML 
    private TextField Poster;
    @FXML
    private Button FileChooser;
    @FXML
    private GridPane productList;
    @FXML
    private Button nextPageButton;
    @FXML
    private Button prevPageButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getProduct();
    }    
    
    public String getPathway (){
        URL URL = AdminFoodController.class.getResource("/com/fop/foptproject/assets/foods/marker.txt");
        File file = null;
        try {
            file = Paths.get(URL.toURI()).toFile();
        } catch (URISyntaxException ex) {
            Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, null, ex);
        }
        String absolutePath = file.getAbsolutePath();   
        
        //the output of absolutePath is "C:\\Users\\kuckn\\Documents\\GitHub\\fop-group-project\\FOP PROJECT\\foptproject\\target\\classes\\com\\fop\\foptproject\\assets\\movies\\poster7.jpg"
        String path = absolutePath.substring(0, absolutePath.indexOf("target"))+ "src\\main\\resources\\com\\fop\\foptproject\\assets\\foods\\";
        System.out.println(absolutePath);
        
        return path;
    }
    
    public void getProduct(){
        HashMap<String,ArrayList<String>> items = sql.queryAllProduct();
        this.productId = items.get("productId").toArray();
        this.price = items.get("price").toArray();
        this.posterPath = items.get("posterPath").toArray();
        this.productDesc = items.get("productDesc").toArray();
        this.productName = items.get("productName").toArray();
        this.category = items.get("category").toArray();
        this.currentPage = 0;
        this.maxPage = (int) Math.ceil(productId.length/6.0);
        this.currentIndex = 0;
                  
        loadCard();   
    }
    
    public void loadCard(){
        int n = productId.length;
        stop:{
            for(int i = 0; i < 6;i++){
                for(int j = 0; j < 1 ; j++){
                    ProductCardAdminFood content = new ProductCardAdminFood((String)productId[currentIndex],(String)posterPath[currentIndex],IMGW,IMGH,SCALE,Double.parseDouble((String)price[currentIndex]),(String)productName[currentIndex],(String)productDesc[currentIndex], (String)category[currentIndex]);
                    HBox card = content.getCard();
                    productList.add(card,j,i);
                    currentIndex++;
                    if (currentIndex>=n){
                        break stop;
                    }   
           
                }
            }
        }
    }
    
    public void checkPage(){
        if(currentPage == maxPage-1){
            nextPageButton.setDisable(true);
        }
        else{
            nextPageButton.setDisable(false);
        }
        if(currentPage == 0){
            prevPageButton.setDisable(true);
        }
        else{
            prevPageButton.setDisable(false);
        }
    }
    
    @FXML
    private void singleImagePathRead(ActionEvent event) throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", lsFile));
        File f = fc.showOpenDialog(null);
        String path = "";
        String ext = "";
                
        if (f!= null){
            path = f.getAbsolutePath();
            Poster.setText(path);
            Image img = new Image(new FileInputStream(path));
            DropImage.setImage(img);      
            this.poster = path.substring(path.lastIndexOf("\\")+1);
            ext = path.substring(path.lastIndexOf(".")+1);
            this.save = "assets\\foods\\" + this.poster;
            
            this.desktopURL = getPathway();
            this.desktopPath = this.desktopURL + this.poster;
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
    public void prevPage(){
        if (currentPage == 0)return;
        productList.getChildren().clear();
        currentIndex = (currentPage-1)*4;
        loadCard();
        currentPage--;
        checkPage();
    }      
        
    @FXML
    public void nextPage(){
        if(currentIndex==productId.length)return;
        productList.getChildren().clear();
        loadCard();
        currentPage++;
        checkPage();
    }
    
}
