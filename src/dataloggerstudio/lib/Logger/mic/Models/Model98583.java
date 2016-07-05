package org.lielas.dataloggerstudio.lib.Logger.mic.Models;

import org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.Channel.MicChannel;
import org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.Channel.MicChannelRH;
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
public class Model98583 extends MicModel{

    public static String ID = "98583";

    public Model98583(){
        id = ID;
        maxSamples = 32000;
        channels = new MicChannel[2];
        channels[0] = new MicChannelTemperature();
        channels[1] = new MicChannelRH();
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
        ds.setValue(micDataLine.getValueByIndex(1), channels[1].getDecimals(), 1, new MeasurementTypeHumidity());

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
        sb.append("channel 2");
        sb.append(delimiter);
        sb.append("\n");

        //create header line 3
        sb.append(delimiter);
        sb.append(delimiter);
        sb.append(channels[0].getUnit(unitClass));
        sb.append(delimiter);
        sb.append(channels[1].getUnit(unitClass));
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
                b = hexStringToByteArray("69204c6d676765725f42332039383538332056322e300d");
                break;
            case 'M':
                b = hexStringToByteArray("6d20362031204d20616635206537663520330d");
                break;
            case 'Q':
                b = hexStringToByteArray("3e0d");
                break;
            case 'D':
                b = hexStringToByteArray("6420323736203536300d6420323736203536300d6420323736203536300d");
                break;
        }

        return b;
    }
}
