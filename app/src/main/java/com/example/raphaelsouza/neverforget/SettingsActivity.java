package com.example.raphaelsouza.neverforget;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
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
        setTitle(getString(R.string.settings));

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


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
                bm = Utils.compressBitmap(bm);
                selfDAO.updateSelfPicture(bm);
                selfPic.setImageBitmap(bm);
            } catch  (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void pickProfilePic() {
        Log.wtf("Tag", "pickProfilePic: will pick pic" );
        Crop.pickImage(this);
    }

    public void editName() {
        Log.wtf("Tag", "Edit Name" );
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);

        alertDialog.setTitle(getString(R.string.change_name));
        alertDialog.setMessage(getString(R.string.new_name));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        alertDialog.setView(input);

        alertDialog.setPositiveButton(getString(R.string.change),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        selfDAO.updateSelfName(input.getText().toString());
                        name.setText(input.getText().toString());
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
