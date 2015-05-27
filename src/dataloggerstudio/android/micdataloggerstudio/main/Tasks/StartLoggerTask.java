package org.lielas.micdataloggerstudio.main.Tasks;

import org.lielas.dataloggerstudio.lib.Logger.mic.MicStartTrigger;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.micdataloggerstudio.main.CommunicationInterface.mic.MicSerialInterface;
import org.lielas.micdataloggerstudio.main.UpdateManager;

/**
 * Created by Andi on 02.05.2015.
 */
public class StartLoggerTask extends MicTask<Void, Void, MicUSBStick> {
    private MicUSBStick stick;
    MicStartTrigger startTrigger;

    public StartLoggerTask(MicUSBStick log, UpdateManager updateManager){
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

        String cmd = "S " + Long.toString(stick.getSampleRate()) + " M " + stick.getMicStartTrigger().getCmdString() + " " + Integer.toString(stick.getMaxSamples() / 1000) + " 3\r";

        if(!com.startLogging(stick, cmd)){
            return null;
        }

        return stick;
    }

    @Override
    protected void onPostExecute(MicUSBStick micUSBStick) {
        try {
            if (get() != null) {
                updateManager.update("Logger successfully started");
            }
        }catch(Exception e){
        }
    }
}
