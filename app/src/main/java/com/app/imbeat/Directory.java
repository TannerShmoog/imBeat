package com.app.imbeat;

import android.net.Uri;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class Directory implements Serializable {

    private String uriPath;
    private String readableName;
    private Boolean isEnabled;

    //Stores one user specified storage directory to pull music files from
    public Directory(Uri uriPath, Boolean isEnabled) {
        this.uriPath = uriPath.toString();

        String[] templist = uriPath.getPath().split(":");
        this.readableName = "/"+templist[templist.length - 1];

        this.isEnabled = isEnabled;
    }

    @Override
    public String toString() {
        return this.readableName;
    }

    public String getUriPath() {
        return uriPath;
    }

    public void setUriPath(String uriPath) {
        this.uriPath = uriPath;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public String getReadableName() {
        return readableName;
    }

    public void setReadableName(String readableName) {
        this.readableName = readableName;
    }
}
