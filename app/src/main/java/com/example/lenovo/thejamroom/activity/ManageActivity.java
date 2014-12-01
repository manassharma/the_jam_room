package com.example.lenovo.thejamroom.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.lenovo.thejamroom.Constants;
import com.example.lenovo.thejamroom.R;
import com.example.lenovo.thejamroom.adapter.SongsListAdapter;
import com.example.lenovo.thejamroom.async.CallAPI;
import com.example.lenovo.thejamroom.pojo.Song;
import com.example.lenovo.thejamroom.util.CommonsUtil;
import com.example.lenovo.thejamroom.util.UserSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ManageActivity extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    static JSONArray arr;
    public static ArrayList<Song> songslist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        String result = "";
        CommonsUtil.calculateLocation();

        try {
            CallAPI api = new CallAPI();
            String uri[] = {Constants.SERVER_URL + Constants.MANAGE_URL };
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


        final View view = inflater.inflate(R.layout.fragment_manage, container, false);
        ListView manageList = (ListView) view.findViewById(R.id.manageSongsList);

        SongsListAdapter adapter = new SongsListAdapter(getActivity(),songslist);
        manageList.setAdapter(adapter);
        return view;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
