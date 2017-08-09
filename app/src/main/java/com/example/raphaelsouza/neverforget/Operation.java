package com.example.raphaelsouza.neverforget;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by raphaelsouza on 17-08-09.
 */

public class Operation extends RealmObject {
    public long id;
    public long contactID;
    public String details;
    public Date date;
    public Date paidDate;
    public boolean isDebt;
    public double amount;
}
