package org.lielas.lielasdataloggerstudio.main.Fragments;

import android.app.ActionBar;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.lielas.dataloggerstudio.lib.Dataset;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.DatasetItemIds;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.DatasetStructure;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.lielasdataloggerstudio.R;
import org.lielas.lielasdataloggerstudio.main.LoggerRecordManager;
import org.lielas.lielasdataloggerstudio.main.Table.LoggerRecordTableAdapter;

import java.util.Date;

/**
 * Created by Andi on 21.02.2015.
 */
public class ViewFragment extends LielasFragment {

    View view;
    ListView table;
    Spinner logfilesSpinner;
    int datasetcount;

    private boolean userinteraction;

    private ImageButton navRight;
    private ImageButton navLeft;

    LinearLayout tblHeaderLayout;

    public static ViewFragment newInstance(){
        ViewFragment f = new ViewFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.view_fragment, container, false);
        view = v;
        datasetcount = 0;

        if(context != null){
            table = (ListView)v.findViewById(R.id.viewTblContent);
            logfilesSpinner = (Spinner)v.findViewById(R.id.spViewLogfiles);
            logfilesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    LogfileSpinnerSelectionChanged();
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

            navRight = (ImageButton)v.findViewById(R.id.viewNavRight);
            navRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPager pager = (ViewPager) getActivity().findViewById(R.id.viewpager);
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
            });

            navLeft = (ImageButton)v.findViewById(R.id.viewNavLeft);
            navLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPager pager = (ViewPager) getActivity().findViewById(R.id.viewpager);
                    pager.setCurrentItem(pager.getCurrentItem() - 1);
                }
            });

            tblHeaderLayout = (LinearLayout)v.findViewById(R.id.viewDataHeader);

            createTable();
            updateUI();
        }

        return v;
    }

    private void createTable(){
        UsbCube cube = (UsbCube) logger;
        DatasetStructure ds = cube.getDatasetStructure();

        LoggerRecord lr = (LoggerRecord)logfilesSpinner.getSelectedItem();

        if(lr!= null){
            datasetcount = lr.getCount();
        }

        if(this.logger != null && ds != null){
            //create table header
            table.setVisibility(View.VISIBLE);

            tblHeaderLayout.removeAllViews();
            tblHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            double widthRation = 1. / (cube.getChannels() + 1);
            layoutParams.weight = (float)widthRation;

            TextView txtDate = new TextView(getActivity());
            txtDate.setText("Date");
            txtDate.setWidth(0);
            txtDate.setTypeface(txtDate.getTypeface(), Typeface.BOLD);
            txtDate.setTextColor(Color.BLACK);
            tblHeaderLayout.addView(txtDate, layoutParams);

            for(int i = 1; i < (cube.getChannels() + 1); i++){
                TextView txtColumn = new TextView(getActivity());

                switch (ds.getItem(i+1).getItemId()){
                    case DatasetItemIds.SHT_T:
                    case DatasetItemIds.PT1K1:
                    case DatasetItemIds.PT1K2:
                    case DatasetItemIds.PT1K3:
                    case DatasetItemIds.PT1K4:
                        txtColumn.setText("Temp. [°C]");
                        break;
                    case DatasetItemIds.SHT_H:
                        txtColumn.setText("R.H. [%]");
                        break;
                    case DatasetItemIds.MS5607:
                        txtColumn.setText("p [mbar]");
                        break;
                    default:
                        txtColumn.setText("");
                        break;
                }

                txtColumn.setTypeface(txtColumn.getTypeface(), Typeface.BOLD);
                txtColumn.setTextColor(Color.BLACK);
                txtColumn.setGravity(Gravity.CENTER);
                txtColumn.setWidth(0);
                tblHeaderLayout.addView(txtColumn, layoutParams);

            }

            LoggerRecordTableAdapter tableAdapter = new LoggerRecordTableAdapter(getActivity(), lr);
            table.setAdapter(tableAdapter);

        }else{
            table.setVisibility(View.INVISIBLE);
        }

    }

    private void LogfileSpinnerSelectionChanged(){

        if(!userinteraction) {
            return;
        }
        userinteraction = false;

        LoggerRecord lr = (LoggerRecord)logfilesSpinner.getSelectedItem();

        if(LoggerRecordManager.getInstance().getActiveLogggerRecord() == null ||
                lr.getId() != LoggerRecordManager.getInstance().getActiveLogggerRecord().getId()) {
            LoggerRecordManager.getInstance().setActiveLogggerRecord(lr);
            updateManager.update();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void update() {
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

            LoggerRecord lr = LoggerRecordManager.getInstance().getActiveLogggerRecord();

            if(lr!= null){

                LoggerRecord loggerRecords[] = l.getRecordsetArray();
                for(int i  = 0; i < loggerRecords.length; i++){
                    if(loggerRecords[i].getId() == lr.getId()){
                        logfilesSpinner.setSelection(i);
                        break;
                    }
                }

                if(datasetcount != lr.getCount()){
                    createTable();
                }
            }
        }else if(l.getRecordCount() == 0){

            //create empty adapter
            String[] emptyString = new String[1];
            emptyString[0] = " ";
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.logfile_spinner_item, emptyString);
            logfilesSpinner.setAdapter(adapter);

            table.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
}
