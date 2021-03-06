package org.lielas.micdataloggerstudio.main;

import org.lielas.dataloggerstudio.lib.LoggerRecord;

/**
 * Created by Andi on 11.04.2015.
 */
public class LoggerRecordManager {

    private LoggerRecord activeLoggerRecord;

    private static LoggerRecordManager instance;

    public static LoggerRecordManager getInstance(){
        if(instance == null){
            instance = new LoggerRecordManager();
        }
        return instance;
    }

    public LoggerRecordManager(){
        activeLoggerRecord = null;
    }

    public void setActiveLoggerRecord(LoggerRecord lr){
        activeLoggerRecord = lr;
    }

    public LoggerRecord getActiveLoggerRecord(){
        return activeLoggerRecord;
    }

}
