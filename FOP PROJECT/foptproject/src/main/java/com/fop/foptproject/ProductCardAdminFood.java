package com.fop.foptproject;

import static com.fop.foptproject.ProductCardAdminMovie.insertString;
import java.util.HashMap;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;

/**
 *
 * @author kuckn
 */
public class ProductCardAdminFood {

    private HashMap<String, Object> productDetails = new HashMap<>();
    private VBox imgCard;
    private VBox detailCard;

    public ProductCardAdminFood(String productID, String imgPath, double imgW, double imgH, double scale, double price, String productName, String productDesc, String category) {
        productDetails.put("productID", productID);
        productDetails.put("imgPath", imgPath);
        productDetails.put("imgW", imgW * scale);
        productDetails.put("imgH", imgH * scale);
        productDetails.put("price", price);
        productDetails.put("productName", productName);
        productDetails.put("productDesc", productDesc);
        productDetails.put("category", category);

        makeCard(productID, price);
    }

    public VBox getImgCard() {
        return this.imgCard;
    }

    public VBox getDetailCard() {
        return this.detailCard;
    }

    private void makeCard(String productId, double price) {
        this.imgCard = productImage(productId);
        this.imgCard.setPrefHeight(250);
        this.imgCard.setPrefHeight(250);

        this.detailCard = makeProductDescription(productId, price);
        this.detailCard.setPrefHeight(200);
        this.detailCard.setPrefWidth(450);
        this.detailCard.setStyle("-fx-padding: 5 0 0 25");

    }

    private VBox productImage(String productId) {
        Image productImg = getImage();
        ImageView img = new ImageView(productImg);
        img.setId(productId + "_img");
        img.setFitWidth((double) this.productDetails.get("imgW"));
        img.setFitHeight((double) this.productDetails.get("imgH"));
        img.setPreserveRatio(true);
        img.setSmooth(true);

        VBox container = new VBox();
        container.setStyle("-fx-padding:14 0 0 13");
        container.getChildren().addAll(img);

        return container;
    }

    private VBox makeProductDescription(String productId, double price) {

        Label productName = new Label();
        Label productDesc = new Label();
        Label priceTag = new Label();
        Label category = new Label();
        Label path = new Label();
        VBox labelContainer = new VBox();

        path.setId(productId + "_path");
        category.setId(productId + "_category");
        productName.setId(productId + "_name");
        productDesc.setId(productId + "_desc");
        priceTag.setId(productId + "_priceTag");

        path.setStyle("-fx-padding:0px 0px 5px 18px");
        category.setStyle("-fx-padding:0px 0px 5px 18px");
        productName.setStyle("-fx-padding:0px 0px 5px 18px");
        productDesc.setStyle("-fx-padding:0px 0px 5px 18px");
        priceTag.setStyle("-fx-padding:0px 0px 5px 18px");

        String a = (String) this.productDetails.get("productDesc");
        for (int i = 47; i < a.length(); i += 57) {
            a = insertString(a, "\n", a.lastIndexOf(" ", i - 3));
        }

        path.setText("Path: " + (String) this.productDetails.get("imgPath"));
        category.setText("Category: " + (String) this.productDetails.get("category"));
        productName.setText("Name: " + (String) this.productDetails.get("productName"));
        priceTag.setText("Price: " + String.format("RM%.2f", price));
        productDesc.setText("Description: " + a);

        labelContainer.setPrefWidth(748);
        labelContainer.setPrefHeight(200);
        labelContainer.setStyle("-fx-padding:20px 0 20px 0");
        labelContainer.getChildren().addAll(path, category, productName, priceTag, productDesc);
        labelContainer.setPrefHeight(Control.USE_COMPUTED_SIZE);
        
        return labelContainer;
    }

    public Object getValue(String key) {
        return this.productDetails.get(key);
    }

    private Image getImage() {
        String path = getClass().getResource((String) getValue("imgPath")).toString();
        Image img = new Image(path, (double) getValue("imgW"), (double) getValue("imgH"), false, false);
        return img;
    }
}
