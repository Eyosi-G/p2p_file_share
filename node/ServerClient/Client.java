package ServerClient;

import Helpers.SettingHelper;
import models.Download;
import models.FileInformation;
import models.Setting;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class Client extends SwingWorker<String,String> {

    private JTable fileTable;
    private models.File fileInfo;
    private final int selectedRow;
    private boolean stopFlag  = false;


    public Client(int selectedRow, JTable fileTable, models.File fileInfo){
        this.fileTable = fileTable;
        this.fileInfo = fileInfo;
        this.selectedRow = selectedRow;
    }

    private void writeFile(String filePath, DataInputStream in){
        try{
            File file = new File(filePath);
            FileOutputStream out = new FileOutputStream(file);
            final long totalFileSize = Long.valueOf(fileInfo.getFilesize());
            int c;
            while(!stopFlag && file.length() != totalFileSize && (c = in.read())!= -1){
                out.write(c);
                out.flush();
                int percent = calculatePercent(file.length(),totalFileSize);
                publish((percent + " %"));
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private int calculatePercent(long current, long total ){
        return ((int)((current/((double)total)) * 100)) ;
    }

    @Override
    protected void process(List<String> chunks) {
        super.process(chunks);
        String val = chunks.get(chunks.size() - 1);
        fileTable.setValueAt(val,selectedRow,3);
    }

    @Override
    protected void done() {
        super.done();
        if(!stopFlag) {
            Download download = new Download(fileInfo.getFilename(),fileInfo.getFilesize() + "", fileInfo.getFiletype());
            if(!SettingHelper.isDownloaded(download.getFilename())){
                Setting setting = SettingHelper.getSetting();
                List<Download> downloads = setting.getDownloads();
                downloads.add(download);
                setting.setDownloads(downloads);
                SettingHelper.saveSetting(setting);
            }

        }
    }

    @Override
    protected String doInBackground() throws Exception {
        Setting setting = SettingHelper.getSetting();
        final String address = fileInfo.getAddress();
        final int port = fileInfo.getPort();
        Socket socket  = new Socket();
        socket.connect(new InetSocketAddress(address,port));
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in  = new DataInputStream(socket.getInputStream());
        out.writeUTF(fileInfo.getFilename());
        out.flush();

        final String outputPath = setting.getDownloadPath() + "\\" + fileInfo.getFilename();
        writeFile(outputPath,in);

        return null;
    }

    public void setStopFlag(boolean stopFlag) {
        this.stopFlag = stopFlag;
    }

    public int getSelectedRow() {
        return selectedRow;
    }
}
