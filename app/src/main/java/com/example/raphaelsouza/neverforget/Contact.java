package com.example.raphaelsouza.neverforget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by raphaelsouza on 17-08-09.
 */

public class Contact extends RealmObject {
    @PrimaryKey
    public long id;

    public String name;
    public byte[] image;

    public Contact() {
        this.id   = UUID.randomUUID().hashCode();
    }

    public Contact(String name, Bitmap picture) {
        this.id   = UUID.randomUUID().hashCode();
        this.name = name;
        setImage(picture);
    }

    public String getFirstName() {
        return name.split(" ")[0];
    }

    public Contact(String name) {
        this.id   = UUID.randomUUID().hashCode();
        this.name = name;
    }

    public Bitmap getImage() {
        if (image == null) {
            return null;
        } else {
            return BitmapFactory.decodeByteArray(this.image, 0, this.image.length);
        }
    }

    public void setImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.image = stream.toByteArray();
    }
}
