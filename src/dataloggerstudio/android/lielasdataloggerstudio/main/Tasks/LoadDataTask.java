package org.lielas.lielasdataloggerstudio.main.Tasks;

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.lielasdataloggerstudio.main.CommunicationInterface.UsbCube.UsbCubeSerialInterface;

import java.util.Date;

/**
 * Created by Andi on 30.03.2015.
 */
public class LoadDataTask extends LielasTask<Void, Integer, UsbCube> {
    private UsbCube logger;
    LoggerRecord lr;

    public LoadDataTask(UsbCube log, UpdateManager updateManager, LoggerRecord lr){
        super(updateManager);
        this.logger = log;
        this.lr = lr;
    }

    @Override
    protected UsbCube doInBackground(Void... params) {
        long id;
        int tries = 3;
        int j;
        long start = new Date().getTime();
        long dt = 0;

        if(logger==null){
            return null;
        }

        UsbCubeSerialInterface com = (UsbCubeSerialInterface)logger.getCommunicationInterface();

        if(!com.isOpen()){
            return null;
        }

        for(id = lr.getStartIndex(); id <= lr.getEndIndex(); id++){
            for(j = 0; j < tries; j++){
                if(com.getLogfileDataset(logger, lr, id)){
                    break;
                }
                if(j == tries){
                    return null;
                }
            }

            dt = new Date().getTime();
            if((dt - start) > 100){
                start = dt;
                publishProgress((int)(id - lr.getStartIndex()));
            }

        }
        publishProgress((int)(id - lr.getStartIndex()));

        return logger;
    }

    @Override protected void onProgressUpdate(Integer... progress){
        if(progress == null){
            return;
        }

        try{
            updateManager.updateProgress(progress[progress.length-1]);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(UsbCube log){
        try {
            if (get() != null) {
                updateManager.update("Logfile successfully loaded");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
