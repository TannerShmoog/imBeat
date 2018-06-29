package com.app.imbeat;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AbstractMediaPlayerActivity implements NavigationView.OnNavigationItemSelectedListener{

    //TODO: Load directories externally
    private SeekBar seekBar;
    private TextView textView;
    private AbstractMediaPlayerService player;
    private Boolean serviceBound = false;
    private Button testButton;
    NavigationView navigationView;
    private static final String TAG = "MainActivity";
    private static String testPath = "/Music/Non-Gachi/-+.mp3";
    //TODO: REMOVE
    private ArrayList<AudioFile> audioList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadVars();

        seekBar = findViewById(R.id.seekBar);
        textView = findViewById(R.id.textView2);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        seekBar.setPadding(0,0,0,0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                float percentProgress = (float) seekBar.getProgress() / 100;
                //float seekLocation = (float) player.getDuration() * percentProgress;
                //textView.setText(Float.toString(seekLocation)+" / "+Integer.toString(player.getDuration()));

                //player.seekTo(Math.round(seekLocation));

            }
        });

        if(!isReadStoragePermissionGranted()) {
            try {
                ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAudio();
                //play the first audio in the ArrayList
               playAudio("a");
            }
        });
    }


    @Override
    public void onBackPressed() {

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            AbstractMediaPlayerService.LocalBinder binder = (AbstractMediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    private void playAudio(String media) {
        //Check is service is active
        if (!serviceBound) {
            Intent playerIntent = new Intent(this, AbstractMediaPlayerService.class);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
    }

    private void loadAudio() {
        ContentResolver contentResolver = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            audioList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                //TODO: Change to file name and not title
                String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                // Save to audioList
                audioList.add(new AudioFile(data, fileName, artistName, duration));
            }
        }
        cursor.close();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_directories) {
            Intent intent = new Intent(this, DirectoriesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_playlists) {

        } else if (id == R.id.nav_history) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //Taken from https://mobikul.com/getting-read-write-permission-external-storage-android/
    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }

}
