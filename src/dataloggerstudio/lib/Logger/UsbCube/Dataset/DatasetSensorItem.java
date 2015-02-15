package org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset;

/**
 * Created by Andi on 12.02.2015.
 */
public abstract class DatasetSensorItem extends DatasetItem{

    public abstract long getValue();

    public abstract int getDecimalPoints();

}
