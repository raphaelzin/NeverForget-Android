package com.example.raphaelsouza.neverforget;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import io.realm.Realm;

import static android.R.id.input;
import static com.example.raphaelsouza.neverforget.R.id.contactName;
import static com.example.raphaelsouza.neverforget.R.id.fab;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    ListView operationsList;
    FullOperationsAdapter adapter;

    OperationDAO operationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Realm.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Never Forget");

        operationDAO = new OperationDAO();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOperation();
            }
        });

        adapter = new FullOperationsAdapter(this);
        operationsList = (ListView) findViewById(R.id.list_id);
        operationsList.setAdapter(adapter);
        operationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Operation selected = (Operation) adapter.getItemAtPosition(position);
                showDetailsActivity(selected);
            }
        });
        operationsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {
//                Snackbar.make(view, "Id:" + id +" pos: " + pos, Snackbar.LENGTH_LONG).show();
                deleteOperation(operationDAO.get(id));
                return true;
            }
        });
    }

    public void deleteOperation(final Operation operation) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Delete Operation");
        alertDialog.setMessage("Are you sure you want to delete the " +
                "operation? This action cannot be undone");

        alertDialog.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        operationDAO.delete(operation);
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

    public void addOperation() {
        Intent addOperation = new Intent(this, AddOperationActivity.class);
        startActivityForResult(addOperation, 0);
    }

    public void showDetailsActivity(Operation op) {
        Intent showDetails = new Intent(this,OperationDetailsActivity.class);
        showDetails.putExtra("OperationID", op.id);
        startActivityForResult(showDetails, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivityForResult(settings, 1);
        }

        if (id == R.id.contacts) {
            Log.i("Ei!", "Go to contacts");
            Intent contacts = new Intent(this, ContactsActivity.class);
            startActivityForResult(contacts, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.notifyDataSetChanged();
    }
}
