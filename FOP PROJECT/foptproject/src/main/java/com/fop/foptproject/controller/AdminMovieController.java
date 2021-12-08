package com.fop.foptproject.controller;

import com.fop.foptproject.ProductCardAdminMovie;
import com.fop.Utility.sqlConnect;
import static com.fop.foptproject.ProductCardAdminMovie.castJsonProcessor;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.json.simple.parser.ParseException;

public class AdminMovieController implements Initializable {
    
    List<String> lsFile = Arrays.asList("*.jpg", "*.png", "*.jpeg");
    String poster;
    String save;
    String desktopURL;
    String desktopPath;

    private ProductCardAdminMovie content;
    private sqlConnect sql=new sqlConnect();

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
    
    private String editmovieId, deletemovieId;
    private boolean deletestatus = false;    
    
    @FXML
    private ImageView DropImage;
    @FXML
    private Button backToMain;
    @FXML
    private Button FileChooser;
    @FXML
    private GridPane movieList;
    @FXML
    private Button nextPageButton;
    @FXML
    private Button prevPageButton;
    @FXML
    private TextField posterT;
    @FXML
    private TextField movieNameT;
    @FXML
    private TextField lengthT;
    @FXML
    private TextField releaseDateT;
    @FXML
    private TextField directorT;
    @FXML
    private TextField castT;
    @FXML
    private TextField languageT;
    @FXML
    private TextArea synopsisT;
    @FXML
    private TextField showTimeT;
    @FXML
    private TextField iMDBT;
    @FXML
    private TextField rottenTomatoT;
    @FXML
    private Button upload;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
//        String s = "M00007";
//        String t = "assets\\movies\\poster7.jpg";
//        this.sql.insertPoster(s, t);
//        
//        String a = "00007";
//        String b = "Encanto";
//        double c = 1.51;
//        String d = "2021-12-17";
//        String e = "{\"Cast\": [\"Vishal\", \"Arya\", \"Mirnalini Ravi\", \"Mamta Mohandas\", \"Prakash Raj\"], \"Director\": \"Deirdre Bowen\"}";
//        String f = "En";
//        String g = "M00007";
//        String h = "{\"0\": [], \"1\": [], \"2\": [], \"3\": [], \"4\": [], \"5\": [], \"6\": []}";
//        String i = "-";
//        double j = 9;
//        double k = 9;
//        int l = 18;
//        this.sql.insertMovie(a, b, c, d, e, f, g, h, i, j, k, l);
//        
//        String u = "S000011";
//        String v = "assets\\foods\\food4.jpg";
//        this.sql.insertPoster(u, v);
//        
//        String m = "S000011";
//        String n = "Special Order";
//        double o = 24;
//        String p = "S000011";
//        String q = "A special surprise meal.";
//        String r = "combo";
//        this.sql.insertProduct(m, n, o, p, q, r);
        
