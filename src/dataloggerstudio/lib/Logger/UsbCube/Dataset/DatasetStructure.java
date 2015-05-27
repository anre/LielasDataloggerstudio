package org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset;

import java.util.ArrayList;

/**
 * Created by Andi on 12.02.2015.
 */
public class DatasetStructure {

    private ArrayList<DatasetItem> items;
    private int count;
    private int channels;
    private int sensors;

    public DatasetStructure(){
        count = 0;
        items = new ArrayList<DatasetItem>();
        channels = 0;
        sensors = 0;
    }

    public int getCount(){
        return count;
    }

    public DatasetItem getItem(int index){
        if(items == null){
            return null;
        }

        if(index >= items.size()){
            return null;
        }

        return items.get(index);
    }

    public DatasetSensorItem getSensorItem(int index){
        int sensorItemCount = 0;
        if(items == null){
            return null;
        }

        for(int i = 0; i < items.size(); i++){
            if(items.get(i) instanceof  DatasetSensorItem){
                if(sensorItemCount == index){
                    //item found
                    DatasetSensorItem ds = (DatasetSensorItem)items.get(i);
                    return ds;
                }
                sensorItemCount += 1;
            }
        }
        return null;
    }

    public boolean parse(byte[] payload){
        int payloadLength;
        int pos;
        int itemId;
        DatasetItem newItem;
        channels = 0;
        sensors = 0;

        if(payload == null){
            return false;
        }

        if(payload.length < 1){
            return false;
        }

        count = payload[0];
        payloadLength = count * 2 + 1;

        if(payload.length < payloadLength){
            return false;
        }

        pos = 1;
        for(int i = 0; i < count; i++){
            itemId = (payload[pos++] & 0xFF) << 8;
            itemId += (payload[pos++] & 0xFF);

            switch (itemId){
                case DatasetItemIds.ID:
                    items.add(new DatasetItemId());
                    break;
                case DatasetItemIds.DT:
                    items.add(new DatasetItemDatetime());
                    break;
                case DatasetItemIds.SHT_T:
                    items.add(new DatasetSensorItemSHTT());
                    channels += 1;
                    sensors += 1;
                    break;
                case DatasetItemIds.SHT_H:
                    items.add(new DatasetSensorItemSHTH());
                    channels += 1;
                    sensors += 1;
                    break;
                case DatasetItemIds.PT1K1:
                case DatasetItemIds.PT1K2:
                case DatasetItemIds.PT1K3:
                case DatasetItemIds.PT1K4:
                    items.add(new DatasetSensorItemPT1K(itemId));
                    channels += 1;
                    sensors += 1;
                    break;
                case DatasetItemIds.MS5607:
                    items.add(new DatasetSensorItemMS5607(itemId));
                    channels += 1;
                    sensors += 1;
                    break;
                case DatasetItemIds.EDLSU1:
                case DatasetItemIds.EDLSU2:
                case DatasetItemIds.EDLSU3:
                case DatasetItemIds.EDLSU4:
                    items.add(new DatasetSensorItemEDLSU(itemId));
                    channels += 1;
                    sensors += 1;
                    break;
                case DatasetItemIds.EDLSI1:
                case DatasetItemIds.EDLSI2:
                case DatasetItemIds.EDLSI3:
                case DatasetItemIds.EDLSI4:
                    items.add(new DatasetSensorItemEDLSI(itemId));
                    channels += 1;
                    sensors += 1;
                    break;
            }
        }
        return true;
    }

    public int getChannelCount(){
        return channels;
    }

    public int getSensorCount(){
        return sensors;
    }

}
