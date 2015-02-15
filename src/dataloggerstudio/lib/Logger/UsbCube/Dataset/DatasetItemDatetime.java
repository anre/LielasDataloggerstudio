package org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset;

/**
 * Created by Andi on 12.02.2015.
 */
public class DatasetItemDatetime extends DatasetItem{
    final int size = 8;
    
    long dt;

    @Override
    public int getItemId() {
        return DatasetItemIds.DT;
    }

    @Override
    public boolean parse(byte[] payload, int start) {
        if(payload == null){
            return false;
        }

        if(payload.length < (start + size)){
            return false;
        }

        dt = (payload[start++] & 0xFF) << 56;
        dt += (payload[start++] & 0xFF) << 48;
        dt += (payload[start++] & 0xFF) << 40;
        dt += (payload[start++] & 0xFF) << 32;

        dt += (payload[start++] & 0xFF) << 24;
        dt += (payload[start++] & 0xFF) << 16;
        dt += (payload[start++] & 0xFF) <<  8;
        dt += (payload[start++] & 0xFF);
        dt *= 1000;

        return true;
    }

    @Override
    public int getSize() {
        return size;
    }
    
    public long getDt(){
        return dt;
    }
}
