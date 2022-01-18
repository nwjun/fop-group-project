package com.fop.Utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author WeiXin
 */
public class Checker {
    private static String emailPattern = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})+$";
    
    public static boolean checkEmail(String email){
        Pattern p = Pattern.compile(emailPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(email);
        return m.find();
    }
    
    //luhn's algorithm
    public static boolean checkCardValidity(String cardNumber){
        boolean isValid = false;
        int sum = 0;
        
        int length = cardNumber.length();
        int check = Integer.parseInt(cardNumber.charAt(length-1)+"");
        
        // double Even
        for(int i = length-2; i >= 0 ; i-=2){
            int num = 0;
            int numStr = (Integer.parseInt(cardNumber.charAt(i)+"")*2);
            
            if(numStr > 9){
                num = numStr/10 + numStr%10;
            }
            else{
                num = numStr;
            }
            sum += num + ((i==0)?0:Integer.parseInt(cardNumber.charAt(i-1)+""));
        }
        
        
        if((sum)%10 != 0){
            isValid = false;
        }
        else{
            isValid = true;
            
        }
        return isValid;
    }
    
    
}
