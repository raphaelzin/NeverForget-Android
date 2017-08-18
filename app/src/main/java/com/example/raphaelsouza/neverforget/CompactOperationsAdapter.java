package com.example.raphaelsouza.neverforget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Raphael on 8/17/2017.
 */

public class CompactOperationsAdapter extends BaseAdapter {
    private final Context context;
    private SelfDAO selfDAO;
    private ContactDAO contactsDAO;
    private OperationDAO operationsDAO;


    public CompactOperationsAdapter(Context context) {
        this.context  = context;
        selfDAO       = new SelfDAO();
        contactsDAO   = new ContactDAO();
        operationsDAO = new OperationDAO();
    }

    @Override
    public int getCount() {
        return operationsDAO.getOperations().findAll().size();
    }

    @Override
    public Object getItem(int position) {
        return operationsDAO.getOperations().findAll().get(position);
    }

    @Override
    public long getItemId(int position) {
        return operationsDAO.getOperations().findAll().get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Operation operation = (Operation) getItem(position);
        Contact contact     = contactsDAO.get(operation.contactID);


        // Init views
        View rowView = inflater.inflate(R.layout.compact_operation_cell, parent, false);

        CircleImageView selfPic    = (CircleImageView) rowView.findViewById(R.id.selfPicture);
        CircleImageView contactPic = (CircleImageView) rowView.findViewById(R.id.contactPicture);

        TextView amount = (TextView) rowView.findViewById(R.id.amount);
        ImageView arrow = (ImageView) rowView.findViewById(R.id.arrow);
        // Init END

        if (selfDAO.getSelf().getImage() != null) {
            selfPic.setImageBitmap(selfDAO.getSelf().getImage());
        }

        if (contact.getImage() != null ) {
            contactPic.setImageBitmap(contact.getImage());
        }

        amount.setText("$" + operation.amount);
        amount.setText("$" + String.valueOf(operation.amount));

        if (operation.isDebt) {
            arrow.setRotation(180);
        }

        return rowView;
    }
}
