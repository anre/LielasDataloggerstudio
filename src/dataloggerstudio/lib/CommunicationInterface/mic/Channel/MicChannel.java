package org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.Channel;

import org.lielas.dataloggerstudio.lib.Logger.Units.UnitClass;

/**
 * Created by Andi on 01.05.2015.
 */
public abstract class MicChannel {

    public abstract int getDecimals();

    public abstract String getUnit(UnitClass c);

}
