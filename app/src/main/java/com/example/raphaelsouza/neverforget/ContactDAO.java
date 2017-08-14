package com.example.raphaelsouza.neverforget;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.R.attr.id;

/**
 * Created by raphaelsouza on 17-08-09.
 */

public class ContactDAO {
    Realm realm = Realm.getDefaultInstance();

    public RealmQuery<Contact> getContacts() {
        return realm.where(Contact.class);
    }

    public Contact get(long id) {
        return realm.where(Contact.class).equalTo("id", id).findFirst();
    }

    public Contact getByName(String name) {
        if (realm.where(Contact.class).equalTo("name", name.toLowerCase()).count() != 0)
            return realm.where(Contact.class).equalTo("name", name.toLowerCase()).findFirst();
        else
            return null;
    }

    public void update(Contact contact) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(contact);
        realm.commitTransaction();
    }

    public void create(Contact contact) {
        realm.beginTransaction();
        realm.copyToRealm(contact);
        realm.commitTransaction();
    }

    public void delete(final Contact contact) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                contact.deleteFromRealm();
            }
        });
    }



}
