package org.lielas.lielasdataloggerstudio.main.Table;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.lielas.dataloggerstudio.lib.Dataset;
import org.w3c.dom.Text;

/**
 * Created by Andi on 31.03.2015.
 */
public class LoggerRecordTableRow extends LinearLayout{

    TextView lblDate;
    TextView[] lblValue;

    public LoggerRecordTableRow(Context context, Dataset ds){
        super(context);

        double withRatio = 1. / (double)(ds.getChannels() + 1);


        this.setOrientation(HORIZONTAL);

        if(ds != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = (float)withRatio;

            lblDate = new TextView(context);
            lblDate.setText(ds.getString(0));
            lblDate.setWidth(0);
            addView(lblDate, layoutParams);

            lblValue = new TextView[ds.getChannels()];
            for (int i = 0; i < ds.getChannels(); i++) {
                lblValue[i] = new TextView(context);

                lblValue[i].setText(ds.getString(i + 1));
                lblValue[i].setGravity(Gravity.CENTER);
                lblValue[i].setWidth(0);
                addView(lblValue[i], layoutParams);
            }
        }
    }

    public void update(Dataset ds){
        setDate(ds.getString(0));
        for(int i = 0 ; i < ds.getChannels(); i++){
            setChannel(ds.getString(i+1), i);
        }
    }

    public void setDate(String dt){
        lblDate.setText(dt);
    }

    public void setChannel(String value, int index){
        if(index >= lblValue.length){
            return;
        }

        if(lblValue[index] == null){
            return;
        }

        lblValue[index].setText(value);
    }

}
