package com.fop.foptproject;

import java.util.HashMap;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

/**
 *
 * @author kuckn
 */
public class ProductCardAdminMovie {

    private HashMap<String, Object> movieDetails = new HashMap<>();
    private VBox imgCard;
    private ScrollPane detailCard;
    private int currentPage = 0;
//    private AdminMovieController ad = new AdminMovieController();

    public ProductCardAdminMovie(String movieID, String movieName, double length, String releaseDate, String directorCast, String language, String imgPath, String synopsis, double rottenTomato, double iMDB, int ageRestrict, double imgW, double imgH, double scale, String theaterId, String time) throws ParseException {
        movieDetails.put("movieID", movieID);
        movieDetails.put("movieName", movieName);
        movieDetails.put("length", length);
        movieDetails.put("releaseDate", releaseDate);
        movieDetails.put("directorCast", directorCast);
        movieDetails.put("language", language);
        movieDetails.put("imgPath", imgPath);
        movieDetails.put("synopsis", synopsis);
        movieDetails.put("rottenTomato", rottenTomato);
        movieDetails.put("iMDB", iMDB);
        movieDetails.put("ageRestrict", ageRestrict);
        movieDetails.put("imgW", imgW * scale);
        movieDetails.put("imgH", imgH * scale);
        movieDetails.put("theaterId", theaterId);
        movieDetails.put("time", time);

        makeCard(movieID, length);
    }

    public VBox getImgCard() {
        return this.imgCard;
    }

    public ScrollPane getDetailCard() {
        return this.detailCard;
    }

    private void makeCard(String movieId, double length) throws ParseException {

        this.imgCard = movieImage(movieId);
        this.imgCard.setPrefHeight(250);
        this.imgCard.setPrefHeight(250);

        this.detailCard = makeMovieDescription(movieId, length);
        this.detailCard.setPrefHeight(260);
        this.detailCard.setPrefWidth(460);


//        card.getChildren().addAll(img,movieDetails);
//        return card;
    }

    private VBox movieImage(String movieId) {
        Image movieImg = getImage();
        ImageView img = new ImageView(movieImg);
        img.setId(movieId + "_img");
        img.setFitWidth((double) this.movieDetails.get("imgW"));
        img.setFitHeight((double) this.movieDetails.get("imgH"));
        img.setPreserveRatio(true);
        img.setSmooth(true);

        VBox container = new VBox();
        container.setStyle("-fx-padding:8 0 0 13.5");
        container.getChildren().addAll(img);

        return container;
    }

