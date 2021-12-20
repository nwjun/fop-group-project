/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.Utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * @author WeiXin
 */
public class Checker {
    private static String emailPattern = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{3,5})$";
    
    public static boolean checkEmail(String email){
        Pattern p = Pattern.compile(emailPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(email);
        return m.find();
    }
}
