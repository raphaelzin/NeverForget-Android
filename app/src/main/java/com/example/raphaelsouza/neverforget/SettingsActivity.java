package com.example.raphaelsouza.neverforget;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.IOException;
import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingsActivity extends AppCompatActivity {

    CircleImageView selfPic;
    SelfDAO selfDAO;
    OperationDAO operationDAO;
    TextView credit;
    TextView debt;
    TextView total;
    TextView name;
    Self self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        selfDAO = new SelfDAO();
        operationDAO = new OperationDAO();

        self = selfDAO.getSelf();

        credit = (TextView) findViewById(R.id.credit);
        total  = (TextView) findViewById(R.id.total);
        debt   = (TextView) findViewById(R.id.debt);
        name   = (TextView) findViewById(R.id.selfName);

        selfPic = (CircleImageView) findViewById(R.id.selfPicture);

        name.setText(self.name);
        if (self.getImage() != null) {
            selfPic.setImageBitmap(self.getImage());
        }

        credit.setText(Utils.currency(operationDAO.getTotal(false)));
        debt.setText(Utils.currency(operationDAO.getTotal(true)));
        total.setText(Utils.currency((operationDAO.getTotal(false)-operationDAO.getTotal(true))));

        ImageButton pickImage = (ImageButton) findViewById(R.id.pickImage);
        ImageButton editName  = (ImageButton) findViewById(R.id.changeName);

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickProfilePic();
            }
        });

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editName();
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
            selfDAO.updateSelfPicture(bm);
            selfPic.setImageBitmap(bm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void pickProfilePic() {
        Log.wtf("Tag", "pickProfilePic: will pick pic" );
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.setType("image/*");
        startActivityForResult(pickPhoto , 1);//one can be replaced with any action code

    }

    public void editName() {
        Log.wtf("Tag", "Edit Name" );
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);

        alertDialog.setTitle("Change Name");
        alertDialog.setMessage("Enter your new name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Change",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        selfDAO.updateSelfName(input.getText().toString());
                        name.setText(input.getText().toString());
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
