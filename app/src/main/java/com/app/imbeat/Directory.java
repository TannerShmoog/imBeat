package com.app.imbeat;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class Directory implements Serializable {

    private String uriPath;
    private String readableName;
    private Boolean isEnabled;

    public Directory(String uriPath, Boolean isEnabled) {
        this.uriPath = uriPath;
        String[] templist = uriPath.split("/");
        try {
            this.readableName = java.net.URLDecoder.decode(templist[templist.length - 1], "UTF-8");
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
            this.readableName = "aaaaaa";//templist[templist.length - 1];
        }
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
