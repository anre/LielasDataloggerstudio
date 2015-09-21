package org.lielas.micdataloggerstudio.main.Tasks;

import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.micdataloggerstudio.main.CommunicationInterface.mic.MicSerialInterface;
import org.lielas.micdataloggerstudio.main.LoggerRecordManager;
import org.lielas.micdataloggerstudio.main.UpdateManager;

/**
 * Created by Andi on 17.08.2015.
 */
public class StopLoggerTask extends MicTask<Void, Void, MicUSBStick> {
    private MicUSBStick stick;

    public StopLoggerTask(MicUSBStick log, UpdateManager updateManager){
        super(updateManager);
        this.stick = log;
    }


    @Override
    protected MicUSBStick doInBackground(Void... params) {
        if(stick==null){
            return null;
        }

        MicSerialInterface com = (MicSerialInterface)stick.getCommunicationInterface();

        if(!com.isOpen()){
            return null;
        }

        if(!com.readIdentifier(stick)){
            return null;
        }

        if(!com.readArguments(stick)){
            return null;
        }

        stick.setLoggingStatus(false);

        return stick;
    }


    @Override
    protected void onPostExecute(MicUSBStick micUSBStick) {
        try {
            if (get() != null) {
                LoggerRecord lr = new LoggerRecord(micUSBStick);
                LoggerRecordManager.getInstance().setActiveLoggerRecord(lr);
                updateManager.update("Logger successfully stopped");
            }
        }catch(Exception e){
        }
    }
}
