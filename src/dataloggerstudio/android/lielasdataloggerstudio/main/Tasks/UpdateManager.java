package org.lielas.lielasdataloggerstudio.main.Tasks;

import org.lielas.lielasdataloggerstudio.main.Fragments.DataFragment;
import org.lielas.lielasdataloggerstudio.main.Fragments.LielasFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andi on 06.03.2015.
 */
public class UpdateManager {

    private List<LielasFragment> fragments;

    public UpdateManager(){
        fragments = new ArrayList<LielasFragment>();
    }

    public void update(){
        for(LielasFragment f : fragments){
            f.update();
        }
    }

    public void update(String text){
        for(LielasFragment f : fragments){
            f.update(text);
            text = null;
        }
    }

    public void updateProgress(Integer progress){
        for(LielasFragment f : fragments){
            if(f instanceof DataFragment){
                ((DataFragment)f).updateProgress(progress);
            }
        }
    }

    public void addFragment(LielasFragment f){
        fragments.add(f);
    }
}
