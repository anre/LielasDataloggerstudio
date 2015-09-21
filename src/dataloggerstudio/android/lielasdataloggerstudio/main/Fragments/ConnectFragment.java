package org.lielas.lielasdataloggerstudio.main.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.lielasdataloggerstudio.R;
import org.lielas.lielasdataloggerstudio.main.LielasToast;
import org.lielas.lielasdataloggerstudio.main.Tasks.ConnectTask;
import org.lielas.lielasdataloggerstudio.main.Tasks.UpdateManager;

public class ConnectFragment extends LielasFragment{

    TextView connectionstate;

    private ImageButton navRight;

    public static final ConnectFragment newInstance(){
        ConnectFragment f = new ConnectFragment();
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            public void onReceive(Context context, Intent intent){
                String action = intent.getAction();

                if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device != null) {
                        // call your method that cleans up and closes communication with the device
                        try {
                            LielasToast.show("Disconnected", context);
                        }catch(Exception e){

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
            navRight = (ImageButton)v.findViewById(R.id.connectNavRight);
            navRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPager pager = (ViewPager)getActivity().findViewById(R.id.viewpager);
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
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

    private void onButtonClick(View v){

        ConnectTask conTask = new ConnectTask((UsbCube)this.logger, updateManager, getActivity());
        conTask.execute();
    }

    @Override
    public void update(){

        UsbCube l = (UsbCube) logger;

        if(l == null || l.getCommunicationInterface() == null){
            return;
        }

        try {
            if (l.getCommunicationInterface().isOpen()) {
                connectionstate.setText(getResources().getString(R.string.state_connected));
            } else {
                connectionstate.setText(getResources().getString(R.string.state_not_connected));
            }
        }catch (Exception e){
        }
    }

}