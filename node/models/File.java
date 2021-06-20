package models;

public class File {
    private String filename;
    private long filesize;
    private String filetype;
    private String status;
    private String address;
    private int port;
    private String owner;



    public File(String filename, long filesize, String filetype, String status, String address, int port, String owner) {
        this.filename = filename;
        this.filesize = filesize;
        this.filetype = filetype;
        this.status = status;
        this.address = address;
        this.port = port;
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
