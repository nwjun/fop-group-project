package com.fop.foptproject;

import com.fop.Utility.sqlConnect;
import com.fop.foptproject.controller.AdminMovieController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 *
 * @author WeiXin
 */
public class ProductCardAdminMovie{
    private HashMap<String,Object> movieDetails = new HashMap<>();
    private HBox movieCard;
    private boolean deletestatus = false;
    private int currentPage = 0;
    private AdminMovieController ad = new AdminMovieController();
    
    public ProductCardAdminMovie(String movieID,String movieName,double length, String releaseDate, String directorCast, String language, String imgPath, String allShowTime, String synopsis, double rottenTomato, double iMDB, int ageRestrict, double imgW,double imgH,double scale) throws ParseException{
        movieDetails.put("movieID",movieID);
        movieDetails.put("movieName",movieName);
        movieDetails.put("length",length);
        movieDetails.put("releaseDate",releaseDate);
        movieDetails.put("directorCast",directorCast);
        movieDetails.put("language", language);
        movieDetails.put("imgPath",imgPath);
        movieDetails.put("allShowTime",allShowTime);
        movieDetails.put("synopsis",synopsis);
        movieDetails.put("rottenTomato",rottenTomato);
        movieDetails.put("iMDB",iMDB);
        movieDetails.put("ageRestrict",ageRestrict);
        movieDetails.put("imgW",imgW*scale);
        movieDetails.put("imgH",imgH*scale);
        
    this.movieCard = makeCard(movieID,length);
        HBox.setMargin(this.movieCard,new Insets(0,45,0,0));   
    }
        
    public HashMap<String,Object> getProductDetails(){
        return movieDetails;
    }
    
    public Object getValue(String key){
        return this.movieDetails.get(key);
    }
    
    public HBox getCard(){
        
        return this.movieCard;
    }
    
    public static String castJsonProcessor(String directorCast) throws ParseException{
        JSONObject json = new JSONObject(directorCast);
        JSONArray ja = json.getJSONArray("Cast");
        int length = ja.length();
 
        String cast = "";
        if(length>0){
            for(int i =0; i<length; i++)
                cast+=ja.getString(i) +", ";
        }
        return cast;
    }
    
    public String directorJsonProcesor(String directorCast){
        JSONObject json = new JSONObject(directorCast);
        
        String director = (String) json.get("Director");
        
        return director;
    }
    
    public String allShowTimeJsonProcessor(String allShowTime){
        String listShowTime = "";
        String Mon = "Mon: ";
        String Tue = "\nTue: ";
        String Wed = "\nWed: ";
        String Thur = "\nThur: ";
        String Fri = "\nFri: ";
        String Sat = "\nSat: ";
        String Sun = "\nSun: ";
        JSONObject json = new JSONObject(allShowTime);
        for (Integer i=0; i<7; i++){
            JSONArray ja = json.getJSONArray(i.toString());
            int length = ja.length();
            String H01 = "\n\tH01: ";
            String H02 = "\n\tH02: ";
            String H03 = "\n\tH03: ";
            String H04 = "\n\tH04: ";
            String H05 = "\n\tH05: ";
            String H06 = "\n\tH06: ";
            String H07 = "\n\tH07: ";
            for(int j=0; j<length; j++){
                JSONArray jainja = ja.getJSONArray(j);
                String x = jainja.getString(0);
                String y = jainja.getString(1);
                
                switch (x) {
                    case "H01":
                        H01 += y +  " ";
                        break;
                    case "H02":
                        H02 += y + " ";
                        break;
                    case "H03":
                        H03 += y + " ";
                        break;
                    case "H04":
                        H04 += y + " ";
                        break;
                    case "H05":
                        H05 += y + " ";
                        break;
                    case "H06":
                        H06 += y + " ";
                        break;
                    case "H07":
                        H07 += y + " ";
                        break;
                    default:
                        break;
                }              
            }
            switch (i) {
                    case 0:
                        Mon = Mon + H01 + H02 + H03 + H04 + H05 + H06 + H07;
                        break;
                    case 1:
                        Tue = Tue + H01 + H02 + H03 + H04 + H05 + H06 + H07;
                        break;
                    case 2:
                        Wed = Wed + H01 + H02 + H03 + H04 + H05 + H06 + H07;
                        break;
                    case 3:
                        Thur = Thur + H01 + H02 + H03 + H04 + H05 + H06 + H07;
                        break;
                    case 4:
                        Fri = Fri + H01 + H02 + H03 + H04 + H05 + H06 + H07;
                        break;
                    case 5:
                        Sat = Sat + H01 + H02 + H03 + H04 + H05 + H06 + H07;
                        break;
                    case 6:
                        Sun = Sun + H01 + H02 + H03 + H04 + H05 + H06 + H07;
                        break;
                    default:
                        break;
                }
        }

        listShowTime = listShowTime + Mon + "\n"+ Tue + "\n"+ Wed + "\n"+ Thur + "\n"+ Fri + "\n"+ Sat + "\n"+ Sun + "\n";
        
        return listShowTime;
    }
    
