package org.lielas.micdataloggerstudio.main.CommunicationInterface;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import org.lielas.dataloggerstudio.lib.CommunicationInterface.SerialInterface;
import org.lielas.lielasdataloggerstudio.usbserial.driver.UsbSerialDriver;
import org.lielas.lielasdataloggerstudio.usbserial.driver.UsbSerialPort;
import org.lielas.lielasdataloggerstudio.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.util.List;

public class AndroidSerialInterface extends SerialInterface {
    protected Context context;
    protected int timeout;
    protected UsbSerialPort serialport;

    protected int baudrate;
    protected int flowcontrol;
    protected int parity;
    protected int stopbits;
    protected int databits;
    protected boolean isOpen;

    byte[] readBuffer;
    int readBufPosition;
    int readBufLen;

    public AndroidSerialInterface() {
        super();
        timeout = 100;

        baudrate = 38400;
        parity = UsbSerialPort.PARITY_NONE;
        stopbits = UsbSerialPort.STOPBITS_1;
        databits = UsbSerialPort.DATABITS_8;

        readBufPosition = 0;
        readBufLen = 0;
        readBuffer = null;
        context = null;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public boolean open(String port) {
        return openUsb();
    }

    public boolean openUsb() {
        if (context == null) {
            return false;
        }

        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);

        if (availableDrivers.isEmpty()) {
            return false;
        }

        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());

        if (connection == null) {
            return false;
        }

        List<UsbSerialPort> ports = driver.getPorts();
        serialport = ports.get(0);

        try {
            serialport.open(connection);
            serialport.setParameters(baudrate, databits, stopbits, parity);
            serialport.setDTR(false);
            serialport.setRTS(false);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isOpen = true;
        return true;
    }

    @Override
    public void close() {
        if (isOpen && serialport != null) {
            isOpen = false;

            try {
                serialport.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setTimeout(int ms) {
        timeout = ms;
    }

    public int getTimeout() {
        return timeout;
    }

    public boolean write(final byte[] src) {
        try {
            serialport.write(src, timeout);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public int read(final byte[] dest) {
        int len = 0;
        try {
            len = serialport.read(dest, timeout);
            readBuffer = dest;
        } catch (IOException e) {
        }
        return len;
    }

    public int read() {
        int len = 0;
        try {
            readBuffer = new byte[500];
            readBufPosition = 0;

            readBufLen = serialport.read(readBuffer, timeout);
            /*for(byte b : readBuffer){
                System.out.print(b);
                System.out.print(":");
            }*/
            System.out.println();
        } catch (IOException e) {
        }
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

    public void flush() {
        try {
            serialport.purgeHwBuffers(true, true);
        } catch (IOException e) {
        }
    }
}
