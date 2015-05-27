package org.lielas.micdataloggerstudio.main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.lielas.dataloggerstudio.lib.Logger.mic.MicStartTrigger;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.micdataloggerstudio.R;
import org.lielas.micdataloggerstudio.main.LielasToast;
import org.lielas.micdataloggerstudio.main.Tasks.StartLoggerTask;

/**
 * Created by Andi on 08.04.2015.
 */
public class SettingsFragment extends MicFragment {

    EditText edSamplerate;
    Integer[] maxSamplesArray;
    MicStartTrigger[] startTriggerArray;
    Spinner startTriggerSpinner;
    Spinner maxSamplesSpinner;

    public static final SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.settings_fragment, container, false);

        if(context != null){
            edSamplerate = (EditText) v.findViewById(R.id.txtSampleRate);

            //fill maxSamples Spinner
            maxSamplesArray = new Integer[32];
            for(int i = 1; i <= 32; i++){
                maxSamplesArray[i-1] = new Integer(i * 1000);
            }
            maxSamplesSpinner = (Spinner) v.findViewById(R.id.spinSamplpoints);
            ArrayAdapter<Integer> maxSamplesAdapter = new ArrayAdapter<Integer>(getActivity(), R.layout.spinner_item, maxSamplesArray);
            maxSamplesSpinner.setAdapter(maxSamplesAdapter);

            //fill startTrigger Spinner
            startTriggerSpinner = (Spinner) v.findViewById(R.id.spinStartTrigger);
            startTriggerArray = new MicStartTrigger[2];
            startTriggerArray[0] = new MicStartTrigger(MicStartTrigger.START_IMMEDIATELY);
            startTriggerArray[1] = new MicStartTrigger(MicStartTrigger.START_KEY);
            ArrayAdapter<MicStartTrigger> startTriggerArrayAdapter = new ArrayAdapter<MicStartTrigger>(getActivity(), R.layout.spinner_item, startTriggerArray);
            startTriggerSpinner.setAdapter(startTriggerArrayAdapter);

            Button startLogging = (Button)v.findViewById(R.id.bttnStartLogger);
            startLogging.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonPressed(v);
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

        if(stick.getCommunicationInterface().isOpen()){
            edSamplerate.setText(Long.toString(stick.getSampleRate()));
        }
    }

    private void onButtonPressed(View v){
        MicUSBStick stick = (MicUSBStick)logger;
        Integer samples = (Integer) maxSamplesSpinner.getSelectedItem();
        int samplerate;
        MicStartTrigger startTrigger = (MicStartTrigger) startTriggerSpinner.getSelectedItem();

        if(!stick.getCommunicationInterface().isOpen()){
            LielasToast.show("no logger connected", getActivity());
            return;
        }

        try{
            samplerate = (int)Long.parseLong(edSamplerate.getText().toString());
        }catch(Exception e){
            e.printStackTrace();
            LielasToast.show("Failed to set samplerate. Please choose a samplerate between 1 and 86000 seconds.", getActivity());
            return;
        }

        if(samplerate < 1){
            LielasToast.show("Failed to set samplerate. Please choose a samplerate between 1 and 86000 seconds.", getActivity());
            return;
        }

        if(samplerate > 86000){
            LielasToast.show("Failed to set samplerate. Please choose a samplerate between 1 and 86000 seconds.", getActivity());
            return;
        }

        stick.setSampleRate(samplerate);
        stick.setMaxSamples(samples);
        stick.setMicStartTrigger(startTrigger);

        StartLoggerTask task = new StartLoggerTask(stick, updateManager);
        task.execute();
    }
}