    public Label makeDateCard(String date){
        Label Date = new Label(date);
        return Date;
    }
   
    private HBox makeCard(String movieId, double length) throws ParseException{
        HBox card = new HBox();
        
        VBox img = movieImage(movieId);
        img.setPrefHeight(250);
        img.setPrefHeight(250);
        
        VBox movieDetails = makeMovieDescription(movieId, length);
        movieDetails.setPrefHeight(260);
        movieDetails.setPrefWidth(450);
        movieDetails.setStyle("-fx-padding: 5 0 0 50");
        
        card.getChildren().addAll(img,movieDetails);
        return card;
    }
    
    public String pathway(){
        String path1 = "";
        path1 = getClass().getResource((String)getValue("\\assets\\movies\\anita.jpg\\")).toString();
        return path1;
    }
    
    private Image getImage(){
        String path = getClass().getResource((String)getValue("imgPath")).toString();
            Image img = new Image(path,(double)getValue("imgW"),(double)getValue("imgH"),false,false);
        return img;
    }

   
    private VBox makeMovieDescription(String movieId, double length) throws ParseException{
        
        Label movieName = new Label();
        Label movielength = new Label();
        Label releaseDate = new Label();
        Label director = new Label();
        Label cast = new Label();
        Label language = new Label();
        Label path = new Label();
        Label listShowTime = new Label();
        Label synopsis = new Label();
        Label rottenTomato = new Label();
        Label iMDB = new Label();
        Label ageRestrict = new Label();
        VBox labelContainer = new VBox();
        ScrollPane x = new ScrollPane();
        VBox cover = new VBox();
        
        int minute = (int)((length-(int)length)*60);
        
        movieName.setId(movieId+"_name");
        movieName.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        movieName.setText("Name: "+(String)this.movieDetails.get("movieName"));
        
        movielength.setId(movieId+"_movielength");
        movielength.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px;-fx-alignment:center");
        movielength.setText("Movie Length: "+String.format("%dh ",(int)length)+ String.format("%dm", minute));
        
        releaseDate.setId(movieId+"_releaseDate");
        releaseDate.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        releaseDate.setText("Release Date: "+ (String)this.movieDetails.get("releaseDate"));
        
        String s = castJsonProcessor((String)this.movieDetails.get("directorCast"));

        for (int i = 0; i<s.length(); i+=10){
            s = insertString(s, "\n\t", s.lastIndexOf(", ", i));
        }
        
        cast.setId(movieId+"_cast");
        cast.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        cast.setText("Cast: "+ s);
        
        director.setId(movieId+"_director");
        director.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        director.setText("Director: "+ directorJsonProcesor((String)this.movieDetails.get("directorCast")));
        
        language.setId(movieId+"_language");
        language.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        language.setText("Language: "+(String)this.movieDetails.get("language"));
        
        path.setId(movieId+"_path");
        path.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        path.setText("Path: "+ (String)this.movieDetails.get("imgPath"));
        
        listShowTime.setId(movieId+"_listShowTime");
        listShowTime.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        listShowTime.setText("All Show Time: \n"+ allShowTimeJsonProcessor((String)this.movieDetails.get("allShowTime")));
        
        String a = (String)this.movieDetails.get("synopsis");
        for (int i = 47; i<a.length();i+=57){
            a = insertString(a, "\n", a.lastIndexOf(" ", i-3));
        }
        
        synopsis.setId(movieId+"_synopsis");
        synopsis.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        synopsis.setText("Synopsis: "+ a);           
        
        rottenTomato.setId(movieId+"_rottenTomato");
        rottenTomato.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px;-fx-alignment:center");
        rottenTomato.setText("rottenTomato: "+String.format("%.1f/10 ",this.movieDetails.get("rottenTomato")));
        
        iMDB.setId(movieId+"_iMDB");
        iMDB.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px;-fx-alignment:center");
        iMDB.setText("iMDB: "+String.format("%.1f/10 ",this.movieDetails.get("iMDB")));
        
        ageRestrict.setId(movieId+"_ageRestrict");
        ageRestrict.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px;-fx-alignment:center");
        ageRestrict.setText("Age Restrict: "+String.format("%d ", this.movieDetails.get("ageRestrict")));
               
        AnchorPane editdelete = MakeButton(movieId, length);
//        Label x = makeDateLabel((String)this.movieDetails.get("allShowTime"), this.currentPage);
        
        labelContainer.setPrefWidth(600);
        labelContainer.setPrefHeight(2500);
        labelContainer.setStyle("-fx-padding:0 0 0 0");
        
//        if(this.currentPage==0){
            labelContainer.getChildren().clear();
            labelContainer.getChildren().addAll(path, movieName, movielength, releaseDate, language, rottenTomato, iMDB, ageRestrict, director, cast, synopsis, listShowTime);
//        }
//        else{
//            labelContainer.getChildren().clear();
//            labelContainer.getChildren().addAll(x);
//        }
        x.setContent(labelContainer);
        x.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        x.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        x.setStyle("-fx-padding: 0 0 0 0; -fx-background-color:transparent;");
        cover.getChildren().addAll(x, editdelete);
            

        return cover;
    }
    
