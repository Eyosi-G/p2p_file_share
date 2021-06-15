package views;

import Helpers.HelperFunctions;
import Helpers.SettingHelper;
import ServerClient.Server;
import models.Setting;

import javax.swing.*;
import java.io.File;

public class FileTransferLogin extends JFrame {
    private JTextField textUploadDirectory;
    private JButton uploadChooseButton;
    private JTextField textDownloadDirectory;
    private JButton downloadChooseButton;
    private JPanel rootPanel;
    private JButton doneButton;
    private JTextField textAddress;
    private JTextField textPort;
    private JTextField textUserName;

    public FileTransferLogin (){
        setContentPane(rootPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650,400);
        setVisible(true);
        uploadChooseButton.addActionListener(e -> {
            Thread t  = new Thread(()->{
                File file = selectFolder();
                if(file!=null){
                    textUploadDirectory.setText(file.getPath());
                }
            });
            t.start();

        });

        downloadChooseButton.addActionListener(e -> {
            Thread t  = new Thread(()->{
                File file = selectFolder();
                if(file!=null){
                    textDownloadDirectory.setText(file.getPath());
                }
            });
            t.start();
        });
        doneButton.addActionListener(e -> {
           login();
        });
        loadSetting();

    }

    private void  login(){
        try{
            final String uploadDirectory = textUploadDirectory.getText();
            final String downloadDirectory = textDownloadDirectory.getText();
            final String address = textAddress.getText();
            final int port =  Integer.parseInt(textPort.getText());
            final String username = textUserName.getText();
            Thread t = new Thread(()->{
                if(!uploadDirectory.isEmpty() && !downloadDirectory.isEmpty()){
                    Setting setting = SettingHelper.getSetting();
                    if(setting != null){
                        String id = setting.getId();
                        HelperFunctions.uploadPeerInformation(uploadDirectory,address, port, username, id);
                        setting.setUsername(username);
                        setting.setUploadPath(uploadDirectory);
                        setting.setDownloadPath(downloadDirectory);
                        setting.setAddress(address);
                        setting.setPort(port);
                    }else{
                        String id = HelperFunctions.uploadPeerInformation(uploadDirectory,address, port, username,null);
                        setting = new Setting(downloadDirectory,uploadDirectory,address,port,username,id);
                    }
                    SettingHelper.saveSetting(setting);
                    setVisible(false);
                    new FileTransferScreen();
                    Server server = new Server();
                    server.start();

                }
            });
            t.start();

        }catch(Exception exp){

        }

    }

    private File selectFolder(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(this);
        if(option == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            return file;
        }else{
            return null;
        }
    }

    private void loadSetting(){
        Thread t = new Thread(()->{
            Setting setting = SettingHelper.getSetting();
            if(setting!=null){
                textDownloadDirectory.setText(setting.getDownloadPath());
                textUploadDirectory.setText(setting.getUploadPath());
                textUserName.setText(setting.getUsername());
                textAddress.setText(setting.getAddress());
                textPort.setText(setting.getPort() + "");
            }
        });
        t.start();
    }
}
