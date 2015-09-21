package org.lielas.dataloggerstudio.lib.Logger.MeasurementType;

/**
 * Created by Andi on 16.09.2015.
 */
public class MeasurementTypeHumidity extends MeasurementType{
    @Override
    public double toMetric(double value) {
        return value;
    }

    @Override
    public double toImperial(double value) {
        return value;
    }
}
