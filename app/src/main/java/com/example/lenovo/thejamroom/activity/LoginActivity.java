package com.example.lenovo.thejamroom.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lenovo.thejamroom.R;
import com.example.lenovo.thejamroom.util.CommonsUtil;
import com.example.lenovo.thejamroom.util.UserSession;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class LoginActivity extends FragmentActivity {
    private static final String TAG = "Login Activity";
    private static final int SPLASH = 0;
    private static final int SELECTION = 1;
    private static final int SETTINGS = 2;
    private static final int FRAGMENT_COUNT = 3;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
    private boolean isResumed = false;
    private UiLifecycleHelper uiHelper;
    private MenuItem settings;

    private Session.StatusCallback callback =
            new Session.StatusCallback() {
                @Override
                public void call(Session session,
                                 SessionState state, Exception exception) {
                    onSessionStateChange(session, state, exception);
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_login);

        CommonsUtil.initLocationManager();
        CommonsUtil.calculateLocation();

        uiHelper = new UiLifecycleHelper(this, callback);
        FragmentManager fm = getSupportFragmentManager();
        fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
        fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
        fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);


        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            transaction.hide(fragments[i]);
        }
        transaction.commit();
    }

    private void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex)
                transaction.show(fragments[i]);
            else
                transaction.hide(fragments[i]);
        }
        transaction.commit();
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        //Only make changes if the activity is visible
        if (isResumed) {
            FragmentManager fm = getSupportFragmentManager();
            //Get the number of entries in the back stack
            int backStackSize = fm.getBackStackEntryCount();
            //Clear the back stack
            for (int i = 0; i < backStackSize; i++)
                fm.popBackStack();

            if (state.isOpened()) {
                UserSession.setSession(session, getApplicationContext());
                UserSession.setAccessToken(getSharedPreferences("jamroom", MODE_PRIVATE).getString("accessToken", null));
                CommonsUtil.currentLocation = (getSharedPreferences("jamroom", MODE_PRIVATE).getString("location", null));

                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
                //showFragment(SELECTION, false);
            } else if (state.isClosed())
                System.out.println("show splash");
            showFragment(SPLASH, false);
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        UserSession.setSession(Session.getActiveSession(), getApplicationContext());
        Session session = UserSession.getSession();
        if (session != null && session.isOpened()) {
            // if the session is already open,
            // try to show the selection fragment
            UserSession.setAccessToken(getSharedPreferences("jamroom", MODE_PRIVATE).getString("accessToken", null));
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
        } else {
            // otherwise present the splash screen
            // and ask the person to login.
            System.out.println("show splash");
            showFragment(SPLASH, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
        ;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //only add the menu when the selection fragment is showing
        // using this because we want to update the menu every times as opposed to on create
        if (fragments[SELECTION].isVisible()) {
            if (menu.size() == 0) {
                settings = menu.add(R.string.settings);
            }
            return true;
        } else {
            menu.clear();
            settings = null;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.equals(settings)) {
            showFragment(SETTINGS, true);
            return true;
        }

        return false;
    }

}
