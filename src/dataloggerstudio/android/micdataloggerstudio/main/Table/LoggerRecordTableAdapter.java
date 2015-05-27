package org.lielas.micdataloggerstudio.main.Table;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.lielas.dataloggerstudio.lib.LoggerRecord;

/**
 * Created by Andi on 01.05.2015.
 */
public class LoggerRecordTableAdapter extends BaseAdapter {
    LoggerRecord lr;
    Context context;


    public LoggerRecordTableAdapter(Context context, LoggerRecord lr){
        super();
        this.lr = lr;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(lr == null) {
            return 0;
        }
        return lr.getCount();
    }

    @Override
    public Object getItem(int position) {
        if(lr == null) {
            return null;
        }
        return lr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LoggerRecordTableRow tr = null;

        if(lr == null){
            return null;
        }

        if(convertView == null){
            tr = new LoggerRecordTableRow(context, lr.get(position));
        }else{
            tr = (LoggerRecordTableRow)convertView;
            tr.update(lr.get(position));
        }
        return tr;
    }
}
