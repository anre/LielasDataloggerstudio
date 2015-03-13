package org.lielas.lielasdataloggerstudio.main.Fragments;

import android.support.v4.app.Fragment;

import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.lielasdataloggerstudio.main.Tasks.UpdateManager;

/**
 * Created by Andi on 05.03.2015.
 */
public abstract class LielasFragment extends Fragment{

    protected Logger logger;
    protected UpdateManager updateManager;

    public void setUpdateManager(UpdateManager updateManager){
        this.updateManager = updateManager;
    }

    public void setLogger(Logger logger){
        this.logger = logger;
    }

    public void update(){
        updateManager.update();
    }

}
