package com.example.raphaelsouza.neverforget;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    ListView operationsList;
    FullOperationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Realm.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        Log.wtf("Count", "Operations: " + adapter.getCount());
        operationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Operation selected = (Operation) adapter.getItemAtPosition(position);
                Log.wtf("TAG", "onItemClick: " + selected.toString() );
                showDetailsActivity(selected);
            }
        });
    }

    public void addOperation() {
        Intent addOperation = new Intent(this, AddOperationActivity.class);
//        startActivity(addOperation);
        startActivityForResult(addOperation, 0);
    }

    public void showDetailsActivity(Operation op) {
        Intent showDetails = new Intent(this,OperationDetails.class);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivityForResult(settings, 1);
        }

        if (id == R.id.contacts)
        {
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
