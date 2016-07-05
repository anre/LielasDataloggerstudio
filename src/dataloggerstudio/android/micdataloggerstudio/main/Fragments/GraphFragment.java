package org.lielas.micdataloggerstudio.main.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.androidplot.Plot;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

import org.lielas.dataloggerstudio.lib.Dataset;
import org.lielas.dataloggerstudio.lib.Logger.MeasurementType.MeasurementTypeHumidity;
import org.lielas.dataloggerstudio.lib.Logger.MeasurementType.MeasurementTypeTemperature;
import org.lielas.dataloggerstudio.lib.Logger.Units.UnitClass;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.micdataloggerstudio.main.LoggerRecordManager;
import org.lielas.micdataloggerstudio.R;
import org.lielas.micdataloggerstudio.main.Graph.LoggerRecordSeries;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Created by Andi on 06.11.2015.
 */
public class GraphFragment extends MicFragment  {

    private XYPlot plot;
    private CheckBox[] chkBoxes;


    public static final GraphFragment newInstance(){
        return new GraphFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.graph_fragment, container, false);

        if(context != null){
            chkBoxes = new CheckBox[3];
            chkBoxes[0] = (CheckBox)v.findViewById(R.id.chkGraph1);
            chkBoxes[1] = (CheckBox)v.findViewById(R.id.chkGraph2);
            chkBoxes[2] = (CheckBox)v.findViewById(R.id.chkGraph3);

            chkBoxes[0].setVisibility(View.INVISIBLE);
            chkBoxes[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoggerRecordManager.getInstance().getActiveLoggerRecord().requestReprocessing();
                    update();
                }
            });
            chkBoxes[1].setVisibility(View.INVISIBLE);
            chkBoxes[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoggerRecordManager.getInstance().getActiveLoggerRecord().requestReprocessing();
                    update();
                }
            });
            chkBoxes[2].setVisibility(View.INVISIBLE);
            chkBoxes[2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoggerRecordManager.getInstance().getActiveLoggerRecord().requestReprocessing();
                    update();
                }
            });


            plot = (XYPlot)v.findViewById(R.id.graph_plot);
            LoggerRecord lr = LoggerRecordManager.getInstance().getActiveLoggerRecord();
            if(lr != null) {
                lr.requestReprocessing();
            }

            drawGraph();

           /* LoggerRecord r = new LoggerRecord(logger);
            LoggerRecordManager.getInstance().setActiveLoggerRecord(r);

            Dataset ds = new Dataset(2);
            ds.setDateTime(1447151296000L);
            ds.setValue(231, 1, 0, new MeasurementTypeTemperature());
            ds.setValue(531, 1, 1, new MeasurementTypeHumidity());
            r.add(ds);

            ds = new Dataset(2);
            ds.setDateTime(1447151297000L);
            ds.setValue(232, 1, 0, new MeasurementTypeTemperature());
            ds.setValue(531, 1, 1, new MeasurementTypeHumidity());
            r.add(ds);

            ds = new Dataset(2);
            ds.setDateTime(1447151298000L);
            ds.setValue(234, 1, 0, new MeasurementTypeTemperature());
            ds.setValue(431, 1, 1, new MeasurementTypeHumidity());
            r.add(ds);

            ds = new Dataset(2);
            ds.setDateTime(1447151299000L);
            ds.setValue(212, 1, 0, new MeasurementTypeTemperature());
            ds.setValue(531, 1, 1, new MeasurementTypeHumidity());
            r.add(ds);

            ds = new Dataset(2);
            ds.setDateTime(14471513008000L);
            ds.setValue(235, 1, 0, new MeasurementTypeTemperature());
            ds.setValue(581, 1, 1, new MeasurementTypeHumidity());
            r.add(ds);

            LoggerRecordSeries ser1 = new LoggerRecordSeries(r, "bla", 0);
            LoggerRecordSeries ser2 = new LoggerRecordSeries(r, "blo", 1);
            LineAndPointFormatter serieFormatter1 = new LineAndPointFormatter(Color.rgb(0, 100, 0), Color.TRANSPARENT, Color.TRANSPARENT, new PointLabelFormatter(Color.TRANSPARENT, 0, 0));
            LineAndPointFormatter serieFormatter2 = new LineAndPointFormatter(Color.BLUE, Color.TRANSPARENT, Color.TRANSPARENT, new PointLabelFormatter(Color.TRANSPARENT, 0, 0));
            plot.addSeries(ser1, serieFormatter1);
            plot.addSeries(ser2, serieFormatter2);
            plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);

            double min = r.getMin(0) * 0.98;
            double max = r.getMax(1) * 1.02;

            plot.setRangeBoundaries(min, max, BoundaryMode.FIXED);
            plot.getLayoutManager().remove(plot.getLegendWidget());
            plot.redraw();*/
        }

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        update();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    private void drawGraph(){
        MicUSBStick stick = (MicUSBStick)logger;

        if(plot == null){
            return;
        }

        plot.getGraphWidget().getGridBackgroundPaint().setAlpha(0);
        plot.getGraphWidget().getBackgroundPaint().setAlpha(0);
        plot.getBackgroundPaint().setAlpha(0);

        plot.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);
        plot.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK);

        plot.setDomainValueFormat(new DecimalFormat("0"));

        plot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().getRangeGridLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().getRangeSubGridLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().getDomainGridLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().getDomainSubGridLinePaint().setColor(Color.BLACK);
        plot.setBorderStyle(Plot.BorderStyle.NONE, null, null);

        LoggerRecord lr = LoggerRecordManager.getInstance().getActiveLoggerRecord();

        if(logger.getCommunicationInterface().isOpen() && lr != null  && lr.getCount() > 0) {

            lr.getUnitClass().setUnitClass(stick.getUnitClass().getUnitClass());
            if (lr != null && lr.newDataAdded()) {
                plot.clear();

                Double min = null;
                Double max = null;

                for (int i = 0; i < ((MicUSBStick) logger).getModel().getChannelCount(); i++) {

                    if (!chkBoxes[i].isChecked()) {
                        continue;
                    }

                    LoggerRecordSeries ser = new LoggerRecordSeries(lr, "bla", i);
                    int color = Color.rgb(0, 100, 0);
                    switch (i) {
                        case 1:
                            color = Color.BLUE;
                            break;
                        case 2:
                            color = Color.RED;
                            break;
                    }
                    LineAndPointFormatter serieFormatter = new LineAndPointFormatter(color, Color.TRANSPARENT, Color.TRANSPARENT, new PointLabelFormatter(Color.TRANSPARENT, 0, 0));
                    plot.addSeries(ser, serieFormatter);
                    if (min == null) {
                        min = lr.getMin(i);
                    } else {
                        if (lr.getMin(i) < min) {
                            min = lr.getMin(i);
                        }
                    }
                    if (max == null) {
                        max = lr.getMax(i);
                    } else {
                        if (lr.getMax(i) > max) {
                            max = lr.getMax(i);
                        }
                    }
                }

                if (min == null) {
                    min = 0.0;
                } else {
                    min *= 0.98;
                }
                if (max == null) {
                    max = 100.0;
                } else {
                    max *= 1.02;
                }

                if(min == max){
                    min = 0.0;
                    max = 100.0;
                }

                plot.setDomainStep(XYStepMode.SUBDIVIDE, 5);
                plot.setRangeBoundaries(min, max, BoundaryMode.FIXED);
                plot.setDomainBoundaries(1, 2, BoundaryMode.AUTO);
                plot.getLayoutManager().remove(plot.getLegendWidget());
                plot.redraw();
                lr.setDataProcessed();
            }
        }else{

            plot.setRangeBoundaries(0.0, 100.0, BoundaryMode.FIXED);
            plot.setRangeStep(XYStepMode.SUBDIVIDE, 10);
            plot.setDomainBoundaries(1, 5, BoundaryMode.FIXED);
            plot.setDomainStep(XYStepMode.SUBDIVIDE, 5);
        }
    }

    @Override
    public void update() {

        if(logger.getCommunicationInterface().isOpen()){

            if(chkBoxes == null){
                return;
            }

            MicUSBStick stick = (MicUSBStick)logger;

            switch(stick.getModel().getChannelCount()){
                default:
                case 1:
                    chkBoxes[0].setVisibility(View.VISIBLE);
                    chkBoxes[1].setVisibility(View.INVISIBLE);
                    chkBoxes[2].setVisibility(View.INVISIBLE);
                    chkBoxes[0].setText(stick.getModel().getChannel(0).getUnit(stick.getUnitClass()));
                    break;
                case 2:
                    chkBoxes[0].setVisibility(View.VISIBLE);
                    chkBoxes[1].setVisibility(View.VISIBLE);
                    chkBoxes[2].setVisibility(View.INVISIBLE);
                    chkBoxes[0].setText(stick.getModel().getChannel(0).getUnit(stick.getUnitClass()));
                    chkBoxes[1].setText(stick.getModel().getChannel(1).getUnit(stick.getUnitClass()));
                    break;
                case 3:
                    chkBoxes[0].setVisibility(View.VISIBLE);
                    chkBoxes[1].setVisibility(View.VISIBLE);
                    chkBoxes[2].setVisibility(View.VISIBLE);
                    chkBoxes[0].setText(stick.getModel().getChannel(0).getUnit(stick.getUnitClass()));
                    chkBoxes[1].setText(stick.getModel().getChannel(1).getUnit(stick.getUnitClass()));
                    chkBoxes[2].setText(stick.getModel().getChannel(2).getUnit(stick.getUnitClass()));
                    break;
            }
            drawGraph();
        }
    }
}
