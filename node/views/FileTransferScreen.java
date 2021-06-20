package views;

import Helpers.*;
import controllers.DownloadTableController;
import controllers.FileTableController;
import models.Setting;

import javax.swing.*;
import java.io.File;

public class FileTransferScreen extends JFrame {
    private JPanel rootPanel;
    private JTable filesTable;
    private JButton filesListRefresh;
    private JTextField textDownloadDirectory;
    private JButton refreshButton;
    private JTable downloadTable;
    private JButton downloadChooseButton;
    private JTextField textUploadDirectory;
    private JButton uploadChooseButton;
    private JButton downloadRefreshButton;
    private JTabbedPane tappedPane;

    public FileTransferScreen() {
        setContentPane(rootPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 400);
        setVisible(true);

        //defining tables controllers
        FileTableController fileTableController = new FileTableController(filesTable);
        DownloadTableController downloadTableController = new DownloadTableController(downloadTable);

        Setting setting = SettingHelper.getSetting();
        setTitle(setting.getUsername() + " ----- " + setting.getAddress()+":" + setting.getPort());

        textDownloadDirectory.setText(setting.getDownloadPath());
        textUploadDirectory.setText(setting.getUploadPath());
        downloadChooseButton.addActionListener(e -> {
            Thread t = new Thread(()->{
                File file = selectFolder();
                if(file!=null){
                    textDownloadDirectory.setText(file.getPath());
                }
            });
            t.start();
        });
        uploadChooseButton.addActionListener(e -> {
            Thread t = new Thread(()->{
                File file = selectFolder();
                if(file!=null){
                    textUploadDirectory.setText(file.getPath());
                }
            });
            t.start();
        });
        refreshButton.addActionListener(e -> refreshSetting());
        downloadRefreshButton.addActionListener(e -> refreshDownloads(downloadTableController));
        filesListRefresh.addActionListener(e -> fileTableController.populateTable());
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
    private void refreshSetting(){
        try{
            Thread t = new Thread(()->{
                final String uploadDirectory = textUploadDirectory.getText();
                final String downloadDirectory = textDownloadDirectory.getText();
                Setting setting = SettingHelper.getSetting();
                setting.setUploadPath(downloadDirectory);
                setting.setUploadPath(uploadDirectory);
                SettingHelper.saveSetting(setting);
                final String address = setting.getAddress();
                final int port = setting.getPort();
                final String owner = setting.getUsername();
                final String id = setting.getId();
                HelperFunctions.uploadPeerInformation(uploadDirectory,address,port,owner,id);
            });
            t.start();

        }catch(Exception exp){

        }
    }
    private void refreshDownloads(DownloadTableController downloadTableController){
        Thread t = new Thread(()->{
            downloadTableController.loadDownloads();
        });
        t.start();
    }


}
