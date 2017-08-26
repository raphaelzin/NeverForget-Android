package com.example.raphaelsouza.neverforget;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Raphael Souza on 17-08-26.
 */

public class Utils {
    static String currency(double amount) {
        String amountString;
        NumberFormat in = NumberFormat.getCurrencyInstance();
        amountString = in.format(amount);

        return amountString;
    }
}
