package org.lielas.lielasdataloggerstudio.main.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.lielasdataloggerstudio.R;
import org.lielas.lielasdataloggerstudio.main.LielasToast;
import org.lielas.lielasdataloggerstudio.main.LoggerRecordManager;
import org.lielas.lielasdataloggerstudio.main.Tasks.DeleteLogfilesTask;
import org.lielas.lielasdataloggerstudio.main.Tasks.StartLoggerTask;
import org.lielas.lielasdataloggerstudio.main.Tasks.StopLoggerTask;

public class LoggerSettingsFragment extends LielasFragment{

    TextView loggerStatus;
    EditText samplerate;
    EditText logfilename;
    Button bttnStartLogger;
    Button bttnDeleteLogger;

    private ImageButton navRight;
    private ImageButton navLeft;

    public static final LoggerSettingsFragment newInstance(){
        LoggerSettingsFragment f = new LoggerSettingsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.logger_settings_fragment, container, false);

        if(context != null){
            loggerStatus = (TextView) v.findViewById(R.id.lblLoggerSettingsStatusData);
            samplerate = (EditText) v.findViewById(R.id.txtLoggerSettingsSamplerateData);
            logfilename= (EditText) v.findViewById(R.id.txtLoggerSettingsLogfilenameData);

            bttnStartLogger = (Button) v.findViewById(R.id.bttnLoggerSettings);
            bttnStartLogger.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onButtonStartClick(v);
                }
            });

            bttnDeleteLogger = (Button) v.findViewById(R.id.bttnLoggerSettingsDelete);
            bttnDeleteLogger.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onButtonDeleteClick(v);
                }
            });

            navRight = (ImageButton)v.findViewById(R.id.logSettingsNavRight);
            navRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPager pager = (ViewPager)getActivity().findViewById(R.id.viewpager);
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
            });

            navLeft = (ImageButton)v.findViewById(R.id.logSettingsNavLeft);
            navLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPager pager = (ViewPager)getActivity().findViewById(R.id.viewpager);
                    pager.setCurrentItem(pager.getCurrentItem() - 1);
                }
            });

            updateUI();
        }

        return v;
    }

    private void onButtonStartClick(View v){
        long lsamplerate;
        String name;
        UsbCube cube = (UsbCube)logger;


        if(!logger.getCommunicationInterface().isOpen()){
            LielasToast.show("no logger connected", getActivity());
            return;
        }

        if(!cube.isLogging()){
            if(((UsbCube) logger).isRealtimeLogging()){
                LielasToast.show("Please stop realtime logging before starting logging.", getActivity());
                return;
            }

            //check samplerate
            try{
                lsamplerate = Long.parseLong(samplerate.getText().toString());
            }catch(NumberFormatException e){
                LielasToast.show("Failed to set samplerate. Please choose a samplerate between 1 and 86000 seconds.", getActivity());
                return;
            }

            if(lsamplerate < 1){
                LielasToast.show("Failed to set samplerate. Please choose a samplerate between 1 and 86000 seconds.", getActivity());
                return;
            }

            if(lsamplerate > 86000){
                LielasToast.show("Failed to set samplerate. Please choose a samplerate between 1 and 86000 seconds.", getActivity());
                return;
            }

            logger.setSampleRate(lsamplerate);

            //check logfile name
            name = logfilename.getText().toString();

            if(name == null || name.equals("")){
                cube.setLogfileName(null);
            }else{
                if(name.length() > 30){
                    name = name.substring(0, 30);
                }
                cube.setLogfileName(name);
            }

            StartLoggerTask task = new StartLoggerTask(cube, updateManager);
            task.execute();

        }else{
            StopLoggerTask task = new StopLoggerTask(cube, updateManager);
            task.execute();
        }
    }

    private void onButtonDeleteClick(View v){


        if(!logger.getCommunicationInterface().isOpen()){
            LielasToast.show("no logger connected", getActivity());
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete all logfiles?");
        builder.setTitle("Delete Logfiles");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteLogfilesTask task = new DeleteLogfilesTask((UsbCube) logger, updateManager);
                task.execute();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
        UsbCube l = (UsbCube) logger;

        if(this.loggerStatus == null){
            return;
        }

        if(l == null || l.getCommunicationInterface() == null){
            return;
        }

        if(l.getCommunicationInterface().isOpen()){
            if(l.isLogging()) {
                loggerStatus.setText("logging");
                bttnStartLogger.setText("Stop logging");
            }else{
                loggerStatus.setText("stopped");
                bttnStartLogger.setText("Start logging");
            }
            samplerate.setText(Long.toString(l.getSampleRate()));
            logfilename.setText("");


        }else{
            loggerStatus.setText("");
            samplerate.setText("");
            logfilename.setText("");
        }
    }
}

