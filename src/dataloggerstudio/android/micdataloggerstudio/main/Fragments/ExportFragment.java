package org.lielas.micdataloggerstudio.main.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import org.lielas.dataloggerstudio.lib.FileCreator.CsvFileCreator;
import org.lielas.dataloggerstudio.lib.FileCreator.FileCreator;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.micdataloggerstudio.R;
import org.lielas.micdataloggerstudio.main.FileSaver.AndroidFileSaver;
import org.lielas.micdataloggerstudio.main.LielasToast;
import org.lielas.micdataloggerstudio.main.LoggerRecordManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andi on 08.04.2015.
 */
public class ExportFragment extends MicFragment{

    Spinner delimiterSpinner;
    Spinner commaSpinner;
    EditText filename;
    Button saveBttn;
    CheckBox chkOverwrite;

    public static final ExportFragment newInstance(){
        return new ExportFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.export_fragment, container, false);

        if(context != null){
            //SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);

            delimiterSpinner = (Spinner) v.findViewById(R.id.spExportDelimiterData);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, getDelimiterList());
            delimiterSpinner.setAdapter(adapter);
            //String delimiter = sharedPreferences.getString("delimiter", ",");
            delimiterSpinner.setSelection(adapter.getPosition(","));

            commaSpinner = (Spinner) v.findViewById(R.id.spExportCommaData);
            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, getCommaList());
            commaSpinner.setAdapter(adapter);
            //String comma = sharedPreferences.getString("comma", ".");
            commaSpinner.setSelection(adapter.getPosition("."));

            saveBttn = (Button)v.findViewById(R.id.bttnExportBttn);
            saveBttn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onButtonClick(v);
                }
            });

            chkOverwrite = (CheckBox) v.findViewById(R.id.chkOverwrite);

            filename = (EditText)v.findViewById(R.id.txtFilename);

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
        updateUI();
    }

    public void updateUI(){

    }

    private List<String> getDelimiterList(){
        List<String> list =  new ArrayList<String>();

        list.add(",");
        list.add(".");
        list.add(";");
        list.add("tab");
        list.add("space");
        return list;
    }

    private List<String> getCommaList(){
        List<String> list =  new ArrayList<String>();

        list.add(".");
        list.add(",");
        return list;
    }

    private void onButtonClick(View v){
        int status;
        String fname = filename.getText().toString();
        LoggerRecord lr = LoggerRecordManager.getInstance().getActiveLoggerRecord();
        final String[] reservedChars = {"|", "\\", "?", "*", "<", "\"", ":", ">"};


        //save settings
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit();
        editor.putString("delimiter", delimiterSpinner.getSelectedItem().toString());
        editor.putString("comma", commaSpinner.getSelectedItem().toString());
        editor.commit();


        if(lr == null){
            new LielasToast().show("No logfile selected", getActivity());
            return;
        }

        if(lr.getCount() == 0){
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

        for ( String c : reservedChars ) {
            if(fname.contains(c)){
                new LielasToast().show("The filename may not include \"| / ? * < \\ : >\"", getActivity());
                return;
            }
        }

        fname = fname + ".csv";

        AndroidFileSaver fileSaver = new AndroidFileSaver("/mic");
        CsvFileCreator fileCreator = new CsvFileCreator();

        fileCreator.setFileSaverType(fileSaver);
        fileCreator.setDelimiter(delimiterSpinner.getSelectedItem().toString());
        fileCreator.setComma(commaSpinner.getSelectedItem().toString());
        fileCreator.setPath(fname);
        fileCreator.create(LoggerRecordManager.getInstance().getActiveLoggerRecord());
        status = fileCreator.save(LoggerRecordManager.getInstance().getActiveLoggerRecord(), chkOverwrite.isChecked());

        if(status == FileCreator.STATUS_OK){
            new LielasToast().show("File successfully written to Download" + fileSaver.getPathPrefix(), getActivity());
        }else if(status == FileCreator.STATUS_FILE_EXISTS){
            new LielasToast().show("File already exists", getActivity());
        }else{
            new LielasToast().show("Failed to write file", getActivity());
        }
    }

}
