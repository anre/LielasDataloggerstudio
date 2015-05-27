package org.lielas.micdataloggerstudio.main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.micdataloggerstudio.R;
import org.lielas.micdataloggerstudio.main.LielasToast;
import org.lielas.micdataloggerstudio.main.Tasks.SaveNameTask;

/**
 * Created by Andi on 08.04.2015.
 */
public class DeviceInfoFragment extends MicFragment{

    TextView txtType;
    TextView txtVersion;
    EditText edName;

    public static final DeviceInfoFragment newInstance(){
        return new DeviceInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.device_info_fragment, container, false);

        if(context != null){

            txtType = (TextView) v.findViewById(R.id.lblProductTypeContent);

            txtVersion = (TextView) v.findViewById(R.id.lblSoftwareVersionContent);

            edName = (EditText) v.findViewById(R.id.txtID);

            Button saveNameButton = (Button) v.findViewById(R.id.bttnSaveId);
            saveNameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonPress(v);
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
            txtType.setText(stick.getModel().toString());
            txtVersion.setText(stick.getVersion());
            edName.setText(stick.getName());
        }

    }

    private void onButtonPress(View v){
        String name = edName.getText().toString();

        if(name.length() > 20){
            LielasToast.show(getResources().getString(R.string.settings_name_too_long), getActivity());
            return;
        }

        if(name.length() == 0){
            LielasToast.show(getResources().getString(R.string.settings_name_empty), getActivity());
            return;
        }

        SaveNameTask task = new SaveNameTask((MicUSBStick)logger, updateManager, name);
        task.execute();
    }

}
