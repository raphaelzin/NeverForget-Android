package com.example.raphaelsouza.neverforget;

import io.realm.RealmObject;

/**
 * Created by raphaelsouza on 17-08-09.
 */

public class Contact extends RealmObject {
    public long id;
    public String name;
    public byte[] image;


}
