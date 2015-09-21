package org.lielas.dataloggerstudio.pc.bootloader;

import jssc.SerialPortException;
import org.lielas.dataloggerstudio.lib.LielasCommunicationProtocol.LielasCommunicationProtocolPaket;
import org.lielas.dataloggerstudio.pc.CommunicationInterface.PcSerialInterface;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Andi on 08.06.2015.
 */
public class Bootloader extends PcSerialInterface {

    public static final int PROGRAM_START  = 0xC00;

    public static final int COMMAND_NACK = 0;
    public static final int COMMAND_ACK = 1;
    public static final int COMMAND_READ_PM = 2;
    public static final int COMMAND_WRITE_PM = 3;
    public static final int COMMAND_READ_EE = 4;
    public static final int COMMAND_WRITE_EE = 5;
    public static final int COMMAND_READ_CM = 6;
    public static final int COMMAND_WRITE_CM = 7;
    public static final int COMMAND_RESET = 8;
    public static final int COMMAND_READ_ID = 9;
    public static final int COMMAND_READ_VERSION = 17;
    public static final int COMMAND_POR_RESET = 19;
    public static final int COMMAND_READ_MODEL = 43;
    public static final int COMMAND_WRITE_MODEL = 44;

    public static final int PM33F_ROW_SIZE_LARGE = 64 * 8;

    public static final int ETYPE_PROGRAM = 0;
    public static final int ETYPE_EEPROM = 1;
    public static final int ETYPE_CONFIGURATION = 2;

    public static final int PM_SIZE = 1536;
    public static final int EE_SIZE = 128;
    public static final int CM_SIZE = 12;

    int extAddr;
    MemRow[] ppMemory;
    MemRow[] ppMemoryVerify;

    private BootloaderReporter reporter;

    public Bootloader(){
        ppMemory = new MemRow[PM_SIZE + EE_SIZE + CM_SIZE];
        ppMemoryVerify = new MemRow[PM_SIZE + EE_SIZE + CM_SIZE];
        reporter = null;
    }

    public boolean connect(String port){
        setBaudrate(500000);
        return open(port);
    }

    public void setBootloaderReporter(BootloaderReporter bootloaderReporter){
        this.reporter = bootloaderReporter;
    }

    private byte[] getPaketData(int count){
        byte[]recv = new byte[2000];
        byte[] paket = null;
        Byte d;
        int len = 0;
        long start = new Date().getTime();
        long time = start;

        if(!isOpen){
            return null;
        }

        while((time < (start + 1000)) && len < recv.length){
            d = readByte();
            if(d != null){
                recv[len++] = d;

                if(len == count){
                    paket = new byte[len];
                    System.arraycopy(recv, 0, paket, 0, len);
                    return paket;
                }
            }
            time = new Date().getTime();
        }

        return paket;
    }

    @Override
    public String getPort() {
        return null;
    }

    public void disconnect(){
        close();
        return;
    }

    public int getVersion(){
        byte[] cmd = new byte[1];
        cmd[0] = COMMAND_READ_VERSION;
        byte[] paket;

        try{
            sp.writeBytes(cmd);
        } catch (SerialPortException e) {
            return 0;
        }



        paket = getPaketData(3);
        if(paket == null || paket.length != 3){
            flush();
            return 0;
        }

        return paket[0];
    }

    public boolean startBootloader(){
        byte[] paket;
        byte[] cmd = new byte[9];
        cmd[0] = 42;
        cmd[1] = 2;
        cmd[2] = 12;
        cmd[3] = 34;
        cmd[4] = 02;
        cmd[5] = 00;
        cmd[6] = 18;
        cmd[7] = 12;
        cmd[8] = 34;


        try{
            sp.writeBytes(cmd);
        } catch (SerialPortException e) {
            return false;
        }

        paket = getPaketData(6);
        if(paket == null || paket.length != 6){
            flush();
            return false;
        }

        return true;
    }

    public void stopBootloader(){
        byte[] cmd = new byte[1];
        cmd[0] = COMMAND_POR_RESET;

        try{
            sp.writeBytes(cmd);
        } catch (SerialPortException e) {
            return;
        }
        return;

    }

    public boolean readDeviceID(){
        byte[] cmd = new byte[1];
        cmd[0] = COMMAND_READ_ID;
        byte[] paket;

        try{
            sp.writeBytes(cmd);
        } catch (SerialPortException e) {
            return false;
        }



        paket = getPaketData(8);
        if(paket == null || paket.length != 8){
            flush();
            return false;
        }

        if(paket[0] != (byte)0xCA ||paket[1] != (byte)0x46){
            return false;
        }

        return true;
    }

