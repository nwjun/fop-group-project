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
import com.fop.readConfig.readConfig;

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
6. sendBookingConfirmations-> send customers' booking details (with attachments)
7. setEncrypt -> Change the encryption protocol of the email. 0 for TLS(Default) 1 for SSL
*/

public class emailTo{
    // shared attributes
    private static String EMAIL;
    private static String PASSWORD;
    private static int currentEncrypt = 0;
    private static Session session;
    
    // independent for each objects
    private String reci;
    
    // constructor
    public emailTo(String recip){
        // setting up destination email
        this.reci = recip;
        
        Properties prop = new readConfig().readconfigfile();
        this.EMAIL = prop.getProperty("configuration.smtpemail");
        this.PASSWORD = prop.getProperty("configuration.smtppassword");
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
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email)); // sender
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recepient)); // multiple recipient
            message.setSubject(subject);
            message.setText(contentText,"utf-8", "html");
            
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
    
    // will be used for both password changing and email verification matters
    public String sendEmailVerification(boolean password){
        // single recipient at a time only
        Random r = new Random();
        int rand = r.nextInt(1000000); // Generate a random number between 0 to 999999
        String OTP = String.format("%06d",rand); // String format the integer to have 6 leading zeroes
        String subject;
        String content;
        
        if(password == false){
            subject = "GSC Account Email Verificaiton";
            content = "<p>Dear customer,"
                    + "<br><br>"
                    + "Thanks for Signing up, we just need to verify your Email address with this One-Time-Password:"
                    + "<br><h3>    "
                    + OTP
                    + "</h3><br>"
                    + "Sincerely,"
                    + "<br>"
                    + "<h4>GSC Support</h4>"
                    + "</p>";
        }
        else{
            subject = "GSC Verification to change password";         
            content = "<p>Dear customer,"
                    + "<br><br>"
                    + "You recently made a request to reset your password. Please use the one-time-password below to continue."
                    + "<br><h3>    "
                    + OTP
                    + "</h3>"
                    + "If you did not make this change or you believe an unauthorised person has accessed your account, you should go to reset your password immediately. Then sign into your GSC account page to review and update your security settings."
                    + "<br><br>"
                    + "Sincerely,"
                    + "<br>"
                    + "<h4>GSC Support</h4>"
                    + "</p>";
        }
        
        try{
            Message message = prepMail(session,EMAIL,reci,subject,content);
            Transport.send(message); // send OTP email
            return OTP; // return to sql handler  
        }
        catch(Exception e){
            Logger.getLogger(emailTo.class.getName()).log(Level.SEVERE, null, e);
            return "OTP failed";
        }
        
    }
    
    // build for sending special promo notifications
    // accept image files, format in an embedded email content
    public boolean sendCustomMail(String subject,String content){
        // multiple recipients are enabled
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
    
    // movie notification, to alert user their movie time 
    public boolean sendNotification(String movieName, String date, String time, String venue){
        // multiple recipients are enabled
        try{
             String subject = "GSC Movie Notification";
             String content = "<p>Dear customer,"
                    + "<br><br>"
                    + "Please be notified the movie you have booked is going to start soon. "
                    + "<br><br>"
                    + "Movie name: " + movieName
                    + "<br>"
                    + "Date: " + date
                    + "<br>"
                    + "Time: " + time
                    + "<br>"
                    + "Venue: " + venue
                    + "<br><br>"
                    + "Hope to see you there!"
                    + "<br><br>"
                    + "Sincerely,"
                    + "<br>"
                    + "<h4>GSC Customer Support</h4>"
                    + "</p>";
            
             Message message = prepMail(session,EMAIL,reci,subject,content);
             Transport.send(message);
         }
         catch (Exception e){
            e.printStackTrace();
            return false;
         }
            return true;
    }
    
    // send booking details after successful purchase  
    public boolean sendBookingConfirmations(String content,String movieName,String firstName,String bookingNumber,String bookingId, String date,String time, String seats, double payment){
        try {
            String subject = "Booking Confirmation for" + movieName;
            String htmlcontent = String.format(content,firstName,bookingNumber,bookingId,movieName,date,time,seats,payment);
            Message message = prepMail(session,EMAIL,reci,subject,htmlcontent);
            Transport.send(message);
        }
        catch(Exception e){
            return false;
        }
        return true;
    }
} 