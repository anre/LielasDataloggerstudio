package org.lielas.lielasdataloggerstudio.main;

import org.lielas.dataloggerstudio.lib.LoggerRecord;

/**
 * Created by Andi on 31.03.2015.
 */
public class LoggerRecordManager {

    private LoggerRecord activeLogggerRecord;

    private static LoggerRecordManager instance;

    public static LoggerRecordManager getInstance(){
        if(instance == null){
            instance = new LoggerRecordManager();
        }
        return instance;
    }

    public LoggerRecordManager(){
        activeLogggerRecord = null;
    }

    public void setActiveLogggerRecord(LoggerRecord lr){
        activeLogggerRecord = lr;
    }

    public LoggerRecord getActiveLogggerRecord(){
        return activeLogggerRecord;
    }

}
