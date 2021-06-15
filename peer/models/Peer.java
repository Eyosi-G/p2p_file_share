package models;

import java.util.List;

public class Peer {
    private String address;
    private int port;
    private String username;
    private List<FileInformation> files;
    private String id;

    public Peer(String address, int port, String username, List<FileInformation> fileInformationList) {
        this.files = fileInformationList;
        this.address = address;
        this.port = port;
        this.username = username;
    }

    public Peer(String address, int port, String username, List<FileInformation> files, String id) {
        this.address = address;
        this.port = port;
        this.username = username;
        this.files = files;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<FileInformation> getFiles() {
        return files;
    }

    public void setFiles(List<FileInformation> files) {
        this.files = files;
    }
}


//
// Type founderListType = new TypeToken<ArrayList<Founder>>(){}.getTypes();
//