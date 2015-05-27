package org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset;

/**
 * Created by Andi on 04.05.2015.
 */
public class DatasetSensorItemEDLSI extends DatasetSensorItem {
    final int size = 4;
    int id;

    long value;

    public DatasetSensorItemEDLSI(int id){
        this.id = id;
    }

    @Override
    public long getValue() {
        return value;
    }

    @Override
    public int getDecimalPoints() {
        return 3;
    }

    @Override
    public int getItemId() {
        return id;
    }

    @Override
    public boolean parse(byte[] payload, int start) {
        if(payload == null){
            return false;
        }

        if(payload.length < (start + size)){
            return false;
        }

        value = (payload[start++] & 0xFF);
        value += (payload[start++] & 0xFF) <<  8;
        value += (payload[start++] & 0xFF) << 16;
        value += (payload[start++] & 0xFF) << 24;

        return true;
    }

    @Override
    public int getSize() {
        return size;
    }

}
