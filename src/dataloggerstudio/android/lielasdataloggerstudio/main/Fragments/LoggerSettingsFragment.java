package org.lielas.lielasdataloggerstudio.main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.lielasdataloggerstudio.R;

public class LoggerSettingsFragment extends LielasFragment{

    TextView loggerStatus;
    EditText samplerate;
    EditText logfilename;

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

            updateUI();
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

        if(this.loggerStatus == null){
            return;
        }

        if(l == null || l.getCommunicationInterface() == null){
            return;
        }

        if(l.getCommunicationInterface().isOpen()){
            if(l.isLogging()) {
                loggerStatus.setText("logging");
            }else{
                loggerStatus.setText("stopped");
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

