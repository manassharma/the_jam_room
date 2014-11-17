package com.example.lenovo.thejamroom.activity;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.lenovo.thejamroom.Constants;
import com.example.lenovo.thejamroom.R;
import com.example.lenovo.thejamroom.pojo.Song;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerActivity extends Fragment {
    boolean isPlaying = true;
    MediaPlayer mp;
    SeekBar bar;
    Handler seekHandler = new Handler();
    ArrayList<Song> songsList;
    int currentSongPos = 0;
    TextView txtCurrentTime;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        final View view = inflater.inflate(R.layout.activity_music_player, container, false);

        songsList = ((HomeActivity) getActivity()).getSongs();
        currentSongPos = ((HomeActivity) getActivity()).getCurrentPosition();
        mp = new MediaPlayer();
        createMediaPlayer(view);
        ImageButton btnPlayPause = (ImageButton)view.findViewById(R.id.btnPlayPause);
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePlayPause();
            }
        });

        ImageButton btnNext = (ImageButton)view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
        ImageButton btnPrev = (ImageButton)view.findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
            }
        });
        ImageButton btnShuffle = (ImageButton)view.findViewById(R.id.btnShuffle);
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffle();
            }
        });

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                 if(fromUser){
                     mp.seekTo(i);
                 }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        v = view;
        return view;
    }

    public void createMediaPlayer(View view){
        Song  currentSong= songsList.get(currentSongPos);

        bar = (SeekBar)view.findViewById(R.id.seekBar);
        TextView txtDuration = (TextView)view.findViewById(R.id.txtDuration);
        TextView txtSongName = (TextView)view.findViewById(R.id.txtSongName);
        TextView txtSongArtist = (TextView)view.findViewById(R.id.txtSongArtist);
        txtCurrentTime = (TextView)view.findViewById(R.id.txtCurrentTime);
        txtSongName.setText(currentSong.getName());
        txtSongArtist.setText(currentSong.getArtist());
        mp.reset();
        try
        {
            System.out.println(currentSong.getPath());
            mp.setDataSource(Constants.SERVER_URL + currentSong.getPath());
            mp.prepare();

            if(isPlaying) {
                mp.start();
            }
            int millis = mp.getDuration();
            bar.setMax(millis);
            String time = convertToMinutes(millis);
            System.out.println("duration" + millis);
            txtDuration.setText(time);
            seekUpdation();

        }
        catch(IOException e)
        {
            Log.v(getString(R.string.app_name), e.getMessage());
        }
    }

    Runnable run = new Runnable() {
        @Override public void run() {
            int currentPos = mp.getCurrentPosition();
            if(currentPos < mp.getDuration()){
                String time = convertToMinutes(currentPos);
                txtCurrentTime.setText(time);
                bar.setProgress(mp.getCurrentPosition());
                seekUpdation();
            }
            else if(currentPos >= mp.getDuration()-1200){
                next();
            }

        }
    };

    public String convertToMinutes(int millis){
        int sec = (millis / 1000) % 60 ;
        int min = ((millis / (1000*60)) % 60);
        String minutes= "";
        String seconds= "";

        if(sec == 0) seconds = "00";
        else if(sec < 10) seconds = "0"+sec;
        else seconds = Integer.toString(sec);

        if(min == 0) minutes = "00";
        else if(min < 10) minutes = "0"+min;
        else minutes = Integer.toString(min);

        return minutes+":"+seconds;
    }

    public void seekUpdation() {
        seekHandler.postDelayed(run, 1000);

    }

    public void togglePlayPause(){
        if(!isPlaying){
            mp.start();
            isPlaying = true;
            ((ImageButton)getActivity().findViewById(R.id.btnPlayPause)).setImageDrawable(getResources().getDrawable(R.drawable.pause));
        }
        else{
            mp.pause();
            isPlaying = false;
            ((ImageButton)getActivity().findViewById(R.id.btnPlayPause)).setImageDrawable(getResources().getDrawable(R.drawable.play_rnd));
        }
    }


    public void next(){
        if(currentSongPos < songsList.size()-1){
            mp.stop();
            currentSongPos++;
            createMediaPlayer(v);
        }
        else{
            currentSongPos = -1;
            next();
        }
    }

    public void prev(){
        if(currentSongPos >= 1){
            mp.stop();
            currentSongPos--;
            createMediaPlayer(v);
        }
        else{
            currentSongPos = songsList.size();
            prev();
        }
    }

    public void shuffle(){
         mp.stop();

         currentSongPos = randomWithRange(0, songsList.size()-1);
         createMediaPlayer(v);

    }

    int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
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
