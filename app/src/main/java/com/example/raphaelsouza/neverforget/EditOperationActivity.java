package com.example.raphaelsouza.neverforget;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import static com.example.raphaelsouza.neverforget.R.id.fab;

public class EditOperationActivity extends AppCompatActivity {

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

    Operation operation;
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_operation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit");

        opDAO   = new OperationDAO();
        contDAO = new ContactDAO();
        selfDAO = new SelfDAO();

        long id = getIntent().getExtras().getLong("OperationID");
        operation = opDAO.get(id);
        contact   = contDAO.get(operation.contactID);

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

        //Setting pre-defined fields
        name.setText(contact.name);
        desc.setText(operation.details);
        amount.setText("" + operation.amount);

        whenCalendar = Calendar.getInstance();
        whenCalendar.setTime(operation.date);
        updateDateLabel();

        TextView selfName = (TextView) findViewById(R.id.selfName);
        CircleImageView selfPic = (CircleImageView) findViewById(R.id.selfPicture);
        selfName.setText(selfDAO.getSelf().getFirstName());
        if (selfDAO.getSelf().getImage() != null) {
            selfPic.setImageBitmap(selfDAO.getSelf().getImage());
        }

        contactName.setText(contact.getFirstName());
        if (contact.getImage() != null) {
            contactPic.setImageBitmap(contact.getImage());
        }

        amountCell.setText("$" + operation.amount);

        if (operation.isDebt)
            arrow.setRotation(180);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOperationEdit();
            }
        });
        
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                contactName.setText(charSequence.toString().split(" ")[0]);
                if(contDAO.getByName(charSequence.toString()) != null
                        && contDAO.getByName(charSequence.toString()).getImage() != null) {

                    contactPic.setImageBitmap(contDAO
                            .getByName(charSequence.toString()).getImage());
                } else {
                    contactPic.setImageDrawable(getDrawable(R.drawable.ic_account_circle));
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
                new DatePickerDialog(EditOperationActivity.this, date,
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
        } else {
            isDebt = false;
            arrow.setRotation(0);
        }
    }

    public void saveOperationEdit() {
        if (!name.getText().toString().isEmpty() && !amount.getText().toString().isEmpty()) {
            contact = contDAO.getByName(name.getText().toString());
//          Contato inexistente, criar um novo
            if ( contDAO.getByName(name.getText().toString()) == null ) {
                contact = new Contact(name.getText().toString());
                contDAO.create(contact);
            }
            opDAO.update(operation, desc.getText().toString(), Double.parseDouble(amount.getText()
                    .toString()), whenCalendar.getTime(), isDebt, contact.id);

        } else {
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
        finish();
    }
}
