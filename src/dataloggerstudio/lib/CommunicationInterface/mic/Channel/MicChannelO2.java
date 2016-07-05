package org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.Channel;

import org.lielas.dataloggerstudio.lib.Logger.Units.UnitClass;

/**
 * Created by Andi on 30.10.2015.
 */
public class MicChannelO2 extends MicChannel{
    @Override
    public int getDecimals() {
        return 1;
    }

    @Override
    public String getUnit(UnitClass c) {
        return "O2 [%]";
    }
}
