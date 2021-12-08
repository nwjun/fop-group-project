package com.fop.foptproject;

import com.fop.Utility.sqlConnect;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
/**
 *
 * @author WeiXin
 */
public class ProductCardAdminMovie{
    private HashMap<String,Object> movieDetails = new HashMap<>();
    private HBox movieCard;
    private boolean deletestatus = false;
    
    public ProductCardAdminMovie(String movieID,String imgPath,double imgW,double imgH,double scale,double length,String movieName,String synopsis, String language){
        movieDetails.put("movieID",movieID);
        movieDetails.put("imgPath",imgPath);
        movieDetails.put("imgW",imgW*scale);
        movieDetails.put("imgH",imgH*scale);
        movieDetails.put("length",length);
        movieDetails.put("movieName",movieName);
        movieDetails.put("synopsis",synopsis);
        movieDetails.put("language", language);
               
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
        
    private HBox makeCard(String movieId, double length){
        HBox card = new HBox();
        
        VBox img = movieImage(movieId);
        img.setPrefHeight(250);
        img.setPrefHeight(250);
        
        VBox movieDetails = makeMovieDescription(movieId, length);
        movieDetails.setPrefHeight(250);
        movieDetails.setPrefWidth(401);
        
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

    
    private VBox makeMovieDescription(String movieId, double length){
        
        Label movieName = new Label();
        Label synopsis = new Label();
        Label movielength = new Label();    
        Label language = new Label();
        Label path = new Label();
        VBox labelContainer = new VBox();
        
        
        int minute = (int)((length-(int)length)*60);
        
        path.setId(movieId+"_path");
        language.setId(movieId+"_language");
        movieName.setId(movieId+"_name");
        synopsis.setId(movieId+"_synopsis");
        movielength.setId(movieId+"_movielength");    
        path.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        language.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        movieName.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        synopsis.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px");
        movielength.setStyle("-fx-text-fill:#FFFFFF;-fx-padding:0px 0 5 18px;-fx-font-size:15px;-fx-alignment:center"); 
        path.setText("Path: "+ (String)this.movieDetails.get("imgPath"));
        language.setText("Language: "+(String)this.movieDetails.get("language"));
        movieName.setText("Name: "+(String)this.movieDetails.get("movieName"));
        movielength.setText("Movie Length: "+String.format("%dh ",(int)length)+ String.format("%dm", minute));
        synopsis.setText("Description: "+(String)this.movieDetails.get("synopsis"));           

        AnchorPane editdelete = MakeButton();
        
        labelContainer.setPrefWidth(652);
        labelContainer.setPrefHeight(400);
        labelContainer.setStyle("-fx-padding:0 0 0 20");
        labelContainer.getChildren().addAll(path, language, movieName, movielength, synopsis, editdelete);
        

        return labelContainer;
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
    
    private AnchorPane MakeButton(){
        Button edit = new Button();
        Button delete = new Button();
        
        edit.setText("Edit");
        edit.setLayoutX(45);
        edit.setLayoutY(204);
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
        delete.setLayoutY(204);
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
        editdelete.setStyle("-fx-padding: 0 0 0 0");
        editdelete.setPrefWidth(401);
        editdelete.setPrefHeight(84);
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
