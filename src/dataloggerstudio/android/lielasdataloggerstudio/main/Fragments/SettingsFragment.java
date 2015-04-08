package org.lielas.lielasdataloggerstudio.main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.lielas.dataloggerstudio.lib.LielasCommunicationProtocol.LielasSettingsProtocolName;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.lielasdataloggerstudio.R;
import org.lielas.lielasdataloggerstudio.main.LielasToast;
import org.lielas.lielasdataloggerstudio.main.Tasks.SetLoggernameTask;
import org.w3c.dom.Text;

public class SettingsFragment extends LielasFragment{

    TextView txtVersion;
    TextView txtId;
    EditText etxName;
    Button bttnSaveName;

    private ImageButton navRight;
    private ImageButton navLeft;


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

            bttnSaveName = (Button)v.findViewById(R.id.bttnSettingsSetName);
            bttnSaveName.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onButtonClick(v);
                }
            });

            navRight = (ImageButton)v.findViewById(R.id.settingsNavRight);
            navRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPager pager = (ViewPager)getActivity().findViewById(R.id.viewpager);
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
            });

            navLeft = (ImageButton)v.findViewById(R.id.settingsNavLeft);
            navLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPager pager = (ViewPager)getActivity().findViewById(R.id.viewpager);
                    pager.setCurrentItem(pager.getCurrentItem() - 1);
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

    public void onButtonClick(View v){


        String name = etxName.getText().toString();

        if(name.length() > LielasSettingsProtocolName.MAX_LOGGER_NAME_LENGTH){
            Toast.makeText(getActivity(), R.string.settings_name_too_long, Toast.LENGTH_SHORT).show();
            return;
        }

        if(name.length() == 0){
            Toast.makeText(getActivity(), R.string.settings_name_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        SetLoggernameTask task = new SetLoggernameTask((UsbCube)this.logger, updateManager, name);
        task.execute();
    }

}
