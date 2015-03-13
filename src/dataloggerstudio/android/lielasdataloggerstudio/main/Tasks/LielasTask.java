package org.lielas.lielasdataloggerstudio.main.Tasks;

import android.os.AsyncTask;

/**
 * Created by Andi on 05.03.2015.
 */
public abstract class LielasTask<T, U, V>  extends AsyncTask<T, U, V>{

    protected UpdateManager updateManager;

    public LielasTask(UpdateManager updateManager){
        this.updateManager = updateManager;
    }

}