    public boolean readHexfile(String path){
        BufferedReader br = null;
        String line;
        int lineCount = 0;

        extAddr = 0;

        for(int i = 0; i < PM_SIZE; i++){
            ppMemory[i] = new MemRow(ETYPE_PROGRAM, 0, i, PM33F_ROW_SIZE_LARGE);
            ppMemoryVerify[i] = new MemRow(ETYPE_PROGRAM, 0, i, PM33F_ROW_SIZE_LARGE);
        }
        for(int i = 0; i < EE_SIZE; i++){
            ppMemory[i + PM_SIZE] = new MemRow(ETYPE_EEPROM, 0, i, PM33F_ROW_SIZE_LARGE);
            ppMemoryVerify[i + PM_SIZE] = new MemRow(ETYPE_EEPROM, 0, i, PM33F_ROW_SIZE_LARGE);
        }
        for(int i = 0; i < CM_SIZE; i++){
            ppMemory[i + PM_SIZE + EE_SIZE] = new MemRow(ETYPE_CONFIGURATION, 0, i, PM33F_ROW_SIZE_LARGE);
            ppMemoryVerify[i + PM_SIZE + EE_SIZE] = new MemRow(ETYPE_CONFIGURATION, 0, i, PM33F_ROW_SIZE_LARGE);
        }

        for(int i = 0; i < PM_SIZE; i++){
            for(int j = 0; j < (3*ppMemory[i].getRowSize()); j++){
                ppMemory[i].setRowByte(j, 0xFF);
                ppMemoryVerify[i].setRowByte(j, 0xFF);
            }
        }

        try {

            File file = new File(path);
            br = new BufferedReader(new FileReader(file));

            line = br.readLine();
            while(line != null){

                if(parseLine(line) == false){
                    br.close();
                    return false;
                }
                line = br.readLine();
            }

            br.close();
        }catch (IOException e){
            return false;
        }


        return true;
    }

    private boolean parseLine(String line){
        int byteCount;
        int address;
        int recordType;
        int base = 1;
        byte buf[] = line.getBytes();
        int iBuf[] = new int[buf.length];

        for(int i = 0; i < buf.length; i++){
            iBuf[i] = buf[i];
        }

        byteCount = BootloaderConverter.Hex2ToByte(buf, base);
        base += 2;
        address = BootloaderConverter.Hex4ToByte(buf, base);
        base += 4;
        recordType = BootloaderConverter.Hex2ToByte(buf, base);
        base += 2;

        if(recordType == 0){
            address = (address + extAddr) / 2;
            if(checkAddressClash(address)){
                return false;
            }
            for(int charCount = 0; charCount < (byteCount * 2); charCount += 4, address ++){
                boolean bInserted = false;
                for(int row = 0; row < (PM_SIZE + EE_SIZE + CM_SIZE); row++){
                    if(checkAddressClash2(address, buf, base + charCount) || checkAddressClash3(address, buf, base + charCount)){
                        return false;
                    }

                    bInserted = ppMemory[row].insertData(address, iBuf, base + charCount);
                    if(bInserted){
                        ppMemoryVerify[row].insertData(address, iBuf, base + charCount);
                        break;
                    }
                }

                if(bInserted != true){
                    return false;
                }
            }
        }else if(recordType == 1){
            return true;
        }else if(recordType == 4){
            extAddr = BootloaderConverter.Hex4ToByte(buf, base) << 16;
        }else{
            return false;
        }

        return true;
    }

    private boolean checkAddressClash(int address){
        if(address == 0x400){
            return true;
        }
        return false;
    }

    private boolean checkAddressClash2(int address, byte[] buf, int base){
        if(address >= 0x200 && address < 0xC00){
            if(BootloaderConverter.Hex4ToByte(buf, base) != 0xFFFF) {
                return true;
            }
        }
        return false;
    }

    private boolean checkAddressClash3(int address, byte[] buf, int base){
        if(address >= 0x15400 && address < 0x157f8){
            if(BootloaderConverter.Hex4ToByte(buf, base) != 0xFFFF) {
                return true;
            }
        }
        return false;
    }

    public void formatData(){
        for(int row = 0; row < (PM_SIZE + EE_SIZE + CM_SIZE); row++){
            ppMemory[row].formatData();
            ppMemoryVerify[row].formatData();
        }
    }

    private boolean skipRow(MemRow memRow){
        int i = memRow.getRowAddress();

        if(i < PROGRAM_START){
            return true;
        }

        if(i >= 0x15400){
            return true;
        }
        return false;
    }

