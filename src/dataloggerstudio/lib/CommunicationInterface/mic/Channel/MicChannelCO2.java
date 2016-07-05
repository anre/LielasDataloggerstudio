package org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.Channel;

import org.lielas.dataloggerstudio.lib.Logger.Units.UnitClass;

/**
 * Created by Andi on 30.10.2015.
 */
public class MicChannelCO2 extends MicChannel {


    @Override
    public int getDecimals() {
        return 0;
    }

    @Override
    public String getUnit(UnitClass c) {
        return "CO2 [ppm]";
    }
}
