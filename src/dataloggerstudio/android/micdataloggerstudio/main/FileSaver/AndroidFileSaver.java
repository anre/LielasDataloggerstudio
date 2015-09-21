package org.lielas.micdataloggerstudio.main.FileSaver;

import android.os.Environment;

import org.lielas.dataloggerstudio.lib.FileCreator.FileSaver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Andi on 01.05.2015.
 */
public class AndroidFileSaver extends FileSaver {

    public static final int ERROR_STORAGE_NOT_WRITEABLE = 1000;
    public static final int ERROR_STORAGE_NOT_READABLE = 1001;
    public static final int ERROR_FAILED_TO_CREATE_FILE = 1002;

    public String path_prefix;

    public AndroidFileSaver(){
        super(ANDROID_FILE_SAVER);
        path_prefix = "";
    }

    public AndroidFileSaver(String path){
        super(ANDROID_FILE_SAVER);
        path_prefix = path;
    }

    public String getPathPrefix(){
        return path_prefix;
    }

    private boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state) ){
            return true;
        }
        return false;
    }

    private boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    @Override
    public int save(final StringBuilder sb, String path, boolean overWrite){

        if(!isExternalStorageWriteable()){
            return ERROR_STORAGE_NOT_WRITEABLE;
        }

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + path_prefix);

        if(!folder.exists()){
            if(!folder.mkdir()){
                return ERROR_FAILED_TO_CREATE_FILE;
            }
        }

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + path_prefix, path);

        if(file.exists() && !overWrite){
            return ERROR_FILE_EXISTS;
        }else if(file.exists()){
            if(!file.delete()){
                return ERROR_FAILED_TO_CREATE_FILE;
            }
        }
        file.setReadable(true);

        try {
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(getUTF8Bom());
            writer.write(sb.toString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return STATUS_OK;
    }

    private String getUTF8Bom(){
        byte[] bom = new byte[3];
        bom[0] = (byte) 0xEF;
        bom[1] = (byte) 0xBB;
        bom[2] = (byte) 0xBF;
        String strBom = "";

        try{
            strBom = new String(bom, "UTF-8");
        }catch (UnsupportedEncodingException e){
            return "";
        }
        return strBom;
    }

}

