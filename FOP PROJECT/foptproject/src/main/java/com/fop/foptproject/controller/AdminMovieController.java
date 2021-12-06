package com.fop.foptproject.controller;

import com.fop.foptproject.ProductCardAdminMovie;
import static com.fop.foptproject.ProductCardAdminMovie.castJsonProcessor;
import com.fop.sqlUtil.sqlConnect;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.imageio.ImageIO;
import org.json.simple.parser.ParseException;

public class AdminMovieController implements Initializable {
    
    List<String> lsFile = Arrays.asList("*.jpg", "*.png", "*.jpeg");
    String poster;
    String save;
    String desktopURL;
    String desktopPath;
    private sqlConnect sql=new sqlConnect();;
    private Object[] movieId;
    private Object[] movieName;
    private Object[] length;
    private Object[] releaseDate;
    private Object[] directorCast;
    private Object[] language;
    private Object[] posterPath;
    private Object[] allShowTime;
    private Object[] synopsis;
    private Object[] rottenTomato;
    private Object[] iMDB;
    private Object[] ageRestrict;
    private double IMGW = 250 ;
    private double IMGH = 375;
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
    private GridPane movieList;
    @FXML
    private Button nextPageButton;
    @FXML
    private Button prevPageButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            getProduct();
        } catch (ParseException ex) {
            Logger.getLogger(AdminMovieController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public String getPathway (){
        URL URL = AdminFoodController.class.getResource("/com/fop/foptproject/assets/movies/marker.txt");
        File file = null;
        try {
            file = Paths.get(URL.toURI()).toFile();
        } catch (URISyntaxException ex) {
            Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, null, ex);
        }
        String absolutePath = file.getAbsolutePath();    
        //the output of absolutePath is "C:\\Users\\kuckn\\Documents\\GitHub\\fop-group-project\\FOP PROJECT\\foptproject\\target\\classes\\com\\fop\\foptproject\\assets\\movies\\poster7.jpg"
        String path = absolutePath.substring(0, absolutePath.indexOf("target"))+ "src\\main\\resources\\com\\fop\\foptproject\\assets\\movies\\";
        
        return path;
    }
    
    public void getProduct() throws ParseException{
        HashMap<String,ArrayList<String>> items = sql.queryAllMovie();
        this.movieId = items.get("movieId").toArray();
        this.movieName = items.get("movieName").toArray();
        this.length = items.get("length").toArray();
        this.releaseDate = items.get("releaseDate").toArray();
        this.directorCast = items.get("directorCast").toArray();
        this.language = items.get("language").toArray();
        this.posterPath = items.get("poster").toArray();
        this.allShowTime = items.get("allShowTime").toArray();
        this.synopsis = items.get("synopsis").toArray();
        this.rottenTomato = items.get("rottenTomato").toArray();
        this.iMDB = items.get("iMDB").toArray();
        this.ageRestrict = items.get("ageRestrict").toArray();
        
        this.currentPage = 0;
        this.maxPage = (int) Math.ceil(movieId.length/4.0);
        this.currentIndex = 0;
                  
        loadCard();   
    }
    
    public void loadCard() throws ParseException{
        int n = movieId.length;
        stop:{
            for(int i = 0; i < 4;i++){
                for(int j = 0; j < 1 ; j++){
                    ProductCardAdminMovie content = new ProductCardAdminMovie((String)movieId[currentIndex], (String)movieName[currentIndex], Double.parseDouble((String)length[currentIndex]), (String)releaseDate[currentIndex], (String)directorCast[currentIndex], (String)language[currentIndex], (String)posterPath[currentIndex], (String)allShowTime[currentIndex],(String)synopsis[currentIndex], Double.parseDouble((String)rottenTomato[currentIndex]),Double.parseDouble((String)iMDB[currentIndex]), Integer.parseInt((String)ageRestrict[currentIndex]),IMGW,IMGH,SCALE);
                    HBox card = content.getCard();
                    movieList.add(card,j,i);
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
    public void prevPage() throws ParseException{
        if (currentPage == 0)return;
        movieList.getChildren().clear();
        currentIndex = (currentPage-1)*4;
        loadCard();
        currentPage--;
        checkPage();
    }      
        
    @FXML
    public void nextPage() throws ParseException{
        if(currentIndex==movieId.length)return;
        movieList.getChildren().clear();
        loadCard();
        currentPage++;
        checkPage();
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
            Image img = new Image(new FileInputStream(path));
            DropImage.setImage(img);      
            this.poster = path.substring(path.lastIndexOf("\\")+1);
            ext = path.substring(path.lastIndexOf(".")+1);
            this.save = "assets\\movies\\" + this.poster;
            this.desktopURL = getPathway();
            this.desktopPath = this.desktopURL+ this.poster;
            
            Poster.setText(path + " -> " + this.save);
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
