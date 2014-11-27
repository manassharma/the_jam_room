package com.example.lenovo.thejamroom.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

import com.example.lenovo.thejamroom.Constants;
import com.example.lenovo.thejamroom.R;
import com.example.lenovo.thejamroom.adapter.SongsListAdapter;
import com.example.lenovo.thejamroom.async.CallAPI;
import com.example.lenovo.thejamroom.fragment.NavigationDrawerFragment;
import com.example.lenovo.thejamroom.pojo.Song;
import com.facebook.Session;

public class HomeActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,LocationListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private MusicPlayerActivity musicPlayerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public static ArrayList<Song> songslist;
    public static int currentPosition;

    LocationManager locationManager;
    String provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getActionBar().show();
        getActionBar().setDisplayShowTitleEnabled(false);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        musicPlayerFragment = new MusicPlayerActivity();

        getActionBar().setDisplayShowTitleEnabled(false);


        //Location Service
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default

        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            Toast.makeText(this, "Location Not available!!", Toast.LENGTH_SHORT);
        }
    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location loc) {
        double lat = loc.getLatitude();
        double lng = loc.getLongitude();
        String txt = lat +"  ,  " + lng;
        System.out.println("LOCATION: "+ txt);

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_home);
                break;
            case 2:
                mTitle = getString(R.string.title_browse);
                break;
            case 3:
                mTitle = getString(R.string.title_playlist);
                break;
            case 4:
                mTitle = getString(R.string.title_create);
                break;
            case 5:
                mTitle = getString(R.string.title_settings);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            callFacebookLogout(getApplicationContext());
            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Song> getSongs(){
        return songslist;
    }

    public int getCurrentPosition(){
        return currentPosition;
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            System.out.println("in-if");
            super.onBackPressed();
            System.exit(0);
        } else {
            System.out.println("in-else");
            getFragmentManager().popBackStack();

        }
    }

    public static void callFacebookLogout(Context context) {
        Session session = Session.getActiveSession();
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                //clear your preferences if saved
            }
        } else {

            session = new Session(context);
            Session.setActiveSession(session);

            session.closeAndClearTokenInformation();
            //clear your preferences if saved

        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        static Cursor c;
        MusicPlayerActivity musicPlayerFragment;
        MediaPlayer mp = new MediaPlayer();

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);


            String result ="";
            try {
                CallAPI api = new CallAPI();
                String uri[] = {Constants.SERVER_URL + Constants.SONGS_URL};
                api.execute(uri);
                result = api.get();
                System.out.println("result" + result);
                songslist = new ArrayList<Song>();

                JSONArray arr = new JSONArray(result);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonProductObject = arr.getJSONObject(i);
                    Song song = new Song();
                    song.setName(jsonProductObject.getString("name"));
                    song.setArtist(jsonProductObject.getString("artist"));
                    song.setGenre(jsonProductObject.getString("genere"));
                    song.setLocation(jsonProductObject.getString("location"));
                    song.setPath(jsonProductObject.getString("path"));

                    songslist.add(song);
                    ListView songsListView = (ListView)rootView.findViewById(R.id.songsList);
                    songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                            currentPosition = position;
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction ft = fragmentManager.beginTransaction();
                            ft.replace(R.id.container, new MusicPlayerActivity());
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    });
                    SongsListAdapter adapter = new SongsListAdapter(getActivity(),songslist);
                    songsListView.setAdapter(adapter);
                }

            }catch(Exception e){
                e.printStackTrace();
            }

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HomeActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
