package com.example.lenovo.thejamroom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.thejamroom.R;


public class SelectionFragment extends Fragment {

    private static final String TAG = "SelectionFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selection,container,false);
        return view;
    }
}
