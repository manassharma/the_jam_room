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
import com.example.lenovo.thejamroom.util.UserSession;
import com.facebook.widget.ProfilePictureView;


/**
 * Created by Avaneesh on 24/10/2014.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<String> {

    private int mSelectedItem;
    private final Context context;
    private static LayoutInflater inflater=null;
    String [] values;
    Integer [] images;
    ProfilePictureView profilePictureView =null ;
    TextView txtName = null;
    TextView txtEmail = null;
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
        View rowView = null;
       if(position == 0){
           inflater = ((Activity) context).getLayoutInflater();
           rowView = inflater.inflate(R.layout.navgation_list_profile_item, null, true);
           profilePictureView = (ProfilePictureView) rowView.findViewById(R.id.selection_profile_pic);
           profilePictureView.setCropped(true);

           profilePictureView.setProfileId(UserSession.getUserId());

           txtName = (TextView)rowView.findViewById(R.id.txtName);
           txtName.setText(UserSession.getName());

           txtEmail = (TextView)rowView.findViewById(R.id.txtEmail);
           txtEmail.setText(UserSession.getUsername());

       }
        else {
           inflater = ((Activity) context).getLayoutInflater();
           rowView = inflater.inflate(R.layout.navigation_list_item, null, true);

           TextView label = (TextView) rowView.findViewById(R.id.navItemLabel);
           ImageView imageView = (ImageView) rowView.findViewById(R.id.navIcon);

           label.setText(values[position]);
           imageView.setImageDrawable(context.getResources().getDrawable(images[position]));

           if (position == mSelectedItem+1) {
               label.setTextColor(getContext().getResources().getColor(R.color.orange));
               label.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
               rowView.setBackgroundColor(getContext().getResources().getColor(R.color.black_overlay));
           } else {
               label.setTextColor(getContext().getResources().getColor(R.color.white));
           }

       }
        return rowView;
    }

}
