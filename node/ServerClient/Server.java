package ServerClient;

import Helpers.SettingHelper;
import models.Setting;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    @Override
    public void run() {
        super.run();
        try{
            Setting setting = SettingHelper.getSetting();

            int port = setting.getPort();
            ServerSocket serverSocket = new ServerSocket(port);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("connected..");
                Thread t  = new Thread(()->{
                    try{
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        String filename = in.readUTF();
                        String filepath = setting.getUploadPath()+ "\\" + filename;
                        readFile(filepath,out);

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                });
                t.start();

            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void readFile(String path, DataOutputStream out){
        try{
            File file  = new File(path);
            FileInputStream in  = new FileInputStream(file);
            int c;
            while((c = in.read())!= -1){
                out.write(c);
                out.flush();
            }
            in.close();
        }catch(Exception e ){
            e.printStackTrace();
        }

    }
}

