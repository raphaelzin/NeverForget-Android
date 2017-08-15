package com.example.raphaelsouza.neverforget;

import android.graphics.Bitmap;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;

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
        if (realm.where(Contact.class).equalTo("name", name, Case.INSENSITIVE).count() != 0 )
            return realm.where(Contact.class)
                    .equalTo("name", name.toLowerCase(), Case.INSENSITIVE).findFirst();
        else
            return null;
    }

    public void updatePicture(Contact contact, Bitmap picture) {
        realm.beginTransaction();
        contact.setImage(picture);
        realm.commitTransaction();
    }

    public void updateName(Contact contact, String name) {
        realm.beginTransaction();
        contact.name = name;
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
