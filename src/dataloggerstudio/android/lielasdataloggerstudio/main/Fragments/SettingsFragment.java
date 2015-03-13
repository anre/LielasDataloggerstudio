package org.lielas.lielasdataloggerstudio.main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.lielasdataloggerstudio.R;
import org.w3c.dom.Text;

public class SettingsFragment extends LielasFragment{

    TextView txtVersion;
    TextView txtId;
    EditText etxName;

    public static final SettingsFragment newInstance(){
        SettingsFragment f = new SettingsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.settings_fragment, container, false);

        if(context != null){
            txtVersion = (TextView)v.findViewById(R.id.lblSettingsVersionData);
            txtId = (TextView)v.findViewById(R.id.lblSettingsIdData);
            etxName = (EditText)v.findViewById(R.id.txtName);
        }

        return v;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void update(){

        UsbCube l = (UsbCube) logger;

        if(l == null || l.getCommunicationInterface() == null){
            return;
        }

        if(l.getCommunicationInterface().isOpen()){
            txtVersion.setText(l.getVersion().toString());
            txtId.setText(l.getId().toString());
            etxName.setText(l.getName());
        }else{
            txtVersion.setText("");
            txtId.setText("");
            etxName.setText("");
        }

    }

}
