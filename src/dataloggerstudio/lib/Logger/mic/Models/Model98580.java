package org.lielas.dataloggerstudio.lib.Logger.mic.Models;

import org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.Channel.MicChannel;
import org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.Channel.MicChannelTemperature;
import org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.MicDataLine;
import org.lielas.dataloggerstudio.lib.Dataset;
import org.lielas.dataloggerstudio.lib.Logger.MeasurementType.MeasurementTypeHumidity;
import org.lielas.dataloggerstudio.lib.Logger.MeasurementType.MeasurementTypeTemperature;
import org.lielas.dataloggerstudio.lib.Logger.Units.UnitClass;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicModel;

/**
 * Created by Andi on 28.10.2015.
 */
public class Model98580 extends MicModel{

    public static String ID = "98580";

    public Model98580(){
        id = ID;
        maxSamples = 16000;
        channels = new MicChannel[1];
        channels[0] = new MicChannelTemperature();
    }

    @Override
    public String toString(){
        return ID;
    }

    public Dataset parseDataLine(byte[] line, long dt){
        MicDataLine micDataLine = new MicDataLine(channels.length);
        Dataset ds = new Dataset(channels.length);

        if(!micDataLine.parse(line)){
            return null;
        }

        ds.setDateTime(dt);
        ds.setValue(micDataLine.getValueByIndex(0), channels[0].getDecimals(), 0, new MeasurementTypeTemperature());

        return ds;
    }

    @Override
    public StringBuilder getCsvHeader(String delimiter, String name, UnitClass unitClass) {
        StringBuilder sb = new StringBuilder();

        //create header line 1
        sb.append(delimiter);
        sb.append(delimiter);
        sb.append(name);
        sb.append("\n");

        //create header line 2
        sb.append(delimiter);
        sb.append(delimiter);
        sb.append("channel 1");
        sb.append(delimiter);
        sb.append("\n");

        //create header line 3
        sb.append(delimiter);
        sb.append(delimiter);
        sb.append(channels[0].getUnit(unitClass));
        sb.append(delimiter);
        sb.append("\n");

        return sb;
    }

    @Override
    public byte[] parseCommand(byte[] cmd) {
        byte[] b = null;

        if(cmd == null || cmd.length == 0){
            return b;
        }

        switch(cmd[0]){
            case 'I':
                b = hexStringToByteArray("692d546573742039383538312056312e300d");
                break;
            case 'M':
                b = hexStringToByteArray("6d20313030302031204d206230332064653720310d");
                break;
        }

        return b;
    }
}
