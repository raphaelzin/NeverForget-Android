package com.example.raphaelsouza.neverforget;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class OperationDetailsActivity extends AppCompatActivity {

    OperationDAO operationDAO;
    ContactDAO contactDAO;
    SelfDAO selfDAO;

    Operation operation;
    Contact contact;

    TextView paidAtDetails;
    TextView descriptionDetails;
    TextView whenDetails;
    TextView amountDetails;

    TextView selfNameCell;
    TextView contactNameCell;
    TextView amountCell;

    CircleImageView selfPic;
    CircleImageView contactPic;
    ImageView arrow;

    Calendar whenCalendar;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        long id = getIntent().getExtras().getLong("OperationID");

        operationDAO = new OperationDAO();
        contactDAO = new ContactDAO();
        selfDAO = new SelfDAO();

        operation = operationDAO.get(id);
        contact = contactDAO.get(operation.contactID);

        descriptionDetails = (TextView) findViewById(R.id.descriptionDetails);
        paidAtDetails      = (TextView) findViewById(R.id.paidAtDetails);
        amountDetails      = (TextView) findViewById(R.id.amountDetails);
        whenDetails        = (TextView) findViewById(R.id.whenDetails);

        contactNameCell    = (TextView) findViewById(R.id.contactName);
        selfNameCell       = (TextView) findViewById(R.id.selfName);
        amountCell         = (TextView) findViewById(R.id.amount);


        selfPic    = (CircleImageView) findViewById(R.id.selfPicture);
        contactPic = (CircleImageView) findViewById(R.id.contactPicture);

        arrow = (ImageView) findViewById(R.id.arrow);


        descriptionDetails.setText(operation.details);
        if (operation.paidDate != null)
            paidAtDetails.setText(dateString(operation.paidDate));
        amountDetails.setText("$" + operation.amount);
        whenDetails.setText(dateString(operation.date));

        contactNameCell.setText(contact.getFirstName());
        selfNameCell.setText(selfDAO.getSelf().getFirstName());
        amountCell.setText("$" + operation.amount);

        if (selfDAO.getSelf().getImage() != null)
            selfPic.setImageBitmap(selfDAO.getSelf().getImage());
        if (contact.getImage() != null)
            contactPic.setImageBitmap(contact.getImage());

        if (operation.isDebt)
            arrow.setRotation(180);

        ToggleButton toggle = (ToggleButton) findViewById(R.id.togglePaid);
        toggle.setChecked(operation.paid);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                operationDAO.setPaid(isChecked, operation);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditActivity();
            }
        });

        whenCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                whenCalendar.set(Calendar.YEAR, year);
                whenCalendar.set(Calendar.MONTH, monthOfYear);
                whenCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updatePaidDate(whenCalendar.getTime());
                paidAtDetails.setText(dateString(operation.paidDate));
            }

        };
        paidAtDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(OperationDetailsActivity.this, date,
                        whenCalendar.get(Calendar.YEAR),
                        whenCalendar.get(Calendar.MONTH),
                        whenCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }


    public void showEditActivity() {
        Intent showDetails = new Intent(this,EditOperationActivity.class);
        showDetails.putExtra("OperationID", operation.id);
        startActivityForResult(showDetails, 1);
    }

    public String dateString(Date date) {
        String myFormat = "MMM, dd - yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        return sdf.format(date.getTime());
    }

    public void updatePaidDate(Date date)
    {

        operationDAO.setPaidDate(date, operation);
    }

}
