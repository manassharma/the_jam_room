package com.example.lenovo.thejamroom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.thejamroom.R;
import com.facebook.widget.LoginButton;

import java.util.Arrays;

/**
 * Created by kulvir on 10/21/2014.
 */
public class SplashFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash,container, false);
        LoginButton btn = (LoginButton)view.findViewById(R.id.authButton);
        btn.setReadPermissions(Arrays.asList("email"));

        return view;
    }
}
