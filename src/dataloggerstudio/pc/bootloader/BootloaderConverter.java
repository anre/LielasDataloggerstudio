package org.lielas.dataloggerstudio.pc.bootloader;

/**
 * Created by Andi on 08.06.2015.
 */
public class BootloaderConverter {

    public static int getAsciiDigit(int val){
        if(val < 10){
            return ('0' + val);
        }
        return ('A' + (val - 10));
    }

    public static int asciiDigit2Byte(byte b){
        int i = (int)b & 0x7F;
        if(i > 'Z'){
            i = i - 0x20;
        }
        if(i > '9'){
            i = (i - 'A') + 0xA;
        }else{
            i = i - '0';
        }
        return i;
    }

    public static int asciiDigit2Byte(int b){
        int i = (int)b & 0x7F;
        if(i > 'Z'){
            i = i - 0x20;
        }
        if(i > '9'){
            i = (i - 'A') + 0xA;
        }else{
            i = i - '0';
        }
        return i;
    }

    public static int Hex2ToByte(byte[] b, int base){
        int val;

        val = asciiDigit2Byte(b[base]);
        val = (val << 4) + asciiDigit2Byte(b[base+1]);

        return val;
    }

    public static int Hex2ToByte(int[] b, int base){
        int val;

        val = asciiDigit2Byte(b[base]);
        val = (val << 4) + asciiDigit2Byte(b[base+1]);

        return val;
    }

    public static int Hex4ToByte(byte[] b, int base){
        int val;

        val = Hex2ToByte(b, base);
        val = (val << 8) + Hex2ToByte(b, base + 2);

        return val;
    }

    public static int Hex4ToByte(int[] b, int base){
        int val;

        val = Hex2ToByte(b, base);
        val = (val << 8) + Hex2ToByte(b, base + 2);

        return val;
    }

    public static int Hex8ToByte(byte[] b, int base){
        int val;

        val = Hex4ToByte(b, base);
        val = (val << 16) + Hex4ToByte(b, base + 4);

        return val;
    }
}
