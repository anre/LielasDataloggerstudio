package org.lielas.dataloggerstudio.lib.LielasCommunicationProtocol;

import org.lielas.dataloggerstudio.lib.Logger.Lielas.LielasVersion;

/**
 * Created by Andi on 02.06.2015.
 */
public class LielasSettingsProtocolBadBlockCount extends LielasSettingsProtocolPayload{
    private int count;

    public int getBadBlockCount(){
        return count;
    }

    @Override
    public int getLspId() {
        return LielasSettingsProtocolIds.GET_BADBLOCK_COUNT;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }

    @Override
    public boolean parse(byte[] payload) {
        if(payload.length < 2){
            count = -1;
            return false;
        }

        count = (payload[0] & 0xFF) << 8;
        count += payload[1] & 0xFF;

        return true;
    }

}
