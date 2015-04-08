package org.lielas.lielasdataloggerstudio.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.lielasdataloggerstudio.main.Tasks.UpdateManager;

/**
 * Created by Andi on 03.04.2015.
 */
public class UsbBroadcastReceiver extends BroadcastReceiver{

    Logger logger;
    UpdateManager updateManager;

    public UsbBroadcastReceiver(Logger logger, UpdateManager updateManager){
        this.logger = logger;
        this.updateManager = updateManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        logger.getCommunicationInterface().close();
        updateManager.update("Logger disconnected");
    }
}
