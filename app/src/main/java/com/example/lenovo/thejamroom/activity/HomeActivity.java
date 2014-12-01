package com.example.lenovo.thejamroom.activity;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.lenovo.thejamroom.R;
import com.example.lenovo.thejamroom.fragment.NavigationDrawerFragment;
import com.example.lenovo.thejamroom.fragment.PlaceholderFragment;
import com.example.lenovo.thejamroom.service.MusicPlayerService;
import com.example.lenovo.thejamroom.util.CommonsUtil;
import com.example.lenovo.thejamroom.util.FragmentUtil;
import com.facebook.Session;

public class HomeActivity extends FragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    static final public String BROADCAST_RESULT = "com.example.lenovo.thejamroom.BROADCASE_RESULT";
    public ImageButton playerButton;
    Intent serviceIntent;
    BroadcastReceiver receiver;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private MusicPlayerService mService;
    private boolean mBound;
    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder serviceBinder) {
            Log.d("MainActivity", "service connected");

            //bound with Service. get Service instance
            MusicPlayerService.MediaPlayerBinder binder = (MusicPlayerService.MediaPlayerBinder) serviceBinder;
            mService = binder.getService();

            //send this instance to the service, so it can make callbacks on this instance as a client
            //mService.setClient(MainActivity.this);
            mBound = true;
            CommonsUtil.mBound = mBound;
            CommonsUtil.mService = mService;

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            CommonsUtil.mBound = mBound;
            CommonsUtil.mService = mService;

        }
    };

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CommonsUtil.initLocationManager();
        CommonsUtil.calculateLocation();

        getActionBar().show();
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);

        setUpNavigationDrawer();
        initializeButtons();
        setUpBroadcastReceiver();

        bindToService();
    }

    private void initializeButtons() {
        playerButton = (ImageButton) findViewById(R.id.btnPlayer);
        playerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.showMusicPlayerFragment(HomeActivity.this);
                playerButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setUpNavigationDrawer() {
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private void setUpBroadcastReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("receiving message broadcast");
                String rec = intent.getStringExtra("MUSIC_PLAYER_STATUS");
                MusicPlayerActivity m = (MusicPlayerActivity) getFragmentManager().findFragmentById(R.id.musicPlayerContainer);
                if (rec.equals("PLAYER_INIT")) {
                    m.seekUpdation(true);
                    m.updateButtons("play");
                    FragmentUtil.showMusicPlayerFragment(HomeActivity.this);
                } else if (rec.equals("PLAYER_PAUSED")) {
                    m.updateButtons("pause");
                    FragmentUtil.hideMusicPlayerFragment(HomeActivity.this);
                    playerButton.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentUtil.createMusicPlayerFragment(this, R.id.musicPlayerContainer);

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

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            System.out.println("in-if");
            super.onBackPressed();
        } else {
            System.out.println("in-else");
            getFragmentManager().popBackStack();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  mService.onDestroy();
        //  stopService(serviceIntent);
        //  unbindService(mConnection);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(BROADCAST_RESULT));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    /**
     * Binds to the instance of MediaPlayerService. If no instance of MediaPlayerService exists, it first starts
     * a new instance of the service.
     */
    public void bindToService() {
        serviceIntent = new Intent(getApplicationContext(), MusicPlayerService.class);

        if (MediaPlayerServiceRunning()) {
            // Bind to LocalService
            bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
        } else {
            startService(serviceIntent);
            bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
        }

    }

    /**
     * Determines if the MediaPlayerService is already running.
     *
     * @return true if the service is running, false otherwise.
     */
    private boolean MediaPlayerServiceRunning() {

        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.example.lenovo.thejamroom.MediaPlayerService".equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

}
