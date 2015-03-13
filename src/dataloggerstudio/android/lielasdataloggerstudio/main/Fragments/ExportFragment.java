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

import org.lielas.dataloggerstudio.lib.FileCreator.FileCreator;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.lielasdataloggerstudio.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andi on 21.02.2015.
 */
public class ExportFragment extends LielasFragment {

    Spinner logfilesSpinner;

    public static ExportFragment newInstance(){
        ExportFragment f = new ExportFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.export_fragment, container, false);

        if(context != null){
            Spinner sp = (Spinner) v.findViewById(R.id.spExportDelimiterData);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, getDelimiterList());
            sp.setAdapter(adapter);

            sp = (Spinner) v.findViewById(R.id.spExportCommaData);
            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, getCommaList());
            sp.setAdapter(adapter);

            logfilesSpinner = (Spinner)v.findViewById(R.id.spExportLogfileData);
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
        }
        updateUI();

        return v;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    private List<String> getDelimiterList(){
        List<String> list =  new ArrayList<String>();

        list.add(";");
        list.add(".");
        list.add(",");
        list.add("tab");
        list.add("space");
        return list;
    }

    private List<String> getCommaList(){
        List<String> list =  new ArrayList<String>();

        list.add(",");
        list.add(".");
        return list;
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

    }

}
