package org.lielas.lielasdataloggerstudio.main.Fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lielas.lielasdataloggerstudio.R;

/**
 * Created by Andi on 21.02.2015.
 */
public class ViewFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static ViewFragment newInstance(String msg){
        ViewFragment f = new ViewFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, msg);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSet){
        Context context = getActivity();
        View v = inflater.inflate(R.layout.view_fragment, container, false);

        return v;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
}
