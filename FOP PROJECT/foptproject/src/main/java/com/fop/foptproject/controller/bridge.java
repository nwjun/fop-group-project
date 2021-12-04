/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.foptproject.controller;

import java.util.HashMap;


/**
 *
 * @author User
 */
interface bridge{   
    HashMap<String,Integer> tempStorage = new HashMap<>();
    FoodnBeverageController getController(String FXML);   
}

