package com.example.raphaelsouza.neverforget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    static Bitmap getResizedBitmap(Bitmap image) {
        int maxSize = 250;
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    static Bitmap compressBitmap(Bitmap image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
    }
}
