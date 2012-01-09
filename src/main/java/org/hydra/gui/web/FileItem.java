package org.hydra.gui.web;

public class FileItem {
    private String fulleName, origName, mimeType;
    private int version;

    public FileItem(String fulleName, String origName, String mimeType) {
        this.fulleName = fulleName;
        this.origName = origName;
        this.mimeType = mimeType;
        this.version = 1;
    }

    public String getFulleName() {
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
