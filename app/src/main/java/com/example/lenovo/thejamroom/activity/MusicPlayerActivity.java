package com.example.lenovo.thejamroom.activity;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.lenovo.thejamroom.service.MusicPlayerService;
import com.example.lenovo.thejamroom.util.CommonsUtil;
import com.example.lenovo.thejamroom.util.FragmentUtil;


public class MusicPlayerActivity extends Fragment {

    MediaPlayer mMediaPlayer;
    Runnable run = new Runnable() {
        @Override
        public void run() {
            if (CommonsUtil.mService.getMediaPlayer() != null && CommonsUtil.mService.isPlaying() && CommonsUtil.mService.isCreated()) {
                mMediaPlayer = CommonsUtil.mService.getMediaPlayer();
                int currentPos = mMediaPlayer.getCurrentPosition();
                if (currentPos < mMediaPlayer.getDuration()) {
                    bar.setProgress(mMediaPlayer.getCurrentPosition());
                    seekUpdation(false);
                } else if (currentPos >= mMediaPlayer.getDuration()) {
                    next();
                }
            }
        }
    };
    SeekBar bar;
    Handler seekHandler = new Handler();
    View v;
    private MusicPlayerService mService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.activity_music_player, container, false);


        ImageButton btnPlayPause = (ImageButton) view.findViewById(R.id.btnPlayPause);
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePlayPause();
            }
        });

        ImageButton btnNext = (ImageButton) view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
        ImageButton btnPrev = (ImageButton) view.findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
            }
        });
        ImageButton btnClose = (ImageButton) view.findViewById(R.id.closeBtn);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.hideMusicPlayerFragment(getActivity());
                ((HomeActivity)getActivity()).playerButton.setVisibility(View.VISIBLE);
            }
        });

        final ImageButton btnStar = (ImageButton) view.findViewById(R.id.btnStar);
        btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStar.setImageDrawable(getResources().getDrawable(R.drawable.star_filled));
            }
        });

  /*      ImageButton btnShuffle = (ImageButton)view.findViewById(R.id.btnShuffle);
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffle();
            }
        });*/
        bar = (SeekBar) view.findViewById(R.id.seekBar);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if (fromUser) {
                    if(mService!= null && mService.isPlaying()) {
                        mService.getMediaPlayer().seekTo(i);
                    }
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

    public void seekUpdation(boolean firstTime) {
        if (firstTime) {
            mMediaPlayer = CommonsUtil.mService.getMediaPlayer();
            bar.setMax(mMediaPlayer.getDuration());
        }
        seekHandler.postDelayed(run, 1000);

    }

    public void togglePlayPause() {
        seekHandler.removeCallbacks(run);
        Song currentSong = CommonsUtil.songsList.get(CommonsUtil.getCurrentSongPos());
        if (CommonsUtil.mBound) {
            mService = CommonsUtil.mService;
            mMediaPlayer = mService.getMediaPlayer();
            if (mService.isPlaying()) {
                mService.pauseMediaPlayer();
                ((ImageButton) getActivity().findViewById(R.id.btnPlayPause)).setImageDrawable(getResources().getDrawable(R.drawable.play_rnd));
            } else {
                // STOPPED, CREATED, EMPTY, -> initialize
                if (!mService.isCreated()) {
                    mService.initializePlayer(Constants.SERVER_URL + currentSong.getPath());
                    populateSongDetails();
                    ((ImageButton) getActivity().findViewById(R.id.btnPlayPause)).setImageDrawable(getResources().getDrawable(R.drawable.pause));
                }

                //prepared, paused -> resume play
                else if (!mMediaPlayer.isPlaying()) {
                    mService.startMediaPlayer();
                    ((ImageButton) getActivity().findViewById(R.id.btnPlayPause)).setImageDrawable(getResources().getDrawable(R.drawable.pause));
                }
            }
        }
    }


    public void next() {
        mService = CommonsUtil.mService;
        if (CommonsUtil.getCurrentSongPos() < CommonsUtil.songsList.size() - 1) {
            if (CommonsUtil.mBound) {
                if (mService.isCreated() && mService.isPlaying()) {
                    mService.stopMediaPlayer();
                }
                CommonsUtil.setCurrentSongPos(CommonsUtil.getCurrentSongPos() + 1);
                Song currentSong = CommonsUtil.songsList.get(CommonsUtil.getCurrentSongPos());
                mService.initializePlayer(Constants.SERVER_URL + currentSong.getPath());
                populateSongDetails();
            }
        } else {
            CommonsUtil.setCurrentSongPos(-1);
            next();
        }
    }

    public void prev() {
        mService = CommonsUtil.mService;
        if (CommonsUtil.getCurrentSongPos() >= 1) {
            if (CommonsUtil.mBound) {
                if (mService.isCreated() && mService.isPlaying()) {
                    mService.stopMediaPlayer();
                }
                CommonsUtil.setCurrentSongPos(CommonsUtil.getCurrentSongPos() - 1);
                Song currentSong = CommonsUtil.songsList.get(CommonsUtil.getCurrentSongPos());
                mService.initializePlayer(Constants.SERVER_URL + currentSong.getPath());
                populateSongDetails();
            }
        } else {
            CommonsUtil.setCurrentSongPos(CommonsUtil.songsList.size());
            prev();
        }
    }

    public void populateSongDetails() {
        Song currentSong = CommonsUtil.songsList.get(CommonsUtil.getCurrentSongPos());
        TextView txtSongName = (TextView) v.findViewById(R.id.txtSongName);
        TextView txtSongArtist = (TextView) v.findViewById(R.id.txtSongArtist);
        txtSongName.setText(currentSong.getName());
        txtSongArtist.setText(currentSong.getArtist());
    }

    public void playCurrent() {
        if (CommonsUtil.mService.isCreated() && CommonsUtil.mService.isPlaying()) {
            CommonsUtil.mService.stopMediaPlayer();
        }
        Song currentSong = CommonsUtil.songsList.get(CommonsUtil.getCurrentSongPos());
        CommonsUtil.mService.initializePlayer(Constants.SERVER_URL + currentSong.getPath());
        populateSongDetails();
    }

    public void updateButtons(String status) {
        if (status.equals("play")) {
            ((ImageButton) getActivity().findViewById(R.id.btnPlayPause)).setImageDrawable(getResources().getDrawable(R.drawable.pause));
        } else {
            ((ImageButton) getActivity().findViewById(R.id.btnPlayPause)).setImageDrawable(getResources().getDrawable(R.drawable.play_rnd));
        }
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
