package com.fop.Utility;

import com.fop.foptproject.App;
import com.fop.foptproject.controller.RealTimeStorage;
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
import com.google.zxing.WriterException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

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

To be changed:
1. convert all hardcoded details into arguments
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
    
    // for email with pdf attachment
    private static Message prepMail(Session session,String email,String recepient, String subject, String contentText, boolean isConfirm) throws IOException, WriterException{
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        
        // other details
        String userId = RealTimeStorage.getUserId();
        String cinema = RealTimeStorage.getMovieBooking().get("cinemaName").toString();
        String movieName = RealTimeStorage.getMovieBooking().get("movieName").toString();
        String showDate = RealTimeStorage.getMovieBooking().get("showdate").toString();
        String showtime = RealTimeStorage.getMovieBooking().get("showTime").toString().split(" - ")[0];
        String theaterId = RealTimeStorage.getMovieBooking().get("theaterId").toString();
        String bookingNumber = RealTimeStorage.getBookingNumber();
        String transactionTimeStamp = RealTimeStorage.getTimestamp();

        // get booked ticketType
        String ticketType = RealTimeStorage.getTypeByQuantity();
        // get booked seats
        String seats = RealTimeStorage.getSeats();
        // get FnB purchase
        String fnb = RealTimeStorage.getfnb();
        
        ops = new TicketGenerator().genTicket(ops,bookingNumber, transactionTimeStamp, movieName, "H0"+theaterId, showDate, showtime, ticketType, seats, fnb);
        //ByteArrayOutputStream ops,String refId, String transactionTimeStamp, String movieName, String hall, String date, String time,String type, String seats, String FnB
        if(isConfirm){
            try{
                // create a MimeMessage object and set recipients and subject
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(email)); // sender
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recepient)); // multiple recipient
                message.setSubject(subject);
                
                // create a multipart object as a container
                MimeMultipart multipart = new MimeMultipart("related"); // a container which divide the emailBody into few parts
                
                // set HTML email body into the email
                BodyPart messageBody = new MimeBodyPart();
                messageBody.setContent(contentText,"text/html; charset=utf-8"); // encode to html and use utf-8 charset
                multipart.addBodyPart(messageBody); // add the email body part into the container
                
                // set GSC Icon at the top of the email
                MimeBodyPart messageHead = new MimeBodyPart();
                DataSource imgsrc = new FileDataSource("src/main/resources/com/fop/foptproject/assets/company/logo.png");
                messageHead.setDataHandler(new DataHandler(imgsrc));
                messageHead.setContentID("<logoimage>");
                messageHead.setDisposition(MimeBodyPart.INLINE);
                multipart.addBodyPart(messageHead);
                

                // attach ticket pdf into the email
                BodyPart messageAttachment = new MimeBodyPart();
                byte[] bytes = ops.toByteArray(); // turn the data in buffer into a byte array
                DataSource src = new ByteArrayDataSource(bytes,"application/pdf"); // tell the method the data is of pdf type
                messageAttachment.setDataHandler(new DataHandler(src));
                messageAttachment.setFileName("GSC E-ticket.pdf");
                multipart.addBodyPart(messageAttachment); // add to the container
                message.setContent(multipart); // set the email to take in the container
                
                 // magic method
                message.saveChanges();
                
                // return message object
                return message;
            } 
            catch (Exception ex){
                Logger.getLogger(emailTo.class.getName()).log(Level.SEVERE, null, ex);

                return null; // if error return null
            } 
        }
        return null;
    }
    
    // for html email only
    private static Message prepMail(Session session,String email,String recepient, String subject, String contentText){
        try{
            // create a MimeMessage object and set recipients and subject
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email)); // sender
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recepient)); // multiple recipient
            message.setSubject(subject);           
            
            // create a multipart object as a container
            MimeMultipart multipart = new MimeMultipart("related"); // a container which divide the emailBody into few parts

            // set HTML email body into the email
            BodyPart messageBody = new MimeBodyPart();
            messageBody.setContent(contentText,"text/html; charset=utf-8"); // encode to html and use utf-8 charset
            multipart.addBodyPart(messageBody); // add the email body part into the container

            // set GSC Icon at the top of the email
            MimeBodyPart messageHead = new MimeBodyPart();
            DataSource imgsrc = new FileDataSource("src/main/resources/com/fop/foptproject/assets/company/logo.png");
            messageHead.setDataHandler(new DataHandler(imgsrc));
            messageHead.setContentID("<logoimage>");
            messageHead.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(messageHead);
            
            message.setContent(multipart);
            message.saveChanges();
            
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
    public String sendEmailVerification(String firstName,boolean password){
        // single recipient at a time only
        Random r = new Random();
        int rand = r.nextInt(1000000); // Generate a random number between 0 to 999999
        String OTP = String.format("%06d",rand); // String format the integer to have 6 leading zeroes
        String subject;
        String content;
        
        if(password == false){
            try {
                content = new templateModifier().readHTML("src/main/resources/com/fop/Templates/emailVerificationTemplate.html");
                subject = "GSC Email Account Verification";
                content = String.format(content,firstName,OTP);
            }
            catch(Exception e){
                return "false";
            }
            
        }
        else{
            try {
                content = new templateModifier().readHTML("src/main/resources/com/fop/Templates/changePasswordTemplate.html");
                subject = "GSC Change Account Password";
                content = String.format(content,firstName,OTP);
            }
            catch(Exception e){
                return "false";
            }
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
    
    // movie notification, to alert user their movie time 
    public boolean sendNotification(String firstName, String bookingId, String bookingNumber ,String movieName, String date, String time, String hall, String seats){
        // multiple recipients are enabled
        try{
            String content = new templateModifier().readHTML("src/main/resources/com/fop/Templates/movieNotificationTemplate.html");
            String subject = "GSC Movie Notification";
            content = String.format(content,firstName,bookingNumber,bookingId,movieName,date,time,hall,seats);
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
    public boolean sendBookingConfirmations(String movieName,String firstName,String bookingNumber,String bookingId, String date,String time, String seats, double payment){
        try {
            String content = new templateModifier().readHTML("src/main/resources/com/fop/Templates/bookingConfirmationTemplate.html");
            String subject = "Booking Confirmation for " + movieName;
            content = String.format(content,firstName,bookingNumber,bookingId,movieName,date,time,seats,payment);
            Message message = prepMail(session,EMAIL,reci,subject,content,true);
            Transport.send(message);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // build for sending special promo notifications
    // accept image files, format in an embedded email content
    // brg to discussion
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
} 