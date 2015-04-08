package org.lielas.micdataloggerstudio.main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lielas.micdataloggerstudio.R;

/**
 * Created by Andi on 08.04.2015.
 */
public class DataFragment extends MicFragment{

    public static final DataFragment newInstance(){
        return new DataFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.data_fragment, container, false);
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

    @Override
    public void update() {

    }
}
