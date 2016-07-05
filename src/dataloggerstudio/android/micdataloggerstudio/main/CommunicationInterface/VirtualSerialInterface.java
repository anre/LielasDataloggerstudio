package org.lielas.micdataloggerstudio.main.CommunicationInterface;

import android.content.Context;

import org.lielas.dataloggerstudio.lib.CommunicationInterface.SerialInterface;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicModel;
import org.lielas.dataloggerstudio.lib.Logger.mic.Models.Model98537;
import org.lielas.dataloggerstudio.lib.Logger.mic.Models.Model98581;
import org.lielas.dataloggerstudio.lib.Logger.mic.Models.Model98583;

/**
 * Created by Andi on 09.11.2015.
 */
public class VirtualSerialInterface extends SerialInterface {
    protected Context context;

    byte[] readBuffer;
    int readBufPosition;
    int readBufLen;

    byte[] writeBuffer;

    MicModel virtualLogger;

    public VirtualSerialInterface(){
        virtualLogger = new Model98537();
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public boolean open(String port) {
        isOpen = true;
        return true;
    }

    @Override
    public void close() {
        if (isOpen) {
            isOpen = false;
        }
    }

    public boolean write(final byte[] src) {
        writeBuffer = virtualLogger.parseCommand(src);
        return true;
    }

    public int read() {
        if(writeBuffer == null){
            return 0;
        }

        readBuffer = new byte[writeBuffer.length];
        readBufPosition = 0;
        readBufLen = writeBuffer.length;
        System.arraycopy(writeBuffer, 0, readBuffer, 0, writeBuffer.length);

        return readBufLen;
    }

    public byte readByte() {
        if (readBuffer == null) {
            return 0;
        }

        if (readBufPosition < readBufLen) {
            return readBuffer[readBufPosition++];
        }
        return 0;
    }

    public int getBytesInBuffer() {
        if (readBuffer == null) {
            return 0;
        }
        return readBufLen - readBufPosition;
    }
}
