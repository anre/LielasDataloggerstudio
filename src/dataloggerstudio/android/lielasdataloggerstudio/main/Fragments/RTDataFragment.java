package org.lielas.lielasdataloggerstudio.main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lielas.dataloggerstudio.lib.Dataset;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.DatasetItemIds;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.lielasdataloggerstudio.R;
import org.lielas.lielasdataloggerstudio.main.CommunicationInterface.UsbCube.UsbCubeSerialInterface;

import java.util.TimerTask;

/**
 * Created by Andi on 05.06.2015.
 */
public class RTDataFragment extends LielasFragment {

    TextView[] lblSensor;
    TextView[] lblSensorValue;

    Dataset ds;

    java.util.Timer timer;

    final Handler guiUpdateHandler = new Handler();

    public static RTDataFragment newInstance(){
        RTDataFragment f = new RTDataFragment();
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.rt_data_fragment, container, false);

        if(context != null){
            lblSensor = new TextView[3];
            lblSensorValue = new TextView[3];

            lblSensor[0] = (TextView)v.findViewById(R.id.lblRTDataChannel1);
            lblSensor[1] = (TextView)v.findViewById(R.id.lblRTDataChannel2);
            lblSensor[2] = (TextView)v.findViewById(R.id.lblRTDataChannel3);

            lblSensorValue[0] = (TextView)v.findViewById(R.id.lblRTDataChannel1Content);
            lblSensorValue[1] = (TextView)v.findViewById(R.id.lblRTDataChannel2Content);
            lblSensorValue[2] = (TextView)v.findViewById(R.id.lblRTDataChannel3Content);

            updateUI();
            if(timer == null){
                timer =  new java.util.Timer();
                UpdateRTValuesTask updateRTDataTask = new UpdateRTValuesTask();
                updateRTDataTask.setLogger((UsbCube)logger);
                timer.schedule(updateRTDataTask, 1000, 5000);
            }
        }

        return v;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
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
        int i;
        UsbCube l = (UsbCube) logger;

        if(this.lblSensor == null){
            return;
        }

        if(l == null || l.getCommunicationInterface() == null){
            return;
        }

        if(l.getCommunicationInterface().isOpen()){

            for(i = 0; i < l.getDatasetStructure().getSensorCount() && i < 3; i++){
                switch (l.getDatasetStructure().getSensorItem(i).getItemId()){
                    case DatasetItemIds.SHT_T:
                        lblSensor[i].setText("Temperature [°C]");
                        break;
                    case DatasetItemIds.SHT_H:
                        lblSensor[i].setText("R.H. [%]");
                        break;
                    case DatasetItemIds.MS5607:
                        lblSensor[i].setText("Air pressure [mbar]");
                        break;
                    case DatasetItemIds.EDLSI1:
                    case DatasetItemIds.EDLSI2:
                    case DatasetItemIds.EDLSI3:
                    case DatasetItemIds.EDLSI4:
                        lblSensor[i].setText("Current [mA]");
                        break;
                    case DatasetItemIds.EDLSU1:
                    case DatasetItemIds.EDLSU2:
                    case DatasetItemIds.EDLSU3:
                    case DatasetItemIds.EDLSU4:
                        lblSensor[i].setText("Voltage [V]");
                        break;
                    case DatasetItemIds.PT1K1:
                    case DatasetItemIds.PT1K2:
                    case DatasetItemIds.PT1K3:
                    case DatasetItemIds.PT1K4:
                        lblSensor[i].setText("Temperature [°C]");
                        break;
                }
            }
            for(;i < 3; i++){
                lblSensor[i].setText("");
            }

        }else{
            lblSensor[0].setText("");
            lblSensor[1].setText("");
            lblSensor[2].setText("");
            lblSensorValue[0].setText("");
            lblSensorValue[1].setText("");
            lblSensorValue[2].setText("");
        }
    }


    final Runnable UpdateGUI = new Runnable() {
        @Override
        public void run() {

            UsbCube l = (UsbCube) logger;

            if(ds != null) {
                for (int i = 0; i < ds.getChannels() && i < 3; i++) {
                    lblSensorValue[i].setText(ds.getString(i + 1));
                }
            }
        }
    };

    class UpdateRTValuesTask extends TimerTask {

        UsbCube logger;
        UsbCubeSerialInterface com;

        public void setLogger(UsbCube logger){
            this.logger = logger;
            com = (UsbCubeSerialInterface)logger.getCommunicationInterface();
        }

        @Override
        public void run() {

            if(logger == null || !logger.getCommunicationInterface().isOpen()){
                return;
            }

            while(com.isBusy()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            com.setBusy(true);

            ds = com.getRTData(logger);

            com.setBusy(false);

            guiUpdateHandler.post(UpdateGUI);
        }
    }
}
