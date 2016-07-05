package org.lielas.micdataloggerstudio.main.Tasks;

import android.content.Context;

import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.micdataloggerstudio.main.CommunicationInterface.mic.MicSerialInterface;
import org.lielas.micdataloggerstudio.main.LoggerRecordManager;
import org.lielas.micdataloggerstudio.main.UpdateManager;

/**
 * Created by Andi on 11.04.2015.
 */
public class ConnectTask extends MicTask<Void, Void, MicUSBStick> {

    private MicUSBStick logger;
    private Context context;

    public ConnectTask(MicUSBStick logger, UpdateManager updateManager, Context context){
        super(updateManager);
        this.logger = logger;
        this.context = context;
    }

    @Override
    protected MicUSBStick doInBackground(Void... params){
        if(logger == null){
            return null;
        }

        MicSerialInterface com = (MicSerialInterface)logger.getCommunicationInterface();
        com.setContext(context);

        if(com.isOpen()){
            com.close();
        }

        if(!com.connect("")){
            return null;
        }

        com.stopRealTimeLogging();

        if(!com.readIdentifier(logger)){
            return null;
        }

        if(!com.readArguments(logger)){
            return null;
        }


        return logger;
    }

    @Override
    protected void onPostExecute(MicUSBStick log){
        try {
            MicUSBStick stick = get();
            if (stick != null) {
                updateManager.update("Successfully connected");
                LoggerRecordManager.getInstance().setActiveLoggerRecord(stick.getRecordset(0));
            }else{
                updateManager.update("Failed to connect");
            }
        }catch(Exception e){

        }
    }
}
