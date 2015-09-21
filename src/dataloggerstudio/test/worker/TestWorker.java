package org.lielas.dataloggerstudio.test.worker;

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.DatasetItemIds;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.dataloggerstudio.pc.CommunicationInterface.UsbCube.UsbCubeSerialInterface;
import org.lielas.dataloggerstudio.test.gui.MainFrame;

import javax.swing.*;

/**
 * Created by Andi on 11.06.2015.
 */
public class TestWorker  extends SwingWorker<Object, String> {

    UsbCube logger;
    MainFrame mainFrame;
    String port;

    public TestWorker(UsbCube logger, MainFrame mainFrame, String port){
        super();
        this.logger = logger;
        this.mainFrame = mainFrame;
        this.port = port;
    }

    @Override
    public UsbCube doInBackground(){

        if(logger != null && logger.getCommunicationInterface() != null && logger.getCommunicationInterface().isOpen()){
            logger.getCommunicationInterface().close();
        }

        logger = new UsbCube();
        logger.setCommunicationInterface(new UsbCubeSerialInterface());

        UsbCubeSerialInterface com = (UsbCubeSerialInterface)logger.getCommunicationInterface();

        if(!com.connect(port)){
            publish("Error connecting to com port <br>");
            System.out.println("not connected");
            return null;
        }

        //stop realtime logging
        for(int i = 0; i < 3; i++){
            if(com.stopRealTimeLogging()){
                break;
            }
            if(i == 2){
                com.close();
                return null;
            }
        }

        //get version
        if(!com.getVersion((UsbCube)logger)){
            publish("Error reading logger version <br>");
            com.close();
            return null;
        }


        //get model
        if(!com.getDatasetStructure((UsbCube)logger)){
            publish("Error reading logger dataset structure <br>");
            com.close();
            return null;
        }

        //get id
        if(!com.getId((UsbCube)logger)){
            publish("Error reading logger id <br>");
            com.close();
            return null;
        }

        //report id and model
        publish("<font color='green' size='5'><b>Verbunden mit " + logger.getId().toString() + "</b></font><br><br>");
        publish("Firmware Version " + logger.getVersion().toString() + "<br>");
        publish(Integer.toString(logger.getDatasetStructure().getSensorCount()) + " Sensor(en) gefunden:");
        publish("<ul>");
        for(int i = 0; i < logger.getDatasetStructure().getSensorCount(); i++){
            int id = logger.getDatasetStructure().getSensorItem(i).getItemId();
            switch (id){
                case DatasetItemIds.SHT_T:
                    publish("<li> SHT Temperatur</li>");
                    break;
                case DatasetItemIds.SHT_H:
                    publish("<li> SHT Relative Luftfeuchtigkeit</li>");
                    break;
                case DatasetItemIds.MS5607:
                    publish("<li> MS5607 Luftdruck</li>");
                    break;
                case DatasetItemIds.EDLSI1:
                case DatasetItemIds.EDLSI2:
                case DatasetItemIds.EDLSI3:
                case DatasetItemIds.EDLSI4:
                    publish("<li> EDLSI Strom 0-20 mA</li>");
                    break;
                case DatasetItemIds.EDLSU1:
                case DatasetItemIds.EDLSU2:
                case DatasetItemIds.EDLSU3:
                case DatasetItemIds.EDLSU4:
                    publish("<li> EDLSU Spannung 0-10V</li>");
                    break;
                case DatasetItemIds.PT1K1:
                case DatasetItemIds.PT1K2:
                case DatasetItemIds.PT1K3:
                case DatasetItemIds.PT1K4:
                    publish("<li> PT1000</li>");
                    break;
            }
        }
        publish("</ul>");


        publish("Logger name abfragen ... ");

        //get logger name
        if(!com.getName((UsbCube) logger)){
            publish("Error reading logger name <br>");
            com.close();
            return null;
        }
        publish(logger.getName() + "<br>");

        publish("Logger status abfragen ... ");

        //get logger status
        if(!com.getLoggerStatus((UsbCube)logger)){
            publish("Error reading logger status <br>");
            com.close();
            return null;
        }

        if(logger.isLogging()){
            publish("Logger läuft, versuche logger zu stoppen ... ");

            if(!com.setLoggerStatus(logger, false)){
                publish("Error stopping logger<br>");
                com.close();
                return null;
            }

            if(!com.getLoggerStatus((UsbCube) logger)){
                publish("Error reading logger status <br>");
                com.close();
                return null;
            }

            if(logger.isLogging()){
                publish("Error stopping logger<br>");
            }
        }
        publish("Logger gestoppt<br>");

        publish("Logger Zeit abfragen ...");

        //get datetime
        if(!com.getClock((UsbCube)logger)){
            publish("Error reading clock <br>");
            com.close();
            return null;
        }
        publish(" Zeitabfrage erfolgreich<br>");

        publish("Logger anzahl der Logfiles abfragen <br>");

        //get logfile count
        if(!com.getLogfileCount((UsbCube)logger)){
            publish("Error reading logfile entries <br>");
            com.close();
            return null;
        }

        publish("Flash speicher testen ... Anzahl der Bad Blocks auslesen ... ");

        //get bad block count
        int bbc = com.getBadBlockCount();
        if(bbc == -1){
            publish("Error reading bad block count<br>");
            com.close();
            return null;
        }else if(bbc > 0){
            publish("Error reading bad block count, too much bad blocks <br>");
            com.close();
            return null;
        }

        publish("Kein Bad Block gefunden<br> Starte Flash Test ... ");

        //test flash
        if(!com.TestFlash()){
            publish("Error while running flash slef test <br>");
            com.close();
            return null;
        }

        publish("Flash Test erfolgreich<br> Lösche speicher ... ");

        //delete flash
        if(!com.deleteLogfiles(logger)){
            publish("Error deleting flash <br>");
            com.close();
            return null;
        }

        publish("Speicher erfolgreich gelöscht <br> Setze Uhrzeit ... ");

        //set time
        logger.setDatetime(1420070400000L);
        if(!com.setClock(logger)){
            publish("Error setting logger time <br>");
            com.close();
            return null;
        }

        publish("Uhrzeit gesetzt <br> Setze Messintervall auf 2s<br>");

        //set samplerate
        logger.setSampleRate(2);
        if(!com.setSamplerate(logger)){
            publish("Error setting logger samplerate <br>");
            com.close();
            return null;
        }

        publish("Messintervall gesetzt, lese Messintervall aus<br>");

        //get samplerate
        if(!com.getSamplerate((UsbCube)logger)){
            publish("Error reading logger samplerate <br>");
            com.close();
            return null;
        }

        publish("Messintervall ist " + Long.toString(logger.getSampleRate()) + "s<br>");

        if(logger.getSampleRate() != 2){
            publish("Error reading logger samplerate, wrong samplerate <br>");
            com.close();
            return null;
        }

        publish("Starte logger<br>");

        if(!com.setLoggerStatus(logger, true)){
            publish("Error starting logger <br>");
            com.close();
            return null;
        }

        publish("Warte 10s<br>");

        try {
            Thread.sleep(10000);
        }catch(Exception e){
            publish("Error waiting 10s <br>");
            com.close();
            return null;
        }

        publish("Stoppe logger<br>");

        if(!com.setLoggerStatus(logger, false)){
            publish("Error stopping logger <br>");
            com.close();
            return null;
        }

        publish("Logger gestoppt<br>");

        publish("Logger anzahl der Logfiles abfragen <br>");

        //get logfile count
        if(!com.getLogfileCount((UsbCube)logger)){
            publish("Error reading logfile entries <br>");
            com.close();
            return null;
        }

        if(logger.getRecordCount() != 1){
            publish("Error reading logfile entries, wrong logfile count <br>");
            com.close();
            return null;
        }

        if(!com.getLogfileProperties(logger, 0)){
            publish("Error reading logfile property<br>");
            com.close();
            return null;
        }

        LoggerRecord lr = logger.getRecordset(0);
        long logfilecount = lr.getEndIndex() - lr.getStartIndex() + 1;
        publish(Long.toString(logfilecount) + " Logfiles gefunden<br><br>");

        //lese logfile



        for(int i = 0; i < logfilecount; i++){
            if(!com.getLogfileDataset(logger, lr, i)){
                publish("Error reading logfile <br>");
                com.close();
                return null;
            }

            publish(lr.getString(i, 0));

            for(int j = 0; j < logger.getDatasetStructure().getSensorCount(); j++){
                publish(" | " + lr.getString(i, j + 1));
            }

            publish("<br>");
        }

        com.close();
        return logger;
    }

    @Override
    public void done(){
        try{
            UsbCube cube = (UsbCube)get();
            if(cube != null){
                mainFrame.appendln("<font color='green' size='5'>Test erfolgreich</font>");
            }

        }catch (Exception e) {
            mainFrame.appendln("Exception<br>");
        }
        mainFrame.appendln("<br><b>Test beendet<b>");
    }

    @Override
    protected void process(java.util.List<String> msg){
        if(msg == null){
            System.out.println("abort");
            return;
        }

        for(int i = 0; i < msg.size(); i++){
            String s = msg.get(i);
            mainFrame.append(s);
        }
    }

}