    private ScrollPane makeMovieDescription(String movieId, double length) throws ParseException {
        Label movieName = new Label();
        Label movielength = new Label();
        Label releaseDate = new Label();
        Label director = new Label();
        Label cast = new Label();
        Label language = new Label();
        Label path = new Label();
        Label synopsis = new Label();
        Label rottenTomato = new Label();
        Label iMDB = new Label();
        Label ageRestrict = new Label();
        VBox labelContainer = new VBox();
        ScrollPane x = new ScrollPane();

        Label hall = new Label();
        Label time = new Label();

        Label[] labels = {movieName,movielength, releaseDate,director, language, path, synopsis, rottenTomato, iMDB, ageRestrict};
        
        
        hall.setId(movieId + "_hall");
        hall.setStyle("-fx-padding:0px 0 5px 18px");
        hall.setText("Hall: H0" + (String) this.movieDetails.get("theaterId"));

        String t = "\n\tH0" + (String) this.movieDetails.get("theaterId") + ": " + (String) this.movieDetails.get("time");
        t = t.replaceAll(", ", "\n\tH0" + (String) this.movieDetails.get("theaterId") + ": ");
        t = "\nMonday: " + t + "\n\nTuesday: " + t + "\n\nWednesday: " + t + "\n\nThursday: " + t + "\n\nFriday: " + t + "\n\nSaturday: " + t + "\n\nSunday: " + t;

        time.setId(movieId + "_time");
        time.setStyle("-fx-padding:0px 0 5px 18px");
        time.setText("All Show Time: " + t);

        int minute = (int) ((length - (int) length) * 60);

        movieName.setId(movieId + "_name");
        movieName.setStyle("-fx-padding:0px 0 5px 18px");
        movieName.setText("Name: " + (String) this.movieDetails.get("movieName"));

        movielength.setId(movieId + "_movielength");
        movielength.setStyle("-fx-padding:0px 0 5px 18px");
        movielength.setText("Movie Length: " + String.format("%dh ", (int) length) + String.format("%dm", minute));

        releaseDate.setId(movieId + "_releaseDate");
        releaseDate.setStyle("-fx-padding:0px 0 5px 18px");
        releaseDate.setText("Release Date: " + (String) this.movieDetails.get("releaseDate"));

        String s = castJsonProcessor((String) this.movieDetails.get("directorCast"));

        for (int i = 0; i < s.length(); i += 10) {
            s = insertString(s, "\n\t", s.lastIndexOf(", ", i));
        }

        cast.setId(movieId + "_cast");
        cast.setStyle("-fx-padding:0px 0 5px 18px");
        cast.setText("Cast: " + s);

        director.setId(movieId + "_director");
        director.setStyle("-fx-padding:0px 0 5px 18px");
        director.setText("Director: " + directorJsonProcesor((String) this.movieDetails.get("directorCast")));

        language.setId(movieId + "_language");
        language.setStyle("-fx-padding:0px 0 5px 18px");
        language.setText("Language: " + (String) this.movieDetails.get("language"));

        path.setId(movieId + "_path");
        path.setStyle("-fx-padding:0px 0 5px 18px");
        path.setText("Path: " + (String) this.movieDetails.get("imgPath"));

        String a = (String) this.movieDetails.get("synopsis");

        synopsis.setId(movieId + "_synopsis");
        synopsis.setStyle("-fx-padding:0px 0 5px 18px");
        synopsis.setText("Synopsis: " + a);
        synopsis.setWrapText(true);
        
        rottenTomato.setId(movieId + "_rottenTomato");
        rottenTomato.setStyle("-fx-padding:0px 0 5px 18px");
        rottenTomato.setText("rottenTomato: " + String.format("%.1f/10 ", this.movieDetails.get("rottenTomato")));

        iMDB.setId(movieId + "_iMDB");
        iMDB.setStyle("-fx-padding:0px 0 5px 18px");
        iMDB.setText("iMDB: " + String.format("%.1f/10 ", this.movieDetails.get("iMDB")));

        ageRestrict.setId(movieId + "_ageRestrict");
        ageRestrict.setStyle("-fx-padding:0px 0 5px 18px");
        ageRestrict.setText("Age Restrict: " + String.format("%d ", this.movieDetails.get("ageRestrict")));

        Label line = new Label("\n ________________________*________________________\n");
        Label line2 = new Label("\n ________________________*________________________\n");

        labelContainer.setPrefWidth(400);
        labelContainer.setStyle("-fx-padding:20px 0 20px 0");
        labelContainer.getChildren().clear();
        labelContainer.getChildren().addAll(movieName, movielength, releaseDate, language, rottenTomato, iMDB, ageRestrict, director, cast, synopsis, /*listShowTime,*/ line, hall, time, line2);
        labelContainer.setPrefHeight(Control.USE_COMPUTED_SIZE);
        
        for (Label label: labels){
            label.setPrefWidth(Control.USE_COMPUTED_SIZE);
        }
        
        x.setContent(labelContainer);
        x.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        x.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        x.setStyle("-fx-padding: 0 0 0 0; -fx-background-color:transparent;");

        return x;
    }

    public Object getValue(String key) {
        return this.movieDetails.get(key);
    }

    public static String castJsonProcessor(String directorCast) throws ParseException {
        JSONObject json = new JSONObject(directorCast);
        JSONArray ja = json.getJSONArray("Cast");
        int length = ja.length();

        String cast = "";
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                cast += ja.getString(i) + ",";
            }
        }
        cast = cast.substring(0, cast.length() - 1);
        return cast;
    }

    public static String directorJsonProcesor(String directorCast) {
        JSONObject json = new JSONObject(directorCast);

        String director = (String) json.get("Director");

        return director;
    }

    public String pathway() {
        String path1 = getClass().getResource((String) getValue("\\assets\\movies\\anita.jpg\\")).toString();
        return path1;
    }

    private Image getImage() {
        String path = App.class.getResource((String) getValue("imgPath")).toString();
        Image img = new Image(path, (double) getValue("imgW"), (double) getValue("imgH"), false, false);
        return img;
    }

    public static String insertString(
            String originalString,
            String stringToBeInserted,
            int index) {

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
