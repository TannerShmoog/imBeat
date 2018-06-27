package com.app.imbeat;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedMetaData;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class AbstractMediaPlayerService extends Service implements
    MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener, AudioManager.OnAudioFocusChangeListener {

    private static ArrayList<String> queueList = new ArrayList<String>();
    private static ArrayList<String> randomList = new ArrayList<String>();
    private static ArrayList<String> historyList = new ArrayList<String>();
    private static ArrayList<String> effectiveQueueList = new ArrayList<String>();
    private final IBinder iBinder = new LocalBinder();
    private MediaPlayer player;

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

    }

    @Override
    public void onCompletion(MediaPlayer player) {

    }

    @Override
    public boolean onError(MediaPlayer player, int what, int extra) {

        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer player) {

    }

    @Override
    public boolean onInfo(MediaPlayer player, int what, int extra) {

        return false;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }


}
