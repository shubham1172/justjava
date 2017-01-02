package com.semicolonlabs.justjava;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;


//This app displays an order form to order coffee
public class MainActivity extends AppCompatActivity {
    int quantity = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //review button onClick handler
    public void reviewOrder(View view) {
        if(checkName())
            displayMessage(createOrderSummary());
    }

    //order button onClick handler
    public void submitOrder(View view){
        if(checkName()) {
            String sendTo = getApplicationContext().getString(R.string.email_id);
            String subject = getApplicationContext().getString(R.string.email_subject) + " " + getName();
            String body = createOrderSummary();
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + sendTo));
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, body);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    //increment the quantity
    public void increment(View view) {
        if (quantity < 10)
            quantity++;
        else
            Toast.makeText(this, R.string.upperbound_message, Toast.LENGTH_SHORT).show();
        displayQuantity(quantity);
    }

    //decrement the quantity
    public void decrement(View view) {
        if (quantity > 1)
            quantity--;
        else
            Toast.makeText(this, R.string.lowerbound_message, Toast.LENGTH_SHORT).show();
        displayQuantity(quantity);
    }

    //calculate price of coffee
    private int calculatePrice(boolean topping1,boolean topping2) {
        //BASE PRICE = Rs. 25
        //TOPPING 1 costs Rs. 5
        //TOPPING 2 costs Rs. 10
        int per_coffee = 25;
        if(topping1)
                per_coffee += 5;
        if(topping2)
                per_coffee+=10;
        return quantity*per_coffee;
    }

    //create order summary
    private String createOrderSummary(){
        boolean[] addTopping = getToppings();
        String message = "Name: " + getName();
        message += "\nAdded " + getApplicationContext().getString(R.string.topping1).toLowerCase() + "? " + addTopping[0];
        message += "\nAdded " + getApplicationContext().getString(R.string.topping2).toLowerCase() + "? " + addTopping[1];
        message += "\nQuantity: " + quantity;
        message += "\nTotal: Rs." + calculatePrice(addTopping[0], addTopping[1]);
        message += "\nThank You!";
        return message;
    }

    //get name
    private String getName(){
        EditText nameText = (EditText)findViewById(R.id.name);
        String name = "";
        if(nameText!=null)
            name = nameText.getText().toString();
        return name.trim();
    }
    private boolean checkName(){
        if(getName().equals("")) {
            Toast.makeText(this, R.string.no_name_message, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //get toppings info
    private boolean[] getToppings(){
        CheckBox topping1CheckBox = (CheckBox) findViewById(R.id.topping1);
        boolean topping1 = false;
        if(topping1CheckBox!=null)
            topping1 = topping1CheckBox.isChecked();
        CheckBox topping2CheckBox = (CheckBox) findViewById(R.id.topping2);
        boolean topping2 = false;
        if(topping2CheckBox!=null)
            topping2 = topping2CheckBox.isChecked();
        boolean toppings[] = new boolean[2];
        toppings[0] = topping1;
        toppings[1] = topping2;
        return toppings;
    }

    /**
     * These methods display the given value on screen
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityView = (TextView) findViewById(R.id.quantity_text_view);
        if (quantityView != null)
            quantityView.setText(String.format(Locale.ENGLISH, "%d", numberOfCoffees));
    }

    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        if (orderSummaryTextView != null)
            orderSummaryTextView.setText(message);
    }

    public void clickAbout(View view){
        Intent AboutIntent = new Intent(this, DisplayAboutActivity.class);
        startActivity(AboutIntent);
    }

}
