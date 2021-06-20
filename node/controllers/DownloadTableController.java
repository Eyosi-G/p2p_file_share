package controllers;

import Helpers.HelperFunctions;
import Helpers.SettingHelper;
import models.Download;
import models.Setting;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadTableController {
    private JTable downloadTable;
    private List<Download> downloadTableFiles = new ArrayList<>();
    private DefaultTableModel downloadTableModel = new DefaultTableModel();
    private final JMenuItem deleteItem = new JMenuItem("delete");
    private final JMenuItem openFileLocation = new JMenuItem("open file");
    public DownloadTableController(JTable downloadTable){
        this.downloadTable = downloadTable;
        defineTable();
        Thread t = new Thread(()->{
            loadDownloads();
        });
        t.start();


    }
    private void defineTable(){
        downloadTableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        downloadTable.setModel(downloadTableModel);
        downloadTableModel.addColumn("File Name");
        downloadTableModel.addColumn("File Size");
        downloadTableModel.addColumn("File Type");
        downloadTableModel.addColumn("Date");
        JPopupMenu rowOptions = new JPopupMenu();

        rowOptions.add(deleteItem);
        rowOptions.add(openFileLocation);
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int selectedRow = downloadTable.getSelectedRow();
                deleteRow(selectedRow);
            }
        });
        openFileLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int selectedRow = downloadTable.getSelectedRow();
                openFileLocation(selectedRow);
            }
        });

        downloadTable.setComponentPopupMenu(rowOptions);
    }

    private void deleteRow(final int selectedRow){
        System.out.println(downloadTableFiles.size());
        final String filename = downloadTableFiles.get(selectedRow).getFilename();
        downloadTableModel.removeRow(selectedRow);
        SettingHelper.deleteDownload(filename);
        HelperFunctions.deleteFile(filename);
    }
    private  void openFileLocation(final int selectedRow){
            final String filename = downloadTableFiles.get(selectedRow).getFilename();
            Setting setting = SettingHelper.getSetting();
            final String filePath = setting.getDownloadPath()+"\\"+filename;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Desktop.getDesktop().open(new File(filePath));
                    }catch(Exception e){

                    }
                }
            });
            t.start();

    }

    public void loadDownloads() {
        downloadTableModel.setRowCount(0);
        final Setting setting = SettingHelper.getSetting();
        if (setting != null) {
            downloadTableFiles = setting.getDownloads();
            downloadTableFiles.forEach(download -> {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        downloadTableModel.addRow(new Object[]{
                                download.getFilename(),
                                download.getFilesize(),
                                download.getFileType(),
                                download.getDownloadDate()
                        });
                    }
                });

            });

        }
    }



}
