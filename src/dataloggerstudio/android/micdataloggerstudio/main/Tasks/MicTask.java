package org.lielas.micdataloggerstudio.main.Tasks;

import android.os.AsyncTask;

import org.lielas.micdataloggerstudio.main.UpdateManager;

/**
 * Created by Andi on 11.04.2015.
 */
public abstract class MicTask<T, U, V> extends AsyncTask<T, U, V>{

    protected UpdateManager updateManager;

    public MicTask(UpdateManager updateManager){
        this.updateManager = updateManager;
    }

}
