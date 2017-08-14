package com.example.raphaelsouza.neverforget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class Contacts extends AppCompatActivity {

    ListView list;
    ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        adapter = new ContactsAdapter(this);
        list = (ListView) findViewById(R.id.contactsList);
        list.setAdapter(adapter);
    }
}
