package Helpers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.File;
import models.Peer;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

public class Request {

    public static String sendPostRequest(String body, String host){
        try{
            URL url = new URL(host);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(body);
            wr.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String ln;
            StringBuffer response = new StringBuffer();
            while((ln = in.readLine())!= null){
                response.append(ln);
            }
            in.close();
            return response.toString();

        }catch(Exception e){
            System.out.println(e);
            return null;
        }

    }
    public static ArrayList<Peer> sendGetRequest(String host){
        try{
            URL url = new URL(host);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            System.out.println(connection.getResponseCode());
            if(!(connection.getResponseCode() >= 200 && connection.getResponseCode() < 300)) return null;
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String ln;
            StringBuffer body = new StringBuffer();
            while((ln = in.readLine())!= null){
                body.append(ln);
            }
            in.close();
            System.out.println(body);
            Type peerListType = new TypeToken<ArrayList<Peer>>(){}.getType();
            String json = body.toString();
            ArrayList<Peer> peers = new Gson().fromJson(json,peerListType);
            return peers;

        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static  ArrayList<File> loadFiles(){
        String id = SettingHelper.getSetting().getId();
        final String host = Constants.URL + "?filterId="+id;
        ArrayList<Peer> peers = sendGetRequest(host);
        ArrayList<File> loadedFiles = new ArrayList<>();
        peers.forEach(peer -> {
                peer.getFiles().forEach(fileInformation -> {
                    final String fileName = fileInformation.getFilename();
                    final long fileSize = Long.valueOf(fileInformation.getFilesize());
                    final String fileType = fileInformation.getFiletype();
                    final String status = SettingHelper.isDownloaded(fileName) ? "completed" : "waiting";
                    final String address = peer.getAddress();
                    final int port = peer.getPort();
                    final String owner = peer.getUsername();
                    File file = new File(fileName,fileSize,fileType, status,address,port,owner);
                    loadedFiles.add(file);

                });
        });
        return loadedFiles;
    }

    public static boolean isSocketLive(String address, int port){
        try{
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(address,port));
            socket.setSoTimeout(1000);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
