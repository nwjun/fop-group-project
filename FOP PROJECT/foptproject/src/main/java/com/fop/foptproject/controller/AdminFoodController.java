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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.json.simple.parser.ParseException;
import com.fop.foptproject.App;
import javafx.scene.control.Alert;

/**
 *
 * @author kuckn
 */
public class AdminFoodController implements Initializable {

    List<String> lsFile = Arrays.asList("*.jpg", "*.png", "*.jpeg");
    String poster;
    String save;
    String desktopURL;
    String desktopPath;

    private ProductCardAdminFood content;
    private sqlConnect sql = new sqlConnect();

    private Object[] productId;
    private Object[] price;
    private Object[] posterPath;
    private Object[] productDesc;
    private Object[] productName;
    private Object[] category;
    private final double IMGW = 250;
    private final double IMGH = 250;
    private final double SCALE = 0.9;
    private int currentIndex = 0;
    private int currentPage = 0;
    private int maxPage;

    private String editproductId, deleteproductId;
    private boolean deletestatus = false;
    private boolean updatestatus = false;

    @FXML
    private ImageView DropImage;
    @FXML
    private GridPane productList;
    @FXML
    private Button nextPageButton, nextPageButton1;
    @FXML
    private Button prevPageButton, prevPageButton1;
    @FXML
    private TextField posterT;
    @FXML
    private TextField categoryT;
    @FXML
    private TextField productnameT;
    @FXML
    private TextField priceT;
    @FXML
    private TextArea productDescriptionT;
    @FXML
    private ImageView logo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (RealTimeStorage.getPermission().equals("3")) {
            String path = App.class.getResource("assets/company/master.png").toString();
            Image img = new Image(path/*, IMGW, IMGH, false, false*/);
            logo.setImage(img);
        }
        getProduct();
    }

    public void getProduct() {
        HashMap<String, ArrayList<String>> items = RealTimeStorage.getAllProducts();
//        HashMap<String, ArrayList<String>> items = sql.queryAllProduct();
        this.productId = items.get("productId").toArray();
        this.price = items.get("price").toArray();
        this.posterPath = items.get("posterPath").toArray();
        this.productDesc = items.get("productDesc").toArray();
        this.productName = items.get("productName").toArray();
        this.category = items.get("category").toArray();

        this.currentPage = 0;
        this.maxPage = (int) Math.ceil(productId.length / 6.0);
        this.currentIndex = 0;

        loadCard();
    }

    public void loadCard() {
        int n = productId.length;

        for (int i = 0; i < 6; i++) {
            this.content = new ProductCardAdminFood((String) productId[currentIndex], (String) posterPath[currentIndex], IMGW, IMGH, SCALE, Double.parseDouble((String) price[currentIndex]), (String) productName[currentIndex], (String) productDesc[currentIndex], (String) category[currentIndex]);
            AnchorPane MB = MakeButton((String) productId[currentIndex], currentIndex);

            VBox img = content.getImgCard();
            VBox DETAILS = content.getDetailCard();

            VBox details = new VBox();
            details.getChildren().addAll(DETAILS, MB);

            HBox card = new HBox();
            card.getChildren().addAll(img, details);
            card.getStyleClass().add("card");

            productList.add(card, 0, i);
            currentIndex++;
            if (currentIndex >= n) {
                break;
            }
        }

    }

    private boolean DeletePopUp(int x) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Delete \"" + (String) productName[x] + "\"");
        window.setMinWidth(340);
        window.setMinHeight(225);
        window.setY(300);
        window.setX(570);
        String path1 = App.class.getResource("assets/company/logo2.png").toString();
        Image img1 = new Image(path1/*, IMGW, IMGH, false, false*/);
        window.getIcons().add(img1);

        Label label = new Label();
        label.setText("Confirm Deletion of \"" + (String) productName[x] + "\"");
        label.setStyle("-fx-font-size: 16px");
        label.setPrefHeight(1);

        Label instruction = new Label("Type \"DELETE\"");

        TextField deleteText = new TextField();
        deleteText.setPromptText("Type \"DELETE\"");

        Button deleteButton = new Button();
        deleteButton.setText("Delete");
        deleteButton.getStyleClass().add("yellowButton");
        deleteButton.setOnAction(ex -> {
            if (deleteText.getText().equals("DELETE")) {
                this.deletestatus = true;
                window.close();
            }
        });

        Button cancel = new Button("Cancel");
        cancel.getStyleClass().add("outlineButton");
        cancel.setOnAction(ex -> window.close());

        String path;
        if (RealTimeStorage.getPermission().equals("3")) {
            path = App.class.getResource("assets/company/master.png").toString();
        } else {
            path = getPathway() + "company/Admin.png";
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
        row3.getChildren().addAll(spacing6, deleteText, spacing7, deleteButton, spacing, cancel, spacing1);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(row0, row1, row2, row3);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add(App.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
        window.showAndWait();

        return this.deletestatus;
    }

    private AnchorPane MakeButton(String productId, int x) {
        Button edit = new Button();
        Button delete = new Button();

        edit.setId(productId);
        edit.setText("Edit");
        edit.setLayoutX(45);
        edit.setLayoutY(20);
        edit.setPrefWidth(100);
        edit.setPrefHeight(31);
        edit.getStyleClass().add("outlineButton");

        edit.setOnAction(ex -> {
            this.editproductId = edit.getId();
            int index = 0;

            for (int i = 0; i < this.productId.length; i++) {
                if (((String) this.productId[i]).equals(this.editproductId)) {
                    index = i;
                    break;
                }
            }

            String path = App.class.getResource((String) this.posterPath[index]).toString();
            Image img = new Image(path, IMGW, IMGH, false, false);
            clean();
            DropImage.setImage(img);
            posterT.setText((String) this.posterPath[index]);
            categoryT.setText((String) this.category[index]);
            productnameT.setText((String) this.productName[index]);
            priceT.setText((String) this.price[index]);
            productDescriptionT.setText((String) this.productDesc[index]);

            this.updatestatus = true;

        });

        delete.setId(productId);
        delete.setText("Delete");
        delete.setLayoutX(235);
        delete.setLayoutY(20);
        delete.setPrefWidth(100);
        delete.setPrefHeight(31);
        delete.getStyleClass().add("yellowButton");

        delete.setOnAction(ex -> {
            this.deletestatus = DeletePopUp(x);
            this.deleteproductId = delete.getId();
            if (this.deletestatus) {
                try {
                    delete();
                } catch (ParseException exp) {
                    exp.printStackTrace();
                }
                this.deletestatus = false;
            }
        });

        AnchorPane editdelete = new AnchorPane();
        editdelete.setStyle("-fx-padding: 0 0 0 0");
        editdelete.setPrefWidth(401);
        editdelete.setPrefHeight(0);
        editdelete.getChildren().addAll(edit, delete);

        return editdelete;
    }

    public void delete() throws ParseException {
        String s = getdeleteproductId();
        RealTimeStorage.deleteProductDetails(s);
        System.out.println("1 row(s) affected in remote database: " + s + " deleted.");
        sql.delete(s);
        productList.getChildren().clear();
        getProduct();
        currentPage = 0;
        checkPage();
    }

    public String getdeleteproductId() {
        return this.deleteproductId;
    }

    public void checkPage() {
        if (currentPage == maxPage - 1) {
            nextPageButton.setDisable(true);
            nextPageButton1.setDisable(true);
        } else {
            nextPageButton.setDisable(false);
            nextPageButton1.setDisable(false);
        }
        if (currentPage == 0) {
            prevPageButton.setDisable(true);
            prevPageButton1.setDisable(true);
        } else {
            prevPageButton.setDisable(false);
            prevPageButton.setDisable(false);
        }
    }

    @FXML
    public void prevPage() {
        if (currentPage == 0) {
            return;
        }
        productList.getChildren().clear();
        currentIndex = (currentPage - 1) * 6;
        loadCard();
        currentPage--;
        checkPage();
    }

    @FXML
    public void nextPage() {
        if (currentIndex == productId.length) {
            return;
        }
        productList.getChildren().clear();
        loadCard();
        currentPage++;
        checkPage();
    }

    public String getPathway() {
        URL URL = AdminFoodController.class.getResource("/com/fop/foptproject/assets/foods/marker.txt");
        File file = null;
        try {
            file = Paths.get(URL.toURI()).toFile();
        } catch (URISyntaxException ex) {
            Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, null, ex);
        }
        String absolutePath = file.getAbsolutePath();

        //the output of absolutePath is "C:\\Users\\kuckn\\Documents\\GitHub\\fop-group-project\\FOP PROJECT\\foptproject\\target\\classes\\com\\fop\\foptproject\\assets\\movies\\poster7.jpg"
        String path = absolutePath.substring(0, absolutePath.indexOf("target"))+ "src/main/resources/com/fop/foptproject/assets/";
        
        return path;
    }

    String pathpath = "";
    String ext = "";

    @FXML
    private void singleImagePathRead(ActionEvent event) throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", lsFile));
        File f = fc.showOpenDialog(null);

        if (f != null) {
            this.pathpath = f.getAbsolutePath();
            Image img = new Image(new FileInputStream(this.pathpath));

            DropImage.setImage(img);      
            this.poster = this.pathpath.substring(this.pathpath.lastIndexOf("\\")+1);
            this.ext = this.pathpath.substring(this.pathpath.lastIndexOf(".")+1);
            this.save = "assets\\foods\\" + this.poster;
            this.desktopURL = getPathway()+"foods\\";
            this.desktopPath = this.desktopURL+ this.poster;
            
// old code
//             DropImage.setImage(img);
//             this.poster = this.pathpath.substring(this.pathpath.lastIndexOf("\\") + 1);
//             this.ext = this.pathpath.substring(this.pathpath.lastIndexOf(".") + 1);
//             this.save = "assets\\foods\\" + this.poster;
//             this.desktopURL = getPathway() + "foods\\";
//             this.desktopPath = this.desktopURL + this.poster;


            posterT.setText(this.save);
        }

    }

    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
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

    public String lastproductId() {
        String x = sql.getProductLastId(b);
        x = x.substring(1, x.length());
        String Id = Integer.toString(Integer.valueOf(x) + 1);
        if (Id.length() == 1) {
            Id = "00000" + Id;
        } else if (Id.length() == 2) {
            Id = "0000" + Id;
        }

        if (b.equals("beverage")) {
            Id = "B" + Id;
        } else if (b.equals("combo")) {
            Id = "S" + Id;
        } else if (b.equals("carte")) {
            Id = "F" + Id;
        } else if (b.equals("popcorn")) {
            Id = "P" + Id;
        }

        return Id;
    }

    public void clean() {
        DropImage.setImage(null);
        posterT.clear();
        categoryT.clear();
        productnameT.clear();
        priceT.clear();
        productDescriptionT.clear();
    }

    public void refresh() throws ParseException {
        productList.getChildren().clear();
        getProduct();
        currentPage = 0;
        checkPage();
    }

    String Id;
    String a;
    String b;
    String c;
    String d;
    String e;

    @FXML
    private void uploadProduct(ActionEvent event) throws ParseException {
        try{
        if (this.updatestatus){
            Id = this.editproductId;
            sql.delete(Id);
            a = posterT.getText();
            sql.insertPoster(Id, a);
            b = categoryT.getText();
        }
        else{
        b = categoryT.getText();
        Id = lastproductId();
        a = this.save;
        sql.insertPoster(Id, a);
        }
        
        c = productnameT.getText();
        d = priceT.getText();
        e = productDescriptionT.getText();
        if(this.updatestatus)
            RealTimeStorage.updateProductDetails(new String [] {e, Id, d, b, c, a}, Id);
        else
            RealTimeStorage.insertProductDetails(new String [] {e, Id, d, b, c, a});
        sql.insertProduct(Id, c, Double.parseDouble(d), Id, e, b);
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
        try {
            BufferedImage img = ImageIO.read(new File(this.pathpath));
            File outputfile = new File(this.desktopPath);
            ImageIO.write(img, this.ext, outputfile);
            System.out.println("Upload Successful: Poster Changed/Uploaded");
            this.pathpath = "";
            this.ext = "";
        } catch (IOException ex) {
            System.out.println("Upload Successful: Poster Unchanged");
        }
    }

    @FXML
    private void ClearButtonAct(ActionEvent event) {
        clean();
    }
}
