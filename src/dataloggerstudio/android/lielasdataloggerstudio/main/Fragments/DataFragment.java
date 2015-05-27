package org.lielas.lielasdataloggerstudio.main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.lielasdataloggerstudio.R;
import org.lielas.lielasdataloggerstudio.main.LielasToast;
import org.lielas.lielasdataloggerstudio.main.LoggerRecordManager;
import org.lielas.lielasdataloggerstudio.main.Tasks.LoadDataTask;

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

    Button bttn;

    private boolean userinteraction;

    private ImageButton navRight;
    private ImageButton navLeft;

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

                }
            });

            userinteraction = false;
            logfilesSpinner.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    userinteraction = true;
                    return false;
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

            bttn = (Button) v.findViewById(R.id.bttnData);
            bttn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onButtonClick(v);
                }
            });


            navRight = (ImageButton)v.findViewById(R.id.dataNavRight);
            navRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPager pager = (ViewPager)getActivity().findViewById(R.id.viewpager);
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
            });

            navLeft = (ImageButton)v.findViewById(R.id.dataNavLeft);
            navLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPager pager = (ViewPager)getActivity().findViewById(R.id.viewpager);
                    pager.setCurrentItem(pager.getCurrentItem() - 1);
                }
            });
        }

        updateUI();

        return v;
    }

    private void onButtonClick(View v){
        LoggerRecord lr = (LoggerRecord)logfilesSpinner.getSelectedItem();


        if(!logger.getCommunicationInterface().isOpen()){
            LielasToast.show("no logger connected", getActivity());
            return;
        }

        if(lr == null){
            return;
        }

        LoadDataTask task = new LoadDataTask((UsbCube)logger, updateManager, lr);
        task.execute();
        lgProgressData.setVisibility(View.VISIBLE);
        lgProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void update(){
        updateUI();
    }

    @Override
    public void onResume(){
        super.onResume();
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

        if(l.getCommunicationInterface().isOpen() && l.getRecordCount() > 0){
            ArrayAdapter<LoggerRecord> adapter = new ArrayAdapter<LoggerRecord>(getActivity(), R.layout.logfile_spinner_item, l.getRecordsetArray());
            logfilesSpinner.setAdapter(adapter);

            LoggerRecord lr = LoggerRecordManager.getInstance().getActiveLoggerRecord();

            if(lr != null) {
                LoggerRecord loggerRecords[] = l.getRecordsetArray();
                for(int i  = 0; i < loggerRecords.length; i++){
                    if(loggerRecords[i].getId() == lr.getId()){
                        logfilesSpinner.setSelection(i);
                        break;
                    }
                }

                lgStart.setText(lr.getDatetimeString());
                lgEnd.setText(lr.getEndDatetimeString());
                lgSamplerate.setText(lr.getSampleRateString());
                lgSamples.setText(Long.toString(lr.getEndIndex() - lr.getStartIndex() + 1));
            }
        }else if(l.getRecordCount() == 0){
            lgStart.setText("");
            lgEnd.setText("");
            lgSamples.setText("");
            lgSamplerate.setText("");

            //create empty adapter
            String[] emptyString = new String[1];
            emptyString[0] = " ";
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.logfile_spinner_item, emptyString);
            logfilesSpinner.setAdapter(adapter);
        }
    }

    public void updateProgress(Integer progress){

        LoggerRecord lr = (LoggerRecord)logfilesSpinner.getSelectedItem();

        if(lr == null){
            return;
        }

        lgProgressData.setText(progress.toString() + "/" + Long.toString(lr.getEndIndex() - lr.getStartIndex() + 1));
    }

    private void UpdateLogfileData(){
        if(!userinteraction) {
            return;
        }
        userinteraction = false;

        lgProgressData.setVisibility(View.INVISIBLE);
        lgProgress.setVisibility(View.INVISIBLE);

        LoggerRecord lr = (LoggerRecord)logfilesSpinner.getSelectedItem();

        if(LoggerRecordManager.getInstance().getActiveLoggerRecord() == null ||
                lr.getId() != LoggerRecordManager.getInstance().getActiveLoggerRecord().getId()) {
            LoggerRecordManager.getInstance().setActiveLoggerRecord(lr);
            updateManager.update();
        }

    }
}
