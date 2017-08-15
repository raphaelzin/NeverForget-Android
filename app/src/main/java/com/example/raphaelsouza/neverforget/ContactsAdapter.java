package com.example.raphaelsouza.neverforget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by raphael on 14/08/17.
 */

public class ContactsAdapter extends BaseAdapter {
    private ContactDAO contactDAO     = new ContactDAO();
    private OperationDAO operationDAO = new OperationDAO();
    private Context context;

    public ContactsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return contactDAO.getContacts().findAll().size();
    }

    @Override
    public Object getItem(int position) {
        return contactDAO.getContacts().findAll().get(position);
    }

    @Override
    public long getItemId(int position) {
        return contactDAO.getContacts().findAll().get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = (Contact) getItem(position);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.contact_cell, parent, false);

        CircleImageView contactPic = (CircleImageView) rowView.findViewById(R.id.contactPic);

        TextView name        = (TextView) rowView.findViewById(R.id.contactName);
        TextView totalDebt   = (TextView) rowView.findViewById(R.id.totalDebt);
        TextView totalCredit = (TextView) rowView.findViewById(R.id.totalCredit);

        name.setText(contact.name);

        if (contact.getImage() != null) {
            contactPic.setImageBitmap(contact.getImage());
        } else {
            contactPic.setImageResource(R.drawable.ic_account_circle);
        }

        totalDebt.setText("$" + operationDAO.operationsWith(contact.id,true));
        totalCredit.setText("$" + operationDAO.operationsWith(contact.id,false));

        return rowView;
    }
}
