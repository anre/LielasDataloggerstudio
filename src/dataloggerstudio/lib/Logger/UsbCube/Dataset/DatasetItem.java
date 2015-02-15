package org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset;

/**
 * Created by Andi on 12.02.2015.
 */
public abstract class DatasetItem {

    public abstract int getItemId();

    public abstract boolean  parse(byte[] payload, int start);

    public abstract int getSize();
}
