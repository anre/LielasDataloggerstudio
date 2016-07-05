package org.lielas.micdataloggerstudio.main.Tasks;

import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.micdataloggerstudio.main.CommunicationInterface.mic.MicSerialInterface;
import org.lielas.micdataloggerstudio.main.UpdateManager;

import java.util.Date;

/**
 * Created by Andi on 30.04.2015.
 */
public class LoadDataTask extends MicTask<Void, Integer, MicUSBStick>{
    private MicUSBStick stick;
    LoggerRecord lr;

    public LoadDataTask(MicUSBStick stick, UpdateManager updateManager, LoggerRecord lr){
        super(updateManager);
        this.stick = stick;
        this.lr = lr;
    }

    @Override
    protected MicUSBStick doInBackground(Void... params){
        final int tries = 3;
        long start = new Date().getTime();
        long dt = 0;
        long datasetDt = stick.getFirstDatasetDate();

        if(stick == null){
            return null;
        }

        MicSerialInterface com = (MicSerialInterface) stick.getCommunicationInterface();

        if(!com.isOpen()){
            return null;
        }

        if(com.getData(stick, lr) == null){
            return null;
        }

        for(int i = 0; i < (stick.getRecordCount() / stick.getModel().getChannelCount()); i++){
            if(!com.addData(stick, lr,datasetDt)){
                return null;
            }

            dt = new Date().getTime();
            if((dt - start) > 100){
                start = dt;
                publishProgress(i);
            }

            datasetDt += stick.getSampleRate() * 1000;

        }

        return stick;
    }

    @Override
    protected void onProgressUpdate(Integer... progress){
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
    protected void onPostExecute(MicUSBStick log){
        try{
            if(get() != null){
                updateManager.update("Logfile successfully loaded");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
