package com.fop.foptproject.controller;

import com.fop.Utility.JSONToolSets;
import com.fop.Utility.emailTo;
import com.fop.Utility.sqlConnect;
import com.fop.foptproject.App;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author WeiXin
 */
public class CheckOutController implements Initializable {

    private HashMap<String, Object> summary = new HashMap<>();
    private double toBePaid = 0;
    private sqlConnect sql = new sqlConnect();
    private String bookedSeats = null;

    @FXML
    private Label movie;
    @FXML
    private Label seats;
    @FXML
    private Label ticketType;
    @FXML
    private Label FnB;
    @FXML
    private Label ticketPrice;
    @FXML
    private Label FnBPrice;
    @FXML
    private Label totalAmount;
    @FXML
    private ComboBox selectCard;
    @FXML
    private ComboBox selectBank;
    @FXML
    private TextField cardNumber;
    @FXML
    private TextField expiryDate;
    @FXML
    private PasswordField cvv;
    @FXML
    private CheckBox saveCard;
    @FXML
    private CheckBox agreement;
    @FXML
    private Button payButton;

    @FXML
    public void back(ActionEvent event) throws IOException {
        new SceneController().switchToFnB(event);
    }

    @FXML
    public void pay(ActionEvent event) {
        Alert a = new Alert(AlertType.INFORMATION);
        if (!agreement.isSelected()) {
            a.setContentText("You must agree to terms of the GSC Customer Agreement in order to make purchase.");
            Stage stage = (Stage) a.getDialogPane().getScene().getWindow(); // get the window of alert box and cast to stage to add icons
            stage.getIcons().add(new Image(App.class.getResource("assets/company/logo2.png").toString()));
            stage.showAndWait();
            return;
        } else {
            // get neccessary info
            String booked = this.bookedSeats.replace(", ", ",");
            String userId = RealTimeStorage.getUserId();
            String cinema = RealTimeStorage.getMovieBooking().get("cinemaName").toString();
            String movieName = RealTimeStorage.getMovieBooking().get("movieName").toString();
            String purchasedItem = JSONToolSets.writeReceiptJSON(RealTimeStorage.getFnB(), RealTimeStorage.getTicketType(), RealTimeStorage.getMovieBooking().get("theaterType").toString().equals("Premium"));
            String showDate = RealTimeStorage.getMovieBooking().get("showdate").toString();
            String showtime = RealTimeStorage.getMovieBooking().get("showTime").toString().split(" - ")[0];
            String theaterId = RealTimeStorage.getMovieBooking().get("theaterId").toString();
            String slots = RealTimeStorage.getMovieBooking().get("slots").toString();
            String chosenDay = RealTimeStorage.getMovieBooking().get("chosenDay").toString();
            //System.out.printf("UserId: %s\n Movie Name: %s\n Cinema Name: %s\n Booked Seats: %s\n Show DateTime: %s %s TheaterId: %s\n Purchased: %s\n",userId,movieName,cinema,booked,showDate,showtime,theaterId,purchasedItem);
            // set booked seat stat to occupied
            JSONToolSets json = new JSONToolSets(sql.querySeats(theaterId, slots, false), false);
            //json.parseTheaterSeat(Integer.parseInt(chosenDay));
            String revert = json.getNewSeatArr().toString();
            json.parseTheaterSeat(Integer.parseInt(chosenDay));
            for (int i = 0; i < RealTimeStorage.getSelectedSeats().size(); i++) {
                int row = Integer.parseInt(RealTimeStorage.getSelectedSeats().get(i)[0]);
                int column = Integer.parseInt(RealTimeStorage.getSelectedSeats().get(i)[1]);
                json.setSeatStat(row, column, 1, chosenDay);
            }
            String currentSeat = json.getNewSeatArr().toString();
            json.parseTheaterSeat(Integer.parseInt(chosenDay));
            // any process fail will reset the jsonArr to prev 
            boolean error = false;
            // save transaction detail to sql
            String bookingNumber = null;
            Date datenow = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp ts = new Timestamp(datenow.getTime());
            String timestamp = format.format(ts);
            RealTimeStorage.setTimestamp(timestamp);
            // send booking email to user
            for (int i = 0; i < 3; i++) {
                bookingNumber = sql.addTransactionDetail(userId, purchasedItem, booked, theaterId, showDate, showtime, movieName, cinema);
                if (bookingNumber == null) {
                    error = true;
                    continue;
                } else {
                    RealTimeStorage.setBookingNumber(bookingNumber);
                    new emailTo(RealTimeStorage.getUserEmail()).sendBookingConfirmations(movieName, RealTimeStorage.getUsername(), bookingNumber, bookingNumber, showDate, showtime, booked, this.toBePaid*1.16);
                    error = false;
                    break;
                }
            }
            if (!error) {
                try {
                    sql.updateSeats(currentSeat, theaterId, slots, false);
                    RealTimeStorage.setToBePaid(String.format("RM%.2f", this.toBePaid * 1.16 + 1.5));
                    new SceneController().switchToDonePayment(event);
                    error = false;
                } catch (Exception e) {
                    error = true;
                }
            }

            if (error) {
                sql.updateSeats(revert, theaterId, slots, false);
            }
            
            if(saveCard.isSelected()){
                String selectedBank = selectBank.getValue().toString();
                String card = cardNumber.getText();
                String expiry = expiryDate.getText();
                String cvvNumber = cvv.getText();
                RealTimeStorage.updateLinkedCard(new String[]{selectedBank, card, expiry, cvvNumber});
                RealTimeStorage.appendLinkedCards();
            }

        }
    }

