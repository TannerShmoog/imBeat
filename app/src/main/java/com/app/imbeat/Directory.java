package com.app.imbeat;

import java.io.Serializable;

public class Directory implements Serializable {

    private String uriPath;
    private Boolean isEnabled;

    public Directory(String uriPath, Boolean isEnabled) {
        this.uriPath = uriPath;
        this.isEnabled = isEnabled;
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
}
