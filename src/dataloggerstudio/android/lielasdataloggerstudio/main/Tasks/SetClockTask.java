package org.lielas.lielasdataloggerstudio.main.Tasks;

import android.content.Context;

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.lielasdataloggerstudio.main.CommunicationInterface.UsbCube.UsbCubeSerialInterface;

/**
 * Created by Andi on 26.03.2015.
 */
public class SetClockTask extends  LielasTask<Void, Void, UsbCube>{
    private UsbCube logger;


    public SetClockTask(UsbCube log, UpdateManager updateManager){
        super(updateManager);
        this.logger = log;
    }

    @Override
    protected UsbCube doInBackground(Void... params) {
        if(logger==null){
            return null;
        }

        UsbCubeSerialInterface com = (UsbCubeSerialInterface)logger.getCommunicationInterface();

        if(!com.isOpen()){
            return null;
        }

        if(!com.setClock(logger)){
            return null;
        }
        return logger;
    }

    @Override
    protected void onPostExecute(UsbCube log){
        try {
            if (get() != null) {
                updateManager.update("Logger clock successfully set");
            }
        }catch(Exception e){
        }
    }
}
