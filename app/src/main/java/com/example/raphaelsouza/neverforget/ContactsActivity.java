package com.example.raphaelsouza.neverforget;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ContactsActivity extends AppCompatActivity {

    ListView list;
    ContactsAdapter adapter;
    ContactDAO contactDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setTitle("Contacts");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        adapter    = new ContactsAdapter(this);
        contactDAO = new ContactDAO();
        list = (ListView) findViewById(R.id.contactsList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Contact selected = (Contact) adapter.getItemAtPosition(position);
                showDetailsActivity(selected);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {
                deleteContact(contactDAO.get(id));
                return true;
            }
        });
    }
    public void deleteContact(final Contact contact) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ContactsActivity.this);

        alertDialog.setTitle("Delete Contact");
        alertDialog.setMessage("Are you sure you want to delete the " +
                "contact? This action cannot be undone");

        alertDialog.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        contactDAO.delete(contact);
                        adapter.notifyDataSetChanged();
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
