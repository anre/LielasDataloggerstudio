package org.lielas.dataloggerstudio.lib.LielasCommunicationProtocol;


/**
 * Created by Andi on 05.06.2015.
 */
public class LielasSettingsProtocolRTData extends LielasSettingsProtocolPayload{

    @Override
    public int getLspId() {
        return LielasSettingsProtocolIds.RT_DATA;
    }

    @Override
    public int getLength() {
        return  0;
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }

    @Override
    public boolean parse(byte[] payload) {
        return true;
    }

}
