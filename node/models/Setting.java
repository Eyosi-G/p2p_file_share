package models;

import java.util.ArrayList;
import java.util.List;

public class Setting {
    private List<Download> downloads = new ArrayList<Download>();
    private String downloadPath;
    private String uploadPath;
    private String address;
    private int port;
    private String username;
    private String id;

    public Setting(String downloadPath, String uploadPath, String address, int port, String username, String id) {
        this.downloadPath = downloadPath;
        this.uploadPath = uploadPath;
        this.address = address;
        this.port = port;
        this.username = username;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<Download> getDownloads() {
        return this.downloads;
    }

    public void setDownloads(List<Download> downloads) {
        this.downloads = downloads;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
}


