package com.example.lenovo.thejamroom.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.lenovo.thejamroom.R;
import com.example.lenovo.thejamroom.activity.HomeActivity;

public class MusicPlayerService extends Service implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener{


    private static final String ACTION_PLAY = "com.example.action.PLAY";
    boolean isPlaying = false;
    boolean isCreated = false;
    private static int classID = 579; // just a number
    LocalBroadcastManager broadcaster;

    static final public String BROADCAST_RESULT = "com.example.lenovo.thejamroom.BROADCASE_RESULT";

    MediaPlayer mMediaPlayer = new MediaPlayer();
    private final Binder mBinder  = new MediaPlayerBinder();

    public class MediaPlayerBinder extends Binder {
        /**
         * Returns the instance of this service for a client to make method calls on it.
         * @return the instance of this service.
         */
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }

    }
    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void initializePlayer(String streamUrl) {
        isCreated = true;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(streamUrl);
        }
        catch (Exception e) {
            Log.e("MediaPlayerService", "error setting data source");
        }
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.prepareAsync();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer player, int percent) {

    }

    @Override
    public boolean onError(MediaPlayer player, int what, int extra) {
        mMediaPlayer.reset();
        isPlaying = false;
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        startMediaPlayer();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        broadcaster = LocalBroadcastManager.getInstance(this);
        return START_STICKY;
    }

    /**
     * Pauses the contained StatefulMediaPlayer
     */
    public void pauseMediaPlayer() {
        Log.d("MediaPlayerService","pauseMediaPlayer() called");
        mMediaPlayer.pause();
        isPlaying = false;
        stopForeground(true);
        sendResult("PLAYER_PAUSED");

    }

    /**
     * Starts the contained StatefulMediaPlayer and foregrounds the service to support
     * persisted background playback.
     */
    public void startMediaPlayer() {
        Context context = getApplicationContext();
        isPlaying = true;
        //set to foreground
        Notification notification = new Notification(R.drawable.icon, "MediaPlayerService",
                System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        CharSequence contentTitle = "MediaPlayerService Is Playing";
        CharSequence contentText = "There She goes";
        notification.setLatestEventInfo(context, contentTitle,contentText, pendingIntent);
        startForeground(1, notification);

        Log.d("MediaPlayerService","startMediaPlayer() called");
        mMediaPlayer.start();

        sendResult("PLAYER_INIT");
    }

    /**
     * Stops the contained StatefulMediaPlayer.
     */
    public void stopMediaPlayer() {
        stopForeground(true);
        mMediaPlayer.stop();
        isPlaying = false;
        mMediaPlayer.release();
        isCreated = false;
    }

    public void resetMediaPlayer() {
        stopForeground(true);
        mMediaPlayer.reset();
        isPlaying = false;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mMediaPlayer.release();
        stopForeground(true);
    }

    public boolean isPlaying(){
        return isPlaying;
    }

    public boolean isCreated(){
        return isCreated;
    }


    public void sendResult(String message) {
        Intent intent = new Intent(BROADCAST_RESULT);
        if(message != null)
             System.out.println("Sending Broadcast");
            intent.putExtra("MUSIC_PLAYER_STATUS", message);
        broadcaster.sendBroadcast(intent);
    }
}
