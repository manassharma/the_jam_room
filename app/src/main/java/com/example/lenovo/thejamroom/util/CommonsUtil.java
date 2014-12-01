package com.example.lenovo.thejamroom.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.example.lenovo.thejamroom.pojo.Song;
import com.example.lenovo.thejamroom.service.MusicPlayerService;

import java.util.ArrayList;

/**
 * Created by avaneeshdesai on 11/28/14.
 */
public class CommonsUtil {
    static String TAG = "CommonUtil";

    //ApplicationUtil
    public static Context ApplicationContext;

    //MusicPlayerUtil
    public static int currentSongPos = 0;
    public static ArrayList<Song> songsList;
    public static boolean mBound;
    public static MusicPlayerService mService;

    //Location Util
    public static String currentLocation;
    public static LocationManager locationManager;

    public static int getCurrentSongPos() {
        return currentSongPos;
    }

    public static void setCurrentSongPos(int currentSongPos) {
        CommonsUtil.currentSongPos = currentSongPos;
    }

    public static MusicPlayerService getMusicPlayerService() {
        return mService;
    }
    public static boolean getMBound() {
        return mBound;
    }


    public static void initLocationManager(){
        Log.d(TAG, "Initializing Location Manager");
        locationManager = (LocationManager) ApplicationContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public static void calculateLocation(){
        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        if(location!=null)
            setCurrentLocation(location);
        else
            Log.d(TAG,"location is null");
    }

    public static void setCurrentLocation(Location location){
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        String locn = lat +"," + lng;
        currentLocation = locn;
    }



}
