package org.lielas.lielasdataloggerstudio.main.Tasks;

import android.content.Context;

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.lielasdataloggerstudio.main.CommunicationInterface.UsbCube.UsbCubeSerialInterface;

import java.util.concurrent.ExecutionException;

/**
 * Created by Andi on 05.03.2015.
 */
public class ConnectTask extends LielasTask<Void, Void, UsbCube>{

    private UsbCube logger;
    private Context context;

    public ConnectTask(UsbCube log, UpdateManager updateManager, Context context){
        super(updateManager);
        this.logger = log;
        this.context = context;
    }


    @Override
    protected UsbCube doInBackground(Void... params) {
        if(logger==null){
            return null;
        }

        UsbCubeSerialInterface com = (UsbCubeSerialInterface)logger.getCommunicationInterface();
        com.setContext(context);

        if(com.isOpen()){
            com.close();
        }

        if(!com.connect("")){
            return null;
        }

        //stop realtime logging
/*        for(int i = 0; i < 3; i++){
            if(com.stopRealTimeLogging()){
                break;
            }
            if(i == 2){
                return null;
            }
        }*/

        //get version
        if(!com.getVersion((UsbCube)logger)){
            return null;
        }

        //get model
        if(!com.getDatasetStructure((UsbCube)logger)){
            return null;
        }

        //get id
        if(!com.getId((UsbCube)logger)){
            return null;
        }

        //get logger name
        if(!com.getName((UsbCube)logger)){
            return null;
        }

        //get samplerate
        if(!com.getSamplerate((UsbCube)logger)){
            return null;
        }

        //get logger status
        if(!com.getLoggerStatus((UsbCube)logger)){
            return null;
        }

        //get datetime
        if(!com.getClock((UsbCube)logger)){
            return null;
        }

        //get logfile count
        if(!com.getLogfileCount((UsbCube)logger)){
            return null;
        }

        for(int i = 0; i < logger.getRecordCount(); i++){
            com.getLogfileProperties(logger, i);
        }

        return logger;
    }

    @Override
    protected void onPostExecute(UsbCube log){
        try {
            if ((UsbCube) get() != null) {
                updateManager.update();
            }
        }catch(Exception e){

        }
    }
}