    @FXML
    public void autoFillCardDetails(ActionEvent event) {
        try{
            if (selectCard.getValue().toString().equals("-")) {
                return;
            }
        }
        catch(Exception e){
            // do nothing
        }
        String card = selectCard.getValue().toString();
        String cardnumber = card.split(" - ")[1];
        int ind = 0;
        for (int i = 0; i < RealTimeStorage.getLinkedCard2D().size(); i++) {
            if (RealTimeStorage.getLinkedCard2D().get(i)[1].equals(cardnumber)) {
                ind = i;
                break;
            }
        }
        String bank = RealTimeStorage.getLinkedCard2D().get(ind)[0];
        String expiry = RealTimeStorage.getLinkedCard2D().get(ind)[2];
        String cvvVal = RealTimeStorage.getLinkedCard2D().get(ind)[3];
        
        selectBank.setValue(bank);
        cardNumber.setText(cardnumber);
        expiryDate.setText(expiry);
        cvv.setText(cvvVal);

    }

    @FXML
    public void clearCardSelectionOnChange(ActionEvent event) {
        selectCard.setValue("-");
    }

    public String[] getFnBPurchase() {
        // get food and beverage purchase
        double total = 0;
        String name = "";
        String productname;
        int quantity;
        HashMap<String, Integer> current = RealTimeStorage.getFnB();
        sqlConnect sql = new sqlConnect();
        int i = 0;
        for (String key : current.keySet()) {
            quantity = current.get(key);
            if (key.equals("S000005P")) {
                productname = "[FREE]" + sql.queryProductInfo(key.substring(0, 7), "productname");
            } else {
                total += quantity * Double.parseDouble(sql.queryProductInfo(key, "price"));
                productname = sql.queryProductInfo(key, "productname");
            }
            name += productname + " x" + quantity + ", ";
        }
        name = name.substring(0, name.length() - 2);
        RealTimeStorage.setfnb(name.replace(", ","\n"));
        this.toBePaid += total;
        return new String[]{name, total + ""};
    }

    public String getSeatBooking(ArrayList<String[]> seats) {
        String booked = "";
        for (String[] xy : seats) {
            int column = (Integer.parseInt(xy[1]) + 1);
            booked += (char) (65 + (Integer.parseInt(xy[0]))) + "" + column + ", ";
        }
        booked = booked.substring(0, booked.length() - 2);
        this.bookedSeats = booked;
        RealTimeStorage.setSeats(booked);
        return booked;
    }

    public String[] getTicketPurchase() {
        int[] ticketTypes = RealTimeStorage.getTicketType(); //no of ticket for elder, adult, student, OKU 
        String[] ticketPrice = RealTimeStorage.getTicketPrices(false); // ticket price for elder, adult, student, OKU
        String[] category = {"Elder", "Adult", "Student", "Disabled"};
        String tickettype = "";
        int quantity = 0;
        double total = 0;
        // get total price and ticket quantity by category
        for (int i = 0; i < RealTimeStorage.ticketTypeQuantity; i++) {
            if (RealTimeStorage.getMovieBooking().get("theaterType").equals("Classic")) {
                total += ticketTypes[i] * (Double.parseDouble(ticketPrice[i]));
                if (ticketTypes[i] != 0) {
                    tickettype += category[i] + " x" + ticketTypes[i] + "\n";
                }
            } else {
                quantity += ticketTypes[i];
                total += ticketTypes[i] * (Double.parseDouble(sql.queryProductInfo("TP", "price")));
                tickettype = "Premium x" + quantity + "\n";
            }
        }
        this.toBePaid += total;
        RealTimeStorage.setTypeByQuantity(tickettype.substring(0, tickettype.length() - 1));
        return new String[]{tickettype.substring(0, tickettype.length() - 1), total + ""};
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // init input field
        selectCard.setDisable(false);
        cardNumber.setDisable(false);
        expiryDate.setDisable(false);
        cvv.setDisable(false);
        agreement.setSelected(false);
        saveCard.setSelected(false);

        // fill booked movie name
        String showdatetime = RealTimeStorage.getMovieBooking().get("showdate").toString() + " " + RealTimeStorage.getMovieBooking().get("showTime").toString().split(" - ")[0];
        movie.setText(RealTimeStorage.getMovieBooking().get("movieName").toString() + "\n" + showdatetime);
        // fill booked seats
        ArrayList<String[]> seat = RealTimeStorage.getSelectedSeats();
        String bookedSeats = getSeatBooking(seat);
        seats.setText(bookedSeats);
        // fill ticket types
        String[] ticketValuePair = getTicketPurchase();
        ticketType.setText(ticketValuePair[0]);
        ticketPrice.setText(String.format("RM%.2f", Double.parseDouble(ticketValuePair[1])));
        // fill FnB purchase
        if (!RealTimeStorage.getFnB().isEmpty()) {
            String[] FnBValuePair = getFnBPurchase();
            FnB.setText(FnBValuePair[0]);
            FnBPrice.setText(String.format("RM%.2f", Double.parseDouble(FnBValuePair[1])));
        } else {
            FnB.setText("");
            FnBPrice.setText("RM0.00");
        }
        // totalAmount
        totalAmount.setText(String.format("RM%.2f", this.toBePaid * 1.16 + 1.5));
        // populate card choices
        ArrayList<String[]> cards = RealTimeStorage.getLinkedCard2D();
        if (cards == null) {
            selectCard.setDisable(true);
        } else {
            for (int i = 0; i < cards.size(); i++) {
                selectCard.getItems().add(cards.get(i)[0] + " - " + cards.get(i)[1]);
            }
            selectCard.getItems().add("-");
        }
        // populate bank choices
        selectBank.getItems().addAll(new String[]{"Ambank", "Maybank", "Public Bank"});
    }

}
