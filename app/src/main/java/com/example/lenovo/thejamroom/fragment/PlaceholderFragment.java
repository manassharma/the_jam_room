package com.example.lenovo.thejamroom.fragment;

/**
 * Created by avaneeshdesai on 11/29/14.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lenovo.thejamroom.Constants;
import com.example.lenovo.thejamroom.R;
import com.example.lenovo.thejamroom.activity.HomeActivity;
import com.example.lenovo.thejamroom.activity.MusicPlayerActivity;
import com.example.lenovo.thejamroom.adapter.SongsListAdapter;
import com.example.lenovo.thejamroom.async.CallAPI;
import com.example.lenovo.thejamroom.pojo.Song;
import com.example.lenovo.thejamroom.util.CommonsUtil;
import com.example.lenovo.thejamroom.util.FragmentUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    static Cursor c;
    static MusicPlayerActivity musicPlayerFragment;
    static JSONArray arr;
    static String currentLocation;
    public static ArrayList<Song> songslist;
    static SwipeRefreshLayout mSwipeRefreshLayout;
    static boolean refresh = false;
    ListView songsListView;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        String result = "";

        currentLocation = CommonsUtil.currentLocation;
        try {
            CallAPI api = new CallAPI();
            String uri[] = {Constants.SERVER_URL + Constants.SONGS_URL  };
            api.execute(uri);
            result = api.get();
            arr = new JSONArray(result);
            songslist = new ArrayList<Song>();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        musicPlayerFragment = FragmentUtil.getMusicPlayerFragment();
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        FragmentManager fm = getActivity().getFragmentManager();

        mSwipeRefreshLayout =(SwipeRefreshLayout)rootView.findViewById(R.id.songsSwipeRefresh);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.blue, R.color.orange, R.color.white, R.color.orange);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("Timeline Activity", "onRefresh called from SwipeRefreshLayout");
                if(!refresh) {
                    refresh = true;
                    initiateRefresh();
                }
            }
        });

        FragmentUtil.hideMusicPlayerFragment(getActivity());
        try {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jsonProductObject = arr.getJSONObject(i);
                Song song = new Song();
                song.setName(jsonProductObject.getString("name"));
                song.setArtist(jsonProductObject.getString("artist"));
                song.setGenre(jsonProductObject.getString("genere"));
                song.setPath(jsonProductObject.getString("path"));
                songslist.add(song);
            }
            CommonsUtil.songsList = songslist;
            songsListView = (ListView)rootView.findViewById(R.id.songsList);
            songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    CommonsUtil.setCurrentSongPos(position);
                    FragmentManager fm = getFragmentManager();
                    FragmentUtil.showMusicPlayerFragment(getActivity());
                    ((HomeActivity)getActivity()).playerButton.setVisibility(View.INVISIBLE);
                    musicPlayerFragment.playCurrent();
                }
            });
            SongsListAdapter adapter = new SongsListAdapter(getActivity(),songslist);
            songsListView.setAdapter(adapter);

            musicPlayerFragment.populateSongDetails();

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


    private void initiateRefresh(){
        String result = "";
        songslist.clear();
        CommonsUtil.calculateLocation();
        currentLocation = CommonsUtil.currentLocation;
        try {
            CallAPI api = new CallAPI();
            String uri[] = {Constants.SERVER_URL + Constants.LOCATION_URL+ currentLocation };
            api.execute(uri);
            result = api.get();
            arr = new JSONArray(result);
            songslist = new ArrayList<Song>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jsonProductObject = arr.getJSONObject(i);
                Song song = new Song();
                song.setName(jsonProductObject.getString("name"));
                song.setArtist(jsonProductObject.getString("artist"));
                song.setGenre(jsonProductObject.getString("genere"));
                song.setPath(jsonProductObject.getString("path"));
                songslist.add(song);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        SongsListAdapter adapter = new SongsListAdapter(getActivity(),songslist);
        songsListView.setAdapter(adapter);
        CommonsUtil.songsList = songslist;
       mSwipeRefreshLayout.setRefreshing(false);
        refresh = false;
    }
}