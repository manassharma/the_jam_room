package com.example.lenovo.thejamroom.util;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;

import com.example.lenovo.thejamroom.R;
import com.example.lenovo.thejamroom.activity.MusicPlayerActivity;
import com.example.lenovo.thejamroom.fragment.PlaceholderFragment;

/**
 * Created by avaneeshdesai on 11/29/14.
 */
public class FragmentUtil {
    public static Context ApplicationContext;
    static MusicPlayerActivity mMusicPlayerFragment;

    public static MusicPlayerActivity getMusicPlayerFragment(){
        return mMusicPlayerFragment;
    }

    public static void hideMusicPlayerFragment(Activity activity){
        if(mMusicPlayerFragment != null) {
            FragmentManager fm = activity.getFragmentManager();
            fm.beginTransaction()
                    .hide(mMusicPlayerFragment)
                    .commit();
        }
    }

    public static void showMusicPlayerFragment(Activity activity){
        if(mMusicPlayerFragment != null) {
            FragmentManager fm = activity.getFragmentManager();
            fm.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .show(mMusicPlayerFragment)
                    .commit();
        }
    }

    public static void createMusicPlayerFragment(Activity activity, int containerID){
        FragmentManager fragmentManager = activity.getFragmentManager();
        mMusicPlayerFragment = new MusicPlayerActivity();
        fragmentManager.beginTransaction().replace(R.id.musicPlayerContainer, mMusicPlayerFragment).commit();
    }


    public static void removeMusicPlayerFragment(Activity activity){
        if(mMusicPlayerFragment != null) {
            FragmentManager fragmentManager = activity.getFragmentManager();
            mMusicPlayerFragment = new MusicPlayerActivity();
            fragmentManager.beginTransaction().remove(mMusicPlayerFragment).commit();
        }
    }



}
