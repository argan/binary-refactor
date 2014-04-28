package org.hydra.gui.web;

import java.io.Serializable;

public class FileItem implements Serializable {

    private static final long serialVersionUID = 6258813137889505933L;
    private String            fulleName, origName, mimeType;
    private int               version;

    public FileItem(String fulleName, String origName, String mimeType) {
        this.fulleName = fulleName;
        this.origName = origName;
        this.mimeType = mimeType;
        this.version = 1;
    }

    public String getFullName() {
        return fulleName;
    }

    public void setFulleName(String fulleName) {
        this.fulleName = fulleName;
        this.version++;
    }

    public String getOrigName() {
        return origName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public int getVersion() {
        return this.version;
    }
}
