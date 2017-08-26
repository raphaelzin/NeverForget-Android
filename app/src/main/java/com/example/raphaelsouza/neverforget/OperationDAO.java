package com.example.raphaelsouza.neverforget;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by raphaelsouza on 17-08-09.
 */

public class OperationDAO {
    Realm realm = Realm.getDefaultInstance();

    public RealmQuery<Operation> getOperations() {
        return realm.where(Operation.class);
    }

    public Operation get(long id) {
        return realm.where(Operation.class).equalTo("id", id).findFirst();
    }

    public void update(Operation operation, String details,
                       double amount, Date when, boolean isDebt, long contID) {
        realm.beginTransaction();

        operation.contactID = contID;
        operation.details   = details;
        operation.isDebt    = isDebt;
        operation.amount    = amount;
        operation.date      = when;

        realm.commitTransaction();
    }

    public void setPaidDate(Date date, Operation operation) {
        realm.beginTransaction();
        operation.paidDate = date;
        realm.commitTransaction();
    }

    public void setPaid(boolean paid, Operation operation) {

        realm.beginTransaction();
        operation.paidDate = null;
        operation.paid = paid;
        realm.commitTransaction();
    }

    public void create(Operation operation) {
        realm.beginTransaction();
        realm.copyToRealm(operation);
        realm.commitTransaction();
    }

    public RealmResults<Operation> getOperationsWith(long id) {
        return realm.where(Operation.class).equalTo("contactID", id).findAll();
    }

    public double operationsWith(long id, boolean debt) {
        return getOperations().equalTo("contactID", id).
                equalTo("isDebt", debt).equalTo("paid", false).sum("amount").doubleValue();
    }

    public double getTotal(boolean debt) {
        return getOperations().equalTo("isDebt", debt)
                .equalTo("paid", false).sum("amount").doubleValue();
    }

    public void delete(final Operation operation) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                operation.deleteFromRealm();
            }
        });
    }
}
