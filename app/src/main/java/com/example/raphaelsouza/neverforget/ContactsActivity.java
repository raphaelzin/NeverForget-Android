package com.example.raphaelsouza.neverforget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ContactsActivity extends AppCompatActivity {

    ListView list;
    ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setTitle("Contacts");

        adapter = new ContactsAdapter(this);
        list = (ListView) findViewById(R.id.contactsList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Contact selected = (Contact) adapter.getItemAtPosition(position);
                showDetailsActivity(selected);
            }
        });
    }


    public void showDetailsActivity(Contact selected) {
        Intent detailsActivity = new Intent(this, ContactDetailsActivity.class);
        detailsActivity.putExtra("ContactID", selected.id);
        startActivityForResult(detailsActivity, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.notifyDataSetChanged();
    }
}
