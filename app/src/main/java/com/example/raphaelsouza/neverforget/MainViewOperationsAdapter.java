package com.example.raphaelsouza.neverforget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Raphael Souza on 17-08-10.
 */

public class MainViewOperationsAdapter extends ArrayAdapter {
    private final Context context;
    private final String[] values;

    public MainViewOperationsAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.full_operation_cell, parent, false);

        CircleImageView selfPic    = (CircleImageView) rowView.findViewById(R.id.selfPicture);
        CircleImageView contactPic = (CircleImageView) rowView.findViewById(R.id.contactPicture);

//        TextView selfName = (TextView) rowView.findViewById(R.id.)


        return rowView;
    }
}