    public static String insertString(
        String originalString,
        String stringToBeInserted,
        int index)
    {
  
        // Create a new string
        String newString = new String();
  
        for (int i = 0; i < originalString.length(); i++) {
  
            // Insert the original string character
            // into the new string
            newString += originalString.charAt(i);
  
            if (i == index) {
  
                // Insert the string to be inserted
                // into the new string
                newString += stringToBeInserted;
            }
        }
  
        // return the modified String
        return newString;
    }    
    
    private boolean DeletePopUp(){ 
        Stage window = new Stage();
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Delete \""+ this.movieDetails.get("movieName")+ "\"");
        window.setMinWidth(340);
        window.setMinHeight(210);
        window.setY(350);
        window.setX(570);
        
        
        Label label = new Label();
        label.setText("Confirm Deletion of \"" + this.movieDetails.get("movieName")+ "\"");
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
        
        String path = getClass().getResource("assets\\company\\Admin.png\\").toString();
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
    
    private AnchorPane MakeButton(String movieId, double length){
        Button edit = new Button();
        Button delete = new Button();

        edit.setText("Edit");
        edit.setLayoutX(45);
        edit.setLayoutY(20);
        edit.setPrefWidth(100);
        edit.setPrefHeight(31);    
        edit.setStyle("-fx-border-radius:20px;-fx-border-color:#FFEE00;-fx-border-width:1px;-fx-background-color:tranparent;-fx-text-fill:#FFEE00");
        
        edit.setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t){
                edit.setStyle("-fx-opacity: 0.6;-fx-background-radius:15px;-fx-border-radius:20px;-fx-border-color:#FFEE00;-fx-border-width:1px;-fx-background-color:#FFEE00;-fx-text-fill:#000000");
                
            }
        });
        edit.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t){
                edit.setStyle("-fx-border-radius:20px;-fx-border-color:#FFEE00;-fx-border-width:1px;-fx-background-color:tranparent;-fx-text-fill:#FFEE00");
        
            }
        });
        
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
            this.deletestatus = DeletePopUp();
            if(this.deletestatus) 
                System.out.println("Link to MySQL to delete");
        }); 

        AnchorPane editdelete = new AnchorPane();
        editdelete.setStyle("-fx-padding: 0 0 50 0");
        editdelete.setPrefWidth(401);
        editdelete.setPrefHeight(0);
        editdelete.getChildren().addAll(edit, delete);
        
        return editdelete;       
    }
    
    private VBox movieImage(String movieId){
        Image movieImg = getImage();
        ImageView img = new ImageView(movieImg);
        img.setId(movieId+"_img");
        img.setFitWidth((double)this.movieDetails.get("imgW"));
        img.setFitHeight((double)this.movieDetails.get("imgH"));
        img.setPreserveRatio(true);
        img.setSmooth(true);
        
        
        VBox container = new VBox();
        container.setStyle("-fx-padding:18.5 0 0 13.5");
        container.getChildren().addAll(img);
        
        return container;
    }
}
