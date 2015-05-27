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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import org.lielas.dataloggerstudio.lib.FileCreator.CsvFileCreator;
import org.lielas.dataloggerstudio.lib.FileCreator.FileCreator;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.lielasdataloggerstudio.R;
import org.lielas.lielasdataloggerstudio.main.FileSaver.AndroidFileSaver;
import org.lielas.lielasdataloggerstudio.main.LielasToast;
import org.lielas.lielasdataloggerstudio.main.LoggerRecordManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andi on 21.02.2015.
 */
public class ExportFragment extends LielasFragment {

    Spinner logfilesSpinner;
    Spinner delimiterSpinner;
    Spinner commaSpinner;
    EditText filename;
    Button saveBttn;
    CheckBox chkOverwrite;

    private boolean userinteraction;
    private ImageButton navLeft;

    public static ExportFragment newInstance(){
        ExportFragment f = new ExportFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.export_fragment, container, false);

        if(context != null){
            delimiterSpinner = (Spinner) v.findViewById(R.id.spExportDelimiterData);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, getDelimiterList());
            delimiterSpinner.setAdapter(adapter);

            commaSpinner = (Spinner) v.findViewById(R.id.spExportCommaData);
            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, getCommaList());
            commaSpinner.setAdapter(adapter);

            logfilesSpinner = (Spinner)v.findViewById(R.id.spExportLogfileData);
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
            logfilesSpinner.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    userinteraction = true;
                    return false;
                }
            });
            logfilesSpinner.setVisibility(View.INVISIBLE);

            saveBttn = (Button)v.findViewById(R.id.bttnExportBttn);
            saveBttn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onButtonClick(v);
                }
            });

            chkOverwrite = (CheckBox) v.findViewById(R.id.chkOverwrite);

            filename = (EditText)v.findViewById(R.id.txtFilename);

            navLeft = (ImageButton)v.findViewById(R.id.exportNavLeft);
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

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
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

        if(l.getCommunicationInterface().isOpen() && l.getRecordCount() > 0){
            ArrayAdapter<LoggerRecord> adapter = new ArrayAdapter<LoggerRecord>(getActivity(), R.layout.logfile_spinner_item, l.getRecordsetArray());
            logfilesSpinner.setAdapter(adapter);
            logfilesSpinner.setVisibility(View.VISIBLE);
            LoggerRecord lr = LoggerRecordManager.getInstance().getActiveLoggerRecord();

            if(lr != null) {
                LoggerRecord loggerRecords[] = l.getRecordsetArray();
                for (int i = 0; i < loggerRecords.length; i++) {
                    if (loggerRecords[i].getId() == lr.getId()) {
                        logfilesSpinner.setSelection(i);
                        break;
                    }
                }

                filename.setText(lr.getName());
            }

        }else if(l.getRecordCount() == 0){

            //create empty adapter
            String[] emptyString = new String[1];
            emptyString[0] = " ";
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.logfile_spinner_item, emptyString);
            logfilesSpinner.setAdapter(adapter);
            logfilesSpinner.setVisibility(View.INVISIBLE);

            filename.setText("");
            chkOverwrite.setChecked(false);
        }

    }

    private void LogfileSpinnerSelectionChanged(){
        if(!userinteraction) {
            return;
        }
        userinteraction = false;

        LoggerRecord lr = (LoggerRecord)logfilesSpinner.getSelectedItem();

        if(LoggerRecordManager.getInstance().getActiveLoggerRecord() == null ||
                lr.getId() != LoggerRecordManager.getInstance().getActiveLoggerRecord().getId()) {
            LoggerRecordManager.getInstance().setActiveLoggerRecord(lr);
            updateManager.update();
        }
    }

    private void onButtonClick(View v){
        int status;
        String fname = filename.getText().toString();
        LoggerRecord lr = LoggerRecordManager.getInstance().getActiveLoggerRecord();

        if(lr == null){
            new LielasToast().show("No logfile selected", getActivity());
            return;
        }

        if(lr.getCount() != (lr.getEndIndex() - lr.getStartIndex() + 1)){
            new LielasToast().show("No data pressent, please go to Data and load the logfile", getActivity());
            return;
        }

        if(fname == null || fname.equals("")){
            new LielasToast().show("Please specify a filename", getActivity());
            return;
        }

        if(fname.contains(" ") ||fname.contains("\n")){
            new LielasToast().show("The filename may not include spaces or newlines", getActivity());
            return;
        }

        fname = fname + ".csv";

        AndroidFileSaver fileSaver = new AndroidFileSaver();
        CsvFileCreator fileCreator = new CsvFileCreator();

        fileCreator.setFileSaverType(fileSaver);
        fileCreator.setDelimiter(delimiterSpinner.getSelectedItem().toString());
        fileCreator.setComma(commaSpinner.getSelectedItem().toString());
        fileCreator.setPath(fname);
        fileCreator.create(LoggerRecordManager.getInstance().getActiveLoggerRecord());
        status = fileCreator.save(LoggerRecordManager.getInstance().getActiveLoggerRecord(), chkOverwrite.isChecked());

        if(status == FileCreator.STATUS_OK){
            new LielasToast().show("File successfully written to Download/lielas", getActivity());
        }else if(status == FileCreator.STATUS_FILE_EXISTS){
            new LielasToast().show("File already exists", getActivity());
        }else{
            new LielasToast().show("Failed to write file", getActivity());
        }
    }

}
