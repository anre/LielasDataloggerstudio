package org.lielas.micdataloggerstudio.main.CommunicationInterface.mic;

import org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.MicLoggerArguments;
import org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.MicLoggerIdentifier;
import org.lielas.dataloggerstudio.lib.Dataset;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.micdataloggerstudio.main.CommunicationInterface.AndroidSerialInterface;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Andi on 11.04.2015.
 */
public class MicSerialInterface extends AndroidSerialInterface {

    final private String CMD_READ_IDENTIFIER = "I\r";
    final private String CMD_READ_ARGUMENTS = "M\r";
    final private String CMD_READ_DATA = "D\r";
    final private String CMD_SET_TIME = "C ";
    final private String CMD_PWR_MODE = "P 2\r";

    private boolean realtimeLogging = false;

    public MicSerialInterface(){
        super();
    }


    public boolean connect(String port){
        return open(port);
    }

    public void disconnect(){
        isOpen = false;
        try{
            serialport.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public byte[] readLine(){
        byte[] recv = new byte[100];
        long start = new Date().getTime();
        long end = start + 200;
        int len = 0;
        int recvLen = 0;
        byte d;
        byte[] paket = null;

        if(!isOpen){
            return null;
        }

        while(new Date().getTime() < end) {

            if(getBytesInBuffer() == 0){
                recvLen += read();
                continue;
            }

            if(len >= recv.length) {
                return null;
            }

            d = readByte();
            recv[len++] = d;

            if(d == '\r'){
                paket = new byte[len];
                System.arraycopy(recv, 0, paket, 0, len);
                break;
            }

        }
        return paket;
    }


    public boolean realTimeLoggingInProgress(){
        return realtimeLogging;
    }

    public boolean readIdentifier(MicUSBStick logger){
        byte[] recv = null;

        if(!isOpen){
            return false;
        }

        isBusy = true;

        write(CMD_READ_IDENTIFIER.getBytes());

        recv = readLine();

        if(recv == null){
            isBusy = false;
            return false;
        }

        MicLoggerIdentifier loggerIdentifier = new MicLoggerIdentifier();
        if(!loggerIdentifier.parse(recv)){
            isBusy = false;
            return false;
        }

        logger.setName(loggerIdentifier.getId());
        logger.setModel(loggerIdentifier.getModel().getModel());
        logger.setVersion(loggerIdentifier.getVersion());

        isBusy = false;
        return true;
    }

    public boolean readArguments(MicUSBStick logger){
        byte[] recv = null;

        if(!isOpen){
            return false;
        }

        isBusy = true;

        write(CMD_READ_ARGUMENTS.getBytes());

        recv = readLine();

        if(recv == null){
            isBusy = false;
            return false;
        }

        MicLoggerArguments loggerArguments = new MicLoggerArguments();
        if(!loggerArguments.parse(recv)){
            isBusy = false;
            return false;
        }

        logger.setSampleRate(loggerArguments.getSampleRate());
        logger.setUnitClass(loggerArguments.getUnitClass());
        logger.setFirstDatasetDate(loggerArguments.getTime());
        logger.setRecordCount(loggerArguments.getSamples());

        return true;
    }

    public boolean saveId(String id, MicUSBStick logger){
        byte[] recv = null;

        if(!isOpen){
            return false;
        }

        isBusy = true;

        String cmd = "J " + id + "\r";
        write(cmd.getBytes());

        recv = readLine();

        if(recv == null){
            isBusy = false;
            return false;
        }

        MicLoggerIdentifier loggerIdentifier = new MicLoggerIdentifier();
        if(!loggerIdentifier.parse(recv)){
            isBusy = false;
            return false;
        }

        logger.setName(loggerIdentifier.getId());

        return true;
    }

    public boolean saveSettings(MicUSBStick logger){
        return true;
    }

    public boolean startLogging(MicUSBStick logger, String cmd){
        byte[] recv = null;

        if(!isOpen){
            return false;
        }

        isBusy = true;

        write(cmd.getBytes());

        //read first line
        recv = readLine();

        if(recv == null){
            isBusy = false;
            return false;
        }

        return true;
    }

    public boolean setClock(MicUSBStick logger){
        byte[] recv = null;

        if(!isOpen){
            return false;
        }

        String cmd = CMD_SET_TIME + logger.getDatetime() + "\r";
        write(cmd.getBytes());
        //read first line
        recv = readLine();

        isBusy = true;

        if(recv == null){
            return false;
        }

        return true;
    }

    public  boolean setPowerMode(MicUSBStick logger){
        byte[] recv = null;

        if(!isOpen){
            return false;
        }

        String cmd = CMD_PWR_MODE;
        write(cmd.getBytes());
        //read first line
        recv = readLine();

        if(recv == null){
            return false;
        }
        return true;
    }

    public LoggerRecord getData(MicUSBStick logger, LoggerRecord loggerRecord){
        byte[] recv = null;

        if(!isOpen){
            return null;
        }

        isBusy = true;

        //send get data command
        write(CMD_READ_DATA.getBytes());

        //read first line
        recv = readLine();

        if(recv == null){
            isBusy = false;
            return null;
        }

        return loggerRecord;
    }

    public boolean addData(MicUSBStick logger, LoggerRecord loggerRecord, long dt){
        byte[] recv = null;

        //read line
        recv = readLine();

        if(recv == null){
            recv = readLine();
            if(recv == null) {
                isBusy = false;
                return false;
            }
        }

        //parse line
        Dataset ds = logger.getModel().parseDataLine(recv, dt);


        if(ds == null){
            return false;
        }

        loggerRecord.add(ds);


        return true;
    }

    public boolean startRealTimeLogging(){
        return true;
    }

    public boolean stopRealTimeLogging(){
        return true;
    }

    public boolean getRealTimeValue(LoggerRecord lr){
        return true;
    }


}

