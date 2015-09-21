package org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.Channel;

import org.lielas.dataloggerstudio.lib.Logger.Units.UnitClass;

/**
 * Created by Andi on 01.05.2015.
 */
public class MicChannelRH extends MicChannel {

    @Override
    public int getDecimals() {
        return 1;
    }

    @Override
    public String getUnit(UnitClass c) {
        return "R.H. [%]";
    }
}
