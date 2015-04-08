package org.lielas.lielasdataloggerstudio.main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.LoggerManager;
import org.lielas.lielasdataloggerstudio.R;
import org.lielas.lielasdataloggerstudio.main.LielasToast;
import org.lielas.lielasdataloggerstudio.main.Tasks.SetClockTask;

import java.util.Date;
import java.util.TimerTask;

public class ClockSettingsFragment extends LielasFragment{

    TextView loggerClock;
    TextView phoneClock;

    String loggerTm;
    String phoneTm;

    Button bttnSetClock;


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

            bttnSetClock = (Button) v.findViewById(R.id.bttnClockSettingsSetClock);
            bttnSetClock.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onButtonClick(v);
                }
            });

            ImageButton navRight = (ImageButton)v.findViewById(R.id.clockSettingsNavRight);
            navRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPager pager = (ViewPager) getActivity().findViewById(R.id.viewpager);
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
            });

            ImageButton navLeft = (ImageButton)v.findViewById(R.id.clockSettingsNavLeft);
            navLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPager pager = (ViewPager)getActivity().findViewById(R.id.viewpager);
                    pager.setCurrentItem(pager.getCurrentItem() - 1);
                }
            });
        }

        return v;
    }

    private void onButtonClick(View v){
        UsbCube cube = (UsbCube)logger;

        if(!logger.getCommunicationInterface().isOpen()){
            LielasToast.show("no logger connected", getActivity());
            return;
        }

        if(cube.isLogging()){
            LielasToast.show("Logging in progress. You have to stop the logger before setting a new date/time.", getActivity());
            return;
        }

        if(cube.isRealtimeLogging()) {
            LielasToast.show("Realtimelogging in progress. You have to stop the logger before setting a new date/time.", getActivity());
            return;
        }

        cube.setDatetime(new Date().getTime());

        SetClockTask task = new SetClockTask(cube, updateManager);
        task.execute();
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
    public void update(){
        updateUI();
    }

    public void updateUI(){

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
