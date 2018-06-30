package com.app.imbeat;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


//Abstract class to store global information about Now playing song, queue, history, directories,
//playlists, and the concatenated list of shuffled+queued songs
public abstract class AbstractMediaPlayerActivity extends AppCompatActivity{

    private static ArrayList<Directory> directoryList = new ArrayList<Directory>();
    private static ArrayList<AudioFile> queueList = new ArrayList<>();
    private static ArrayList<AudioFile> historyList = new ArrayList<>();
    private static ArrayList<AudioFile> effectiveQueueList = new ArrayList<>();
    private static ArrayList<AudioFile> totalAudioPool = new ArrayList<>();
    private AudioFile nowPlaying;
    private AudioFile upNext;
    private AudioFile previous;

    //List of Directories
    public static ArrayList<Directory> getDirectoryList() {
        return directoryList;
    }

    public static void setDirectoryList(ArrayList<Directory> directoryList) {
        AbstractMediaPlayerActivity.directoryList = directoryList;
    }

    //Add a new directory
    public void addDirectoryList(Directory directory) {
        directoryList.add(directory);
    }

    //Delete the directory at a specified position
    public void deleteDirectoryList(int position) {
        directoryList.remove(position);
    }

    //List of manually Queued songs
    public static ArrayList<AudioFile> getQueueList() {
        return queueList;
    }

    public void addQueueList(AudioFile audiofile) {
        queueList.add(audiofile);
    }

    public static void setQueueList(ArrayList<AudioFile> queueList) {
        AbstractMediaPlayerActivity.queueList = queueList;
    }

    //List of recently played songs
    public static ArrayList<AudioFile> getHistoryList() {
        return historyList;
    }

    public void addHistoryList(AudioFile audiofile) {
        historyList.add(audiofile);
        while(historyList.size() > 100) {
            historyList.remove(0);
        }
    }

    public static void setHistoryList(ArrayList<AudioFile> historyList) {
        AbstractMediaPlayerActivity.historyList = historyList;
    }



    //Total list of sorted, manually queued and non-manually queued songs
    public static ArrayList<AudioFile> getEffectiveQueueList() {
        return effectiveQueueList;
    }

    //Write history, queue, and selected directories to an external file
    public void saveVars() {

        SharedPreferences.Editor prefEditor = getSharedPreferences( "appData", 0).edit();
        try {
            prefEditor.putString("directoryList", ObjectSerializer.serialize(directoryList));
        } catch(IOException e) {
            e.printStackTrace();
        }
        try {
            prefEditor.putString("queueList", ObjectSerializer.serialize(queueList));
        } catch(IOException e) {
            e.printStackTrace();
        }
        try {
            prefEditor.putString("historyList", ObjectSerializer.serialize(historyList));
        } catch(IOException e) {
            e.printStackTrace();
        }
        prefEditor.apply();
    }

    //Load history, queue, and selected directories from an external file
    public void loadVars() {
        SharedPreferences prefs = getSharedPreferences( "appData", 0);

        try {
            directoryList = (ArrayList<Directory>) ObjectSerializer.deserialize(prefs.getString("directoryList", ObjectSerializer.serialize(new ArrayList<Directory>())));
        } catch (IOException e) {
            e.printStackTrace();
            directoryList = new ArrayList<Directory>();
        }
        try {
            queueList = (ArrayList<AudioFile>) ObjectSerializer.deserialize(prefs.getString("queueList", ObjectSerializer.serialize(new ArrayList<AudioFile>())));
        } catch (IOException e) {
            e.printStackTrace();
            queueList = new ArrayList<AudioFile>();
        }
        try {
            historyList = (ArrayList<AudioFile>) ObjectSerializer.deserialize(prefs.getString("historyList", ObjectSerializer.serialize(new ArrayList<AudioFile>())));
        } catch (IOException e) {
            e.printStackTrace();
            historyList = new ArrayList<AudioFile>();
        }

        Log.i("aaaaaaaaaaaaa", Integer.toString(directoryList.size()));
        Log.i("bbbbbbbbbbbbb", directoryList.toString());
    }

    //Load audio files from all specified directories and playlists, seeded shuffle them and set the global queue
    public void loadAudio() {
        ContentResolver contentResolver = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            effectiveQueueList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                // Save to audioList
                effectiveQueueList.add(new AudioFile(data, fileName, artistName, duration));
            }
        }
        cursor.close();
    }

    //Shuffle songs from all selected directories
    public void buildEffectiveQueue() {
        for(Directory directory : directoryList) {
            File file = new File(Environment.getExternalStorageDirectory()+directory.getReadableName());
            File[] files = file.listFiles();
            for(File ifile : files) {
                String filename = ifile.getName();
                if(filename.substring(filename.length()-4, filename.length()).toLowerCase().equals(".mp3")) {
                    //TODO: make a new AudioFile object and add it to the pool
                } else if(filename.substring(filename.length()-4, filename.length()).toLowerCase().equals(".mp4")) {
                    //TODO: make a new AudioFile object and add it to the pool
                }
            }
        }
        //TODO: Shuffle the total pool, add queue to the front of the list and append the shuffled list
    }



}
