/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.sqlUtil;

import java.sql.*;
import com.fop.readConfig.readConfig;
import java.util.Properties;
import org.apache.commons.codec.digest.DigestUtils;

public class sqlConnect {
    private Connection conn;
    
    public sqlConnect(){
        Properties prop = new readConfig().readconfigfile();
        try{
            this.conn = DriverManager.getConnection(
            "jdbc:mysql://127.0.0.1:3306/fopdb",prop.getProperty("configuration.sqlUser"),prop.getProperty("configuration.sqlPassword")
            );
            System.out.println("Connection established!");
        }
        catch (Exception e){
            e.printStackTrace();
        }  
    }
    
    public void addTestData() throws SQLException{
        // values to be filled
        String userId = "U00000"; //Store in DB
        String username = "Test"; //Store in DB
        String password1 = "testing12345"; // Cannot Store in DB
        String email = "test@email.com"; //Store in DB
        String phone = "0123456789"; //Store in DB
        int permission = 4;
        
        String salt = "Pepper"; //To encrypt password with SHA-256
        String password = DigestUtils.sha256Hex(username+salt+password1); // Store in DB //encrypted password
        
        //Debug
//        String myShaString = "a01082202c2afca4e3995e4e86caf97e63d644f4855dfa5a492032877a7a22a4";
//        if(myShaString.equals(password))
//            System.out.println("Login Successfully");
//        else
//            System.out.println("Bad Credentials");
//        System.out.println(password);

        // SQL Statement
        String query = "INSERT INTO usercredentials(userId, username, password, email,phoneNumber,permission)" +
"                          VALUE(?,?,?,?,?,?)";
        
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
        int rowAffected = prepstat.executeUpdate(/*String.format(query,userId,username,password,email,phone,permission)*/);
    
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
}
