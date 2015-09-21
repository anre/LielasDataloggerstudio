package org.lielas.lielasdataloggerstudio.main.Tasks;

import android.content.Context;

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.lielasdataloggerstudio.R;
import org.lielas.lielasdataloggerstudio.main.CommunicationInterface.UsbCube.UsbCubeSerialInterface;

/**
 * Created by Andi on 26.03.2015.
 */
public class SetLoggernameTask extends LielasTask<Void, Void, UsbCube>{
    private UsbCube logger;
    private String name;

    public SetLoggernameTask(UsbCube log, UpdateManager updateManager, String name){
        super(updateManager);
        this.logger = log;
        this.name = name;
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

        while(com.isBusy()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        com.setBusy(true);

        if(!com.setName(name, logger)){
            return null;
        }
        return logger;
    }


    @Override
    protected void onPostExecute(UsbCube log){
        logger.getCommunicationInterface().setBusy(false);
        try {
            if (get() != null) {
                updateManager.update("Logger name successfully set");
            }
        }catch(Exception e){
        }
    }
}
