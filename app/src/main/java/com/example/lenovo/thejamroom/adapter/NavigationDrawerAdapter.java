package com.example.lenovo.thejamroom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lenovo.thejamroom.R;


/**
 * Created by Avaneesh on 24/10/2014.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<String> {

    private int mSelectedItem;
    String [] values;
    public NavigationDrawerAdapter(Context context, int resource, String[] objects){
        super(context, resource, objects);
        values = objects;
     }

    public int getSelectedItem(){
        return mSelectedItem;
    }

    public void setSelectedItem(int selectedItem){
        mSelectedItem = selectedItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView view = (TextView)super.getView(position, convertView, parent);
        view.setText(values[position]);

        if(position == mSelectedItem){
            view.setTextColor(getContext().getResources().getColor(R.color.orange));
            view.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
        }
        else{
            view.setTextColor(getContext().getResources().getColor(R.color.white));
        }
        return view;
    }
}
