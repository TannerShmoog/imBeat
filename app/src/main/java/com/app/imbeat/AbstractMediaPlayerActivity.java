package com.app.imbeat;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMediaPlayerActivity extends AppCompatActivity{

    private static ArrayList<Directory> directoryList = new ArrayList<Directory>();
    private static ArrayList<AudioFile> queueList = new ArrayList<>();
    private static ArrayList<AudioFile> historyList = new ArrayList<>();
    private static ArrayList<AudioFile> effectiveQueueList;



    //List of Directories
    public static ArrayList<Directory> getDirectoryList() {
        return directoryList;
    }

    public static void setDirectoryList(ArrayList<Directory> directoryList) {
        AbstractMediaPlayerActivity.directoryList = directoryList;
    }

    public void addDirectoryList(Directory directory) {
        directoryList.add(directory);
    }

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

}
