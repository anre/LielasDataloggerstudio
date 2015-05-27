package org.lielas.micdataloggerstudio.main.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.micdataloggerstudio.R;
import org.lielas.micdataloggerstudio.main.LielasToast;
import org.lielas.micdataloggerstudio.main.Tasks.ConnectTask;

/**
 * Created by Andi on 08.04.2015.
 */
public class ConnectFragment extends MicFragment{


    TextView connectionstate;

    public static final ConnectFragment newInstance(){
        ConnectFragment f = new ConnectFragment();

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent != null){
                    String action = intent.getAction();

                    if(UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)){
                        UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if(device != null){
                            try{
                                LielasToast.show("Disconnected", context);
                            }catch(Exception e){

                            }
                        }
                    }
                }

            }
        };
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.connect_fragment, container, false);

        if(context != null){
            Button bttn = (Button)v.findViewById(R.id.bttnConnect);
            bttn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onButtonClick(v);
                }
            });


            connectionstate = (TextView)v.findViewById(R.id.lblConnectState);

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
        MicUSBStick stick = (MicUSBStick)logger;

        if(stick == null || stick.getCommunicationInterface() == null){
            return;
        }

        try{
            if(stick.getCommunicationInterface().isOpen()){
                connectionstate.setText(getResources().getString(R.string.state_connected));
            }else{
                connectionstate.setText(getResources().getString(R.string.state_not_connected));
            }
        }catch (Exception e){
        }

    }

    private void onButtonClick(View v){
        ConnectTask connectTask = new ConnectTask((MicUSBStick)this.logger, updateManager, getActivity());
        connectTask.execute();
    }
}
