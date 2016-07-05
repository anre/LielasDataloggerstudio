package org.lielas.micdataloggerstudio.main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.micdataloggerstudio.R;
import org.lielas.micdataloggerstudio.main.LielasToast;
import org.lielas.micdataloggerstudio.main.LoggerRecordManager;
import org.lielas.micdataloggerstudio.main.Tasks.LoadDataTask;
import org.lielas.micdataloggerstudio.main.Tasks.UpdateStatusTask;

/**
 * Created by Andi on 08.04.2015.
 */
public class DataFragment extends MicFragment{

    TextView txtStart;
    TextView txtEnd;
    TextView txtSamples;
    TextView txtSamplerate;
    TextView txtProgress;
    TextView txtProgressContent;

    public static final DataFragment newInstance(){
        return new DataFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.data_fragment, container, false);

        if(context != null){
            txtStart = (TextView) v.findViewById(R.id.lblLoggerStarttimeContent);
            txtEnd = (TextView) v.findViewById(R.id.lblLoggerEndtimeContent);
            txtSamples = (TextView)v.findViewById(R.id.lblLogfileSamplesContent);
            txtSamplerate = (TextView) v.findViewById(R.id.lblLogfileSamplerateContent);
            txtProgress = (TextView)v.findViewById(R.id.lblProgress);
            txtProgress.setVisibility(View.INVISIBLE);
            txtProgressContent = (TextView)v.findViewById(R.id.lblProgressContent);
            txtProgressContent.setVisibility(View.INVISIBLE);

            Button loadDataBttn = (Button)v.findViewById(R.id.bttnLoadData);
            loadDataBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClick(v);
                }
            });

            Button updateDataBttn = (Button)v.findViewById(R.id.bttnUpdateData);
            updateDataBttn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    onButtonUpdateClick(v);
                }
            });
        }

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        update();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void update() {
        MicUSBStick stick = (MicUSBStick) logger;

        if(stick == null || stick.getCommunicationInterface() == null){
            return;
        }

        if(txtStart == null){
            return;
        }

        if(stick.getCommunicationInterface().isOpen()){
            long recordCountEnd = stick.getRecordCount() / stick.getModel().getChannelCount();
            if(recordCountEnd > 1){
                recordCountEnd -= 1;
            }
            long end = stick.getFirstDatasetDate() + stick.getSampleRate() * recordCountEnd * 1000;

            txtStart.setText(Logger.dateToString(stick.getFirstDatasetDate()));
            txtEnd.setText(Logger.dateToString(end));
            txtSamplerate.setText(Long.toString(stick.getSampleRate())+ "s");
            txtSamples.setText(Integer.toString(stick.getRecordCount()/ stick.getModel().getChannelCount()));

            LoggerRecord lr = LoggerRecordManager.getInstance().getActiveLoggerRecord();

            if(lr != null) {
                txtProgressContent.setText(lr.getCount() + "/" + Integer.toString(stick.getRecordCount() / stick.getModel().getChannelCount()));
            }
        }
    }

    public void updateProgress(Integer progress){
        MicUSBStick stick = (MicUSBStick) logger;

        txtProgressContent.setText(progress.toString() + "/" + Integer.toString(stick.getRecordCount() / stick.getModel().getChannelCount()));
    }


    private void onButtonUpdateClick(View v){

        if(!logger.getCommunicationInterface().isOpen()){
            LielasToast.show("no logger connected", getActivity());
            return;
        }

        if(org.lielas.micdataloggerstudio.main.LoggerRecordManager.getInstance().getActiveLoggerRecord() == null){
            return;
        }

        UpdateStatusTask task = new UpdateStatusTask((MicUSBStick)logger, updateManager);
        task.execute();
    }

    private void onButtonClick(View v){

        if(!logger.getCommunicationInterface().isOpen()){
            LielasToast.show("no logger connected", getActivity());
            return;
        }

        LoggerRecord r = new LoggerRecord(logger);
        LoggerRecordManager.getInstance().setActiveLoggerRecord(r);
        LoadDataTask loadDataTask = new LoadDataTask((MicUSBStick)logger, updateManager, org.lielas.micdataloggerstudio.main.LoggerRecordManager.getInstance().getActiveLoggerRecord());
        loadDataTask.execute();

        txtProgress.setVisibility(View.VISIBLE);
        txtProgressContent.setVisibility(View.VISIBLE);
    }
}
