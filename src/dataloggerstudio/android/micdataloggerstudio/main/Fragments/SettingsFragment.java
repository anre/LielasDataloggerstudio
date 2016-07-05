package org.lielas.micdataloggerstudio.main.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.lielas.dataloggerstudio.lib.Logger.Units.UnitClass;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicStartTrigger;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.micdataloggerstudio.R;
import org.lielas.micdataloggerstudio.main.LielasToast;
import org.lielas.micdataloggerstudio.main.LoggerRecordManager;
import org.lielas.micdataloggerstudio.main.Tasks.StartLoggerTask;
import org.lielas.micdataloggerstudio.main.Tasks.StopLoggerTask;
import org.lielas.micdataloggerstudio.main.UpdateManager;

/**
 * Created by Andi on 08.04.2015.
 */
public class SettingsFragment extends MicFragment {

    EditText edSamplerate;
    Integer[] maxSamplesArray;
    MicStartTrigger[] startTriggerArray;
    Spinner startTriggerSpinner;
    Spinner maxSamplesSpinner;
    Spinner unitsSpinner;
    Button startLogging;
    TextView statusLbl;

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

            startLogging = (Button)v.findViewById(R.id.bttnStartLogger);
            startLogging.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonPressed(v);
                }
            });

            statusLbl = (TextView)v.findViewById(R.id.lblLoggerStatusContent);

            //fill units Spinner
            unitsSpinner = (Spinner) v.findViewById(R.id.spinUnits);


            String[] unitStrings = new String[2];
            unitStrings[0] = "metric";
            unitStrings[1] = "imperial";

            ArrayAdapter<String> unitsArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, unitStrings);
            unitsSpinner.setAdapter(unitsArrayAdapter);
            unitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    onSpinnerUnitsChanged(unitsSpinner.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    onSpinnerUnitsChanged("metric");
                }
            });

            MicUSBStick stick = (MicUSBStick) logger;
            if(logger.getUnitClass().getUnitClass() == UnitClass.UNIT_CLASS_IMPERIAL){
                unitsSpinner.setSelection(unitsArrayAdapter.getPosition("imperial"));
            }else{
                unitsSpinner.setSelection(unitsArrayAdapter.getPosition("metric"));
            }

            /*SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
            String unit = sharedPreferences.getString("unit_class", "imperial");

            unitsSpinner.setSelection(unitsArrayAdapter.getPosition(unit));*/
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

        //fill maxSamples Spinner
        try {
            maxSamplesArray = new Integer[(stick.getModel().getMaxSamples() / 1000)];
            for (int i = 1; i <= maxSamplesArray.length; i++) {
                maxSamplesArray[i - 1] = new Integer(i * 1000);
            }
            ArrayAdapter<Integer> maxSamplesAdapter = new ArrayAdapter<Integer>(getActivity(), R.layout.spinner_item, maxSamplesArray);
            maxSamplesSpinner.setAdapter(maxSamplesAdapter);
        }catch (NullPointerException e){};


        if(stick.getCommunicationInterface().isOpen()){
            edSamplerate.setText(Long.toString(stick.getSampleRate()));

            if(stick.getLoggingStatus()){
                startLogging.setText("Stop");
                statusLbl.setText("logging");
            }else{
                startLogging.setText("Start");
                statusLbl.setText("stopped");
            }
        }
    }

    private void onSpinnerUnitsChanged(String unitClass){
        MicUSBStick stick = (MicUSBStick)logger;
        boolean changed = false;

        if(stick != null){
            //SharedPreferences.Editor editor = getActivity().getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit();

            if(unitClass.equals("imperial") && stick.getUnitClass().getUnitClass() != UnitClass.UNIT_CLASS_IMPERIAL){
                stick.getUnitClass().setUnitClass(UnitClass.UNIT_CLASS_IMPERIAL);
                changed = true;
            }else if(unitClass.equals("metric") && stick.getUnitClass().getUnitClass() != UnitClass.UNIT_CLASS_METRIC){
                stick.getUnitClass().setUnitClass(UnitClass.UNIT_CLASS_METRIC);
                changed = true;
            }

            if(changed){
                LoggerRecordManager.getInstance().getActiveLoggerRecord().requestReprocessing();
                updateManager.update();
            }
        }
    }

    private void onButtonPressed(View v){
        MicUSBStick stick = (MicUSBStick)logger;
        if(stick.getLoggingStatus()){
            stopLogger();
        }else{
            startLogger();
        }
    }

    private void startLogger(){
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

    private void stopLogger(){
        MicUSBStick stick = (MicUSBStick)logger;



        StopLoggerTask task = new StopLoggerTask(stick, updateManager);
        task.execute();
    }
}
