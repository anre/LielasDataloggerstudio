package org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset;

/**
 * Created by Andi on 12.02.2015.
 */
public class DatasetItemId extends DatasetItem{
    final int size = 4;

    long id;

    @Override
    public int getItemId() {
        return DatasetItemIds.ID;
    }

    @Override
    public boolean parse(byte[] payload, int start) {
        if(payload == null){
            return false;
        }

        if(payload.length < (start + size)){
            return false;
        }

        id = (payload[start++] & 0xFF) << 24;
        id += (payload[start++] & 0xFF) << 16;
        id += (payload[start++] & 0xFF) << 8;
        id += (payload[start++] & 0xFF);

        return true;
    }

    @Override
    public int getSize() {
        return size;
    }

    public long getId(){
        return id;
    }
}
