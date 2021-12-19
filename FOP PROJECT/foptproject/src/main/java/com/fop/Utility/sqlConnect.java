/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.Utility;

import com.fop.foptproject.App;
import java.sql.*;

import java.time.LocalDateTime;
import java.util.Properties;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class sqlConnect {

    private static Connection conn;
    protected static final String SALT = "c2afca4e3995e4e86caf97e63d644f";
    private final static int TRIAL = 5;

    public sqlConnect() {
        Properties prop = new readConfig().readconfigfile();
        try {
            this.conn = DriverManager.getConnection(
            prop.getProperty("configuration.sqlConnection"),prop.getProperty("configuration.sqlUser"),prop.getProperty("configuration.sqlPassword")
            );
            System.out.println("connected");
        } catch (SQLException e) {
            System.out.println("failed");
            e.printStackTrace();
        }
    }

    public void addTestData() throws SQLException {
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
        PreparedStatement prepstat = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

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

    public void createTestQuery() throws SQLException {
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

        System.out.printf("UserID : %s\nUsername : %s\nPassword : %s\nEmail : %s\nPhone : %s\nPermission : %d\n", userId, username, password, email, phone, permission);

    }

    public static void fillBlankToNull() {
        String query = "UPDATE usercredentials "
                + "SET phoneNumber = NULL "
                + "WHERE phoneNumber = ''";
        try {
            PreparedStatement prep = conn.prepareStatement(query);
            prep.executeUpdate();
        } catch (SQLException e) {
            return;
        }

    }

    public static int checkDup(String email, String phoneNumber) {
        String query = "SELECT (SELECT COUNT(email) FROM usercredentials WHERE email = ?) AS DE,(SELECT COUNT(phoneNumber) FROM usercredentials WHERE phoneNumber = ? AND phoneNumber IS NOT NULL) AS DP";

        int duplicateE, duplicateP;

        try {
            PreparedStatement prep = conn.prepareStatement(query);
            prep.setString(1, email);
            prep.setString(2, phoneNumber);
            ResultSet rs = prep.executeQuery();
            rs.next();
            duplicateE = rs.getInt("DE");
            duplicateP = rs.getInt("DP");

            int result = 0;
            duplicateE = (duplicateE > 0) ? -1 : 0;
            duplicateP = (duplicateP > 0) ? -2 : 0;

            return result + duplicateE + duplicateP; // 0 = no dup -1 =  dup email -2 = dup phone -3 = both dup
        } catch (SQLException e) {
            e.printStackTrace();
            return -4; // error code
        }
    }

    public static int checkCredentials(String userEmail, String password) {
        // preprocess input
        // SHA-256
        String combination = userEmail + SALT + password;
        String inputPass = DigestUtils.sha256Hex(combination);

        // query statement
        String query = "SELECT email, password, permission "
                + "FROM usercredentials "
                + "WHERE email = ?";

        try {
            // retrieve from Database
            PreparedStatement prep = conn.prepareStatement(query);

            prep.setString(1, userEmail);

            ResultSet rs = prep.executeQuery();
            rs.next();
            String dbEmail = rs.getString("email");
            String dbPassword = rs.getString("password");
            int permission = rs.getInt("permission");

            if (dbPassword.equals(inputPass)) {
                System.out.println("Successfully login");
                return permission;
            } else {
                System.out.println("Invalid Password");
                return -1;
            }

        } catch (SQLException e) {
            System.out.println("Invalid Email");
            e.printStackTrace();
            return -2;
        }

    }

    public static boolean addNewUser(String username, String email, String password, String phoneNumber, int permission) {
        // get last user ID
        // kuck section below
        String userID = "";
        String query = "SELECT userId "
                + "FROM usercredentials "
                + "ORDER BY userId DESC "
                + "LIMIT 1";
        String combination = email + SALT + password;
        password = DigestUtils.sha256Hex(combination);

        try {
            //get last userID
            PreparedStatement prepstat1 = conn.prepareStatement(query);
            ResultSet rs = prepstat1.executeQuery();
            rs.next();
            userID = rs.getString("userId");
            userID = (userID.isBlank()) ? "U00000" : userID;
            // increment by 1
            userID = String.format("U%05d", Integer.parseInt(userID.substring(1)) + 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // SQL Statement
        query = "INSERT INTO usercredentials(userId, username, password, email, phoneNumber, permission)"
                + "VALUE(?,?,?,?,?,?)";

        int rowAffected = 0; // check sql response

        try {
            // create a SQL prepare statement object
            PreparedStatement prepstat = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // filling the fields
            prepstat.setString(1, userID);
            prepstat.setString(2, username);
            prepstat.setString(3, password);
            prepstat.setString(4, email);
            prepstat.setString(5, phoneNumber);
            prepstat.setInt(6, permission);

            // execute the statement
            rowAffected = prepstat.executeUpdate();

            // fill blank field to null
            fillBlankToNull();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return (rowAffected > 0) ? true : false;
    }

    public static boolean addNewRegisterOTP(String username, String email, String phoneNumber, String password, String OTP) {

        String query = "INSERT INTO otps(username,email,phoneNumber,password,OTP,isExpired,timestamp)"
                + "VALUE(?,?,?,?,?,?,?)";

        // try connection to SQL for three times before breaking
        for (int i = 0; i < TRIAL; i++) {
            try {
                PreparedStatement prepstat = conn.prepareStatement(query);

                prepstat.setString(1, username);
                prepstat.setString(2, email);
                prepstat.setString(3, phoneNumber);
                prepstat.setString(4, password);
                prepstat.setString(5, OTP);
                prepstat.setInt(6, 1);
                prepstat.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

                int rowAffected = prepstat.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                continue;
            }
        }
        return false;
    }

    public static boolean updateOTP(String email, String OTP) {

        String query = "UPDATE otps SET OTP = ? WHERE email = ?";

        for (int i = 0; i < TRIAL; i++) {
            try {
                PreparedStatement prepstat = conn.prepareStatement(query);

                prepstat.setString(2, email);
                prepstat.setString(1, OTP);

                int rowAffected = prepstat.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                continue;
            }
        }

        return false;
    }

    public static boolean removeNewRegisterOTP(String email, boolean isCancelled) {

        if (!(isCancelled)) {
            transferToUserCred(email);
        }

        String query = "DELETE FROM otps WHERE email = ?";

        for (int i = 0; i < TRIAL; i++) {
            try {
                PreparedStatement prepstat = conn.prepareStatement(query);

                prepstat.setString(1, email);

                int rowAffected = prepstat.executeUpdate();

                return (rowAffected > 0) ? true : false;
            } catch (SQLException e) {
                e.printStackTrace();
                continue;
            }
        }

        return false;
    }

    public static String queryOTP(String email) {
        String query = "SELECT OTP from otps WHERE email = ?";

        for (int i = 0; i < TRIAL; i++) {
            try {
                PreparedStatement prepstat = conn.prepareStatement(query);

                prepstat.setString(1, email);

                ResultSet rs = prepstat.executeQuery();

                rs.next();
                String OTP = rs.getString("OTP");

                return OTP;
            } catch (SQLException e) {
                e.printStackTrace();
                continue;
            }
        }

        return null;
    }

    public static Timestamp queryTimestamp(String email) {
        String query = "SELECT timestamp from otps WHERE email = ?";

        for (int i = 0; i < TRIAL; i++) {
            try {
                PreparedStatement prepstat = conn.prepareStatement(query);

                prepstat.setString(1, email);

                ResultSet rs = prepstat.executeQuery();

                rs.next();
                Timestamp timeIssued = rs.getTimestamp("timestamp");

                return timeIssued;
            } catch (SQLException e) {
                e.printStackTrace();
                continue;
            }
        }

        return null;
    }

    public static void transferToUserCred(String email) {
        String query = "SELECT * from otps WHERE email = ?";

        for (int i = 0; i < TRIAL; i++) {
            try {
                PreparedStatement prepstat = conn.prepareStatement(query);

                prepstat.setString(1, email);

                ResultSet rs = prepstat.executeQuery();

                rs.next();
                String username = rs.getString("username");
                String password = rs.getString("password");
                String phonenumber = rs.getString("phoneNumber");

                boolean status = addNewUser(username, email, password, phonenumber, 1);
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                continue;
            }
        }

    }

    public void delete(String movieId) {
        String query = "DELETE FROM pos "
                + "WHERE posterId = ?";

        try {
            PreparedStatement prepstat = conn.prepareStatement(query);

            prepstat.setString(1, movieId);

            int rowAffected = prepstat.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertPoster(String posterId, String poster) {
        String query = "INSERT INTO pos (posterId, poster) "
                + "VALUES (?,?)";

        try {
            PreparedStatement prepstat = conn.prepareStatement(query);

            prepstat.setString(1, posterId);
            prepstat.setString(2, poster);

            int rowAffected = prepstat.executeUpdate();

        } catch (SQLException e) {
//                e.printStackTrace();
            System.out.println("Fail");
        }

    }

    public boolean theaterIDCheck(int x, String movieName) {
        String query = "SELECT movieName, COUNT(theaterId) as DUP "
                + "FROM movies "
                + "WHERE theaterId = ?";

        if (movieName == null) {
            return true;
        }

        boolean dup = false;
        try {
            PreparedStatement prepstat = conn.prepareStatement(query);

            prepstat.setInt(1, x);

            ResultSet rs = prepstat.executeQuery();

            rs.next();

            int count = rs.getInt("DUP");
            String movie = rs.getString("movieName");

            if (count == 1 && !(movie.contains(movieName) || movieName.contains(movie))) {
                dup = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dup;
    }

    public void insertMovie(String movieId, String movieName, double length, String releaseDate, String directorCast, String language, String posterId, String synopsis, double rottenTomato, double iMDB, int theaterId, String time) {
        String query = "INSERT INTO movies (movieId, movieName, length, releaseDate, directorCast, language, posterId, synopsis, rottenTomato, iMDB, ageRestrict, theaterId, time) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement prepstat = conn.prepareStatement(query);

            prepstat.setString(1, movieId);
            prepstat.setString(2, movieName);
            prepstat.setDouble(3, length);
            prepstat.setString(4, releaseDate);
            prepstat.setString(5, directorCast);
            prepstat.setString(6, language);
            prepstat.setString(7, posterId);
            prepstat.setString(8, synopsis);
            prepstat.setDouble(9, rottenTomato);
            prepstat.setDouble(10, iMDB);
            prepstat.setInt(11, 18);
            prepstat.setInt(12, theaterId);
            prepstat.setString(13, time);

            int rowAffected = prepstat.executeUpdate();
            System.out.println("Success");
        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Data Entry Error");
            a.setContentText("Data Entry Error. \nPlease Check Your Input.");
            Stage stage = (Stage) a.getDialogPane().getScene().getWindow(); // get the window of alert box and cast to stage to add icons
            stage.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
            stage.showAndWait();
            e.printStackTrace();

        }
    }

    public HashMap<String, Double> queryTicketPrice() {
        String query = "SELECT productId, price "
                + "FROM products "
                + "WHERE category = \"ticket\"";

        HashMap<String, Double> tickets = new HashMap<>();

        try {
            PreparedStatement prepstat = conn.prepareStatement(query);
            ResultSet rs = prepstat.executeQuery();
            while (rs.next()) {
                tickets.put(rs.getString("productId"), rs.getDouble("price"));

            }
            return tickets;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void changeTicketPrice(String Id, Double price) {
        String query = "UPDATE products "
                + "SET price = ? "
                + "WHERE productId = ?";

        try {
            PreparedStatement prepstat = conn.prepareStatement(query);

            prepstat.setDouble(1, price);
            prepstat.setString(2, Id);

            prepstat.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public HashMap<String, ArrayList<String>> queryAdmin() {
        String query = "SELECT userId, username, email "
                + "FROM usercredentials "
                + "WHERE permission = 2";

        HashMap<String, ArrayList<String>> admin = new HashMap<>();
        ArrayList<String> Id = new ArrayList<>();
        ArrayList<String> username = new ArrayList<>();
        ArrayList<String> email = new ArrayList<>();

        try {
            PreparedStatement prepstat = conn.prepareStatement(query);
            ResultSet rs = prepstat.executeQuery();
            while (rs.next()) {
                Id.add(rs.getString("userId"));
                username.add(rs.getString("username"));
                email.add(rs.getString("email"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        admin.put("Id", Id);
        admin.put("username", username);
        admin.put("email", email);

        return admin;
    }

    public void addAdmin(String email) {
        String query = "UPDATE usercredentials "
                + "SET permission = 2 "
                + "WHERE email = ?";

        try {
            PreparedStatement prepstat = conn.prepareStatement(query);

            prepstat.setString(1, email);

            prepstat.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removeAdmin(String email, String password) {
        String query = "DELETE FROM usercredentials "
                + "WHERE email = ? AND password = ?";

        String combination = email + SALT + password;
        password = DigestUtils.sha256Hex(combination);

        try {
            PreparedStatement prepstat = conn.prepareStatement(query);

            prepstat.setString(1, email);
            prepstat.setString(2, password);

            prepstat.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Incorrect Email or Password! Try Again!");
        }
    }

    public static HashMap<String, ArrayList<String>> queryAllMovie() {
        String query = "SELECT movieId, movieName, length, releaseDate, directorCast, language, poster, allShowTime, synopsis, rottenTomato, iMDB, ageRestrict, theaterId, slot, time "
                + "FROM movies "
                + "INNER JOIN pos USING (posterId)";

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
        ArrayList<String> theaterId = new ArrayList<>();
        ArrayList<String> slot = new ArrayList<>();
        ArrayList<String> time = new ArrayList<>();

        try {
            PreparedStatement prepstat = conn.prepareStatement(query);
            ResultSet rs = prepstat.executeQuery();
            while (rs.next()) {
                movieId.add(rs.getString("movieId"));
                movieName.add(rs.getString("movieName"));
                length.add(rs.getString("length"));
                releaseDate.add(rs.getString("releaseDate"));
                directorCast.add(rs.getString("directorCast"));
                language.add(rs.getString("language"));
                String path = rs.getString("poster");
                poster.add(path.replace("\\", "/")); // preprocess to fit linux system
                allShowTime.add(rs.getString("allShowTime"));
                synopsis.add(rs.getString("synopsis"));
                rottenTomato.add(rs.getString("rottenTomato"));
                iMDB.add(rs.getString("iMDB"));
                ageRestrict.add(rs.getString("ageRestrict"));
                theaterId.add(rs.getString("theaterId"));
                slot.add(rs.getString("slot"));
                time.add(rs.getString("time"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        movies.put("movieId", movieId);
        movies.put("movieName", movieName);
        movies.put("length", length);
        movies.put("releaseDate", releaseDate);
        movies.put("directorCast", directorCast);
        movies.put("language", language);
        movies.put("poster", poster);
        movies.put("allShowTime", allShowTime);
        movies.put("synopsis", synopsis);
        movies.put("rottenTomato", rottenTomato);
        movies.put("iMDB", iMDB);
        movies.put("ageRestrict", ageRestrict);
        movies.put("theaterId", theaterId);
        movies.put("slot", slot);
        movies.put("time", time);

        return movies;
    }

    public String getProductLastId(String category) {
        String query = "SELECT productId "
                + "FROM products "
                + "WHERE category = ? "
                + "ORDER BY productId DESC "
                + "LIMIT 1";
        String result;
        try {
            PreparedStatement prepstat = conn.prepareStatement(query);
            prepstat.setString(1, category);
            ResultSet rs = prepstat.executeQuery();
            rs.next();
            result = rs.getString("productId");
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertProduct(String productId, String productname, double price, String posterId, String productDescription, String category) {
        String query = "INSERT INTO products (productId, productname, price, posterId, productDescription, category) "
                + "VALUES (?,?,?,?,?,?)";

        try {
            PreparedStatement prepstat = conn.prepareStatement(query);

            prepstat.setString(1, productId);
            prepstat.setString(2, productname);
            prepstat.setDouble(3, price);
            prepstat.setString(4, posterId);
            prepstat.setString(5, productDescription);
            prepstat.setString(6, category);

            int rowAffected = prepstat.executeUpdate();

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Data Entry Error");
            a.setContentText("Data Entry Error. \nPlease Check Your Input.");
            Stage stage = (Stage) a.getDialogPane().getScene().getWindow(); // get the window of alert box and cast to stage to add icons
            stage.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
            stage.showAndWait();
            e.printStackTrace();
        }
    }

    public static HashMap<String, ArrayList<String>> queryAllProduct() {
        String query = "SELECT productId, productname, poster, price, productDescription, category "
                + "FROM products "
                + "INNER JOIN pos USING (posterId) "
                + "WHERE category = 'beverage' "
                + "UNION "
                + "SELECT productId, productname, poster, price, productDescription, category "
                + "FROM products "
                + "INNER JOIN pos USING (posterId) "
                + "WHERE category = 'popcorn' "
                + "UNION "
                + "SELECT productId, productname, poster, price, productDescription, category "
                + "FROM products "
                + "INNER JOIN pos USING (posterId) "
                + "WHERE category = 'carte' "
                + "UNION "
                + "SELECT productId, productname, poster, price, productDescription, category "
                + "FROM products "
                + "INNER JOIN pos USING (posterId) "
                + "WHERE category = 'combo' ";

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

            while (rs.next()) {
                productId.add(rs.getString("productId"));
                String path = rs.getString("poster");
                posterPath.add(path.replace("\\", "/"));
                price.add(rs.getString("price"));
                productDesc.add(rs.getString("productDescription"));
                productName.add(rs.getString("productname"));
                category.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        items.put("productId", productId);
        items.put("posterPath", posterPath);
        items.put("price", price);
        items.put("productDesc", productDesc);
        items.put("productName", productName);
        items.put("category", category);

        return items;
    }

    public static HashMap<String, ArrayList<String>> queryProduct(String category) {
        String query = "SELECT products.productId,pos.poster,products.price,products.productDescription,products.productname "
                + "FROM pos "
                + "INNER JOIN products "
                + "ON pos.posterId = products.posterId "
                + "WHERE products.category = ?";

        HashMap<String, ArrayList<String>> items = new HashMap<>();
        ArrayList<String> productId = new ArrayList<>();
        ArrayList<String> posterPath = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        ArrayList<String> productDesc = new ArrayList<>();
        ArrayList<String> productName = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            try {
                PreparedStatement prepstat = conn.prepareStatement(query);

                prepstat.setString(1, category);

                ResultSet rs = prepstat.executeQuery();

                while (rs.next()) {
                    productId.add(rs.getString("productId"));
                    String path = rs.getString("poster"); // preprocess to fit linux system
                    posterPath.add(path.replace("\\", "/"));
                    price.add(rs.getString("price"));
                    productDesc.add(rs.getString("productDescription"));
                    productName.add(rs.getString("productname"));
                }
                break;
            } catch (SQLException e) {
                e.printStackTrace();
                continue;
            }
        }
        items.put("productId", productId);
        items.put("posterPath", posterPath);
        items.put("price", price);
        items.put("productDesc", productDesc);
        items.put("productName", productName);

        return items;
    }

    public static String queryProductInfo(String productId, String fieldName) {
        String query = "SELECT %s FROM products WHERE productId = ?";

        for (int i = 0; i < TRIAL; i++) {
            try {
                query = String.format(query, fieldName);
                PreparedStatement prepstat = conn.prepareStatement(query);
                prepstat.setString(1, productId);

                ResultSet rs = prepstat.executeQuery();

                String result;
                if (rs.next()) {
                    result = rs.getString(fieldName);
                } else {
                    return null;
                }
                return result;
            } catch (SQLException e) {
                e.printStackTrace();
                continue;
            }
        }

        return "0";
    }

    public static void updateLinkedCard(String jsonString, String email) {
        String query = "UPDATE usercredentials SET linkedCards = ? WHERE email = ?";

        try {
            PreparedStatement prep = conn.prepareStatement(query);
            prep.setString(1, jsonString);
            prep.setString(2, email);

            int rowAffected = prep.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, String> queryUserCredentials(String email) {

        String query = "SELECT * FROM usercredentials "
                + "WHERE email = ?;";
        HashMap<String, String> result = new HashMap<>();
        try {
            PreparedStatement prep = conn.prepareStatement(query);
            prep.setString(1, email);

            ResultSet rs = prep.executeQuery();

            rs.next();
            result.put("userId", rs.getString("userId"));
            result.put("username", rs.getString("username"));
            result.put("phoneNumber", rs.getString("phoneNumber"));
            result.put("permission", Integer.toString(rs.getInt("permission")));
            String linkedCard = rs.getString("linkedCards");
            result.put("linkedCard", (linkedCard == null) ? "{}" : linkedCard);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String querySeats(String theaterId, String time, boolean isTemplate) {
        String query = "SELECT * FROM theaters WHERE theaterId = ?";
        String jsonString = null;

        try {
            PreparedStatement prep = conn.prepareStatement(query);
            prep.setString(1, theaterId);

            ResultSet rs = prep.executeQuery();

            rs.next();
            jsonString = rs.getString((isTemplate) ? "seatTemplate" : "seat" + time);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    public static void updateSeats(String jsonString, String theaterId, boolean isTemplate) {

        String query = (isTemplate) ? "UPDATE theaters SET seatTemplate = ? WHERE theaterId = ?" : "UPDATE theaters SET seat = ? WHERE theaterId = ?";

        try {
            PreparedStatement prep = conn.prepareStatement(query);
            prep.setString(1, jsonString);
            prep.setString(2, theaterId);

            int rowAffected = prep.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, ArrayList<String>> queryLandingFood(String category, int limit) {
        HashMap<String, ArrayList<String>> foodCategory = queryProduct(category);
        int length = foodCategory.get("productId").size();
        for (String key : foodCategory.keySet()) {
            for (int i = length - 1; i >= limit; i--) {
                foodCategory.get(key).remove(i);
            }
            foodCategory.get(key).trimToSize();
        }

        return foodCategory;
    }

    public static int setNewPassword(String userEmail, String newPassword) {
        // preprocess input
        // SHA-256
        String combination = userEmail + SALT + newPassword;
        String inputPass = DigestUtils.sha256Hex(combination);

        // query statement
        String query = "UPDATE usercredentials SET `password` = ? "
                + "WHERE email = ?";
        int rowAffected = 0;
        try {
            // retrieve from Database
            PreparedStatement prep = conn.prepareStatement(query);

            prep.setString(1, inputPass);
            prep.setString(2, userEmail);

            rowAffected = prep.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowAffected;
    }

    public static int setNewUsernameOrPhoneNumber(String userEmail, String input, boolean isPhone) {

        // query statement
        String query = "UPDATE usercredentials SET %s = ? "
                + "WHERE email = ?";
        if (isPhone) {
            query = String.format(query, "phoneNumber");
        } else {
            query = String.format(query, "username");
        }
        int rowAffected = 0;
        try {
            // retrieve from Database
            PreparedStatement prep = conn.prepareStatement(query);

            prep.setString(1, input);
            prep.setString(2, userEmail);

            rowAffected = prep.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowAffected;
    }

    public int setNewUserEmail(String userId, String newFieldValue) {
        // query statement
        String query = "UPDATE usercredentials SET email = ? "
                + "WHERE userId = ?";

        int rowAffected = 0;
        try {
            // retrieve from Database
            PreparedStatement prep = conn.prepareStatement(query);

            prep.setString(1, newFieldValue);
            prep.setString(2, userId);

            rowAffected = prep.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowAffected;
    }

}
