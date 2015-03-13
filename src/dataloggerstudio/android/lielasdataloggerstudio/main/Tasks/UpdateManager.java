package org.lielas.lielasdataloggerstudio.main.Tasks;

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

    public void addFragment(LielasFragment f){
        fragments.add(f);
    }
}
