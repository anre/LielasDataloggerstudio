package org.lielas.dataloggerstudio.lib.LielasCommunicationProtocol;

/**
 * Created by Andi on 09.06.2015.
 */
public class LielasSettingsProtocolModelNumber extends LielasSettingsProtocolPayload{

    private int model;

    public LielasSettingsProtocolModelNumber(){
        super();
        model = 0;
    }

    @Override
    public int getLspId() {
        return LielasSettingsProtocolIds.MODEL_NUMBER;
    }

    @Override
    public int getLength() {
        return 2;
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }

    @Override
    public boolean parse(byte[] payload) {
        if(payload == null || payload.length != 2){
            model = 0;
            return false;
        }

        model = payload[1] & 0xFF;
        model += (payload[0] & 0xFF) << 8;

        return true;
    }

    public int getModelNumber(){
        return model;
    }
}
