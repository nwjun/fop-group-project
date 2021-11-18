package com.fop.EmailUtil;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

/*
Dependencies:
1. javax mail
2. activation

Description:
This class will be used to store static functions for user notification and verification
Helper methods are set private to prevent abuse of these methods which may cause damage to the system
This email class used gmail smtp server and supports both TLS and SSL encryption, TLS will be used by default

Methods description:
1. sendTestMail     -> Only meant for internal testing only, DO NOT USE IT ON CLIENTS, return null if error is caught 
                       otherwise a message object which is configured to correct subject and content
2. sendCustomMail   -> Made for the Master and Admins to send custom email.
3. sendEmailVerification -> send email verification
4. sendNotification -> send movie notiification before the movie is being played
5. sendChangePasswordConfirm -> Send email verificaiton and update the password 
6. setEncrypt -> Change the encryption protocol of the email. 0 for TLS(Default) 1 for SSL
*/

public class emailTo{
    // shared attributes
    private static final String EMAIL = "smtp@email";
    private static final String PASSWORD = "smtp@password";
    private static int currentEncrypt = 0;
    private static Session session;
    
    // independent for each objects
    private String reci;
    
    // constructor
    public emailTo(String recip){
        // setting up destination email
        this.reci = recip;
        
        // setting up session object
        Properties properties = setUpTLSProp(); // use TLS by default
        emailTo.session = Session.getInstance(properties, 
                new Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(EMAIL,PASSWORD);
                    }
                });
        
    }
    
    // helper methods
    private static Properties setUpSSLProp(){
        // for configuring smtp properties
        Properties prop =  new Properties(); 
        prop.put("mail.smtp.auth","true");
        prop.put("mail.smtp.starttls.enable","true");
        prop.put("mail.smtp.ssl.trust","smtp.gmail.com");
        prop.put("mail.smtp.host","smtp.gmail.com");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        return prop;
    }
    
    private static Properties setUpTLSProp(){
        // for configuring smtp properties
        Properties prop =  new Properties(); 
        prop.put("mail.smtp.auth","true");
        prop.put("mail.smtp.starttls.enable","true");
        prop.put("mail.smtp.tls.trust","smtp.gmail.com");
        prop.put("mail.smtp.host","smtp.gmail.com");
        prop.put("mail.smtp.port","587");
        
        return prop;
    }
  
    private static Message prepMail(Session session,String email,String recepient, String subject, String contentText){
        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email)); // sender
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient)); // recipient
            message.setSubject(subject);
            message.setText(contentText);
            
            return message; // return message object
        } 
        catch (Exception ex){
            Logger.getLogger(emailTo.class.getName()).log(Level.SEVERE, null, ex);
            
            return null; // if error return null
        }   
    }
    
    // class methods
    public static void setEncrypt(int i){
        // check i value
        if(i >= 2 && i <= -1){
            System.out.println("Invalid value");
        }
        // check repetition
        else if(i == currentEncrypt){
            System.out.println("Already using this method!");
        }
        else if(i == 0){
            // update session object
            System.out.println(String.format("Changed to Method %d",i+1));
            Properties properties = setUpTLSProp();
            emailTo.session = Session.getInstance(properties, 
                new Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(EMAIL,PASSWORD);
                    }
                });
            emailTo.currentEncrypt = i;
        }
        else{
            // update session object
            System.out.println(String.format("Changed to Method %d",i+1));
            Properties properties = setUpSSLProp();
            emailTo.session = Session.getInstance(properties, 
                new Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(EMAIL,PASSWORD);
                    }
                });
            emailTo.currentEncrypt = i;
        }
    }
    
    public boolean sendTestMail() throws Exception{
        try{
            Message message = prepMail(session,EMAIL,reci,"Internal Test Mail","This is an internal test for email functionality");
            Transport.send(message);
            return true; // return true signal which indicates 200 response
        
        }
        catch(Exception me){
            Logger.getLogger(emailTo.class.getName()).log(Level.SEVERE, null, me);
            return false; // other errors will return false
        }
    }
    
    public String sendEmailVerification(boolean password){
        Random r = new Random();
        int rand = r.nextInt(1000000); // Generate a random number between 0 to 999999
        String OTP = String.format("%06d",rand); // String format the integer to have 6 leading zeroes
        String subject;
        String content;
        
        if(password == false){
            subject = "GSC Account Email Verificaiton";
            content = "Thanks for Signing up, we just need to verify your Email address.\nPlease use this OTP to verify: "+ OTP;
        }
        else{
            subject = "GSC Verification to change password";
            content = "Greetings,\nYou recently made a request to reset your password. Please use the one-time-password below to continue.\n\n" + OTP + "\nIf you did not make this change or you believe an unauthorised person has accessed your account, you should go to reset your password immediately. Then sign into your GSC account page to review and update your security settings. \n\nSincerely,\n\nGSC Support" ;           
        }
        
        try{
            Message message = prepMail(session,EMAIL,reci,subject,content);
            Transport.send(message);
            return OTP;    
        }
        catch(Exception e){
            Logger.getLogger(emailTo.class.getName()).log(Level.SEVERE, null, e);
            return "OTP failed";
        }
        
        
    }

    public boolean sendCustomMail(String subject,String content){
        try{
            Message message = prepMail(session,EMAIL,reci,subject,content);
            Transport.send(message);
            return true;    
        }
        catch(Exception e){
            Logger.getLogger(emailTo.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
       
    }
} 