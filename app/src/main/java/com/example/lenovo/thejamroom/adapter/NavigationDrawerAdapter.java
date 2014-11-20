package com.example.lenovo.thejamroom.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.thejamroom.R;


/**
 * Created by Avaneesh on 24/10/2014.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<String> {

    private int mSelectedItem;
    private final Context context;
    private static LayoutInflater inflater=null;
    String [] values;
    Integer [] images;
    public NavigationDrawerAdapter(Context context, int resource, String[] objects, Integer[] images){
        super(context, resource, objects);
        values = objects;
        this.images = images;
        this.context = context;
     }

    public int getSelectedItem(){
        return mSelectedItem;
    }

    public void setSelectedItem(int selectedItem){
        mSelectedItem = selectedItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = ((Activity)context).getLayoutInflater();
        View rowView = null;
        rowView = inflater.inflate(R.layout.navigation_list_item, null, true);

        TextView label = (TextView)rowView.findViewById(R.id.navItemLabel);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.navIcon);

        label.setText(values[position]);
        imageView.setImageDrawable(context.getResources().getDrawable(images[position]));

        if(position == mSelectedItem){
            label.setTextColor(getContext().getResources().getColor(R.color.orange));
            label.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
            rowView.setBackgroundColor(getContext().getResources().getColor(R.color.black_overlay));
        }
        else{
            label.setTextColor(getContext().getResources().getColor(R.color.white));
        }
        return rowView;
    }
}
