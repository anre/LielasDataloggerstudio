package org.lielas.dataloggerstudio.lib.LielasCommunicationProtocol;

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.DatasetStructure;

/**
 * Created by Andi on 12.02.2015.
 */
public class LielasSettingsProtocolDatasetStructure extends LielasSettingsProtocolPayload {

    int length;

    DatasetStructure dsStructure;

    public LielasSettingsProtocolDatasetStructure(){
        length = 0;
        dsStructure = null;
    }

    @Override
    public int getLspId() {
        return LielasSettingsProtocolIds.GET_DATASET_STRUCTURE;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public byte[] getBytes() {
        return null;
    }

    @Override
    public boolean parse(byte[] payload) {
        if(payload == null){
            dsStructure = null;
            return false;
        }

        dsStructure = new DatasetStructure();
        if(dsStructure.parse(payload)){
            return true;
        }
        dsStructure = null;
        return false;
    }

    public DatasetStructure getDatasetStructure(){
        return dsStructure;
    }

}
