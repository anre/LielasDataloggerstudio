package org.lielas.micdataloggerstudio.main.Fragments;

import android.support.v4.app.Fragment;

import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.micdataloggerstudio.main.LielasToast;
import org.lielas.micdataloggerstudio.main.UpdateManager;

/**
 * Created by Andi on 08.04.2015.
 */
public abstract class MicFragment extends Fragment{
    protected Logger logger;
    protected UpdateManager updateManager;

    public void setUpdateManager(UpdateManager updateManager){
        this.updateManager = updateManager;
    }

    public void setLogger(Logger logger){
        this.logger = logger;
    }

    public abstract void update();

    public void update(String text){
        LielasToast.show(text, getActivity());
        update();
    }


}
