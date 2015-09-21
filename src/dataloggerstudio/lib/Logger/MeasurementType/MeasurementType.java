package org.lielas.dataloggerstudio.lib.Logger.MeasurementType;

/**
 * Created by Andi on 16.09.2015.
 */
public abstract class MeasurementType {


    public abstract double toMetric(double value);
    public abstract double toImperial(double value);

}
