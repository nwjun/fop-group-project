/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fop.foptproject;

import com.opencsv.CSVReader;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jun
 */
public class ReadCSV {

    public static ArrayList<String[]> readCSV(String file) throws FileNotFoundException, IOException {
        ArrayList<String[]> items = new ArrayList<>();

        try {
            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader(file);

            // create csvReader object passing
            // file reader as a parameter
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                items.add(nextRecord);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return items;
    }

    public static Movie[] csvToMovie(String file) {
        
        ArrayList<String[]> items = null;

        try {
            items = readCSV(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        // Get number of rows(number of items) in CSV
        int length = items.size();
        Movie[] movies = new Movie[length];

        // POSTERH -> posterHeight
        // POSTERW -> posterWidth
        // scale -> how to scale the poster
        final double POSTERH = 210, POSTERW = 140;
        final double SCALE = 1.5;

        for (int i = 0; i < length; i++) {
            // Get item read from csv, accessing each row by a time
            String[] item = items.get(i);
            // Convert string to array (just for testing only)
            String[] allShowTimes = new String[]{item[6]};
            String[] allShowDates = new String[]{item[7]};
            // Convert from string to integer and double
            // Trim space before and after word, or else it will fail to convert
            int len = Integer.parseInt(item[8].trim());
            double rottenTomato = Double.parseDouble(item[9].trim());
            double iMDB = Double.parseDouble(item[10].trim());
            
            // Create movie object and store to movies array
            movies[i] = new Movie(item[0], item[1], item[2], item[3], item[4], item[5],
                    allShowTimes, allShowDates, len, rottenTomato, iMDB, POSTERW, POSTERH, SCALE);
        }

        return movies;
    }

    public static Food[] csvToFood(String file) {
        ArrayList<String[]> items = null;
        
        try {
            items = readCSV(file);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        // Get number of rows(number of items) in CSV
        int length = items.size();
        Food[] foods = new Food[length];
        
        // POSTERH -> posterHeight
        // POSTERW -> posterWidth
        // scale -> how to scale the poster
        final double POSTERH = 160, POSTERW = 160;
        final double SCALE = 1.5;

        for (int i = 0; i < length; i++) {
            // Get item read from csv, accessing each row by a time
            String[] item = items.get(i);
            double price = Double.parseDouble(item[4].trim());
            
            // Create food object and store to movies array
            foods[i] = new Food(item[0], item[1], item[2], item[3], price, POSTERW, POSTERH, SCALE);
        }

        return foods;
    }
}
