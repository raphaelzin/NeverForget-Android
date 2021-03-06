package com.example.raphaelsouza.neverforget;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.graphics.BitmapCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Sort;

/**
 * Created by Raphael Souza on 17-08-10.
 */

public class FullOperationsAdapter extends BaseAdapter {
    private final Context context;
    private SelfDAO selfDAO;
    private ContactDAO contactsDAO;
    private OperationDAO operationsDAO;


    public FullOperationsAdapter(Context context) {
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
        return operationsDAO.getOperations().findAllSorted("paid", Sort.ASCENDING).get(position);
    }

    @Override
    public long getItemId(int position) {
        return operationsDAO.getOperations().findAllSorted("paid", Sort.ASCENDING).get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Operation operation = (Operation) getItem(position);





        // Init views
        View rowView = inflater.inflate(R.layout.full_operation_cell, parent, false);

        CircleImageView selfPic    = (CircleImageView) rowView.findViewById(R.id.selfPicture);
        CircleImageView contactPic = (CircleImageView) rowView.findViewById(R.id.contactPicture);

        TextView selfName = (TextView) rowView.findViewById(R.id.selfName);
        TextView contactName = (TextView) rowView.findViewById(R.id.contactName);

        TextView amount = (TextView) rowView.findViewById(R.id.amount);
        ImageView arrow = (ImageView) rowView.findViewById(R.id.arrow);
        // Init END


        if (contactsDAO.get(operation.contactID) != null) {
            Contact contact     = contactsDAO.get(operation.contactID);
            contactName.setText( contact.getFirstName() );
            if (contact.getImage() != null ) {
                contactPic.setImageBitmap(contact.getImage());
                Log.wtf(contact.getFirstName() + "Image size:",""+ BitmapCompat.getAllocationByteCount(Utils.compressBitmap(contact.getImage())) );
            }
        } else {
            contactName.setText( Resources.getSystem().getString(R.string.someone) );
        }


        if (selfDAO.getSelf().getImage() != null)
            selfPic.setImageBitmap(selfDAO.getSelf().getImage());
        selfName.setText(selfDAO.getSelf().getFirstName());


        amount.setText(Utils.currency(operation.amount));
        amount.setText(Utils.currency(operation.amount));

//        GradientDrawable bgShape = (GradientDrawable)arrow.getBackground();
        if (operation.isDebt) {
            arrow.setRotation(180);
            arrow.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_red));
        } else {
            arrow.setImageDrawable(context.getDrawable(R.drawable.ic_arrow));
        }




        rowView.setAlpha( (operation.paid) ? 0.5f : 1f);

        return rowView;
    }
}
