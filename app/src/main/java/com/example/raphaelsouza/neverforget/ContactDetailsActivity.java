package com.example.raphaelsouza.neverforget;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactDetailsActivity extends AppCompatActivity {
    ContactDAO contactDAO;
    OperationDAO operationDAO;
    Contact contact;

    TextView contactName;
    CircleImageView contactPicture;

    ImageButton pickImage;
    ImageButton changeName;

    TextView credit;
    TextView debt;
    TextView overall;

    ListView compactList;
    CompactOperationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contactDAO = new ContactDAO();
        operationDAO = new OperationDAO();

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        long id = getIntent().getExtras().getLong("ContactID");
        contact = contactDAO.get(id);
        setTitle(contact.name);

        pickImage  = (ImageButton) findViewById(R.id.pickImage);
        changeName = (ImageButton) findViewById(R.id.changeName);

        contactPicture = (CircleImageView) findViewById(R.id.contactPicture);
        contactName = (TextView) findViewById(R.id.contactName);

        contactName.setText(contact.name);

        overall = (TextView) findViewById(R.id.overall);
        credit  = (TextView) findViewById(R.id.credit);
        debt    = (TextView) findViewById(R.id.debt);

        adapter = new CompactOperationsAdapter(this, contact.id);

        compactList = (ListView) findViewById(R.id.compatcList);
        compactList.setAdapter(adapter);

        if (contact.getImage() != null) {
            contactPicture.setImageBitmap(contact.getImage());
        }

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickProfilePic();
            }
        });

        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editName();
            }
        });

        compactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Operation selected = (Operation) adapter.getItemAtPosition(position);
                showDetailsActivity(selected);
            }
        });

        setup();
    }

    public void showDetailsActivity(Operation op) {
        Intent showDetails = new Intent(this,OperationDetailsActivity.class);
        showDetails.putExtra("OperationID", op.id);
        startActivityForResult(showDetails, 1);
    }

    public void setup() {
        credit.setText(Utils.currency(operationDAO.operationsWith(contact.id,false)));
        debt.setText(Utils.currency(operationDAO.operationsWith(contact.id,true)));
        overall.setText(Utils.currency(operationDAO.
                operationsWith(contact.id,false) - operationDAO.operationsWith(contact.id,true)));
        adapter.notifyDataSetChanged();
    }

    public void pickProfilePic() {
        Log.wtf("Tag", "pickProfilePic: will pick pic" );
        Crop.pickImage(this);
    }

    public void editName() {
        Log.wtf("Tag", "Edit Name" );
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ContactDetailsActivity.this);

        alertDialog.setTitle(getString(R.string.change_name));
        alertDialog.setMessage(getString(R.string.new_contact_name));
        final EditText input = new EditText(this);
        input.setText(contact.name);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        alertDialog.setView(input);

        alertDialog.setPositiveButton(getString(R.string.change),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        contactName.setText(input.getText().toString());
                        contactDAO.updateName(contact, input.getText().toString());
                    }
                });
        alertDialog.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            try {
                Bitmap bm = MediaStore.Images
                        .Media.getBitmap(this.getContentResolver(), Crop.getOutput(result));
                bm = Utils.getResizedBitmap(bm);
                contactDAO.updatePicture(contact, bm);
                contactPicture.setImageBitmap(Utils.getResizedBitmap(bm));
            } catch  (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_LONG).show();
        }
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
