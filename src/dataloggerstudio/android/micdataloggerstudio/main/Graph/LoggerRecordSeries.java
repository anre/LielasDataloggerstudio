package org.lielas.micdataloggerstudio.main.Graph;

import com.androidplot.xy.XYSeries;

import org.lielas.dataloggerstudio.lib.LoggerRecord;

/**
 * Created by Andi on 06.11.2015.
 */
public class LoggerRecordSeries implements XYSeries {

    String title;
    LoggerRecord lr;
    int channel;

    public LoggerRecordSeries(LoggerRecord lr, String title, int channel){
        this.title = title;
        this.lr = lr;
        this.channel = channel;
    }

    @Override
    public int size() {
        if(lr == null){
            return 0;
        }
        return lr.getCount();
    }

    @Override
    public Number getX(int index) {
        return (index + 1);
    }

    @Override
    public Number getY(int index) {
        if(lr == null){
            return 0;
        }
        return lr.get(index).getValue(channel);
    }

    @Override
    public String getTitle() {
        return null;
    }
}