        try {
            getProduct();
        } catch (ParseException ex) {
            Logger.getLogger(AdminMovieController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
                    this.content = new ProductCardAdminMovie((String)movieId[currentIndex], (String)movieName[currentIndex], Double.parseDouble((String)length[currentIndex]), (String)releaseDate[currentIndex], (String)directorCast[currentIndex], (String)language[currentIndex], (String)posterPath[currentIndex], (String)allShowTime[currentIndex],(String)synopsis[currentIndex], Double.parseDouble((String)rottenTomato[currentIndex]),Double.parseDouble((String)iMDB[currentIndex]), Integer.parseInt((String)ageRestrict[currentIndex]),IMGW,IMGH,SCALE);
                    AnchorPane MB = MakeButton((String)movieId[currentIndex],Double.parseDouble((String)length[currentIndex]), currentIndex);
                    
                    VBox img = content.getImgCard();
                    ScrollPane DETAILS = content.getDetailCard();
                    DETAILS.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    DETAILS.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    DETAILS.setStyle("-fx-padding: 0 0 0 0; -fx-background-color:rgba(0,0,0,0);");
                    DETAILS.setPrefHeight(650);
//                    HBox card = content.getCard();

                    VBox details = new VBox();
                    details.setStyle("-fx-padding:0 0 0 50 ");                
                    details.setPrefHeight(650);
                    details.setPrefWidth(500);
                    details.getChildren().addAll(DETAILS, MB);
                    
                    HBox card = new HBox();
                    card.getChildren().addAll(img, details);

                    movieList.add(card,j,i);
                    currentIndex++;
                    if (currentIndex>=n){
                        break stop;
                    }     
                }
            }
            
        }
    }
    
    private boolean DeletePopUp(int x){ 
        Stage window = new Stage();
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Delete \""+ (String)movieName[x]+ "\"");
        window.setMinWidth(340);
        window.setMinHeight(210);
        window.setY(350);
        window.setX(570);
        
        
        Label label = new Label();
        label.setText("Confirm Deletion of \"" + (String)movieName[x]+ "\"");
        label.setStyle("-fx-font-size: 16px");
        label.setPrefHeight(1);
        
        Label instruction = new Label("Type \"DELETE\"");
        
        TextField deleteText = new TextField();
        deleteText.setPromptText("Type \"DELETE\"");
        
        Button deleteButton = new Button();
        deleteButton.setText("Delete");
        deleteButton.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
        deleteButton.setOnAction(e->{
            if(deleteText.getText().equals("DELETE")){
                this.deletestatus = true;
                window.close();
        }
        });
        
        deleteButton.setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t){
                deleteButton.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px;-fx-opacity : 0.6");
            
            }
        });
        deleteButton.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t){
                deleteButton.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
        
            }
        });         
        
        Button cancel = new Button("Cancel");
        cancel.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
        cancel.setOnAction(e -> window.close());
        
        cancel.setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t){
                cancel.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px;-fx-opacity : 0.6");
            
            }
        });
        cancel.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t){
                cancel.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
        
            }
        }); 
        
        String path = getPathway()+"company\\Admin.png\\";
        Image img = new Image(path, 168.75, 24.375, false, false);
        ImageView setImg = new ImageView();
        setImg.setImage(img);
        
        HBox row0 = new HBox();
        Label spacing8 = new Label("     ");
        row0.setStyle("-fx-padding: 20 0 0 0");
        row0.getChildren().addAll(spacing8, setImg);
        
        HBox row1 = new HBox();
        Label spacing2 = new Label("     ");
        Label spacing3 = new Label("     ");
        row1.getChildren().addAll(spacing2, label, spacing3);
        
        HBox row2 = new HBox();
        Label spacing4 = new Label("     ");
        Label spacing5 = new Label("     ");
        row2.getChildren().addAll(spacing4, instruction, spacing5);
        
        HBox row3 = new HBox();
        Label spacing6 = new Label("     ");
        Label spacing7 = new Label("     ");
        Label spacing = new Label("     ");
        Label spacing1 = new Label("     ");
        row3.getChildren().addAll(spacing6, deleteText, spacing7, deleteButton,spacing, cancel, spacing1);
        

        VBox layout = new VBox(10);
        layout.setStyle("-fx-background: #141414;-fx-font-family: \"Montserrat\";-fx-text-fill: #ffffff;");
        layout.getChildren().addAll(row0,row1,row2, row3);
        
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        
        return this.deletestatus;
    }
    
    private AnchorPane MakeButton(String movieId, double length, int x){
        Button edit = new Button();
        Button delete = new Button();
        
        edit.setId(movieId);
        edit.setText("Edit");
        edit.setLayoutX(15);
        edit.setLayoutY(20);
        edit.setPrefWidth(100);
        edit.setPrefHeight(31);    
        edit.setStyle("-fx-border-radius:20px;-fx-border-color:#FFEE00;-fx-border-width:1px;-fx-background-color:rgba(0,0,0,0);-fx-text-fill:#FFEE00");
        
        edit.setOnMouseEntered(new EventHandler<MouseEvent>(){
            
            @Override
            public void handle(MouseEvent t){
                edit.setStyle("-fx-opacity: 0.6;-fx-background-radius:15px;-fx-border-radius:20px;-fx-border-color:#FFEE00;-fx-border-width:1px;-fx-background-color:#FFEE00;-fx-text-fill:#000000");    
            }
        });
        edit.setOnMouseExited(new EventHandler<MouseEvent>(){
            
            @Override
            public void handle(MouseEvent t){
                edit.setStyle("-fx-border-radius:20px;-fx-border-color:#FFEE00;-fx-border-width:1px;-fx-background-color:rgba(0,0,0,0);-fx-text-fill:#FFEE00");
            }
        });
        edit.setOnAction(e->{ 
            this.editmovieId=edit.getId();
        });
        
        delete.setId(movieId);
        delete.setText("Delete");
        delete.setLayoutX(193);
        delete.setLayoutY(20);
        delete.setPrefWidth(100);
        delete.setPrefHeight(31);        
        delete.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
        
        delete.setOnMouseEntered(new EventHandler<MouseEvent>(){
            
            @Override
            public void handle(MouseEvent t){
                delete.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px;-fx-opacity : 0.6");  
            }
        });
        delete.setOnMouseExited(new EventHandler<MouseEvent>(){
            
            @Override
            public void handle(MouseEvent t){
                delete.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
            }
        });  

        delete.setOnAction(e->{
            this.deletestatus = DeletePopUp(x);
            this.deletemovieId = "M"+delete.getId();
            if(this.deletestatus){
                try {
                    delete();
//                sql.deleteMovie(movieId);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                this.deletestatus = false;
            }
        }); 
        
        AnchorPane editdelete = new AnchorPane();
        editdelete.setStyle("-fx-padding: 0 0 45 0");
        editdelete.setPrefWidth(401);
        editdelete.setPrefHeight(0);
        editdelete.getChildren().addAll(edit, delete);
        
        return editdelete;       
    }   
    
    public void delete() throws ParseException{
        String s="";
        s = getdeletemovieId();
        System.out.println("1 row(s) affected in remote database: "+s + " deleted.");
        sql.delete(s);
        movieList.getChildren().clear();
        getProduct();
        currentPage =0;
        checkPage();
    }
    
    public String getdeletemovieId(){
        return this.deletemovieId;
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
        String path = absolutePath.substring(0, absolutePath.indexOf("target"))+ "src\\main\\resources\\com\\fop\\foptproject\\assets\\";
        
        return path;
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
            this.desktopURL = getPathway()+"movies\\";
            this.desktopPath = this.desktopURL+ this.poster;
            
            posterT.setText(path + " -> " + this.save);
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
    
    String Id;
    String a;
    String b;
    String c;
    String d;
    String e;
    String f;
    String g;
    String h;
    String i;
    String j;
    String k;
    String l;
    
    public String lastmovieId (){
        String Id = Integer.toString(Integer.valueOf((String)this.movieId[movieId.length-1])+1);
        if(Id.length()==1)
            Id = "0000" +Id;
        else if(Id.length() == 2)
            Id = "000" + Id;
        return Id;
    }
    
    public String JSONdc (String e, String f){
        String director = ", \"Director\":\""+e+"\"}";
        String cast ="{\"Cast\":[";
        String member;
        int i =f.length();
        while(i!=-1){     
            if(f.lastIndexOf(", ")==-1){
                member = f;
                cast += "\""+member+"\"";
                break;     
            }
            member = f.substring(f.lastIndexOf(", ")+2, i);
            cast += "\""+member+"\",";
            i = f.lastIndexOf(", ", i);
            f = (f.substring(0, f.lastIndexOf(", ", i)));       
        }
        cast += "]";
        String JSON = cast + director;
        return JSON;
    }
    
    public void clean(){
        DropImage.setImage(null);
        posterT.clear();
        movieNameT.clear();
        lengthT.clear();
        releaseDateT.clear();
        directorT.clear();
        castT.clear();     
        languageT.clear();
        showTimeT.clear();
        synopsisT.clear();
        rottenTomatoT.clear();
        iMDBT.clear();
        showTimeT.setText("{\"0\": [], \"1\": [], \"2\": [], \"3\": [], \"4\": [], \"5\": [], \"6\": []}");
    }
    
    public void refresh() throws ParseException{
        movieList.getChildren().clear();
        getProduct();
        currentPage =0;
        checkPage();
    }
    
    @FXML
    private void uploadMovie(ActionEvent event) throws ParseException {
        Id = lastmovieId();
        a = this.save;
        sql.insertPoster("M"+Id, a);
        
        b = movieNameT.getText();
        c = lengthT.getText();
        d = releaseDateT.getText();
        
        e = directorT.getText();
        f = castT.getText();
        String directorcast = JSONdc(e, f);
        
        g = languageT.getText();
        h = posterT.getText();
        i = showTimeT.getText();
        j = synopsisT.getText();
        k = rottenTomatoT.getText();
        l = iMDBT.getText();
        
        sql.insertMovie(Id, b, Double.parseDouble(c), d, directorcast, g, "M"+Id, i, j, Double.parseDouble(k), Double.parseDouble(l));
        
        clean();
        refresh();

    }
}
