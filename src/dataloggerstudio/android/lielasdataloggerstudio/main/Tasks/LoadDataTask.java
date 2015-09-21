package org.lielas.lielasdataloggerstudio.main.Tasks;

import android.util.Log;

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
        long id = 0;
        int tries = 3;
        int j;
        long start = new Date().getTime();
        long dStart = start;
        long dt = 0;
        long tmpId;

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

        /*for(id = lr.getStartIndex(); id <= lr.getEndIndex(); id++){
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
        publishProgress((int)(id - lr.getStartIndex()));*/

        lr.clearData();
        tmpId = com.getLdpPaket(logger, lr, true);
        while(tmpId > 0){
            id += tmpId;
            tmpId = com.getLdpPaket(logger, lr, false);
            publishProgress((int)(id));
        }

        if(lr.getCount() < 1){
            id = 0;
        }else{
            id = lr.get(0).getId();
        }


        /*for(int i = 0; i < (lr.getEndIndex() - lr.getStartIndex() + 1); i++){
            if(lr.get(i) == null || lr.get(i).getId() != (id+1)){
                //missing a dataset, try to get it via settings manager
                com.getLogfileDataset(logger, lr, id+1);
                lr.sort();
                publishProgress((int)(id+1));
            }
            try {
                id = lr.get(i).getId();
            }catch(Exception e){
                return logger;
            }
        }*/

        Log.d("DOWN", "Download: " + Long.toString(new Date().getTime() - dStart));

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
        logger.getCommunicationInterface().setBusy(false);

        try {
            if (get() != null) {
                updateManager.update("Logfile successfully loaded");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
