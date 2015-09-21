package org.lielas.lielasdataloggerstudio.main.Tasks;

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.lielasdataloggerstudio.main.CommunicationInterface.UsbCube.UsbCubeSerialInterface;
import org.lielas.lielasdataloggerstudio.main.LoggerRecordManager;

/**
 * Created by Andi on 26.03.2015.
 */
public class StopLoggerTask extends LielasTask<Void, Void, UsbCube> {
    private UsbCube logger;

    public StopLoggerTask(UsbCube log, UpdateManager updateManager){
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

        while(com.isBusy()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        com.setBusy(true);

        if(!com.setLoggerStatus(logger, false)){
            return null;
        }

        //get logfile count
        int oldRecordCount = logger.getRecordCount();
        if(!com.getLogfileCount((UsbCube)logger)){
            return null;
        }

        for(int i = oldRecordCount; i < logger.getRecordCount(); i++){
            com.getLogfileProperties(logger, i);
        }

        return logger;
    }


    @Override
    protected void onPostExecute(UsbCube log){
        logger.getCommunicationInterface().setBusy(false);
        try {
            UsbCube cube = get();
            if (cube != null) {
                LoggerRecordManager lrm = LoggerRecordManager.getInstance();
                if(lrm.getActiveLoggerRecord() == null){
                    lrm.setActiveLoggerRecord(cube.getRecordset(0));
                }

                updateManager.update("Logger successfully stopped");

            }
        }catch(Exception e){
        }
    }

}
