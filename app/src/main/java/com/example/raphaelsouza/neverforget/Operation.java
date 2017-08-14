package com.example.raphaelsouza.neverforget;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by raphaelsouza on 17-08-09.
 */

public class Operation extends RealmObject {
    @PrimaryKey
    public long id;

    public long contactID;
    public String details;
    public Date date;
    public Date paidDate;
    public boolean isDebt;
    public double amount;
    public boolean paid;

    public Operation() {
        this.id = UUID.randomUUID().hashCode();
    }

    public Operation(long contactID, String details, Date date,
                      boolean isDebt, double amount) {
        this.id        = UUID.randomUUID().hashCode();
        this.contactID = contactID;
        this.details   = details;
        this.amount    = amount;
        this.isDebt    = isDebt;
        this.date      = date;
        this.paid      = false;
    }
}
