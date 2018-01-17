/*
 * Some things to keep in mind:
 * In this specific example, the top of the screen is kept aside for the buying
 * aspect of the cash register. The bottom portion of the screen is devoted to
 * showing the results of the purchase and payment options.
 */
package cash.register;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

/**
 *
 * @author Saurav
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label  lblSubtotal, lblTax, lblLine, lblTotalText, lblTotal, lblChangeText, lblChange, lblBillText, lblBill;
    
    @FXML
    private Button calcNoodlesBtn, calcPizzaBtn, calcIceBtn, handleDone, handlePayment, handleReset;
    
    @FXML
    private TextField inputNoodleNum, inputPizzaNum, inputIceNum, inputPayment;
    
    /*
    The following calculations a used to set the prices of each of the items as constants that call functions
    to set a random price for each of them (within a reasonable range).
    
    In addition, the subtotal and total variables are initialized as instance fields
    so that they will work through this class.
    */
    private final double NOODLE_PRICE = randomPriceInRange(8,11);       //final keyword for a variable means
    private final double PIZZA_PRICE = randomPriceInRange(15,18);       //that its value cannot be changed
    private final double ICE_PRICE = randomPriceInRange(7,9);           //so that we can use it as a constant
    private final double TAX_RATE = 0.07;
    
    private double subtotal = 0;
    private double total = 0;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        All of these following commands are used to hide bottom portion of screen
        This is done in order to show only the top of the screen that pertains to
        making the purchase
        */
        lblTax.setVisible(false);
        lblLine.setVisible(false);
        lblTotalText.setVisible(false);
        lblTotal.setVisible(false);
        lblChangeText.setVisible(false);
        lblChange.setVisible(false);
        inputPayment.setVisible(false);
        handlePayment.setVisible(false);
        lblBillText.setVisible(false);
        lblBill.setVisible(false);
        handleReset.setVisible(false);
        
        
        //setup initial values in visible TextFields in case this function was called a second time
        inputNoodleNum.setText("0");
        inputPizzaNum.setText("0");
        inputIceNum.setText("0");
        inputPayment.setText("");
        lblSubtotal.setText("$0.00");
        
        // These lines below display the name and price of each of the items on the GUI
        calcNoodlesBtn.setText("Noodles ($" + NOODLE_PRICE + ")");
        calcPizzaBtn.setText("Pizza ($" + PIZZA_PRICE + ")");
        calcIceBtn.setText("Ice Cream ($" + ICE_PRICE + ")");
    }    
    
    /*
    Rounds the parameter to the hundredths place
    */
    private double roundToNearestHundredth(double price) {
        return ((int) ((price * 100.0) + ((price < 0.0) ? -0.5 : 0.5))) / 100.0;     //converts the parameter into cents,
                                                //then rounds, and finally converts back into dollars
    }
    
    /*
    Takes in two numbers, a lower bound and an upper bound, and returns a decimal
    with two decimal places. For example, if the parameters were 5 and 9, this function
    could return 5.09, 5.98, 7.41, 8.25, etc.
    */
    public double randomPriceInRange(double lower, double upper){
        Random ran = new Random();
        return roundToNearestHundredth(lower + (upper - lower) * ran.nextDouble());
    }
    
    
    @FXML
    private void handleNoodle(ActionEvent event) {
        calcSub();
    }
    
    @FXML
    private void handlePizza(ActionEvent event) {
        calcSub();
    }
    
    @FXML
    private void handleIce(ActionEvent event) {
        calcSub();
    }
    
    
    /*
    This function accepts two doubles as parameters, the first as the cost of a product
    and the second as quantity of the product.
    
    This method will multiply the input cost of an individual item with its quantity
    and adds this number to the subtotal.
    */
    public void addToSub(double cost, double num){
        subtotal += cost*num;
    }
    
    /*
    This function calculates the subtotal every time it is called by setting the
    subtotal back to zero and taking the sum of the products of the current quantities of each of the items
    and their values.
    This function also displays the subtotal in dollar format in the appopriate label.
    */
    public void calcSub(){      
        subtotal = 0;
        
        double noodleNum = Double.parseDouble(inputNoodleNum.getText());
        double pizzaNum = Double.parseDouble(inputPizzaNum.getText());
        double iceNum = Double.parseDouble(inputIceNum.getText());       
        
        addToSub(roundToNearestHundredth(Double.parseDouble(inputNoodleNum.getText())), NOODLE_PRICE);        
        addToSub(roundToNearestHundredth(Double.parseDouble(inputPizzaNum.getText())), PIZZA_PRICE);
        addToSub(roundToNearestHundredth(Double.parseDouble(inputIceNum.getText())), ICE_PRICE);
        
        lblSubtotal.setText("$" + roundToNearestHundredth(subtotal));
    }
    
    /*
    This function will start when the Done Shopping button is pressed
    When this function is called, this function will pass the value of the subtotal
    to the appropriate function and will cause the GUI to display the total cost
    at the bottom of the screen.
    This function will also display the necessary TextField for the payment.
    */
    @FXML
    private void handleDone(ActionEvent event) {
        //checkValidInput();    //implement to handle any possible errors in user input
                                //for example, if the user put in no numbers or invalid characters,
                                //deal with these errors in a way that does not cause the program to crash
        calcSub();
        
        inputNoodleNum.setEditable(false);
        inputPizzaNum.setEditable(false);
        inputIceNum.setEditable(false);
        
        handleDone.setVisible(false);
        lblTax.setVisible(true);
        lblLine.setVisible(true);
        lblTotalText.setVisible(true);
        lblTotal.setVisible(true);
        total = roundToNearestHundredth(subtotal + (subtotal*TAX_RATE));
        lblTotal.setText("$" + total);
        inputPayment.setVisible(true);
        handlePayment.setVisible(true);
    }
    
    /*
    This accessor takes in two double values and gives as, as an integer, the number
    of whole bills that can fit that fit in that amount of money.
    */
    public int getBalance(double payment, double quotient){
        double temp = ((int)((payment / quotient))*100+.5)/100.0;
        int num = (int)temp;
        return num;
        
    }
    
    /*
    This accessor method takes in a double variable as a parameter representing how much change is due
    Through a series of statements, the number of bills and coins of each denomination are recorded.
    This method will return those values as a String that is preformatted to be printed directly
    */
    public String getChange(double payment) {
        double moneyLeftToGive = payment;
        int hundred = Math.round(getBalance(moneyLeftToGive, 100));//100 dollar bills
        moneyLeftToGive = roundToNearestHundredth(moneyLeftToGive % (100));
        int twenty = Math.round(getBalance(moneyLeftToGive, 20));//20 dollar bills
        moneyLeftToGive = roundToNearestHundredth(moneyLeftToGive % (20));
        int ten = Math.round(getBalance(moneyLeftToGive, 10));//10 dollar bills
        moneyLeftToGive = roundToNearestHundredth(moneyLeftToGive % (10));
        int five = Math.round(getBalance(moneyLeftToGive, 5));//5 dollar bills
        moneyLeftToGive = roundToNearestHundredth(moneyLeftToGive % (5));
        int one = Math.round(getBalance(moneyLeftToGive, 1));//1 dollar bills
        moneyLeftToGive = roundToNearestHundredth(moneyLeftToGive % (1));
        int quarter = Math.round(getBalance(moneyLeftToGive, .25));//quarters
        moneyLeftToGive = roundToNearestHundredth(moneyLeftToGive % (.25));
        int dime = Math.round(getBalance(moneyLeftToGive, .1));//dimes
        moneyLeftToGive = roundToNearestHundredth(moneyLeftToGive % (.1));
        int nickel = Math.round(getBalance(moneyLeftToGive, .05));//nickels
        moneyLeftToGive = roundToNearestHundredth(moneyLeftToGive % .05);
        int penny = Math.round(getBalance(moneyLeftToGive, .01));          //the rest as pennies
        
        String denomination = hundred + " hundreds, " + twenty + " twenties,\n"
                + ten + " tens, " + five + " fives,\n"
                + one + " ones, " + quarter + " quarters,\n"
                + dime + " dimes, " + nickel + " nickels, and\n"
                + penny + " pennies.";
        return denomination;
    }
    
    @FXML
    private void handlePayment(ActionEvent event) {
        String resp = inputPayment.getText();
        double payment = roundToNearestHundredth(Double.parseDouble(resp));
        if(payment < subtotal){
            JOptionPane.showMessageDialog(null, "Pay at least the total price!", "Insufficient Funds!", ERROR_MESSAGE);
        } else {
            double diff = payment - total;          
            inputPayment.setVisible(false);
            handlePayment.setVisible(false);
            lblChangeText.setVisible(true);
            lblChange.setVisible(true);
            lblBillText.setVisible(true);
            lblBill.setVisible(true);
            lblChange.setText("$" + roundToNearestHundredth(diff));
            lblBill.setText(getChange(roundToNearestHundredth(diff)));
            handleReset.setVisible(true);
            
        }     
    }
    
    
    /*
    Reset the entire user interface back to the original state and position
    */
    @FXML
    private void handleReset(ActionEvent event){
        lblTax.setVisible(false);
        lblLine.setVisible(false);
        lblTotalText.setVisible(false);
        lblTotal.setVisible(false);
        lblChangeText.setVisible(false);
        lblChange.setVisible(false);
        inputPayment.setVisible(false);
        handlePayment.setVisible(false);
        lblBillText.setVisible(false);
        lblBill.setVisible(false);
        handleReset.setVisible(false);
        handleDone.setVisible(true);

        //make all initial TextFields editable again in case this method was called a second time
        inputNoodleNum.setEditable(true);
        inputPizzaNum.setEditable(true);
        inputIceNum.setEditable(true);
        
        //setup initial values in visible TextFields in case this method was called a second time
        inputNoodleNum.setText("0");
        inputPizzaNum.setText("0");
        inputIceNum.setText("0");
        inputPayment.setText("");
        lblSubtotal.setText("$0.00");
        
        
        
        // These lines below display the name and price of each of the items on the GUI
        calcNoodlesBtn.setText("Noodles ($" +NOODLE_PRICE + ")");
        calcPizzaBtn.setText("Pizza ($" + PIZZA_PRICE + ")");
        calcIceBtn.setText("Ice Cream ($" + ICE_PRICE + ")");
    }
    
}

