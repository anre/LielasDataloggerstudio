package org.lielas.micdataloggerstudio.main;

import org.lielas.micdataloggerstudio.main.Fragments.DataFragment;
import org.lielas.micdataloggerstudio.main.Fragments.MicFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andi on 08.04.2015.
 */
public class UpdateManager {

    private List<MicFragment> fragments;

    public UpdateManager(){
        fragments = new ArrayList<MicFragment>();
    }

    public void update(){
        for(MicFragment f : fragments){
            f.update();
        }
    }

    public void update(String text){
        for(MicFragment f : fragments){
            f.update(text);
            text = null;
        }
    }

    public void updateProgress(Integer progress){
        for(MicFragment f : fragments){
            if(f instanceof DataFragment){
                ((DataFragment)f).updateProgress(progress);
            }
        }
    }

    public void addFragment(MicFragment f){
        fragments.add(f);
    }
}
