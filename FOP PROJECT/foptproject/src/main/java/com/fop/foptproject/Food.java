package com.fop.foptproject;

/**
 *
 * @author jun
 */
public class Food extends Item {

    Food(String foodID, String foodName, String posterPath, String description, double price,
            double posterW, double posterH, double scale) {
        /**
         * Constructor for Food object that is extended from Item class.
         *
         * @param foodID unique id of food
         * @param foodName name of the food to be shown
         * @param posterPath relative path to the poster of food
         * @param description description of food to be shown
         * @param price price of the food
         * @param posterW default width of poster
         * @param posterH default height of poster
         * @param scale magnitude of scaling for poster
         */

        super(foodID, foodName, posterPath, posterW, posterH, scale);
        this.setDetail("description", description);
        this.setDetail("price", price);
    }

}
