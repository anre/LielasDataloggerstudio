package org.lielas.lielasdataloggerstudio.main.Fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.lielasdataloggerstudio.R;

/**
 * Created by Andi on 21.02.2015.
 */
public class DataFragment extends LielasFragment {

    Spinner logfilesSpinner;
    TextView lgStart;
    TextView lgEnd;
    TextView lgSamplerate;
    TextView lgSamples;
    TextView lgProgress;
    TextView lgProgressData;

    public static DataFragment newInstance(){
        DataFragment f = new DataFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.data_fragment, container, false);

        if(context != null){
            logfilesSpinner = (Spinner)v.findViewById(R.id.spDataLogfiles);
            logfilesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    UpdateLogfileData();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    UpdateLogfileData();
                }
            });


            lgStart = (TextView)v.findViewById(R.id.lblDataStartData);
            lgEnd = (TextView)v.findViewById(R.id.lblDataEndData);
            lgSamplerate = (TextView)v.findViewById(R.id.lblDataSamplerateData);
            lgSamples = (TextView)v.findViewById(R.id.lblDataSamplesData);

            lgProgress = (TextView)v.findViewById(R.id.lblDataProgress);
            lgProgress.setVisibility(View.INVISIBLE);
            lgProgressData = (TextView)v.findViewById(R.id.lblDataProgressData);
            lgProgressData.setVisibility(View.INVISIBLE);
        }

        updateUI();

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

        if(this.logfilesSpinner == null){
            return;
        }

        if(l == null || l.getCommunicationInterface() == null){
            return;
        }

        if(l.getCommunicationInterface().isOpen()){
            ArrayAdapter<LoggerRecord> adapter = new ArrayAdapter<LoggerRecord>(getActivity(), R.layout.logfile_spinner_item, l.getRecordsetArray());
            logfilesSpinner.setAdapter(adapter);

            UpdateLogfileData();

        }else{
        }
    }

    private void UpdateLogfileData(){

        LoggerRecord lr = (LoggerRecord)logfilesSpinner.getSelectedItem();

        if(lr == null){
            return;
        }

        lgStart.setText(lr.getDatetimeString());
        lgEnd.setText(lr.getEndDatetimeString());
        lgSamplerate.setText(lr.getSampleRateString());
        lgSamples.setText(Long.toString(lr.getEndIndex() - lr.getStartIndex() + 1));
    }
}
