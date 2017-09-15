package com.example.raphaelsouza.neverforget;

import android.content.res.Resources;
import android.graphics.Bitmap;

import io.realm.Realm;

/**
 * Created by raphaelsouza on 17-08-09.
 */

public class SelfDAO {
    private Realm realm = Realm.getDefaultInstance();
    private Self self;

    public SelfDAO() {
        if (realm.where(Contact.class).count() == 0)  {
            realm.beginTransaction();
            realm.copyToRealm(new Self(Resources.getSystem().getString(R.string.you)));
            realm.commitTransaction();
        }
        self = realm.where(Self.class).findFirst();
    }

    public Self getSelf() {
        return self;
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
