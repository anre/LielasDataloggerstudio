package org.lielas.micdataloggerstudio.main.Tasks;

import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.micdataloggerstudio.main.CommunicationInterface.mic.MicSerialInterface;
import org.lielas.micdataloggerstudio.main.UpdateManager;

/**
 * Created by Andi on 02.05.2015.
 */
public class SaveNameTask extends MicTask<Void, Void, MicUSBStick> {

    MicUSBStick stick;
    String name;

    public SaveNameTask(MicUSBStick stick, UpdateManager updateManager, String name){
        super(updateManager);
        this.stick = stick;
        this.name = name;
    }

    @Override
    protected MicUSBStick doInBackground(Void... params) {
        if(stick == null){
            return null;
        }

        MicSerialInterface com = (MicSerialInterface) stick.getCommunicationInterface();

        if(!com.isOpen()){
            return null;
        }

        if(!com.saveId(name, stick)){
            return null;
        }

        return stick;
    }

    @Override
    protected void onPostExecute(MicUSBStick stick){
        try{
            if(get() != null){
                updateManager.update("Loggername successfully set");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
