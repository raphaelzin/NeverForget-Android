package com.example.raphaelsouza.neverforget;

import android.graphics.Bitmap;

import io.realm.Realm;

/**
 * Created by raphaelsouza on 17-08-09.
 */

public class SelfDAO {
    Realm realm = Realm.getDefaultInstance();

    public SelfDAO() {
        if (realm.where(Contact.class).count() == 0)  {
            realm.beginTransaction();
            realm.copyToRealm(new Self("You"));
            realm.commitTransaction();
        }
    }

    public Self getSelf() {
        return realm.where(Self.class).findFirst();
    }

    public void updateSelfName(String name) {
        realm.beginTransaction();
        getSelf().name = name;
        realm.commitTransaction();
    }

    public void updateSelfPicture(Bitmap picture) {
        realm.beginTransaction();
        getSelf().setImage(picture);
        realm.commitTransaction();
    }

}
