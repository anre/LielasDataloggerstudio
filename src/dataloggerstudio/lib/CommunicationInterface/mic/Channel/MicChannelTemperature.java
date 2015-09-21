package org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.Channel;

import org.lielas.dataloggerstudio.lib.Logger.Units.UnitClass;

/**
 * Created by Andi on 01.05.2015.
 */
public class MicChannelTemperature extends MicChannel{

    @Override
    public int getDecimals() {
        return 1;
    }

    @Override
    public String getUnit(UnitClass c) {
        if(c == null) {
            return "Temp. [°C]";
        }
        if(c.getUnitClass() == UnitClass.UNIT_CLASS_IMPERIAL){
            return "Temp. [°F]";
        }
        return "Temp. [°C]";
    }


}
