package com.example.raphaelsouza.neverforget;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    TextView overview;

    TextView selfNameCell;
    TextView contactNameCell;
    TextView amountCell;

    CircleImageView selfPic;
    CircleImageView contactPic;
    ImageView arrow;

    Calendar whenCalendar;
    DatePickerDialog.OnDateSetListener date;

    ToggleButton toggle;
    LinearLayout paidAtRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.details));

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        long id = getIntent().getExtras().getLong("OperationID");

        operationDAO = new OperationDAO();
        contactDAO   = new ContactDAO();
        selfDAO      = new SelfDAO();

        operation = operationDAO.get(id);
        contact = contactDAO.get(operation.contactID);

        descriptionDetails = (TextView) findViewById(R.id.descriptionDetails);
        paidAtDetails      = (TextView) findViewById(R.id.paidAtDetails);
        amountDetails      = (TextView) findViewById(R.id.amountDetails);
        whenDetails        = (TextView) findViewById(R.id.whenDetails);
        overview           = (TextView) findViewById(R.id.overview);

        contactNameCell    = (TextView) findViewById(R.id.contactName);
        selfNameCell       = (TextView) findViewById(R.id.selfName);
        amountCell         = (TextView) findViewById(R.id.amount);


        selfPic    = (CircleImageView) findViewById(R.id.selfPicture);
        contactPic = (CircleImageView) findViewById(R.id.contactPicture);

        arrow = (ImageView) findViewById(R.id.arrow);
        toggle = (ToggleButton) findViewById(R.id.togglePaid);
        paidAtRow = (LinearLayout) findViewById(R.id.paidAtRow);

        setupView();


        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                operationDAO.setPaid(isChecked, operation);
                paidAtRow.setVisibility((operation.paid) ? View.VISIBLE : View.INVISIBLE);

                SpannableString content = new SpannableString((operation.paidDate != null) ?
                        dateString(operation.paidDate): getString(R.string.set_paid_date));
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                paidAtDetails.setText(content);
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
                SpannableString content = new SpannableString(dateString(operation.paidDate));
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                paidAtDetails.setText(content);
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

    public void setupView() {
        contact = contactDAO.get(operation.contactID);
        descriptionDetails.setText(operation.details);
        if (operation.paid) {
            String paidAtLabel;
            if (operation.paidDate != null)
                paidAtLabel = dateString(operation.paidDate);
            else
                paidAtLabel = getString(R.string.set_paid_date);
            SpannableString paid = new SpannableString(paidAtLabel);
            paid.setSpan(new UnderlineSpan(), 0, paid.length(), 0);
            paidAtDetails.setText(paid);
        }


        amountDetails.setText(Utils.currency(operation.amount));
        whenDetails.setText(dateString(operation.date));


        selfNameCell.setText(selfDAO.getSelf().getFirstName());
        amountCell.setText(Utils.currency(operation.amount));

        if (selfDAO.getSelf().getImage() != null)
            selfPic.setImageBitmap(selfDAO.getSelf().getImage());

        if (operation.isDebt) {
            arrow.setRotation(180);
            arrow.setImageDrawable(this.getDrawable(R.drawable.ic_arrow_red));
            overview.setText(String.format(getResources().getString(R.string.debt_overview),
                    contact.getFirstName(), Utils.currency(operation.amount)));
        } else {
            arrow.setRotation(0);
            arrow.setImageDrawable(this.getDrawable(R.drawable.ic_arrow));
            overview.setText(String.format(getResources().getString(R.string.credit_overview),
                    contact.getFirstName(), Utils.currency(operation.amount)));
        }

        if (contact != null) {
            contactNameCell.setText(contact.getFirstName());
            if (contact.getImage() != null)
                contactPic.setImageBitmap(contact.getImage());
        }


        toggle.setChecked(operation.paid);
        paidAtRow.setVisibility((operation.paid) ? View.VISIBLE : View.INVISIBLE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setupView();
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
