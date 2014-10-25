package com.example.lenovo.thejamroom;

import com.example.lenovo.thejamroom.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
public class FullscreenActivity extends ListActivity {

    private static final String SD_PATH = new String("/sdcard/");
    private List<String> songs = new ArrayList<String>();
    private MediaPlayer mp = new MediaPlayer();

       public void onCreate(Bundle savedInstanceState){
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_fullscreen);
           updatePlayList();

           Button stopPlay = (Button) findViewById(R.id.stopbtn);
           stopPlay.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                mp.stop();
               }
           });
       }

    protected  void  onListItemClick(ListView list, View view, int position, long id)
    {
        try {
            mp.reset();
            mp.setDataSource(SD_PATH + songs.get(position));
            mp.prepare();
            mp.start();
        }
        catch(IOException e)
        {
            Log.v(getString(R.string.app_name), e.getMessage());
        }
    }
    private void updatePlayList()
    {
        File home = new File(SD_PATH);
        if(home.listFiles(new Mp3Filter()).length>0)
        {
            for(File file: home.listFiles(new Mp3Filter()))
            {
                songs.add(file.getName());
            }

            ArrayAdapter<String> songList = new ArrayAdapter<String>(this, R.layout.song_item,songs);
            setListAdapter(songList);
        }
    }
}