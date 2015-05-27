package org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset;

/**
 * Created by Andi on 12.02.2015.
 */
public class DatasetSensorItemSHTT extends DatasetSensorItem{
    final int size = 4;

    long value;

    @Override
    public long getValue() {
        return value;
    }

    @Override
    public int getDecimalPoints() {
        return 2;
    }

    @Override
    public int getItemId() {
        return DatasetItemIds.SHT_T;
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

        value -= 27310;

        return true;
    }

    @Override
    public int getSize() {
        return size;
    }
}
