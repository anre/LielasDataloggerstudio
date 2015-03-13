package org.lielas.lielasdataloggerstudio.main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.LoggerManager;
import org.lielas.lielasdataloggerstudio.R;

import java.util.Date;
import java.util.TimerTask;

public class ClockSettingsFragment extends LielasFragment{

    TextView loggerClock;
    TextView phoneClock;

    String loggerTm;
    String phoneTm;

    final Handler guiUpdateHandler = new Handler();

    public static final ClockSettingsFragment newInstance(){
        ClockSettingsFragment f = new ClockSettingsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.clock_settings_fragment, container, false);

        if(context != null){
            loggerClock = (TextView) v.findViewById(R.id.lblClockSettingsLoggerDtData);
            phoneClock = (TextView) v.findViewById(R.id.lblClockSettingsPhoneDtData);

            updateUI();
            java.util.Timer t = new java.util.Timer();
            UpdateTimeTask updateTimeTask = new UpdateTimeTask();
            updateTimeTask.setLogger((UsbCube)logger);
            t.schedule(updateTimeTask, 1000, 1000);
        }

        return v;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void update(){
        updateUI();
    }

    public void updateUI(){
        UsbCube l = (UsbCube) logger;

        if(l == null || l.getCommunicationInterface() == null){
            return;
        }

        if(this.loggerClock == null){
            return;
        }

    }

    final Runnable UpdateGUI = new Runnable() {
        @Override
        public void run() {
            loggerClock.setText(loggerTm);
            phoneClock.setText(phoneTm);
        }
    };

    class UpdateTimeTask extends TimerTask {

        UsbCube logger;
        
        public void setLogger(UsbCube logger){
            this.logger = logger;
        }

        @Override
        public void run() {

            if(logger == null){
                return;
            }

            Date pcDate = new Date();
            Date loggerDate = new Date(logger.getDatetime());
            long timediff = loggerDate.getTime() - pcDate.getTime();
            org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");

            if(timediff > 2000 || timediff < -2000){
                loggerTm = formatter.print(loggerDate.getTime());
                phoneTm = formatter.print(pcDate.getTime());
            }else{
                loggerTm = formatter.print(pcDate.getTime());
                phoneTm = formatter.print(pcDate.getTime());
            }
            guiUpdateHandler.post(UpdateGUI);
        }
    }
}
