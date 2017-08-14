package com.example.raphaelsouza.neverforget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    SelfDAO selfDAO;
    OperationDAO operationDAO;
    TextView credit;
    TextView debt;
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        selfDAO = new SelfDAO();
        operationDAO = new OperationDAO();

        credit = (TextView) findViewById(R.id.credit);
        total  = (TextView) findViewById(R.id.total);
        debt   = (TextView) findViewById(R.id.debt);

        credit.setText("$" + operationDAO.getTotal(false));
        debt.setText("$" + operationDAO.getTotal(true));
        total.setText("$" + (operationDAO.getTotal(false) - operationDAO.getTotal(true)));

        ImageButton pickImage = (ImageButton) findViewById(R.id.pickImage);
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickProfilePic();
            }
        });
    }

    public void pickProfilePic() {
        //TODO implement Picture Picker
        Log.wtf("Tag", "pickProfilePic: will pick pic" );
    }

    public void editName(View view) {
        //TODO dialog with EditText
        Log.wtf("Tag", "pickProfilePic: will pick pic" );
    }
}
