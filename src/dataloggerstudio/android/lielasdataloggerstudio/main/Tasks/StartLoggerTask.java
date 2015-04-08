package org.lielas.lielasdataloggerstudio.main.Tasks;

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.lielasdataloggerstudio.main.CommunicationInterface.UsbCube.UsbCubeSerialInterface;

/**
 * Created by Andi on 26.03.2015.
 */
public class StartLoggerTask extends LielasTask<Void, Void, UsbCube> {
    private UsbCube logger;

    public StartLoggerTask(UsbCube log, UpdateManager updateManager){
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

        //set samplerate
        for(int i = 0; i < 3; i++){
            if(com.setSamplerate(logger)){
                break;
            }
            if(i == 2){
                return null;
            }
            com.flush();
            try{
                Thread.sleep(20);
            }catch(Exception e){

            }
        }

        //set logfilename
        if(logger.getLogfileName() != null) {
            for (int i = 0; i < 3; i++) {
                if (com.setLoggerFilename(logger)) {
                    break;
                }
                if (i == 2) {
                    return null;
                }
                com.flush();
                try {
                    Thread.sleep(20);
                } catch (Exception e) {

                }
            }
        }

        for(int i = 0; i < 3; i++){
            if(com.setLoggerStatus(logger, true)){
                break;
            }
            if(i == 2){
                return null;
            }
            com.flush();
            try{
                Thread.sleep(20);
            }catch(Exception e){

            }
        }
        
        return logger;
    }


    @Override
    protected void onPostExecute(UsbCube log){
        try {
            if (get() != null) {
                updateManager.update("Logger successfully started");
            }
        }catch(Exception e){
        }
    }
}
