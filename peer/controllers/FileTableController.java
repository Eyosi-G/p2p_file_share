package controllers;

import Helpers.Request;
import Helpers.SettingHelper;
import ServerClient.Client;
import models.File;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class FileTableController {
    private JTable filesTable;
    private ArrayList<File> fileArrayList = new ArrayList<>();
    private DefaultTableModel fileTableModel;
    private JMenuItem downloadOptionItem = new JMenuItem("download");
    private JMenuItem cancelOptionItem = new JMenuItem("cancel");
    private HashMap<Integer,Client> selectedRows = new HashMap<>();

    public FileTableController(JTable filesTable){
        this.filesTable = filesTable;
        defineTable();
        populateTable();
    }
    public void populateTable(){
        Thread t = new Thread(()->{
            fileTableModel.setRowCount(0);
            fileArrayList = Request.loadFiles();
            for (int i = 0; i < fileArrayList.size() ; i++) {
                final File file = fileArrayList.get(i);
                fileTableModel.addRow(new Object[]{
                        file.getFilename(),
                        file.getFilesize(),
                        file.getFiletype(),
                        file.getStatus(),
                        file.getOwner(),
                });
            }
        });
        t.start();
    }
    private void defineTable(){
        this.fileTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        filesTable.setModel(fileTableModel);
        fileTableModel.addColumn("File Name");
        fileTableModel.addColumn("File Size");
        fileTableModel.addColumn("File Type");
        fileTableModel.addColumn("Status");
        fileTableModel.addColumn("Owner");
        JPopupMenu rowOptions = new JPopupMenu();
        rowOptions.add(downloadOptionItem);
        rowOptions.add(cancelOptionItem);

        filesTable.setComponentPopupMenu(rowOptions);
        downloadOptionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int selectedRow = filesTable.getSelectedRow();
                final File file = fileArrayList.get(selectedRow);
                Thread t = new Thread(()->{
                    boolean isLive = Request.isSocketLive(file.getAddress(),file.getPort());
                    if(isLive){
                        Client client = selectedRows.get(selectedRow);
                        if(client != null && client.getSelectedRow() == selectedRow ){
                            selectedRows.remove(selectedRow);
                        }
                        client = new Client(selectedRow,filesTable,file);
                        selectedRows.put(selectedRow,client);
                        client.execute();
                    }else{
                        JOptionPane.showMessageDialog(filesTable,"User is not live");
                    }
                });
                t.start();



            }
        });
        cancelOptionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int selectedRow = filesTable.getSelectedRow();
                final File file = fileArrayList.get(selectedRow);
                Client client = selectedRows.get(selectedRow);
                if(client == null || client.getSelectedRow() != selectedRow ){
                    client = new Client(selectedRow,filesTable,file);
                }
                client.setStopFlag(true);

            }
        });

    }




}
