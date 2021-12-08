package com.fop.foptproject;

import com.fop.Utility.sqlConnect;
import com.fop.foptproject.controller.AdminMovieController;
import java.util.HashMap;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

public class ProductCardAdminMovie{
    private HashMap<String,Object> movieDetails = new HashMap<>();
    private HBox movieCard;
    private VBox imgCard;
    private ScrollPane detailCard;
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
        
        /*this.movieCard = */makeCard(movieID,length);
//        HBox.setMargin(this.movieCard,new Insets(0,45,0,0));   
    }
    
    public VBox getImgCard(){
        return this.imgCard;
    }
    
    public ScrollPane getDetailCard(){
        return this.detailCard;
    }
    
    private void makeCard(String movieId, double length) throws ParseException{
//        HBox card = new HBox();
        
        this.imgCard = movieImage(movieId);
        this.imgCard.setPrefHeight(250);
        this.imgCard.setPrefHeight(250);
        
        this.detailCard = makeMovieDescription(movieId, length);
        this.detailCard.setPrefHeight(260);
        this.detailCard.setPrefWidth(460);
        this.detailCard.setStyle("-fx-padding: 0 0 0 0");
        
//        card.getChildren().addAll(img,movieDetails);
//        return card;
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
    
    private ScrollPane makeMovieDescription(String movieId, double length) throws ParseException{
        
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
               
//        AnchorPane editdelete = MakeButton(movieId, length);
//        Label x = makeDateLabel((String)this.movieDetails.get("allShowTime"), this.currentPage);
        
        labelContainer.setPrefWidth(450);
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
//        cover.getChildren().addAll(x/*, editdelete*/);
            

        return x;
    }    

    public Object getValue(String key){
        return this.movieDetails.get(key);
    }
        
//    public HashMap<String,Object> getProductDetails(){
//        return movieDetails;
//    }    
//    public HBox getCard(){
//        
//        return this.movieCard;
//    }
    
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
        String Mon = "\tMonday: ";
        String Tue = "\t\nTuesday: ";
        String Wed = "\t\nWednesday: ";
        String Thur = "\t\nThursday: ";
        String Fri = "\t\nFriday: ";
        String Sat = "\t\nSaturday: ";
        String Sun = "\t\nSunday: ";
        JSONObject json = new JSONObject(allShowTime);
        for (Integer i=0; i<7; i++){
            JSONArray ja = json.getJSONArray(i.toString());
            int length = ja.length();
            String H01 = "\n\t\tH01: ";
            String H02 = "\n\t\tH02: ";
            String H03 = "\n\t\tH03: ";
            String H04 = "\n\t\tH04: ";
            String H05 = "\n\t\tH05: ";
            String H06 = "\n\t\tH06: ";
            String H07 = "\n\t\tH07: ";
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
//    
//    public Label makeDateCard(String date){
//        Label Date = new Label(date);
//        return Date;
//    }

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
}
