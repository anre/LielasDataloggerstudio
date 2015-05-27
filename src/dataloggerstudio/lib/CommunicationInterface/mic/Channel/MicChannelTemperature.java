package org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.Channel;

/**
 * Created by Andi on 01.05.2015.
 */
public class MicChannelTemperature extends MicChannel{

    @Override
    public int getDecimals() {
        return 1;
    }

    @Override
    public String getUnit() {
        return "Temp. [°C]";
    }
}
