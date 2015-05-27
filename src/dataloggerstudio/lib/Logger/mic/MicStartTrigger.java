package org.lielas.dataloggerstudio.lib.Logger.mic;

/**
 * Created by Andi on 30.04.2015.
 */
public class MicStartTrigger {

    public final static int START_IMMEDIATELY = 1;
    public final static int START_KEY =2;

    private int trigger;


    public MicStartTrigger(int trigger){
        this.trigger = trigger;
    }

    public int getTrigger(){
        return trigger;
    }

    public String getCmdString(){
        switch (trigger){
            case START_IMMEDIATELY:
                return "1";
            case START_KEY:
                return "3";
        }
        return "";
    }

    @Override
    public String toString(){
        switch (trigger){
            case START_IMMEDIATELY:
                return "immediately";
            case START_KEY:
                return "key";
        }
        return "";
    }
}
