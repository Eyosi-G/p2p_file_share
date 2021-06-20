package Helpers;

import com.google.gson.Gson;
import models.FileInformation;
import models.Peer;

import java.io.*;

import java.util.ArrayList;

public class HelperFunctions {
    public static ArrayList<FileInformation> getFiles(String directoryPath){
        File directory = new File(directoryPath);
        ArrayList<FileInformation> fileInformationArrayList = new ArrayList<>();
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length ; i++) {
            File file = files[i];
            if(file.isFile()){
                String ext = file.getName().split("\\.")[1];
                String fileType = decideType(ext);
                String fileSize = file.length()+"";
                String fileName = file.getName();
                FileInformation fileInformation = new FileInformation(
                        fileName,
                        fileType,
                        fileSize
                );
                fileInformationArrayList.add(fileInformation);
            }
        }
        return fileInformationArrayList;

    }
    private static String decideType(String extension){
        switch (extension){
            case "mp3":
                return "audio";
            case "txt":
                return "text";
            case "jpg":
            case "png":
            case "jpeg":
                return "image";
            case "mp4":
                return "video";
            case "pdf":
            case "epub":
            case "docx":
                return "document";
                default:
                    return "unknown";
        }
    }
    public static String uploadPeerInformation(String uploadDirectoryPath,String address, int port, String username, String id){
        try{
            Peer peer;
            ArrayList<FileInformation> fileInformationArrayList = getFiles(uploadDirectoryPath);
            if(id != null){
                peer = new Peer(address,port,username,fileInformationArrayList,id);
            }else{
                peer = new Peer(address,port,username,fileInformationArrayList);

            }
            String peerJson = new Gson().toJson(peer,Peer.class);
            String resposneId = Request.sendPostRequest(peerJson,Constants.URL);
            return resposneId;
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }
    public static boolean deleteFile(String filename){
        try {
            String path = SettingHelper.getSetting().getDownloadPath() + "\\" + filename;
            File file = new File(path);
            return file.delete();
        }catch(Exception e){
            return false;
        }
    }

}
