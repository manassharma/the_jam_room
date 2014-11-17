package com.example.lenovo.thejamroom;

import com.example.lenovo.thejamroom.pojo.Song;
import com.example.lenovo.thejamroom.util.SystemUiHider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */

class Mp3Filter implements FilenameFilter
{
    public boolean accept(File dir, String name)
    {
        return (name.endsWith(".mp3"));
    }
}
public class FullscreenActivity extends Activity {

    private static final String SD_PATH = new String("/sdcard/");
    private List<String> songs = new ArrayList<String>();
    private MediaPlayer mp = new MediaPlayer();
    Cursor c;
    ListView listView;

    public void onCreate(Bundle savedInstanceState){
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_fullscreen);
           listView = (ListView)findViewById(R.id.listView);
           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Song currentSong = (Song)parent.getAdapter().getItem(position);

                mp.reset();
                try
                {
                    System.out.println(currentSong.getPath());
                    mp.setDataSource(currentSong.getPath());
                    mp.prepare();
                    mp.start();
                }
                catch(IOException e)
                {
                    Log.v(getString(R.string.app_name), e.getMessage());
                }
            }
        });
           updatePlayList();


           Button stopPlay = (Button) findViewById(R.id.stopbtn);
           stopPlay.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                 mp.stop();
               }
           });
       }

      private void updatePlayList()
    {   c = getTrackTrackcursor( getApplicationContext());
        System.out.println("Count" + c.getCount());

        String[] from = { MediaStore.MediaColumns.TITLE};
        int[] to = {android.R.id.text1};

        ArrayAdapter<String> songList = new ArrayAdapter<String>(this, R.layout.song_item, songs);
        listView.setAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, from, to));
    }

    public Cursor getTrackTrackcursor(Context context)
    {
        final String track_id = MediaStore.Audio.Media._ID;
        final String track_no =MediaStore.Audio.Media.TRACK;
        final String track_name =MediaStore.Audio.Media.TITLE;
        final String artist = MediaStore.Audio.Media.ARTIST;
        final String duration = MediaStore.Audio.Media.DURATION;
        final String album = MediaStore.Audio.Media.ALBUM;
        final String composer = MediaStore.Audio.Media.COMPOSER;
        final String year = MediaStore.Audio.Media.YEAR;
        final String path = MediaStore.Audio.Media.DATA;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        ContentResolver cr =  context.getContentResolver();
        final String[]columns={track_id, track_no, artist, track_name,album, duration, path, year, composer};
        Cursor cursor = cr.query(uri,columns,null,null,null);
        return cursor;
    }
}