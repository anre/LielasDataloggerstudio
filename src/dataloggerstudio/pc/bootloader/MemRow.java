package org.lielas.dataloggerstudio.pc.bootloader;

/**
 * Created by Andi on 09.06.2015.
 */
public class MemRow {

    public int[] data;

    private int[] buffer;
    private int address;
    private boolean empty;
    private int etype;
    private int rownumber;
    private int rowsize;



    public MemRow(int etype, int startAddr, int rownumber, int rowsize){
        int size = 0;

        this.rownumber = rownumber;
        this.etype = etype;
        this.empty = true;
        data = new int[rowsize*2];

        this.rowsize = rowsize;

        if(etype == Bootloader.ETYPE_PROGRAM){
            size = rowsize * 3;
            address = startAddr + rownumber * rowsize * 2;
        }else if(etype == Bootloader.ETYPE_EEPROM){
            size = rowsize * 2;
            address = startAddr + rownumber * rowsize * 2;
        }else if(etype == Bootloader.ETYPE_CONFIGURATION){
            size = 3;
            address = startAddr + rownumber * 2;
        }

        buffer = new int[size];
        for(int i = 0; i < (rowsize * 2); i++){
            data[i] = 0xFFFF;
        }

    }

    public int getRowtype(){
        return etype;
    }

    public int getRowByte(int index){
        return buffer[index];
    }

    public void setRowByte(int index, int i){
        buffer[index] = i;
    }

    public int getRowSize(){
        return rowsize;
    }

    public int getRowAddress(){
        return address;
    }

    public boolean getRowEmpty(){
        return empty;
    }

    public int getRownumber(){
        return rownumber;
    }

    public boolean insertData(int address, int[] data, int base){

        if(address < this.address){
            return false;
        }

        if((etype == Bootloader.ETYPE_PROGRAM) && (address >= (this.address + rowsize * 2))){
            return false;
        }
        if((etype == Bootloader.ETYPE_EEPROM) && (address >= (this.address + rowsize * 2))){
            return false;
        }
        if((etype == Bootloader.ETYPE_CONFIGURATION) && (address >= (this.address + 2))){
            return false;
        }

        empty = false;
        int index = address - this.address;

        if(index >=this.data.length){
            return false;
        }

        this.data[index] = BootloaderConverter.Hex4ToByte(data, base);

        return true;
    }

    public void formatData(){
        if(empty){
            return;
        }

        if(etype == Bootloader.ETYPE_PROGRAM){
            for(int count = 0; count < rowsize; count++){
                buffer[count * 3] = (data[count * 2] >> 8) & 0xFF;
                buffer[count * 3 + 1] = (data[count * 2] ) & 0xFF;
                buffer[count * 3 + 2] = (data[count * 2 + 1] >> 8) & 0xFF;
            }
        }else if(etype == Bootloader.ETYPE_CONFIGURATION){
            buffer[0] = (data[0] >> 8) & 0xFF;
            buffer[1] = (data[0] ) & 0xFF;
            buffer[2] = (data[1] >> 8) & 0xFF;
        }else{
            for(int count = 0; count < rowsize; count++){
                buffer[count * 3] = (data[count * 2] >> 8) & 0xFF;
                buffer[count * 3 + 1] = (data[count * 2] ) & 0xFF;
            }
        }
    }

    public byte[] getBytes(){
        byte[] row = new byte[buffer.length];

        for(int i = 0; i < buffer.length; i++){
            row[i] = (byte)buffer[i];
        }

        return row;
    }

}
