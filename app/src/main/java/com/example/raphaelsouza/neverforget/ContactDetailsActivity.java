package com.example.raphaelsouza.neverforget;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.id;
import static android.R.attr.name;
import static com.example.raphaelsouza.neverforget.R.id.fab;

public class ContactDetailsActivity extends AppCompatActivity {
    ContactDAO contactDAO;
    Contact contact;

    TextView contactName;
    CircleImageView contactPicture;

    ImageButton pickImage;
    ImageButton changeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contactDAO = new ContactDAO();

        long id = getIntent().getExtras().getLong("ContactID");
        contact = contactDAO.get(id);

        pickImage  = (ImageButton) findViewById(R.id.pickImage);
        changeName = (ImageButton) findViewById(R.id.changeName);

        contactPicture = (CircleImageView) findViewById(R.id.contactPicture);
        contactName = (TextView) findViewById(R.id.contactName);

        contactName.setText(contact.name);

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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (imageReturnedIntent == null) {
            return;
        }
        try {
            Bitmap bm = MediaStore.Images
                    .Media.getBitmap(this.getContentResolver(), imageReturnedIntent.getData());
            bm = getResizedBitmap(bm,500);
            contactDAO.updatePicture(contact, bm);
            contactPicture.setImageBitmap(bm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void pickProfilePic() {
        Log.wtf("Tag", "pickProfilePic: will pick pic" );
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.setType("image/*");
        startActivityForResult(pickPhoto , 1);

    }

    public void editName() {
        Log.wtf("Tag", "Edit Name" );
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ContactDetailsActivity.this);

        alertDialog.setTitle("Change Name");
        alertDialog.setMessage("Enter your new name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Change",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        contactName.setText(input.getText().toString());
                        contactDAO.updateName(contact, input.getText().toString());
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

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
