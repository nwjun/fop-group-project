/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.sqlUtil;

import java.sql.*;

import com.fop.readConfig.readConfig;
import java.time.LocalDateTime;
import java.util.Properties;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class sqlConnect {
    private static Connection conn;
    protected static final String SALT = "c2afca4e3995e4e86caf97e63d644f";
    private final static int trial = 5; 
    
    public sqlConnect(){
        Properties prop = new readConfig().readconfigfile();
        try{
            this.conn = DriverManager.getConnection(
            "jdbc:mysql://127.0.0.1:3306/fopdb",prop.getProperty("configuration.sqlUser"),prop.getProperty("configuration.sqlPassword")
            );
        }
        catch (Exception e){
            e.printStackTrace();
        }  
    }
    
    public void addTestData() throws SQLException{
        // values to be filled
        String userId = "U00000"; //Store in DB
        String username = "Test"; //Store in DB
        String password = "testing12345"; // Cannot Store in DB
        String email = "test@email.com"; //Store in DB
        String phone = "0123456789"; //Store in DB
        int permission = 4;

        // SQL Statement
        String query = "INSERT INTO usercredentials(userId, username, password, email,phoneNumber,permission)"
                       + "VALUE(?,?,?,?,?,?)";
        
        // create a SQL prepare statement object
        PreparedStatement prepstat = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
        
        // filling the fields
        prepstat.setString(1, userId);
        prepstat.setString(2, username);
        prepstat.setString(3, password);
        prepstat.setString(4, email);
        prepstat.setString(5, phone);
        prepstat.setInt(6, permission);
        
        // execute the statement
        int rowAffected = prepstat.executeUpdate();
    
    }    
    
    public void createTestQuery() throws SQLException{
        // createa a SQL prepare statement object
        PreparedStatement prep = conn.prepareStatement("SELECT * FROM usercredentials");
        
        // execute the statement
        ResultSet rs = prep.executeQuery();
        
        // move the cursor to the beginning
        rs.next();
        String userId = rs.getString("userId");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        String phone = rs.getString("phonenumber");
        int permission = rs.getInt("permission");
        
        System.out.printf("UserID : %s\nUsername : %s\nPassword : %s\nEmail : %s\nPhone : %s\nPermission : %d\n",userId,username,password,email,phone,permission);
        
    }
    
    public static int checkDup(String email, String phoneNumber){ 
        String query = "SELECT (SELECT COUNT(email) FROM usercredentials WHERE email = ?) AS DE,(SELECT COUNT(phoneNumber) FROM usercredentials WHERE phoneNumber = ? AND phoneNumber IS NOT NULL) AS DP";
        
        int duplicateE,duplicateP;
        
        try{
            PreparedStatement prep = conn.prepareStatement(query);
            prep.setString(1,email);
            prep.setString(2,phoneNumber);
            ResultSet rs = prep.executeQuery();
            rs.next();
            duplicateE = rs.getInt("DE");
            duplicateP = rs.getInt("DP");
            
            int result = 0; 
            duplicateE = (duplicateE > 0)?-1:0;
            duplicateP = (duplicateP > 0)?-2:0;
            
            return result+duplicateE+duplicateP; // 0 = no dup -1 =  dup email -2 = dup phone -3 = both dup
        }
        catch(SQLException e){
            e.printStackTrace();
            return -4; // error code
        }
    }
    
    public static int checkCredentials(String userEmail, String password){
        // preprocess input
        // SHA-256
        System.out.println("checking cred");
        String combination = userEmail + SALT + password;
        String inputPass = DigestUtils.sha256Hex(combination);
        
        // query statement
        String query = "SELECT email, password, permission " + 
                       "FROM usercredentials " + 
                       "WHERE email = ?";
        
        try {
            // retrieve from Database
            PreparedStatement prep = conn.prepareStatement(query);

            prep.setString(1, userEmail);

            ResultSet rs = prep.executeQuery();
            rs.next();
            String dbEmail = rs.getString("email");
            String dbPassword = rs.getString("password");
            int permission = rs.getInt("permission");

            if(dbPassword.equals(inputPass)){
                System.out.println("Successfully login");
                return permission;
            }
            else {
                System.out.println("Invalid Password");
                return -1;
            }
            
        }catch (SQLException e){
            System.out.println("Invalid Email");
            e.printStackTrace();
            return -2;
        }
        
    }
    
    public static boolean addNewUser(String username, String email, String password, String phoneNumber, int permission){
        // get last user ID
        // kuck section below
        String userID = "";
        String query = "SELECT userId " +
                     "FROM usercredentials "+
                     "ORDER BY userId DESC "+
                     "LIMIT 1";
        String combination = email + SALT + password;
        password = DigestUtils.sha256Hex(combination);

        try{
            //get last userID
            PreparedStatement prepstat1 = conn.prepareStatement(query);
            ResultSet rs = prepstat1.executeQuery();
            rs.next();
            userID = rs.getString("userId");
            userID = (userID.isBlank())?"U00000":userID;
            // increment by 1
            userID = String.format("U%05d",Integer.parseInt(userID.substring(1))+1);
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        
        // SQL Statement
        query = "INSERT INTO usercredentials(userId, username, password, email, phoneNumber, permission)" 
              +"VALUE(?,?,?,?,?,?)";
        
        int rowAffected = 0; // check sql response
        
        try{
            // create a SQL prepare statement object
            PreparedStatement prepstat = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);

            // filling the fields
            prepstat.setString(1, userID);
            prepstat.setString(2, username);
            prepstat.setString(3, password);
            prepstat.setString(4, email);
            prepstat.setString(5, phoneNumber);
            prepstat.setInt(6, permission);

            // execute the statement
            rowAffected = prepstat.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        
        return (rowAffected > 0)?true:false;
    }
    
    public static boolean addNewRegisterOTP(String username, String email, String phoneNumber, String password, String OTP){
        
        String query = "INSERT INTO otps(username,email,phoneNumber,password,OTP,isExpired,timestamp)"
                +"VALUE(?,?,?,?,?,?,?)";
        
        // try connection to SQL for three times before breaking
        for(int i = 0 ; i < trial ; i++){
            try {
                PreparedStatement prepstat = conn.prepareStatement(query);

                prepstat.setString(1,username);
                prepstat.setString(2,email);
                prepstat.setString(3,phoneNumber);
                prepstat.setString(4,password);
                prepstat.setString(5,OTP);
                prepstat.setInt(6,1);
                prepstat.setTimestamp(7,Timestamp.valueOf(LocalDateTime.now()));
                
                int rowAffected = prepstat.executeUpdate();
                return true;
            }
            catch (SQLException e){
                e.printStackTrace();
                continue;
            }
        }
        return false;
    }
    
    public static boolean updateOTP(String email, String OTP){
        
        String query = "UPDATE otps SET OTP = ? WHERE email = ?";
        
        for(int i = 0 ; i < trial ; i++){
            try{
                PreparedStatement prepstat = conn.prepareStatement(query);

                prepstat.setString(2,email);
                prepstat.setString(1,OTP);

                int rowAffected = prepstat.executeUpdate();
                return true;
            }
            catch(SQLException e){
                e.printStackTrace();
                continue;
            }
        }
        
        return false;
    }
    
    public static boolean removeNewRegisterOTP(String email, boolean isCancelled){
        
        if(!(isCancelled)){
            System.out.println("transferring");
            transferToUserCred(email);
        }
        
        String query = "DELETE FROM otps WHERE email = ?";

        for(int i = 0 ; i < trial ; i++){
            try{
                PreparedStatement prepstat = conn.prepareStatement(query);

                prepstat.setString(1,email);

                int rowAffected = prepstat.executeUpdate();

                return (rowAffected > 0)?true:false;
            }
            catch(SQLException e){
                e.printStackTrace();
                continue;
            }
        }
     
        
        return false;
    }
    
    public static String queryOTP(String email){
        String query = "SELECT OTP from otps WHERE email = ?";
        
        for(int i = 0 ; i < trial ; i++){
            try{
                PreparedStatement prepstat = conn.prepareStatement(query);
                
                prepstat.setString(1,email);
                
                ResultSet rs = prepstat.executeQuery();
                
                rs.next();
                String OTP = rs.getString("OTP");
                
                return OTP;
            }
            catch(SQLException e){
                e.printStackTrace();
                continue;
            }
        }
        
        return null;
    }
    
    public static Timestamp queryTimestamp(String email){
        String query = "SELECT timestamp from otps WHERE email = ?";
        
        for(int i = 0 ; i < trial ; i++){
            try{
                PreparedStatement prepstat = conn.prepareStatement(query);
                
                prepstat.setString(1,email);
                
                ResultSet rs = prepstat.executeQuery();
                
                rs.next();
                Timestamp timeIssued = rs.getTimestamp("timestamp");
                
                return timeIssued;
            }
            catch(SQLException e){
                e.printStackTrace();
                continue;
            }
        }
        
        return null;
    }
    
    public static void transferToUserCred(String email){
        String query = "SELECT * from otps WHERE email = ?";
        
        for(int i = 0 ; i < trial ; i++){
            try{
                PreparedStatement prepstat = conn.prepareStatement(query);
                
                prepstat.setString(1,email);
                
                ResultSet rs = prepstat.executeQuery();
                
                rs.next();
                String username = rs.getString("username");
                String password = rs.getString("password");
                String phonenumber = rs.getString("phoneNumber");

                boolean status = addNewUser(username,email,password,phonenumber,1);
                System.out.println(status);
                return;
            }
            catch(SQLException e){
                e.printStackTrace();
                continue;
            }
        }
        
    }
    
    public static HashMap<String, ArrayList<String>> queryAllMovie(){
        String query = "SELECT movieId, movieName, length, releaseDate, directorCast, language, poster, allShowTime, synopsis, rottenTomato, iMDB, ageRestrict "
                        + "FROM movies "
                        + "INNER JOIN pos USING (posterId) ";
        
        HashMap<String, ArrayList<String>> movies = new HashMap<>();
        ArrayList<String> movieId = new ArrayList<>();
        ArrayList<String> movieName = new ArrayList<>();
        ArrayList<String> length = new ArrayList<>();
        ArrayList<String> releaseDate = new ArrayList<>();
        ArrayList<String> directorCast = new ArrayList<>();
        ArrayList<String> language = new ArrayList<>();
        ArrayList<String> poster = new ArrayList<>();
        ArrayList<String> allShowTime = new ArrayList<>();
        ArrayList<String> synopsis = new ArrayList<>();
        ArrayList<String> rottenTomato = new ArrayList<>();
        ArrayList<String> iMDB = new ArrayList<>();
        ArrayList<String> ageRestrict = new ArrayList<>();
        
        try {
            PreparedStatement prepstat = conn.prepareStatement(query);
            ResultSet rs = prepstat.executeQuery();
            while(rs.next()){
                movieId.add(rs.getString("movieId"));
                movieName.add(rs.getString("movieName"));
                length.add(rs.getString("length"));
                releaseDate.add(rs.getString("releaseDate"));
                directorCast.add(rs.getString("directorCast"));
                language.add(rs.getString("language"));
                poster.add(rs.getString("poster"));
                allShowTime.add(rs.getString("allShowTime"));
                synopsis.add(rs.getString("synopsis"));
                rottenTomato.add(rs.getString("rottenTomato"));
                iMDB.add(rs.getString("iMDB"));
                ageRestrict.add(rs.getString("ageRestrict"));
            }
        } catch (SQLException ex) {
                ex.printStackTrace();
        }
        
        movies.put("movieId",movieId);
        movies.put("movieName",movieName);
        movies.put("length",length);
        movies.put("releaseDate",releaseDate);
        movies.put("directorCast",directorCast);
        movies.put("language",language);
        movies.put("poster",poster);
        movies.put("allShowTime",allShowTime);
        movies.put("synopsis",synopsis);
        movies.put("rottenTomato",rottenTomato);
        movies.put("iMDB",iMDB);
        movies.put("ageRestrict",ageRestrict);
        
        return movies;
    }
    
    public static HashMap<String, ArrayList<String>> queryAllProduct(){
        String query = "SELECT productId, productname, poster, price, productDescription, category "
                       + "FROM products "
                       + "INNER JOIN pos USING (posterId) ";
        
        HashMap<String, ArrayList<String>> items = new HashMap<>();
        ArrayList<String> productId = new ArrayList<>();
        ArrayList<String> posterPath = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        ArrayList<String> productDesc = new ArrayList<>();
        ArrayList<String> productName = new ArrayList<>();
        ArrayList<String> category = new ArrayList<>();
        
        try {
            PreparedStatement prepstat = conn.prepareStatement(query);

            ResultSet rs = prepstat.executeQuery();

            while(rs.next()){  
            productId.add(rs.getString("productId"));
            posterPath.add(rs.getString("poster"));
            price.add(rs.getString("price"));
            productDesc.add(rs.getString("productDescription"));
            productName.add(rs.getString("productname"));   
            category.add(rs.getString("category"));
            }   
        } catch (SQLException ex) {
                ex.printStackTrace();
        }
        
        items.put("productId",productId);
        items.put("posterPath",posterPath);
        items.put("price",price);
        items.put("productDesc",productDesc);
        items.put("productName",productName);
        items.put("category", category);
        
        return items;                            
    }

    public static HashMap<String,ArrayList<String>> queryProduct(String category){
        String query = "SELECT products.productId,pos.poster,products.price,products.productDescription,products.productname " 
                       + "FROM pos " 
                       + "INNER JOIN products " 
                       + "ON pos.posterId = products.posterId "
                       + "WHERE products.category = ?";
        
        HashMap<String,ArrayList<String>> items = new HashMap<>();
        ArrayList<String> productId = new ArrayList<>();
        ArrayList<String> posterPath = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        ArrayList<String> productDesc = new ArrayList<>();
        ArrayList<String> productName = new ArrayList<>();
   
        for(int i = 0; i < 5 ; i++){
            try{
                PreparedStatement prepstat = conn.prepareStatement(query);
                
                prepstat.setString(1,category);
                
                ResultSet rs = prepstat.executeQuery();
                
                while(rs.next()){
                    //System.out.printf("%s %s %s %s",rs.getString("productId"),rs.getString("poster"),rs.getString("price"),rs.getString("productDescription"));
                    productId.add(rs.getString("productId"));
                    posterPath.add(rs.getString("poster"));
                    price.add(rs.getString("price"));
                    productDesc.add(rs.getString("productDescription"));
                    productName.add(rs.getString("productname"));
                }
                break;
            }
            catch(SQLException e){
                e.printStackTrace();
                continue;
            }
        }
        items.put("productId",productId);
        items.put("posterPath",posterPath);
        items.put("price",price);
        items.put("productDesc",productDesc);
        items.put("productName",productName);
        
        return items;
    }

    public static String queryProductInfo(String productId,String fieldName){
        String query = "SELECT %s FROM products WHERE productId = ?";
        
        for(int i = 0 ; i < trial ; i++){
            try{
                query = String.format(query,fieldName);
                PreparedStatement prepstat = conn.prepareStatement(query);
                prepstat.setString(1,productId);
                
                ResultSet rs = prepstat.executeQuery();
                
                String result;
                if(rs.next()){
                    result = rs.getString(fieldName);
                }
                else{
                    return null;
                }
                return result;
            }
            catch(SQLException e){
                e.printStackTrace();
                continue;
            }
        }
        
        return "0";
    }
}
