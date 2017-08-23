package com.example.raphaelsouza.neverforget;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddOperationActivity extends AppCompatActivity {
    EditText name;
    EditText desc;
    EditText amount;
    EditText when;

    TextView contactName;
    TextView amountCell;

    ImageView arrow;
    CircleImageView contactPic;

    Button isDebtButton;
    Button notDebitButton;

    Calendar whenCalendar;
    DatePickerDialog.OnDateSetListener date;

    OperationDAO opDAO;
    ContactDAO contDAO;
    SelfDAO selfDAO;

    boolean isDebt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_operation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        opDAO   = new OperationDAO();
        contDAO = new ContactDAO();
        selfDAO = new SelfDAO();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("myTag","FAB Clicked");
                saveOperation();
            }
        });

        name   = (EditText) findViewById(R.id.nameEditText);
        when   = (EditText) findViewById(R.id.when);
        desc   = (EditText) findViewById(R.id.description);
        amount = (EditText) findViewById(R.id.amountEditText);

        contactName = (TextView) findViewById(R.id.contactName);
        amountCell  = (TextView) findViewById(R.id.amount);

        arrow      = (ImageView) findViewById(R.id.arrow);
        contactPic = (CircleImageView) findViewById(R.id.contactPicture);

        isDebtButton   = (Button) findViewById(R.id.isDebt);
        notDebitButton = (Button) findViewById(R.id.notDebt);
        isDebt = false;

        TextView selfName = (TextView) findViewById(R.id.selfName);
        CircleImageView selfPic = (CircleImageView) findViewById(R.id.selfPicture);

        selfName.setText(selfDAO.getSelf().getFirstName());
        if (selfDAO.getSelf().getImage() != null) {
            selfPic.setImageBitmap(selfDAO.getSelf().getImage());
        }

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                contactName.setText(charSequence.toString().split(" ")[0]);
                if(contDAO.getByName(charSequence.toString()) != null) {
                    contactPic.setImageBitmap(contDAO
                            .getByName(charSequence.toString()).getImage());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                amountCell.setText("$"+charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        whenCalendar = Calendar.getInstance();
        updateDateLabel();


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                whenCalendar.set(Calendar.YEAR, year);
                whenCalendar.set(Calendar.MONTH, monthOfYear);
                whenCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }

        };
        when.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddOperationActivity.this, date,
                        whenCalendar.get(Calendar.YEAR),
                        whenCalendar.get(Calendar.MONTH),
                        whenCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void updateDateLabel() {
        String myFormat = "MMM, dd - yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        when.setText(sdf.format(whenCalendar.getTime()));
    }

    public void setDebt(View view) {
        if (view.getId() == R.id.isDebt) {
            isDebt = true;
            arrow.setRotation(180);
//            notDebit.setBackgroundColor(Color.argb(1,120,120,120));
//            isDebt.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            isDebt = false;
            arrow.setRotation(0);
//            isDebt.setBackgroundColor(Color.argb(1,120,120,120));
//            notDebit.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
    }

    public void saveOperation() {
        if (name.getText().toString().isEmpty() || amount.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("The fields amount and name have to be filled")
                    .setTitle("Ops, there's a problem");
            builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) { }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        Contact contact = contDAO.getByName(name.getText().toString());
        Log.wtf("Count", "saveOperation: " + contDAO.getContacts().count() );

        //Contato inexistente, criar um novo
        if ( contDAO.getByName(name.getText().toString()) == null ) {
            contact = new Contact(name.getText().toString());
            contDAO.create(contact);
        }

        Operation operation = new Operation( contact.id, desc.getText().toString(),
                whenCalendar.getTime(), isDebt,  Double.parseDouble(amount.getText().toString()) );

        opDAO.create(operation);
        finish();
    }
}
