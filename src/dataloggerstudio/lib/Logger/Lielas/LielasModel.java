package org.lielas.dataloggerstudio.lib.Logger.Lielas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andi on 09.06.2015.
 */
public class LielasModel {

    public static final int MSHT      = 1001;
    public static final int M1PT1K    = 1002;
    public static final int M2PT1K    = 1003;
    public static final int M1U       = 1004;
    public static final int M2U       = 1005;
    public static final int M1I       = 1006;
    public static final int M2I       = 1007;
    public static final int MSHTAP    = 1008;

    private int model;

    public LielasModel(int modelNumber){
        this.model = modelNumber;
    }

    public static ArrayList<LielasModel> getList(){
        ArrayList<LielasModel> list = new ArrayList<LielasModel>();

        list.add(new LielasModel(MSHT));
        list.add(new LielasModel(M1PT1K));
        list.add(new LielasModel(M2PT1K));
        list.add(new LielasModel(M1U));
        list.add(new LielasModel(M1I));
        list.add(new LielasModel(MSHTAP));

        return list;
    }


    public int get(){
        return model;
    }

    @Override
    public String toString() {
        switch (model){
            case MSHT:
                return "SHT";
            case M1PT1K:
                return "1 PT1000";
            case M2PT1K:
                return "2 PT1000";
            case M1U:
                return "Spannung";
            case M1I:
                return "Strom";
            case MSHTAP:
                return "SHT + Luftdruck";
        }
        return null;
    }
}
