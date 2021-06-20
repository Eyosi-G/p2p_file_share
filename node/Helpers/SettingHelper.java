package Helpers;

import com.google.gson.Gson;
import models.Download;
import models.Setting;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class SettingHelper {
    public static void saveSetting(Setting setting){
        try{
            String settingJson = new Gson().toJson(setting,Setting.class);
            FileWriter fileWriter = new FileWriter(Constants.SETTING_FILE_NAME);
            fileWriter.write(settingJson);
            fileWriter.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public static Setting getSetting(){
        try{
            FileReader fileReader = new FileReader(Constants.SETTING_FILE_NAME);
            StringBuffer stringBuffer = new StringBuffer();
            int character;
            while((character = fileReader.read())!= -1){
                stringBuffer.append((char)character);
            }
            System.out.println(stringBuffer.toString());
            String json = stringBuffer.toString();
            Setting setting = new Gson().fromJson(json,Setting.class);
            return setting;
        }catch(Exception e){
            return null;
        }
    }

    public static boolean isDownloaded(String filename){
        Setting setting = getSetting();
        List<Download> downloadList = setting.getDownloads();
        for (int i = 0; i < downloadList.size() ; i++) {
            if(downloadList.get(i).getFilename().equals(filename)){
                return true;
            }
        }
        return false;
    }

    public static void deleteDownload(String filename){
        Setting setting = getSetting();
        List<Download> downloadList = setting.getDownloads();
        for (int i = 0; i < downloadList.size() ; i++) {
            if(downloadList.get(i).getFilename().equals(filename)){
                downloadList.remove(i);
                break;
            }
        }
        setting.setDownloads(downloadList);
        saveSetting(setting);

    }

}
