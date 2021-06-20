package models;

public class FileInformation {
    private String filename;
    private String filetype;
    private String filesize;

    public FileInformation(String filename, String filetype, String filesize) {
        this.filename = filename;
        this.filetype = filetype;
        this.filesize = filesize;
    }
    public FileInformation(String filename, String filesize){
        this.filename = filename;
        this.filesize = filesize;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

}
