package com.app.imbeat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedMetaData;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class AbstractMediaPlayerService extends Service implements
    MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener {

    //TODO: Implement audio focus
    private int playPosition = 0;
    private final IBinder iBinder = new LocalBinder();
    private MediaPlayer player;
    private static String testPath = "/Music/Non-Gachi/-+.mp3";
    private String mediaFile;

    public class LocalBinder extends Binder {
        public AbstractMediaPlayerService getService() {
            return AbstractMediaPlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        startPlayback();
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        if (player.isPlaying()) {
            player.stop();
            stopSelf();
        }
    }

    @Override
    public boolean onError(MediaPlayer player, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer player) {

    }

    @Override
    public boolean onInfo(MediaPlayer player, int what, int extra) {

        return false;
    }


    private void initPlayer() {
        player = new MediaPlayer();
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setOnSeekCompleteListener(this);
        player.setOnInfoListener(this);
        player.reset();
        player.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
        try {
            player.setDataSource(Environment.getExternalStorageDirectory().toString() + testPath);
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }

        player.prepareAsync();

    }

    private void startPlayback() {
        if(!player.isPlaying()) {
            player.start();
        }
    }

    private void pausePlayback() {
        if(player.isPlaying()) {
            player.pause();
            playPosition = player.getCurrentPosition();
        }
    }

    private void stopPlayback() {
        if (player == null) return;
        if (player.isPlaying()) {
            player.stop();
        }
    }

    private void resumePlayback() {
        player.seekTo(playPosition);
        player.start();
    }

    //The system calls this method when an activity, requests the service be started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaFile = testPath;
        initPlayer();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            stopPlayback();
            player.release();
        }
    }

}
