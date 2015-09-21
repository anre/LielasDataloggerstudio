package org.lielas.dataloggerstudio.lib.LielasCommunicationProtocol;

/**
 * Created by Andi on 05.06.2015.
 */
public class LielasSettingsProtocolTestFlash  extends LielasSettingsProtocolPayload{
    private int status;

    public static int OK = 0;

    public int getTestStatus(){
        return status;
    }

    @Override
    public int getLspId() {
        return LielasSettingsProtocolIds.TEST_FLASH;
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
        if(payload.length < 1){
            status = -1;
            return false;
        }

        status = (payload[0] & 0xFF);

        return true;
    }
}
