package org.lielas.micdataloggerstudio.main.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.micdataloggerstudio.R;
import org.lielas.micdataloggerstudio.main.LoggerRecordManager;
import org.lielas.micdataloggerstudio.main.Table.LoggerRecordTableAdapter;

/**
 * Created by Andi on 08.04.2015.
 */
public class ViewFragment extends MicFragment {

    ListView table;
    LinearLayout tblHeaderLayout;
    int datasetcount;

    public static final ViewFragment newInstance(){
        return new ViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.view_fragment, container, false);

        if(context != null){
            table = (ListView) v.findViewById(R.id.viewTblContent);
            tblHeaderLayout = (LinearLayout)v.findViewById(R.id.viewDataHeader);
        }
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        datasetcount = 0;
        update();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void update() {
        updateUI();
    }

    public void updateUI(){
        MicUSBStick stick = (MicUSBStick) logger;

        if(stick == null || stick.getCommunicationInterface() == null){
            return;
        }

        if(stick.getCommunicationInterface().isOpen()){
            LoggerRecord lr = LoggerRecordManager.getInstance().getActiveLoggerRecord();

            if(lr != null){


                if(lr.getCount() != datasetcount){
                    createTable();
                    datasetcount = lr.getCount();
                }

            }
        }
    }

    private void createTable(){
        MicUSBStick stick = (MicUSBStick)logger;

        LoggerRecord lr = LoggerRecordManager.getInstance().getActiveLoggerRecord();

        if(lr!= null){
            datasetcount = lr.getCount();
        }

        if(this.logger != null && lr.getCount() > 0){
            //create table header
            table.setVisibility(View.VISIBLE);

            tblHeaderLayout.removeAllViews();
            tblHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            double widthRation = 1. / (stick.getModel().getChannels() + 1);
            layoutParams.weight = (float)widthRation;

            TextView txtDate = new TextView(getActivity());
            txtDate.setText("Date");
            txtDate.setWidth(0);
            txtDate.setTextAppearance(getActivity(), R.style.MyTableHeaderStyle);
            tblHeaderLayout.addView(txtDate, layoutParams);

            for(int i = 1; i < (stick.getModel().getChannels() + 1); i++){
                TextView txtColumn = new TextView(getActivity());

                txtColumn.setText(stick.getModel().getChannel(i - 1).getUnit(stick.getUnitClass()));
                txtColumn.setGravity(Gravity.CENTER);
                txtColumn.setWidth(0);
                txtColumn.setTextAppearance(getActivity(), R.style.MyTableHeaderStyle);
                tblHeaderLayout.addView(txtColumn, layoutParams);

            }

            lr.getUnitClass().setUnitClass(stick.getUnitClass().getUnitClass());
            LoggerRecordTableAdapter tableAdapter = new LoggerRecordTableAdapter(getActivity(), lr);
            table.setAdapter(tableAdapter);

        }else{
            table.setVisibility(View.INVISIBLE);
        }

    }

}
