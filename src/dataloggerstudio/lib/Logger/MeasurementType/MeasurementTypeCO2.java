package org.lielas.dataloggerstudio.lib.Logger.MeasurementType;

/**
 * Created by Andi on 30.10.2015.
 */
public class MeasurementTypeCO2 extends MeasurementType {
    @Override
    public double toMetric(double value) {
        return value;
    }

    @Override
    public double toImperial(double value) {
        return value;
    }
}
