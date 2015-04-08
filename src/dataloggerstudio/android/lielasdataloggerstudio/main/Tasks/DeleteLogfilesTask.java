package org.lielas.lielasdataloggerstudio.main.Tasks;

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.lielasdataloggerstudio.main.CommunicationInterface.UsbCube.UsbCubeSerialInterface;
import org.lielas.lielasdataloggerstudio.main.LoggerRecordManager;

/**
 * Created by Andi on 01.04.2015.
 */
public class DeleteLogfilesTask extends LielasTask<Void, Void, UsbCube>{
    private UsbCube logger;

    public DeleteLogfilesTask(UsbCube log, UpdateManager updateManager){
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

        if(!com.deleteLogfiles(logger)){
            return null;
        }

        return logger;
    }


    @Override
    protected void onPostExecute(UsbCube log){
        try {
            UsbCube cube = get();
            if (cube != null) {
                updateManager.update("Logfiles successfully deleted");
                cube.removeAllRecordsets();
                LoggerRecordManager.getInstance().setActiveLogggerRecord(null);
            }
        }catch(Exception e){
        }
    }
}