    public boolean program(){
        for(int row = 0; row < PM_SIZE; row++){
            if(ppMemory[row].getRowtype() != ETYPE_PROGRAM){
                continue;
            }
            if(skipRow(ppMemory[row])){
                continue;
            }
            if(reporter!= null){
                if(row > 0) {
                    reporter.publish("<rm>Writing 0x" + Integer.toHexString(ppMemory[row].getRowAddress()) + "<br>");
                }else{
                    reporter.publish("Writing 0x" + Integer.toHexString(ppMemory[row].getRowAddress()) + "<br>");
                }
            }
            writeMemoryRow(ppMemory[row]);
        }
        return true;
    }

    public boolean verify(){
        int verifyAddress = 0;

        for(int row = 0; row < PM_SIZE; row++){
            if(skipRow(ppMemory[row])){
                continue;
            }
            if(readMemoryRow(ppMemory[row]) == false){
                return false;
            }

            verifyAddress = ppMemory[row].getRowAddress();

            if(reporter!= null){
                if(row > 0) {
                    reporter.publish("<rm>Verifying 0x" + Integer.toHexString(verifyAddress) + "<br>");
                }else{
                    reporter.publish("Verifying 0x" + Integer.toHexString(verifyAddress) + "<br>");
                }
            }

            for(int index = 0; index < ppMemory[row].getRowSize(); index++) {
                int instrExpected = ppMemoryVerify[row].getRowByte(index * 3) & 0xFF;
                instrExpected += (ppMemoryVerify[row].getRowByte(index * 3+1) & 0xFF) <<8;
                instrExpected += (ppMemoryVerify[row].getRowByte(index * 3+2) & 0xFF) <<16;
                int instrGot = ppMemory[row].getRowByte(index * 3+2) & 0xFF;
                instrGot += (ppMemory[row].getRowByte(index * 3+1) & 0xFF) <<8;
                instrGot += (ppMemory[row].getRowByte(index * 3+0) & 0xFF) <<16;

                if(instrExpected != instrGot){
                    return false;
                }
                verifyAddress += 2;
            }

        }

        return true;
    }

    private boolean readMemoryRow(MemRow memRow){


        byte[] paket;
        byte[] cmd = new byte[4];
        cmd[0] = COMMAND_READ_PM;
        cmd[1] = (byte)(memRow.getRowAddress() & 0xFF);
        cmd[2] = (byte)((memRow.getRowAddress() >> 8) & 0xFF);
        cmd[3] = (byte)((memRow.getRowAddress() >> 16) & 0xFF);

        try{
            sp.writeBytes(cmd);
        } catch (SerialPortException e) {
            return false;
        }
        paket = getPaketData(memRow.getRowSize() * 3);
        if(paket == null || paket.length != (memRow.getRowSize() * 3)){
            flush();
            return false;
        }

        for(int i = 0; i < paket.length; i++) {
            memRow.setRowByte(i, paket[i]);
        }

        return true;
    }

    private boolean writeMemoryRow(MemRow memRow){
        byte[] paket;
        byte[] cmd = new byte[4];
        cmd[0] = COMMAND_WRITE_PM;
        cmd[1] = (byte)(memRow.getRowAddress() & 0xFF);
        cmd[2] = (byte)((memRow.getRowAddress() >> 8) & 0xFF);
        cmd[3] = (byte)((memRow.getRowAddress() >> 16) & 0xFF);

        try{
            sp.writeBytes(cmd);
        } catch (SerialPortException e) {
            return false;
        }

        cmd = memRow.getBytes();

        try{
            sp.writeBytes(cmd);
        } catch (SerialPortException e) {
            return false;
        }

        paket = getPaketData(1);

        if(paket.length == 1 && paket[0] == COMMAND_ACK) {
            return true;
        }
        return false;
    }

    public int getModel(){

        byte[] cmd = new byte[1];
        cmd[0] = COMMAND_READ_MODEL;
        byte[] paket;

        try{
            sp.writeBytes(cmd);
        } catch (SerialPortException e) {
            return 0;
        }
        paket = getPaketData(2);
        if(paket == null || paket.length != 2){
            flush();
            return 0;
        }

        return ((paket[0] & 0xFF) << 8) + (paket[1] &0xFF);
    }

    public boolean setModel(int model){

        byte[] cmd = new byte[3];
        cmd[0] = COMMAND_WRITE_MODEL;
        cmd[1] = (byte)(model >> 8);
        cmd[2] = (byte)model;
        byte[] paket;

        try{
            sp.writeBytes(cmd);
        } catch (SerialPortException e) {
            return false;
        }
        paket = getPaketData(1);

        if(paket == null || paket.length != 1){
            flush();
            return false;
        }

        if(paket[0] == 1){
            return true;
        }

        return false;
    }


}
