package com.fop.foptproject.controller;

import com.fop.foptproject.ProductCardAdminMovie;
import com.fop.Utility.sqlConnect;
import com.fop.foptproject.App;
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
import static com.fop.foptproject.ProductCardAdminMovie.castJsonProcessor;
import static com.fop.foptproject.ProductCardAdminMovie.directorJsonProcesor;
import static com.fop.foptproject.controller.SceneController.showPopUpStage;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.StageStyle;
import org.controlsfx.control.CheckComboBox;

/**
 * 
 * @author kuckn
 */
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
    private Object[] theaterId;
    private Object[] slot;
    private Object[] time;
    private double IMGW = 250 ;
    private double IMGH = 375;
    private double SCALE = 0.9;
    private int currentIndex = 0;
    private int currentPage = 0;
    private int maxPage;
    
    private String editmovieId, deletemovieId;
    private boolean deletestatus = false; 
    private boolean updatestatus = false;
    
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
    private TextField iMDBT;
    @FXML
    private TextField rottenTomatoT;
    @FXML
    private Button upload;
    @FXML
    private Button ticketPrice;
    @FXML
    private Button adminAdd;
    @FXML
    private ImageView logo;
    @FXML
    private CheckComboBox<String> checkCombo;
    @FXML
    private ComboBox<String> combobox;
    @FXML
    private Button editSeat;
    @FXML
    private Button clearButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(RealTimeStorage.getPermission().equals("3")){
            adminAdd.setDisable(false);
            adminAdd.setVisible(true);
            String path = App.class.getResource("assets/company/master.png").toString(); 
            Image img = new Image(path/*, IMGW, IMGH, false, false*/);
            logo.setImage(img);
        }
        
        try {
            setValue();
            getProduct();
        } catch (ParseException ex) {
            Logger.getLogger(AdminMovieController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void setValue(){
        
        combobox.getItems().addAll("H01","H02","H03","H04","H05","H06","H07","H08");
        checkCombo.getItems().addAll("10.00AM","03.30PM","08.00PM","01.00PM","11.00PM");
        
        combobox.getSelectionModel().select(combobox.getValue());
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
        this.theaterId = items.get("theaterId").toArray();
        this.slot = items.get("slot").toArray();
        this.time = items.get("time").toArray();
       
        
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
                    this.content = new ProductCardAdminMovie((String)movieId[currentIndex], (String)movieName[currentIndex], Double.parseDouble((String)length[currentIndex]), (String)releaseDate[currentIndex], (String)directorCast[currentIndex], (String)language[currentIndex], (String)posterPath[currentIndex], (String)allShowTime[currentIndex],(String)synopsis[currentIndex], Double.parseDouble((String)rottenTomato[currentIndex]),Double.parseDouble((String)iMDB[currentIndex]), Integer.parseInt((String)ageRestrict[currentIndex]),IMGW,IMGH,SCALE, (String)theaterId[currentIndex], (String)time[currentIndex]);
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
        window.setMinHeight(225);
        window.setY(300);
        window.setX(570);
        String path1 = App.class.getResource("assets/company/logo2.png").toString(); 
        Image img1 = new Image(path1/*, IMGW, IMGH, false, false*/);
        window.getIcons().add(img1);
        
        
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
        
        deleteButton.setOnMouseEntered((MouseEvent t) -> {
            deleteButton.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px;-fx-opacity : 0.6");
        });
        deleteButton.setOnMouseExited((MouseEvent t) -> {
            deleteButton.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
        });         
        
        Button cancel = new Button("Cancel");
        cancel.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
        cancel.setOnAction(e -> window.close());
        
        cancel.setOnMouseEntered((MouseEvent t) -> {
            cancel.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px;-fx-opacity : 0.6");
        });
        cancel.setOnMouseExited((MouseEvent t) -> {
            cancel.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
        }); 
        String path;
        if(RealTimeStorage.getPermission().equals("3")){
            path = App.class.getResource("assets/company/master.png").toString();    
        }
        else{
        path = getPathway()+"company\\Admin.png\\";
        }
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
        
        edit.setOnMouseEntered((MouseEvent t) -> {
            edit.setStyle("-fx-opacity: 0.6;-fx-background-radius:15px;-fx-border-radius:20px;-fx-border-color:#FFEE00;-fx-border-width:1px;-fx-background-color:#FFEE00;-fx-text-fill:#000000");
        });
        edit.setOnMouseExited((MouseEvent t) -> {
            edit.setStyle("-fx-border-radius:20px;-fx-border-color:#FFEE00;-fx-border-width:1px;-fx-background-color:rgba(0,0,0,0);-fx-text-fill:#FFEE00");
        });
        edit.setOnAction(e->{ 
            this.editmovieId=edit.getId(); 
            int index=0;
            editSeat.setDisable(true);
            for (int i =0; i<this.movieId.length;i++){
                if(((String)this.movieId[i]).equals(this.editmovieId)){
                    index = i;
                    break;
                }
                
            }
            
            
            String path = App.class.getResource((String)this.posterPath[index]).toString(); 
            Image img = new Image(path, IMGW, IMGH, false, false);
            clean();
            DropImage.setImage(img);
            posterT.setText((String)this.posterPath[index]);
            movieNameT.setText((String)this.movieName[index]);
            lengthT.setText((String)this.length[index]);
            releaseDateT.setText((String)this.releaseDate[index]);
            languageT.setText((String)this.language[index]);
            synopsisT.setText((String)this.synopsis[index]);
            rottenTomatoT.setText((String)this.rottenTomato[index]);
            iMDBT.setText((String)this.iMDB[index]);
            directorT.setText(directorJsonProcesor((String)this.directorCast[index]));
            combobox.setValue("H0"+(String)this.theaterId[index]);
            
            boxchecking(index);

            try {     
                castT.setText(castJsonProcessor((String)this.directorCast[index]));
            } catch (ParseException ex) {
                System.out.println("Parse Error");
            }
            this.updatestatus = true;
            
        });
        
        delete.setId(movieId);
        delete.setText("Delete");
        delete.setLayoutX(193);
        delete.setLayoutY(20);
        delete.setPrefWidth(100);
        delete.setPrefHeight(31);        
        delete.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
        
        delete.setOnMouseEntered((MouseEvent t) -> {
            delete.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px;-fx-opacity : 0.6");
        });
        delete.setOnMouseExited((MouseEvent t) -> {
            delete.setStyle("-fx-background-color:#FFEE00;-fx-background-insets:0;-fx-background-radius:15px");
        });  

        delete.setOnAction(e->{
            this.deletestatus = DeletePopUp(x);
            this.deletemovieId = "M"+delete.getId();
            if(this.deletestatus){
                try {
                    delete();
//                sql.deleteMovie(movieId);
                } catch (ParseException ex) {
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
    
    public void boxchecking(int index){
        String y = (String)this.time[index];
            if(y.contains("10.00AM"))
                checkCombo.getCheckModel().check(0);
            if(y.contains("03.30PM"))
                checkCombo.getCheckModel().check(1);
            if(y.contains("08.00PM"))
                checkCombo.getCheckModel().check(2);
            if(y.contains("01.00PM"))
                checkCombo.getCheckModel().check(3);
            if(y.contains("11.00PM"))
                checkCombo.getCheckModel().check(4);
    }
    
    public void delete() throws ParseException{
        String s = getdeletemovieId();
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
    
    String pathpath = "";
    String ext = "";
    @FXML
    private void singleImagePathRead(ActionEvent event) throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("Image Files", lsFile));
        File f = fc.showOpenDialog(null);
        
        //Getting Path of the Image and the Saving Path
        if (f!= null){
            this.pathpath = f.getAbsolutePath();
            Image img = new Image(new FileInputStream(this.pathpath));
            DropImage.setImage(img);      
            this.poster = this.pathpath.substring(this.pathpath.lastIndexOf("\\")+1);
            this.ext = this.pathpath.substring(this.pathpath.lastIndexOf(".")+1);
            this.save = "assets\\movies\\" + this.poster;
            this.desktopURL = getPathway()+"movies\\";
            this.desktopPath = this.desktopURL+ this.poster;
            
            posterT.setText(this.save);
        }
        
        //Move to Upload Button OnAction
        
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
        movieNameT.setText(null);
        lengthT.clear();
        releaseDateT.clear();
        directorT.clear();
        castT.clear();     
        languageT.clear();
        synopsisT.clear();
        rottenTomatoT.clear();
        iMDBT.clear();
        combobox.setValue(null);
        checkCombo.getCheckModel().clearChecks(); 
    }
    
    public void refresh() throws ParseException{
        movieList.getChildren().clear();
        getProduct();
        currentPage =0;
        checkPage();
    }
    
    @FXML
    private void uploadMovie(ActionEvent event) throws ParseException {
        try{
        String m = combobox.getValue();
        m = m.substring(m.length()-1);
        Integer m1 = Integer.parseInt(m);
        String b = movieNameT.getText();
        
        if(sql.theaterIDCheck(m1,b)){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Theater Hall Occupied");
            a.setContentText("Theater Hall H0"+m1+" Occupied. \nPlease Choose Another Hall.");
            Stage stage = (Stage) a.getDialogPane().getScene().getWindow(); // get the window of alert box and cast to stage to add icons
            stage.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
            stage.showAndWait();
            return;
        }
        
        String a;
        String Id;
        if (this.updatestatus){
            Id = this.editmovieId;
            sql.delete("M"+Id);
            a = posterT.getText();
            sql.insertPoster("M"+Id, a);
        }
        else{
            Id = lastmovieId();
            a = this.save;
            sql.insertPoster("M"+Id, a);
            }
        
        
        String c = lengthT.getText();
        String d = releaseDateT.getText(); 
        String e = directorT.getText();
        String f = castT.getText();
        String directorcast = JSONdc(e, f);
        String g = languageT.getText();
        String j = synopsisT.getText();
        String k = rottenTomatoT.getText();
        String l = iMDBT.getText();

        String n = "";
        ObservableList list = checkCombo.getCheckModel().getCheckedItems();
        for(Object obj : list){
            n += obj.toString() + ", ";
        }
        n = n.substring(0, n.length()-2);
        
        sql.insertMovie(Id, b, Double.parseDouble(c), d, directorcast, g, "M"+Id, j, Double.parseDouble(k), Double.parseDouble(l), m1, n);
        }catch(Exception ex){
            Alert ax = new Alert(Alert.AlertType.ERROR);
            ax.setTitle("Data Entry Error");
            ax.setContentText("Data Entry Error. \nPlease Check Your Input.");
            Stage stage = (Stage) ax.getDialogPane().getScene().getWindow(); // get the window of alert box and cast to stage to add icons
            stage.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
            stage.showAndWait();
            return;
        }
        
        clean();
        refresh();
        this.updatestatus = false;
        
        // from singleImagePathRead
        try{
            BufferedImage img = ImageIO.read(new File(this.pathpath));
            File outputfile = new File(this.desktopPath);
            ImageIO.write(img, this.ext, outputfile);
            System.out.println("Upload Successful: Poster Changed/Uploaded");
            this.pathpath = "";
            this.ext="";
            editSeat.setDisable(false);
        }catch(IOException ex){
            System.out.println("Upload Successful: Poster Unchanged");
        }
        // 

    }

    @FXML
    private void ticketPrice(ActionEvent event) {
        Stage x = showPopUpStage("TicketPrice.fxml");
        x.initStyle(StageStyle.UNDECORATED);
        x.show();
    }

    @FXML
    private void adminAdd(ActionEvent event) {
        Stage x = showPopUpStage("EditAdmin.fxml");
        x.initStyle(StageStyle.UNDECORATED);
        x.show();
    }

    @FXML
    private void EditSeats(ActionEvent event) throws IOException {
        String m = combobox.getValue();
        String b = movieNameT.getText();
        Alert a = new Alert(Alert.AlertType.ERROR);
        
        if(m == null||b == null){
            a.setTitle("Empty theatre hall or movie name");
            a.setContentText("Please select a theatre hall or enter your movie name");
            Stage stage = (Stage) a.getDialogPane().getScene().getWindow(); // get the window of alert box and cast to stage to add icons
            stage.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
            stage.showAndWait();
            return;
        }
        else{
            m = m.substring(m.length()-1);
            Integer m1 = Integer.parseInt(m);
            if(sql.theaterIDCheck(m1,b)){
                a.setTitle("Theater Hall Occupied");
                a.setContentText("Theater Hall H0"+m1+" Occupied. \nPlease Choose Another Hall.");
                Stage stage = (Stage) a.getDialogPane().getScene().getWindow(); // get the window of alert box and cast to stage to add icons
                stage.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
                stage.showAndWait();
                return;

            }

        new SceneController().switchToAdminSeats(event);

        }
    }

    @FXML
    private void clearButtonAct(ActionEvent event) {
        editSeat.setDisable(false);
        clean();
    }
}
